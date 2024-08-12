package sunsetsatellite.signalindustries.mixin.experimental;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.EntityPlayerSP;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.chunk.IChunkLoader;
import net.minecraft.core.world.chunk.provider.IChunkProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.experimental.ChunkProviderDynamic;

@Mixin(value = Minecraft.class,remap = false)
public abstract class MinecraftMixin {

    @Shadow public World theWorld;

    @Shadow public EntityPlayerSP thePlayer;

    @Inject(method = "createChunkProvider",at = @At("HEAD"),cancellable = true)
    public void switchProvider(World world, IChunkLoader chunkLoader, CallbackInfoReturnable<IChunkProvider> cir){
        if(SignalIndustries.config.getBoolean("Experimental.enableDynamicChunkProvider")){
            cir.setReturnValue(new ChunkProviderDynamic(world,chunkLoader,world.getWorldType().createChunkGenerator(world)));
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getChunkProvider()Lnet/minecraft/core/world/chunk/provider/IChunkProvider;"))
    public void SwitchProvider2(CallbackInfo ci){
        IChunkProvider chunkProvider = theWorld.getChunkProvider();
        if(chunkProvider instanceof ChunkProviderDynamic)
        {
            ChunkProviderDynamic chunkProviderDynamic = (ChunkProviderDynamic)chunkProvider;
            int playerChunkX = MathHelper.floor_float((int)thePlayer.x) >> 4;
            int playerChunkZ = MathHelper.floor_float((int)thePlayer.z) >> 4;
            chunkProviderDynamic.setCurrentChunkOver(playerChunkX, playerChunkZ);
        }
    }

    @Inject(method = "changeWorld(Lnet/minecraft/core/world/World;Ljava/lang/String;Lnet/minecraft/core/entity/player/EntityPlayer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getChunkProvider()Lnet/minecraft/core/world/chunk/provider/IChunkProvider;"))
    public void SwitchProvider3(CallbackInfo ci){
        IChunkProvider chunkProvider = theWorld.getChunkProvider();
        if(chunkProvider instanceof ChunkProviderDynamic)
        {
            ChunkProviderDynamic chunkProviderDynamic = (ChunkProviderDynamic)chunkProvider;
            int playerChunkX = MathHelper.floor_float((int)thePlayer.x) >> 4;
            int playerChunkZ = MathHelper.floor_float((int)thePlayer.z) >> 4;
            chunkProviderDynamic.setCurrentChunkOver(playerChunkX, playerChunkZ);
        }
    }

    @Inject(method = "func_6255_d", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getChunkProvider()Lnet/minecraft/core/world/chunk/provider/IChunkProvider;"))
    public void SwitchProvider4(CallbackInfo ci){
        ChunkCoordinates chunkcoordinates = theWorld.getSpawnPoint();
        IChunkProvider iChunkProvider = theWorld.getChunkProvider();
        if(iChunkProvider instanceof ChunkProviderDynamic)
        {
            ChunkProviderDynamic chunkProvider = (ChunkProviderDynamic)iChunkProvider;
            chunkProvider.setCurrentChunkOver(Math.floorDiv(chunkcoordinates.x, Chunk.CHUNK_SIZE_X), Math.floorDiv(chunkcoordinates.z, Chunk.CHUNK_SIZE_Z));
        }
    }

    @Inject(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;getChunkProvider()Lnet/minecraft/core/world/chunk/provider/IChunkProvider;"))
    public void SwitchProvider(CallbackInfo ci, @Local(name = "bedSpawnCoordinates") ChunkCoordinates bedSpawnCoordinates){
        IChunkProvider iChunkProvider = theWorld.getChunkProvider();
        if(iChunkProvider instanceof ChunkProviderDynamic)
        {
            ChunkProviderDynamic chunkProviderDynamic = (ChunkProviderDynamic)iChunkProvider;
            chunkProviderDynamic.setCurrentChunkOver(bedSpawnCoordinates.x >> 4, bedSpawnCoordinates.z >> 4);
        }
    }
}
