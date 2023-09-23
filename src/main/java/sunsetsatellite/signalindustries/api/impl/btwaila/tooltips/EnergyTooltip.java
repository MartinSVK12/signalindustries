package sunsetsatellite.signalindustries.api.impl.btwaila.tooltips;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.*;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.IBTWailaCustomBlockTooltip;
import toufoumaster.btwaila.TooltipGroup;
import toufoumaster.btwaila.TooltipRegistry;
import toufoumaster.btwaila.gui.GuiBlockOverlay;
import toufoumaster.btwaila.util.ColorOptions;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

public class EnergyTooltip implements IBTWailaCustomBlockTooltip {
    @Override
    public void addTooltip() {
        BTWaila.LOGGER.info("Adding tooltips for: " + this.getClass().getSimpleName());
        TooltipGroup tooltipGroup = new TooltipGroup(SignalIndustries.MOD_ID, TileEntityFluidItemContainer.class, this);
        tooltipGroup.addTooltip(TileEntityDimensionalAnchor.class);
        tooltipGroup.addTooltip(TileEntityBooster.class);
        tooltipGroup.addTooltip(TileEntityAlloySmelter.class);
        tooltipGroup.addTooltip(TileEntityExtractor.class);
        tooltipGroup.addTooltip(TileEntityInfuser.class);
        tooltipGroup.addTooltip(TileEntityPump.class);
        tooltipGroup.addTooltip(TileEntityStabilizer.class);
        tooltipGroup.addTooltip(TileEntityPlateFormer.class);
        tooltipGroup.addTooltip(TileEntityCrusher.class);
        tooltipGroup.addTooltip(TileEntityCrystalChamber.class);
        tooltipGroup.addTooltip(TileEntityCrystalCutter.class);
        tooltipGroup.addTooltip(TileEntityEnergyCell.class);
        tooltipGroup.addTooltip(TileEntityCentrifuge.class);
        tooltipGroup.addTooltip(TileEntityIgnitor.class);

        TooltipRegistry.tooltipMap.add(tooltipGroup);
    }

    @Override
    public void drawAdvancedTooltip(TileEntity tileEntity, GuiBlockOverlay guiBlockOverlay) {
        TileEntityFluidItemContainer tile = (TileEntityFluidItemContainer) tileEntity;
        int energySlotId = -1;
        for (int i = 0; i < tile.acceptedFluids.size(); i++) {
            if(tile.acceptedFluids.get(i).contains(SignalIndustries.energyFlowing)){
                energySlotId = i;
                break;
            }
        }
        if(energySlotId != -1){
            int energy = 0;
            int energyMax = tile.fluidCapacity[energySlotId];
            if(tile.fluidContents[energySlotId] != null){
                energy = tile.fluidContents[energySlotId].amount;
            }
            TextureOptions fgOptions = new TextureOptions().setBlockId(SignalIndustries.energyFlowing.id).setSide(Side.BOTTOM).setMetadata(0);
            TextureOptions bgOptions = new TextureOptions().setBlockId(SignalIndustries.realityFabric.id).setSide(Side.BOTTOM).setMetadata(0);
            ProgressBarOptions options = new ProgressBarOptions().setValues(true).setPercentage(true).setBackgroundOptions(bgOptions).setForegroundOptions(fgOptions).setText("SE: ");
            guiBlockOverlay.drawProgressBarTextureWithText(energy,energyMax,options,0);
        }
        if(tile instanceof TileEntityTieredMachine) {
            ProgressBarOptions options2 = new ProgressBarOptions().setValues(true).setPercentage(true).setBackgroundOptions(new ColorOptions(0x000000)).setForegroundOptions(new ColorOptions(0x00AA00)).setText("Progress: ");
            guiBlockOverlay.drawProgressBarWithText(((TileEntityTieredMachine) tile).progressTicks,((TileEntityTieredMachine) tile).progressMaxTicks,options2,0);
        }
        for (int i = 0; i < tile.fluidContents.length; i++) {
            if (tile.fluidContents[i] != null && tile.fluidContents[i].liquid != null && tile.fluidContents[i].liquid != SignalIndustries.energyFlowing) {
                String name = I18n.getInstance().translateNameKey(tile.fluidContents[i].liquid.getLanguageKey(0)).replace("Flowing ","");
                TextureOptions bgOptions = new TextureOptions().setBlockId(SignalIndustries.realityFabric.id).setSide(Side.BOTTOM);
                TextureOptions fgOptions = new TextureOptions().setBlockId(tile.fluidContents[i].liquid.id).setSide(Side.BOTTOM);
                if(tile.fluidContents[i].liquid == Block.fluidWaterFlowing){
                    fgOptions.setColor(0xA0A0FF);
                }
                ProgressBarOptions options = new ProgressBarOptions().setValues(true).setPercentage(false).setBackgroundOptions(bgOptions).setForegroundOptions(fgOptions).setText(name+": ");
                guiBlockOverlay.drawProgressBarTextureWithText(tile.fluidContents[i].amount,tile.fluidCapacity[i],options,0);
            }
        }
        guiBlockOverlay.drawInventory(tile,0);
        /*guiBlockOverlay.addOffY(12);
        for (int i = 0; i < tile.fluidContents.length; i++) {
            if(tile.fluidContents[i] != null && tile.fluidContents[i].liquid != null && tile.fluidContents[i].liquid != SignalIndustries.energyFlowing ){
                String name = I18n.getInstance().translateNameKey(tile.fluidContents[i].liquid.getLanguageKey(0)).replace("Flowing ","");
                guiBlockOverlay.drawProgressBarWithText(name+": ",tile.fluidContents[i].amount,tile.fluidCapacity[i],true,false,0);
            }
        }*/
        //guiBlockOverlay.drawStringWithShadow("This is a machine from SignalIndustries!",0,0xFFFF0000);
    }
}
