package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
        value={RenderBlocks.class},
        remap = false
)

public class RenderBlocksMixin {
    @Shadow private IBlockAccess blockAccess;

    @Inject(
            method = "renderBlockByRenderType",
            at = @At("TAIL"),
            cancellable = true
    )
    void renderBlockByRenderType(Block block, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        //TODO: Add conduits here
        /*if(block.blockID == EnergyAPI.wire.blockID){
            cir.setReturnValue(RenderWire.render((RenderBlocks) ((Object)this),this.blockAccess,i,j,k,block,0));
        }*/
    }
}
