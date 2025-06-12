/**
 * Base class for toggleable modules.
 * Handles enabling and disabling logic so child classes only need
 * to implement their specific behavior.
 */
package client.modules;

public abstract class ModuleBase implements Module {
    /** Indicates whether the module is currently active. */
    private boolean enabled = false;

    @Override
    /**
     * Toggles the active state of this module and calls the appropriate hooks.
     */
    public void toggle() {
        // Flip the enabled flag
        enabled = !enabled;
        // Invoke hooks depending on the new state
        if (enabled) {
            // Trigger enable callback
            onEnable();
        } else {
            // Trigger disable callback
            onDisable();
        }
    }

    @Override
    /**
     * Returns whether the module is currently enabled.
     */
    public boolean isEnabled() {
        // Expose the internal enabled flag
        return enabled;
    }

    /**
     * Called when the module is enabled.
     * Subclasses should override to implement behavior.
     */
    protected void onEnable() {}
    /**
     * Called when the module is disabled.
     * Subclasses should override to undo their enable actions.
     */
    protected void onDisable() {}
}
