package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.perlin.overworld.hell.ChunkDecoratorOverworldHell;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIConfig;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureGeode;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureMeteor;
import sunsetsatellite.signalindustries.worldgen.WorldFeatureObelisk;

import java.util.Random;

@Mixin(value = ChunkDecoratorOverworldHell.class, remap = false)
@Debug(export = true)
public class ChunkDecoratorOverworldHellMixin {

    @Shadow
    @Final
    private World world;

    @Inject(method = "decorate", at = @At(value = "TAIL"))
    public void decorate(Chunk chunk, CallbackInfo ci) {
        int chunkX = chunk.xPosition;
        int chunkZ = chunk.zPosition;

        int minY = world.getWorldType().getMinY();
        int maxY = world.getWorldType().getMaxY();
        int rangeY = (maxY + 1) - minY;

        float oreHeightModifier = rangeY / 128f;
        int x = chunkX * 16;
        int z = chunkZ * 16;
        int y = world.getHeightValue(x + 16, z + 16);
        Random rand = new Random(world.getRandomSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long) chunkX * l1 + (long) chunkZ * l2 ^ world.getRandomSeed());

        if (rand.nextInt(SIConfig.config.getInt("WorldGen.signaliteGeodeChance")) == 0) {
            int i = x + rand.nextInt(16);
            int j = (minY + 12) + rand.nextInt(rangeY / 8);
            int k = z + rand.nextInt(16);
            new WorldFeatureGeode(SIBlocks.signalumOre.id(), 0, 20, 4).place(world, rand, i, j, k);
        }

        if (rand.nextInt(SIConfig.config.getInt("WorldGen.ironMeteorChance")) == 0) {
            int i = x + rand.nextInt(16);
            int k = z + rand.nextInt(16);
            int j = world.getHeightValue(i, k);
            new WorldFeatureMeteor(Blocks.ORE_IRON_BASALT.id(), 0, 25).place(world, rand, i, j, k);
        }

        if (rand.nextInt(SIConfig.config.getInt("WorldGen.signaliteMeteorChance")) == 0) {
            int i = x + rand.nextInt(16);
            int k = z + rand.nextInt(16);
            int j = world.getHeightValue(i, k);
            new WorldFeatureMeteor(SIBlocks.signalumOre.id(), 0, 15).place(world, rand, i, j, k);
        }

        if (rand.nextInt(SIConfig.config.getInt("WorldGen.dilithiumMeteorChance")) == 0) {
            int i = x + rand.nextInt(16);
            int k = z + rand.nextInt(16);
            int j = world.getHeightValue(i, k);
            new WorldFeatureMeteor(SIBlocks.dilithiumOre.id(), 0, 5).place(world, rand, i, j, k);
        }

        if (rand.nextInt(SIConfig.config.getInt("WorldGen.obeliskChance")) == 0) {
            int i = x + rand.nextInt(16);
            int k = z + rand.nextInt(16);
            int j = world.getHeightValue(i, k);
            new WorldFeatureObelisk().place(world, rand, i, j, k);
        }

    }
}
