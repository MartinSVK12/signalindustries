package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.gui.GuiMultiConduitConfig;
import sunsetsatellite.catalyst.core.util.IConduitBlock;
import sunsetsatellite.signalindustries.inventories.TileEntityMultiConduit;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Random;

public class BlockMultiConduit extends BlockContainerTiered {
    public BlockMultiConduit(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityMultiConduit();
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntity tile = world.getBlockTileEntity(x,y,z);
        if(tile instanceof TileEntityMultiConduit) {
            TileEntityMultiConduit multiConduit = (TileEntityMultiConduit) tile;
            for (IConduitBlock conduit : multiConduit.conduits) {
                if(conduit == null) continue;
                Random random = new Random();
                float xr = random.nextFloat() * 0.8F + 0.1F;
                float yr = random.nextFloat() * 0.8F + 0.1F;
                float zr = random.nextFloat() * 0.8F + 0.1F;

                EntityItem entityitem = new EntityItem(world, (float) x + xr, (float) y + yr, (float) z + zr, new ItemStack(((Block) conduit)));
                float f3 = 0.05F;
                entityitem.xd = (float) random.nextGaussian() * f3;
                entityitem.yd = (float) random.nextGaussian() * f3 + 0.2F;
                entityitem.zd = (float) random.nextGaussian() * f3;
                world.entityJoinedWorld(entityitem);
            }
        }
        super.onBlockRemoved(world,x,y,z,data);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if(player.getCurrentEquippedItem() != null){
            if(player.getCurrentEquippedItem().itemID < 16384){
                Block block = Block.getBlock(player.getCurrentEquippedItem().itemID);
                if(block instanceof IConduitBlock){
                    IConduitBlock conduit = (IConduitBlock) block;
                    TileEntity tile = world.getBlockTileEntity(x,y,z);
                    if(tile instanceof TileEntityMultiConduit){
                        TileEntityMultiConduit multiConduit = (TileEntityMultiConduit) tile;
                        if(multiConduit.addConduit(conduit)){
                            player.getCurrentEquippedItem().consumeItem(player);
                            return true;
                        }
                    }
                }
            }
        } else {
            TileEntity tile = world.getBlockTileEntity(x,y,z);
            if(tile instanceof TileEntityMultiConduit){
                boolean normalConduitsConnected = false;
                Vec3i pos = new Vec3i(x,y,z);
                for (Direction dir : Direction.values()) {
                    Block connectedBlock = dir.getBlock(world, pos);
                    if(connectedBlock instanceof IConduitBlock){
                        normalConduitsConnected = true;
                        break;
                    }
                }
                TileEntityMultiConduit multiConduit = (TileEntityMultiConduit) tile;
                if(normalConduitsConnected){
                    SignalIndustries.displayGui(player, () -> new GuiMultiConduitConfig(player, multiConduit, null), multiConduit, tile.x,tile.y,tile.z);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        world.setBlockMetadataWithNotify(x, y, z, entity.getPlacementDirection(side).getOpposite().getId());
    }
}
