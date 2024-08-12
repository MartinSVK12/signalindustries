package sunsetsatellite.signalindustries.mixin.experimental;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.chunk.provider.IChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.experimental.ChunkProviderDynamic;

@Mixin(value = WorldRenderer.class,remap = false)
public class WorldRendererMixin {

    @Shadow public Minecraft mc;

    @Inject(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getChunkProvider()Lnet/minecraft/core/world/chunk/provider/IChunkProvider;", shift = At.Shift.AFTER))
    public void switchProvider(float partialTick, long updateRenderersUntil, CallbackInfo ci, @Local(name = "viewEntityX") double viewEntityX, @Local(name = "viewEntityZ") double viewEntityZ){
        IChunkProvider iChunkProvider = mc.theWorld.getChunkProvider();
        if(iChunkProvider instanceof ChunkProviderDynamic)
        {
            ChunkProviderDynamic chunkProviderStatic = (ChunkProviderDynamic)iChunkProvider;
            int chunkX = MathHelper.floor_float((int)viewEntityX) >> 4;
            int chunkZ = MathHelper.floor_float((int)viewEntityZ) >> 4;
            chunkProviderStatic.setCurrentChunkOver(chunkX, chunkZ);
        }
    }
}
