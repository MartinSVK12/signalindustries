package sunsetsatellite.signalindustries.gui;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.GuiFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerCollector;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityCollector;

public class GuiCollector extends GuiFluid {

    public String name = "Signalum Collector";
    public EntityPlayer entityplayer;
    public TileEntityCollector tile;


    public GuiCollector(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerCollector(inventoryPlayer, (TileEntityTieredContainer) tile),inventoryPlayer);
        this.ySize = 192;
        this.tile = (TileEntityCollector) tile;
        this.entityplayer = inventoryPlayer.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/basic_energy_collector.png");
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
            case BASIC:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/basic_energy_collector.png");
                break;
            case REINFORCED:
            case AWAKENED:
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
        int i5;
        if(tile.isBurning()){
            i5 = tile.getProgressScaled(12);
            this.drawTexturedModalRect(i3 + 80, i4 + 17 + 12 - i5, 176, 12 - i5, 14, i5 + 2);
        }
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
                SignalIndustries.displayGui(entityplayer, () -> new GuiFluidIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            case 1:
                SignalIndustries.displayGui(entityplayer, () -> new GuiItemIOConfig(entityplayer,inventorySlots, tile, this), inventorySlots, tile,tile.x,tile.y,tile.z);
                break;
            default:
                break;
        }
    }

    public void init()
    {
        controlList.add(new GuiButton(0, Math.round(width / 2) + 63, Math.round(height / 2) - 90, 20, 20, "F"));
        controlList.add(new GuiButton(1, Math.round(width / 2) + 63, Math.round(height / 2) - 70, 20, 20, "I"));
        super.init();
    }
}
