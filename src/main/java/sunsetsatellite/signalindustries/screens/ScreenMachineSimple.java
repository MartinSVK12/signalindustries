package sunsetsatellite.signalindustries.screens;

import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.IExtendedScreenDraw;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.render.FakeItemElement;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineSimple;

public class ScreenMachineSimple extends ScreenFluid implements IExtendedScreenDraw {

    public TileEntityTieredMachineSimple tile;

    public ScreenMachineSimple(MenuFluid container) {
        super(container);
        tile = (TileEntityTieredMachineSimple) container.itemInventory;
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
        FakeItemElement fakeItemRenderer = new FakeItemElement(mc);
        if (tile.currentRecipe != null) {
            if(tile.currentRecipe.getOutput() instanceof ItemStack){
                for (int itemOutput : tile.itemOutputs) {
                    Slot slot = fluidSlots.getSlot(itemOutput);
                    fakeItemRenderer.render((ItemStack) tile.currentRecipe.getOutput(), slot.x, slot.y, false, slot, true);
                }
            } else if (tile.currentRecipe.getOutput() instanceof FluidStack) {
                for (int itemOutput : tile.itemOutputs) {
                    Slot slot = fluidSlots.getSlot(itemOutput);
                    fakeItemRenderer.render(((FluidStack) tile.currentRecipe.getOutput()).toItemStack(), slot.x, slot.y, false, slot, true);
                }
            }
        }
    }
}
