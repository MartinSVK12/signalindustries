package sunsetsatellite.signalindustries.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.hud.component.ComponentAnchor;
import net.minecraft.client.gui.hud.component.layout.Layout;
import net.minecraft.client.gui.hud.component.layout.LayoutAbsolute;
import net.minecraft.client.gui.options.components.OptionsComponent;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.core.block.Blocks;
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
import sunsetsatellite.catalyst.screens.component.base.GuiComponent;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.render.RenderMultiblockInGUI;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Map;

public class BlockRenderComponent extends GuiComponent {

	public static final String ID = "blockRender";

	private float yRot = 0;
	private float xRot = 0;
	private Direction lastHoveredSide = null;

	public BlockRenderComponent(String name, float x, float y) {
		super(name, 64, 64, new LayoutAbsolute(x,y, ComponentAnchor.TOP_LEFT));
	}

	@Override
	public void renderComponent(Minecraft mc, Screen screen, int x, int y, int xScreenSize, int yScreenSize, float partialTick) {

	}

	@Override
	public void renderComponentPreview(Minecraft mc, Gui gui, Layout layout, int x, int y, int xScreenSize, int yScreenSize) {
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(x, y, 0);
		GLRenderer.modelM4f().rotate(120.0F, 1.0F, 0.0F, 0.0F);
		Lighting.enableInventoryLight();
		renderBlock();
		Lighting.disable();
		GLRenderer.popFrame();
	}

	public void renderBlock(){
		float size = 10;
		if(Mouse.isButtonDown(0)){
			xRot = (float) Math.atan(mx / 40F) * 80F;
			yRot = -(float) Math.atan(my / 40F) * 60F;
		}
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(0,0, 900F);
		GLRenderer.modelM4f().scale(size, -size, -size);
		GLRenderer.modelM4f().rotate(yRot, 1.0F, 0.0F, 0.0F);
		GLRenderer.modelM4f().rotate(xRot, 0, 1F, 0);
		lastHoveredSide = getHoveredSide();
		Lighting.enableLight();
		RenderMultiblockInGUI r = new RenderMultiblockInGUI();
		ArrayList<BlockInstance> list = new ArrayList<>();
		list.add(new BlockInstance(Blocks.BEDROCK, new Vec3i(), 0, null));
		r.render(list, 1);
		for (Direction dir : Direction.values()) {
			list.add(new BlockInstance(Blocks.GLASS, dir.getVec(), 0, null));
		}
		r.render(list, 0.4f);
		GLRenderer.popFrame();
		Lighting.disable();
		SignalIndustries.LOGGER.info("{}, {}, {}, {}, {}", lastHoveredSide, mx, my, xRot, yRot);
	}

	public Direction getHoveredSide() {
		FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
		FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		GL41.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		GL41.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projMatrix);
		GL41.glGetIntegerv(GL11.GL_VIEWPORT, viewport);

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
		}

		return hovered;
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

	@Override
	public Map<String, OptionsComponent> getProperties() {
		return Map.of();
	}

	@Override
	public String getId() {
		return ID;
	}
}
