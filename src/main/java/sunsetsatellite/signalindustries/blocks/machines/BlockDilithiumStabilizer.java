package sunsetsatellite.signalindustries.blocks.machines;


import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.helper.Sides;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockMachineBase;
import sunsetsatellite.signalindustries.blocks.base.BlockVerticalMachineBase;
import sunsetsatellite.signalindustries.containers.ContainerStabilizer;
import sunsetsatellite.signalindustries.gui.GuiStabilizer;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityStabilizer;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.Random;

public class BlockDilithiumStabilizer extends BlockVerticalMachineBase {

    public BlockDilithiumStabilizer(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
        hasOverbright = true;
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityStabilizer();
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityStabilizer tile = (TileEntityStabilizer) world.getBlockTileEntity(i, j, k);
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
                        entityitem.xd = (float) random.nextGaussian() * f3;
                        entityitem.yd = (float) random.nextGaussian() * f3 + 0.2F;
                        entityitem.zd = (float) random.nextGaussian() * f3;
                        world.entityJoinedWorld(entityitem);
                    }
                }
            }
        }

        super.onBlockRemoved(world, i, j, k, data);
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
            TileEntityStabilizer tile = (TileEntityStabilizer) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer,() -> new GuiStabilizer(entityplayer.inventory, tile),new ContainerStabilizer(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }

    @Override
    public void onBlockPlaced(World world, int x, int y, int z, Side side, EntityLiving entity, double sideHeight) {
        world.setBlockMetadataWithNotify(x, y, z, entity.getPlacementDirection(side).getOpposite().getId());
    }

    /*@Override
    public int getBlockTextureFromSideAndMetadata(Side side, int meta) {

        if(SignalIndustries.textures == null) return this.atlasIndices[side.getId()];
        int index;
        int[] orientationLookUpVertical = new int[]{1, 0, 2, 3, 4, 5,  0, 1, 2, 3, 4, 5};
        if(meta == 0 || meta == 1){
           index = orientationLookUpVertical[6 * meta + side.getId()];
           return SignalIndustries.textures.get("dilithiumStabilizer.vertical").getTexture(Side.getSideById(index));
        } else {
           index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        }
        return this.atlasIndices[index];
    }

    @Override
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        TileEntityStabilizer tile = (TileEntityStabilizer) blockAccess.getBlockTileEntity(x,y,z);
        int meta = blockAccess.getBlockMetadata(x,y,z);
        int index; //3, 2, 1, 0, 5, 4
        int[] orientationLookUpVertical = new int[]{1, 0, 2, 3, 4, 5,  0, 1, 2, 3, 4, 5};
        if(meta == 0 || meta == 1){
            index = orientationLookUpVertical[6 * meta + side.getId()];
            if(tile.isBurning()){
                return SignalIndustries.textures.get("dilithiumStabilizer.vertical.active").getTexture(Side.getSideById(index));
            } else {
                return SignalIndustries.textures.get("dilithiumStabilizer.vertical").getTexture(Side.getSideById(index));
            }
        } else {
            index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        }
        if(tile.isBurning()){
            return SignalIndustries.textures.get("dilithiumStabilizer.active").getTexture(Side.getSideById(index));
        }
        return this.atlasIndices[index];
    }

    @Override
    public int getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntityStabilizer tile = (TileEntityStabilizer) blockAccess.getBlockTileEntity(x,y,z);
        if(tile.preview != IOPreview.NONE){
            Direction dir = Direction.getDirectionFromSide(side);
            Connection con = Connection.NONE;
            switch (tile.preview){
                case ITEM: {
                    con = tile.itemConnections.get(dir);
                    break;
                }
                case FLUID: {
                    con = tile.fluidConnections.get(dir);
                    break;
                }
            }
            switch (con){
                case INPUT:
                    return TextureHelper.getOrCreateBlockTextureIndex(SignalIndustries.MOD_ID,"input_overlay.png");
                case OUTPUT:
                    return TextureHelper.getOrCreateBlockTextureIndex(SignalIndustries.MOD_ID,"output_overlay.png");
                case BOTH:
                    return TextureHelper.getOrCreateBlockTextureIndex(SignalIndustries.MOD_ID,"both_io_overlay.png");
                case NONE:
                    return -1;
            }
        } else {
            int meta = blockAccess.getBlockMetadata(x, y, z);
            int index; //3, 2, 1, 0, 5, 4
            int[] orientationLookUpVertical = new int[]{1, 0, 2, 3, 4, 5,  0, 1, 2, 3, 4, 5};
            if (meta == 0 || meta == 1) {
                index = orientationLookUpVertical[6 * meta + side];
                if (tile.isBurning()) {
                    return SignalIndustries.textures.get("dilithiumStabilizer.vertical.active.overlay").getTexture(Side.getSideById(index));
                } else {
                    return -1;
                }
            } else {
                index = Sides.orientationLookUpHorizontal[6 * meta + side];
            }
            if (tile.isBurning()) {
                return SignalIndustries.textures.get("dilithiumStabilizer.active.overlay").getTexture(Side.getSideById(index));
            }
            return -1;
        }
        return -1;
    }*/
}
