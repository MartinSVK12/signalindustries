package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.MobRendererBiped;
import net.minecraft.client.render.entity.MobRendererBipedArmored;
import net.minecraft.core.entity.IArmorWearing;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemSuitColorizer;
import sunsetsatellite.signalindustries.items.base.ItemArmorTiered;

@Mixin(value = MobRendererBipedArmored.class, remap = false)
public abstract class MobRendererBipedArmoredMixin<T extends Mob & IArmorWearing<HumanArmorShape>> extends MobRendererBiped<T> {

	private MobRendererBipedArmoredMixin(float shadowSize) {
		super(shadowSize);
	}

		@Inject(method = "getAndSetupModelForLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/MobRendererBipedArmored;bindTexture(Ljava/lang/String;)V", ordinal = 3, shift = At.Shift.AFTER))
	public void getAndSetupModelForLayer(@NotNull T entity, float brightness, float partialTick, int layer, CallbackInfoReturnable<StaticEntityModel> cir, @Local(name = "itemstack") ItemStack itemstack){
		final Item item = itemstack.getItem();
		if (item instanceof ItemArmorTiered && entity instanceof Player player) {
			if (item instanceof ItemSignalumPowerHarness) {
				if ((layer == 0 || layer == 1) && player.inventory.armorItemInSlot(HumanArmorShape.CHEST) != null && player.inventory.armorItemInSlot(HumanArmorShape.CHEST).getData().getBoolean("active_shield")) {
					if (player.inventory.armorItemInSlot(HumanArmorShape.HEAD) != null && player.inventory.armorItemInSlot(HumanArmorShape.HEAD).getItem() instanceof ItemSignalumPowerHarness) {
						bindTexture("/assets/signalindustries/textures/armor/harness_shield_1.png");
					} else {
						bindTexture("/assets/signalindustries/textures/armor/harness_shield_no_goggles_1.png");
					}
				}
			} else if (item instanceof ItemSignalumPowerSuit) {
				IPowerSuit powerSuit = ((IPlayerPowerSuit<?>) player).getPowerSuit();
				if (powerSuit != null) {
					if (powerSuit.hasAttachment(SIItems.awakenedAbilityModule)) {
						ItemStack stack = powerSuit.getAttachment(SIItems.awakenedAbilityModule);
						if (stack != null) {
							bindTexture("/assets/signalindustries/textures/armor/power_suit_awakened" + "_" + (layer != 3 ? 1 : 2) + ".png");
						}
					}
					if (powerSuit.hasAttachmentClass(ItemSuitColorizer.class)) {
						ItemStack stack = powerSuit.getAttachmentClass(ItemSuitColorizer.class);
						if (stack != null) {
							ItemSuitColorizer suitColorizer = (ItemSuitColorizer) stack.getItem();
							bindTexture(suitColorizer.path + "_" + (layer != 3 ? 1 : 2) + ".png");
						}
					}
				}
			}
		}
	}

}
