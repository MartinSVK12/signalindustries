package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerFluidHatch;
import sunsetsatellite.signalindustries.gui.GuiFluidHatch;
import sunsetsatellite.signalindustries.inventories.TileEntityFluidHatch;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.sunsetutils.util.Direction;

import java.util.ArrayList;
import java.util.Random;

public class BlockFluidInputHatch extends BlockContainerTiered{
    public BlockFluidInputHatch(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityFluidHatch();
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityFluidHatch tile = (TileEntityFluidHatch) world.getBlockTileEntity(i, j, k);
        if (tile != null) {
            for (Direction dir : Direction.values()) {
                TileEntity tile2 = dir.getTileEntity(world, tile);
                if (tile2 instanceof TileEntityFluidPipe) {
                    tile.unpressurizePipes((TileEntityFluidPipe) tile2, new ArrayList<>());
                }
            }
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
                        entityitem.xd = (float) random.nextGaussian() * f3;
                        entityitem.yd = (float) random.nextGaussian() * f3 + 0.2F;
                        entityitem.zd = (float) random.nextGaussian() * f3;
                        world.entityJoinedWorld(entityitem);
                    }
                }
            }
        }

        super.onBlockRemoval(world, i, j, k);
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityFluidHatch tile = (TileEntityFluidHatch) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer,new GuiFluidHatch(entityplayer.inventory, tile),new ContainerFluidHatch(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }
}
