package sunsetsatellite.signalindustries.mixin;

//TODO: Remake world gen

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
    }
}

/*import net.minecraft.core.block.Block;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.provider.IChunkProvider;
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

    @Shadow protected World world;

    @Inject(method = "populate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/src/World;getRandomSeed()J", shift = At.Shift.AFTER, ordinal = 1),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void populateMeteor(IChunkProvider ichunkprovider, int chunkX, int chunkZ, CallbackInfo ci, int x, int z, BiomeGenBase biomegenbase, long l1, long l2) {
        Random random = new Random();
        if(random.nextInt(512) == 0){
            int y = this.world.getHeightValue(x,z) - 4;
            new WorldGenMeteor(Block.oreIronBasalt.id,0,25).generate(world,random,x,y,z);
        }
        if(random.nextInt(1024) == 0){
            int y = this.world.getHeightValue(x,z) - 4;
            new WorldGenMeteor(SignalIndustries.signalumOre.id,0,15).generate(world,random,x,y,z);
        }
        if(random.nextInt(2048) == 0){
            int y = this.world.getHeightValue(x,z) - 4;
            new WorldGenMeteor(SignalIndustries.dilithiumOre.id,0,3).generate(world,random,x,y,z);
        }
    }
}*/
