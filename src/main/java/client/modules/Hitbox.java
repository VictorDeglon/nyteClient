package client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;

/**
 * Holds the player in the sneaking pose at all times, shrinking their
 * hitbox from 1.8 to 1.5 blocks tall -- the same reduction vanilla already
 * gives anyone for holding shift, and one the server itself computes (pose
 * is synced), so it isn't something an anti-cheat can flag as impossible.
 *
 * <p>This module deliberately does <em>not</em> remove the vanilla sneak
 * movement penalty. Doing that ("NoSlow") would turn a harmless pose toggle
 * into an actual speed-hack, which is out of scope for this mod.
 */
public class Hitbox extends ModuleBase {
    public Hitbox() {
        super("Hitbox", "Auto-crouches for a smaller, vanilla-legal hitbox.", Category.PLAYER);
    }

    @Override
    protected void onDisable() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.setSneaking(false);
        }
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            player.setSneaking(true);
        }
    }
}
