package client;

/**
 * Central color palette for Nyte Client's UI.
 *
 * <p>Every screen and HUD element should pull its colors from here instead
 * of hard-coding hex values, so the whole UI stays visually consistent and
 * a palette swap only has to happen in one place. See {@code THEME.md} in
 * the repo root for the full style guide (what each token is for, and the
 * contrast reasoning behind it).
 */
public final class Theme {
    private Theme() {
    }

    /** Full-screen dim behind an open panel. */
    public static final int BACKGROUND = 0xC80D0B12;

    /** Panel body fill. */
    public static final int PANEL = 0xF0191622;
    /** Panel header strip and hovered-row fill. */
    public static final int PANEL_HEADER = 0xFF241F30;
    /** 1px panel outline. */
    public static final int BORDER = 0x33FFFFFF;

    /** Brand accent, used for the selected tab and active highlights. */
    public static final int ACCENT = 0xFF8A5CFF;
    public static final int ACCENT_HOVER = 0xFFA57BFF;
    /** Low-opacity accent for "selected but not focused" states. */
    public static final int ACCENT_MUTED = 0x668A5CFF;

    /** Module-enabled indicator. */
    public static final int ENABLED = 0xFF4CE0B3;
    /** Module-disabled indicator. */
    public static final int DISABLED = 0xFF4A4658;
    /** Reserved for destructive/warning UI, not currently used by a module. */
    public static final int DANGER = 0xFFFF5C7A;

    public static final int TEXT_PRIMARY = 0xFFF2F0FA;
    public static final int TEXT_MUTED = 0xFF9D97B5;
    public static final int TEXT_ON_ACCENT = 0xFF0D0B14;
}
