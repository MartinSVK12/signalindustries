package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.menus.MenuMultiblock;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;

public class ScreenMultiblock extends ScreenFluid {

    public String name = "Unknown Multiblock";
    public Player player;
    public TileEntityTieredMultiblock tile;

    public ScreenMultiblock(ContainerInventory inventoryPlayer, TileEntity tile) {
        super(new MenuMultiblock(inventoryPlayer, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityTieredMultiblock) tile;
        this.player = inventoryPlayer.player;
        this.name = ((TileEntityTieredMultiblock) tile).multiblock.data.getTranslatedName();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f1) {
        Texture bgTex = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/prototype_multiblock_gui.png");
        switch (tile.tier) {
            case PROTOTYPE:
                bgTex = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/prototype_multiblock_gui.png");
                break;
            case BASIC:
                bgTex = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/basic_multiblock_gui.png");
                break;
            case REINFORCED:
                bgTex = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/reinforced_multiblock_gui.png");
                break;
            case AWAKENED:
                bgTex = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/awakened_multiblock_gui.png");
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bgTex);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int counter;
        if (this.tile.isBurning()) {
            counter = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(x + 56, y + 36 + 12 - counter, 176, 12 - counter, 14, counter + 2);
        }

        counter = this.tile.getProgressScaled(24);
        this.drawTexturedModalRect(x + 79, y + 34, 176, 14, counter + 1, 16);
        /*if(this.tile.speedMultiplier > 1){
            this.drawStringCentered(fontRenderer, this.tile.speedMultiplier+"x",x + xSize - 16,y + ySize/2 - 16,tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }*/
    }

    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();
        int color = 0xFFFFFFFF;
        switch (tile.tier) {
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
        font.drawStringWithShadow(name, 10, 10, color);
        if (tile.isBurning()) {
            font.drawStringWithShadow("Current Parallel: " + TextFormatting.ORANGE + tile.parallel, 10, 20, 0xFFFFFFFF);
        } else {
            font.drawStringWithShadow("Max Parallel: " + TextFormatting.ORANGE + tile.parallel, 10, 20, 0xFFFFFFFF);
        }
        font.drawStringWithShadow("Speed Multiplier: " + TextFormatting.MAGENTA + tile.speedMultiplier + "x", 10, 30, 0xFFFFFFFF);
        if (tile.isDisabled()) {
            font.drawStringWithShadow("Disabled", 10, 50, 0xFFFF0000);
        } else if (tile.isBurning()) {
            font.drawStringWithShadow(String.format("Processing: %d%%", tile.getProgressScaled(100)), 10, 50, 0xFF00FF00);
        } else {
            font.drawStringWithShadow(TextFormatting.LIGHT_GRAY + "Idling..", 10, 50, 0xFFFFFFFF);
        }
    }
}
