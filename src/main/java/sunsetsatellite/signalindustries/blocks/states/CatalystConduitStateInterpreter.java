package sunsetsatellite.signalindustries.blocks.states;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.data.block.mojang.state.MetaStateInterpreter;
import sunsetsatellite.catalyst.CatalystEnergy;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.energy.simple.api.IEnergyContainer;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicCatalystConduit;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicConduit;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicFluidConduit;

import java.util.HashMap;

public class CatalystConduitStateInterpreter extends MetaStateInterpreter {
    @Override
    public HashMap<String, String> getStateMap(WorldSource worldSource, int i, int j, int k, Block<?> block, int meta) {
        HashMap<String, String> states = new HashMap<>();
        for (Direction direction : Direction.values()) {
            boolean show = false;
            Vec3i offset = new Vec3i(i, j, k).add(direction.getVec());
            Block<?> neighbouringBlock = worldSource.getBlock(offset.x, offset.y, offset.z);
            if (neighbouringBlock != null) {
                if (block.getLogic().getClass().isAssignableFrom(neighbouringBlock.getLogic().getClass())) {
                    show = true;
                } else if (!(neighbouringBlock.getLogic() instanceof BlockLogicCatalystConduit)) {
                    if (neighbouringBlock.isEntityTile) {
                        TileEntity neighbouringTile = worldSource.getTileEntity(offset.x, offset.y, offset.z);
                        if (neighbouringTile instanceof IEnergyContainer) {
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
