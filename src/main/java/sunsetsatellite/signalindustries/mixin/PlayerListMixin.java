package sunsetsatellite.signalindustries.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.entity.player.PlayerServer;
import net.minecraft.server.net.PlayerList;
import net.minecraft.server.net.handler.PacketHandlerLogin;
import net.minecraft.server.world.ServerPlayerController;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(value = PlayerList.class,remap = false)
public class PlayerListMixin {

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "getPlayerForLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/net/handler/PacketHandlerServer;kickPlayer(Ljava/lang/String;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void getPlayerForLogin(PacketHandlerLogin handler, String username, UUID uuid, CallbackInfoReturnable<PlayerServer> cir){
        cir.setReturnValue(new PlayerServer(server, server.getDimensionWorld(0), username, uuid, new ServerPlayerController(server.getDimensionWorld(0))));
    }

}
