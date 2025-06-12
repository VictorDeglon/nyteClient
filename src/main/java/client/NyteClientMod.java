/**
 * Entry point for the Nyte Client Fabric mod.
 *
 * <p>This class is loaded by Fabric when the client starts. It registers
 * built-in modules and sets up tick handlers used by the client GUI and other systems.
 */
package client;

import net.fabricmc.api.ModInitializer;
import client.modules.ModuleManager;
import client.modules.FullBright;
import client.modules.CoordinatesHUD;
import client.utils.ModTickHandler;

public class NyteClientMod implements ModInitializer {
    /** Unique identifier for this mod defined in fabric.mod.json. */
    public static final String MOD_ID = "nyteclient";
    /** Manager that stores and controls all feature modules. */
    public static final ModuleManager MODULES = new ModuleManager();
    /**
     * Called by Fabric when the mod is loaded.
     * Registers built in modules and sets up tick handlers.
     */

    @Override
    public void onInitialize() {
        // Register built in modules so they can be toggled
        MODULES.register(new FullBright());
        // HUD element with player coordinates
        MODULES.register(new CoordinatesHUD());
        // Initialize tick hooks for GUI activation
        ModTickHandler.init();
        // Log confirmation of successful start
        System.out.println("Nyte Client Initialized");
    }
}
