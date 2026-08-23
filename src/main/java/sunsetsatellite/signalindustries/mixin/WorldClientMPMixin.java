package sunsetsatellite.signalindustries.mixin;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.world.WorldClient;
import net.minecraft.client.world.WorldClientMP;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.save.LevelStorage;
import net.minecraft.core.world.settings.WorldConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIWeather;

@Mixin(
        value = WorldClientMP.class,
        remap = false
)
public abstract class WorldClientMPMixin extends WorldClient {

	private WorldClientMPMixin(@NotNull Minecraft mc, @NotNull LevelStorage levelStorage, @Nullable WorldConfiguration worldConfiguration, @NotNull Dimension dimension) {
		super(mc, levelStorage, worldConfiguration, dimension);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci){
		if (FabricLoaderImpl.INSTANCE.isModLoaded("vintagequesting")) {
			if (Minecraft.getMinecraft().thePlayer.getStat(SIAchievements.HELP) == 0) {
				Minecraft.getMinecraft().thePlayer.triggerAchievement(SIAchievements.HELP);
			}
		}
	}
}
