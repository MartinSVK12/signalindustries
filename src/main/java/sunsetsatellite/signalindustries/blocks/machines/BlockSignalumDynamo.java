package sunsetsatellite.signalindustries.blocks.machines;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.containers.ContainerSignalumDynamo;
import sunsetsatellite.signalindustries.gui.GuiSignalumDynamo;
import sunsetsatellite.signalindustries.inventories.machines.TileEntitySignalumDynamo;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.Random;

public class BlockSignalumDynamo extends BlockMachineBase {
    public BlockSignalumDynamo(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntitySignalumDynamo();
    }

    @Override
    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(super.blockActivated(world, i, j, k, entityplayer)){
            return true;
        }
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntitySignalumDynamo tile = (TileEntitySignalumDynamo) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer,() -> new GuiSignalumDynamo(entityplayer.inventory, tile),new ContainerSignalumDynamo(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        TileEntitySignalumDynamo tile = (TileEntitySignalumDynamo) world.getBlockTileEntity(x, y, z);
        if (tile != null) {
           
            Random random = new Random();
            for (int l = 0; l < tile.getSizeInventory(); ++l) {
                ItemStack itemstack = tile.getStackInSlot(l);
                if (itemstack != null) {
                    float f = random.nextFloat() * 0.8F + 0.1F;
                    float f1 = random.nextFloat() * 0.8F + 0.1F;
                    float f2 = random.nextFloat() * 0.8F + 0.1F;

                    while (itemstack.stackSize > 0) {
                        int i1 = random.nextInt(21) + 10;
                        if (i1 > itemstack.stackSize) {
                            i1 = itemstack.stackSize;
                        }

                        itemstack.stackSize -= i1;
                        EntityItem entityitem = new EntityItem(world, (float) x + f, (float) y + f1, (float) z + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata()));
                        float f3 = 0.05F;
                        entityitem.xd = (float) random.nextGaussian() * f3;
                        entityitem.yd = (float) random.nextGaussian() * f3 + 0.2F;
                        entityitem.zd = (float) random.nextGaussian() * f3;
                        world.entityJoinedWorld(entityitem);
                    }
                }
            }
        }
        super.onBlockRemoved(world, x, y, z, data);
    }

}
