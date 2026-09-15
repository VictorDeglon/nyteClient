package client.modules;

/**
 * Grouping used to organize modules into ClickGUI tabs.
 */
public enum Category {
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    MISC("Misc");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    /** Display label shown on the GUI's tab strip. */
    public String getLabel() {
        return label;
    }
}
