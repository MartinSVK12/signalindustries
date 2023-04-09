package sunsetsatellite.signalindustries.interfaces.mixins;

import net.minecraft.src.Container;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import sunsetsatellite.signalindustries.util.Tiers;

public interface IEntityPlayerMP {
    void displayGuiScreen_si(GuiScreen guiScreen, Container container, IInventory inventory, int x, int y, int z);

    void displayItemGuiScreen_si(GuiScreen guiScreen, Container container, IInventory inventory, ItemStack stack);
}
