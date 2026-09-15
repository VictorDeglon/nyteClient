package client;

import net.fabricmc.api.ModInitializer;
import client.modules.ModuleManager;
import client.modules.Flight;
import client.modules.Speed;
import client.modules.JumpBoost;
import client.modules.AirJump;
import client.modules.Hitbox;
import client.modules.FullBright;
import client.modules.Zoom;
import client.modules.Xray;
import client.modules.Crosshair;
import client.modules.CoordinatesHUD;
import client.modules.Waypoints;
import client.modules.Category;
import client.modules.PlaceholderModule;
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
        MODULES.register(new Speed());
        MODULES.register(new JumpBoost());
        MODULES.register(new AirJump());
        MODULES.register(new Hitbox());
        MODULES.register(new FullBright());
        MODULES.register(new Zoom());
        MODULES.register(new Xray());
        MODULES.register(new Crosshair());
        MODULES.register(new CoordinatesHUD());
        MODULES.register(new Waypoints());

        registerRoadmapPlaceholders();

        ModTickHandler.init();

        System.out.println("[Nyte Client] Initialized " + MODULES.getModules().size() + " modules");
    }

    /**
     * Roadmap entries from the full feature spec (see {@code ROADMAP.md}) that
     * aren't built yet. Registered as {@link PlaceholderModule}s so the planned
     * feature set is visible and organized in the ClickGUI -- greyed out,
     * labeled "SOON", not clickable -- rather than only living in a doc nobody
     * opens. Promote one to a real module (and delete its entry here) as it
     * gets built; don't leave a module in both places.
     */
    private void registerRoadmapPlaceholders() {
        MODULES.register(new PlaceholderModule("World Dashboard",
                "Dimension, coordinates, biome, time, and weather in one view.", Category.WORLD));
        MODULES.register(new PlaceholderModule("Block Inspector",
                "Hold a key to see the block you're looking at: hardness, best tool, light level.",
                Category.WORLD));
        MODULES.register(new PlaceholderModule("Entity Inspector",
                "Hold a key to see an entity's health, name, and equipment.", Category.WORLD));

        MODULES.register(new PlaceholderModule("Session Statistics",
                "Playtime, blocks mined/placed, deaths -- local only, never sent anywhere.", Category.PLAYER));
        MODULES.register(new PlaceholderModule("Damage Feedback",
                "Configurable screen/sound feedback when you take damage.", Category.PLAYER));
        MODULES.register(new PlaceholderModule("Item Pickup Feedback",
                "A small icon + name animation for notable pickups.", Category.PLAYER));

        MODULES.register(new PlaceholderModule("Camera System",
                "Smoother camera motion and landing/sprint effects, with an intensity slider.", Category.RENDER));
        MODULES.register(new PlaceholderModule("Ambient Immersion",
                "Optional ambient particles and audio per biome/dimension.", Category.RENDER));
        MODULES.register(new PlaceholderModule("Dynamic Lighting",
                "Held-item light and emissive item presentation.", Category.RENDER));

        MODULES.register(new PlaceholderModule("Command Center Search",
                "Right Shift becomes a fuzzy search across every module and action.", Category.MISC));
        MODULES.register(new PlaceholderModule("Notification Center",
                "Elegant, temporary toasts instead of chat spam.", Category.MISC));
        MODULES.register(new PlaceholderModule("Modern Chat",
                "Tabs, timestamps, collapsible system messages, clickable coordinates.", Category.MISC));
        MODULES.register(new PlaceholderModule("Inventory Search",
                "Ctrl+F in any inventory to highlight matching items.", Category.MISC));
        MODULES.register(new PlaceholderModule("Screenshot Mode",
                "One key to hide HUD/chat/nametags for a clean screenshot.", Category.MISC));
        MODULES.register(new PlaceholderModule("Performance Advisor",
                "Explains what's costing you FPS, not just a number.", Category.MISC));
        MODULES.register(new PlaceholderModule("Client Profiles",
                "Swap HUD/crosshair/settings automatically per server or world.", Category.MISC));
    }
}
