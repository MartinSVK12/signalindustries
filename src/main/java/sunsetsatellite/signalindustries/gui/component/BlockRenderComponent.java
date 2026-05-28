package sunsetsatellite.signalindustries.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.hud.component.ComponentAnchor;
import net.minecraft.client.gui.hud.component.layout.Layout;
import net.minecraft.client.gui.hud.component.layout.LayoutAbsolute;
import net.minecraft.client.gui.options.components.OptionsComponent;
import net.minecraft.client.render.renderer.GLRenderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL41;
import org.lwjgl.util.vector.Vector2f;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.screens.component.base.GuiComponent;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
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
		/*float size = 25;
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().scale(size, -size, -size);
		GLRenderer.modelM4f().rotate(-(float) Math.atan(yRot / 40F) * 60F, 1.0F, 0.0F, 0.0F);
		GLRenderer.modelM4f().rotate((float) Math.atan(xRot / 40F) * 80F, 0, 1F, 0);
		lastHoveredSide = getHoveredSide(mx, my);*/
	}

	public Direction getHoveredSide(int mx, int my) {
		FloatBuffer modelMatrix = BufferUtils.createFloatBuffer(16);
		FloatBuffer projMatrix = BufferUtils.createFloatBuffer(16);
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		GL41.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelMatrix);
		GL41.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projMatrix);
		GL41.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
		return Direction.Y_POS;
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
	public Map<String, OptionsComponent> getProperties() {
		return Map.of();
	}

	@Override
	public String getId() {
		return ID;
	}
}
