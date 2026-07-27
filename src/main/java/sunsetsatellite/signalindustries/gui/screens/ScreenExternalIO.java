package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.Side;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.signalindustries.gui.menus.MenuExternalIO;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageExternalIOLinkBreak;
import sunsetsatellite.signalindustries.tiles.TileEntityExternalIO;
import sunsetsatellite.signalindustries.util.IO;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenExternalIO extends ScreenFluid {

    public Player player;
    public TileEntityExternalIO tile;

    public ScreenExternalIO(ContainerInventory inv, TileEntityExternalIO tile) {
        super(new MenuExternalIO(inv, tile));
        this.tile = tile;
        this.player = inv.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/basic_gui_blank.png");
        switch (tile.tier) {
            case PROTOTYPE:
            case BASIC:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/basic_gui_blank.png");
                break;
            case REINFORCED:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/reinforced_gui_base.png");
                break;
            case AWAKENED:
                break;
        }
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
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
		drawStringCenteredShadow(fontRenderer, Catalyst.translateNameKey(tile.getNameTranslationKey()), 90, 6, color);

        int x = (this.xSize) / 2;
        int y = (this.ySize) / 4;
        if (tile.tier == Tier.REINFORCED) {
            if (tile.externalTilePos == null) {
                drawStringCenteredShadow(fontRenderer,"Disconnected.", x, y, 0xFFFF0000);
                drawStringCenteredShadow(fontRenderer,"No position.", x, y + 12, 0xFFFF0000);
            } else if (tile.externalTilePos.containsKey("x") && tile.externalTilePos.containsKey("y") && tile.externalTilePos.containsKey("z") && tile.externalTilePos.containsKey("dim") && tile.externalTilePos.containsKey("side")) {
                if (tile.externalTile != null) {
                    drawStringCenteredShadow(fontRenderer,"Connected!", x, y, 0xFF00FF00);
                    drawStringCenteredShadow(fontRenderer,tile.externalTile.getClass().getSimpleName().replace("TileEntity", "") + " @ " + tile.externalTile.tilePos, x, y + 12, 0xFF00FF00);
                    drawStringCenteredShadow(fontRenderer,String.valueOf(Side.fromId(tile.externalTilePos.getInteger("side"))), x, y + 24, 0xFF00FF00);
                } else {
                    int eX = tile.externalTilePos.getInteger("x");
                    int eY = tile.externalTilePos.getInteger("y");
                    int eZ = tile.externalTilePos.getInteger("z");
                    int dim = tile.externalTilePos.getInteger("dim");
                    Vec3i pos = new Vec3i(eX, eY, eZ);
                    Vec3f selfPos = new Vec3f(tile.tilePos);
                    if (tile.worldObj != null && dim != tile.worldObj.dimension.id) {
                        drawStringCenteredShadow(fontRenderer,"Can't connect.", x, y, 0xFFFFA500);
                        drawStringCenteredShadow(fontRenderer,"Outside this world.", x, y + 12, 0xFFFFA500);
                    } else if (pos.distanceTo(selfPos) > TileEntityExternalIO.range) {
                        drawStringCenteredShadow(fontRenderer,"Can't connect.", x, y, 0xFFFFA500);
                        drawStringCenteredShadow(fontRenderer,"Out of reach.", x, y + 12, 0xFFFFA500);
                    }
                }
            }
        } else {
            if (tile.externalTile != null) {
                drawStringCenteredShadow(fontRenderer,"Connected!", x, y, 0xFF00FF00);
                drawStringCenteredShadow(fontRenderer,tile.externalTile.getClass().getSimpleName().replace("TileEntity", "") + " @ " + tile.externalTileSide.getName(), x, y + 12, 0xFF00FF00);
            } else {
                drawStringCenteredShadow(fontRenderer,"Disconnected.", x, y, 0xFFFF0000);
            }
        }
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;
    public ButtonElement removeLinkButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 80, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) + 60, Math.round((float) height / 2) - 60, 20, 20, "I");
        buttons.add(itemIo);
        ButtonElement removeLink = new ButtonElement(2, Math.round((float) width / 2) - 80, Math.round((float) height / 2) - 80, 20, 20, "X");
        buttons.add(removeLink);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;
        removeLinkButton = removeLink;
        if (!(tile.externalTile instanceof Container)) {
            itemIo.enabled = false;
        }
        if (!(tile.externalTile instanceof IFluidInventory)) {
            fluidIo.enabled = false;
        }
        super.init();
    }

	@Override
	protected void buttonClicked(ButtonElement button) {
		if (!button.enabled) return;

		if (button == itemIoButton) {
			mc.displayScreen(new ScreenIO((MenuComposed) inventorySlots, scene("configure"), IO.ITEM));
		} else if (button == fluidIoButton) {
			mc.displayScreen(new ScreenIO((MenuComposed) inventorySlots, scene("configure"), IO.FLUID));
		} else if (button == removeLinkButton) {
			if (EnvironmentHelper.isMultiplayerClient()) {
				NetworkHandler.sendToServer(new NetworkMessageExternalIOLinkBreak(tile.getPosition(), tile.getClass()));
			}
			player.sendMessage("Link removed!");
			tile.externalTile = null;
			tile.externalTileSide = null;
			tile.externalTilePos = null;
		}
		super.buttonClicked(button);
	}
}
