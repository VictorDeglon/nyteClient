package client.modules;

import client.Theme;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

/**
 * Replaces the vanilla crosshair with a themed one: a plain gapped cross,
 * colored from {@link Theme#ACCENT}. The vanilla crosshair is actually
 * suppressed (see {@code client.mixin.InGameHudMixin}), not just drawn over
 * — {@link #isActive()} is what that mixin checks.
 */
public class Crosshair extends ModuleBase implements HudRenderCallback {
    private static final int LENGTH = 10;
    private static final int GAP = 3;
    private static final int THICKNESS = 2;

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

        // Vertical bar, split around the center to leave a gap.
        context.fill(centerX - halfThickness, centerY - half, centerX + halfThickness, centerY - GAP, Theme.ACCENT);
        context.fill(centerX - halfThickness, centerY + GAP, centerX + halfThickness, centerY + half, Theme.ACCENT);
        // Horizontal bar, same gap.
        context.fill(centerX - half, centerY - halfThickness, centerX - GAP, centerY + halfThickness, Theme.ACCENT);
        context.fill(centerX + GAP, centerY - halfThickness, centerX + half, centerY + halfThickness, Theme.ACCENT);
    }
}
