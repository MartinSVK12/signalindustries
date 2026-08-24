package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.save.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.dim.custom.DimensionCustom;
import sunsetsatellite.signalindustries.util.MeteorLocation;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Mixin(value = LevelStorageBase.class, remap = false)
public abstract class LevelStorageBaseMixin implements LevelStorage {

    @Shadow
    @Final
    protected ISaveFormat saveFormat;

    @Shadow
    @Final
    protected String worldDirName;

    @Inject(method = "getDimensionData", at = @At("HEAD"))
    public void getDimensionData(Dimension dimension, CallbackInfoReturnable<DimensionData> cir) {
        CompoundTag data = saveFormat.getDimensionDataRaw(worldDirName, dimension);
        if (data != null) {
            CompoundTag meteorLocations = data.getCompound("MeteorLocations");
            //CompoundTag chunkloaders = data.getCompound("ChunkLoaders");
            SignalIndustries.meteorLocations.clear();
            //SignalIndustries.chunkLoaders.clear();
            for (Tag<?> value : meteorLocations.getValues()) {
                if (value instanceof CompoundTag compoundTag) {
					Vec3i coordinates = new Vec3i(compoundTag.getInteger("x"), compoundTag.getInteger("y"), compoundTag.getInteger("z"));
                    SignalIndustries.meteorLocations.add(new MeteorLocation(MeteorLocation.Type.valueOf(Objects.equals(compoundTag.getString("type"), "") ? "UNKNOWN" : compoundTag.getString("type")), coordinates));
                }
            }
            /*for (Tag<?> value : chunkloaders.getValues()) {
                if (value instanceof CompoundTag) {
                    CompoundTag compoundTag = (CompoundTag) value;
                    ChunkCoordinates coordinates = new ChunkCoordinates(compoundTag.getInteger("x"), compoundTag.getInteger("y"), compoundTag.getInteger("z"));
                    SignalIndustries.chunkLoaders.add(coordinates);
                }
            }*/
        }
    }

    @Inject(method = "saveDimensionDataRaw", at = @At("HEAD"))
    public void saveDimensionDataRaw(int dimensionId, CompoundTag dimensionDataTag, CallbackInfo ci) {
        CompoundTag meteorNbt = new CompoundTag();
        //CompoundTag chunkloaderNbt = new CompoundTag();
        List<MeteorLocation> meteorLocations = SignalIndustries.meteorLocations;
        for (int i = 0; i < meteorLocations.size(); i++) {
            Vec3i meteorLocation = meteorLocations.get(i).location;
            CompoundTag locationNbt = new CompoundTag();
            locationNbt.putInt("x", meteorLocation.x);
            locationNbt.putInt("y", meteorLocation.y);
            locationNbt.putInt("z", meteorLocation.z);
            locationNbt.putString("type", meteorLocations.get(i).type.name());
            meteorNbt.putCompound(String.valueOf(i), locationNbt);
        }
       /* List<ChunkCoordinates> chunkLoaders = SignalIndustries.chunkLoaders;
        for (int i = 0; i < chunkLoaders.size(); i++) {
            ChunkCoordinates chunkLoader = chunkLoaders.get(i);
            CompoundTag chunkNbt = new CompoundTag();
            chunkNbt.putInt("x", chunkLoader.x);
            chunkNbt.putInt("y", chunkLoader.y);
            chunkNbt.putInt("z", chunkLoader.z);
            chunkloaderNbt.putCompound(String.valueOf(i), chunkNbt);
        }*/
        dimensionDataTag.putCompound("MeteorLocations", meteorNbt);
        //dimensionDataTag.putCompound("ChunkLoaders", chunkloaderNbt);
    }

	@Inject(method = "saveLevelDataAndPlayerData", at = @At(value = "NEW", target = "()Lcom/mojang/nbt/tags/CompoundTag;", ordinal = 1))
    public void saveLevelDataAndPlayerData(LevelData levelData, List<Player> playerList, CallbackInfo ci, @Local(name = "dataTag") CompoundTag dataTag) {
        CompoundTag dimensionsTag = new CompoundTag();
        for (Map.Entry<Integer, Dimension> dimensionEntry : Dimension.getDimensionList().int2ObjectEntrySet()) {
            Dimension d = dimensionEntry.getValue();
            CompoundTag dimensionTag = new CompoundTag();
            if (d instanceof DimensionCustom dim) {
				dim.data.writeToNbt(dimensionTag);
                dimensionsTag.putCompound(String.valueOf(dimensionEntry.getKey()), dimensionTag);
            }
        }
        if(!SignalIndustries.worldSavedIDs){
            SignalIndustries.worldSavedIDs = true;
            CompoundTag nbt = new CompoundTag();
            try {
                List<Field> blockFields = Arrays.stream(SIBlocks.class.getDeclaredFields()).filter((F) -> Block.class.isAssignableFrom(F.getType())).toList();
                List<Field> itemFields = Arrays.stream(SIItems.class.getDeclaredFields()).filter((F) -> Item.class.isAssignableFrom(F.getType())).toList();
                for (Field field : itemFields) {
					if(field.get(null) == null) continue;
                    nbt.putInt(field.getName(),((Item) field.get(null)).id);
                }
                for (Field field : blockFields) {
					if(field.get(null) == null) continue;
                    nbt.putInt(field.getName(),((Block<?>) field.get(null)).id());
                }
            } catch (Exception e){
                e.printStackTrace();
            }

            dataTag.putCompound("SISavedIDs", nbt);
        }
        dataTag.putCompound("CustomDimensions", dimensionsTag);
    }
}
