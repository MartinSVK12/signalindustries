package sunsetsatellite.signalindustries.mixin;

import de.jcm.discordgamesdk.activity.Activity;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.launch.FabricLauncher;
import net.fabricmc.loader.impl.launch.FabricLauncherBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.discord.RichPresenceHandlerThread;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.weather.Weather;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.Map;

@Mixin(value = RichPresenceHandlerThread.class, remap = false)
public class RichPresenceHandlerThreadMixin {

    @Shadow private static Map<Biome, String> biomeNamesMap;

    @Shadow private static Map<Dimension, String> dimensionNamesMap;

    @Inject(method = "<init>",at = @At("TAIL"))
    public void init(Minecraft mc, CallbackInfo ci){
        dimensionNamesMap.put(SignalIndustries.dimEternity,"a barren realm");
        biomeNamesMap.put(SignalIndustries.biomeEternity,"the edge of reality");
    }

}
