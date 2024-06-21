package sunsetsatellite.signalindustries.items.models;

import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.util.ConfigurationTabletMode;

public class ItemModelConfigurationTablet extends ItemModelStandard {

    public IconCoordinate rotation = TextureRegistry.getTexture("signalindustries:item/configuration_tablet_rotation");
    public IconCoordinate item = TextureRegistry.getTexture("signalindustries:item/configuration_tablet_item");
    public IconCoordinate fluid = TextureRegistry.getTexture("signalindustries:item/configuration_tablet_fluid");
    public IconCoordinate pipeDisconnect = TextureRegistry.getTexture("signalindustries:item/configuration_tablet_pipe_disconnect");
    public IconCoordinate config = TextureRegistry.getTexture("signalindustries:item/configuration_tablet_configurator");



    public ItemModelConfigurationTablet(Item item, String namespace) {
        super(item, namespace);
    }

    @Override
    public @NotNull IconCoordinate getIcon(@Nullable Entity entity, ItemStack itemStack) {
        ConfigurationTabletMode mode = ConfigurationTabletMode.values()[itemStack.getData().getInteger("mode")];
        switch (mode){
            case ROTATION:
                return rotation;
            case ITEM:
                return item;
            case FLUID:
                return fluid;
            case DISCONNECTOR:
                return pipeDisconnect;
            case CONFIGURATOR:
                return config;
        }
        return super.getIcon(entity, itemStack);
    }
}
