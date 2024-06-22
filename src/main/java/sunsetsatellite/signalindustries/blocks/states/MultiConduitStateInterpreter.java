package sunsetsatellite.signalindustries.blocks.states;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IConduitBlock;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.signalindustries.blocks.BlockMultiConduit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.inventories.TileEntityMultiConduit;

import java.util.HashMap;

public class MultiConduitStateInterpreter extends MetaStateInterpreter {
    @Override
    public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block block, int meta) {
        HashMap<String, String> result = new HashMap<>();
        TileEntity tile = worldSource.getBlockTileEntity(x, y, z);
        if (tile instanceof TileEntityMultiConduit) {
            TileEntityMultiConduit conduit = (TileEntityMultiConduit) tile;
            result.put("conduit_1_type", conduit.conduits[0] == null ? "none" : conduit.conduits[0].getConduitCapability().name().toLowerCase());
            result.put("conduit_2_type", conduit.conduits[1] == null ? "none" : conduit.conduits[1].getConduitCapability().name().toLowerCase());
            result.put("conduit_3_type", conduit.conduits[2] == null ? "none" : conduit.conduits[2].getConduitCapability().name().toLowerCase());
            result.put("conduit_4_type", conduit.conduits[3] == null ? "none" : conduit.conduits[3].getConduitCapability().name().toLowerCase());
            result.put("conduit_1_tier", conduit.conduits[0] instanceof ITiered ? ((ITiered) conduit.conduits[0]).getTier().name().toLowerCase() : "none");
            result.put("conduit_2_tier", conduit.conduits[1] instanceof ITiered ? ((ITiered) conduit.conduits[1]).getTier().name().toLowerCase() : "none");
            result.put("conduit_3_tier", conduit.conduits[2] instanceof ITiered ? ((ITiered) conduit.conduits[2]).getTier().name().toLowerCase() : "none");
            result.put("conduit_4_tier", conduit.conduits[3] instanceof ITiered ? ((ITiered) conduit.conduits[3]).getTier().name().toLowerCase() : "none");
            Vec3i pos = new Vec3i(x, y, z);
            int connected = 0;
            boolean split = false;
            for (Direction dir : Direction.values()) {
                Block connectedBlock = dir.getBlock(worldSource, pos);
                if (connectedBlock instanceof BlockMultiConduit) {
                    connected++;
                    Direction side = Direction.getDirectionFromSide(worldSource.getBlockMetadata(x, y, z));
                    if (side != dir && side != dir.getOpposite()) {
                        split = true;
                        break;
                    }
                }
                if (connectedBlock instanceof IConduitBlock) {
                    split = true;
                    break;
                }
            }
            result.put("split", split || connected > 2 ? "true" : "false");
        } else {
            result.put("conduit_1_type", "none");
            result.put("conduit_2_type", "none");
            result.put("conduit_3_type", "none");
            result.put("conduit_4_type", "none");
            result.put("conduit_1_tier", "none");
            result.put("conduit_2_tier", "none");
            result.put("conduit_3_tier", "none");
            result.put("conduit_4_tier", "none");
            result.put("split", "false");
        }
        result.put("axis", Side.getSideById(worldSource.getBlockMetadata(x, y, z)).getAxis().name().toLowerCase());
        return result;
    }
}
