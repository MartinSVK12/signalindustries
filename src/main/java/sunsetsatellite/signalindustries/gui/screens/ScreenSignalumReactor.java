package sunsetsatellite.signalindustries.gui.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageReactorStart;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntitySignalumReactor;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenSignalumReactor extends Screen {

    public TileEntitySignalumReactor tile;
    public Player player;

    public int xSize;
    public int ySize;
    public TooltipElement tooltip;

    public ScreenSignalumReactor(ContainerInventory inventory, TileEntity tile) {
        this.xSize = 256;
        this.ySize = 166;
        this.tile = (TileEntitySignalumReactor) tile;
        this.player = inventory.player;
        this.tooltip = new TooltipElement(Minecraft.getMinecraft());
    }

    @Override
    public void render(int mx, int my, float partialTick) {
        this.renderBackground();
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(partialTick);
        GL11.glPushMatrix();
        GL11.glTranslatef(centerX, centerY, 0.0f);
        this.drawGuiContainerForegroundLayer();
        GL11.glPopMatrix();
        super.render(mx, my, partialTick);
    }

    private void drawGuiContainerForegroundLayer() {
		drawStringCenteredShadow(fontRenderer, Catalyst.translateNameKey("container.signalindustries.reactor"), 128, 6, 0xFFFF0000);
        //fontRenderer.drawCenteredString("State: "+tile.state,128,20,0xFFFFFFFF);
        //fontRenderer.drawCenteredString("Fuel: "+tile.getFuel()+"|"+tile.getDepletedFuel(),128,30,0xFFFFFFFF);
        float capacity = ((float) (tile.getFuel() + tile.getDepletedFuel()) / (4000 * 9)) * 100;
        float fill = 100 - ((float) tile.getDepletedFuel() / (tile.getFuel() + tile.getDepletedFuel())) * 100;
        if (Float.isNaN(fill)) {
            fill = 0;
        }
        int color = switch (tile.state) {
	        case INACTIVE -> 0xFF404040;
	        case IGNITING -> 0xFFFF8000;
	        case RUNNING -> 0xFF00FF00;
        };
		drawStringCenteredShadow(fontRenderer, String.format("%.0f%%", fill), 128, 63, 0xFFFFFFFF);
		drawStringCenteredShadow(fontRenderer, String.valueOf(tile.state), 128, 77, color);
		drawStringCenteredShadow(fontRenderer, String.format("%.0f%%", capacity), 128, 90, 0xFF808080);
    }

    private void drawGuiContainerBackgroundLayer(float partialTick) {
        @NotNull Texture tex = this.mc.textureManager.loadTexture("/assets/signalindustries/textures/gui/container/old/signalum_reactor_ui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.textureManager.bindTexture(tex);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(x, y, 0, 0, this.xSize, this.ySize);
        int h = (tile.getFuel() + tile.getDepletedFuel()) * 64 / (4000 * 9);
        int depletedH = (int) ((tile.getDepletedFuel() * h) / ((tile.getFuel() + tile.getDepletedFuel()) == 0 ? Float.MIN_VALUE : tile.getFuel() + tile.getDepletedFuel()));
        this.drawTexturedModalRect(x + 96, y + 50 + (64 - h), 0, 166 + (64 - h), 64, h);
        this.drawTexturedModalRect(x + 96, y + 50 + (64 - depletedH), 64, 166 + (64 - depletedH), 64, depletedH);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.mc.thePlayer.isAlive() || this.mc.thePlayer.removed) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (button.id == 0) {
            if (EnvironmentHelper.isMultiplayerClient()) {
                NetworkHandler.sendToServer(new NetworkMessageReactorStart(tile.getPosition(), tile.getClass()));
            }
            tile.start();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void init() {
        buttons.add(new ButtonElement(0, Math.round((float) width / 2) - 30, Math.round((float) height / 2) + 50, 60, 20, "ON/OFF"));
        super.init();
    }
}
