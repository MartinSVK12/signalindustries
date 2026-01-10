package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.menus.MenuBonsaiPot;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityBonsaiPot;
import turniplabs.halplibe.helper.RecipeBuilder;

public class ScreenBonsaiPot extends ScreenMachineSimple {

    public Player player;
    public TileEntityBonsaiPot tile;

    public ScreenBonsaiPot(ContainerInventory inv, TileEntity tile) {
        super(new MenuBonsaiPot(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityBonsaiPot) tile;
        this.player = inv.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/basic_bonsai_pot_gui.png");
        switch (tile.tier) {
            case PROTOTYPE:
            case BASIC:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/basic_bonsai_pot_gui.png");
                break;
            case REINFORCED:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/reinforced_bonsai_pot_gui.png");
                break;
            case AWAKENED:
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        ItemStack stack = tile.getItem(0);
        if (stack != null) {
            if (RecipeBuilder.getItemGroup("minecraft", "saplings").stream().anyMatch(s -> s.isItemEqual(stack))) {
                this.drawTexturedModalRect(x + 43, y + 14, 176, 26, 54, 57);
            }
            if (stack.getItem().id == SIBlocks.ashenTreeSapling.id()) {
                this.drawTexturedModalRect(x + 43, y + 14, 176, 83, 54, 57);
            }
        }
        int counter;
        if (this.tile.isBurning()) {
            counter = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(x + 9, y + 39 + 12 - counter, 176, 12 - counter, 14, counter + 2);
        }

        counter = this.tile.getProgressScaled(12);
        if (stack != null && stack.getItem().id == SIBlocks.ashenTreeSapling.id()) {
            this.drawTexturedModalRect(x + 64, y + 17 + 12 - counter, 189, 14 + 12 - counter, 12, counter);
        } else {
            this.drawTexturedModalRect(x + 64, y + 17 + 12 - counter, 176, 14 + 12 - counter, 12, counter);
        }

        if (this.tile.speedMultiplier > 1) {
            this.drawStringCentered(font, this.tile.speedMultiplier + "x", x + xSize - 16, y + ySize / 2 - 16, tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }
    }

    @Override
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
        font.drawCenteredString(I18n.getInstance().translateNameKey(tile.getNameTranslationKey()), 90, 4, color);
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 65, Math.round((float) height / 2) - 80, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) + 65, Math.round((float) height / 2) - 60, 20, 20, "I");
        buttons.add(itemIo);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;
        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (button == itemIoButton) {
            mc.displayScreen(new ScreenItemIOConfig(mc.thePlayer, fluidSlots, this, tile));
        } else if (button == fluidIoButton) {
            mc.displayScreen(new ScreenFluidIOConfig(mc.thePlayer, fluidSlots, this, tile));
        }
        super.buttonClicked(button);
    }
}
