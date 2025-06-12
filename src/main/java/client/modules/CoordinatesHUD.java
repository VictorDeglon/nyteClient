package client.modules;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class CoordinatesHUD extends ModuleBase implements HudRenderCallback {
    @Override
    public String getName() {
        return "Coordinates HUD";
    }

    @Override
    protected void onEnable() {
        HudRenderCallback.EVENT.register(this);
    }

    @Override
    protected void onDisable() {
        HudRenderCallback.EVENT.unregister(this);
    }

    @Override
    public void onHudRender(MatrixStack matrices, float tickDelta) {
        if (!isEnabled()) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        String coords = String.format("XYZ: %d %d %d", (int) client.player.getX(), (int) client.player.getY(), (int) client.player.getZ());
        client.textRenderer.drawWithShadow(matrices, coords, 2, 2, 0xFFFFFF);
    }
}
