package sunsetsatellite.signalindustries.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.inventories.TileEntityMultiConduit;

import java.util.Arrays;
import java.util.Objects;


public class GuiMultiConduitConfig extends GuiScreen {

    public GuiScreen parentScreen;
    public EntityPlayer entityplayer;
    public TileEntityMultiConduit tile;
    public int xSize = 176;
    public int ySize = 166;
    private static final ItemEntityRenderer itemRenderer = new ItemEntityRenderer();
    public GuiMultiConduitConfig(EntityPlayer player, TileEntityMultiConduit tile, GuiScreen parent) {
        super(parent);
        this.tile = tile;
        this.parentScreen = parent;
        this.entityplayer = player;
    }

    public void mouseClicked(int x, int y, int button) {
        super.mouseClicked(x,y,button);
        if (button == 1) {
            for (int l = 0; l < this.controlList.size(); ++l) {
                GuiButton guibutton = this.controlList.get(l);
                if (guibutton.mouseClicked(this.mc, x, y)) {
                    this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
                    action2Performed(guibutton);
                }
            }
        }
    }

    public void action2Performed(GuiButton guibutton) {
        if(guibutton.id > 5){
            Direction dir = Direction.values()[Math.min(6,Math.max(0,guibutton.id-6))];
            Integer currentValue = tile.conduitConnections.get(dir);
            if(currentValue > -1){
                tile.conduitConnections.put(dir,currentValue-1);
                guibutton.displayString = tile.conduitConnections.get(dir) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(dir));
            }

        }
    }

    public void keyTyped(char c, int i) {
        /*if (i == 1) {
            SignalIndustries.displayGui(entityplayer, () -> parentScreen, inventorySlots, (IInventory) tile,tile.x,tile.y,tile.z);
        }*/
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/assets/signalindustries/gui/ioconfig.png");
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
        Lighting.enableInventoryLight();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)centerX, (float)centerY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(32826);
        GL11.glDisable(32826);
        Lighting.disable();
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        this.drawGuiContainerForegroundLayer();
        GL11.glPopMatrix();
        super.drawScreen(x, y, renderPartialTicks);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2929);

    }

    @Override
    public void init() {
        int centerX = (width - xSize) / 2;
        int centerY = (height - ySize) / 2;

        controlList.add(new GuiButton(2, (width / 2) - 10, (height / 2) - 63, 15, 15, "B")); //Y+
        controlList.add(new GuiButton(4, (width / 2) - 10, (height / 2) - 48, 15, 15, "B")); //Z+
        controlList.add(new GuiButton(3, (width / 2) - 10, (height / 2) - 33, 15, 15, "B")); //Y-
        controlList.add(new GuiButton(0, (width / 2) + 4, (height / 2) - 48, 15, 15, "B")); //X+
        controlList.add(new GuiButton(1, (width / 2) - 24, (height / 2) - 48, 15, 15,"B")); //X-
        controlList.add(new GuiButton(5, (width / 2) + 4, (height / 2) - 33, 15, 15, "B")); //Z-

        controlList.add(new GuiButton(8, (width / 2) - 10 + 50, (height / 2) - 63, 15, 15, tile.conduitConnections.get(Direction.Y_POS) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.Y_POS))));
        controlList.add(new GuiButton(10, (width / 2) - 10 + 50, (height / 2) - 48, 15, 15, tile.conduitConnections.get(Direction.Z_POS) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.Z_POS))));
        controlList.add(new GuiButton(9, (width / 2) - 10 + 50, (height / 2) - 33, 15, 15, tile.conduitConnections.get(Direction.Y_NEG) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.Y_NEG))));
        controlList.add(new GuiButton(6, (width / 2) + 4 + 50, (height / 2) - 48, 15, 15, tile.conduitConnections.get(Direction.X_POS) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.X_POS))));
        controlList.add(new GuiButton(7, (width / 2) - 24 + 50, (height / 2) - 48, 15, 15, tile.conduitConnections.get(Direction.X_NEG) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.X_NEG))));
        controlList.add(new GuiButton(11, (width / 2) + 4 + 50, (height / 2) - 33, 15, 15, tile.conduitConnections.get(Direction.Z_NEG) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.Z_NEG))));

        //controlList.add(new GuiButton(12,(width / 2) - 85, (height / 2)-12, 30, 15, "All I"));
        //controlList.add(new GuiButton(13, (width / 2) - 55, (height / 2)-12, 30, 15, "All O"));

        if(tile instanceof IHasIOPreview){
            controlList.add(new GuiButton(14, (width / 2) + 60, (height / 2) - 75, 20, 20, "P"));
        }

        controlList.get(0).enabled = false;
        controlList.get(1).enabled = false;
        controlList.get(2).enabled = false;
        controlList.get(3).enabled = false;
        controlList.get(4).enabled = false;
        controlList.get(5).enabled = false;

        if(Arrays.stream(tile.conduits).allMatch(Objects::isNull)){
            controlList.get(6).enabled = false;
            controlList.get(7).enabled = false;
            controlList.get(8).enabled = false;
            controlList.get(9).enabled = false;
            controlList.get(10).enabled = false;
            controlList.get(11).enabled = false;
        }

        /*if(tile instanceof TileEntityEnergyConnector && ((TileEntityEnergyConnector) tile).isConnected()){
            controlList.get(0).enabled = false;
        }*/

        super.init();
    }

    @Override
    protected void buttonPressed(GuiButton guibutton) {
        if(tile != null) {
            /*if (guibutton.id >= 0 && guibutton.id < 6) {
                switch (tile.fluidConnections.get(Direction.values()[guibutton.id])) {
                    case NONE:
                        if (tile.getBlockType() == SIBlocks.infiniteEnergyCell || tile.getBlockType() == SIBlocks.infiniteFluidTank) {
                            tile.fluidConnections.replace(Direction.values()[guibutton.id], Connection.OUTPUT);
                            break;
                        }
                        tile.fluidConnections.replace(Direction.values()[guibutton.id], Connection.INPUT);
                        break;
                    case INPUT:
                        tile.fluidConnections.replace(Direction.values()[guibutton.id], Connection.OUTPUT);
                        break;
                    case OUTPUT:
                        if (tile.getBlockType() == SIBlocks.infiniteEnergyCell || tile.getBlockType() == SIBlocks.infiniteFluidTank) {
                            tile.fluidConnections.replace(Direction.values()[guibutton.id], Connection.NONE);
                            break;
                        }
                        tile.fluidConnections.replace(Direction.values()[guibutton.id], Connection.BOTH);
                        break;
                    case BOTH:
                        tile.fluidConnections.replace(Direction.values()[guibutton.id], Connection.NONE);
                        break;
                }

                guibutton.displayString = tile.fluidConnections.get(Direction.values()[guibutton.id]).getLetter();

            }*/
            if (guibutton.id > 5 && guibutton.id < 12) {
                Direction dir = Direction.values()[guibutton.id - 6];
                Integer currentValue = tile.conduitConnections.get(dir);
                if (currentValue < tile.getAmountOfConduits() - 1) {
                    tile.conduitConnections.replace(dir, currentValue + 1);
                }

                guibutton.displayString = String.valueOf(tile.conduitConnections.get(dir));
            }
            /*if (guibutton.id == 12) {
                for (Direction direction : Direction.values()) {
                    tile.fluidConnections.replace(direction, Connection.INPUT);
                }
                for (GuiButton button : controlList) {
                    if (button.id >= 0 && button.id < 6) {
                        button.displayString = tile.fluidConnections.get(Direction.values()[button.id]).getLetter();
                    }
                }
            }
            if (guibutton.id == 13) {
                for (Direction direction : Direction.values()) {
                    tile.fluidConnections.replace(direction, Connection.OUTPUT);
                }
                for (GuiButton button : controlList) {
                    if (button.id >= 0 && button.id < 6) {
                        button.displayString = tile.fluidConnections.get(Direction.values()[button.id]).getLetter();
                    }
                }
            }
            if (guibutton.id == 14) {
                if (tile instanceof IHasIOPreview) {
                    IHasIOPreview p = ((IHasIOPreview) tile);
                    p.setPreview(((IHasIOPreview) tile).getPreview() != IOPreview.FLUID ? IOPreview.FLUID : IOPreview.NONE);
                }
            }*/
        }
        super.buttonPressed(guibutton);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Configure: Multi Conduit", 30, 6, 0xFF404040);
        fontRenderer.drawString("I/O", 78, 70, 0xFF404040);
        fontRenderer.drawString("Conduit", 118, 70, 0xFF404040);
        fontRenderer.drawString("Y+", 26, 22, 0xFFFFFFFF);
        fontRenderer.drawString("Y-", 26, 58, 0xFFFFFFFF);
        fontRenderer.drawString("Z+", 26, 40, 0xFFFFFFFF);
        fontRenderer.drawString("X+", 44, 40, 0xFFFFFFFF);
        fontRenderer.drawString("Z-", 44, 58, 0xFFFFFFFF);
        fontRenderer.drawString("X-", 8, 40, 0xFFFFFFFF);
    }
}
