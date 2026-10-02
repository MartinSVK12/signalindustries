package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MenuAbstract.class, remap = false)
public abstract class MenuAbstractMixinCommon {

	@Shadow
	public abstract @Nullable Slot getSlot(int i);

	@Shadow
	public abstract void slotsChanged(Container iinventory);


	@Inject(method = "clicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/player/inventory/menu/MenuAbstract;getSlot(I)Lnet/minecraft/core/player/inventory/slot/Slot;", ordinal = 0), cancellable = true)
	public void clicked(InventoryAction action, int[] args, Player player, CallbackInfoReturnable<ItemStack> cir) {
		int slotId = args[0];
		Slot slot = getSlot(slotId);
		if(slot != null && slot.isLocked()){
			slotsChanged(player.inventory);
			cir.setReturnValue(slot.getItemStack());
		}
	}

}
