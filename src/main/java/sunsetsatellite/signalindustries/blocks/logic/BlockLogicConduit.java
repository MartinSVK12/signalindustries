package sunsetsatellite.signalindustries.blocks.logic;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.conduit.ConduitCapability;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicConduitBase;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

public class BlockLogicConduit extends BlockLogicConduitBase {
    public BlockLogicConduit(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier) {
        super(block, material, tier, tileEntitySupplier, ConduitCapability.SIGNALUM);
    }

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		ItemStack stack = player.getCurrentEquippedItem();
		TileEntity tile = world.getTileEntity(tilePos);
		if(tile != null && stack != null && Blocks.getBlock(stack.getItem().id).getLogic() instanceof BlockLogicConduit conduit){
			if(conduit.tier.ordinal() > tier.ordinal()){
				if (stack.consumeItem(player)) {
					CompoundTag data = new CompoundTag();
					tile.writeToNBT(data);
					world.setBlockTypeRaw(tilePos, conduit.block);
					tile = world.getTileEntity(tilePos);
					if(tile != null) tile.readFromNBT(data);
					player.inventory.insertItem(getDefaultStack(), false);
				}
				return true;
			}
		}
		return super.onInteracted(world, tilePos, player, side, xHit, yHit);
	}
}
