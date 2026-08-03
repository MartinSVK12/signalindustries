package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.CompoundTag;
import com.mojang.nbt.tags.Tag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.render.worldtype.WorldTypeFXDispatcher;
import net.minecraft.core.data.registry.Registries;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.type.WorldType;
import net.minecraft.core.world.type.WorldTypeGroups;
import net.minecraft.core.world.type.WorldTypes;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.mixins.IMutableDimensionListAccess;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.KeyboardHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {

	@Shadow
	public PlayerLocal thePlayer;

	@Shadow
	@Final
	private @NotNull File mcDataDir;

	@Inject(
		method = "runTick",
		at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;next()Z", shift = At.Shift.AFTER)
	)
	public void handleKeyboard(CallbackInfo ci) {
		KeyboardHandler.handleKeyboard((Minecraft) (Object) this, ci);
	}

	@ModifyExpressionValue(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/player/PlayerLocal;hasNoPhysics()Z"))
	public boolean modifyWingsFlightSpeed(boolean original) {
		SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>) thePlayer).getPowerSuit();
		if (ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)) {
			return original || ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active");
		}
		return original;
	}

	@ModifyExpressionValue(method = "runTick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/Minecraft;toggleFlyPressed:Z"))
	public boolean modifyWingsFlightSpeed2(boolean original) {
		boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
		SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>) thePlayer).getPowerSuit();
		if (ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)) {
			return original || (ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active") && control);
		}
		return original;
	}

	@Inject(method = "startWorld(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/lang/System;gc()V"))
	public void startWorld(String worldDirName, CallbackInfo ci) {
		try {
			Map<Integer, Dimension> dimensionMap = ((IMutableDimensionListAccess) Dimension.OVERWORLD).getMutableDimensionList();
			//todo: might not be the best idea, probably remove them when player exists the level (with the save & quit button)
			/*dimensionMap.forEach((id, dim) -> {
				if (dim instanceof DimensionCustom) {
					for (BiomeCustom biome : ((DimensionCustom) dim).data.biomes) {
						Registries.BIOMES.unregister(SignalIndustries.key(biome.translationKey.replace("biome.","")));
					}
				}
			});
			dimensionMap.entrySet().removeIf(entry -> entry.getValue() instanceof DimensionCustom);*/

			/*Iterator<WorldType> iter = Registries.WORLD_TYPES.iterator();
			while (iter.hasNext()) {
				WorldType worldType = iter.next();
				if (worldType instanceof WorldTypeWrapper) {
					iter.remove();
				}
			}*/
			File saveFile = new File(this.mcDataDir, "saves/" + worldDirName);
			File worldLevelDat = new File(saveFile, "level.dat");
			if (worldLevelDat.exists()) {
				CompoundTag nbt = NbtIo.readCompressed(Files.newInputStream(worldLevelDat.toPath())).getCompound("Data");
				CompoundTag dimensionsTag = nbt.getCompound("CustomDimensions");
				SignalIndustries.worldSavedIDs = nbt.containsKey("SISavedIDs");
				/*for (Tag<?> tag : dimensionsTag.getValues()) {
					if (tag instanceof CompoundTag) {
						CompoundTag dimTag = (CompoundTag) tag;
						CustomDimensionData data = new CustomDimensionData(dimTag);
						WorldTypes.register(SignalIndustries.key("custom/" + data.name), data.getWorldType());
						WorldTypeFXDispatcher.getInstance().addDispatch(data.getWorldType(), data.properties.worldTypeFX);
						DimensionCustom dim = new DimensionCustom(data);
						Dimension.registerDimension(data.id, dim);
					}
				}*/
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
