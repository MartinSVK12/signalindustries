package sunsetsatellite.signalindustries.interfaces.mixins;






public interface IEntityPlayerMP {
    void displayGuiScreen_si(GuiScreen guiScreen, Container container, IInventory inventory, int x, int y, int z);

    void displayItemGuiScreen_si(GuiScreen guiScreen, Container container, IInventory inventory, ItemStack stack);
}
