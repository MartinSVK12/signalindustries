package sunsetsatellite.signalindustries.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiRenderItem;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTooltip;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;

public abstract class GuiTileEntity<TILE extends TileEntity> extends GuiScreen {
    public TILE tile;
    public EntityPlayer player;

    public int xSize;
    public int ySize;
    GuiTooltip guiTooltip;
    GuiRenderItem guiRenderItem;

    public GuiTileEntity(InventoryPlayer inventory, TILE tile) {
        mc = Minecraft.getMinecraft(Minecraft.class);
        this.tile = tile;
        this.player = inventory.player;
        this.guiTooltip = new GuiTooltip(mc);
        this.guiRenderItem = new GuiRenderItem(mc);
        this.xSize = 176;
        this.ySize = 166;
    }

    @Override
    public void drawScreen(int x, int y, float renderPartialTicks) {
        this.drawDefaultBackground();
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(renderPartialTicks);
        GL11.glPushMatrix();
        GL11.glTranslatef(centerX, centerY, 0.0f);
        this.drawGuiContainerForegroundLayer();
        GL11.glPopMatrix();
        super.drawScreen(x, y, renderPartialTicks);
    }

    protected abstract void drawGuiContainerBackgroundLayer(float f);

    protected void drawGuiContainerForegroundLayer() {
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!this.mc.thePlayer.isAlive() || this.mc.thePlayer.removed) {
            this.mc.thePlayer.closeScreen();
        }
    }
}
