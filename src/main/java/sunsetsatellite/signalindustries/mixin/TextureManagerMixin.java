package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.render.DynamicTextureMeteorTracker;

import java.util.Collection;

@Mixin(
        value = TextureManager.class,
        remap = false
)
public abstract class TextureManagerMixin {

    @Shadow @Final public Minecraft mc;

    @Shadow protected abstract void addDynamicTexture(DynamicTexture texture);

    @Inject(
            method = "initDynamicTextures",
            at = @At(value = "TAIL")
    )
    public void initSIDynamicTextures(Collection<? super Throwable> errors, CallbackInfo ci) {
        SignalIndustries.LOGGER.info("Loading dynamic textures..");
        addDynamicTexture(new DynamicTextureMeteorTracker(mc, TextureRegistry.getTexture("signalindustries:item/meteor_tracker")));
    }
}
