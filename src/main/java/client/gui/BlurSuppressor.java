package client.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;

import java.lang.reflect.Field;

/**
 * Neutralizes vanilla's own menu-background-blur option (if the running
 * Minecraft version has one) while {@link ClickGui} is open.
 *
 * <p>The exact 1.21 build this project compiles against has no such option
 * at all (checked directly against the decompiled game classes) -- but
 * Mojang added a "Menu Background Blurriness" setting in a later point
 * release, and {@code fabric.mod.json} accepts any {@code ~1.21} patch
 * version, so a player could be running a Minecraft version where it does
 * exist. Rather than hard-coding a field name that would only be correct
 * for one specific patch (and silently do nothing, or fail to compile, on
 * any other), this looks up any {@link SimpleOption} field on
 * {@link GameOptions} whose name contains "blur" and drives it directly.
 * On a version with no such field, every call here is a harmless no-op.
 */
final class BlurSuppressor {
    private BlurSuppressor() {
    }

    private static boolean searched;
    private static Field blurField;
    private static Object previousValue;

    static void suppress() {
        Field field = findBlurField();
        if (field == null) {
            return;
        }
        try {
            SimpleOption<Object> option = optionFrom(field);
            previousValue = option.getValue();
            option.setValue(minValue(option, previousValue));
        } catch (ReflectiveOperationException | RuntimeException e) {
            previousValue = null;
        }
    }

    static void restore() {
        Field field = findBlurField();
        if (field == null || previousValue == null) {
            return;
        }
        try {
            optionFrom(field).setValue(previousValue);
        } catch (ReflectiveOperationException | RuntimeException e) {
            // Nothing sensible to do if restoring fails; leaving the option at its suppressed
            // value is a cosmetic issue at worst, not worth crashing over.
        } finally {
            previousValue = null;
        }
    }

    @SuppressWarnings("unchecked")
    private static SimpleOption<Object> optionFrom(Field field) throws ReflectiveOperationException {
        return (SimpleOption<Object>) field.get(MinecraftClient.getInstance().options);
    }

    /** Zero for a numeric blur amount, false for a boolean toggle -- covers both plausible shapes. */
    private static Object minValue(SimpleOption<Object> option, Object current) {
        if (current instanceof Double) {
            return 0.0;
        }
        if (current instanceof Integer) {
            return 0;
        }
        if (current instanceof Boolean) {
            return Boolean.FALSE;
        }
        return current;
    }

    private static Field findBlurField() {
        if (searched) {
            return blurField;
        }
        searched = true;
        for (Field field : GameOptions.class.getDeclaredFields()) {
            if (SimpleOption.class.isAssignableFrom(field.getType())
                    && field.getName().toLowerCase().contains("blur")) {
                field.setAccessible(true);
                blurField = field;
                break;
            }
        }
        return blurField;
    }
}
