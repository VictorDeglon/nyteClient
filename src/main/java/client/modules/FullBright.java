package client.modules;

import net.minecraft.client.MinecraftClient;

public class FullBright extends ModuleBase {
    private double prevGamma = 1.0;

    @Override
    public String getName() {
        return "FullBright";
    }

    @Override
    protected void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        prevGamma = client.options.getGamma().getValue();
        client.options.getGamma().setValue(16.0);
    }

    @Override
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        client.options.getGamma().setValue(prevGamma);
    }
}
