package sunsetsatellite.signalindustries.items.applications.base;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseAbility;
import sunsetsatellite.signalindustries.interfaces.IApplicationItem;
import sunsetsatellite.signalindustries.util.ApplicationType;
import sunsetsatellite.signalindustries.util.Tier;


public class ItemWithAbility extends Item implements IApplicationItem<SuitBaseAbility>, ICustomDescription {

    SuitBaseAbility ability;

    public ItemWithAbility(String name, int id, SuitBaseAbility ability) {
        super(name, id);
        this.ability = ability;
    }

    @Override
    public ApplicationType getType() {
        return ApplicationType.ABILITY;
    }

    @Override
    public SuitBaseAbility getApplication() {
        return ability;
    }

    @Override
    public String getDescription(ItemStack stack) {
        I18n t = I18n.getInstance();
        return "Tier: " + ability.tier.getTextColor() + ability.tier.getRank() + "\n" +
                TextFormatting.YELLOW + "Ability Application" + TextFormatting.WHITE + "\n" +
                TextFormatting.LIGHT_GRAY + t.translateKey(ability.desc) + TextFormatting.WHITE + "\n" +
                "Cost: " + TextFormatting.RED + ability.cost + TextFormatting.WHITE;
    }

    @Override
    public Tier getTier() {
        return ability.tier;
    }
}
