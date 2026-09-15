package client.modules;

import net.minecraft.client.MinecraftClient;

/**
 * Maximizes the in-game brightness setting when enabled. Useful for playing
 * in dark areas without needing external light sources.
 */
public class FullBright extends ModuleBase {
    /** Previous gamma value so it can be restored on disable. */
    private double prevGamma = 1.0;

    public FullBright() {
        super("FullBright", "Maxes out gamma so dark areas look fully lit.", Category.RENDER);
    }

    @Override
    protected void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        prevGamma = client.options.getGamma().getValue();
        client.options.getGamma().setValue(16.0);
    }

    @Override
    protected void onDisable() {
        MinecraftClient.getInstance().options.getGamma().setValue(prevGamma);
    }
}
