package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.material.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;

@Mixin(
        value = BlockFluid.class,
        remap = false
)
public abstract class BlockFluidMixin extends Block {

    @Unique
    private final BlockFluid thisAs = (BlockFluid) ((Object)this);

    private BlockFluidMixin(String key, int id, Material material) {
        super(key, id, material);
    }



    @Inject(method = "getRenderBlockPass",at = @At("HEAD"), cancellable = true)
    public void getRenderBlockPass(CallbackInfoReturnable<Integer> cir) {
        if(thisAs == SignalIndustries.energyFlowing || thisAs == SignalIndustries.energyStill){
            cir.setReturnValue(1);
        }
    }
}
