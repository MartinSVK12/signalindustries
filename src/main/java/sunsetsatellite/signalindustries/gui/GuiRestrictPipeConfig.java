package sunsetsatellite.signalindustries.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.inventories.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.PipeMode;


public class GuiRestrictPipeConfig extends GuiScreen {

    public GuiScreen parentScreen;
    public EntityPlayer entityplayer;
    public TileEntityItemConduit tile;
    public int xSize = 176;
    public int ySize = 166;
    private static final ItemEntityRenderer itemRenderer = new ItemEntityRenderer();
    public GuiRestrictPipeConfig(EntityPlayer player, TileEntity tile, GuiScreen parent) {
        super(parent);
        this.tile = (TileEntityItemConduit) tile;
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
                }
            }
        }
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
        controlList.add(new GuiButton(2, Math.round(width / 2) - 10, Math.round(height / 2) - 63, 15, 15, tile.restrictDirections.get(Direction.Y_POS) ? "R" : "-")); //Y+
        controlList.add(new GuiButton(4, Math.round(width / 2) - 10, Math.round(height / 2) - 48, 15, 15, tile.restrictDirections.get(Direction.Z_POS) ? "R" : "-")); //Z+
        controlList.add(new GuiButton(3, Math.round(width / 2) - 10, Math.round(height / 2) - 33, 15, 15, tile.restrictDirections.get(Direction.Y_NEG) ? "R" : "-")); //Y-
        controlList.add(new GuiButton(0, Math.round(width / 2) + 4, Math.round(height / 2) - 48, 15, 15, tile.restrictDirections.get(Direction.X_POS) ? "R" : "-")); //X+
        controlList.add(new GuiButton(1, Math.round(width / 2) - 24, Math.round(height / 2) - 48, 15, 15, tile.restrictDirections.get(Direction.X_NEG) ? "R" : "-")); //X-
        controlList.add(new GuiButton(5, Math.round(width / 2) + 4, Math.round(height / 2) - 33, 15, 15, tile.restrictDirections.get(Direction.Z_NEG) ? "R" : "-")); //Z-

        controlList.add(new GuiButton(6, Math.round(width / 2) + 4 + 22, Math.round(height / 2) - 48, 50, 15, String.valueOf(tile.mode)));

        super.init();
    }

    @Override
    protected void buttonPressed(GuiButton guibutton) {
        if(tile != null){
            if(guibutton.id >= 0 && guibutton.id < 6){
                if (tile.restrictDirections.get(Direction.values()[guibutton.id])) {
                    tile.restrictDirections.replace(Direction.values()[guibutton.id], false);
                } else {
                    tile.restrictDirections.replace(Direction.values()[guibutton.id], true);
                }

                guibutton.displayString = tile.restrictDirections.get(Direction.values()[guibutton.id]) ? "R" : "-";

            }
            if(guibutton.id == 6){
                switch (tile.mode){
                    case RANDOM:
                        tile.mode = PipeMode.SPLIT;
                        break;
                    case SPLIT:
                        tile.mode = PipeMode.RANDOM;
                        break;
                }
                guibutton.displayString = String.valueOf(tile.mode);
            }
        }
        super.buttonPressed(guibutton);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Configure: Restriction", 36, 6, 0xFF404040);
        fontRenderer.drawString("R/-", 78, 70, 0xFF404040);
        fontRenderer.drawString("Mode", 128, 70, 0xFF404040);
        fontRenderer.drawString("Y+", 26, 22, 0xFFFFFFFF);
        fontRenderer.drawString("Y-", 26, 58, 0xFFFFFFFF);
        fontRenderer.drawString("Z+", 26, 40, 0xFFFFFFFF);
        fontRenderer.drawString("X+", 44, 40, 0xFFFFFFFF);
        fontRenderer.drawString("Z-", 44, 58, 0xFFFFFFFF);
        fontRenderer.drawString("X-", 8, 40, 0xFFFFFFFF);
    }
}
