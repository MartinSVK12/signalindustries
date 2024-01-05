package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.save.DimensionData;
import net.minecraft.core.world.save.ISaveFormat;
import net.minecraft.core.world.save.LevelStorage;
import net.minecraft.core.world.save.SaveHandlerBase;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.List;

@Debug(export = true)
@Mixin(value = SaveHandlerBase.class,remap = false)
public abstract class SaveHandlerBaseMixin implements LevelStorage {

    @Shadow @Final private ISaveFormat saveFormat;

    @Shadow @Final private String worldDirName;

    @Inject(method = "getDimensionData", at = @At("HEAD"))
    public void getDimensionData(int dimensionId, CallbackInfoReturnable<DimensionData> cir) {
        CompoundTag data = saveFormat.getDimensionDataRaw(worldDirName, dimensionId);
        if(data != null){
            CompoundTag meteorLocations = data.getCompound("MeteorLocations");
            SignalIndustries.meteorLocations.clear();
            for (Tag<?> value : meteorLocations.getValues()) {
                if(value instanceof CompoundTag){
                    CompoundTag compoundTag = (CompoundTag) value;
                    ChunkCoordinates coordinates = new ChunkCoordinates(compoundTag.getInteger("x"),compoundTag.getInteger("y"),compoundTag.getInteger("z"));
                    SignalIndustries.meteorLocations.add(coordinates);
                }
            }
        }
    }

    @Inject(method = "saveDimensionDataRaw", at = @At("HEAD"))
    public void saveDimensionDataRaw(int dimensionId, CompoundTag dimensionDataTag, CallbackInfo ci) {
        CompoundTag meteorNbt = new CompoundTag();
        List<ChunkCoordinates> meteorLocations = SignalIndustries.meteorLocations;
        for (int i = 0; i < meteorLocations.size(); i++) {
            ChunkCoordinates meteorLocation = meteorLocations.get(i);
            CompoundTag locationNbt = new CompoundTag();
            locationNbt.putInt("x",meteorLocation.x);
            locationNbt.putInt("y",meteorLocation.y);
            locationNbt.putInt("z",meteorLocation.z);
            meteorNbt.putCompound(String.valueOf(i),locationNbt);

        }
        dimensionDataTag.putCompound("MeteorLocations",meteorNbt);
    }
}
