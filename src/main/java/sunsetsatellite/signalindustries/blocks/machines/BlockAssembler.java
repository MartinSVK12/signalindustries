package sunsetsatellite.signalindustries.blocks.machines;

import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
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
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.containers.ContainerAssembler;
import sunsetsatellite.signalindustries.gui.GuiAssembler;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.inventories.machines.TileEntityAssembler;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.TextureHelper;

import java.util.ArrayList;
import java.util.Random;

public class BlockAssembler extends BlockContainerTiered {
    public BlockAssembler(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
        hasOverbright = true;
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityAssembler tile = (TileEntityAssembler) world.getBlockTileEntity(i, j, k);
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
            for (int l = 0; l < tile.template.getSizeInventory(); ++l) {
                ItemStack itemstack = tile.template.getStackInSlot(l);
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
    public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
        TileEntityTieredMachineBase tile = (TileEntityTieredMachineBase) blockAccess.getBlockTileEntity(x,y,z);
        int meta = blockAccess.getBlockMetadata(x,y,z);
        int index = Sides.orientationLookUpHorizontal[6 * meta + side.getId()];
        if(tile.isBurning() && tile.tier == tier){
            return SignalIndustries.textures.get(tile.tier.name()+".assembler.active").getTexture(Side.getSideById(index));
        }
        return this.atlasIndices[index];
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if(world.isClientSide)
        {
            return true;
        } else
        {
            TileEntityAssembler tile = (TileEntityAssembler) world.getBlockTileEntity(i, j, k);
            if(tile != null) {
                SignalIndustries.displayGui(entityplayer,new GuiAssembler(entityplayer.inventory, tile),new ContainerAssembler(entityplayer.inventory,tile),tile,i,j,k);
            }
            return true;
        }
    }

    @Override
    public int getBlockOverbrightTexture(WorldSource blockAccess, int x, int y, int z, int side) {
        TileEntityTieredMachineBase tile = (TileEntityTieredMachineBase) blockAccess.getBlockTileEntity(x,y,z);
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
            int meta = blockAccess.getBlockMetadata(x,y,z);
            int index = Sides.orientationLookUpHorizontal[6 * meta + side];
            if(tile.isBurning() && tile.tier == tier){
                return SignalIndustries.textures.get(tile.tier.name()+".assembler.active.overlay").getTexture(Side.getSideById(index));
            }
            return -1;
        }
        return -1;
    }

    @Override
    protected TileEntity getNewBlockEntity() {
        return new TileEntityAssembler();
    }
}
