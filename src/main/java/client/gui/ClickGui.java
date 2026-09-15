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
 * The mod's ClickGUI: a category tab strip on the left, a module list on
 * the right, and a hover tooltip showing each module's description.
 *
 * <p>Every color used here comes from {@link Theme} -- see {@code THEME.md}
 * in the repo root for the palette reference and rationale. Everything is
 * drawn manually with {@link DrawContext} rather than vanilla
 * {@code ButtonWidget}s so the whole panel can share one consistent look.
 */
public class ClickGui extends Screen {
    private static final int PANEL_WIDTH = 260;
    private static final int TAB_WIDTH = 90;
    private static final int ROW_HEIGHT = 20;
    private static final int HEADER_HEIGHT = 18;
    private static final int SWATCH_SIZE = 10;
    private static final int SWATCH_MARGIN = 6;

    private Category selectedCategory = Category.MOVEMENT;

    public ClickGui() {
        super(Text.literal("Nyte Client"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        List<Module> modules = NyteClientMod.MODULES.getByCategory(selectedCategory);
        int rowCount = Math.max(modules.size(), Category.values().length);
        int panelHeight = HEADER_HEIGHT + rowCount * ROW_HEIGHT;
        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - panelHeight) / 2;
        int listX = panelX + TAB_WIDTH;
        int listWidth = PANEL_WIDTH - TAB_WIDTH;
        int rowsTop = panelY + HEADER_HEIGHT;

        // Dim the game behind the panel.
        context.fill(0, 0, width, height, Theme.BACKGROUND);

        // Panel body + border.
        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + panelHeight, Theme.PANEL);
        context.drawBorder(panelX, panelY, PANEL_WIDTH, panelHeight, Theme.BORDER);

        // Header bar.
        context.fill(panelX, panelY, panelX + PANEL_WIDTH, panelY + HEADER_HEIGHT, Theme.PANEL_HEADER);
        context.drawTextWithShadow(textRenderer, "NYTE CLIENT", panelX + 8, panelY + 5, Theme.TEXT_PRIMARY);
        renderThemeSwatch(context, panelX, panelY, mouseX, mouseY);

        renderTabs(context, panelX, rowsTop, mouseX, mouseY);
        renderModuleList(context, modules, listX, rowsTop, listWidth, mouseX, mouseY);

        super.render(context, mouseX, mouseY, delta);
    }

    /** Small clickable square in the header that cycles {@link Theme.Preset}. */
    private void renderThemeSwatch(DrawContext context, int panelX, int panelY, int mouseX, int mouseY) {
        int[] rect = themeSwatchRect(panelX, panelY);
        boolean hovered = isHovering(mouseX, mouseY, rect[0], rect[1], SWATCH_SIZE, SWATCH_SIZE);
        context.fill(rect[0], rect[1], rect[0] + SWATCH_SIZE, rect[1] + SWATCH_SIZE, Theme.ACCENT);
        context.drawBorder(rect[0], rect[1], SWATCH_SIZE, SWATCH_SIZE, Theme.BORDER);
        if (hovered) {
            context.drawTooltip(textRenderer,
                    Text.literal("Theme: " + Theme.getPreset().getLabel() + " (click to cycle)"), mouseX, mouseY);
        }
    }

    private int[] themeSwatchRect(int panelX, int panelY) {
        int x = panelX + PANEL_WIDTH - SWATCH_SIZE - SWATCH_MARGIN;
        int y = panelY + (HEADER_HEIGHT - SWATCH_SIZE) / 2;
        return new int[] {x, y};
    }

    private void renderTabs(DrawContext context, int tabX, int tabY, int mouseX, int mouseY) {
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            int y = tabY + i * ROW_HEIGHT;
            boolean selected = category == selectedCategory;
            boolean hovered = isHovering(mouseX, mouseY, tabX, y, TAB_WIDTH, ROW_HEIGHT);

            int background = selected ? Theme.ACCENT_MUTED : hovered ? Theme.PANEL_HEADER : Theme.PANEL;
            context.fill(tabX, y, tabX + TAB_WIDTH, y + ROW_HEIGHT, background);
            if (selected) {
                context.fill(tabX, y, tabX + 2, y + ROW_HEIGHT, Theme.ACCENT);
            }

            int textColor = selected ? Theme.TEXT_PRIMARY : Theme.TEXT_MUTED;
            context.drawTextWithShadow(textRenderer, category.getLabel(), tabX + 8, y + 6, textColor);
        }
    }

    private void renderModuleList(DrawContext context, List<Module> modules, int listX, int listY, int listWidth,
            int mouseX, int mouseY) {
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int y = listY + i * ROW_HEIGHT;
            boolean hovered = isHovering(mouseX, mouseY, listX, y, listWidth, ROW_HEIGHT);

            if (hovered) {
                context.fill(listX, y, listX + listWidth, y + ROW_HEIGHT, Theme.PANEL_HEADER);
            }

            int dotColor = module.isEnabled() ? Theme.ENABLED : Theme.DISABLED;
            context.fill(listX + 6, y + 8, listX + 10, y + 12, dotColor);

            int textColor = module.isEnabled() ? Theme.TEXT_PRIMARY : Theme.TEXT_MUTED;
            context.drawTextWithShadow(textRenderer, module.getName(), listX + 16, y + 6, textColor);

            if (hovered) {
                context.drawTooltip(textRenderer, Text.literal(module.getDescription()), mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        List<Module> modules = NyteClientMod.MODULES.getByCategory(selectedCategory);
        int rowCount = Math.max(modules.size(), Category.values().length);
        int panelHeight = HEADER_HEIGHT + rowCount * ROW_HEIGHT;
        int panelX = (width - PANEL_WIDTH) / 2;
        int panelY = (height - panelHeight) / 2;
        int rowsTop = panelY + HEADER_HEIGHT;

        int[] swatch = themeSwatchRect(panelX, panelY);
        if (isHovering(mouseX, mouseY, swatch[0], swatch[1], SWATCH_SIZE, SWATCH_SIZE)) {
            Theme.cyclePreset();
            return true;
        }

        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            if (isHovering(mouseX, mouseY, panelX, rowsTop + i * ROW_HEIGHT, TAB_WIDTH, ROW_HEIGHT)) {
                selectedCategory = categories[i];
                return true;
            }
        }

        int listX = panelX + TAB_WIDTH;
        int listWidth = PANEL_WIDTH - TAB_WIDTH;
        for (int i = 0; i < modules.size(); i++) {
            if (isHovering(mouseX, mouseY, listX, rowsTop + i * ROW_HEIGHT, listWidth, ROW_HEIGHT)) {
                modules.get(i).toggle();
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
