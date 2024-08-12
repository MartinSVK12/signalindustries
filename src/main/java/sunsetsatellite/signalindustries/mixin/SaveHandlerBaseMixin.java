package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.Tag;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import net.minecraft.core.world.save.DimensionData;
import net.minecraft.core.world.save.ISaveFormat;
import net.minecraft.core.world.save.LevelStorage;
import net.minecraft.core.world.save.SaveHandlerBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.MeteorLocation;

import java.util.List;
import java.util.Objects;


@Mixin(value = SaveHandlerBase.class,remap = false)
public abstract class SaveHandlerBaseMixin implements LevelStorage {

    @Shadow @Final
    ISaveFormat saveFormat;

    @Shadow @Final
    String worldDirName;

    @Inject(method = "getDimensionData", at = @At("HEAD"))
    public void getDimensionData(int dimensionId, CallbackInfoReturnable<DimensionData> cir) {
        CompoundTag data = saveFormat.getDimensionDataRaw(worldDirName, dimensionId);
        if(data != null){
            CompoundTag meteorLocations = data.getCompound("MeteorLocations");
            CompoundTag chunkloaders = data.getCompound("ChunkLoaders");
            SignalIndustries.meteorLocations.clear();
            SignalIndustries.chunkLoaders.clear();
            for (Tag<?> value : meteorLocations.getValues()) {
                if(value instanceof CompoundTag){
                    CompoundTag compoundTag = (CompoundTag) value;
                    ChunkCoordinates coordinates = new ChunkCoordinates(compoundTag.getInteger("x"),compoundTag.getInteger("y"),compoundTag.getInteger("z"));
                    SignalIndustries.meteorLocations.add(new MeteorLocation(MeteorLocation.Type.valueOf(Objects.equals(compoundTag.getString("type"), "") ? "UNKNOWN" : compoundTag.getString("type")),coordinates));
                }
            }
            for (Tag<?> value : chunkloaders.getValues()) {
                if(value instanceof CompoundTag){
                    CompoundTag compoundTag = (CompoundTag) value;
                    ChunkCoordinates coordinates = new ChunkCoordinates(compoundTag.getInteger("x"),compoundTag.getInteger("y"),compoundTag.getInteger("z"));
                    SignalIndustries.chunkLoaders.add(coordinates);
                }
            }
        }
    }

    @Inject(method = "saveDimensionDataRaw", at = @At("HEAD"))
    public void saveDimensionDataRaw(int dimensionId, CompoundTag dimensionDataTag, CallbackInfo ci) {
        CompoundTag meteorNbt = new CompoundTag();
        CompoundTag chunkloaderNbt = new CompoundTag();
        List<MeteorLocation> meteorLocations = SignalIndustries.meteorLocations;
        for (int i = 0; i < meteorLocations.size(); i++) {
            ChunkCoordinates meteorLocation = meteorLocations.get(i).location;
            CompoundTag locationNbt = new CompoundTag();
            locationNbt.putInt("x",meteorLocation.x);
            locationNbt.putInt("y",meteorLocation.y);
            locationNbt.putInt("z",meteorLocation.z);
            locationNbt.putString("type",meteorLocations.get(i).type.name());
            meteorNbt.putCompound(String.valueOf(i),locationNbt);
        }
        List<ChunkCoordinates> chunkLoaders = SignalIndustries.chunkLoaders;
        for (int i = 0; i < chunkLoaders.size(); i++) {
            ChunkCoordinates chunkLoader = chunkLoaders.get(i);
            CompoundTag chunkNbt = new CompoundTag();
            chunkNbt.putInt("x", chunkLoader.x);
            chunkNbt.putInt("y", chunkLoader.y);
            chunkNbt.putInt("z", chunkLoader.z);
            chunkloaderNbt.putCompound(String.valueOf(i),chunkNbt);
        }
        dimensionDataTag.putCompound("MeteorLocations",meteorNbt);
        dimensionDataTag.putCompound("ChunkLoaders",chunkloaderNbt);
    }
}
