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

public class ItemModelSaber extends ItemModelTool {

    public IconCoordinate saberDisabled = TextureRegistry.getTexture("signalindustries:item/signalum_saber_unpowered");
    public IconCoordinate saberEnabled = TextureRegistry.getTexture("signalindustries:item/signalum_saber");

    public ItemModelSaber(Item item) {
        super(item, NamespaceID.fromPool(SignalIndustries.MOD_ID, "item/signalum_saber_unpowered"));
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
        if (itemStack.getData().getBoolean("active")) {
            return saberEnabled;
        }
        return saberDisabled;
    }
}
