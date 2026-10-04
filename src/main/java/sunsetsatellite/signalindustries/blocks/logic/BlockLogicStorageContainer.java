package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.gamemode.Gamemodes;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicTiered;
import sunsetsatellite.signalindustries.tiles.TileEntityStorageContainer;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class BlockLogicStorageContainer extends BlockLogicTiered {
    public BlockLogicStorageContainer(Block<?> block, Material material, Tier tier) {
        super(block, material, tier);
        block.withEntity(TileEntityStorageContainer::new);
    }

	@Override
	public void onAttacked(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @NotNull Side side, double xHit, double yHit) {
		super.onAttacked(world, tilePos, player, side, xHit, yHit);
		if (!EnvironmentHelper.isMultiplayerClient()) {
			TileEntityStorageContainer tile = (TileEntityStorageContainer) world.getTileEntity(tilePos);
			if (tile != null) {
				if (player.getCurrentEquippedItem() == null || !(player.getCurrentEquippedItem().getItem() instanceof ItemTool)) {
					ItemStack stack;
					if (player.isSneaking()) {
						stack = tile.extractStack(64);
					} else {
						stack = tile.extractStack(1);
					}
					if (stack != null) {
						Vec3f vec = new Vec3f(tilePos).add(Direction.getDirectionFromSide(world.getBlockData(tilePos)).getVecF()).add(0.5f);
						EntityItem entityitem = new EntityItem(world, vec.x, vec.y, vec.z, stack);
						world.entityJoinedWorld(entityitem);
					}
				}
			}
		}
	}

	@Override
	public boolean onInteracted(@NotNull World world, @NotNull TilePosc tilePos, @NotNull Player player, @Nullable Side side, double xHit, double yHit) {
		super.onInteracted(world, tilePos, player, side, xHit, yHit);
		if (!EnvironmentHelper.isMultiplayerClient()) {
			TileEntityStorageContainer tile = (TileEntityStorageContainer) world.getTileEntity(tilePos);
			if (tile != null) {
				if (player.getCurrentEquippedItem() != null) {
					if (player.isSwinging) {
						tile.insertStack(player.getCurrentEquippedItem());
						if (player.getCurrentEquippedItem().stackSize <= 0) {
							player.destroyCurrentEquippedItem();
						} else {
							player.getCurrentEquippedItem().animationsToGo = 5;
						}
					} else {
						ItemStack stack = player.getCurrentEquippedItem().copy();
						stack.stackSize = 1;
						if (tile.insertStack(stack)) {
							player.getCurrentEquippedItem().stackSize--;
							if (player.getCurrentEquippedItem().stackSize <= 0) {
								player.destroyCurrentEquippedItem();
							} else {
								player.getCurrentEquippedItem().animationsToGo = 5;
							}
						}
					}
					return true;
				} else {
					if (tile.infinite && player.gamemode == Gamemodes.CREATIVE) {
						tile.contents = null;
					} else {
						if(player.isSneaking()){
							@Nullable ItemStack @NotNull [] mainInventory = player.inventory.mainInventory;
							for (int i = 0; i < mainInventory.length; i++) {
								ItemStack stack = mainInventory[i];
								if (stack != null && stack.stackSize > 0 && stack.canStackWith(tile.contents)) {
									tile.insertStack(stack);
									if (stack.stackSize <= 0) {
										player.inventory.setItem(i, null);
									}
								}
							}
						} else {
							tile.locked = !tile.locked;
							if (tile.locked) {
								player.sendTranslatedChatMessage("event.signalindustries.containerLocked");
							} else {
								player.sendTranslatedChatMessage("event.signalindustries.containerUnlocked");
							}
						}
					}
				}
			}
		}
		return true;
	}

}
