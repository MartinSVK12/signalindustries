package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.core.block.Block;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.TileEntitySIFluidTank;
import sunsetsatellite.signalindustries.inventories.TileEntityStorageContainer;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

public class StorageContainerTooltip extends SIBaseTooltip<TileEntityStorageContainer> {
    @Override
    public void initTooltip() {
        this.addClass(TileEntityStorageContainer.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityStorageContainer tile, AdvancedInfoComponent c) {
        if(tile.contents != null){
            int color = 0x00FF00;
            float ratio = (float) tile.contents.stackSize / tile.capacity;
            if(ratio >= 0.5f && ratio < 0.8f){
                color = 0xFFFF00;
            } else if (ratio >= 0.8f) {
                color = 0xFF0000;
            }
            if(tile.locked){
                c.drawStringWithShadow("Locked",0,0xFFFF00);
            }
            c.drawStringWithShadow("Holding: "+tile.contents.stackSize+"x "+tile.contents.getDisplayName(),0);
            ProgressBarOptions options = new ProgressBarOptions()
                    .setForegroundOptions(new TextureOptions(color, Block.sand.atlasIndices[0]))
                    .setBackgroundOptions(new TextureOptions(0, SignalIndustries.realityFabric.atlasIndices[0]))
                    .setText("Capacity: ");
            c.drawProgressBarTextureWithText(tile.contents.stackSize,tile.capacity,options,0);
        } else {
            if(tile.locked){
                c.drawStringWithShadow("Locked",0,0xFFFF00);
            }
            c.drawStringWithShadow("Empty",0);
            ProgressBarOptions options = new ProgressBarOptions()
                    .setForegroundOptions(new TextureOptions(0x00FF00, Block.sand.atlasIndices[0]))
                    .setBackgroundOptions(new TextureOptions(0, SignalIndustries.realityFabric.atlasIndices[0]))
                    .setText("Capacity: ");
            c.drawProgressBarTextureWithText(0,tile.capacity,options,0);
        }

    }
}
