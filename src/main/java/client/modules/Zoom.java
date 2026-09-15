package client.modules;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * While enabled, holding the bound key narrows the FOV like looking through
 * a spyglass -- purely a rendering/QoL convenience, no gameplay effect.
 */
public class Zoom extends ModuleBase {
    private static final KeyBinding KEY = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.nyteclient.zoom", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "Nyte Client"));

    private int prevFov;
    private boolean zoomed;

    public Zoom() {
        super("Zoom", "Hold C to zoom in.", Category.RENDER);
    }

    @Override
    protected void onDisable() {
        restoreFov();
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        boolean held = KEY.isPressed();
        if (held && !zoomed) {
            prevFov = client.options.getFov().getValue();
            client.options.getFov().setValue(prevFov / 4);
            zoomed = true;
        } else if (!held && zoomed) {
            restoreFov();
        }
    }

    private void restoreFov() {
        if (zoomed) {
            MinecraftClient.getInstance().options.getFov().setValue(prevFov);
            zoomed = false;
        }
    }
}
