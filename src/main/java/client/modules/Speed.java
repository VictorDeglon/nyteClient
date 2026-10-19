package client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Multiplies horizontal movement speed, up to a fixed ceiling.
 *
 * <p>Like {@link Flight}, this only changes what your own client reports —
 * a server with real movement validation (most anti-cheats, including this
 * project's own {@code mc_anticheat}) will flag or reject the resulting
 * movement. That's expected: this module doesn't try to hide what it's
 * doing, it's a plain toggle for a server you control and have configured
 * to tolerate it.
 *
 * <p>The first version of this multiplied {@code player.getVelocity()} by a
 * constant every tick with no ceiling -- since that runs again on the
 * already-boosted result each tick, it compounded geometrically (1.6^n)
 * instead of settling anywhere, which is exactly the "flying" behavior that
 * got reported. Clamping to {@link #MAX_HORIZONTAL_SPEED} is what actually
 * fixes that: once a tick's boosted speed would exceed the ceiling, this
 * sets the magnitude to the ceiling instead of multiplying past it, so
 * speed converges there and stays instead of climbing every tick.
 */
public class Speed extends ModuleBase {
    private static final double MULTIPLIER = 1.3;
    /** Hard ceiling in blocks/tick (~10 blocks/sec) -- roughly 2x sprint speed, not "flying". */
    private static final double MAX_HORIZONTAL_SPEED = 0.5;

    public Speed() {
        super("Speed", "Multiplies horizontal movement speed, capped. Will be flagged by real anti-cheat.",
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
        double currentSpeed = Math.sqrt(horizontalSpeedSq);
        double targetSpeed = Math.min(currentSpeed * MULTIPLIER, MAX_HORIZONTAL_SPEED);
        double scale = targetSpeed / currentSpeed;
        player.setVelocity(velocity.x * scale, velocity.y, velocity.z * scale);
    }
}
