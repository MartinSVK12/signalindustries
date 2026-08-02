package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.conduit.IConduitBlock;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicTiered;
import sunsetsatellite.signalindustries.tiles.conduit.TileEntityMultiConduit;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.Random;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class BlockLogicMultiConduit extends BlockLogicTiered {
    public BlockLogicMultiConduit(Block<?> block, Material material, Tier tier) {
        super(block, material, tier);
        block.withEntity(TileEntityMultiConduit::new);
    }

	@Override
	public void onRemoved(@NotNull World world, @NotNull TilePosc tilePos, int data) {
		TileEntity tile = world.getTileEntity(tilePos);
		if (tile instanceof TileEntityMultiConduit multiConduit) {
			for (IConduitBlock conduit : multiConduit.conduits) {
				if (conduit == null) continue;
				Random random = new Random();
				float xr = random.nextFloat() * 0.8F + 0.1F;
				float yr = random.nextFloat() * 0.8F + 0.1F;
				float zr = random.nextFloat() * 0.8F + 0.1F;

				EntityItem entityitem = new EntityItem(world, (float) tilePos.x() + xr, (float) tilePos.y() + yr, (float) tilePos.z() + zr, new ItemStack((BlockLogic) conduit));
				float f3 = 0.05F;
				entityitem.xd = (float) random.nextGaussian() * f3;
				entityitem.yd = (float) random.nextGaussian() * f3 + 0.2F;
				entityitem.zd = (float) random.nextGaussian() * f3;
				world.entityJoinedWorld(entityitem);
			}
		}
		super.onRemoved(world, tilePos, data);
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		if (player.getCurrentEquippedItem() != null) {
			if (player.getCurrentEquippedItem().itemID < 16384) {
				Block<?> b = Blocks.getBlock(player.getCurrentEquippedItem().itemID);
				if (b == null) return false;
				BlockLogic block = b.getLogic();
				if (block instanceof IConduitBlock conduit) {
					TileEntity tile = world.getTileEntity(tilePos);
					if (tile instanceof TileEntityMultiConduit multiConduit) {
						if (multiConduit.addConduit(conduit)) {
							player.getCurrentEquippedItem().consumeItem(player);
							return true;
						}
					}
				}
			}
		} else {
			TileEntity tile = world.getTileEntity(tilePos);
			if (tile instanceof TileEntityMultiConduit multiConduit) {
				boolean normalConduitsConnected = false;
				Vec3i pos = new Vec3i(tilePos);
				for (Direction dir : Direction.values()) {
					Block<?> b = dir.getBlock(world, pos);
					if (b == null) continue;
					BlockLogic connectedBlock = b.getLogic();
					if (connectedBlock instanceof IConduitBlock) {
						normalConduitsConnected = true;
						break;
					}
				}
				if (normalConduitsConnected && !EnvironmentHelper.isMultiplayerServer()) {
					Catalyst.displayGui(player, multiConduit, key("gui/multi_conduit"));
					return true;
				}
			}
		}
		return false;
	}

    @Override
    public boolean isSolidRender() {
        return false;
    }
}
