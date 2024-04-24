package sunsetsatellite.signalindustries.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.ContainerFluid;
import sunsetsatellite.catalyst.fluids.impl.GuiFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerAlloySmelter;
import sunsetsatellite.signalindustries.containers.ContainerReinforcedExtractor;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityAlloySmelter;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityReinforcedExtractor;

public class GuiReinforcedExtractor extends GuiFluid {

    public String name = "Extraction Manifold";
    public EntityPlayer entityplayer;
    public TileEntityReinforcedExtractor tile;


    public GuiReinforcedExtractor(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerReinforcedExtractor(inventoryPlayer, (TileEntityTieredContainer) tile),inventoryPlayer);
        this.tile = (TileEntityReinforcedExtractor) tile;
        this.entityplayer = inventoryPlayer.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/reinforced_extractor.png");
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
            case BASIC:
            case REINFORCED:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/reinforced_extractor.png");
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
        i5 = this.tile.getBurnTimeRemainingScaled(12);
        if(this.tile.isBurning()) {
            this.drawTexturedModalRect(i3 + 82, i4 + 20 + 12 - i5, 176, 12 - i5, 14, i5 + 2);
        }
        i5 = this.tile.getProgressScaled(54);
        this.drawTexturedModalRect(i3 + 61, i4 + 57, 176, 14, i5 + 1, 7);
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
        GuiButton button = new GuiButton(0, Math.round(width / 2) + 60, Math.round(height / 2) - 80, 20, 20, "F");
        button.enabled = false;
        controlList.add(button);
        controlList.add(new GuiButton(1, Math.round(width / 2) + 60, Math.round(height / 2) - 60, 20, 20, "I"));
        super.init();
    }

}
