package sunsetsatellite.signalindustries.mixin;


import net.minecraft.client.render.RenderBlocks;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.util.RenderConduit;

@Mixin(
        value={RenderBlocks.class},
        remap = false
)

public class RenderBlocksMixin {
    @Shadow private WorldSource blockAccess;

    @Shadow private World world;

    @Inject(
            method = "renderBlockByRenderType",
            at = @At("HEAD"),
            cancellable = true
    )
    void renderBlockByRenderType(Block block, int renderType, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        TileEntity tile = world.getBlockTileEntity(x,y,z);
        if(tile instanceof TileEntityFluidPipe){
            cir.setReturnValue(RenderConduit.render((RenderBlocks) ((Object)this),this.blockAccess,x,y,z,block,0));
        }
        /* else if (block.getRenderType() == 33) {
            cir.setReturnValue(RenderItemPipe.render((RenderBlocks) ((Object)this),this.blockAccess,x,y,z,block,0));

        }*/
    }
}
