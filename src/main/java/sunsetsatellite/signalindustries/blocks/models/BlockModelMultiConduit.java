package sunsetsatellite.signalindustries.blocks.models;

import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.block.model.generic.BlockModelGenericFullyRotatable;
import net.minecraft.client.render.tessellator.TessellatorGeneral;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicFullyRotatable;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.dragonfly.data.block.BlockModelData;
import org.useless.dragonfly.data.block.mojang.BlockModelMojangData;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.conduit.IConduitBlock;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.blocks.logic.BlockLogicMultiConduit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityMultiConduit;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockModelMultiConduit<T extends BlockLogic> extends BlockModelGenericFullyRotatable<T> {

	public BlockModelMultiConduit(@NotNull Block<T> block) {
		super(block, BlockModelDispatcher.loadDataModel("signalindustries:block/multi_conduit/frame"));
	}

	@Override
	public boolean renderAttached(@NotNull TessellatorGeneral tessellator, @NotNull WorldSource worldSource, @NotNull TilePosc tilePos, boolean cullFaces, @Nullable IconCoordinate overrideTexture) {
		TileEntityMultiConduit tile = (TileEntityMultiConduit) worldSource.getTileEntity(tilePos);

		if (tile.conduits[0] == null && tile.conduits[1] == null && tile.conduits[2] == null && tile.conduits[3] == null) {
			return super.renderAttached(tessellator, worldSource, tilePos, cullFaces, overrideTexture);
		}

		Vec3i pos = new Vec3i(tilePos);
		boolean split = false;
		for (Direction dir : Direction.values()) {
			Block<?> b = dir.getBlock(worldSource, pos);
			BlockLogic connectedBlock = b.getLogic();
			if (connectedBlock instanceof BlockLogicMultiConduit) {
				Direction side = Direction.getDirectionFromSide(worldSource.getBlockData(tilePos));
				if (side != dir && side != dir.getOpposite()) {
					split = true;
					break;
				}
			}
			if (connectedBlock instanceof IConduitBlock) {
				split = true;
				break;
			}
		}

		if(split){
			BlockModelMojangData model = BlockModelDispatcher.loadDataModel("signalindustries:block/multi_conduit/casing");
			return model.asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
		}

		for (int i = 0; i < 4; i++) {
			if(tile.conduits[i] == null) continue;
			Tier tier = null;
			if(tile.conduits[i] instanceof ITiered tiered){
				tier = tiered.getTier();
			}
			BlockModelData model = loadConduitModel(tile.conduits[i].getConduitCapability(), i, tier);
			final Direction dir = Direction.getDirectionFromSide(worldSource.getBlockData(tilePos));
			switch (dir) {
				case Y_POS -> {
					model.asModel().renderAttached(this, tessellator, worldSource, tilePos, 1, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
				}
				case Y_NEG -> {
					model.asModel().renderAttached(this, tessellator, worldSource, tilePos, 1, 0, 0, 0, 0, 0, false, cullFaces, overrideTexture);
				}
				case Z_NEG -> {
					model.asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, -4, 0, 0, 0, false, cullFaces, overrideTexture);
				}
				case Z_POS -> {
					model.asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, 0, -4, 0, 0, 0, false, cullFaces, overrideTexture);
				}
				case X_NEG -> {
					model.asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, -1, 0, 0, 0, 0, false, cullFaces, overrideTexture);
				}
				case X_POS -> {
					model.asModel().renderAttached(this, tessellator, worldSource, tilePos, 0, -1, 0, 0, 0, 0, false, cullFaces, overrideTexture);
				}
			}
		}

		return true;
	}

	public static BlockModelData loadConduitModel(ConduitCapability type, int position, Tier tier) {
		return BlockModelDispatcher.loadDataModel(String.format("signalindustries:block/multi_conduit/%s/%d/%s",type.name().toLowerCase(),position+1,tier == null ? "null" : tier.name().toLowerCase()));
	}
}
