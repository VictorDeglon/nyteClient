package client;

/**
 * Central color palette for Nyte Client's UI, with three switchable presets.
 *
 * <p>Every screen and HUD element pulls its colors from these static fields
 * instead of hard-coding hex values, so the whole UI restyles at once when
 * {@link #setPreset(Preset)} runs (the ClickGUI header's swatch button
 * calls it). See {@code THEME.md} for the full palette guide and the
 * reasoning behind each preset.
 */
public final class Theme {
    private Theme() {
    }

    public enum Preset {
        VIOLET("Violet"),
        CRIMSON("Crimson"),
        TEAL("Teal");

        private final String label;

        Preset(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private static Preset current = Preset.VIOLET;

    public static int BACKGROUND;
    public static int PANEL;
    public static int PANEL_HEADER;
    public static int BORDER;
    public static int ACCENT;
    public static int ACCENT_HOVER;
    public static int ACCENT_MUTED;
    public static int ENABLED;
    public static int DISABLED;
    public static int DANGER;
    public static int TEXT_PRIMARY;
    public static int TEXT_MUTED;
    public static int TEXT_ON_ACCENT;

    static {
        apply(current);
    }

    public static Preset getPreset() {
        return current;
    }

    public static void setPreset(Preset preset) {
        current = preset;
        apply(preset);
    }

    /** Cycles to the next preset in declaration order, wrapping around. */
    public static void cyclePreset() {
        Preset[] values = Preset.values();
        setPreset(values[(current.ordinal() + 1) % values.length]);
    }

    private static void apply(Preset preset) {
        // BACKGROUND is intentionally close to fully opaque (0xF0, matching
        // PANEL) rather than strongly translucent: with the world still
        // animating behind the ClickGUI (see ClickGui#shouldPause), a very
        // see-through overlay reads as a motion blur rather than a dim.
        switch (preset) {
            case CRIMSON -> {
                BACKGROUND = 0xF0120A0D;
                PANEL = 0xF0221419;
                PANEL_HEADER = 0xFF301B22;
                BORDER = 0x33FFFFFF;
                ACCENT = 0xFFFF5C7A;
                ACCENT_HOVER = 0xFFFF7C93;
                ACCENT_MUTED = 0x66FF5C7A;
                ENABLED = 0xFF4CE0B3;
                DISABLED = 0xFF5A4650;
                DANGER = 0xFFFF3355;
                TEXT_PRIMARY = 0xFFFAF0F2;
                TEXT_MUTED = 0xFFB5979D;
                TEXT_ON_ACCENT = 0xFF140B0D;
            }
            case TEAL -> {
                BACKGROUND = 0xF00A1212;
                PANEL = 0xF0142120;
                PANEL_HEADER = 0xFF1B302D;
                BORDER = 0x33FFFFFF;
                ACCENT = 0xFF3ED9B8;
                ACCENT_HOVER = 0xFF63E6CB;
                ACCENT_MUTED = 0x663ED9B8;
                ENABLED = 0xFF9AE04C;
                DISABLED = 0xFF46605A;
                DANGER = 0xFFFF5C7A;
                TEXT_PRIMARY = 0xFFF0FAF7;
                TEXT_MUTED = 0xFF97B5AD;
                TEXT_ON_ACCENT = 0xFF0B1413;
            }
            case VIOLET -> {
                BACKGROUND = 0xF00D0B12;
                PANEL = 0xF0191622;
                PANEL_HEADER = 0xFF241F30;
                BORDER = 0x33FFFFFF;
                ACCENT = 0xFF8A5CFF;
                ACCENT_HOVER = 0xFFA57BFF;
                ACCENT_MUTED = 0x668A5CFF;
                ENABLED = 0xFF4CE0B3;
                DISABLED = 0xFF4A4658;
                DANGER = 0xFFFF5C7A;
                TEXT_PRIMARY = 0xFFF2F0FA;
                TEXT_MUTED = 0xFF9D97B5;
                TEXT_ON_ACCENT = 0xFF0D0B14;
            }
        }
    }
}
