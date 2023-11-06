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
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int h = (tile.getFuel()+tile.getDepletedFuel()) * 64 / (4000*9);
        int depletedH = (int) ((tile.getDepletedFuel() * h) / ((tile.getFuel()+tile.getDepletedFuel()) == 0 ? Float.MIN_VALUE : tile.getFuel()+tile.getDepletedFuel()));
        this.drawTexturedModalRect(x+96, y+50+(64-h), 0, 166+(64-h), 64, h);
        this.drawTexturedModalRect(x+96, y+50+(64-depletedH), 64, 166+(64-depletedH), 64, depletedH);
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawCenteredString("Signalum Reactor", 128, 6, 0xFFFF0000);
        //fontRenderer.drawCenteredString("State: "+tile.state,128,20,0xFFFFFFFF);
        //fontRenderer.drawCenteredString("Fuel: "+tile.getFuel()+"|"+tile.getDepletedFuel(),128,30,0xFFFFFFFF);
        float capacity = ((float)(tile.getFuel()+tile.getDepletedFuel())/(4000*9))*100;
        float fill = 100-((float)tile.getDepletedFuel()/(tile.getFuel()+tile.getDepletedFuel()))*100;
        if(Float.isNaN(fill)){
            fill = 0;
        }
        int color = 0xFFFFFFF;
        switch (tile.state){
            case INACTIVE:
                color = 0xFF404040;
                break;
            case IGNITING:
                color = 0xFFFF8000;
                break;
            case RUNNING:
                color = 0xFF00FF00;
                break;
        }
        fontRenderer.drawCenteredString(String.format("%.0f%%",fill),128,63,0xFFFFFFFF);
        fontRenderer.drawCenteredString(String.valueOf(tile.state),128,77,color);
        fontRenderer.drawCenteredString(String.format("%.0f%%",capacity),128,90,0xFF808080);
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
        controlList.add(new GuiButton(0, Math.round((float) width / 2) - 30, Math.round((float) height / 2) + 50, 60, 20, "ON/OFF"));
        super.initGui();
    }
}
