package sunsetsatellite.signalindustries.items.applications.base;

import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.interfaces.IApplicationItem;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.ItemTiered;
import sunsetsatellite.signalindustries.util.ApplicationType;
import sunsetsatellite.signalindustries.util.Tier;

public abstract class ItemWithUtility extends ItemTiered implements IApplicationItem<ItemWithUtility> {

    public ItemWithUtility(String translationKey, String namespaceId, int id, Tier tier) {
        super(translationKey, namespaceId, id, tier);
    }

    @Override
    public ApplicationType getType() {
        return ApplicationType.UTILITY;
    }

    @Override
    public ItemWithUtility getApplication() {
        return this;
    }

    @Override
    public String getDescription(ItemStack stack) {
        String s = super.getDescription(stack);
        return s + "\n" + TextFormatting.YELLOW + "Application" + TextFormatting.WHITE;
    }

    public abstract void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world);
}
