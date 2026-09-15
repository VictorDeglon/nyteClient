package client.modules;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * Boilerplate for a toggleable module: name/description/category storage,
 * the enabled flag, and enable/disable callback plumbing. Subclasses only
 * need to override {@link #onEnable()}, {@link #onDisable()} and/or
 * {@link #onTick()} for whatever behavior they add.
 *
 * <p>Every toggle also posts an action-bar confirmation ("Flight: ON" in
 * green / "Flight: OFF" in red) -- direct feedback that a click actually
 * registered, independent of whether the module's own effect is easy to
 * see or the server ends up rejecting it.
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
        announceState();
    }

    private void announceState() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) {
            return;
        }
        Text message = Text.literal(name + ": " + (enabled ? "ON" : "OFF"))
                .formatted(enabled ? Formatting.GREEN : Formatting.RED);
        player.sendMessage(message, true);
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
