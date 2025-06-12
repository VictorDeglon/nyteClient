/**
 * Simple configuration screen for Nyte Client.
 * Provides a keybind to open the UI from anywhere in game.
 */
package client.gui;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
public class ModGui extends Screen {
    /** Keybinding used to open this GUI screen. */
    private static final net.minecraft.client.option.KeyBinding OPEN_GUI = KeyBindingHelper.registerKeyBinding(
            new net.minecraft.client.option.KeyBinding("key.nyteclient.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Nyte Client"));

    /**
     * Creates the screen with a simple title.
     */
    public ModGui() {
        super(Text.literal("Nyte Client"));
    }

    /**
     * Checks the keybind each tick and opens the GUI when pressed.
     */
    public static void tick() {
        // If the player pressed the keybinding open the screen
        if (OPEN_GUI.wasPressed()) {
            // Show the GUI to the user
            MinecraftClient.getInstance().setScreen(new ModGui());
        }
    }

    @Override
    /**
     * Lays out the widgets on the screen when opened.
     */
    protected void init() {
        // Add a simple button that closes the screen
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close()).dimensions(width / 2 - 40, height / 2 - 10, 80, 20).build());
    }
}
