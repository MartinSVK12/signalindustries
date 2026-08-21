package sunsetsatellite.signalindustries.gui.screens;

import com.mojang.nbt.tags.CompoundTag;
import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.minecraft.client.gui.TooltipElement;
import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.State;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import org.joml.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


import org.lwjgl.opengl.GL41;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec2f;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.util.FluidItemContainer;
import sunsetsatellite.catalyst.screens.component.ButtonComponent;
import sunsetsatellite.catalyst.screens.component.TextComponent;
import sunsetsatellite.catalyst.screens.menu.MenuComposed;
import sunsetsatellite.catalyst.screens.screen.ScreenComposedContainer;
import sunsetsatellite.signalindustries.blocks.models.BlockModelIOPreview;
import sunsetsatellite.signalindustries.mp.message.NetworkMessageIOChange;
import sunsetsatellite.signalindustries.render.RenderMultiblockInGUI;
import sunsetsatellite.signalindustries.util.IO;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.lang.Math;
import java.util.ArrayList;
import java.util.HashMap;

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
		this.<TextComponent>get("title").text = "Configure: "+io.name();
		this.<ButtonComponent>get("allI").buttonClicked.connect((s, t)->{
			switch (io) {
				case FLUID -> {
					for (Direction dir : Direction.values()) {
						if(EnvironmentHelper.isSingleplayerClient()){
							tile.fluidConnections.replaceAll((D,C)->Connection.INPUT);
						} else if(EnvironmentHelper.isMultiplayerClient()){
							Vec3i position = tile.getPosition();
							int slot = tile.activeFluidSlots.get(dir);
							NetworkHandler.sendToServer(new NetworkMessageIOChange(position, Connection.INPUT, dir, io, slot, tile.getClass()));
						}
					}
				}
				case ITEM -> {
					for (Direction dir : Direction.values()) {
						if(EnvironmentHelper.isSingleplayerClient()){
							tile.itemConnections.replaceAll((D,C)->Connection.INPUT);
						} else if(EnvironmentHelper.isMultiplayerClient()){
							Vec3i position = tile.getPosition();
							int slot = tile.activeItemSlots.get(dir);
							NetworkHandler.sendToServer(new NetworkMessageIOChange(position, Connection.INPUT, dir, io, slot, tile.getClass()));
						}
					}
				}
			}
		});
		this.<ButtonComponent>get("allO").buttonClicked.connect((s, t)->{
			switch (io) {
				case ITEM -> {
					for (Direction dir : Direction.values()) {
						if(EnvironmentHelper.isSingleplayerClient()){
							tile.itemConnections.replaceAll((D,C)->Connection.OUTPUT);
						} else if(EnvironmentHelper.isMultiplayerClient()){
							Vec3i position = tile.getPosition();
							int slot = tile.activeItemSlots.get(dir);
							NetworkHandler.sendToServer(new NetworkMessageIOChange(position, Connection.OUTPUT, dir, io, slot, tile.getClass()));
						}
					}
				}
				case FLUID -> {
					for (Direction dir : Direction.values()) {
						if(EnvironmentHelper.isSingleplayerClient()){
							tile.fluidConnections.replaceAll((D,C)->Connection.OUTPUT);
						} else if(EnvironmentHelper.isMultiplayerClient()){
							Vec3i position = tile.getPosition();
							int slot = tile.activeFluidSlots.get(dir);
							NetworkHandler.sendToServer(new NetworkMessageIOChange(position, Connection.OUTPUT, dir, io, slot, tile.getClass()));
						}
					}
				}
			}

		});
		this.<ButtonComponent>get("clear").buttonClicked.connect((s, t)->{
			switch (io){
				case ITEM -> {
					for (Direction dir : Direction.values()) {
						if(EnvironmentHelper.isSingleplayerClient()){
							tile.itemConnections.replaceAll((D,C)->Connection.NONE);
						} else if(EnvironmentHelper.isMultiplayerClient()){
							Vec3i position = tile.getPosition();
							int slot = tile.activeItemSlots.get(dir);
							NetworkHandler.sendToServer(new NetworkMessageIOChange(position, Connection.NONE, dir, io, slot, tile.getClass()));
						}
					}
				}
				case FLUID -> {
					for (Direction dir : Direction.values()) {
						if(EnvironmentHelper.isSingleplayerClient()){
							tile.fluidConnections.replaceAll((D,C)->Connection.NONE);
						} else if(EnvironmentHelper.isMultiplayerClient()){
							Vec3i position = tile.getPosition();
							int slot = tile.activeFluidSlots.get(dir);
							NetworkHandler.sendToServer(new NetworkMessageIOChange(position, Connection.NONE, dir, io, slot, tile.getClass()));
						}
					}
				}
			}

		});
	}

	@Override
	public void mouseClicked(int mx, int my, int buttonNum) {
		super.mouseClicked(mx, my, buttonNum);
		if(lastHoveredSide != null){
			Direction dir = lastHoveredSide;
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)){
				switch (io) {
					case FLUID -> tile.cycleActiveFluidSlotForSide(dir, false);
					case ITEM -> tile.cycleActiveItemSlotForSide(dir, false);
				}
			} else {
				switch (io) {
					case FLUID -> tile.cycleFluidIOForSide(dir);
					case ITEM -> tile.cycleItemIOForSide(dir);
				}
			}
			if(EnvironmentHelper.isMultiplayerClient()){
				Vec3i position = tile.getPosition();
				Connection connection = Connection.NONE;// = tile.itemConnections.get(dir);
				int slot = 0;// = tile.activeItemSlots.get(dir);
				switch (io) {
					case FLUID -> {
						connection = tile.fluidConnections.get(dir);
						slot = tile.activeFluidSlots.get(dir);
					}
					case ITEM -> {
						connection = tile.itemConnections.get(dir);
						slot = tile.activeItemSlots.get(dir);
					}
				}
				NetworkHandler.sendToServer(new NetworkMessageIOChange(position, connection, dir, io, slot, tile.getClass()));
			}
		}
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
			HashMap<Direction,Integer> slots = new HashMap<>();
			HashMap<Direction,Connection> connections = new HashMap<>();
			switch (io) {
				case FLUID -> {
					slots = tile.activeFluidSlots;
					connections = tile.fluidConnections;
				}
				case ITEM -> {
					slots = tile.activeItemSlots;
					connections = tile.itemConnections;
				}
			}
			TooltipElement tooltip = new TooltipElement(mc);
			String slot = "Slot: " + (slots.get(lastHoveredSide) == -1 ? "Any" : String.valueOf(slots.get(lastHoveredSide)));
			String io = "I/O: " + connections.get(lastHoveredSide).name();
			tooltip.render(lastHoveredSide.getName()+"\n"+io+"\n"+slot,mx,my,8,8);
		}
		GLRenderer.enableState(State.DEPTH_TEST);
	}

	public void renderBlock(){
		GLRenderer.enableState(State.DEPTH_TEST);
		GLRenderer.pushFrame();
		GLRenderer.modelM4f().translate(85,45, 900);
		float size = 25;
		float centerX = (this.width - this.xSize) / 2f;// + 85;
		float centerY = (this.height - this.ySize) / 2f;// + 45;
		if(Mouse.isButtonDown(1)){
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
		list.add(new BlockInstance(tile.getBlock(),new Vec3i(),tile.getBlockMeta(),tile));
		BlockModelIOPreview.ioConfig = true;
		BlockModelIOPreview.ioConfigPos = tile.tilePos;
		BlockModelIOPreview.ioConfigWorld = tile.worldObj;
		BlockModelIOPreview.ioType = io;
		r.render(list, 1);
		BlockModelIOPreview.ioConfig = false;
		BlockModelIOPreview.ioConfigPos = null;
		BlockModelIOPreview.ioConfigWorld = null;
		BlockModelIOPreview.ioType = io;
		for (Direction dir : Direction.values()) {
			Block<?> block = dir.getBlock(tile.worldObj, tile);
			TileEntity te = dir.getTileEntity(tile.worldObj, tile);
			int meta = dir.getBlockMetadata(tile.worldObj, tile);
			Vec3i vec = dir.getVec();
			list.add(new BlockInstance(block, vec, meta, te));
		}
		r.render(list, 0.4f);
		GLRenderer.popFrame();
		Lighting.disable();
	}

	public Direction getHoveredSide() {
		Direction hovered = null;
		double minDepth = Double.MAX_VALUE;
		int[] viewport = new int[16];
		GL41.glGetIntegerv(GL41.GL_VIEWPORT, viewport);

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
}
