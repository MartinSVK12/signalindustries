package sunsetsatellite.signalindustries.blocks.states;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import org.useless.dragonfly.data.block.mojang.state.MetaStateInterpreter;
import sunsetsatellite.signalindustries.items.ItemWarpOrb;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPulsar;

import java.util.HashMap;

public class PulsarStateInterpreter extends MetaStateInterpreter {
    @Override
    public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block<?> block, int meta) {
        HashMap<String, String> result = new HashMap<>();
        TileEntityPulsar pulsar = (TileEntityPulsar) worldSource.getTileEntity(x, y, z);

        result.put("active", pulsar.isBurning() ? "true" : "false");
        result.put("warp", (pulsar.getItem(0) != null && pulsar.getItem(0).getItem() instanceof ItemWarpOrb) ? "true" : "false");

        return result;
    }
}
