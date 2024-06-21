package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IFluidIO;
import sunsetsatellite.catalyst.core.util.IItemIO;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.util.IOPreview;

public class BlockModelIOPreview extends BlockModelStandard<Block> {

    public IconCoordinate input = TextureRegistry.getTexture("signalindustries:block/input_overlay");
    public IconCoordinate output = TextureRegistry.getTexture("signalindustries:block/output_overlay");
    public IconCoordinate both = TextureRegistry.getTexture("signalindustries:block/both_io_overlay");

    public BlockModelIOPreview(Block block) {
        super(block);
    }

    @Override
    public IconCoordinate getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntity tileEntity = blockAccess.getBlockTileEntity(x,y,z);
        if(tileEntity instanceof IHasIOPreview) {
            if (((IHasIOPreview) tileEntity).getPreview() != IOPreview.NONE) {
                switch (((IHasIOPreview) tileEntity).getPreview()){
                    case ITEM:
                        if(tileEntity instanceof IItemIO){
                            Connection io = ((IItemIO) tileEntity).getItemIOForSide(Direction.getDirectionFromSide(side));
                            switch (io){
                                case INPUT:
                                    return input;
                                case OUTPUT:
                                    return output;
                                case BOTH:
                                    return both;
                                case NONE:
                                    return null;
                            }
                        }
                        break;
                    case FLUID:
                        if(tileEntity instanceof IFluidIO){
                            Connection io = ((IFluidIO) tileEntity).getFluidIOForSide(Direction.getDirectionFromSide(side));
                            switch (io){
                                case INPUT:
                                    return input;
                                case OUTPUT:
                                    return output;
                                case BOTH:
                                    return both;
                                case NONE:
                                    return null;
                            }
                        }
                        break;
                }
            }
        }
        return null;
    }
}
