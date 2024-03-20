package sunsetsatellite.signalindustries.blocks;


import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.inventories.TileEntityConduit;
import sunsetsatellite.signalindustries.inventories.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.PipeMode;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockItemConduit extends BlockContainerTiered {

    public BlockItemConduit(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    protected TileEntity getNewBlockEntity() {
        return new TileEntityItemConduit();
    }

    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer) {
        if (entityplayer.isSneaking() && !world.isClientSide) {
            TileEntityItemConduit tile = (TileEntityItemConduit) world.getBlockTileEntity(i,j,k);
            tile.mode = PipeMode.values()[tile.mode.ordinal()+1 <= PipeMode.values().length-1 ? tile.mode.ordinal()+1 : 0];
            Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Pipe mode changed to: "+tile.mode);
        }
        return false;
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityItemConduit tile = (TileEntityItemConduit) world.getBlockTileEntity(i,j,k);
        List<ItemStack> stacks = tile.getContents().stream().map(TileEntityItemConduit.PipeItem::getStack).collect(Collectors.toList());
        for (ItemStack itemstack : stacks) {
            if (itemstack != null) {
                Random random = new Random();
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
        super.onBlockRemoved(world, i, j, k, data);
    }

    public boolean renderAsNormalBlock() {
        return false;
    }
}
