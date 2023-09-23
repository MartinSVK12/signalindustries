package sunsetsatellite.signalindustries.interfaces.mixins;


import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.signalindustries.inventories.TileEntityWithName;

public interface IEntityPlayerMP {
    void displayGuiScreen_si(GuiScreen guiScreen, Container container, IInventory inventory, int x, int y, int z);

    void displayGuiScreen_si(GuiScreen guiScreen, TileEntityWithName inventory, int x, int y, int z);

    void displayItemGuiScreen_si(GuiScreen guiScreen, Container container, IInventory inventory, ItemStack stack);
}
