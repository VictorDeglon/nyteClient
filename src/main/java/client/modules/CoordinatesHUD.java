/**
 * Heads-up display module that prints the player's XYZ coordinates on screen.
 * Registers itself as a HUD render callback when enabled.
 */
package client.modules;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class CoordinatesHUD extends ModuleBase implements HudRenderCallback {
    @Override
    /**
     * @return the name shown in the module list.
     */
    public String getName() {
        return "Coordinates HUD";
    }

    @Override
    /**
     * Registers this instance to start rendering coordinates on the HUD.
     */
    protected void onEnable() {
        // Subscribe to HUD render events
        HudRenderCallback.EVENT.register(this);
    }

    @Override
    /**
     * Stops rendering coordinates on the HUD when disabled.
     */
    protected void onDisable() {
        // Unsubscribe from HUD render events
        HudRenderCallback.EVENT.unregister(this);
    }

    @Override
    /**
     * Draws the player's current coordinates each frame.
     */
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        // Skip rendering when the module is disabled
        if (!isEnabled()) return;
        // Acquire game client instance
        MinecraftClient client = MinecraftClient.getInstance();
        // Nothing to draw if the player doesn't exist
        if (client.player == null) return;
        // Build the XYZ string for display
        String coords = String.format("XYZ: %d %d %d", (int) client.player.getX(), (int) client.player.getY(), (int) client.player.getZ());
        // Render the coordinates on the screen
        client.textRenderer.drawWithShadow(matrices, coords, 2, 2, 0xFFFFFF);
    }
}
