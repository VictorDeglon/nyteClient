/**
 * Base interface for all feature modules.
 * Defines the methods required for module management.
 */
package client.modules;

public interface Module {
    /**
     * @return human readable name for the module.
     */
    String getName();
    /**
     * Toggles the module on or off.
     */
    void toggle();
    /**
     * @return true if the module is currently enabled.
     */
    boolean isEnabled();
}
