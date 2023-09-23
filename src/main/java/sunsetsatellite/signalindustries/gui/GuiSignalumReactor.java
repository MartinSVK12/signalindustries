package sunsetsatellite.signalindustries.gui;

import net.minecraft.core.player.inventory.InventoryPlayer;
import sunsetsatellite.signalindustries.inventories.TileEntitySignalumReactor;

public class GuiSignalumReactor extends GuiTileEntity<TileEntitySignalumReactor> {

    public GuiSignalumReactor(InventoryPlayer inventory, TileEntitySignalumReactor tile) {
        super(inventory, tile);
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {

    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        drawStringCentered(fontRenderer,"Testing",0,0,0xFFFF0000);
    }
}
