package sunsetsatellite.signalindustries.blocks.states;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;
import sunsetsatellite.catalyst.CatalystEnergy;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.energy.api.IEnergy;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockCatalystConduit;
import sunsetsatellite.signalindustries.blocks.BlockConduit;
import sunsetsatellite.signalindustries.blocks.BlockFluidConduit;

import java.util.HashMap;

public class CatalystConduitStateInterpreter extends MetaStateInterpreter {
    @Override
    public HashMap<String, String> getStateMap(WorldSource worldSource, int i, int j, int k, Block block, int meta) { HashMap<String, String> states = new HashMap<>();
        for (Direction direction : Direction.values()) {
            boolean show = false;
            Vec3i offset = new Vec3i(i,j,k).add(direction.getVec());
            Block neighbouringBlock = worldSource.getBlock(offset.x, offset.y, offset.z);
            if(neighbouringBlock != null) {
                if(block.getClass().isAssignableFrom(neighbouringBlock.getClass())){
                    show = true;
                } else if(!(neighbouringBlock instanceof BlockCatalystConduit)) {
                    if(neighbouringBlock instanceof BlockTileEntity){
                        TileEntity neighbouringTile = worldSource.getBlockTileEntity(offset.x, offset.y, offset.z);
                        if(neighbouringTile instanceof IEnergy){
                            show = true;
                        } else if (neighbouringBlock.hasTag(CatalystEnergy.ENERGY_CONDUITS_CONNECT)) {
                            show = true;
                        }
                    } else if (neighbouringBlock.hasTag(CatalystEnergy.ENERGY_CONDUITS_CONNECT)) {
                        show = true;
                    }
                }
            }
            states.put(direction.getName().toLowerCase(), String.valueOf(show));
        }
        return states;
    }
}
