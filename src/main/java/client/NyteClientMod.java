package client;

import net.fabricmc.api.ModInitializer;
import client.modules.ModuleManager;
import client.modules.FullBright;
import client.modules.CoordinatesHUD;
import client.utils.ModTickHandler;

public class NyteClientMod implements ModInitializer {
    public static final String MOD_ID = "nyteclient";
    public static final ModuleManager MODULES = new ModuleManager();

    @Override
    public void onInitialize() {
        MODULES.register(new FullBright());
        MODULES.register(new CoordinatesHUD());
        ModTickHandler.init();
        System.out.println("Nyte Client Initialized");
    }
}
