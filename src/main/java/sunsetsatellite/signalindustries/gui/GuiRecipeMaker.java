package sunsetsatellite.signalindustries.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.api.GuiFluid;
import sunsetsatellite.fluidapi.template.containers.ContainerFluidTank;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerRecipeMaker;
import sunsetsatellite.signalindustries.tiles.TileEntityRecipeMaker;

public class GuiRecipeMaker extends GuiContainer {

    public String name = "Recipe Maker";
    public EntityPlayer entityplayer;
    public TileEntity tile;
    public GuiRecipeMaker(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerRecipeMaker(inventoryPlayer, (TileEntityRecipeMaker) tile));
        this.entityplayer = inventoryPlayer.player;
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("/gui/crafting.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawString(name, 64, 6, 0xFF404040);
    }


    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        switch (guibutton.id){
            case 0:
                ((TileEntityRecipeMaker)tile).makeRecipe();
                //SignalIndustries.displayGui(entityplayer, new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, (IInventory) tile);
                break;
            case 1:
                ((TileEntityRecipeMaker)tile).deleteContents();
            default:
                break;
        }
    }

    public void initGui()
    {
        controlList.add(new GuiButton(0, Math.round(width / 2) - 80, Math.round(height / 2) - 80, 20, 20, "+"));
        controlList.add(new GuiButton(1, Math.round(width / 2) - 80, Math.round(height / 2) - 55, 20, 20, "X"));
        super.initGui();
    }
}

