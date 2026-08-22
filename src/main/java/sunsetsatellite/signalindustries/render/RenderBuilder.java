package sunsetsatellite.signalindustries.render;

import net.minecraft.client.render.Lighting;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.renderer.BlendFactor;
import net.minecraft.client.render.renderer.GLRenderer;
import net.minecraft.client.render.renderer.Shaders;
import net.minecraft.client.render.renderer.State;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.client.render.tileentity.TileEntityRenderer;
import net.minecraft.core.util.helper.LightIndexHelper;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.HologramWorld;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.items.ItemBlueprint;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityBuilder;
import sunsetsatellite.signalindustries.util.SIMultiblock;

import java.util.ArrayList;
import java.util.Objects;

public class RenderBuilder extends RenderSI<TileEntityBuilder> {
	@Override
	public void doRender(TessellatorGeneral tessellatorGeneral, TileEntityBuilder builder, double d, double e, double f, float g) {
		int i = builder.tilePos.x;
		int j = builder.tilePos.y;
		int k = builder.tilePos.z;
		Direction dir = builder.rotation;
		World world = this.renderDispatcher.textureManager.mc.currentWorld;
		if (builder.itemContents[0] != null && builder.itemContents[0].getItem() instanceof ItemBlueprint) {
			Structure multiblock = SignalIndustries.getStructureFromBlueprint(builder.itemContents[0], world);
			if (multiblock == null) {
				return;
			}
			ArrayList<BlockInstance> blocks = multiblock.getBlocks(new Vec3i(i, j, k).add(builder.offset), dir);
			if (multiblock instanceof SIMultiblock) {
				blocks.add(multiblock.getOrigin(new Vec3i(i, j, k).add(builder.offset), dir.getOpposite().shiftAxis()));
			}
			ArrayList<BlockInstance> substitutions = multiblock.getSubstitutions(new Vec3i(i, j, k), dir);
			hologram = new HologramWorld(blocks);
			for (BlockInstance block : blocks) {
				if (!block.exists(world)) {
					boolean foundSub = substitutions.stream().anyMatch((BI) -> BI.pos.equals(block.pos) && BI.exists(world));
					if (!foundSub) {
						if (!Objects.equals(world.getLevelData().getWorldName(), "modelviewer")) {
							GLRenderer.pushFrame();
							Lighting.disable();
							GLRenderer.modelM4f().translate((float) d + (block.pos.x - i) + 0.5f, (float) e + (block.pos.y - j) + 0.5f, (float) f + (block.pos.z - k) + 0.5f);
							BlockModel<?> model = BlockModelDispatcher.getInstance().getDispatch(block.block);
							float alpha = 1;
							if (world.getBlockType(block.pos.tilePos()).id() != 0) {
								GLRenderer.setColor4f(1,0,0,0.90f);
								alpha = 0.9f;
								GLRenderer.modelM4f().scale(1.1f,1.1f,1.1f);
							} else {
								GLRenderer.setColor4f(1,1,1,0.75f);
								alpha = 0.75f;
								GLRenderer.modelM4f().scale(0.9f, 0.9f, 0.9f);
							}
							drawBlock(GLRenderer.getTessellator(),
								model,
								block.meta, alpha);
							Lighting.enableLight();
							GLRenderer.popFrame();
							GLRenderer.setColor4f(1,1,1,1);
						}
					}
				}
			}

			if (!Objects.equals(builder.currentlyBuilding, new Vec3i())) {
				if (builder.buildingMultiblock != null && !builder.buildingBlocks.isEmpty()) {
					renderLineBetweenTwoPoints(i, j, k, builder.currentlyBuilding.x, builder.currentlyBuilding.y, builder.currentlyBuilding.z, 1f, 0f, 0f, 1f, 8f, d, e, f);
				}
			}
		}
	}

	protected HologramWorld hologram;
}
