package client.gui;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ModGui extends Screen {
    private static final net.minecraft.client.option.KeyBinding OPEN_GUI = KeyBindingHelper.registerKeyBinding(
            new net.minecraft.client.option.KeyBinding("key.nyteclient.gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "Nyte Client"));

    public ModGui() {
        super(Text.literal("Nyte Client"));
    }

    public static void tick() {
        if (OPEN_GUI.wasPressed()) {
            MinecraftClient.getInstance().setScreen(new ModGui());
        }
    }

    @Override
    protected void init() {
        addDrawableChild(ButtonWidget.builder(Text.literal("Close"), button -> close()).dimensions(width / 2 - 40, height / 2 - 10, 80, 20).build());
    }
}
