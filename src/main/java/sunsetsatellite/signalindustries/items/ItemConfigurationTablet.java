package sunsetsatellite.signalindustries.items;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.world.World;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.IWrench;
import sunsetsatellite.catalyst.core.util.section.ISideInteractable;
import sunsetsatellite.signalindustries.util.ConfigurationTabletMode;

public class ItemConfigurationTablet extends Item implements IWrench, ISideInteractable {

    public ItemConfigurationTablet(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public @NonNull CompoundTag getDefaultTag() {
        CompoundTag data = new CompoundTag();
        data.putInt("mode", 0);
        return data;
    }

    @Override
    public @NonNull String getLanguageKey(ItemStack itemstack) {
        ConfigurationTabletMode mode = ConfigurationTabletMode.values()[itemstack.getData().getInteger("mode")];
		return switch (mode) {
		    case ROTATION -> "item.signalindustries.configurationTablet.rotation";
		    case ITEM -> "item.signalindustries.configurationTablet.item";
		    case FLUID -> "item.signalindustries.configurationTablet.fluid";
		    case DISCONNECTOR -> "item.signalindustries.configurationTablet.disconnect";
		    case CONFIGURATOR -> "item.signalindustries.configurationTablet.config";
			case COPY_PASTE -> "item.signalindustries.configurationTablet.copyPaste";
		};
	}

    @Override
    public ItemStack onUse(@NonNull ItemStack itemstack, @NonNull World world, Player entityplayer) {
        if (entityplayer.isSneaking()) {
            int mode = itemstack.getData().getInteger("mode");
            mode = (mode + 1) % ConfigurationTabletMode.values().length;
            itemstack.getData().putInt("mode", mode);
            entityplayer.sendStatusMessage(itemstack.getDisplayName());
        } else {
			ConfigurationTabletMode mode = ConfigurationTabletMode.values()[itemstack.getData().getInteger("mode")];
			if(mode == ConfigurationTabletMode.COPY_PASTE){
				itemstack.getData().getValue().remove("CopyPaste");
				entityplayer.sendStatusMessage(I18n.getInstance().translateKey("event.signalindustries.clearedCopyPaste"));
			}
		}

        return super.onUse(itemstack, world, entityplayer);
    }
}
