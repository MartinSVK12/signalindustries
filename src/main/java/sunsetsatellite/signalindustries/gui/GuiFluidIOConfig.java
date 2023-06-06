package sunsetsatellite.signalindustries.gui;

import b100.utils.ReflectUtils;
import net.minecraft.shared.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidContainer;
import sunsetsatellite.sunsetutils.util.Connection;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;

import java.lang.reflect.Field;

public class GuiFluidIOConfig extends GuiScreen {

    public GuiScreen parentScreen;
    public EntityPlayer entityplayer;
    public TileEntityFluidContainer tile;
    public int xSize = 176;
    public int ySize = 166;
    public Container inventorySlots;
    private static final RenderItem itemRenderer = new RenderItem();
    public GuiFluidIOConfig(EntityPlayer player, Container container, TileEntity tile, GuiScreen parent) {
        super(parent);
        this.tile = (TileEntityFluidContainer) tile;
        this.parentScreen = parent;
        this.entityplayer = player;
        this.inventorySlots = container;
    }

    public void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x,y,button);
        if (button == 1) {
            for (int l = 0; l < this.controlList.size(); ++l) {
                GuiButton guibutton = this.controlList.get(l);
                if (guibutton.mousePressed(this.mc, x, y)) {
                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    action2Performed(guibutton);
                }
            }
        }
    }

    public void action2Performed(GuiButton guibutton) {
        if(guibutton.id > 5){
            Direction dir = Direction.values()[guibutton.id-6];
            Integer currentValue = tile.activeFluidSlots.get(dir);
            if(currentValue > 0){
                tile.activeFluidSlots.put(dir,currentValue-1);
                guibutton.displayString = String.valueOf(tile.activeFluidSlots.get(dir));
            }

        }
    }

    public void keyTyped(char c, int i) {
        if (i == 1) {
            SignalIndustries.displayGui(entityplayer, parentScreen, inventorySlots, (IInventory) tile,tile.xCoord,tile.yCoord,tile.zCoord);
        }
    }

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
    public void drawScreen(int x, int y, float renderPartialTicks) {
        this.drawDefaultBackground();
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(renderPartialTicks);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)centerX, (float)centerY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        GL11.glDisable(32826);
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        this.drawGuiContainerForegroundLayer();
        GL11.glPopMatrix();
        super.drawScreen(x, y, renderPartialTicks);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2929);

    }

    @Override
    public void initGui() {
        controlList.add(new GuiButton(2, Math.round(width / 2) - 10, Math.round(height / 2) - 63, 15, 15, tile.connections.get(Direction.Y_POS).getLetter())); //Y+
        controlList.add(new GuiButton(4, Math.round(width / 2) - 10, Math.round(height / 2) - 48, 15, 15, tile.connections.get(Direction.Z_POS).getLetter())); //Z+
        controlList.add(new GuiButton(3, Math.round(width / 2) - 10, Math.round(height / 2) - 33, 15, 15, tile.connections.get(Direction.Y_NEG).getLetter())); //Y-
        controlList.add(new GuiButton(0, Math.round(width / 2) + 4, Math.round(height / 2) - 48, 15, 15, tile.connections.get(Direction.X_POS).getLetter())); //X+
        controlList.add(new GuiButton(1, Math.round(width / 2) - 24, Math.round(height / 2) - 48, 15, 15, tile.connections.get(Direction.X_NEG).getLetter())); //X-
        controlList.add(new GuiButton(5, Math.round(width / 2) + 4, Math.round(height / 2) - 33, 15, 15, tile.connections.get(Direction.Z_NEG).getLetter())); //Z-

        controlList.add(new GuiButton(8, Math.round(width / 2) - 10 + 50, Math.round(height / 2) - 63, 15, 15, String.valueOf(tile.activeFluidSlots.get(Direction.Y_POS))));
        controlList.add(new GuiButton(10, Math.round(width / 2) - 10 + 50, Math.round(height / 2) - 48, 15, 15, String.valueOf(tile.activeFluidSlots.get(Direction.Z_POS))));
        controlList.add(new GuiButton(9, Math.round(width / 2) - 10 + 50, Math.round(height / 2) - 33, 15, 15, String.valueOf(tile.activeFluidSlots.get(Direction.Y_NEG))));
        controlList.add(new GuiButton(6, Math.round(width / 2) + 4 + 50, Math.round(height / 2) - 48, 15, 15, String.valueOf(tile.activeFluidSlots.get(Direction.X_POS))));
        controlList.add(new GuiButton(7, Math.round(width / 2) - 24 + 50, Math.round(height / 2) - 48, 15, 15, String.valueOf(tile.activeFluidSlots.get(Direction.X_NEG))));
        controlList.add(new GuiButton(11, Math.round(width / 2) + 4 + 50, Math.round(height / 2) - 33, 15, 15, String.valueOf(tile.activeFluidSlots.get(Direction.Z_NEG))));

        if(tile.fluidContents.length == 1){
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
        if(guibutton.id > 5){
            Direction dir = Direction.values()[guibutton.id-6];
            Integer currentValue = tile.activeFluidSlots.get(dir);
            if(currentValue < tile.fluidContents.length-1){
                tile.activeFluidSlots.replace(dir,currentValue+1);
            }

            guibutton.displayString = String.valueOf(tile.activeFluidSlots.get(dir));
        }
        super.actionPerformed(guibutton);
    }

    protected void drawGuiContainerForegroundLayer()
    {
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
