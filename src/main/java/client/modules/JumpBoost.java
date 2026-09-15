package client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Amplifies the velocity of a normal jump — covers both "jump higher" and
 * "jump further" with one multiplier, since they're the same vanilla
 * impulse. Detected on the tick right after a jump leaves the ground; like
 * {@link Speed}, whether the resulting movement survives is up to the
 * server.
 */
public class JumpBoost extends ModuleBase {
    private static final double VERTICAL_BOOST = 1.8;
    private static final double HORIZONTAL_BOOST = 1.6;

    private boolean wasOnGround = true;

    public JumpBoost() {
        super("JumpBoost", "Higher, longer jumps. Will be flagged by real anti-cheat.", Category.MOVEMENT);
    }

    @Override
    public void onTick() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        boolean onGround = player.isOnGround();
        Vec3d velocity = player.getVelocity();
        if (wasOnGround && !onGround && velocity.y > 0) {
            player.setVelocity(velocity.x * HORIZONTAL_BOOST, velocity.y * VERTICAL_BOOST,
                    velocity.z * HORIZONTAL_BOOST);
        }
        wasOnGround = onGround;
    }
}
