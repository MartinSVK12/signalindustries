package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.util.NumberUtil;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

import java.util.ArrayList;
import java.util.List;

public abstract class SIBaseTooltip<T> extends TileTooltip<T> {
    public void drawFluids(IFluidInventory inv, AdvancedInfoComponent c, boolean collapse){
        if(inv.getFluidInventorySize() <= 2){
            for (int id = 0; id < inv.getFluidInventorySize(); id++) {
                if(inv.getFluidCapacityForSlot(id) <= 0) continue;
                FluidStack stack = inv.getFluidInSlot(id);
                if(stack != null && stack.liquid != null){
                    BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(stack.liquid);
                    ProgressBarOptions options = new ProgressBarOptions(
                            0,
                            I18n.getInstance().translateKey(stack.getFluidName()+".name")
                                    .replace("Flowing ","")
                                    .replace("Still ","")
                                    .replace("Signalum Energy","sE")
                                    +": "+ NumberUtil.format(stack.amount)+"/"+NumberUtil.format(inv.getFluidCapacityForSlot(id))+" ",
                            false,
                            true,
                            new TextureOptions(0, TextureRegistry.getTexture("signalindustries:block/reality_fabric")),
                            new TextureOptions(0xFFFFFF, model.getBlockTextureFromSideAndMetadata(Side.TOP,0)));
                    c.drawProgressBarTextureWithText(stack.amount,inv.getFluidCapacityForSlot(id),options,0);
                } else {
                    ProgressBarOptions options = new ProgressBarOptions().setValues(false).setText("Empty: 0/"+NumberUtil.format(inv.getFluidCapacityForSlot(id))+" ");
                    c.drawProgressBarWithText(0,inv.getFluidCapacityForSlot(id),options,0);
                }
            }
        } else {
            List<ItemStack> stacks = new ArrayList<>();
            for (int id = 0; id < inv.getFluidInventorySize(); id++) {
                if(inv.getFluidCapacityForSlot(id) <= 0) continue;
                FluidStack stack = inv.getFluidInSlot(id);
                if(stack != null && stack.liquid != null && stack.liquid != SIBlocks.energyFlowing && collapse){
                    stacks.add(new ItemStack(stack.liquid,stack.amount));
                } else if (stack != null && (stack.liquid == SIBlocks.energyFlowing || !collapse)){
                    BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(stack.liquid);
                    ProgressBarOptions options = new ProgressBarOptions(
                            0,
                            I18n.getInstance().translateKey(stack.getFluidName()+".name")
                                    .replace("Flowing ","")
                                    .replace("Still ","")
                                    .replace("Signalum Energy","sE")
                                    +": "+ NumberUtil.format(stack.amount)+"/"+NumberUtil.format(inv.getFluidCapacityForSlot(id))+" ",
                            false,
                            true,
                            new TextureOptions(0, TextureRegistry.getTexture("signalindustries:block/reality_fabric")),
                            new TextureOptions(0xFFFFFF, model.getBlockTextureFromSideAndMetadata(Side.TOP,0)));
                    c.drawProgressBarTextureWithText(stack.amount,inv.getFluidCapacityForSlot(id),options,0);
                } else {
                    ProgressBarOptions options = new ProgressBarOptions().setValues(false).setText("Empty: 0/"+NumberUtil.format(inv.getFluidCapacityForSlot(id))+" ");
                    c.drawProgressBarWithText(0,inv.getFluidCapacityForSlot(id),options,0);
                }
            }
            c.drawItemList(stacks.toArray(new ItemStack[0]), 0);
            c.addOffY(8);
        }

    }
}
