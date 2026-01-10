package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.block.BlockLogicFarmland;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIItems;

@Debug(export = true)
@Mixin(value = BlockLogicFarmland.class, remap = false)
public class BlockLogicFarmlandMixin {

    @Inject(method = "onEntityWalking", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/BlockLogicFarmland;isWet(I)Z", shift = At.Shift.BEFORE), cancellable = true)
    public void onEntityWalking(World world, int x, int y, int z, Entity entity, CallbackInfo ci) {
        if (((Player) entity).inventory.armorInventory[0] != null && ((Player) entity).inventory.armorInventory[0].getItem().equals(SIItems.signalumPowerSuitBoots)) {
            ci.cancel();
        }
    }

}
