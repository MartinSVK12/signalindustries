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
import sunsetsatellite.signalindustries.covers.CoverBase;
import sunsetsatellite.signalindustries.covers.SwitchCover;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.util.IOPreview;


public class GuiSwitchCoverConfig extends GuiScreen {

    public EntityPlayer entityplayer;
    public TileEntityTieredMachineBase tile;
    public int xSize = 176;
    public int ySize = 90;
    public SwitchCover cover;
    private static final ItemEntityRenderer itemRenderer = new ItemEntityRenderer();
    public GuiSwitchCoverConfig(EntityPlayer player, TileEntity tile, SwitchCover switchCover) {
        super();
        this.tile = (TileEntityTieredMachineBase) tile;
        this.entityplayer = player;
        this.cover = switchCover;
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
        int i = mc.renderEngine.getTexture("/assets/signalindustries/gui/config.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartialTicks) {
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
        super.drawScreen(mouseX, mouseY, renderPartialTicks);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(2929);
    }

    @Override
    public void init() {
        controlList.add(new GuiButton(0, (width / 2) - 15, (height / 2) - 10, 30, 20,  tile.disabled ? "OFF" : "ON"));

        super.init();
    }

    @Override
    protected void buttonPressed(GuiButton guibutton) {
        if(tile != null){
            switch (guibutton.id){
                case 0: {
                    tile.disabled = !tile.disabled;
                    guibutton.displayString = tile.disabled ? "OFF" : "ON";
                    break;
                }
            }
        }
        super.buttonPressed(guibutton);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString("Configure: Switch", 45, 6, 0xFF404040);
    }
}
