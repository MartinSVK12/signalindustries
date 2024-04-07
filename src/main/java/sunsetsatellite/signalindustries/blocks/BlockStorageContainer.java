package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.tool.ItemTool;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3f;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.inventories.TileEntityStorageContainer;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Random;

public class BlockStorageContainer extends BlockContainerTiered {
    public BlockStorageContainer(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
        withTags(SignalIndustries.ITEM_CONDUITS_CONNECT);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityStorageContainer();
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityStorageContainer tile = (TileEntityStorageContainer) world.getBlockTileEntity(i, j, k);
        if (tile != null && tile.contents != null && tier != Tier.INFINITE) {
            Random random = new Random();
            float f = random.nextFloat() * 0.8F + 0.1F;
            float f1 = random.nextFloat() * 0.8F + 0.1F;
            float f2 = random.nextFloat() * 0.8F + 0.1F;
            EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, tile.contents.copy());
            float f3 = 0.05F;
            entityitem.xd = (float) random.nextGaussian() * f3;
            entityitem.yd = (float) random.nextGaussian() * f3 + 0.2F;
            entityitem.zd = (float) random.nextGaussian() * f3;
            world.entityJoinedWorld(entityitem);
        }
        super.onBlockRemoved(world, i, j, k, data);
    }

    @Override
    public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
        super.onBlockClicked(world, x, y, z, player);
        TileEntityStorageContainer tile = (TileEntityStorageContainer) world.getBlockTileEntity(x, y, z);
        if (tile != null) {
            if(player.getCurrentEquippedItem() == null || !(player.getCurrentEquippedItem().getItem() instanceof ItemTool)){
                ItemStack stack;
                if(!player.isSneaking()){
                    stack = tile.extractStack(1);
                } else {
                    stack = tile.extractStack();
                }
                if(stack != null){
                    Vec3f vec = new Vec3f(x,y,z).add(Direction.getDirectionFromSide(world.getBlockMetadata(x,y,z)).getVecF()).add(0.5f);
                    EntityItem entityitem = new EntityItem(world, vec.x, vec.y, vec.z, stack);
                    world.entityJoinedWorld(entityitem);
                }
            }
        }
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer player)
    {
        TileEntityStorageContainer tile = (TileEntityStorageContainer) world.getBlockTileEntity(i, j, k);
        if (tile != null) {
            if(player.getCurrentEquippedItem() != null) {
                if (player.getCurrentEquippedItem().animationsToGo <= 0) {
                    ItemStack stack = player.getCurrentEquippedItem().copy();
                    stack.stackSize = 1;
                    if(tile.insertStack(stack)){
                        player.getCurrentEquippedItem().stackSize--;
                        if(player.getCurrentEquippedItem().stackSize <= 0){
                            player.destroyCurrentEquippedItem();
                        } else {
                            player.getCurrentEquippedItem().animationsToGo = 5;
                        }
                    }
                } else {
                    tile.insertStack(player.getCurrentEquippedItem());
                    if(player.getCurrentEquippedItem().stackSize <= 0){
                        player.destroyCurrentEquippedItem();
                    } else {
                        player.getCurrentEquippedItem().animationsToGo = 5;
                    }
                }
                return true;
            } else {
                if(tile.infinite && player.gamemode == Gamemode.creative){
                    tile.contents = null;
                } else {
                    tile.locked = !tile.locked;
                    if(tile.locked){
                        player.addChatMessage("event.signalindustries.containerLocked");
                    } else {
                        player.addChatMessage("event.signalindustries.containerUnlocked");
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        int meta = blockAccess.getBlockMetadata(x,y,z);
        int index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        return this.atlasIndices[index];
    }
}
