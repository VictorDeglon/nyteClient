package client.modules;

import client.Theme;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

/**
 * Draws the player's XYZ position in the top-left corner while enabled.
 *
 * <p>Registers with {@link HudRenderCallback} once, in the constructor, and
 * gates its own drawing on {@link #isEnabled()} -- Fabric's event bus has no
 * unregister hook, so toggling has to happen inside the callback itself.
 */
public class CoordinatesHUD extends ModuleBase implements HudRenderCallback {
    public CoordinatesHUD() {
        super("Coordinates", "Shows your XYZ position on screen.", Category.RENDER);
        HudRenderCallback.EVENT.register(this);
    }

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        if (!isEnabled()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) {
            return;
        }
        String coords = String.format("XYZ: %d %d %d",
                (int) client.player.getX(), (int) client.player.getY(), (int) client.player.getZ());
        drawContext.drawTextWithShadow(client.textRenderer, coords, 4, 4, Theme.TEXT_PRIMARY);
    }
}
