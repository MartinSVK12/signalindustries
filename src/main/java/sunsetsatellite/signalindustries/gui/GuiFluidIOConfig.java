package sunsetsatellite.signalindustries.gui;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidContainer;
import sunsetsatellite.fluidapi.util.Connection;
import sunsetsatellite.fluidapi.util.Direction;
import sunsetsatellite.signalindustries.SignalIndustries;

public class GuiFluidIOConfig extends GuiContainer {

    public GuiScreen parentScreen;
    public EntityPlayer entityplayer;
    public TileEntityFluidContainer tile;
    public GuiFluidIOConfig(EntityPlayer player, Container container, TileEntity tile, GuiScreen parent) {
        super(container);
        this.tile = (TileEntityFluidContainer) tile;
        this.parentScreen = parent;
        this.entityplayer = player;
    }

    public void keyTyped(char c, int i) {
        if (i == 1) {
            SignalIndustries.displayGui(entityplayer, parentScreen, inventorySlots, (IInventory) tile);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/signalindustries/gui/ioconfig.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    public void initGui() {
        controlList.add(new GuiButton(2, Math.round(width / 2) - 10, Math.round(height / 2) - 63, 15, 15, tile.connections.get(Direction.Y_POS).getLetter())); //Y+
        controlList.add(new GuiButton(4, Math.round(width / 2) - 10, Math.round(height / 2) - 48, 15, 15, tile.connections.get(Direction.Z_POS).getLetter())); //Z+
        controlList.add(new GuiButton(3, Math.round(width / 2) - 10, Math.round(height / 2) - 33, 15, 15, tile.connections.get(Direction.Y_NEG).getLetter())); //Y-
        controlList.add(new GuiButton(0, Math.round(width / 2) + 4, Math.round(height / 2) - 48, 15, 15, tile.connections.get(Direction.X_POS).getLetter())); //X+
        controlList.add(new GuiButton(1, Math.round(width / 2) - 24, Math.round(height / 2) - 48, 15, 15, tile.connections.get(Direction.X_NEG).getLetter())); //X-
        controlList.add(new GuiButton(5, Math.round(width / 2) + 4, Math.round(height / 2) - 33, 15, 15, tile.connections.get(Direction.Z_NEG).getLetter())); //Z-

        controlList.add(new GuiButton(6, Math.round(width / 2) - 10 + 50, Math.round(height / 2) - 63, 15, 15, "0"));
        controlList.add(new GuiButton(7, Math.round(width / 2) - 10 + 50, Math.round(height / 2) - 48, 15, 15, "0"));
        controlList.add(new GuiButton(8, Math.round(width / 2) - 10 + 50, Math.round(height / 2) - 33, 15, 15, "0"));
        controlList.add(new GuiButton(9, Math.round(width / 2) + 4 + 50, Math.round(height / 2) - 48, 15, 15, "0"));
        controlList.add(new GuiButton(10, Math.round(width / 2) - 24 + 50, Math.round(height / 2) - 48, 15, 15, "0"));
        controlList.add(new GuiButton(11, Math.round(width / 2) + 4 + 50, Math.round(height / 2) - 33, 15, 15, "0"));

        if(((TileEntityFluidContainer)tile).fluidContents.length == 1){
            controlList.get(6).enabled = false;
            controlList.get(7).enabled = false;
            controlList.get(8).enabled = false;
            controlList.get(9).enabled = false;
            controlList.get(10).enabled = false;
            controlList.get(11).enabled = false;
        }

        super.initGui();
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        if(guibutton.id >= 0 && guibutton.id < 6){
            switch (tile.connections.get(Direction.values()[guibutton.id])) {
                case NONE:
                    tile.connections.replace(Direction.values()[guibutton.id], Connection.INPUT);
                    break;
                case INPUT:
                    tile.connections.replace(Direction.values()[guibutton.id], Connection.OUTPUT);
                    break;
                case OUTPUT:
                    tile.connections.replace(Direction.values()[guibutton.id], Connection.BOTH);
                    break;
                case BOTH:
                    tile.connections.replace(Direction.values()[guibutton.id], Connection.NONE);
                    break;
            }

            guibutton.displayString = tile.connections.get(Direction.values()[guibutton.id]).getLetter();

        }
        super.actionPerformed(guibutton);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawString("Configure: Fluids", 45, 6, 0xFF404040);
        fontRenderer.drawString("I/O", 78, 70, 0xFF404040);
        fontRenderer.drawString("Slot", 128, 70, 0xFF404040);
        fontRenderer.drawString("Y+", 26, 22, 0xFFFFFFFF);
        fontRenderer.drawString("Y-", 26, 58, 0xFFFFFFFF);
        fontRenderer.drawString("Z+", 26, 40, 0xFFFFFFFF);
        fontRenderer.drawString("X+", 44, 40, 0xFFFFFFFF);
        fontRenderer.drawString("Z-", 44, 58, 0xFFFFFFFF);
        fontRenderer.drawString("X-", 8, 40, 0xFFFFFFFF);
    }
}
