package sunsetsatellite.signalindustries.blocks;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.tiles.TileEntityDimensionalAnchor;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.sunsetutils.util.BlockInstance;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.util.ArrayList;
import java.util.Random;

public class BlockDimensionalAnchor extends BlockContainerTiered {
    public BlockDimensionalAnchor(int i, Tier tier, Material material) {
        super(i, tier, material);
    }

    @Override
    protected TileEntity getBlockEntity() {
        return new TileEntityDimensionalAnchor();
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
    }

    @Override
    public String getDescription(ItemStack stack) {
        String s = super.getDescription(stack);
        return s+"\n"+ChatColor.yellow+"Multiblock"+ChatColor.white;
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityDimensionalAnchor tile = (TileEntityDimensionalAnchor) world.getBlockTileEntity(i, j, k);
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
        world.setBlockMetadata(i,j,k,3);
        if(world.isMultiplayerAndNotHost)
        {
            return true;
        } else
        {
            TileEntityDimensionalAnchor tile = (TileEntityDimensionalAnchor) world.getBlockTileEntity(i, j, k);
            if(tile.multiblock.isValidAt(world,new BlockInstance(this,new Vec3i(i,j,k),tile),Direction.getDirectionFromSide(world.getBlockMetadata(i,j,k)))){
                Minecraft.getMinecraft().ingameGUI.addChatMessage("Multiblock complete!");
                //TODO: Open machine if multiblock valid
            } else {
                entityplayer.addChatMessage("event.signalindustries.invalidMultiblock");
            }
            /*if(tile != null) {
                SignalIndustries.displayGui(entityplayer,new GuiInfuser(entityplayer.inventory, tile),new ContainerInfuser(entityplayer.inventory,tile),tile,i,j,k);
            }*/
            return true;
        }
    }

    @Override
    public int getBlockTexture(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        TileEntityDimensionalAnchor tile = (TileEntityDimensionalAnchor) iblockaccess.getBlockTileEntity(i,j,k);
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
        if(index == 1){
            if(tile.isBurning()){
                return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.anchorTex[4][0],SignalIndustries.anchorTex[4][1]);
            }
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.anchorTex[1][0],SignalIndustries.anchorTex[1][1]);
        }
        if(index == 0){
            if(tile.isBurning()){
                return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.anchorTex[5][0],SignalIndustries.anchorTex[5][1]);
            }
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.anchorTex[2][0],SignalIndustries.anchorTex[2][1]);
        }
        if(index > 1 && index < 6){
            if(tile.isBurning()){
                return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.anchorTex[3][0],SignalIndustries.anchorTex[3][1]);
            }
            return this.atlasIndices[index] = texCoordToIndex(SignalIndustries.anchorTex[0][0],SignalIndustries.anchorTex[0][1]);
        }
        return this.atlasIndices[index];
    }
}
