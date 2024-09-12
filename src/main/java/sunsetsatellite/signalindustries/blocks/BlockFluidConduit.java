package sunsetsatellite.signalindustries.blocks;


import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.CatalystMultipart;
import sunsetsatellite.catalyst.core.util.ConduitCapability;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.catalyst.multipart.api.ISupportsMultiparts;
import sunsetsatellite.catalyst.multipart.api.Multipart;
import sunsetsatellite.signalindustries.blocks.base.BlockConduitBase;
import sunsetsatellite.signalindustries.inventories.TileEntityFluidConduit;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.List;

public class BlockFluidConduit extends BlockConduitBase {

    public BlockFluidConduit(String key, int i, Tier tier, Material material) {
        super(key, i, tier, material);
    }

    protected TileEntity getNewBlockEntity() {
        return new TileEntityFluidConduit();
    }

    public boolean isSolidRender() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, EntityPlayer entityplayer, Side side, double xHit, double yHit) {
        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }
        if (entityplayer.isSneaking() && !world.isClientSide) {
            TileEntityFluidPipe tile = (TileEntityFluidPipe) world.getBlockTileEntity(i, j, k);
            if (tile.getFluidInSlot(0) != null && tile.getFluidInSlot(0).getLiquid() != null) {
                Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Liquid: " + tile.getFluidInSlot(0).toString());
            } else {
                Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Liquid: Empty");
            }
            Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Capacity: " + tile.fluidCapacity[0]);
            return false;
        }
        return false;
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
    public ConduitCapability getConduitCapability() {
        return ConduitCapability.FLUID;
    }
}
