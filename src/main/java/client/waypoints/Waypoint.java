package client.waypoints;

/**
 * A named world location: a manual marker or the automatic "Last Death"
 * entry. Plain data holder, serialized directly by Gson in
 * {@link WaypointStore} -- field names double as the JSON keys.
 */
public class Waypoint {
    public String name;
    public double x;
    public double y;
    public double z;
    /** Dimension registry id, e.g. "minecraft:overworld" -- waypoints only show up in their own dimension. */
    public String dimension;
    /** True for the auto-managed "Last Death" entry; there's at most one at a time. */
    public boolean autoDeath;

    /** No-args constructor required by Gson for deserialization. */
    public Waypoint() {
    }

    public Waypoint(String name, double x, double y, double z, String dimension, boolean autoDeath) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dimension = dimension;
        this.autoDeath = autoDeath;
    }
}
