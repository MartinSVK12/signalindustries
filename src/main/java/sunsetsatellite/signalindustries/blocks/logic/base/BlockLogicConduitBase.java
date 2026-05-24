package sunsetsatellite.signalindustries.blocks.logic.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.Pair;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBdc;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.IWrench;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.catalyst.core.util.conduit.IConduitBlock;
import sunsetsatellite.catalyst.core.util.section.BlockSection;
import sunsetsatellite.catalyst.core.util.vector.Vec2f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidPipe;
import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.items.ItemConfigurationTablet;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.ConfigurationTabletMode;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Objects;
import java.util.function.Supplier;

public class BlockLogicConduitBase extends BlockLogicNonSolid implements ITiered, IConduitBlock {
    public Tier tier;
    public final ConduitCapability conduitCapability;

    public BlockLogicConduitBase(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier, ConduitCapability conduitCapability) {
        super(block, material);
        this.tier = tier;
        this.conduitCapability = conduitCapability;
        block.withEntity(tileEntitySupplier);
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getTextColor() + tier.getRank();
    }

    @Override
    public Tier getTier() {
        return tier;
    }

	@Override
	public @NotNull AABBdc getBoundsFromState(@NotNull WorldSource source, @NotNull TilePosc tilePos) {
		setBlockBoundsFromState(source, tilePos.x(), tilePos.y(), tilePos.z());
		return super.getBoundsFromState(source, tilePos);
	}

	/*@Override
    public AABB getBlockBoundsFromState(WorldSource world, int x, int y, int z) {
        setBlockBoundsFromState(world, x, y, z);
        return super.getBlockBoundsFromState(world, x, y, z);
    }*/

    public void setBlockBoundsFromState(WorldSource world, int x, int y, int z) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof ISupportsMultiparts) {
            if (((ISupportsMultiparts) tile).getParts().values().stream().anyMatch(Objects::nonNull)) {
                setBlockBounds(0, 0, 0, 1, 1, 1);
                return;
            }
        }
        float bx = 0.3f, by = 0.3f, bz = 0.3f;
        float tx = 0.7f, ty = 0.7f, tz = 0.7f;
        // Loop de-loop
        for (Direction dir : Direction.values()) {
            Vec3i v = dir.getVec();
            TileEntity te = world.getTileEntity(x + v.x, y + v.y, z + v.z);
            Block<?> b = world.getBlock(x + v.x, y + v.y, z + v.z);
            if (!(te instanceof TileEntityFluidPipe)) {
                if (b != null && b.getLogic() instanceof IConduitBlock) {
                    if (getConduitCapability() != ((IConduitBlock) b.getLogic()).getConduitCapability()) {
                        continue;
                    }
                } else {
                    continue;
                }
            }
            if (v.x > 0) tx = 1.0f;
            else if (v.x < 0) bx = 0.0f;
            if (v.z > 0) tz = 1.0f;
            else if (v.z < 0) bz = 0.0f;
            if (v.y > 0) ty = 1.0f;
            else if (v.y < 0) by = 0.0f;
        }
        setBlockBounds(bx, by, bz, tx, ty, tz);
    }

    @Override
    public ConduitCapability getConduitCapability() {
        return conduitCapability;
    }

    @Override
    public boolean onBlockRightClicked(@NonNull World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
        if (!isPlayerHoldingWrench(player)) {
            return false;
        }

        Pair<Direction, BlockSection> pair = Catalyst.getBlockSurfaceClickPosition(world, player, side, new Vec2f(xHit, yHit));
        Side playerFacing = Catalyst.calculatePlayerFacing(player.yRot);
        if (pairIsInvalid(pair)) {
            return false;
        }

        if (isPlayerHoldingConfigTablet(player)) {
            handleConfigTabletAction(player, pair, world, x, y, z, playerFacing);
        }
        return true;
    }

    private boolean isPlayerHoldingWrench(Player player) {
        return player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IWrench;
    }

    private boolean pairIsInvalid(Pair<Direction, BlockSection> pair) {
        return pair == null || pair.getLeft() == null || pair.getRight() == null;
    }

    private boolean isPlayerHoldingConfigTablet(Player player) {
        return player.getCurrentEquippedItem().getItem() instanceof ItemConfigurationTablet;
    }

    private void handleConfigTabletAction(Player player, Pair<Direction, BlockSection> pair,
                                          World world, int x, int y, int z, Side playerFacing) {

        ConfigurationTabletMode mode = ConfigurationTabletMode.values()[player.getCurrentEquippedItem().getData().getInteger("mode")];
        if (Objects.requireNonNull(mode) == ConfigurationTabletMode.DISCONNECTOR) {
            handlePipeDisconnect(pair, world, x, y, z, playerFacing, player);
        }
    }

    private void handlePipeDisconnect(Pair<Direction, BlockSection> pair, World world, int x, int y, int z, Side playerFacing, Player player) {
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile instanceof TileEntityItemConduit) {
            Direction dir = pair.getRight().toDirection(pair.getLeft(), playerFacing);
            ((TileEntityItemConduit) tile).noConnectDirections.put(dir, !((TileEntityItemConduit) tile).noConnectDirections.get(dir));
        }
    }

	@Override
	public @NotNull ItemStack @Nullable [] getBreakResult(@NotNull World world, @NotNull EnumDropCause dropCause, @NotNull TilePosc tilePos, int data, @Nullable TileEntity tileEntity) {
		ItemStack[] breakResult = super.getBreakResult(world, dropCause, tilePos, data, tileEntity);
		/*if (tileEntity instanceof ISupportsMultiparts) {
			List<ItemStack> list = new ArrayList<>();
			for (Multipart multipart : ((ISupportsMultiparts) tileEntity).getParts().values()) {
				if (multipart == null) continue;
				ItemStack stack = new ItemStack(CatalystMultipart.multipartItem, 1, 0);
				CompoundTag tag = new CompoundTag();
				CompoundTag multipartTag = new CompoundTag();
				multipartTag.putString("Type", multipart.type.name);
				multipartTag.putInt("Block", multipart.block.id());
				multipartTag.putInt("Meta", multipart.meta);
				if (multipart.side != null) {
					multipartTag.putInt("Side", multipart.side.getId());
				}
				tag.putCompound("Multipart", multipartTag);
				stack.setData(tag);
				list.add(stack);
			}
			if (breakResult != null) list.add(breakResult[0]);
			return list.toArray(new ItemStack[0]);
		}*/
		return breakResult;
	}
}
