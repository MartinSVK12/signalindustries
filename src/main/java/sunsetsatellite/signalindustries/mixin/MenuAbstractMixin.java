package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.RecipeBuilder;

@Mixin(value = MenuAbstract.class, remap = false)
public class MenuAbstractMixin {

	@Inject(method = "setItem", at = @At("HEAD"))
	public void setItem(int i, ItemStack itemstack, CallbackInfo ci){
		if(!EnvironmentHelper.isMultiplayerServer()){
			if(Catalyst.listContains(RecipeBuilder.getItemGroup(SignalIndustries.MOD_ID, "rom_chips"),itemstack, ItemStack::isItemEqual)){
				Minecraft.getMinecraft().thePlayer.triggerAchievement(SIAchievements.ROM_CHIP);
			}
		}
	}

}
