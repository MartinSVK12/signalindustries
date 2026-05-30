package sunsetsatellite.signalindustries.gui.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.gui.hud.component.ComponentAnchor;
import net.minecraft.client.gui.hud.component.layout.Layout;
import net.minecraft.client.gui.hud.component.layout.LayoutAbsolute;
import net.minecraft.client.gui.options.components.OptionsComponent;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.block.Blocks;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec2f;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.screens.component.base.GuiComponent;
import sunsetsatellite.signalindustries.render.RenderMultiblockInGUI;

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
		GLRenderer.modelM4f().rotate(120.0F, 1.0F, 0.0F, 0.0F);
		Lighting.enableLight();
		GLRenderer.popFrame();
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(x, y, 0);
		renderBlock();
		Lighting.disable();
		GLRenderer.popFrame();
		if(lastHoveredSide != null){
			TooltipElement tooltip = new TooltipElement(mc);
			tooltip.render(lastHoveredSide.getName(),mx,my,8,8);
			//String slot = "Slot: " + (tile.activeItemSlots.get(lastHoveredSide) == -1 ? "Any" : String.valueOf(tile.activeItemSlots.get(lastHoveredSide)));
			//String io = "I/O: " + tile.itemConnections.get(lastHoveredSide).name();
			//tooltip.render(lastHoveredSide.getName()+"\n"+io+"\n"+slot,mx,my,8,8);
		}
		GLRenderer.enableState(State.DEPTH_TEST);
	}

	public void renderBlock(){
		GLRenderer.enableState(State.DEPTH_TEST);
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(getBaseXSize()/2f,getBaseYSize()/2f, 900);
		float size = 25;
		float centerX = 0;//(this.width - this.xSize) / 2f;// + 85;
		float centerY = 0;//(this.height - this.ySize) / 2f;// + 45;
		if(Mouse.isButtonDown(0)){
			xRot = mx - centerX;
			yRot = my - centerY;
		}
		GLRenderer.modelM4f().scale(size, -size, size);
		GLRenderer.modelM4f().rotateX(-(float) Math.atan(yRot / 40F) * 20F);
		GLRenderer.modelM4f().rotate((float) Math.atan(xRot / 40F) * 20F, 0, 1F, 0);
		lastHoveredSide = getHoveredSide();
		Lighting.enableLight();
		RenderMultiblockInGUI r = new RenderMultiblockInGUI();
		ArrayList<BlockInstance> list = new ArrayList<>();
		list.add(new BlockInstance(Blocks.BEDROCK, new Vec3i(), 0, null));
		//list.add(new BlockInstance(tile.getBlock(),new Vec3i(),tile.getBlockMeta(),tile));
		/*BlockModelIOPreview.ioConfig = true;
		BlockModelIOPreview.ioConfigPos = tile.getPosition();
		BlockModelIOPreview.ioConfigWorld = tile.worldObj;
		BlockModelIOPreview.ioType = io;*/
		r.render(list, 1);
		/*BlockModelIOPreview.ioConfig = false;
		BlockModelIOPreview.ioConfigPos = null;
		BlockModelIOPreview.ioConfigWorld = null;
		BlockModelIOPreview.ioType = io;*/
		int meta = 0;
		for (Direction dir : Direction.values()) {
			/*Block<?> block = dir.getBlock(tile.worldObj, tile);
			TileEntity te = dir.getTileEntity(tile.worldObj, tile);
			int meta = dir.getBlockMetadata(tile.worldObj, tile);
			Vec3i vec = dir.getVec();
			list.add(new BlockInstance(block, vec, meta, te));*/
			list.add(new BlockInstance(Blocks.WOOL, dir.getVec(), meta, null));
			meta++;
		}
		r.render(list, 0.4f);
		GLRenderer.popFrame();
		Lighting.disable();
	}

	public Direction getHoveredSide() {
		Direction hovered = null;
		double minDepth = Double.MAX_VALUE;
		int[] viewport = new int[16];
		GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);

		for (Direction dir : Direction.values()) {
			//create face vertices of side
			Vec3f[] faceVertices = Direction.getVerticesForSide(dir);
			Vec2f[] screenCoords = new Vec2f[4];
			float avgDepth = 0;
			for (int i = 0; i < 4; i++) {
				Vector3f screenPos = project(faceVertices[i], GLRenderer.modelM4f(), GLRenderer.projectionM4f(), viewport);
				// adjust for mc screen
				float screenX = screenPos.x / mc.resolution.getScale();
				float screenY = (viewport[3] - screenPos.y) / mc.resolution.getScale();
				screenCoords[i] = new Vec2f(screenX, screenY);
				avgDepth += screenPos.z;
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

	private Vector3f project(
		Vec3f pos,
		Matrix4f model,
		Matrix4f projection,
		int[] viewport
	) {
		Vector4f clip = new Vector4f((float) pos.x, (float) pos.y, (float) pos.z, 1.0f);

		model.transform(clip);
		projection.transform(clip);

		if (clip.w == 0.0f) {
			return new Vector3f();
		}

		float ndcX = clip.x / clip.w;
		float ndcY = clip.y / clip.w;
		float ndcZ = clip.z / clip.w;

		float screenX = viewport[0] + (ndcX + 1.0f) * 0.5f * viewport[2];
		float screenY = viewport[1] + (ndcY + 1.0f) * 0.5f * viewport[3];
		float screenZ = (ndcZ + 1.0f) * 0.5f;

		return new Vector3f(screenX, screenY, screenZ);
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
