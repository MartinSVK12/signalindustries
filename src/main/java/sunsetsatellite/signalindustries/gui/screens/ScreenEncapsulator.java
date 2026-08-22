package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.IExtendedScreenDraw;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.signalindustries.gui.menus.MenuEncapsulator;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityEncapsulator;
import sunsetsatellite.signalindustries.util.IO;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenEncapsulator extends ScreenFluid implements IExtendedScreenDraw {

    public Player player;
    public TileEntityEncapsulator tile;
    private ButtonElement storeButton;
    private ButtonElement cutButton;

    public ScreenEncapsulator(ContainerInventory inv, TileEntity tile) {
        super(new MenuEncapsulator(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityEncapsulator) tile;
        this.player = inv.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/encapsulator_awakened.png");

        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int counter;
        if (this.tile.isBurning()) {
            counter = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(x + 8, y + 17 + 12 - counter, 176, 16 - counter, 14, counter + 2);
        }

        counter = this.tile.getProgressScaled(25);
        this.drawTexturedModalRect(x + 149, y + 29, 176, 29, 12, counter + 1);
        if (this.tile.speedMultiplier > 1) {
            drawStringCenteredShadow(fontRenderer, this.tile.speedMultiplier + "x", x + xSize - 45, y + ySize / 2 - 16, tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer() {
        super.drawGuiContainerForegroundLayer();

        storeButton.enabled = tile.state == TileEntityEncapsulator.State.NONE;
        cutButton.enabled = tile.state == TileEntityEncapsulator.State.NONE;

        cutButton.enabled = false; //todo: unfinished, so it is disabled
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) - 80, Math.round((float) height / 2) - 25, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) - 60, Math.round((float) height / 2) - 25, 20, 20, "I");
        buttons.add(itemIo);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;
        storeButton = new ButtonElement(2, Math.round((float) width / 2), Math.round((float) height / 2) - 50, 60, 20, "Store");
        cutButton = new ButtonElement(3, Math.round((float) width / 2) - 60, Math.round((float) height / 2) - 50, 60, 20, "Cut");
        storeButton.enabled = tile.state == TileEntityEncapsulator.State.NONE;
        cutButton.enabled = tile.state == TileEntityEncapsulator.State.NONE;
        buttons.add(storeButton);
        buttons.add(cutButton);
        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (!button.enabled) return;

		if (button == itemIoButton) {
			mc.displayScreen(new ScreenIO((MenuComposed) inventorySlots, scene("configure"), IO.ITEM));
		} else if (button == fluidIoButton) {
			mc.displayScreen(new ScreenIO((MenuComposed) inventorySlots, scene("configure"), IO.FLUID));
		}

        if (button.id == 2) {
            tile.state = TileEntityEncapsulator.State.STORING;
        } else if (button.id == 3) {
            tile.state = TileEntityEncapsulator.State.CUTTING;
        }
        tile.reset();

        if (EnvironmentHelper.isMultiplayerClient()) {
            NetworkHandler.sendToServer(new PacketScreenAction(button.id, 0, 0, tile.getPosition(), tile.getClass()));
        }
        super.buttonClicked(button);
    }

    @Override
    public void drawAfterSlotAndButtonRendering(int mx, int my, float partialTick) {


        int color = 0xFFFFA500;
        drawStringCenteredShadow(fontRenderer,Catalyst.translateNameKey(tile.getNameTranslationKey()), 90, 6, color);
        drawStringCenteredShadow(fontRenderer,tile.structure.size() + " blocks.", 90, 70, color);
        drawStringCenteredShadow(fontRenderer,String.valueOf(tile.size), 90, 60, color);
    }
}
