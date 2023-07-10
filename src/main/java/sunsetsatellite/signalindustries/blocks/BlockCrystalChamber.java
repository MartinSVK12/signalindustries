package sunsetsatellite.signalindustries.blocks;

import net.minecraft.src.*;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerCrystalChamber;
import sunsetsatellite.signalindustries.gui.GuiCrystalChamber;
import sunsetsatellite.signalindustries.tiles.TileEntityCrystalChamber;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.sunsetutils.util.Direction;

import java.util.ArrayList;
import java.util.Random;

public class BlockCrystalChamber extends BlockContainerTiered{
    public BlockCrystalChamber(int i, Tier tier, Material material) {
        super(i, tier, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityCrystalChamber();
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityCrystalChamber tile = (TileEntityCrystalChamber) world.getBlockTileEntity(i, j, k);
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

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityCrystalChamber tile = (TileEntityCrystalChamber) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer,new GuiCrystalChamber(entityplayer.inventory, tile),new ContainerCrystalChamber(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        TileEntityCrystalChamber tile = (TileEntityCrystalChamber) iblockaccess.getBlockTileEntity(i,j,k);
        int meta = iblockaccess.getBlockMetadata(i,j,k);
        /*
        this.atlasIndices[1] = texCoordToIndex(topX, topY);
        this.atlasIndices[0] = texCoordToIndex(bottomX, bottomY);
        this.atlasIndices[4] = texCoordToIndex(northX, northY);
        this.atlasIndices[2] = texCoordToIndex(eastX, eastY);
        this.atlasIndices[5] = texCoordToIndex(southX, southY);
        this.atlasIndices[3] = texCoordToIndex(westX, westY);
         */
        int index = Sides.orientationLookUp[6 * meta + side];
        if(index == 4){
            if(tile.isBurning()){
                return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.crystalChamberTex[1][0],SignalIndustries.crystalChamberTex[1][1]);
            }
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.crystalChamberTex[0][0],SignalIndustries.crystalChamberTex[0][1]);
        }
        return this.atlasIndices[index];
    }
}
