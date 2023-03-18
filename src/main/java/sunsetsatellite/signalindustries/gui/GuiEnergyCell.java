package sunsetsatellite.signalindustries.gui;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.api.GuiFluid;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.fluidapi.template.containers.ContainerFluidTank;
import sunsetsatellite.signalindustries.SignalIndustries;

public class GuiEnergyCell extends GuiFluid {

    public String name = "Energy Cell";
    public EntityPlayer entityplayer;
    public TileEntity tile;
    public GuiEnergyCell(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerFluidTank(inventoryPlayer, (TileEntityFluidItemContainer) tile),inventoryPlayer);
        this.entityplayer = inventoryPlayer.player;
        this.tile = tile;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f)
    {
        int i = mc.renderEngine.getTexture("assets/fluidapi/gui/tank_gui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);

    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        fontRenderer.drawString(name, 60, 6, 0xFF404040);
    }

    protected void actionPerformed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        switch (guibutton.id){
            case 0:
                SignalIndustries.displayGui(entityplayer, new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, (IInventory) tile);
                break;
            default:
                break;
        }
    }

    public void initGui()
    {
        controlList.add(new GuiButton(0, Math.round(width / 2) - 80, Math.round(height / 2) - 80, 20, 20, "F"));
        super.initGui();
    }
}
