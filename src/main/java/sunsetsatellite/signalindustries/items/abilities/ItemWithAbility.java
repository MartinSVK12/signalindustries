package sunsetsatellite.signalindustries.items.abilities;


import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseAbility;
import sunsetsatellite.signalindustries.interfaces.IHasAbility;
import sunsetsatellite.sunsetutils.util.ICustomDescription;

public class ItemWithAbility extends Item implements IHasAbility, ICustomDescription {

    SuitBaseAbility ability;

    public ItemWithAbility(int i, SuitBaseAbility ability) {
        super(i);
        this.ability = ability;
    }

    @Override
    public SuitBaseAbility getAbility() {
        return ability;
    }

    @Override
    public String getDescription(ItemStack stack) {
        I18n t = I18n.getInstance();
        StringBuilder s = new StringBuilder(TextFormatting.YELLOW + "Ability Container" + TextFormatting.WHITE).append("\n");
        s.append("Name: ").append(TextFormatting.LIGHT_GRAY).append(t.translateKey(ability.name)).append(TextFormatting.WHITE).append("\n");
        s.append("Mode: ").append(ability.mode.getChatColor()).append(ability.mode.getName()).append(TextFormatting.WHITE).append("\n");
        s.append("Description: ").append(TextFormatting.LIGHT_GRAY).append(t.translateKey(ability.desc)).append(TextFormatting.WHITE).append("\n");
        s.append("Cost: ").append(TextFormatting.RED).append(ability.cost).append(TextFormatting.WHITE);
        return s.toString();
    }
}
