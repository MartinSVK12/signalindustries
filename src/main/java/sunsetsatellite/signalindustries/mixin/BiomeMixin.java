package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Global;
import net.minecraft.core.entity.SpawnListEntry;
import net.minecraft.core.enums.EnumCreatureType;
import net.minecraft.core.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIWeather;
import sunsetsatellite.signalindustries.entities.mob.EntityInfernal;

import java.util.Collections;
import java.util.List;

@Mixin(value = Biome.class,remap = false)
public class BiomeMixin {

    @Unique
    private final List<SpawnListEntry> infernals = Collections.singletonList(new SpawnListEntry(EntityInfernal.class,50));

    @Inject(method = "getSpawnableList", at = @At("HEAD"), cancellable = true)
    public void getSpawnableList(EnumCreatureType creatureType, CallbackInfoReturnable<List<SpawnListEntry>> cir) {
        if(Global.isServer || Minecraft.getMinecraft(Minecraft.class).theWorld == null) {
            return;
        }
        if(!Global.isServer && creatureType == EnumCreatureType.monster){
            if(Minecraft.getMinecraft(Minecraft.class).theWorld.getCurrentWeather() == SIWeather.weatherEclipse){
                cir.setReturnValue(infernals);
            }
        }
    }
}
