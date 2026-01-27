package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.menus.MenuAssembler;
import sunsetsatellite.signalindustries.render.FakeItemElement;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityAssembler;

public class ScreenAssembler extends ScreenFluid {

    public Player player;
    public TileEntityAssembler tile;

    public ScreenAssembler(ContainerInventory inv, TileEntity tile) {
        super(new MenuAssembler(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityAssembler) tile;
        this.player = inv.player;
        ySize = 215;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/basic_assembler_gui.png");
        switch (tile.tier) {
            case PROTOTYPE:
            case BASIC:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/basic_assembler_gui.png");
                break;
            case REINFORCED:
            case AWAKENED:
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
            this.drawTexturedModalRect(x + 153, y + 18 + 12 - counter, 176, 28 - counter, 14, counter + 2);
        }

        counter = this.tile.getProgressScaled(24);
        this.drawTexturedModalRect(x + 85, y + 35, 176, 0, counter + 1, 16);
        if (this.tile.speedMultiplier > 1) {
            this.drawString(font, this.tile.speedMultiplier + "x", x + 6, y + 6, tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
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
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 40, Math.round((float) height / 2) - 50, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 50, 20, 20, "I");
        buttons.add(itemIo);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;
        super.init();
    }

    @Override
    public void render(int i1, int i2, float f3) {
        super.render(i1, i2, f3);
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        Slot slot = this.inventorySlots.slots.get(0);
        FakeItemElement guiRenderFakeItem = new FakeItemElement(mc);
        if (tile.recipe != null && slot.getItemStack() == null) {
            guiRenderFakeItem.render(tile.recipe.getOutput(), i + slot.x, j + slot.y);
        }
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (!button.enabled) return;

        if (button == itemIoButton) {
            mc.displayScreen(new ScreenVisualItemIOConfig(mc.thePlayer, fluidSlots, this, tile));
        } else if (button == fluidIoButton) {
            mc.displayScreen(new ScreenVisualFluidIOConfig(mc.thePlayer, fluidSlots, this, tile));
        }
        super.buttonClicked(button);
    }
}
