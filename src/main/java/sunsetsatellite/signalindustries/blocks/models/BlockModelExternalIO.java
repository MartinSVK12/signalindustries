package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.Block;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicMachine;
import sunsetsatellite.signalindustries.tiles.TileEntityExternalIO;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockModelExternalIO extends BlockModelStandard<BlockLogicMachine> {

    public IconCoordinate input = TextureRegistry.getTexture("signalindustries:block/external_io_input");
    public IconCoordinate output = TextureRegistry.getTexture("signalindustries:block/external_io_output");
    public IconCoordinate both = TextureRegistry.getTexture("signalindustries:block/external_io_both");
    public IconCoordinate blank = TextureRegistry.getTexture("signalindustries:block/external_io_blank");

    public IconCoordinate reinforcedInput = TextureRegistry.getTexture("signalindustries:block/reinforced_external_io_input");
    public IconCoordinate reinforcedOutput = TextureRegistry.getTexture("signalindustries:block/reinforced_external_io_output");
    public IconCoordinate reinforcedBoth = TextureRegistry.getTexture("signalindustries:block/reinforced_external_io_both");
    public IconCoordinate reinforcedBlank = TextureRegistry.getTexture("signalindustries:block/reinforced_external_io_blank");

    public BlockModelExternalIO(Block<BlockLogicMachine> block) {
        super(block);

        if (block.getLogic().tier == Tier.BASIC) {
            setTex(0, blank, Side.TOP);
            setTex(0, blank, Side.BOTTOM);
            setTex(0, blank, Side.NORTH);
            setTex(0, blank, Side.SOUTH);
            setTex(0, blank, Side.EAST);
            setTex(0, blank, Side.WEST);
        } else {
            setTex(0, reinforcedBlank, Side.TOP);
            setTex(0, reinforcedBlank, Side.BOTTOM);
            setTex(0, reinforcedBlank, Side.NORTH);
            setTex(0, reinforcedBlank, Side.SOUTH);
            setTex(0, reinforcedBlank, Side.EAST);
            setTex(0, reinforcedBlank, Side.WEST);
        }
    }

    @Override
    public IconCoordinate getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        TileEntityExternalIO tile = (TileEntityExternalIO) blockAccess.getTileEntity(x, y, z);
        Connection connection = tile.itemConnections.get(Direction.getDirectionFromSide(side.getId()));
        if (block.getLogic().tier == Tier.BASIC) {
            if (connection == Connection.INPUT) {
                return input;
            } else if (connection == Connection.OUTPUT) {
                return output;
            } else if (connection == Connection.BOTH) {
                return both;
            } else {
                return blank;
            }
        } else if (block.getLogic().tier == Tier.REINFORCED) {
            if (connection == Connection.INPUT) {
                return reinforcedInput;
            } else if (connection == Connection.OUTPUT) {
                return reinforcedOutput;
            } else if (connection == Connection.BOTH) {
                return reinforcedBoth;
            } else {
                return reinforcedBlank;
            }
        }
        return super.getBlockTexture(blockAccess, x, y, z, side);
    }
}
