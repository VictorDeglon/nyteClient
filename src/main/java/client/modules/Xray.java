package client.modules;

import client.Theme;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Periodically scans already-loaded blocks around the player for valuable
 * ores and lists the closest few on the HUD (name, distance, coordinates).
 *
 * <p>This is a radar, not a see-through-walls renderer: it only reads block
 * data the client already has for currently loaded chunks (the server sent
 * it that data the moment the chunk loaded), so it needs no world seed and
 * no render-pipeline tricks. Finding ores in chunks you haven't loaded yet
 * would require reconstructing world generation from the seed -- a much
 * bigger, separate project, not what this does.
 */
public class Xray extends ModuleBase implements HudRenderCallback {
    private static final Map<Block, String> TARGETS = new LinkedHashMap<>();

    static {
        TARGETS.put(Blocks.DIAMOND_ORE, "Diamond");
        TARGETS.put(Blocks.DEEPSLATE_DIAMOND_ORE, "Diamond");
        TARGETS.put(Blocks.ANCIENT_DEBRIS, "Ancient Debris");
        TARGETS.put(Blocks.EMERALD_ORE, "Emerald");
        TARGETS.put(Blocks.DEEPSLATE_EMERALD_ORE, "Emerald");
    }

    /** Half-width of the cube scanned around the player, in blocks. */
    private static final int SCAN_RADIUS = 16;
    /** Rescans this often rather than every tick -- a full cube scan isn't free. */
    private static final int SCAN_INTERVAL_TICKS = 40;
    private static final int MAX_RESULTS = 5;

    private int ticksSinceScan = SCAN_INTERVAL_TICKS;
    private List<String> lines = List.of();

    public Xray() {
        super("Xray", "Radar for nearby loaded ore blocks. Not a wallhack, needs no seed.", Category.RENDER);
        HudRenderCallback.EVENT.register(this);
    }

    @Override
    protected void onDisable() {
        lines = List.of();
    }

    @Override
    public void onTick() {
        if (++ticksSinceScan < SCAN_INTERVAL_TICKS) {
            return;
        }
        ticksSinceScan = 0;
        scan();
    }

    private void scan() {
        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) {
            lines = List.of();
            return;
        }

        Vec3d playerPos = player.getPos();
        BlockPos origin = player.getBlockPos();
        List<BlockPos> found = new ArrayList<>();

        for (int dx = -SCAN_RADIUS; dx <= SCAN_RADIUS; dx++) {
            for (int dy = -SCAN_RADIUS; dy <= SCAN_RADIUS; dy++) {
                for (int dz = -SCAN_RADIUS; dz <= SCAN_RADIUS; dz++) {
                    BlockPos pos = origin.add(dx, dy, dz);
                    if (TARGETS.containsKey(client.world.getBlockState(pos).getBlock())) {
                        found.add(pos.toImmutable());
                    }
                }
            }
        }

        found.sort(Comparator.comparingDouble(pos -> pos.getSquaredDistance(playerPos.x, playerPos.y, playerPos.z)));

        List<String> result = new ArrayList<>();
        int limit = Math.min(MAX_RESULTS, found.size());
        for (int i = 0; i < limit; i++) {
            BlockPos pos = found.get(i);
            String name = TARGETS.get(client.world.getBlockState(pos).getBlock());
            double distance = Math.sqrt(pos.getSquaredDistance(playerPos.x, playerPos.y, playerPos.z));
            result.add(String.format("%s - %.0fm (%d, %d, %d)", name, distance, pos.getX(), pos.getY(), pos.getZ()));
        }
        lines = result;
    }

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        if (!isEnabled() || lines.isEmpty()) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        int y = 18;
        for (String line : lines) {
            drawContext.drawTextWithShadow(client.textRenderer, line, 4, y, Theme.ACCENT);
            y += 10;
        }
    }
}
