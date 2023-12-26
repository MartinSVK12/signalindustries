package sunsetsatellite.signalindustries.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.GuiFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerFluidHatch;
import sunsetsatellite.signalindustries.inventories.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredContainer;

public class GuiFluidHatch extends GuiFluid {

    public String name = "Fluid Hatch";
    public EntityPlayer entityplayer;
    public TileEntityFluidHatch tile;


    public GuiFluidHatch(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerFluidHatch(inventoryPlayer, (TileEntityTieredContainer) tile),inventoryPlayer);
        this.ySize = 192;
        this.tile = (TileEntityFluidHatch) tile;
        this.entityplayer = inventoryPlayer.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/reinforced_fluid_hatch.png");
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
            case BASIC:
            case REINFORCED:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/reinforced_fluid_hatch.png");
                break;
            case AWAKENED:
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
        int i5;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
                break;
            case BASIC:
                color = 0xFFFF8080;
                break;
            case REINFORCED:
                color = 0xFFFF0000;
                break;
            case AWAKENED:
                color = 0xFFFFA500;
                break;
        }
        fontRenderer.drawCenteredString(name, 90, 6, color);
    }
    protected void buttonPressed(GuiButton guibutton) {
        if (!guibutton.enabled) {
            return;
        }
        switch (guibutton.id){
            case 0:
                SignalIndustries.displayGui(entityplayer, new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            case 1:
                SignalIndustries.displayGui(entityplayer, new GuiItemIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            default:
                break;
        }
    }

    public void init()
    {
        GuiButton fluid = new GuiButton(0, Math.round(width / 2) + 63, Math.round(height / 2) - 90, 20, 20, "F");
        controlList.add(fluid);
        GuiButton item = new GuiButton(1, Math.round(width / 2) + 63, Math.round(height / 2) - 70, 20, 20, "I");
        item.enabled = false;
        controlList.add(item);
        super.init();
    }
}
