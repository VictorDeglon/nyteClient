package client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Lets the player fly like Creative mode while in Survival.
 *
 * <p>This only controls the local client's movement flags -- it cannot make
 * a server trust movement it doesn't already allow. For this to actually
 * work without getting the player kicked, the target server needs
 * {@code allow-flight=true} in {@code server.properties} (that's the vanilla
 * setting for "a client mod may provide flight"), or the player already has
 * flight some other way (creative/spectator). Without that, vanilla's own
 * anti-fly check kicks players who stay airborne too long.
 */
public class Flight extends ModuleBase {
    private boolean prevAllowFlying;
    private boolean prevFlying;

    public Flight() {
        super("Flight", "Creative-style flight in Survival. Needs allow-flight=true on the server.",
                Category.MOVEMENT);
    }

    @Override
    protected void onEnable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        prevAllowFlying = player.getAbilities().allowFlying;
        prevFlying = player.getAbilities().flying;
        player.getAbilities().allowFlying = true;
        player.getAbilities().flying = true;
    }

    @Override
    protected void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        player.getAbilities().flying = prevFlying;
        player.getAbilities().allowFlying = prevAllowFlying;
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        // Re-assert every tick: the server (or a respawn/gamemode change) can
        // reset these flags whenever it doesn't recognize us as flight-allowed.
        player.getAbilities().allowFlying = true;
        player.getAbilities().flying = true;
    }
}
