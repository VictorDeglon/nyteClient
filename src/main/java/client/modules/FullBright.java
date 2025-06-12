/**
 * Module that maximizes the in-game brightness setting when enabled.
 * Useful for playing in dark areas without needing external light sources.
 */
package client.modules;

import net.minecraft.client.MinecraftClient;

public class FullBright extends ModuleBase {
    /** Previous gamma value so it can be restored on disable. */
    private double prevGamma = 1.0;

    @Override
    /**
     * @return the display name of this module.
     */
    public String getName() {
        return "FullBright";
    }

    @Override
    /**
     * Stores the current gamma and sets it to a very high value.
     */
    protected void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        // Save current gamma so we can restore it later
        prevGamma = client.options.getGamma().getValue();
        // Apply maximum gamma for brightness
        client.options.getGamma().setValue(16.0);
    }

    @Override
    /**
     * Restores the previous gamma setting when the module is disabled.
     */
    protected void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        // Reset gamma to the saved value
        client.options.getGamma().setValue(prevGamma);
    }
}
