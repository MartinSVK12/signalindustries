package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;

import org.useless.dragonfly.data.entity.mojang.EntityGeometryMojangData;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.IExtendedScreenDraw;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.signalindustries.gui.menus.MenuPulsarBlock;
import sunsetsatellite.signalindustries.items.ItemWarpOrb;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityPulsar;
import sunsetsatellite.signalindustries.util.IO;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenPulsarBlock extends ScreenFluid implements IExtendedScreenDraw {

    public Player player;
    public TileEntityPulsar tile;

    public ScreenPulsarBlock(ContainerInventory inv, TileEntity tile) {
        super(new MenuPulsarBlock(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityPulsar) tile;
        this.player = inv.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/pulsar_block_ui.png");
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
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
            this.drawStringCentered(font, this.tile.speedMultiplier+"x",x + xSize - 16,y + ySize/2 - 16,tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
        }*/
        drawStringShadow(fontRenderer, (int) ((tile.progressTicks / (float) tile.progressMaxTicks) * 100) + "%", x + 24, y + 36, 0xFFFFFFFF);
        if (tile.getItem(0) != null && tile.getItem(0).getItem() instanceof ItemWarpOrb) {
            drawStringCenteredShadow(fontRenderer, "Warp", x + 140, y + 36, 0xFFFF00FF);
        } else {
            drawStringCenteredShadow(fontRenderer, "Pulse", x + 140, y + 36, 0xFFFF0000);
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
		drawStringCenteredShadow(fontRenderer, Catalyst.translateNameKey(tile.getNameTranslationKey()), 90, 6, color);
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 56, Math.round((float) height / 2) - 75, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) + 56, Math.round((float) height / 2) - 30, 20, 20, "I");
        buttons.add(itemIo);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;
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
		super.buttonClicked(button);
	}

	@Override
	public void drawAfterSlotAndButtonRendering(int mouseX, int mouseY, float partialTick) {
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().scale(2, 2, 2);
		Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/textures/block/pulsar.png").bind();
		if (tile.getItem(0) != null && tile.getItem(0).getItem() instanceof ItemWarpOrb) {
			Minecraft.getMinecraft().textureManager.loadTexture("/assets/signalindustries/textures/block/pulsar_warp.png").bind();
		}
		StaticEntityModel item = EntityGeometryMojangData.Cache.getModel("geometry.signalindustries.pulsar_item", 0);
		StaticEntityModel innerCore = EntityGeometryMojangData.Cache.getModel("geometry.signalindustries.pulsar_inner_core", 0);
		StaticEntityModel outerCore = EntityGeometryMojangData.Cache.getModel("geometry.signalindustries.pulsar_outer_core", 0);
		GLRenderer.enableState(State.BLEND);
		GLRenderer.setBlendFunc(BlendFactor.SRC_ALPHA, BlendFactor.ONE_MINUS_SRC_ALPHA);
		GLRenderer.modelM4f().translate(44, -10, 0);
		GLRenderer.modelM4f().rotate((float) Math.toRadians(tile.orbRotation * 20 + partialTick), 0, 1, 0);
		GLRenderer.modelM4f().scale(1.3f, 1.3f, 1.3f);
		if (tile.fuelBurnTicks <= 0) {
			item.render();
		}
		if (tile.progressTicks > tile.progressMaxTicks / 2) {
			innerCore.render();
		}
		if (tile.progressTicks >= tile.progressMaxTicks) {
			outerCore.render();
		}
		GLRenderer.disableState(State.BLEND);
		GLRenderer.popFrame();
	}
}
