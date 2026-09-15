package client.modules;

/**
 * A roadmap entry: shows up in the ClickGUI so the planned feature set is
 * visible and organized by category, but can't be toggled and does
 * nothing. {@code toggle()} is intentionally a no-op rather than delegating
 * to {@link ModuleBase} -- there's no on/off state to announce for
 * something that isn't built yet.
 */
public class PlaceholderModule implements Module {
    private final String name;
    private final String description;
    private final Category category;

    public PlaceholderModule(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public void toggle() {
        // Not implemented yet -- see ROADMAP.md.
    }

    @Override
    public void onTick() {
    }

    @Override
    public boolean isPlaceholder() {
        return true;
    }
}
