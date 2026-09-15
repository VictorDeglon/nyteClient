package client.modules;

/**
 * Boilerplate for a toggleable module: name/description/category storage,
 * the enabled flag, and enable/disable callback plumbing. Subclasses only
 * need to override {@link #onEnable()}, {@link #onDisable()} and/or
 * {@link #onTick()} for whatever behavior they add.
 */
public abstract class ModuleBase implements Module {
    private final String name;
    private final String description;
    private final Category category;
    private boolean enabled;

    protected ModuleBase(String name, String description, Category category) {
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
        return enabled;
    }

    @Override
    public void toggle() {
        enabled = !enabled;
        if (enabled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    @Override
    public void onTick() {
        // No-op by default; only modules that need per-tick work override this.
    }

    /** Runs once when the module is switched on. */
    protected void onEnable() {
    }

    /** Runs once when the module is switched off; undo whatever onEnable() changed. */
    protected void onDisable() {
    }
}
