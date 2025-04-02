package sunsetsatellite.signalindustries.blocks.states;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.data.block.mojang.state.MetaStateInterpreter;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.tiles.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.PipeType;

import java.util.HashMap;

public class ItemConduitStateInterpreter extends MetaStateInterpreter {
    @Override
    public HashMap<String, String> getStateMap(WorldSource worldSource, int i, int j, int k, Block<?> block, int meta) {
        HashMap<String, String> states = new HashMap<>();
        /*if(!(worldSource.getTileEntity(i,j,k) instanceof TileEntityItemConduit)){
            return states;
        }*/
        TileEntityItemConduit tile = (TileEntityItemConduit) worldSource.getTileEntity(i, j, k);
        for (Direction direction : Direction.values()) {
            boolean show = false;
            if (tile != null && tile.noConnectDirections.get(direction)) {
                states.put(direction.getName().toLowerCase(), String.valueOf(show));
                continue;
            }
            Vec3i offset = new Vec3i(i, j, k).add(direction.getVec());
            Block<?> neighbouringBlock = worldSource.getBlock(offset.x, offset.y, offset.z);
            if (neighbouringBlock != null) {
                if (block.getLogic().getClass().isAssignableFrom(neighbouringBlock.getLogic().getClass())) {
                    TileEntity neighbouringTile = worldSource.getTileEntity(offset.x, offset.y, offset.z);
                    if (neighbouringTile instanceof TileEntityItemConduit) {
                        if (((TileEntityItemConduit) neighbouringTile).noConnectDirections.get(direction.getOpposite())) {
                            states.put(direction.getName().toLowerCase(), String.valueOf(show));
                            continue;
                        }
                    }
                    show = true;
                } else {
                    if (neighbouringBlock.isEntityTile) {
                        TileEntity neighbouringTile = worldSource.getTileEntity(offset.x, offset.y, offset.z);
                        if (neighbouringTile instanceof Container) {
                            show = true;
                        } else if (neighbouringBlock.hasTag(SignalIndustries.ITEM_CONDUITS_CONNECT)) {
                            show = true;
                        }
                    } else if (neighbouringBlock.hasTag(SignalIndustries.ITEM_CONDUITS_CONNECT)) {
                        show = true;
                    }
                }
            }
            states.put(direction.getName().toLowerCase(), String.valueOf(show));
            if (tile != null) {
                if (tile.type == PipeType.RESTRICT) {
                    states.put("restrict_" + direction.getName().toLowerCase(), String.valueOf(tile.restrictDirections.get(direction)));
                } else if (tile.type == PipeType.SENSOR) {
                    states.put("sensor_active", String.valueOf(tile.sensorActive));

                }
                states.put("mode", tile.mode.name());
            }
        }
        return states;
    }
}
