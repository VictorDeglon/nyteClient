package client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Grants an extra jump while airborne (double jump), reset the moment you
 * touch the ground. Reads the jump keybinding's edge-triggered
 * {@code wasPressed()} rather than the level-triggered state vanilla's own
 * ground-jump handling polls, so this doesn't interfere with normal
 * jumping.
 */
public class AirJump extends ModuleBase {
    private static final int MAX_AIR_JUMPS = 1;
    private static final double AIR_JUMP_VELOCITY = 0.42;

    private int airJumpsUsed;

    public AirJump() {
        super("AirJump", "One extra jump mid-air. Will be flagged by real anti-cheat.", Category.MOVEMENT);
    }

    @Override
    protected void onDisable() {
        airJumpsUsed = 0;
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }

        if (player.isOnGround()) {
            airJumpsUsed = 0;
            return;
        }

        if (client.options.jumpKey.wasPressed() && airJumpsUsed < MAX_AIR_JUMPS) {
            Vec3d velocity = player.getVelocity();
            player.setVelocity(velocity.x, AIR_JUMP_VELOCITY, velocity.z);
            airJumpsUsed++;
        }
    }
}
