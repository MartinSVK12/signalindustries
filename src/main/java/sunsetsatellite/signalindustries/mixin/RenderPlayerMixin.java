package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.items.ItemArmorTiered;
import sunsetsatellite.signalindustries.items.ItemSignalumPrototypeHarness;

@Debug(
        export = true
)
@Mixin(
        value = RenderPlayer.class,
        remap = false
)
public class RenderPlayerMixin extends RenderLiving {

    @Shadow @Final private static String[] armorFilenamePrefix;

    @Shadow private ModelBiped modelArmorChestplate;

    @Shadow private ModelBiped modelArmor;

    public RenderPlayerMixin(ModelBase modelbase, float f) {
        super(modelbase, f);
    }

    @Inject(
            method = "setArmorModel",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/src/ItemStack;getItem()Lnet/minecraft/src/Item;", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true)
    protected void setArmorModel(EntityPlayer entityplayer, int i, float f, CallbackInfoReturnable<Boolean> cir, ItemStack itemstack) {
        Item item = itemstack.getItem();
        if(item instanceof ItemArmorTiered){
            ItemArmor itemarmor = (ItemArmor)item;
            this.loadTexture("/assets/signalindustries/armor/" + armorFilenamePrefix[itemarmor.material.renderIndex] + "_" + (i != 2 ? 1 : 2) + ".png");
            ModelBiped modelbiped = i != 2 ? this.modelArmorChestplate : this.modelArmor;
            modelbiped.bipedHead.showModel = i == 0;
            modelbiped.bipedHeadwear.showModel = i == 0;
            modelbiped.bipedBody.showModel = i == 1 || i == 2;
            modelbiped.bipedRightArm.showModel = i == 1;
            modelbiped.bipedLeftArm.showModel = i == 1;
            modelbiped.bipedRightLeg.showModel = i == 2 || i == 3;
            modelbiped.bipedLeftLeg.showModel = i == 2 || i == 3;
            this.setRenderPassModel(modelbiped);
            cir.setReturnValue(true);
        }
    }
}
