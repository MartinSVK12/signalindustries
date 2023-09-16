package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkDecorator;
import net.minecraft.core.world.generate.chunk.perlin.overworld.ChunkDecoratorOverworld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureMeteor;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureObelisk;

import java.util.Random;

@Mixin(
        value = ChunkDecoratorOverworld.class,
        remap = false
)
public abstract class ChunkDecoratorOverworldMixin implements ChunkDecorator {

    @Shadow @Final private World world;

    @Inject(method = "decorate",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/core/world/World;getRandomSeed()J",ordinal = 2,shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void decorate(Chunk chunk, CallbackInfo ci, int chunkX, int chunkZ, int minY, int maxY, int rangeY, float oreHeightModifier, int x, int z, int y, Biome biome, Random rand, long l1, long l2) {
        if(rand.nextInt(512) == 0){
            SignalIndustries.LOGGER.info(String.format("Meteor fell at X:%d Z:%d (X:%d Y:%d Z:%d)",chunkX,chunkZ,x,y,z));
            y = this.world.getHeightValue(x,z) - 4;
            new WorldFeatureMeteor(Block.oreIronBasalt.id,0,25).generate(world,rand,x,y,z);
        }
        if(rand.nextInt(1024) == 0){
            SignalIndustries.LOGGER.info(String.format("Meteor fell at X:%d Z:%d (X:%d Y:%d Z:%d)",chunkX,chunkZ,x,y,z));
            y = this.world.getHeightValue(x,z) - 4;
            new WorldFeatureMeteor(SignalIndustries.signalumOre.id,0,15).generate(world,rand,x,y,z);
        }
        if(rand.nextInt(2048) == 0){
            SignalIndustries.LOGGER.info(String.format("Meteor fell at X:%d Z:%d (X:%d Y:%d Z:%d)",chunkX,chunkZ,x,y,z));
            y = this.world.getHeightValue(x,z) - 4;
            new WorldFeatureMeteor(SignalIndustries.dilithiumOre.id,0,3).generate(world,rand,x,y,z);
        }
        if(rand.nextInt(4096) == 0){
            SignalIndustries.LOGGER.info(String.format("Obelisk at X:%d Z:%d (X:%d Y:%d Z:%d)",chunkX,chunkZ,x,y,z));
            y = this.world.getHeightValue(x,z);
            new WorldFeatureObelisk().generate(world,rand,x,y,z);
        }
    }
}
