package client.utils;

import client.NyteClientMod;
import client.gui.ClickGui;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Wires the module tick loop and the GUI-open keybind into Fabric's client
 * tick event.
 */
public class ModTickHandler {
    private static final KeyBinding OPEN_GUI = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.nyteclient.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Nyte Client"));

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            NyteClientMod.MODULES.tick();
            if (OPEN_GUI.wasPressed()) {
                MinecraftClient.getInstance().setScreen(new ClickGui());
            }
        });
    }
}
