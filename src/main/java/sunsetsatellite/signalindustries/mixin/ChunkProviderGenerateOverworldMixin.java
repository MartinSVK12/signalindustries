package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.worldgen.WorldGenMeteor;

import java.util.Random;

@Debug(
        export = true
)
@Mixin(
        value = ChunkProviderGenerateOverworld.class,
        remap = false
)
public abstract class ChunkProviderGenerateOverworldMixin implements IChunkProvider {

    @Shadow protected int terrainMaxHeight;

    @Shadow protected World worldObj;

    @Inject(method = "populate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getRandomSeed()J", shift = At.Shift.AFTER, ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void populateMeteor(IChunkProvider ichunkprovider, int chunkX, int chunkZ, CallbackInfo ci, int x, int z, BiomeGenBase biomegenbase, long l1, long l2) {
        Random random = new Random();
        if(random.nextInt(512) == 0){
            int y = this.worldObj.getHeightValue(x,z) - 4;
            new WorldGenMeteor(Block.oreIronBasalt.blockID,0,25).generate(worldObj,random,x,y,z);
        }
        if(random.nextInt(1024) == 0){
            int y = this.worldObj.getHeightValue(x,z) - 4;
            new WorldGenMeteor(SignalIndustries.signalumOre.blockID,0,15).generate(worldObj,random,x,y,z);
        }
        if(random.nextInt(2048) == 0){
            int y = this.worldObj.getHeightValue(x,z) - 4;
            new WorldGenMeteor(SignalIndustries.dilithiumOre.blockID,0,3).generate(worldObj,random,x,y,z);
        }
    }
}
