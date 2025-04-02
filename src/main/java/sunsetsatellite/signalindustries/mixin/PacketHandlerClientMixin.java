package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.net.handler.PacketHandlerClient;
import net.minecraft.core.net.packet.PacketLogin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(value = PacketHandlerClient.class,remap = false)
public class PacketHandlerClientMixin {

    @Inject(method = "handleLogin", at = @At("TAIL"))
    public void handleLogin(PacketLogin loginPacket, CallbackInfo ci){
        SignalIndustries.meteorLocations.clear();
    }
}
