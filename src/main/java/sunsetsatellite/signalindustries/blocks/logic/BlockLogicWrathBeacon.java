package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicRotatable;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicTiered;
import sunsetsatellite.signalindustries.tiles.base.TileEntityWrathBeaconBase;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityWrathBeacon;
import sunsetsatellite.signalindustries.tiles.machines.multiblocks.TileEntityReinforcedWrathBeacon;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockLogicWrathBeacon extends BlockLogicTiered {

    public BlockLogicWrathBeacon(Block<?> block, Material material, Tier tier) {
        super(block, material, tier);
        block.withEntity(tier == Tier.REINFORCED ? TileEntityReinforcedWrathBeacon::new : TileEntityWrathBeacon::new);
    }

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if (world.isClientSide) {
			return true;
		} else {
			if (tier == Tier.BASIC) {
				TileEntityWrathBeaconBase tile = (TileEntityWrathBeaconBase) world.getTileEntity(tilePos);
				if (tile != null) {
					tile.activate(player);
				}
			} else {
				TileEntityReinforcedWrathBeacon tile = (TileEntityReinforcedWrathBeacon) world.getTileEntity(tilePos);
				if (tile != null && tile.multiblock != null && tile.multiblock.isValid()) {
					tile.activate(player);
					//entityplayer.triggerAchievement(SIAchievements.HORIZONS);
				} else {
					player.sendTranslatedChatMessage("event.signalindustries.invalidMultiblock");
				}
			}
			return true;
		}
	}

	@Override
	public void onRemoved(@NotNull World world, @NotNull TilePosc tilePos, int data) {
		TileEntityWrathBeaconBase tile = (TileEntityWrathBeaconBase) world.getTileEntity(tilePos);
		if (tile != null && tile.active) {
			for (Player player : world.players) {
				if (player.distanceToSqr(tilePos.x(), tilePos.y(), tilePos.z()) > 64) continue;
				player.sendMessage("Challenge failed!");
			}

			if (tier == Tier.REINFORCED) {
				TileEntityReinforcedWrathBeacon w = (TileEntityReinforcedWrathBeacon) tile;
				for (BlockInstance bi : w.multiblock.data.getBlocks(new Vec3i(tilePos), Direction.Z_POS)) {
					if (world.getBlockId(bi.pos.x, bi.pos.y, bi.pos.z) == SIBlocks.fueledEternalTreeLog.id()) {
						world.setBlockWithNotify(bi.pos.x, bi.pos.y, bi.pos.z, bi.block.id());
					}
				}
			}
		}
		super.onRemoved(world, tilePos, data);
	}

	@Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityWrathBeaconBase tile = (TileEntityWrathBeaconBase) world.getTileEntity(i, j, k);
        if (tile != null && tile.active) {
            for (Player player : world.players) {
                if (player.distanceToSqr(i, j, k) > 64) continue;
                player.sendMessage("Challenge failed!");
            }

            if (tier == Tier.REINFORCED) {
                TileEntityReinforcedWrathBeacon w = (TileEntityReinforcedWrathBeacon) tile;
                for (BlockInstance bi : w.multiblock.data.getBlocks(new Vec3i(i, j, k), Direction.Z_POS)) {
                    if (world.getBlockId(bi.pos.x, bi.pos.y, bi.pos.z) == SIBlocks.fueledEternalTreeLog.id()) {
                        world.setBlockWithNotify(bi.pos.x, bi.pos.y, bi.pos.z, bi.block.id());
                    }
                }
            }
        }


        super.onBlockRemoved(world, i, j, k, data);
    }

    @Override
    public String getDescription(ItemStack stack) {
        if (tier == Tier.REINFORCED) {
            String s = super.getDescription(stack);
            return s + "\n" + TextFormatting.YELLOW + "Multiblock" + TextFormatting.WHITE;
        } else {
            return super.getDescription(stack);
        }
    }

    public static final int MASK_DIRECTION = 0b0000_0111;

    public static net.minecraft.core.util.helper.Direction getDirectionFromMeta(int meta) {
        return net.minecraft.core.util.helper.Direction.fromId(meta & MASK_DIRECTION);
    }

    public static int setDirection(int meta, net.minecraft.core.util.helper.Direction direction) {
        return (meta & ~MASK_DIRECTION) | direction.id;
    }

    @Override
    public void onBlockPlacedByMob(World world, int x, int y, int z, @NotNull Side side, Mob mob, double xPlaced, double yPlaced) {
        world.setBlockMetadataWithNotify(x, y, z, mob.getHorizontalPlacementDirection(side).opposite().id);
    }

    @Override
    public void onBlockPlacedOnSide(World world, int x, int y, int z, @NotNull Side side, double xPlaced, double yPlaced) {
        if (!side.isHorizontal()) side = Side.SOUTH;
        world.setBlockMetadataWithNotify(x, y, z, BlockLogicRotatable.setDirection(0, side.direction()));
    }

    public static void setDefaultDirection(World world, int x, int y, int z) {
        if (world.isClientSide) {
            return;
        }
        int bN = world.getBlockId(x, y, z - 1);
        int bS = world.getBlockId(x, y, z + 1);
        int bW = world.getBlockId(x - 1, y, z);
        int bE = world.getBlockId(x + 1, y, z);
        net.minecraft.core.util.helper.Direction direction = net.minecraft.core.util.helper.Direction.NORTH;
        if (Blocks.solid[bN] && !Blocks.solid[bS]) {
            direction = net.minecraft.core.util.helper.Direction.SOUTH;
        }
        if (Blocks.solid[bS] && !Blocks.solid[bN]) {
            direction = net.minecraft.core.util.helper.Direction.NORTH;
        }
        if (Blocks.solid[bW] && !Blocks.solid[bE]) {
            direction = net.minecraft.core.util.helper.Direction.EAST;
        }
        if (Blocks.solid[bE] && !Blocks.solid[bW]) {
            direction = net.minecraft.core.util.helper.Direction.WEST;
        }
        world.setBlockMetadataWithNotify(x, y, z, setDirection(world.getBlockMetadata(x, y, z), direction));
    }
}
