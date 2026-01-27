package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.fluids.impl.ScreenFluid;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.menus.MenuAutoMiner;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageAutoMinerStart;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityAutoMiner;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenAutoMiner extends ScreenFluid {

    public Player player;
    public TileEntityAutoMiner tile;

    public ScreenAutoMiner(ContainerInventory inv, TileEntity tile) {
        super(new MenuAutoMiner(inv, (TileEntityFluidItemContainer) tile));
        this.tile = (TileEntityAutoMiner) tile;
        this.player = inv.player;
        if (this.tile.tier == Tier.REINFORCED) {
            ySize = 249;
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f) {
        super.drawGuiContainerBackgroundLayer(f);
        Texture bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/autominer.png");
        switch (tile.tier) {
            case PROTOTYPE:
            case BASIC:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/autominer.png");
                break;
            case REINFORCED:
                bg = this.mc.textureManager.loadTexture("/assets/signalindustries/gui/reinforced_autominer.png");
                break;
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
            counter = tile.getBurnTimeRemainingScaled(12);
            this.drawTexturedModalRect(x + 9, y + 36 + 12 - counter, 176, 12 - counter, 14, counter + 2);
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
        font.drawCenteredString(I18n.getInstance().translateNameKey(tile.getNameTranslationKey()), 90, 6, color);
        font.drawStringWithShadow("X: " + (tile.current.x - tile.x), 38, 20, 0xFFFF0000);
        font.drawStringWithShadow("Y: " + (tile.current.y - tile.y), 38, 32, 0xFF4080FF);
        font.drawStringWithShadow("Z: " + (tile.current.z - tile.z), 38, 44, 0xFF00FF00);
        font.drawStringWithShadow("C: " + (tile.cost) + "/" + (tile.fuelMaxBurnTicks) + "t", 80, 32, 0xFF800000);
        font.drawStringWithShadow("S: " + tile.workTimer.max + "t", 80, 44, 0xFFFF8000);
        font.drawStringWithShadow("M: " + tile.multiplier, 80, 56, 0xFFFF00FF);

        if (tile.tier == Tier.REINFORCED) {
            font.drawCenteredString("Size", 42, 82, 0xFFFF0000);
            font.drawString(String.valueOf(tile.size.x), 35, 100, 0xFFFFFFFF, true);
            font.drawString(String.valueOf(tile.size.y), 35, 125, 0xFFFFFFFF, true);
        }
    }

    public ButtonElement itemIoButton;
    public ButtonElement fluidIoButton;

    @Override
    public void init() {
        ButtonElement fluidIo = new ButtonElement(0, Math.round((float) width / 2) + 60, tile.tier == Tier.BASIC ? Math.round(height / 2f) - 80 : Math.round(height / 2f) - 120, 20, 20, "F");
        buttons.add(fluidIo);
        ButtonElement itemIo = new ButtonElement(1, Math.round((float) width / 2) + 60, tile.tier == Tier.BASIC ? Math.round(height / 2f) - 60 : Math.round(height / 2f) - 100, 20, 20, "I");
        buttons.add(itemIo);
        fluidIoButton = fluidIo;
        itemIoButton = itemIo;
        buttons.add(new ButtonElement(2, Math.round((float) width / 2) - 81, tile.tier == Tier.BASIC ? Math.round(height / 2f) - 80 : Math.round(height / 2f) - 120, 20, 20, tile.workTimer.isPaused() ? "OFF" : "ON"));

        if (tile.tier == Tier.REINFORCED) {
            buttons.add(new ButtonElement(4, Math.round(width / 2f) - 75, Math.round(height / 2f) - 30, 20, 20, "-"));
            buttons.add(new ButtonElement(3, Math.round(width / 2f) - 35, Math.round(height / 2f) - 30, 20, 20, "+"));
            buttons.add(new ButtonElement(6, Math.round(width / 2f) - 75, Math.round(height / 2f) - 5, 20, 20, "-"));
            buttons.add(new ButtonElement(5, Math.round(width / 2f) - 35, Math.round(height / 2f) - 5, 20, 20, "+"));
        }

        super.init();
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (!button.enabled) return;

        if (button == itemIoButton) {
            mc.displayScreen(new ScreenVisualItemIOConfig(mc.thePlayer, fluidSlots, this, tile));
        } else if (button == fluidIoButton) {
            mc.displayScreen(new ScreenVisualFluidIOConfig(mc.thePlayer, fluidSlots, this, tile));
        } else if (button.id == 2) {
            if (tile.workTimer.isPaused()) {
                tile.workTimer.unpause();
            } else {
                tile.workTimer.pause();
            }
            button.displayString = tile.workTimer.isPaused() ? "OFF" : "ON";
            if (EnvironmentHelper.isClientWorld()) {
                NetworkHandler.sendToServer(new NetworkMessageAutoMinerStart(tile.getPosition(), tile.getClass()));
            }
        } else if (button.id == 3) { //todo: do this better lol
            if (tile.size.x < tile.maxSize.x) {
                tile.size.x++;
            }
        } else if (button.id == 4) {
            if (tile.size.x > 3) {
                tile.size.x--;
            }
        } else if (button.id == 5) {
            if (tile.size.y < tile.maxSize.y) {
                tile.size.y++;
            }
        } else if (button.id == 6) {
            if (tile.size.y > 3) {
                tile.size.y--;
            }
        }

        if (EnvironmentHelper.isClientWorld()) {
            NetworkHandler.sendToServer(new PacketScreenAction(button.id, 0, 0, tile.getPosition(), tile.getClass()));
        }

        super.buttonClicked(button);
    }
}
