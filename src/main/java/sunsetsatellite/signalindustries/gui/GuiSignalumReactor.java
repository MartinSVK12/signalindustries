package sunsetsatellite.signalindustries.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.inventories.TileEntitySignalumReactor;

public class GuiSignalumReactor extends GuiTileEntity<TileEntitySignalumReactor> {

    public GuiSignalumReactor(InventoryPlayer inventory, TileEntitySignalumReactor tile) {
        super(inventory, tile);
        xSize = 256;
    }
    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/signalum_reactor_ui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawCenteredString("Signalum Reactor", 128, 6, 0xFFFF0000);
        fontRenderer.drawCenteredString("State: "+tile.state,128,20,0xFFFFFFFF);
        fontRenderer.drawCenteredString("Fuel: "+tile.getFuel()+"|"+tile.getDepletedFuel(),128,30,0xFFFFFFFF);
    }

    @Override
    protected void buttonPressed(GuiButton guibutton) {
        switch (guibutton.id){
            case 0:
                tile.start();
                break;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui()
    {
        controlList.add(new GuiButton(0, Math.round((float) width / 2) - 10, Math.round((float) height / 2) + 50, 20, 20, "S"));
        super.initGui();
    }
}
