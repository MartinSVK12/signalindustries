package sunsetsatellite.signalindustries.items.models;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.SignalIndustries;

public class ItemModelPulsar extends ItemModelStandard {

    public IconCoordinate pulsarInactive = TextureRegistry.getTexture("signalindustries:item/pulsarinactive");
    public IconCoordinate pulsarActive = TextureRegistry.getTexture("signalindustries:item/pulsaractive");
    public IconCoordinate pulsarCharged = TextureRegistry.getTexture("signalindustries:item/pulsarcharged");
    public IconCoordinate pulsarWarpActive = TextureRegistry.getTexture("signalindustries:item/pulsarwarpactive");
    public IconCoordinate pulsarWarpCharged = TextureRegistry.getTexture("signalindustries:item/pulsarwarpcharged");

    public ItemModelPulsar(Item item, String namespace) {
        super(item, namespace);
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemstack) {
        if(getFluidStack(0,itemstack).getInteger("amount") <= 0 && itemstack.getData().getByte("charge") <= 0){
            return pulsarInactive;
        }
        IconCoordinate tex = pulsarActive;
        if(itemstack.getData().getByte("charge") >= 100){
            tex = pulsarCharged;
        }
        if(getItemIdFromSlot(0,itemstack) == SignalIndustries.warpOrb.id){
            tex = pulsarWarpActive;
            if(itemstack.getData().getByte("charge") >= 100){
                tex = pulsarWarpCharged;
            }
        }
        return tex;
    }

    public int getItemIdFromSlot(int id, ItemStack stack){
        return stack.getData().getCompound("inventory").getCompound(String.valueOf(id)).getShort("id");
    }

    public CompoundTag getFluidStack(int id, ItemStack stack){
        return stack.getData().getCompound("fluidInventory").getCompound(String.valueOf(id));
    }
}
