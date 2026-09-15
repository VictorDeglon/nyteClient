package client.gui;

import client.NyteClientMod;
import client.Theme;
import client.modules.Category;
import client.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * The mod's ClickGUI: a category sidebar on the left, a spacious module
 * list on the right (name + description on their own lines, plus an
 * ON/OFF label -- not just a small color dot, since "I can't tell what I'm
 * enabling" was the direct feedback that drove this layout).
 *
 * <p>Every color used here comes from {@link Theme} -- see {@code THEME.md}
 * in the repo root for the palette reference and rationale. Everything is
 * drawn manually with {@link DrawContext} rather than vanilla
 * {@code ButtonWidget}s so the whole panel can share one consistent look.
 */
public class ClickGui extends Screen {
    private static final int SIDEBAR_WIDTH = 150;
    private static final int HEADER_HEIGHT = 30;
    private static final int TAB_HEIGHT = 30;
    private static final int ROW_HEIGHT = 40;
    private static final int SWATCH_SIZE = 12;
    private static final int SWATCH_MARGIN = 10;
    /** Content area always reserves room for at least this many rows, so a small category doesn't look cramped. */
    private static final int MIN_VISIBLE_ROWS = 6;

    private Category selectedCategory = Category.MOVEMENT;

    public ClickGui() {
        super(Text.literal("Nyte Client"));
    }

    @Override
    protected void init() {
        super.init();
        BlurSuppressor.suppress();
    }

    @Override
    public void removed() {
        BlurSuppressor.restore();
        super.removed();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        List<Module> modules = NyteClientMod.MODULES.getByCategory(selectedCategory);

        int panelWidth = Math.min(width - 40, 620);
        int contentRows = Math.max(modules.size(), MIN_VISIBLE_ROWS);
        int panelHeight = Math.min(height - 40, HEADER_HEIGHT + contentRows * ROW_HEIGHT);
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;

        int sidebarX = panelX;
        int contentX = panelX + SIDEBAR_WIDTH;
        int contentWidth = panelWidth - SIDEBAR_WIDTH;
        int bodyTop = panelY + HEADER_HEIGHT;
        int bodyHeight = panelHeight - HEADER_HEIGHT;

        // Full-screen backdrop -- opaque, since the world keeps animating
        // behind this panel (shouldPause() is false) and any translucency
        // here lets that motion bleed through and read as a blur.
        context.fill(0, 0, width, height, Theme.BACKGROUND);

        // Panel body + border.
        context.fill(panelX, panelY, panelX + panelWidth, panelY + panelHeight, Theme.PANEL);
        context.drawBorder(panelX, panelY, panelWidth, panelHeight, Theme.BORDER);

        // Header bar.
        context.fill(panelX, panelY, panelX + panelWidth, panelY + HEADER_HEIGHT, Theme.PANEL_HEADER);
        context.drawTextWithShadow(textRenderer, "NYTE CLIENT", panelX + 12, panelY + 11, Theme.TEXT_PRIMARY);
        renderThemeSwatch(context, panelX, panelWidth, panelY, mouseX, mouseY);

        // Sidebar / content divider.
        context.fill(sidebarX + SIDEBAR_WIDTH, bodyTop, sidebarX + SIDEBAR_WIDTH + 1, panelY + panelHeight,
                Theme.BORDER);

        renderSidebar(context, sidebarX, bodyTop, bodyHeight, mouseX, mouseY);
        renderModuleList(context, modules, contentX, bodyTop, contentWidth, mouseX, mouseY);

        super.render(context, mouseX, mouseY, delta);
    }

    /** Clickable square in the header that cycles {@link Theme.Preset}. */
    private void renderThemeSwatch(DrawContext context, int panelX, int panelWidth, int panelY, int mouseX,
            int mouseY) {
        int[] rect = themeSwatchRect(panelX, panelWidth, panelY);
        boolean hovered = isHovering(mouseX, mouseY, rect[0], rect[1], SWATCH_SIZE, SWATCH_SIZE);
        context.fill(rect[0], rect[1], rect[0] + SWATCH_SIZE, rect[1] + SWATCH_SIZE, Theme.ACCENT);
        context.drawBorder(rect[0], rect[1], SWATCH_SIZE, SWATCH_SIZE, Theme.BORDER);
        if (hovered) {
            context.drawTooltip(textRenderer,
                    Text.literal("Theme: " + Theme.getPreset().getLabel() + " (click to cycle)"), mouseX, mouseY);
        }
    }

    private int[] themeSwatchRect(int panelX, int panelWidth, int panelY) {
        int x = panelX + panelWidth - SWATCH_SIZE - SWATCH_MARGIN;
        int y = panelY + (HEADER_HEIGHT - SWATCH_SIZE) / 2;
        return new int[] {x, y};
    }

    /** Category list down the left side, one full-width row per category with a module count. */
    private void renderSidebar(DrawContext context, int sidebarX, int bodyTop, int bodyHeight, int mouseX,
            int mouseY) {
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            int y = bodyTop + i * TAB_HEIGHT;
            boolean selected = category == selectedCategory;
            boolean hovered = isHovering(mouseX, mouseY, sidebarX, y, SIDEBAR_WIDTH, TAB_HEIGHT);

            int background = selected ? Theme.ACCENT_MUTED : hovered ? Theme.PANEL_HEADER : Theme.PANEL;
            context.fill(sidebarX, y, sidebarX + SIDEBAR_WIDTH, y + TAB_HEIGHT, background);
            if (selected) {
                context.fill(sidebarX, y, sidebarX + 3, y + TAB_HEIGHT, Theme.ACCENT);
            }

            int textColor = selected ? Theme.TEXT_PRIMARY : Theme.TEXT_MUTED;
            context.drawTextWithShadow(textRenderer, category.getLabel(), sidebarX + 14, y + 11, textColor);

            int count = NyteClientMod.MODULES.getByCategory(category).size();
            String countText = String.valueOf(count);
            int countWidth = textRenderer.getWidth(countText);
            context.drawTextWithShadow(textRenderer, countText, sidebarX + SIDEBAR_WIDTH - countWidth - 12, y + 11,
                    Theme.TEXT_MUTED);
        }

        // Fill any leftover sidebar height below the last category so the divider/border reads as one solid column.
        int usedHeight = categories.length * TAB_HEIGHT;
        if (usedHeight < bodyHeight) {
            context.fill(sidebarX, bodyTop + usedHeight, sidebarX + SIDEBAR_WIDTH, bodyTop + bodyHeight, Theme.PANEL);
        }
    }

    /** Module rows: name + description on their own lines, plus an explicit ON/OFF label -- not just a dot. */
    private void renderModuleList(DrawContext context, List<Module> modules, int listX, int listY, int listWidth,
            int mouseX, int mouseY) {
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int y = listY + i * ROW_HEIGHT;
            boolean placeholder = module.isPlaceholder();
            boolean hovered = !placeholder && isHovering(mouseX, mouseY, listX, y, listWidth, ROW_HEIGHT);

            if (hovered) {
                context.fill(listX, y, listX + listWidth, y + ROW_HEIGHT, Theme.PANEL_HEADER);
            }
            if (i > 0) {
                context.fill(listX, y, listX + listWidth, y + 1, Theme.BORDER);
            }

            if (placeholder) {
                renderPlaceholderRow(context, module, listX, y, listWidth);
                continue;
            }

            boolean enabled = module.isEnabled();
            String stateText = enabled ? "ON" : "OFF";
            int stateColor = enabled ? Theme.ENABLED : Theme.DISABLED;
            int stateWidth = textRenderer.getWidth(stateText);
            int statePillWidth = stateWidth + 12;
            int stateX = listX + listWidth - statePillWidth - 14;
            int stateY = y + ROW_HEIGHT / 2 - 7;
            context.fill(stateX, stateY, stateX + statePillWidth, stateY + 14, enabled ? stateColor : Theme.PANEL);
            context.drawBorder(stateX, stateY, statePillWidth, 14, stateColor);
            context.drawTextWithShadow(textRenderer, stateText, stateX + 6, stateY + 3,
                    enabled ? Theme.TEXT_ON_ACCENT : stateColor);

            int textColor = enabled ? Theme.TEXT_PRIMARY : Theme.TEXT_MUTED;
            context.drawTextWithShadow(textRenderer, module.getName(), listX + 12, y + 7, textColor);
            context.drawTextWithShadow(textRenderer, module.getDescription(), listX + 12, y + 21, Theme.TEXT_MUTED);
        }
    }

    /** A roadmap entry: dimmed name, a "SOON" pill instead of ON/OFF, no hover highlight (it isn't clickable). */
    private void renderPlaceholderRow(DrawContext context, Module module, int listX, int y, int listWidth) {
        String stateText = "SOON";
        int stateWidth = textRenderer.getWidth(stateText);
        int statePillWidth = stateWidth + 12;
        int stateX = listX + listWidth - statePillWidth - 14;
        int stateY = y + ROW_HEIGHT / 2 - 7;
        context.fill(stateX, stateY, stateX + statePillWidth, stateY + 14, Theme.PANEL);
        context.drawBorder(stateX, stateY, statePillWidth, 14, Theme.DISABLED);
        context.drawTextWithShadow(textRenderer, stateText, stateX + 6, stateY + 3, Theme.DISABLED);

        context.drawTextWithShadow(textRenderer, module.getName(), listX + 12, y + 7, Theme.DISABLED);
        context.drawTextWithShadow(textRenderer, module.getDescription(), listX + 12, y + 21, Theme.DISABLED);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        List<Module> modules = NyteClientMod.MODULES.getByCategory(selectedCategory);
        int panelWidth = Math.min(width - 40, 620);
        int contentRows = Math.max(modules.size(), MIN_VISIBLE_ROWS);
        int panelHeight = Math.min(height - 40, HEADER_HEIGHT + contentRows * ROW_HEIGHT);
        int panelX = (width - panelWidth) / 2;
        int panelY = (height - panelHeight) / 2;
        int bodyTop = panelY + HEADER_HEIGHT;

        int[] swatch = themeSwatchRect(panelX, panelWidth, panelY);
        if (isHovering(mouseX, mouseY, swatch[0], swatch[1], SWATCH_SIZE, SWATCH_SIZE)) {
            Theme.cyclePreset();
            return true;
        }

        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            if (isHovering(mouseX, mouseY, panelX, bodyTop + i * TAB_HEIGHT, SIDEBAR_WIDTH, TAB_HEIGHT)) {
                selectedCategory = categories[i];
                return true;
            }
        }

        int listX = panelX + SIDEBAR_WIDTH;
        int listWidth = panelWidth - SIDEBAR_WIDTH;
        for (int i = 0; i < modules.size(); i++) {
            if (isHovering(mouseX, mouseY, listX, bodyTop + i * ROW_HEIGHT, listWidth, ROW_HEIGHT)) {
                // Placeholder rows consume the click (so it doesn't fall through to whatever
                // is behind the panel) but don't toggle -- there's nothing to turn on yet.
                if (!modules.get(i).isPlaceholder()) {
                    modules.get(i).toggle();
                }
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        // Keep ticking (and rendering the world) behind the panel like a real ClickGUI.
        return false;
    }

    private boolean isHovering(double mouseX, double mouseY, int x, int y, int w, int h) {
        return mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + h;
    }
}
