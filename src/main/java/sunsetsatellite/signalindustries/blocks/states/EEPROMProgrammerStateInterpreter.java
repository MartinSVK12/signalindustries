package sunsetsatellite.signalindustries.blocks.states;

import net.minecraft.core.block.Block;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityProgrammer;
import sunsetsatellite.signalindustries.items.ItemRomChip;
import sunsetsatellite.signalindustries.items.ItemTrigger;
import useless.dragonfly.model.blockstates.processed.MetaStateInterpreter;

import java.util.HashMap;

public class EEPROMProgrammerStateInterpreter extends MetaStateInterpreter {
    @Override
    public HashMap<String, String> getStateMap(WorldSource worldSource, int x, int y, int z, Block block, int meta) {
        HashMap<String, String> result = new HashMap<>();
        TileEntityProgrammer tile = (TileEntityProgrammer) worldSource.getBlockTileEntity(x,y,z);
        if (tile != null) {
            if(tile.itemContents[0] != null && tile.itemContents[0].getItem() instanceof ItemRomChip){
                result.put("loaded","true");
            } else {
                result.put("loaded","false");
            }
            if (tile.progressTicks > 0) {
                result.put("activated", "true");
            } else {
                result.put("activated", "false");
            }
            if(tile.itemContents[1] != null && tile.itemContents[1].getItem() instanceof ItemTrigger){
                if(tile.itemContents[1].getData().containsKey("ability")){
                    result.put("finished","true");
                } else {
                    result.put("finished","false");
                }
            } else {
                result.put("finished","false");
            }
        } else {
            result.put("loaded","false");
            result.put("activated", "false");
            result.put("finished", "false");
        }
        return result;
    }
}
