package sunsetsatellite.signalindustries.api.impl.btwaila.tooltips;

import net.minecraft.client.render.block.color.BlockColorWater;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.fluidapi.template.tiles.TileEntityMassFluidItemContainer;
import sunsetsatellite.fluidapi.template.tiles.TileEntityMultiFluidTank;
import sunsetsatellite.signalindustries.SignalIndustries;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.IBTWailaCustomBlockTooltip;
import toufoumaster.btwaila.TooltipGroup;
import toufoumaster.btwaila.TooltipRegistry;
import toufoumaster.btwaila.gui.GuiBlockOverlay;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;


//TODO: Move to fluidapi eventually
public class FluidTooltip implements IBTWailaCustomBlockTooltip {

    @Override
    public void addTooltip() {
        BTWaila.LOGGER.info("Adding tooltips for: " + this.getClass().getSimpleName());
        TooltipGroup tooltipGroup = new TooltipGroup(SignalIndustries.MOD_ID, TileEntityMassFluidItemContainer.class, this);
        tooltipGroup.addTooltip(TileEntityMultiFluidTank.class);
        TooltipRegistry.tooltipMap.add(tooltipGroup);
    }

    @Override
    public void drawAdvancedTooltip(TileEntity tileEntity, GuiBlockOverlay guiBlockOverlay) {
        TileEntityMassFluidItemContainer tile = (TileEntityMassFluidItemContainer) tileEntity;
        for (int i = 0; i < tile.fluidContents.size(); i++) {
            if (tile.fluidContents.get(i) != null && tile.fluidContents.get(i).liquid != null && tile.fluidContents.get(i).liquid != SignalIndustries.energyFlowing) {
                String name = I18n.getInstance().translateNameKey(tile.fluidContents.get(i).liquid.getLanguageKey(0)).replace("Flowing ","");
                TextureOptions bgOptions = new TextureOptions().setBlockId(SignalIndustries.realityFabric.id).setSide(Side.BOTTOM).setMetadata(0);
                TextureOptions fgOptions = new TextureOptions().setBlockId(tile.fluidContents.get(i).liquid.id).setSide(Side.BOTTOM).setMetadata(0);
                if(tile.fluidContents.get(i).liquid == Block.fluidWaterFlowing){
                    fgOptions.setColor(new BlockColorWater().getWorldColor(tile.worldObj, tile.xCoord, tile.yCoord, tile.zCoord));
                }
                ProgressBarOptions options = new ProgressBarOptions().setValues(true).setPercentage(false).setBackgroundOptions(bgOptions).setForegroundOptions(fgOptions).setText(name+": ");
                guiBlockOverlay.drawProgressBarTextureWithText(tile.fluidContents.get(i).amount,tile.fluidCapacity,options,0);
            }
        }
        /*TileEntityMassFluidItemContainer tile = (TileEntityMassFluidItemContainer) tileEntity;
        guiBlockOverlay.addOffY(12);
        for (int i = 0; i < tile.fluidContents.size(); i++) {
            String name = I18n.getInstance().translateNameKey(tile.fluidContents.get(i).liquid.getLanguageKey(0));
            guiBlockOverlay.drawProgressBarWithText(name+": ",99,tile.fluidCapacity,true,false,0);
        }*/
    }

}
