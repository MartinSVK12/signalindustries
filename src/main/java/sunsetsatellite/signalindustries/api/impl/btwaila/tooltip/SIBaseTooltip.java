package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.core.util.NumberUtil;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.tooltips.TileTooltip;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

import java.util.ArrayList;
import java.util.List;

public abstract class SIBaseTooltip<T> extends TileTooltip<T> {

    public void drawFluids(IFluidInventory inv, AdvancedInfoComponent c, boolean collapse) {
        drawFluids(inv, c, collapse, 2);
    }

    public void drawFluids(IFluidInventory inv, AdvancedInfoComponent c, boolean collapse, int maxFluidBars) {
        if (inv.getFluidInventorySize() <= maxFluidBars) {
            for (int id = 0; id < inv.getFluidInventorySize(); id++) {
                if (inv.getFluidCapacityForSlot(id) <= 0) continue;
                FluidStack stack = inv.getFluidInSlot(id);
                if (stack != null && stack.fluid != null) {
                    BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(stack.fluid.blocks.get(0));
                    ProgressBarOptions options = new ProgressBarOptions(
                            152,
                            stack.fluid.getName()
                                    .replace("Flowing ", "")
                                    .replace("Still ", "")
                                    .replace("Signaling Energy", "sE")
                                    + ": " + NumberUtil.format(stack.amount) + "/" + NumberUtil.format(inv.getFluidCapacityForSlot(id)) + " ",
                            false,
                            true,
                            new TextureOptions(0, TextureRegistry.getTexture("signalindustries:block/reality_fabric")),
                            new TextureOptions(0xFFFFFF, model.getBlockTextureFromSideAndMetadata(Side.TOP, 0)));
                    c.drawProgressBarTextureWithText(stack.amount, inv.getFluidCapacityForSlot(id), options, 0);
                } else {
                    ProgressBarOptions options = new ProgressBarOptions(
                            152,
                            "Empty: 0/" + NumberUtil.format(inv.getFluidCapacityForSlot(id)) + " ",
                            false,
                            true,
                            new TextureOptions(0, TextureRegistry.getTexture("signalindustries:block/reality_fabric")),
                            new TextureOptions(0, TextureRegistry.getTexture("signalindustries:block/reality_fabric")));
                    c.drawProgressBarTextureWithText(0, inv.getFluidCapacityForSlot(id), options, 0);
                }
            }
        } else {
            List<ItemStack> stacks = new ArrayList<>();
            for (int id = 0; id < inv.getFluidInventorySize(); id++) {
                if (inv.getFluidCapacityForSlot(id) <= 0) continue;
                FluidStack stack = inv.getFluidInSlot(id);
                if (stack != null && stack.fluid != null && stack.fluid != SIFluids.ENERGY && collapse) {
                    stacks.add(new ItemStack(stack.fluid.blocks.get(0), stack.amount));
                } else if (stack != null && stack.fluid != null && stack.fluid == SIFluids.ENERGY) {
                    BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(stack.fluid.blocks.get(0));
                    ProgressBarOptions options = new ProgressBarOptions(
                            152,
                            stack.fluid.getName()
                                    .replace("Flowing ", "")
                                    .replace("Still ", "")
                                    .replace("Signaling Energy", "sE")
                                    + ": " + NumberUtil.format(stack.amount) + "/" + NumberUtil.format(inv.getFluidCapacityForSlot(id)) + " ",
                            false,
                            true,
                            new TextureOptions(0, TextureRegistry.getTexture("signalindustries:block/reality_fabric")),
                            new TextureOptions(0xFFFFFF, model.getBlockTextureFromSideAndMetadata(Side.TOP, 0)));
                    c.drawProgressBarTextureWithText(stack.amount, inv.getFluidCapacityForSlot(id), options, 0);
                } else if (!collapse) {
                    ProgressBarOptions options = new ProgressBarOptions(
                            152,
                            "Empty: 0/" + NumberUtil.format(inv.getFluidCapacityForSlot(id)) + " ",
                            false,
                            true,
                            new TextureOptions(0, TextureRegistry.getTexture("signalindustries:block/reality_fabric")),
                            new TextureOptions(0, TextureRegistry.getTexture("signalindustries:block/reality_fabric")));
                    c.drawProgressBarTextureWithText(0, inv.getFluidCapacityForSlot(id), options, 0);
                }
            }
            c.drawItemList(stacks.toArray(new ItemStack[0]), 0);
            c.addOffY(8);
        }

    }
}
