package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkLoaderRegionAsync;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(value = ChunkLoaderRegionAsync.class,remap = false)
public class ChunkLoaderRegionAsyncMixin {

	@Inject(method = "saveChunk",at = @At("HEAD"), cancellable = true)
	public void saveCHunk(World world, Chunk chunk, CallbackInfo ci) {
		if(world.dimension.id == SIDimensions.ETERNITY.id && SignalIndustries.DEBUG) ci.cancel();
	}

}
