package sunsetsatellite.signalindustries.api.impl.catalyst.multipart;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.ContainerPlayerCreative;
import sunsetsatellite.catalyst.CatalystMultipart;
import sunsetsatellite.catalyst.multipart.api.MultipartType;
import sunsetsatellite.signalindustries.util.IdMetaPair;

import java.util.HashMap;

public class SIMultipartIndexPlugin {

    public void add(HashMap<IdMetaPair, ItemStack> items) {
        final int[] i = {0};
        MultipartType.types.forEach((K, V)->{
            for (ItemStack item : ContainerPlayerCreative.creativeItems) {
                if (item == null) continue;
                if (item.itemID >= 16384) continue;
                if (!Block.getBlock(item.itemID).hasTag(CatalystMultipart.CAN_BE_MULTIPART)) continue;
                if (!Block.getBlock(item.itemID).hasTag(CatalystMultipart.TYPE_TAGS.get(K))) continue;
                ItemStack stack = new ItemStack(CatalystMultipart.multipartItem,1, 0);
                CompoundTag tag = new CompoundTag();
                CompoundTag multipartTag = new CompoundTag();
                multipartTag.putString("Type",K);
                multipartTag.putInt("Block", item.itemID);
                multipartTag.putInt("Meta", item.getMetadata());
                tag.putCompound("Multipart",multipartTag);
                stack.setData(tag);
                items.put(new IdMetaPair(CatalystMultipart.multipartItem.id,i[0]++), stack);
                i[0]++;
            }
        });
    }
}
