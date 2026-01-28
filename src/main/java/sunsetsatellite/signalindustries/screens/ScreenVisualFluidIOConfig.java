package sunsetsatellite.signalindustries.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ButtonElement;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.menu.MenuAbstract;
import net.minecraft.core.sound.SoundCategory;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.blocks.models.BlockModelIOPreview;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageIOChange;
import sunsetsatellite.signalindustries.render.RenderMultiblockInGUI;
import sunsetsatellite.signalindustries.util.IOPreview;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class ScreenVisualFluidIOConfig extends Screen {

    public Screen parentScreen;
    public Player entityplayer;
    public TileEntityFluidItemContainer tile;
    public int xSize = 176;
    public int ySize = 166;
    public MenuAbstract inventorySlots;
    private float yRot = 0;
    private float xRot = 0;
    private Direction lastHoveredSide = null;

    public ScreenVisualFluidIOConfig(Player player, MenuAbstract menu, Screen parent, TileEntityFluidItemContainer tile) {
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
        GL11.glTranslatef((float) centerX, (float) centerY, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        renderBlock(mx, my);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        Lighting.disable();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawGuiContainerForegroundLayer();
        GL11.glPopMatrix();
        super.render(mx, my, partialTick);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(lastHoveredSide != null){
            TooltipElement tooltip = new TooltipElement(mc);
            String slot = "Slot: " + (tile.activeFluidSlots.get(lastHoveredSide) == -1 ? "Any" : String.valueOf(tile.activeFluidSlots.get(lastHoveredSide)));
            String io = "I/O: " + tile.fluidConnections.get(lastHoveredSide).name();
            tooltip.render(lastHoveredSide.getName()+"\n"+io+"\n"+slot,mx,my,8,8);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    public void renderBlock(int mx, int my) {
        Minecraft mc = Minecraft.getMinecraft();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(85,45, 900F);

        double size = 25;

        float centerX = (this.width - this.xSize) / 2f + 85;
        float centerY = (this.height - this.ySize) / 2f + 50;

        if(Mouse.isButtonDown(1)){
            xRot = mx - centerX;
            yRot = my - centerY;
        }

        GL11.glScaled(size, -size, size);
        //GL11.glRotatef(135F, 0.0F, 1.0F, 0.0F);
        //GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(float) Math.atan(yRot / 40F) * 60F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef((float) Math.atan(xRot / 40F) * 80F, 0, 1F, 0);
        lastHoveredSide = getHoveredSide(mx, my);
        Lighting.enableLight();
        GL11.glTranslatef(0.0F, 0, 0.0F);
        RenderMultiblockInGUI r = new RenderMultiblockInGUI();
        ArrayList<BlockInstance> list = new ArrayList<>();
        list.add(new BlockInstance(tile.getBlock(),new Vec3i(),tile.getBlockMeta(),tile));
        BlockModelIOPreview.ioConfig = true;
        BlockModelIOPreview.ioConfigPos = tile.getPosition();
        BlockModelIOPreview.ioConfigWorld = tile.worldObj;
        BlockModelIOPreview.ioType = IOPreview.FLUID;
        r.doRender(list, mc.textureManager, mc.font, 0, 0, 0, 1);
        BlockModelIOPreview.ioConfig = false;
        BlockModelIOPreview.ioConfigPos = null;
        BlockModelIOPreview.ioConfigWorld = null;
        BlockModelIOPreview.ioType = IOPreview.NONE;
        for (Direction dir : Direction.values()) {
            Block<?> block = dir.getBlock(tile.worldObj, tile);
            TileEntity te = dir.getTileEntity(tile.worldObj, tile);
            int meta = dir.getBlockMetadata(tile.worldObj, tile);
            Vec3i vec = dir.getVec();
            if(block != null){
                list.add(new BlockInstance(block, vec, meta, te));
            }
        }
        r.doRender(list, mc.textureManager, mc.font, 0, 0, 0, 0.4f);
        GL11.glPopMatrix();
        Lighting.disable();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);


        //SignalIndustries.LOGGER.info("Hovered side: {}", side);
    }

    public Direction getHoveredSide(int mx, int my) {
        FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
        FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
        GL11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projMatrix);
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);

        Direction hovered = null;
        double minDepth = Double.MAX_VALUE;

        for (Direction dir : Direction.values()) {
            Vector3f[] faceVertices = Direction.getVerticesForSide(dir);

            // project vertices to screen
            Vector2f[] screenCoords = new Vector2f[4];
            float avgDepth = 0;

            for(int i = 0; i < 4; i++) {
                FloatBuffer screenPos = BufferUtils.createFloatBuffer(3);
                GLU.gluProject(faceVertices[i].x, faceVertices[i].y, faceVertices[i].z,
                        modelMatrix, projMatrix, viewport, screenPos);

                // adjust for mc screen
                float screenX = screenPos.get(0) / mc.resolution.getScale();
                float screenY = (viewport.get(3) - screenPos.get(1)) / mc.resolution.getScale();
                screenCoords[i] = new Vector2f(screenX, screenY);
                avgDepth += screenPos.get(2);
            }

            // check if mouse is in projected polygon
            if (isPointInPolygon(mx, my, screenCoords)) {
                // check depth to pick the closest face
                if (avgDepth < minDepth) {
                    minDepth = avgDepth;
                    hovered = dir;
                }
            }
        }
        return hovered;
    }

    private boolean isPointInPolygon(int x, int y, Vector2f[] vertices) {
        boolean inside = false;
        int n = vertices.length;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            if (((vertices[i].y > y) != (vertices[j].y > y)) &&
                    (x < (vertices[j].x - vertices[i].x) * (y - vertices[i].y) / (vertices[j].y - vertices[i].y) + vertices[i].x)) {
                inside = !inside;
            }
        }
        return inside;
    }

    @Override
    public void init() {
        buttons.add(new ButtonElement(0, (width / 2) + 60, (height / 2) - 75, 20, 20, "M"));

        buttons.add(new ButtonElement(1, (width / 2) - 85, (height / 2) - 12, 30, 15, "All I"));
        buttons.add(new ButtonElement(2, (width / 2) - 55, (height / 2) - 12, 30, 15, "All O"));
        buttons.add(new ButtonElement(3, (width / 2) - 25, (height / 2) - 12, 30, 15, "Clear"));

        super.init();
    }

    @Override
    public void keyPressed(char eventCharacter, int eventKey, int mx, int my) {
        if (eventKey == 1) {
            mc.thePlayer.closeScreen();
        }
    }

    @Override
    public void mouseClicked(int mx, int my, int buttonNum) {
        super.mouseClicked(mx, my, buttonNum);
        if(lastHoveredSide != null) {
            for (Direction dir : Direction.values()) {
                if(dir == lastHoveredSide){
                    if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
                        tile.cycleActiveFluidSlotForSide(dir, false);
                    } else {
                        tile.cycleFluidIOForSide(dir);
                    }
                    if(EnvironmentHelper.isClientWorld()){
                        Vec3i position = tile.getPosition();
                        Connection connection = tile.fluidConnections.get(dir);
                        int slot = tile.activeFluidSlots.get(dir);
                        NetworkHandler.sendToServer(new NetworkMessageIOChange(position, connection, dir, IOPreview.ITEM, slot, tile.getClass()));
                    }
                    break;
                }
            }
        }
        if (buttonNum == 1) {
            for (ButtonElement button : buttons) {
                if (button.mouseClicked(mc, mx, my)) {
                    this.mc.sndManager.playSound("random.click", SoundCategory.GUI_SOUNDS, 1.0F, 1.0F);
                    buttonClickedAlt(button);
                }

            }
        }
    }

    @Override
    protected void buttonClicked(ButtonElement button) {
        if (tile != null) {
            int currentButtonId = -1;
            if(button.id == 0){
                mc.displayScreen(new ScreenFluidIOConfig(mc.thePlayer, inventorySlots, this, tile));
            }

            HashMap<Direction, Connection> fluidConnections = new HashMap<>(tile.fluidConnections);

            if (button.id == 1) {
                for (Direction direction : Direction.values()) {
                    fluidConnections.replace(direction, Connection.INPUT);
                }
                currentButtonId = button.id;
            }

            if (button.id == 2) {
                for (Direction direction : Direction.values()) {
                    fluidConnections.replace(direction, Connection.OUTPUT);
                }
                currentButtonId = button.id;
            }

            if (button.id == 3) {
                for (Direction direction : Direction.values()) {
                    fluidConnections.replace(direction, Connection.NONE);
                }
                currentButtonId = button.id;
            }

            if (EnvironmentHelper.isClientWorld() && currentButtonId != -1) {
                for (Direction dir : Direction.values()) {
                    Vec3i position = tile.getPosition();
                    Connection connection = fluidConnections.get(dir);
                    int slot = tile.activeFluidSlots.get(dir);
                    NetworkHandler.sendToServer(new NetworkMessageIOChange(position, connection, dir, IOPreview.ITEM, slot, tile.getClass()));
                }
            }
        }
        super.buttonClicked(button);
    }

    protected void buttonClickedAlt(ButtonElement button) {
    }

    protected void drawGuiContainerForegroundLayer() {
        font.drawString("Configure: Fluids", 45, 6, 0xFF404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f) {
        Texture i = mc.textureManager.loadTexture("/assets/signalindustries/gui/config.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.textureManager.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
    }
}
