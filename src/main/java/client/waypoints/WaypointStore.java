package client.waypoints;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads/saves waypoints as JSON under {@code config/nyteclient/waypoints.json}.
 * Gson is already on the classpath as one of Minecraft's own library
 * dependencies (verified against the resolved build classpath before
 * writing this), so this doesn't add a new dependency. Kept as a static
 * in-memory list plus best-effort disk persistence -- a corrupt or missing
 * file just starts from an empty list rather than crashing the game.
 */
public final class WaypointStore {
    private WaypointStore() {
    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LIST_TYPE = new TypeToken<ArrayList<Waypoint>>() {
    }.getType();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir()
            .resolve("nyteclient").resolve("waypoints.json");

    private static final List<Waypoint> waypoints = load();

    public static List<Waypoint> getAll() {
        return waypoints;
    }

    public static void add(Waypoint waypoint) {
        waypoints.add(waypoint);
        save();
    }

    public static void remove(Waypoint waypoint) {
        waypoints.remove(waypoint);
        save();
    }

    /** Replaces whatever the previous auto-death entry was with a new one. */
    public static void setDeathWaypoint(Waypoint waypoint) {
        waypoints.removeIf(w -> w.autoDeath);
        waypoints.add(waypoint);
        save();
    }

    private static List<Waypoint> load() {
        try {
            if (Files.exists(FILE)) {
                String json = Files.readString(FILE, StandardCharsets.UTF_8);
                List<Waypoint> loaded = GSON.fromJson(json, LIST_TYPE);
                if (loaded != null) {
                    return loaded;
                }
            }
        } catch (IOException | RuntimeException e) {
            // Corrupt or unreadable file: start fresh rather than crash the game.
        }
        return new ArrayList<>();
    }

    private static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(waypoints), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Best-effort persistence -- losing a save on a write failure isn't fatal.
        }
    }
}
