package client.mixin;

import client.modules.Crosshair;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Suppresses vanilla's own crosshair render pass while the {@link Crosshair}
 * module is enabled, so our themed crosshair (drawn separately via
 * {@link net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback})
 * replaces it instead of drawing on top of it.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void nyteclient$suppressVanillaCrosshair(DrawContext context, RenderTickCounter tickCounter,
            CallbackInfo ci) {
        if (Crosshair.isActive()) {
            ci.cancel();
        }
    }
}
