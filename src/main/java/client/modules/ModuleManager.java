/**
 * Manages all feature modules registered by the client.
 * Provides simple registration and retrieval of modules used in-game.
 */
package client.modules;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    /** Collection of all registered modules. */
    private final List<Module> modules = new ArrayList<>();

    /**
     * Registers a module so it can be managed and toggled.
     */
    public void register(Module module) {
        // Store the module in the internal list
        modules.add(module);
    }

    /**
     * Returns an immutable list of all registered modules.
     */
    public List<Module> getModules() {
        // Provide the caller with the list of modules
        return modules;
    }
}
