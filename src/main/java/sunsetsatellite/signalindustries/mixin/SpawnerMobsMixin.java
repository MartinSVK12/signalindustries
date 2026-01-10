package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.entity.SpawnListEntry;
import net.minecraft.core.enums.MobCategory;
import net.minecraft.core.world.SpawnerMobs;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import sunsetsatellite.signalindustries.SIWeather;
import sunsetsatellite.signalindustries.entities.MobInfernal;

import java.util.Collections;
import java.util.List;

@Mixin(value = SpawnerMobs.class, remap = false)
public class SpawnerMobsMixin {

    @Unique
    private static final List<SpawnListEntry> infernals = Collections.singletonList(new SpawnListEntry(MobInfernal.class, 50));

    @WrapOperation(method = "performSpawning", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/biome/Biome;getSpawnableList(Lnet/minecraft/core/enums/MobCategory;)Ljava/util/List;"))
    private static List<SpawnListEntry> eclipseSpawning(Biome instance, MobCategory creatureType, Operation<List<SpawnListEntry>> original, World world) {
        if (creatureType == MobCategory.monster && world.getCurrentWeather() == SIWeather.weatherEclipse) {
            return infernals;
        }
        return original.call(instance, creatureType);
    }
}
