package sunsetsatellite.signalindustries.interfaces.mixins;

import net.minecraft.src.Container;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.IInventory;

public interface IEntityPlayerMP {
    void displayGuiScreen(GuiScreen guiScreen, Container container, IInventory inventory);
}
