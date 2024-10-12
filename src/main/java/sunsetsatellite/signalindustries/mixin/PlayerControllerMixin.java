package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.Side;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.signalindustries.commands.StructMakerCommand;

@Mixin(value = PlayerController.class,remap = false)
public class PlayerControllerMixin {
    @Shadow @Final protected Minecraft mc;

    @Inject(method = "destroyBlock", at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/core/world/World;playSoundEffect(IIIII)V"))
    public void destroyBlock(int x, int y, int z, Side side, EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
        if(StructMakerCommand.autoAddRemove){
            StructMakerCommand.internalRemoveBlock(mc, new Vec3i(x,y,z));
        }
    }
}
