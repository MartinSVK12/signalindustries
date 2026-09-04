package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.core.net.packet.PacketLogin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(value = PacketHandlerClient.class, remap = false)
public class PacketHandlerClientMixin {

    @Inject(method = "handleLogin", at = @At("TAIL"), order = 900)
    public void handleLogin(PacketLogin packetLogin, CallbackInfo ci) {
		SIRecipes.loaded = false;
        SignalIndustries.meteorLocations.clear();
    }
}
