package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.discord.RichPresenceHandlerThread;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIBiomes;
import sunsetsatellite.signalindustries.SIDimensions;

import java.util.Map;

@Mixin(value = RichPresenceHandlerThread.class, remap = false)
public class RichPresenceHandlerThreadMixin {

    @Shadow private static Map<Biome, String> biomeNamesMap;

    @Shadow private static Map<Dimension, String> dimensionNamesMap;

    @Inject(method = "<init>",at = @At("TAIL"))
    public void init(Minecraft mc, CallbackInfo ci){
        dimensionNamesMap.put(SIDimensions.dimEternity,"a barren realm");
        biomeNamesMap.put(SIBiomes.biomeEternity,"the edge of reality");
    }

}
