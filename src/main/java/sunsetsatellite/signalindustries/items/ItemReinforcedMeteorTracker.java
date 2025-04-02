package sunsetsatellite.signalindustries.items;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.SIBlocks;

public class ItemReinforcedMeteorTracker extends Item implements ICustomDescription {

    public ItemReinforcedMeteorTracker(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
        int range = 4;
        if(itemstack.getMetadata() == 0){
            itemstack.setMetadata(1);
        } else {
            int oreFound = 0;
            int oreYLevel = -1;
            for (int i = -range; i < range; i++) {
                for (int j = -range; j < range; j++) {
                    for (int k = 0; k < world.getHeightValue((int) (entityplayer.x + i), (int) (entityplayer.z + j)); k++) {
                        int blockId = world.getBlockId((int) (entityplayer.x + i), k, (int) (entityplayer.z + j));
                        if (blockId == SIBlocks.signalumOre.id()) {
                            oreFound++;
                            if (k > oreYLevel) {
                                oreYLevel = k;
                            }
                        }
                    }
                }
            }
            if (oreFound > 0 && oreYLevel < entityplayer.y) {
                entityplayer.sendStatusMessage(String.format("%d Signalite Ore blocks detected, approx. %d blocks underground.", oreFound, (int) entityplayer.y - oreYLevel));
            } else if(oreFound > 0 && oreYLevel > entityplayer.y) {
                entityplayer.sendStatusMessage(String.format("%d Signalite Ore blocks detected, approx. %d blocks above.", oreFound,  oreYLevel - (int) entityplayer.y));
            } else {
                entityplayer.sendStatusMessage("No nearby traces of Signalite could be found.");
            }
        }
        return super.onUseItem(itemstack, world, entityplayer);
    }

    @Override
    public String getDescription(ItemStack stack) {
        if(stack.getMetadata() != 1){
            return "Uncalibrated!\n"+ TextFormatting.GRAY +"Right-click while holding in your hand to calibrate.";
        }
        return "Calibrated.";
    }
}
