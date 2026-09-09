package sunsetsatellite.signalindustries.items.models;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.collection.NamespaceID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.SignalIndustries;

public class ItemModelMagnet extends ItemModelTool {

    public IconCoordinate magnetDisabled = TextureRegistry.getTexture("signalindustries:item/magnet_inactive");
    public IconCoordinate magnetEnabled = TextureRegistry.getTexture("signalindustries:item/magnet_active");

    public ItemModelMagnet(Item item) {
        super(item, NamespaceID.fromPool(SignalIndustries.MOD_ID, "item/magnet_inactive"));
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
        if (itemStack.getData().getBoolean("active")) {
            return magnetEnabled;
        }
        return magnetDisabled;
    }
}
