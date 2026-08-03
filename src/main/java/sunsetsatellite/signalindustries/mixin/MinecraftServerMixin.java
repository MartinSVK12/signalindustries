package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.save.ISaveFormat;
import net.minecraft.core.world.save.LevelStorageServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(value = MinecraftServer.class, remap = false)
public class MinecraftServerMixin {


    @Inject(method = "initWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/Dimension;getDimensionList()Lit/unimi/dsi/fastutil/ints/Int2ObjectMap;", shift = At.Shift.BEFORE))
    private void initWorld(ISaveFormat saveFormat, String worldDirName, long l, CallbackInfo ci, @Local(name = "levelStorage") LevelStorageServer levelStorage) {
        CompoundTag nbt = saveFormat.getLevelDataRaw(worldDirName);
        if(nbt != null){
            SignalIndustries.worldSavedIDs = nbt.containsKey("SISavedIDs");
        }
    }

}
