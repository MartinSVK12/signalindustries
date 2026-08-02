package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.Color;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.CatalystEnergy;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.signalindustries.gui.menus.MenuSignalumDynamo;
import sunsetsatellite.signalindustries.tiles.machines.TileEntitySignalumDynamo;
import sunsetsatellite.signalindustries.util.IO;

import static sunsetsatellite.signalindustries.SignalIndustries.scene;

public class ScreenSignalumDynamo extends ScreenFluid {

    public Player player;
    public TileEntitySignalumDynamo tile;

    public ScreenSignalumDynamo(ContainerInventory inv, TileEntity tile) {
        super(new MenuSignalumDynamo(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntitySignalumDynamo) tile;
        this.player = inv.player;
    }

    @Override
    public void render(int x, int y, float renderPartialTicks) {
        int i = (width - xSize) / 2;
        int j = (height - ySize) / 2;
        super.render(x, y, renderPartialTicks);
        I18n trans = I18n.getInstance();
        StringBuilder text = new StringBuilder();
        if (x > i + 80 && x < i + 94) {
            if (y > j + 40 && y < j + 46) {
                text.append(CatalystEnergy.ENERGY_NAME).append(": ").append(tile.getEnergy()).append(" ").append(CatalystEnergy.ENERGY_SUFFIX).append("/").append(tile.getCapacity()).append(" ").append(CatalystEnergy.ENERGY_SUFFIX);
                TooltipElement tooltip = new TooltipElement(mc);
                tooltip.render(text.toString(), x, y, 8, -8);
                //this.drawTooltip(text.toString(),x,y,8,-8,true);
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/dynamo_basic.png");
        switch (tile.tier) {
            case PROTOTYPE:
            case BASIC:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/dynamo_basic.png");
                break;
            case REINFORCED:
            case AWAKENED:
                break;
        }
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(bg);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int color;
        //1 (red, empty) -> 0.65 (green, full)
        double color_mapped = Catalyst.map((float) tile.getEnergy() / (float) tile.getCapacity(), 0, 1, 1, 0.65);
        double x_mapped = Catalyst.map((float) tile.getEnergy() / (float) tile.getCapacity(), 0, 1, 0, 15);
        Color c = new Color();
        byte[] colorArray = Catalyst.HSBtoRGB((float) color_mapped, 1.0F, 1.0F);
        c.setRGB(colorArray[0], colorArray[1], colorArray[2]);
        color = c.getAlpha() << 24 | c.getRed() << 16 | c.getBlue() << 8 | c.getGreen();
        drawRectWidthHeight(x + 80, y + 40, (int) x_mapped, 7, color);
		//GLRenderer.setShader(Shaders.COLOR);
        GLRenderer.setColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int counter;
        if (this.tile.isBurning()) {
            counter = this.tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(x + 9, y + 39 + 12 - counter, 176, 12 - counter, 14, counter + 2);
        }
        if (this.tile.speedMultiplier > 1) {
            drawStringCenteredShadow(fontRenderer, this.tile.speedMultiplier + "x", x + xSize - 16, y + ySize / 2 - 16, tile.speedMultiplier >= 3 ? 0xFFFFA500 : (tile.speedMultiplier >= 2 ? 0xFFFF00FF : 0xFFFF8080));
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
			mc.displayScreen(new ScreenIO((MenuComposed) inventorySlots, scene("configure"), IO.ITEM));
		} else if (button == fluidIoButton) {
			mc.displayScreen(new ScreenIO((MenuComposed) inventorySlots, scene("configure"), IO.FLUID));
		}
		super.buttonClicked(button);
	}
}
