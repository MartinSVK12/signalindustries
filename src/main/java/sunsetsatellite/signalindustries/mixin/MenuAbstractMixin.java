package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.RecipeBuilder;

@Mixin(value = MenuAbstract.class, remap = false)
public abstract class MenuAbstractMixin {
	@Inject(method = "setItem", at = @At("HEAD"))
	public void setItem(int i, ItemStack itemstack, CallbackInfo ci){
		if(!EnvironmentHelper.isMultiplayerServer() && itemstack != null){
			if(Catalyst.listContains(RecipeBuilder.getItemGroup(SignalIndustries.MOD_ID, "rom_chips"),itemstack, ItemStack::isItemEqual)){
				Minecraft.getMinecraft().thePlayer.triggerAchievement(SIAchievements.ROM_CHIP);
			}
		}
	}
}
