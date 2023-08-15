package sunsetsatellite.signalindustries.api.impl.btwaila.tooltips;

import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.fluidapi.FluidAPI;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.inventories.*;
import sunsetsatellite.signalindustries.mixin.accessors.GuiBlockOverlayAccessor;
import sunsetsatellite.signalindustries.mixin.invokers.GuiInvoker;
import sunsetsatellite.signalindustries.util.NumberUtil;
import toufoumaster.btwaila.BTWaila;
import toufoumaster.btwaila.IBTWailaCustomBlockTooltip;
import toufoumaster.btwaila.TooltipGroup;
import toufoumaster.btwaila.TooltipRegistry;
import toufoumaster.btwaila.gui.GuiBlockOverlay;

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

        TooltipRegistry.tooltipMap.add(tooltipGroup);
    }

    @Override
    public void drawAdvancedTooltip(TileEntity tileEntity, GuiBlockOverlay guiBlockOverlay) {
        TileEntityFluidItemContainer tile = (TileEntityFluidItemContainer) tileEntity;
        int energySlotId = 0;
        for (int i = 0; i < tile.acceptedFluids.size(); i++) {
            if(tile.acceptedFluids.get(i).contains(SignalIndustries.energyFlowing)){
                energySlotId = i;
                break;
            }
        }
        int energy = 0;
        int energyMax = tile.fluidCapacity[energySlotId];
        if(tile.fluidContents[energySlotId] != null){
            energy = tile.fluidContents[energySlotId].amount;
        }
        int x = ((GuiBlockOverlayAccessor)guiBlockOverlay).getPosX();
        int y = ((GuiBlockOverlayAccessor)guiBlockOverlay).getOffY();

        double fill = FluidAPI.map(energy, 0, energyMax, 0, 140);

        ((GuiInvoker)guiBlockOverlay).callDrawGradientRect(x,y+4,x+142,y+20,0xFF000000,0xFF000000);
        ((GuiInvoker)guiBlockOverlay).callDrawGradientRect(x+2,y+6, x+140,y+18,0xFF404040,0xFF404040);
        ((GuiInvoker)guiBlockOverlay).callDrawGradientRect(x+2,y+6, (int) (x+fill),y+18,0xFFFF0000,0xFFFF0060);
        guiBlockOverlay.addOffY(8);
        guiBlockOverlay.drawStringWithShadow(String.format("SE: %s/%s (%.1f%%)", NumberUtil.format(energy),NumberUtil.format(energyMax),(((double)energy/(double)energyMax)*100)),-22,0xFFFFFFFF);
        guiBlockOverlay.addOffY(-12);
        guiBlockOverlay.drawInventory(tile,120);
        //guiBlockOverlay.drawStringWithShadow("This is a machine from SignalIndustries!",0,0xFFFF0000);
    }
}
