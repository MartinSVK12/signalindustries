package sunsetsatellite.signalindustries.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiContainer;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.signalindustries.containers.ContainerFilter;
import sunsetsatellite.signalindustries.inventories.TileEntityFilter;
import sunsetsatellite.signalindustries.inventories.TileEntityFilter.FilterSide;

public class GuiFilter extends GuiContainer {
    public String name = "Filter";
    public EntityPlayer entityplayer;
    public TileEntityFilter tile;


    public GuiFilter(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerFilter(inventoryPlayer, (TileEntityFilter) tile));
        this.tile = (TileEntityFilter) tile;
        this.entityplayer = inventoryPlayer.player;
        this.ySize = 233;
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

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawString(name, 76, 6, 4210752,false);
    }

    @Override
    protected void buttonPressed(GuiButton button) {
        if(button.id == 0){
            int ord = tile.defaultSide.ordinal();
            if(ord++ >= FilterSide.values().length-1){
                ord = 0;
            }
            tile.defaultSide = FilterSide.values()[ord];
            button.displayString = "Default: " + (ord+1) + " ("+tile.defaultSide.name()+")";
        } else if (button.id == 1) {
            tile.ignoreMeta = !tile.ignoreMeta;
            button.displayString = tile.ignoreMeta ? "!M" : "M";
        }
    }

    @Override
    public void init() {
        int w = (this.width - this.xSize) / 2;
        int h = (this.height - this.ySize) / 2;
        controlList.add(new GuiButton(0,w+8+22,h+128,120,20,"Default: "+(tile.defaultSide.ordinal()+1)+" ("+tile.defaultSide.name()+")"));
        controlList.add(new GuiButton(1,w+8,h+128,20,20, tile.ignoreMeta ? "!M" : "M"));
        super.init();
    }
}
