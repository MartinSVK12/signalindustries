package sunsetsatellite.signalindustries.api.impl.itempipes.blocks;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.api.impl.itempipes.containers.ContainerFilter;
import sunsetsatellite.signalindustries.api.impl.itempipes.gui.GuiFilter;
import sunsetsatellite.signalindustries.api.impl.itempipes.tiles.TileEntityFilter;
import sunsetsatellite.signalindustries.containers.ContainerAlloySmelter;
import sunsetsatellite.signalindustries.gui.GuiAlloySmelter;
import sunsetsatellite.signalindustries.tiles.TileEntityAlloySmelter;
import sunsetsatellite.signalindustries.tiles.TileEntityExtractor;

import java.util.ArrayList;
import java.util.Random;

public class BlockFilter extends BlockContainer {
    public BlockFilter(int i, Material material) {
        super(i, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityFilter();
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityFilter tile = (TileEntityFilter) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer,new GuiFilter(entityplayer.inventory, tile),new ContainerFilter(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityFilter tile = (TileEntityFilter) world.getBlockTileEntity(i, j, k);
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
                        EntityItem entityitem = new EntityItem(world, (float) i + f, (float) j + f1, (float) k + f2, new ItemStack(itemstack.itemID, i1, itemstack.getMetadata()));
                        float f3 = 0.05F;
                        entityitem.motionX = (float) random.nextGaussian() * f3;
                        entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
                        entityitem.motionZ = (float) random.nextGaussian() * f3;
                        world.entityJoinedWorld(entityitem);
                    }
                }
            }
        }

        super.onBlockRemoval(world, i, j, k);
    }
}
