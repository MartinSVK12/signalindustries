package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.mp.PacketScreenAction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageIOChange;
import sunsetsatellite.signalindustries.tiles.TileEntityMultiConduit;
import sunsetsatellite.signalindustries.util.IOPreview;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.Arrays;
import java.util.Objects;

public class ScreenMultiConduitConfig extends Screen {

    public Player entityplayer;
    public TileEntityMultiConduit tile;
    public int xSize = 176;
    public int ySize = 166;

    public ScreenMultiConduitConfig(ContainerInventory playerInv, TileEntity tile) {
        super();
        this.entityplayer = playerInv.player;
        this.tile = (TileEntityMultiConduit) tile;
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
        GL11.glTranslatef((float)centerX, (float)centerY, 0.0F);
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
        buttons.add(new ButtonElement(2, (width / 2) - 10, (height / 2) - 63, 15, 15, "B")); //Y+
        buttons.add(new ButtonElement(4, (width / 2) - 10, (height / 2) - 48, 15, 15, "B")); //Z+
        buttons.add(new ButtonElement(3, (width / 2) - 10, (height / 2) - 33, 15, 15, "B")); //Y-
        buttons.add(new ButtonElement(0, (width / 2) + 4, (height / 2) - 48, 15, 15, "B")); //X+
        buttons.add(new ButtonElement(1, (width / 2) - 24, (height / 2) - 48, 15, 15,"B")); //X-
        buttons.add(new ButtonElement(5, (width / 2) + 4, (height / 2) - 33, 15, 15, "B")); //Z-

        buttons.add(new ButtonElement(8, (width / 2) - 10 + 50, (height / 2) - 63, 15, 15, tile.conduitConnections.get(Direction.Y_POS) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.Y_POS))));
        buttons.add(new ButtonElement(10, (width / 2) - 10 + 50, (height / 2) - 48, 15, 15, tile.conduitConnections.get(Direction.Z_POS) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.Z_POS))));
        buttons.add(new ButtonElement(9, (width / 2) - 10 + 50, (height / 2) - 33, 15, 15, tile.conduitConnections.get(Direction.Y_NEG) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.Y_NEG))));
        buttons.add(new ButtonElement(6, (width / 2) + 4 + 50, (height / 2) - 48, 15, 15, tile.conduitConnections.get(Direction.X_POS) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.X_POS))));
        buttons.add(new ButtonElement(7, (width / 2) - 24 + 50, (height / 2) - 48, 15, 15, tile.conduitConnections.get(Direction.X_NEG) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.X_NEG))));
        buttons.add(new ButtonElement(11, (width / 2) + 4 + 50, (height / 2) - 33, 15, 15, tile.conduitConnections.get(Direction.Z_NEG) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(Direction.Z_NEG))));

        /*buttons.add(new ButtonElement(12,(width / 2) - 85, (height / 2)-12, 30, 15, "All I"));
        buttons.add(new ButtonElement(13, (width / 2) - 55, (height / 2)-12, 30, 15, "All O"));

        if(tile instanceof IHasIOPreview){
            buttons.add(new ButtonElement(14, (width / 2) + 60, (height / 2) - 75, 20, 20, "P"));
        }*/

        buttons.get(0).enabled = false;
        buttons.get(1).enabled = false;
        buttons.get(2).enabled = false;
        buttons.get(3).enabled = false;
        buttons.get(4).enabled = false;
        buttons.get(5).enabled = false;

        if(Arrays.stream(tile.conduits).allMatch(Objects::isNull)){
            buttons.get(6).enabled = false;
            buttons.get(7).enabled = false;
            buttons.get(8).enabled = false;
            buttons.get(9).enabled = false;
            buttons.get(10).enabled = false;
            buttons.get(11).enabled = false;
        }

        super.init();
    }

    @Override
    public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {
        if(eventKey == 1){
            mc.thePlayer.closeScreen();
        }
    }

    @Override
    public void mouseClicked(int mx, int my, int buttonNum) {
        super.mouseClicked(mx, my, buttonNum);
        if(buttonNum == 1){
            for (ButtonElement button : buttons) {
                if(button.mouseClicked(mc,mx,my)){
                    this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
                    buttonClickedAlt(button);
                }
            }
        }
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if(tile != null){
            if (button.id > 5 && button.id < 12) {
                Direction dir = Direction.values()[button.id - 6];
                Integer currentValue = tile.conduitConnections.get(dir);
                if (currentValue < tile.getAmountOfConduits() - 1) {
                    tile.conduitConnections.replace(dir, currentValue + 1);
                }

                button.displayString = String.valueOf(tile.conduitConnections.get(dir));
            }
            if(EnvironmentHelper.isClientWorld()){
                NetworkHandler.sendToServer(new PacketScreenAction(button.id,0,0,tile.getPosition(), tile.getClass()));
            }
        }
        super.buttonClicked(button);
    }

    protected void buttonClickedAlt(ButtonElement button) {
        if(button.id > 5 && button.id < 12){
            Direction dir = Direction.values()[Math.min(6,Math.max(0,button.id-6))];
            Integer currentValue = tile.conduitConnections.get(dir);
            if(currentValue > -1){
                tile.conduitConnections.put(dir,currentValue-1);
                button.displayString = tile.conduitConnections.get(dir) == -1 ? "X" : String.valueOf(tile.conduitConnections.get(dir));
            }
        }
        if(EnvironmentHelper.isClientWorld()){
            NetworkHandler.sendToServer(new PacketScreenAction(button.id,1,0,tile.getPosition(), tile.getClass()));
        }
    }

    protected void drawGuiContainerForegroundLayer()
    {
        font.drawString("Configure: Multi Conduit", 45, 6, 0xFF404040);
        font.drawString("I/O", 78, 70, 0xFF404040);
        font.drawString("Conduit", 128, 70, 0xFF404040);
        font.drawString("Y+", 26, 22, 0xFFFFFFFF);
        font.drawString("Y-", 26, 58, 0xFFFFFFFF);
        font.drawString("Z+", 26, 40, 0xFFFFFFFF);
        font.drawString("X+", 44, 40, 0xFFFFFFFF);
        font.drawString("Z-", 44, 58, 0xFFFFFFFF);
        font.drawString("X-", 8, 40, 0xFFFFFFFF);
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        Texture i = mc.textureManager.loadTexture("/assets/signalindustries/gui/ioconfig.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
}
