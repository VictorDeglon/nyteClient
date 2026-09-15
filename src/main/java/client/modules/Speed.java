package client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Multiplies horizontal movement speed.
 *
 * <p>Like {@link Flight}, this only changes what your own client reports —
 * a server with real movement validation (most anti-cheats, including this
 * project's own {@code mc_anticheat}) will flag or reject the resulting
 * movement. That's expected: this module doesn't try to hide what it's
 * doing, it's a plain toggle for a server you control and have configured
 * to tolerate it.
 */
public class Speed extends ModuleBase {
    private static final double MULTIPLIER = 1.6;

    public Speed() {
        super("Speed", "Multiplies horizontal movement speed. Will be flagged by real anti-cheat.",
                Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        Vec3d velocity = player.getVelocity();
        double horizontalSpeedSq = velocity.x * velocity.x + velocity.z * velocity.z;
        if (horizontalSpeedSq < 1.0E-6) {
            return;
        }
        player.setVelocity(velocity.x * MULTIPLIER, velocity.y, velocity.z * MULTIPLIER);
    }
}
