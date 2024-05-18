package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.client.render.texturepack.TexturePack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.render.DynamicTextureMeteorTracker;

import java.util.List;

@Mixin(
        value = RenderEngine.class,
        remap = false
)
public abstract class RenderEngineMixin {

    @Shadow private List<DynamicTexture> dynamicTextures;

    @Shadow @Final public Minecraft mc;


    @Inject(
            method = "initDynamicTextures",
            at = @At(value = "TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void initSIDynamicTextures(List<Throwable> errors, CallbackInfo ci) {
        SignalIndustries.LOGGER.info("Loading dynamic textures..");
        //dynamicTextures.add(new DynamicTextureMeteorTracker(mc));
    }
}
