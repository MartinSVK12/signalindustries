package sunsetsatellite.signalindustries.items;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.IWrench;
import sunsetsatellite.catalyst.core.util.section.ISideInteractable;
import sunsetsatellite.signalindustries.util.ConfigurationTabletMode;

public class ItemConfigurationTablet extends Item implements IWrench, ISideInteractable {
    
    public ItemConfigurationTablet(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public CompoundTag getDefaultTag() {
        CompoundTag data = new CompoundTag();
        data.putInt("mode",0);
        return data;
    }

    @Override
    public String getLanguageKey(ItemStack itemstack) {
        ConfigurationTabletMode mode = ConfigurationTabletMode.values()[itemstack.getData().getInteger("mode")];
        switch (mode){
            case ROTATION:
                return "item.signalindustries.configurationTablet.rotation";
            case ITEM:
                return "item.signalindustries.configurationTablet.item";
            case FLUID:
                return "item.signalindustries.configurationTablet.fluid";
            case DISCONNECTOR:
                return "item.signalindustries.configurationTablet.disconnect";
            case CONFIGURATOR:
                return "item.signalindustries.configurationTablet.config";
        }
        return super.getLanguageKey(itemstack);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
        if(entityplayer.isSneaking()){
            int mode = itemstack.getData().getInteger("mode");
            mode = (mode + 1) % ConfigurationTabletMode.values().length;
            itemstack.getData().putInt("mode", mode);
            entityplayer.sendStatusMessage(itemstack.getDisplayName());
        }
        return super.onUseItem(itemstack, world, entityplayer);
    }
}
