package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.entity.SpawnListEntry;
import net.minecraft.core.enums.EnumCreatureType;
import net.minecraft.core.world.SpawnerMobs;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.chunk.ChunkCoordIntPair;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.mob.EntityInfernal;

import java.util.Iterator;
import java.util.List;

@Mixin(
        value = SpawnerMobs.class,
        remap = false
)
public class SpawnerMobsMixin {


    @Inject(
            method = "performSpawning",
            at = @At(value = "INVOKE",target = "Ljava/util/List;isEmpty()Z",shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private static void performSpawning(World world, boolean spawnHostileMobs, boolean spawnPassiveMobs, CallbackInfoReturnable<Integer> cir, int playerIndex, ChunkCoordinates spawnChunk, EnumCreatureType[] creatureTypes, int i, EnumCreatureType creatureType, Iterator chunkIt, ChunkCoordIntPair chunk, Biome biome, List spawnableList) {
        if(world.currentWeather == SignalIndustries.weatherEclipse){
            for (Object o : spawnableList) {
                SpawnListEntry entry = (SpawnListEntry) o;
                if(entry.entityClass == EntityInfernal.class) return;
            }
            spawnableList.add(new SpawnListEntry(EntityInfernal.class, 50));
        } else {
            spawnableList.removeIf((S)->{
                SpawnListEntry entry = (SpawnListEntry) S;
                return entry.entityClass == EntityInfernal.class;
            });
        }
    }
}
