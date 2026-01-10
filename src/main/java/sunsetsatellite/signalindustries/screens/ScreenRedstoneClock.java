package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.entity.player.Player;
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
import sunsetsatellite.signalindustries.tiles.TileEntityRedstoneClock;
import sunsetsatellite.signalindustries.util.IOPreview;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

public class ScreenRedstoneClock extends Screen {

    public TileEntityRedstoneClock tile;
    public int xSize = 176;
    public int ySize = 166;

    public ScreenRedstoneClock(TileEntityRedstoneClock tile) {
        super();
        this.tile = tile;
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
        buttons.add(new ButtonElement(1, Math.round((float) width / 2 + 30) ,Math.round((float)height / 2 - 65), 20, 20, "+"));
        buttons.add(new ButtonElement(2, Math.round((float) width / 2 + 30),Math.round((float)height / 2 - 30), 20, 20, "-"));
        buttons.add(new ButtonElement(3, Math.round((float) width / 2 - 80) ,Math.round((float)height / 2 - 65), 20, 20, "+"));
        buttons.add(new ButtonElement(4, Math.round((float) width / 2 - 80) ,Math.round((float)height / 2 - 30), 20, 20, "-"));

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
            if(button.id == 1){
                tile.ticksOn++;
            } else if(button.id == 2 && tile.ticksOn > 1){
                tile.ticksOn--;
            }
            if(button.id == 3){
                tile.ticksOff++;
            } else if(button.id == 4 && tile.ticksOff > 1){
                tile.ticksOff--;
            }
        }
        if(EnvironmentHelper.isClientWorld()){
            NetworkHandler.sendToServer(new PacketScreenAction(button.id,0,0,new Vec3i(tile.x, tile.y, tile.z), tile.getClass()));
        }
        super.buttonClicked(button);
    }

    protected void buttonClickedAlt(ButtonElement button) {

    }

    protected void drawGuiContainerForegroundLayer()
    {
        font.drawString("Configure: Clock", 45, 6, 0xFF404040);
        font.drawString(tile.ticksOn + "t On", 120, 42, 0x404040);
        font.drawString(tile.ticksOff + "t Off", 10, 42, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f)
    {
        Texture i = mc.textureManager.loadTexture("/assets/signalindustries/gui/config.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
}
