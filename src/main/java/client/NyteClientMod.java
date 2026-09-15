package client;

import net.fabricmc.api.ModInitializer;
import client.modules.ModuleManager;
import client.modules.Flight;
import client.modules.Hitbox;
import client.modules.FullBright;
import client.modules.Zoom;
import client.modules.CoordinatesHUD;
import client.utils.ModTickHandler;

/**
 * Entry point for the Nyte Client Fabric mod.
 *
 * <p>This class is loaded by Fabric when the client starts. It registers
 * every built-in module and starts the tick loop that drives them and the
 * ClickGUI keybind.
 */
public class NyteClientMod implements ModInitializer {
    /** Unique identifier for this mod defined in fabric.mod.json. */
    public static final String MOD_ID = "nyteclient";
    /** Manager that stores and controls all feature modules. */
    public static final ModuleManager MODULES = new ModuleManager();

    @Override
    public void onInitialize() {
        MODULES.register(new Flight());
        MODULES.register(new Hitbox());
        MODULES.register(new FullBright());
        MODULES.register(new Zoom());
        MODULES.register(new CoordinatesHUD());

        ModTickHandler.init();

        System.out.println("[Nyte Client] Initialized " + MODULES.getModules().size() + " modules");
    }
}
