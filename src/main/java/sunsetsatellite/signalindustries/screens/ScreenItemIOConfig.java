package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.NetworkManager;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageIOChange;
import sunsetsatellite.signalindustries.util.IOPreview;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenItemIOConfig extends Screen {

    public Screen parentScreen;
    public Player entityplayer;
    public TileEntityFluidItemContainer tile;
    public int xSize = 176;
    public int ySize = 166;
    public MenuAbstract inventorySlots;

    public ScreenItemIOConfig(Player player, MenuAbstract menu, Screen parent, TileEntityFluidItemContainer tile) {
        super(parent);
        this.parentScreen = parent;
        this.entityplayer = player;
        this.tile = tile;
        this.inventorySlots = menu;
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
        buttons.add(new ButtonElement(2, Math.round(width / 2f) - 10, Math.round(height / 2f) - 63, 15, 15, tile.itemConnections.get(Direction.Y_POS).getLetter())); //Y+
        buttons.add(new ButtonElement(4, Math.round(width / 2f) - 10, Math.round(height / 2f) - 48, 15, 15, tile.itemConnections.get(Direction.Z_POS).getLetter())); //Z+
        buttons.add(new ButtonElement(3, Math.round(width / 2f) - 10, Math.round(height / 2f) - 33, 15, 15, tile.itemConnections.get(Direction.Y_NEG).getLetter())); //Y-
        buttons.add(new ButtonElement(0, Math.round(width / 2f) + 4, Math.round(height / 2f) - 48, 15, 15, tile.itemConnections.get(Direction.X_POS).getLetter())); //X+
        buttons.add(new ButtonElement(1, Math.round(width / 2f) - 24, Math.round(height / 2f) - 48, 15, 15, tile.itemConnections.get(Direction.X_NEG).getLetter())); //X-
        buttons.add(new ButtonElement(5, Math.round(width / 2f) + 4, Math.round(height / 2f) - 33, 15, 15, tile.itemConnections.get(Direction.Z_NEG).getLetter())); //Z-

        buttons.add(new ButtonElement(8, Math.round(width / 2f) - 10 + 50, Math.round(height / 2f) - 63, 15, 15, tile.activeItemSlots.get(Direction.Y_POS) == -1 ? "*" : String.valueOf(tile.activeItemSlots.get(Direction.Y_POS))));
        buttons.add(new ButtonElement(10, Math.round(width / 2f) - 10 + 50, Math.round(height / 2f) - 48, 15, 15, tile.activeItemSlots.get(Direction.Z_POS) == -1 ? "*" : String.valueOf(tile.activeItemSlots.get(Direction.Z_POS))));
        buttons.add(new ButtonElement(9, Math.round(width / 2f) - 10 + 50, Math.round(height / 2f) - 33, 15, 15, tile.activeItemSlots.get(Direction.Y_NEG) == -1 ? "*" : String.valueOf(tile.activeItemSlots.get(Direction.Y_NEG))));
        buttons.add(new ButtonElement(6, Math.round(width / 2f) + 4 + 50, Math.round(height / 2f) - 48, 15, 15, tile.activeItemSlots.get(Direction.X_POS) == -1 ? "*" : String.valueOf(tile.activeItemSlots.get(Direction.X_POS))));
        buttons.add(new ButtonElement(7, Math.round(width / 2f) - 24 + 50, Math.round(height / 2f) - 48, 15, 15, tile.activeItemSlots.get(Direction.X_NEG) == -1 ? "*" : String.valueOf(tile.activeItemSlots.get(Direction.X_NEG))));
        buttons.add(new ButtonElement(11, Math.round(width / 2f) + 4 + 50, Math.round(height / 2f) - 33, 15, 15, tile.activeItemSlots.get(Direction.Z_NEG) == -1 ? "*" : String.valueOf(tile.activeItemSlots.get(Direction.Z_NEG))));

        buttons.add(new ButtonElement(12,(width / 2) - 85, (height / 2)-12, 30, 15, "All I"));
        buttons.add(new ButtonElement(13, (width / 2) - 55, (height / 2)-12, 30, 15, "All O"));

        if(tile instanceof IHasIOPreview){
            buttons.add(new ButtonElement(14, (width / 2) + 60, (height / 2) - 75, 20, 20, "P"));
        }

        if(tile.getContainerSize() == 1){
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
            int currentButtonId = -1;
            if(button.id >= 0 && button.id <= 5){
                Direction dir = Direction.values()[button.id];
                tile.cycleItemIOForSide(dir);
                button.displayString = tile.itemConnections.get(dir).getLetter();
                currentButtonId = button.id;
            }

            if(button.id > 5 && button.id < 12){
                Direction dir = Direction.values()[button.id-6];
                tile.cycleActiveItemSlotForSide(dir,false);
                button.displayString = String.valueOf(tile.activeItemSlots.get(dir));
                currentButtonId = button.id;
            }

            if(button.id == 12) {
                for (Direction direction : Direction.values()) {
                    tile.itemConnections.replace(direction, Connection.INPUT);
                }
                for (ButtonElement b : buttons) {
                    if(b.id >= 0 && b.id < 6){
                        b.displayString = tile.itemConnections.get(Direction.values()[b.id]).getLetter();
                    }
                }
                currentButtonId = button.id;
            }

            if(button.id == 13) {
                for (Direction direction : Direction.values()) {
                    tile.itemConnections.replace(direction, Connection.OUTPUT);
                }
                for (ButtonElement b : buttons) {
                    if(b.id >= 0 && b.id < 6){
                        b.displayString = tile.itemConnections.get(Direction.values()[b.id]).getLetter();
                    }
                }
                currentButtonId = button.id;
            }

            if (button.id == 14) {
                if (tile instanceof IHasIOPreview) {
                    IHasIOPreview p = ((IHasIOPreview) tile);
                    p.setPreview(((IHasIOPreview) tile).getPreview() != IOPreview.ITEM ? IOPreview.ITEM : IOPreview.NONE);
                }
            }

            if(EnvironmentHelper.isClientWorld() && currentButtonId != -1){
                if(currentButtonId == 12 || currentButtonId == 13){
                    for (Direction dir : Direction.values()) {
                        Vec3i position = tile.getPosition();
                        Connection connection = tile.itemConnections.get(dir);
                        int slot = tile.activeItemSlots.get(dir);
                        NetworkHandler.sendToServer(new NetworkMessageIOChange(position, connection, dir, IOPreview.ITEM, slot, tile.getClass()));
                    }
                } else {
                    Direction dir = Direction.Y_POS;
                    if(currentButtonId < 5 && currentButtonId >= 0){
                        dir = Direction.values()[currentButtonId];
                    } else if(currentButtonId < 12) {
                        dir = Direction.values()[currentButtonId - 6];
                    }
                    Vec3i position = tile.getPosition();
                    Connection connection = tile.itemConnections.get(dir);
                    int slot = tile.activeItemSlots.get(dir);
                    NetworkHandler.sendToServer(new NetworkMessageIOChange(position, connection, dir, IOPreview.ITEM, slot, tile.getClass()));
                }

            }
        }
        super.buttonClicked(button);
    }

    protected void buttonClickedAlt(ButtonElement button) {
        if(button.id > 5 && button.id < 12){
            Direction dir = Direction.values()[button.id-6];
            tile.cycleActiveItemSlotForSide(dir,true);
            button.displayString = tile.activeItemSlots.get(dir) == -1 ? "*" : String.valueOf(tile.activeItemSlots.get(dir));

            if(EnvironmentHelper.isClientWorld()){
                Vec3i position = tile.getPosition();
                Connection connection = tile.itemConnections.get(dir);
                int slot = tile.activeItemSlots.get(dir);
                NetworkHandler.sendToServer(new NetworkMessageIOChange(position, connection, dir, IOPreview.ITEM, slot, tile.getClass()));
            }
        }
    }

    protected void drawGuiContainerForegroundLayer()
    {
        font.drawString("Configure: Items", 45, 6, 0xFF404040);
        font.drawString("I/O", 78, 70, 0xFF404040);
        font.drawString("Slot", 128, 70, 0xFF404040);
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
