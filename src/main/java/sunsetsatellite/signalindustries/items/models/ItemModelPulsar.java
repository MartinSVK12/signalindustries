package sunsetsatellite.signalindustries.items.models;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.render.ItemRenderer;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIItems;

public class ItemModelPulsar extends ItemModelStandard {

    public IconCoordinate pulsarInactive = TextureRegistry.getTexture("signalindustries:item/pulsar_inactive");
    public IconCoordinate pulsarActive = TextureRegistry.getTexture("signalindustries:item/pulsar_active");
    public IconCoordinate pulsarCharged = TextureRegistry.getTexture("signalindustries:item/pulsar_charged");
    public IconCoordinate pulsarWarpActive = TextureRegistry.getTexture("signalindustries:item/pulsar_warp_active");
    public IconCoordinate pulsarWarpCharged = TextureRegistry.getTexture("signalindustries:item/pulsar_warp_charged");

    public ItemModelPulsar(Item item) {
        super(item);
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, @NonNull ItemStack itemstack) {
        if (getFluidStack(0, itemstack).getInteger("amount") <= 0 && itemstack.getData().getByte("charge") <= 0) {
            return pulsarInactive;
        }
        IconCoordinate tex = pulsarActive;
        if (itemstack.getData().getByte("charge") >= 100) {
            tex = pulsarCharged;
        }
        if (getItemIdFromSlot(0, itemstack) == SIItems.warpOrb.id) {
            tex = pulsarWarpActive;
            if (itemstack.getData().getByte("charge") >= 100) {
                tex = pulsarWarpCharged;
            }
        }
        return tex;
    }

    public int getItemIdFromSlot(int id, ItemStack stack) {
        return stack.getData().getCompound("inventory").getCompound(String.valueOf(id)).getShort("id");
    }

    public CompoundTag getFluidStack(int id, ItemStack stack) {
        return stack.getData().getCompound("fluidInventory").getCompound(String.valueOf(id));
    }
}
