package sunsetsatellite.signalindustries.interfaces.mixins;


import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.signalindustries.interfaces.INamedTileEntity;
import sunsetsatellite.signalindustries.inventories.base.TileEntityWithName;

import java.util.function.Function;
import java.util.function.Supplier;

public interface IEntityPlayerMP {
    void displayGuiScreen_si(Supplier<GuiScreen> screenSupplier, Container container, IInventory inventory, int x, int y, int z);

    void displayGuiScreen_si(Supplier<GuiScreen> screenSupplier, INamedTileEntity inventory, int x, int y, int z);

    void displayItemGuiScreen_si(Supplier<GuiScreen> screenSupplier, Container container, IInventory inventory, ItemStack stack);
}
