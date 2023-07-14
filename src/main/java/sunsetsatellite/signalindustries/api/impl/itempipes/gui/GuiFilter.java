package sunsetsatellite.signalindustries.api.impl.itempipes.gui;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.api.impl.itempipes.containers.ContainerFilter;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityFilter;

public class GuiFilter extends GuiContainer {
    public String name = "Filter";
    public EntityPlayer entityplayer;
    public TileEntityFilter tile;


    public GuiFilter(InventoryPlayer inventoryPlayer, TileEntityFilter tile) {
        super(new ContainerFilter(inventoryPlayer, tile));
        this.tile = tile;
        this.entityplayer = inventoryPlayer.player;
        this.ySize = 222;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {

        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/filter.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawCenteredString(name, 90, 6, 0xFFFFFFFF);
    }

    public void initGui()
    {
        super.initGui();
    }
}
