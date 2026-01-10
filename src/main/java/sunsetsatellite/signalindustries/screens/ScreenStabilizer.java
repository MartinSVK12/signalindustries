package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.menus.MenuStabilizer;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityStabilizer;

public class ScreenStabilizer extends ScreenFluid {

    public Player player;
    public TileEntityStabilizer tile;

    public ScreenStabilizer(ContainerInventory inv, TileEntity tile) {
        super(new MenuStabilizer(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityStabilizer) tile;
        this.player = inv.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/redstone_booster.png");
        switch (tile.tier) {
            case BASIC:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/redstone_booster.png");
                break;
            case REINFORCED:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/dilithium_booster.png");
                break;
            case AWAKENED:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/awakened_booster.png");
                break;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int counter;
        if (this.tile.isBurning()) {
            counter = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(x + 44, y + 21 + 12 - counter, 176, 44 - counter, 14, counter + 2);
        }

        counter = this.tile.getProgressScaled(32);
        this.drawTexturedModalRect(x + 72, y + 62 - counter, 176, 32 - counter, 32, counter);
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
        font.drawCenteredString(I18n.getInstance().translateNameKey(tile.getNameTranslationKey()), 90, 6, color);
        if (tile.connectedTo != null) {
            font.drawCenteredString("Connected!", 90, 70, 0xFF00FF00);
        } else {
            font.drawCenteredString("Nothing to stabilize.", 90, 70, 0xFFFF0000);
        }
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 80, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 60, 20, 20, "I");
        buttons.add(itemIo);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;
        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (!button.enabled) return;

        if (button == itemIoButton) {
            mc.displayScreen(new ScreenItemIOConfig(mc.thePlayer, fluidSlots, this, tile));
        } else if (button == fluidIoButton) {
            mc.displayScreen(new ScreenFluidIOConfig(mc.thePlayer, fluidSlots, this, tile));
        }
        super.buttonClicked(button);
    }
}
