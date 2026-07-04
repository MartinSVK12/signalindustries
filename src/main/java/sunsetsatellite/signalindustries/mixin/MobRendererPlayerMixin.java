package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.MobRenderer;
import net.minecraft.client.render.entity.MobRendererPlayer;
import net.minecraft.client.render.model.ModelBase;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.IArmorItem;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.base.ItemArmorTiered;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.items.attachments.ItemSuitColorizer;

@Mixin(value = MobRendererPlayer.class, remap = false)
public abstract class MobRendererPlayerMixin extends MobRenderer<Player> {
    @Shadow
    private ModelBiped modelBipedMain;

    @Shadow
    @Final
    private ModelBiped modelArmor;

    private MobRendererPlayerMixin(ModelBase model, float shadowSize) {
        super(model, shadowSize);
    }

    @Inject(method = "prepareArmor(Lnet/minecraft/core/entity/player/Player;IF)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/MobRendererPlayer;bindTexture(Ljava/lang/String;)V", ordinal = 3, shift = At.Shift.AFTER))
    protected void prepareArmor(Player entity, int layer, float partialTick, CallbackInfoReturnable<Boolean> cir, @Local(name = "item") Item item) {
        if (item instanceof ItemArmorTiered) {
            if (item instanceof ItemSignalumPowerHarness) {
                if ((layer == 0 || layer == 1) && entity.inventory.armorItemInSlot(2) != null && entity.inventory.armorItemInSlot(2).getData().getBoolean("active_shield")) {
                    if (entity.inventory.armorItemInSlot(3) != null && entity.inventory.armorItemInSlot(3).getItem() instanceof ItemSignalumPowerHarness) {
                        bindTexture("/assets/signalindustries/textures/armor/harness_shield_1.png");
                    } else {
                        bindTexture("/assets/signalindustries/textures/armor/harness_shield_no_goggles_1.png");
                    }
                }
            } else if (item instanceof ItemSignalumPowerSuit) {
                IPowerSuit powerSuit = ((IPlayerPowerSuit<?>) entity).getPowerSuit();
                if (powerSuit != null) {
                    if (powerSuit.hasAttachment(SIItems.awakenedAbilityModule)) {
                        ItemStack stack = powerSuit.getAttachment(SIItems.awakenedAbilityModule);
                        if (stack != null) {
                            bindTexture("/assets/signalindustries/textures/armor/power_suit_awakened" + "_" + (layer != 2 ? 1 : 2) + ".png");
                        }
                    }
                    if (powerSuit.hasAttachmentClass(ItemSuitColorizer.class)) {
                        ItemStack stack = powerSuit.getAttachmentClass(ItemSuitColorizer.class);
                        if (stack != null) {
                            ItemSuitColorizer suitColorizer = (ItemSuitColorizer) stack.getItem();
                            bindTexture(suitColorizer.path + "_" + (layer != 2 ? 1 : 2) + ".png");
                        }
                    }
                }
            }
        }
    }

    @Inject(
            method = "renderAdditional(Lnet/minecraft/core/entity/player/Player;F)V",
            at = @At("HEAD")
    )
    protected void renderAdditional(Player player, float partialTick, CallbackInfo ci) {
        IPowerSuit powerSuit = ((IPlayerPowerSuit<?>) player).getPowerSuit();
        if (powerSuit != null) {
            for (ItemStack content : powerSuit.getArmorPiece(IArmorItem.PIECE_HEAD).contents) {
                if (content != null) {
                    GL11.glPushMatrix();
                    ((ItemAttachment) content.getItem()).renderWhenAttached(player, powerSuit, modelBipedMain, content);
                    GL11.glPopMatrix();
                }
            }
            for (ItemStack content : powerSuit.getArmorPiece(IArmorItem.PIECE_CHEST).contents) {
                if (content != null) {
                    GL11.glPushMatrix();
                    ((ItemAttachment) content.getItem()).renderWhenAttached(player, powerSuit, modelBipedMain, content);
                    GL11.glPopMatrix();
                }
            }
            for (ItemStack content : powerSuit.getArmorPiece(IArmorItem.PIECE_LEGS).contents) {
                if (content != null) {
                    GL11.glPushMatrix();
                    ((ItemAttachment) content.getItem()).renderWhenAttached(player, powerSuit, modelBipedMain, content);
                    GL11.glPopMatrix();
                }
            }
            for (ItemStack content : powerSuit.getArmorPiece(IArmorItem.PIECE_BOOTS).contents) {
                if (content != null) {
                    GL11.glPushMatrix();
                    ((ItemAttachment) content.getItem()).renderWhenAttached(player, powerSuit, modelBipedMain, content);
                    GL11.glPopMatrix();
                }
            }
        }
    }
}
