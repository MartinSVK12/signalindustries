package sunsetsatellite.signalindustries.blocks.logic;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.blocks.logic.base.BlockLogicTiered;
import sunsetsatellite.signalindustries.tiles.TileEntityStorageContainer;
import sunsetsatellite.signalindustries.util.Tier;

public class BlockLogicStorageContainer extends BlockLogicTiered {
    public BlockLogicStorageContainer(Block<?> block, Material material, Tier tier) {
        super(block, material, tier);
        block.withEntity(TileEntityStorageContainer::new);
    }

    @Override
    public void onBlockLeftClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
        super.onBlockLeftClicked(world, x, y, z, player, side, xHit, yHit);
        TileEntityStorageContainer tile = (TileEntityStorageContainer) world.getTileEntity(x, y, z);
        if(tile != null){
            if (player.getCurrentEquippedItem() == null || !(player.getCurrentEquippedItem().getItem() instanceof ItemTool)) {
                ItemStack stack;
                if (!player.isSneaking()) {
                    stack = tile.extractStack(1);
                } else {
                    stack = tile.extractStack();
                }
                if (stack != null) {
                    Vec3f vec = new Vec3f(x, y, z).add(Direction.getDirectionFromSide(world.getBlockMetadata(x, y, z)).getVecF()).add(0.5f);
                    EntityItem entityitem = new EntityItem(world, vec.x, vec.y, vec.z, stack);
                    world.entityJoinedWorld(entityitem);
                }
            }
        }
    }

    @Override
    public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xHit, double yHit) {
        super.onBlockRightClicked(world, x, y, z, player, side, xHit, yHit);
        TileEntityStorageContainer tile = (TileEntityStorageContainer) world.getTileEntity(x, y, z);
        if (tile != null) {
            if (player.getCurrentEquippedItem() != null) {
                if (player.getCurrentEquippedItem().animationsToGo <= 0) {
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
                } else {
                    tile.insertStack(player.getCurrentEquippedItem());
                    if (player.getCurrentEquippedItem().stackSize <= 0) {
                        player.destroyCurrentEquippedItem();
                    } else {
                        player.getCurrentEquippedItem().animationsToGo = 5;
                    }
                }
                return true;
            } else {
                if (tile.infinite && player.gamemode == Gamemode.creative) {
                    tile.contents = null;
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
        return false;
    }
}
