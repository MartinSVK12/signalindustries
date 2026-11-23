package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.items.tools.ItemSignalumDrill;

@Mixin(value = BlockLogic.class, remap = false)
public class BlockLogicMixin {

	@Inject(method = "onHarvest", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/item/Item;isSilkTouch()Z", shift = At.Shift.BEFORE))
	public void onHarvest(World world, Player player, TilePosc tilePos, int data, TileEntity tileEntity, CallbackInfo ci, @Local(name = "heldItemStack") ItemStack heldItemStack) {
		if(heldItemStack != null && heldItemStack.getItem() instanceof ItemSignalumDrill){
			ItemSignalumDrill.currentStack = heldItemStack;
		}
	}
}
