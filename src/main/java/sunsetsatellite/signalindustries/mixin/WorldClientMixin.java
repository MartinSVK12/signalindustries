package sunsetsatellite.signalindustries.mixin;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.camera.ICamera;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
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
        value = WorldClient.class,
        remap = false
)
public abstract class WorldClientMixin extends World {

	@Shadow
	@Final
	private @NotNull Minecraft mc;

	private WorldClientMixin(@NotNull World parent, @NotNull Dimension dimension) {
		super(parent, dimension);
	}

	@Inject(
            method = "getSkyColor",
            at = @At("HEAD"),
            cancellable = true)
    public void getSkyColor(ICamera camera, float partialTick, CallbackInfoReturnable<Vector3fc> cir) {
        if (getCurrentWeather() == SIWeather.weatherEclipse) {
            cir.setReturnValue(new Vector3f(0,0,0));
        } else if (dimension == SIDimensions.ETERNITY) {
			cir.setReturnValue(new Vector3f(0.7f,0.7f,0.7f));
        }
    }

	@Override
	public void tick() {
		if (FabricLoaderImpl.INSTANCE.isModLoaded("vintagequesting")) {
			if (mc.thePlayer.getStat(SIAchievements.HELP) == 0) {
				mc.thePlayer.triggerAchievement(SIAchievements.HELP);
			}
		}
	}
}
