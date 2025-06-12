package client.utils;

import client.gui.ModGui;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ModTickHandler {
    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> ModGui.tick());
    }
}
