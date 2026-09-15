package client.modules;

import client.Theme;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

/**
 * Replaces the vanilla crosshair with a themed one: a gapped cross with a
 * dark outline (for contrast against bright backgrounds, since this is a
 * flat fill rather than vanilla's inverting blend), colored from
 * {@link Theme#ACCENT}. The vanilla crosshair is actually suppressed (see
 * {@code client.mixin.InGameHudMixin}), not just drawn over —
 * {@link #isActive()} is what that mixin checks.
 */
public class Crosshair extends ModuleBase implements HudRenderCallback {
    private static final int LENGTH = 16;
    private static final int GAP = 4;
    private static final int THICKNESS = 3;
    private static final int OUTLINE = 1;
    private static final int OUTLINE_COLOR = 0xFF000000;

    private static Crosshair instance;

    public Crosshair() {
        super("Crosshair", "Replaces the vanilla crosshair with a themed one.", Category.RENDER);
        instance = this;
        HudRenderCallback.EVENT.register(this);
    }

    /** Checked by {@code client.mixin.InGameHudMixin} to decide whether to suppress the vanilla crosshair. */
    public static boolean isActive() {
        return instance != null && instance.isEnabled();
    }

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.options.hudHidden || client.currentScreen != null) {
            return;
        }

        int centerX = context.getScaledWindowWidth() / 2;
        int centerY = context.getScaledWindowHeight() / 2;
        int half = LENGTH / 2;
        int halfThickness = THICKNESS / 2;

        // Vertical bar, split around the center to leave a gap; horizontal bar, same gap.
        drawBar(context, centerX - halfThickness, centerY - half, centerX + halfThickness, centerY - GAP);
        drawBar(context, centerX - halfThickness, centerY + GAP, centerX + halfThickness, centerY + half);
        drawBar(context, centerX - half, centerY - halfThickness, centerX - GAP, centerY + halfThickness);
        drawBar(context, centerX + GAP, centerY - halfThickness, centerX + half, centerY + halfThickness);
    }

    /** Draws one crosshair segment with a 1px dark outline so it stays visible on any background. */
    private void drawBar(DrawContext context, int x1, int y1, int x2, int y2) {
        context.fill(x1 - OUTLINE, y1 - OUTLINE, x2 + OUTLINE, y2 + OUTLINE, OUTLINE_COLOR);
        context.fill(x1, y1, x2, y2, Theme.ACCENT);
    }
}
