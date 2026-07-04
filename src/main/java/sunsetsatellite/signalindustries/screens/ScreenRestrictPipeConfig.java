package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.PipeMode;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenRestrictPipeConfig extends Screen {

    public Player entityplayer;
    public TileEntityItemConduit tile;
    public int xSize = 176;
    public int ySize = 166;

    public ScreenRestrictPipeConfig(ContainerInventory playerInv, TileEntity tile) {
        super();
        this.entityplayer = playerInv.player;
        this.tile = (TileEntityItemConduit) tile;
    }

    @Override
    public void render(int mx, int my, float partialTick) {
        this.renderBackground();
        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawGuiContainerBackgroundLayer(partialTick);
        GL11.glPushMatrix();
        GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
        Lighting.enableInventoryLight();
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glTranslatef((float) centerX, (float) centerY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        Lighting.disable();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawGuiContainerForegroundLayer();
        GL11.glPopMatrix();
        super.render(mx, my, partialTick);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    @Override
    public void init() {
        buttons.add(new ButtonElement(2, Math.round((float) width / 2) - 10, Math.round((float) height / 2) - 63, 15, 15, tile.restrictDirections.get(Direction.Y_POS) ? "R" : "-")); //Y+
        buttons.add(new ButtonElement(4, Math.round((float) width / 2) - 10, Math.round((float) height / 2) - 48, 15, 15, tile.restrictDirections.get(Direction.Z_POS) ? "R" : "-")); //Z+
        buttons.add(new ButtonElement(3, Math.round((float) width / 2) - 10, Math.round((float) height / 2) - 33, 15, 15, tile.restrictDirections.get(Direction.Y_NEG) ? "R" : "-")); //Y-
        buttons.add(new ButtonElement(0, Math.round((float) width / 2) + 4, Math.round((float) height / 2) - 48, 15, 15, tile.restrictDirections.get(Direction.X_POS) ? "R" : "-")); //X+
        buttons.add(new ButtonElement(1, Math.round((float) width / 2) - 24, Math.round((float) height / 2) - 48, 15, 15, tile.restrictDirections.get(Direction.X_NEG) ? "R" : "-")); //X-
        buttons.add(new ButtonElement(5, Math.round((float) width / 2) + 4, Math.round((float) height / 2) - 33, 15, 15, tile.restrictDirections.get(Direction.Z_NEG) ? "R" : "-")); //Z-

        buttons.add(new ButtonElement(6, Math.round((float) width / 2) + 4 + 22, Math.round((float) height / 2) - 48, 50, 15, String.valueOf(tile.mode)));

        super.init();
    }

    @Override
    public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {
        if (eventKey == 1) {
            mc.thePlayer.closeScreen();
        }
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (tile != null) {
            if (button.id >= 0 && button.id < 6) {
                if (tile.restrictDirections.get(Direction.values()[button.id])) {
                    tile.restrictDirections.replace(Direction.values()[button.id], false);
                } else {
                    tile.restrictDirections.replace(Direction.values()[button.id], true);
                }

                button.displayString = tile.restrictDirections.get(Direction.values()[button.id]) ? "R" : "-";

            }
            if (button.id == 6) {
                switch (tile.mode) {
                    case RANDOM:
                        tile.mode = PipeMode.SPLIT;
                        break;
                    case SPLIT:
                        tile.mode = PipeMode.RANDOM;
                        break;
                }
                button.displayString = String.valueOf(tile.mode);
            }
            if (EnvironmentHelper.isClientWorld()) {
                NetworkHandler.sendToServer(new PacketScreenAction(button.id, 0, 0, new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
            }
        }
        super.buttonClicked(button);
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Configure: Restriction", 36, 6, 0xFF404040);
        font.drawString("R/-", 78, 70, 0xFF404040);
        font.drawString("Mode", 128, 70, 0xFF404040);
        font.drawString("Y+", 26, 22, 0xFFFFFFFF);
        font.drawString("Y-", 26, 58, 0xFFFFFFFF);
        font.drawString("Z+", 26, 40, 0xFFFFFFFF);
        font.drawString("X+", 44, 40, 0xFFFFFFFF);
        font.drawString("Z-", 44, 58, 0xFFFFFFFF);
        font.drawString("X-", 8, 40, 0xFFFFFFFF);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        Texture i = mc.textureManager.loadTexture("/assets/signalindustries/gui/ioconfig.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
}
