package client.modules;

public abstract class ModuleBase implements Module {
    private boolean enabled = false;

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
    public boolean isEnabled() {
        return enabled;
    }

    protected void onEnable() {}
    protected void onDisable() {}
}
