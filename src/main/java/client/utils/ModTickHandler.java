/**
 * Utility class that hooks the mod GUI tick logic into the Fabric event system.
 */
package client.utils;

import client.gui.ModGui;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ModTickHandler {
    /**
     * Registers the GUI ticker so the screen can be opened via keybind.
     */
    public static void init() {
        // Attach our tick handler to the Fabric API event
        ClientTickEvents.END_CLIENT_TICK.register(client -> ModGui.tick());
    }
}
