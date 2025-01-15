package sunsetsatellite.signalindustries.gui;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.GuiFluid;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerMultiblock;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMultiblock;

public class GuiMultiblock extends GuiFluid {

    public String name = "Unknown Multiblock";
    public EntityPlayer entityplayer;
    public TileEntityTieredMultiblock tile;


    public GuiMultiblock(InventoryPlayer inventoryPlayer, TileEntity tile) {
        super(new ContainerMultiblock(inventoryPlayer, (TileEntityFluidItemContainer) tile),inventoryPlayer);
        this.tile = (TileEntityTieredMultiblock) tile;
        this.entityplayer = inventoryPlayer.player;
        this.name = ((TileEntityTieredMultiblock) tile).multiblock.data.getTranslatedName();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {
        int i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/prototype_multiblock_gui.png");
        switch (((BlockContainerTiered)tile.getBlockType()).tier){
            case PROTOTYPE:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/prototype_multiblock_gui.png");
                break;
            case BASIC:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/basic_multiblock_gui.png");
                break;
            case REINFORCED:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/reinforced_multiblock_gui.png");
                break;
            case AWAKENED:
                i2 = this.mc.renderEngine.getTexture("/assets/signalindustries/gui/awakened_multiblock_gui.png");
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(i2);
        int i3 = (this.width - this.xSize) / 2;
        int i4 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i3, i4, 0, 0, this.xSize, this.ySize);
        int i5;
        if(this.tile.isBurning()) {
            i5 = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(i3 + 56, i4 + 36 + 12 - i5, 176, 12 - i5, 14, i5 + 2);
        }

        i5 = this.tile.getProgressScaled(24);
        this.drawTexturedModalRect(i3 + 79, i4 + 34, 176, 14, i5 + 1, 16);
        /*if(this.tile.speedMultiplier > 1){
            this.drawStringCentered(fontRenderer, this.tile.speedMultiplier+"x",i3 + xSize - 16,i4 + ySize/2 - 16,tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }*/
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
        fontRenderer.drawStringWithShadow(name, 10, 10, color);
        if (tile.isBurning()) {
            fontRenderer.drawStringWithShadow("Current Parallel: "+ TextFormatting.ORANGE+tile.parallel, 10, 20, 0xFFFFFFFF);
        } else {
            fontRenderer.drawStringWithShadow("Max Parallel: "+ TextFormatting.ORANGE+tile.parallel, 10, 20, 0xFFFFFFFF);
        }
        fontRenderer.drawStringWithShadow("Speed Multiplier: "+ TextFormatting.MAGENTA+tile.speedMultiplier+"x", 10, 30, 0xFFFFFFFF);
        if(tile.isDisabled()){
            fontRenderer.drawStringWithShadow("Disabled", 10, 50, 0xFFFF0000);
        } else if (tile.isBurning()) {
            fontRenderer.drawStringWithShadow(String.format("Processing: %d%%",tile.getProgressScaled(100)), 10, 50, 0xFF00FF00);
        } else {
            fontRenderer.drawStringWithShadow(TextFormatting.LIGHT_GRAY+"Idling..", 10, 50, 0xFFFFFFFF);
        }
    }
}
