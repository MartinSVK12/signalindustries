package sunsetsatellite.signalindustries.blocks;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerDimAnchor;
import sunsetsatellite.signalindustries.gui.GuiDimAnchor;
import sunsetsatellite.signalindustries.inventories.TileEntityDimensionalAnchor;
import sunsetsatellite.signalindustries.inventories.TileEntityStabilizer;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.sunsetutils.util.BlockInstance;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.util.ArrayList;
import java.util.Random;

public class BlockDimensionalAnchor extends BlockContainerTiered {

    public BlockDimensionalAnchor(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityDimensionalAnchor();
    }

    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        super.onBlockAdded(world, i, j, k);
    }

    @Override
    public String getDescription(ItemStack stack) {
        String s = super.getDescription(stack);
        return s+"\n"+TextFormatting.YELLOW+"Multiblock"+ TextFormatting.WHITE;
    }

    @Override
    public void onBlockRemoval(World world, int i, int j, int k) {
        TileEntityDimensionalAnchor tile = (TileEntityDimensionalAnchor) world.getBlockTileEntity(i, j, k);
        if (tile != null) {
            for (Direction dir : Direction.values()) {

                if(dir == Direction.Y_NEG || dir == Direction.Y_POS) continue;
                Vec3i v = dir.getVec().multiply(2);
                Vec3i tileVec = new Vec3i(i,j,k);
                TileEntity stabilizer = dir.getTileEntity(world,tileVec.add(v));
                if(stabilizer instanceof TileEntityStabilizer){
                    ((TileEntityStabilizer) stabilizer).connectedTo = null;
                }

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
        world.setBlockMetadata(i,j,k,3);
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityDimensionalAnchor tile = (TileEntityDimensionalAnchor) world.getBlockTileEntity(i, j, k);
            if(tile.multiblock.isValidAt(world,new BlockInstance(this,new Vec3i(i,j,k),tile),Direction.getDirectionFromSide(world.getBlockMetadata(i,j,k)))){
                SignalIndustries.displayGui(entityplayer,new GuiDimAnchor(entityplayer.inventory, tile),new ContainerDimAnchor(entityplayer.inventory,tile),tile,i,j,k);
            } else {
                entityplayer.addChatMessage("event.signalindustries.invalidMultiblock");
            }
            return true;
        }
    }

    @Override
    public int getBlockTexture(WorldSource iblockaccess, int i, int j, int k, Side side) {
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
        int index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
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
