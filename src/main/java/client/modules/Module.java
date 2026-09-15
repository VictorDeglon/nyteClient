package client.modules;

/**
 * Contract every feature module implements so {@link ModuleManager} and the
 * ClickGUI can treat them uniformly regardless of what the module does.
 */
public interface Module {
    String getName();

    /** Short blurb shown as a tooltip in the ClickGUI. */
    String getDescription();

    Category getCategory();

    boolean isEnabled();

    void toggle();

    /** Called once per client tick while the module is enabled. */
    void onTick();
}
