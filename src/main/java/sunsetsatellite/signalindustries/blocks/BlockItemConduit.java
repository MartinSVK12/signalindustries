package sunsetsatellite.signalindustries.blocks;


import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.catalyst.CatalystMultipart;
import sunsetsatellite.catalyst.core.util.ConduitCapability;
import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
import sunsetsatellite.catalyst.multipart.api.Multipart;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockConduitBase;
import sunsetsatellite.signalindustries.gui.GuiRestrictPipeConfig;
import sunsetsatellite.signalindustries.gui.GuiSensorPipeConfig;
import sunsetsatellite.signalindustries.inventories.TileEntityItemConduit;
import sunsetsatellite.signalindustries.util.PipeMode;
import sunsetsatellite.signalindustries.util.PipeType;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockItemConduit extends BlockConduitBase {

    public PipeType type;

    public BlockItemConduit(String key, int i, Tier tier, Material material, PipeType type) {
        super(key, i, tier, material);
        this.type = type;
    }

    protected TileEntity getNewBlockEntity() {
        return new TileEntityItemConduit();
    }

    public boolean isSolidRender() {
        return false;
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (entityplayer.isSneaking() && type == PipeType.NORMAL && !world.isClientSide) {
            TileEntityItemConduit tile = (TileEntityItemConduit) world.getBlockTileEntity(i, j, k);
            tile.mode = PipeMode.values()[tile.mode.ordinal() + 1 <= PipeMode.values().length - 1 ? tile.mode.ordinal() + 1 : 0];
            Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Pipe mode changed to: " + tile.mode);
            return true;
        }
        if (!world.isClientSide && type == PipeType.RESTRICT) {
            TileEntityItemConduit tile = (TileEntityItemConduit) world.getBlockTileEntity(i, j, k);
            SignalIndustries.displayGui(entityplayer, () -> new GuiRestrictPipeConfig(entityplayer, tile, null), tile, tile.x, tile.y, tile.z);
            return true;
        }
        if (!world.isClientSide && type == PipeType.SENSOR) {
            TileEntityItemConduit tile = (TileEntityItemConduit) world.getBlockTileEntity(i, j, k);
            SignalIndustries.displayGui(entityplayer, () -> new GuiSensorPipeConfig(entityplayer.inventory, tile), tile, tile.x, tile.y, tile.z);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockRemoved(World world, int i, int j, int k, int data) {
        TileEntityItemConduit tile = (TileEntityItemConduit) world.getBlockTileEntity(i, j, k);
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

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public boolean isPoweringTo(WorldSource worldSource, int x, int y, int z, int side) {
        TileEntityItemConduit tile = (TileEntityItemConduit) worldSource.getBlockTileEntity(x, y, z);
        return tile != null && tile.type == PipeType.SENSOR && tile.sensorActive;
    }

    @Override
    public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
        TileEntityItemConduit tile = (TileEntityItemConduit) world.getBlockTileEntity(x, y, z);
        return tile != null && tile.type == PipeType.SENSOR && tile.sensorActive;
    }

    @Override
    public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
        ItemStack[] breakResult = super.getBreakResult(world, dropCause, x, y, z, meta, tileEntity);
        if(tileEntity instanceof ISupportsMultiparts){
            List<ItemStack> list = new ArrayList<>();
            for (Multipart multipart : ((ISupportsMultiparts) tileEntity).getParts().values()) {
                if(multipart == null) continue;
                ItemStack stack = new ItemStack(CatalystMultipart.multipartItem,1, 0);
                CompoundTag tag = new CompoundTag();
                CompoundTag multipartTag = new CompoundTag();
                multipartTag.putString("Type",multipart.type.name);
                multipartTag.putInt("Block", multipart.block.id);
                multipartTag.putInt("Meta", multipart.meta);
                multipartTag.putInt("Side", multipart.side.getId());
                tag.putCompound("Multipart",multipartTag);
                stack.setData(tag);
                list.add(stack);
            }
            if(breakResult != null) list.add(breakResult[0]);
            return list.toArray(new ItemStack[0]);
        }
        return breakResult;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public ConduitCapability getConduitCapability() {
        return ConduitCapability.ITEM;
    }
}
