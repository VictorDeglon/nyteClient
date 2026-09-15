package client.modules;

import client.Theme;
import client.waypoints.Waypoint;
import client.waypoints.WaypointStore;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Manual waypoints (press the bound key to drop one at your feet) plus an
 * automatic "Last Death" marker, with a HUD readout of the nearest one in
 * your current dimension: an 8-direction arrow, name, and distance.
 *
 * <p>Persisted via {@link WaypointStore} so they survive a restart.
 * Deliberately doesn't attempt "Manage Waypoints" list/rename/delete UI
 * yet -- see {@code ROADMAP.md}; edit {@code config/nyteclient/waypoints.json}
 * directly to remove one for now.
 */
public class Waypoints extends ModuleBase implements HudRenderCallback {
    private static final KeyBinding CREATE_KEY = KeyBindingHelper.registerKeyBinding(
            new KeyBinding("key.nyteclient.waypoint", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_B, "Nyte Client"));
    private static final String[] ARROWS = {"↑", "↗", "→", "↘", "↓", "↙", "←", "↖"};

    private boolean wasAlive = true;
    private Vec3d lastAlivePos = Vec3d.ZERO;
    private String lastAliveDimension = "";

    public Waypoints() {
        super("Waypoints", "Press B to drop a waypoint; auto-marks your last death.", Category.WORLD);
        HudRenderCallback.EVENT.register(this);
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            return;
        }

        boolean alive = player.getHealth() > 0.0f;
        if (alive) {
            lastAlivePos = player.getPos();
            lastAliveDimension = client.world.getRegistryKey().getValue().toString();
        } else if (wasAlive) {
            WaypointStore.setDeathWaypoint(new Waypoint("Last Death", lastAlivePos.x, lastAlivePos.y,
                    lastAlivePos.z, lastAliveDimension, true));
        }
        wasAlive = alive;

        if (CREATE_KEY.wasPressed()) {
            int manualCount = (int) WaypointStore.getAll().stream().filter(w -> !w.autoDeath).count();
            String dimension = client.world.getRegistryKey().getValue().toString();
            WaypointStore.add(new Waypoint("Waypoint " + (manualCount + 1), player.getX(), player.getY(),
                    player.getZ(), dimension, false));
            player.sendMessage(Text.literal("Waypoint created").formatted(Formatting.GREEN), true);
        }
    }

    @Override
    public void onHudRender(DrawContext context, RenderTickCounter tickCounter) {
        if (!isEnabled()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            return;
        }

        String dimension = client.world.getRegistryKey().getValue().toString();
        Vec3d playerPos = player.getPos();
        Optional<Waypoint> nearest = nearestInDimension(dimension, playerPos);
        if (nearest.isEmpty()) {
            return;
        }

        Waypoint waypoint = nearest.get();
        double dx = waypoint.x - playerPos.x;
        double dz = waypoint.z - playerPos.z;
        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        double verticalDiff = waypoint.y - playerPos.y;

        double targetYaw = Math.toDegrees(Math.atan2(-dx, dz));
        double relativeYaw = ((targetYaw - player.getYaw()) % 360 + 360) % 360;
        String arrow = ARROWS[(int) Math.round(relativeYaw / 45.0) % ARROWS.length];

        String line = String.format("%s %s %.0fm (%+.0f)", arrow, waypoint.name, horizontalDistance, verticalDiff);
        int centerX = context.getScaledWindowWidth() / 2;
        int y = context.getScaledWindowHeight() / 2 + 14;
        int textWidth = client.textRenderer.getWidth(line);
        context.drawTextWithShadow(client.textRenderer, line, centerX - textWidth / 2, y, Theme.ACCENT);
    }

    private Optional<Waypoint> nearestInDimension(String dimension, Vec3d playerPos) {
        List<Waypoint> all = WaypointStore.getAll();
        return all.stream()
                .filter(w -> dimension.equals(w.dimension))
                .min(Comparator.comparingDouble(w -> {
                    double dx = w.x - playerPos.x;
                    double dz = w.z - playerPos.z;
                    return dx * dx + dz * dz;
                }));
    }
}
