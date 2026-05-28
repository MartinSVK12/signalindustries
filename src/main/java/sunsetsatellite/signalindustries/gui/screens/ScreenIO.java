package sunsetsatellite.signalindustries.gui.screens;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL41;
import org.lwjgl.util.glu.GLU;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec2f;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.FluidItemContainer;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.catalyst.screens.screen.ScreenComposedContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.models.BlockModelIOPreview;
import sunsetsatellite.signalindustries.render.RenderMultiblockInGUI;
import sunsetsatellite.signalindustries.util.IO;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class ScreenIO extends ScreenComposedContainer {

	public TileEntityFluidItemContainer tile;
	private int mx;
	private int my;
	private float yRot = 0;
	private float xRot = 0;
	private Direction lastHoveredSide = null;
	public IO io;

	public ScreenIO(MenuComposed menuComposed, CompoundTag tag, IO io) {
		super(new MenuComposed(menuComposed.playerInventory, (FluidItemContainer) menuComposed.inventory), tag);
		tile = (TileEntityFluidItemContainer) menuComposed.itemInventory;
		this.io = io;
	}

	@Override
	public void render(int mx, int my, float partialTick) {
		this.mx = mx;
		this.my = my;
		super.render(mx, my, partialTick);
		int centerX = (this.width - this.xSize) / 2;
		int centerY = (this.height - this.ySize) / 2;
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().rotate(120.0F, 1.0F, 0.0F, 0.0F);
		Lighting.enableLight();
		GLRenderer.popFrame();
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(centerX, centerY, 0);
		GLRenderer.setColor4f(1,1,1,1);
		renderBlock();
		Lighting.disable();
		GLRenderer.popFrame();
		if(lastHoveredSide != null){
			TooltipElement tooltip = new TooltipElement(mc);
			String slot = "Slot: " + (tile.activeItemSlots.get(lastHoveredSide) == -1 ? "Any" : String.valueOf(tile.activeItemSlots.get(lastHoveredSide)));
			String io = "I/O: " + tile.itemConnections.get(lastHoveredSide).name();
			tooltip.render(lastHoveredSide.getName()+"\n"+io+"\n"+slot,mx,my,8,8);
		}
		GLRenderer.enableState(State.DEPTH_TEST);
	}

	public void renderBlock(){
		GLRenderer.enableState(State.DEPTH_TEST);
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(85,45, 900);
		float size = 25;
		float centerX = (this.width - this.xSize) / 2f + 85;
		float centerY = (this.height - this.ySize) / 2f + 50;
		if(Mouse.isButtonDown(1)){
			xRot = mx - centerX;
			yRot = my - centerY;
		}
		GLRenderer.modelM4f().scale(size, -size, -size);
		GLRenderer.modelM4f().rotate(-(float) Math.atan(yRot / 40F) * 60F, 1.0F, 0.0F, 0.0F);
		GLRenderer.modelM4f().rotate((float) Math.atan(xRot / 40F) * 80F, 0, 1F, 0);
		lastHoveredSide = getHoveredSide();
		Lighting.enableLight();
		RenderMultiblockInGUI r = new RenderMultiblockInGUI();
		ArrayList<BlockInstance> list = new ArrayList<>();
		list.add(new BlockInstance(tile.getBlock(),new Vec3i(),tile.getBlockMeta(),tile));
		BlockModelIOPreview.ioConfig = true;
		BlockModelIOPreview.ioConfigPos = tile.getPosition();
		BlockModelIOPreview.ioConfigWorld = tile.worldObj;
		BlockModelIOPreview.ioType = io;
		r.render(list, 1);
		BlockModelIOPreview.ioConfig = false;
		BlockModelIOPreview.ioConfigPos = null;
		BlockModelIOPreview.ioConfigWorld = null;
		BlockModelIOPreview.ioType = io;
		/*for (Direction dir : Direction.values()) {
			Block<?> block = dir.getBlock(tile.worldObj, tile);
			TileEntity te = dir.getTileEntity(tile.worldObj, tile);
			int meta = dir.getBlockMetadata(tile.worldObj, tile);
			Vec3i vec = dir.getVec();
			if(block != null){
				list.add(new BlockInstance(block, vec, meta, te));
			}
		}
		r.render(list, 0.4f);*/
		GLRenderer.popFrame();
		Lighting.disable();
		//SignalIndustries.LOGGER.info("{}, {}, {}, {}, {}", lastHoveredSide, mx, my, xRot, yRot);
	}

	public Direction getHoveredSide() {
		//FIXME:
		/*FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
		FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		GL11.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projMatrix);
		GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);

		Direction hovered = null;
		double minDepth = Double.MAX_VALUE;

		for (Direction dir : Direction.values()) {
			Vec3f[] faceVertices = Direction.getVerticesForSide(dir);

			// project vertices to screen
			Vec2f[] screenCoords = new Vec2f[4];
			float avgDepth = 0;

			for (int i = 0; i < 4; i++) {
				FloatBuffer screenPos = BufferUtils.createFloatBuffer(3);
				GLU.gluProject((float) faceVertices[i].x, (float) faceVertices[i].y, (float) faceVertices[i].z,
					modelMatrix, projMatrix, viewport, screenPos);

				// adjust for mc screen
				float screenX = screenPos.get(0) / mc.resolution.getScale();
				float screenY = (viewport.get(3) - screenPos.get(1)) / mc.resolution.getScale();
				screenCoords[i] = new Vec2f(screenX, screenY);
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
		}*/

		return null;
	}

	private boolean isPointInPolygon(int x, int y, Vec2f[] vertices) {
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
}
