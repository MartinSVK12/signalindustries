package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(
        value = RenderEngine.class,
        remap = false
)
public abstract class RenderEngineMixin {

    @Shadow private List<DynamicTexture> dynamicTextures;

    @Shadow @Final public Minecraft mc;

    @Shadow public abstract void updateDynamicTexture(DynamicTexture texture);

    @Inject(
            method = "initDynamicTextures",
            at = @At(value = "INVOKE",target = "Ljava/lang/Integer;intValue()I",ordinal = 0,shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void initSIDynamicTextures(CallbackInfo ci, int terrainResolution, int itemsResolution) {
        //TODO: Animated textures????
        /*SignalIndustries.LOGGER.info("Loading dynamic textures..");
        SignalIndustries.LOGGER.info(String.valueOf(Block.texCoordToIndex(SignalIndustries.pumpTex[1][0],SignalIndustries.pumpTex[1][1])));
        DynamicTexture dynTex = new DynamicTextureAnimated(minecraft,"/terrain.png", "/assets/signalindustries/block/prototypepumpanimated.png", Block.texCoordToIndex(SignalIndustries.pumpTex[1][0],SignalIndustries.pumpTex[1][1]), terrainResolution, 2);
        this.dynamicTextures.add(dynTex);
        updateDynamicTexture(dynTex);*/
    }
}
