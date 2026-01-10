package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.net.packet.PacketLogin;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.handler.PacketHandlerLogin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageMeteorLocationSync;
import sunsetsatellite.signalindustries.util.MeteorLocation;
import turniplabs.halplibe.helper.network.NetworkHandler;

@Mixin(value = PacketHandlerLogin.class, remap = false)
public class PacketHandlerLoginMixin {

    @Inject(method = "doLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/handler/PacketHandlerServer;sendPacket(Lnet/minecraft/core/net/packet/Packet;)V", ordinal = 13))
    public void doLogin(PacketLogin loginPacket, CallbackInfo ci, @Local(name = "player") PlayerServer player) {
        for (MeteorLocation location : SignalIndustries.meteorLocations) {
            NetworkHandler.sendToPlayer(player, new NetworkMessageMeteorLocationSync(location));
        }
    }

}
