package sunsetsatellite.signalindustries.items.abilities;




import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseAbility;
import sunsetsatellite.signalindustries.interfaces.ICustomDescription;
import sunsetsatellite.signalindustries.interfaces.IHasAbility;
import sunsetsatellite.signalindustries.util.AttachmentPoint;

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
        StringBuilder s = new StringBuilder(ChatColor.yellow + "Ability Container" + ChatColor.white).append("\n");
        s.append("Name: ").append(ChatColor.lightGray).append(ability.name).append(ChatColor.white).append("\n");
        s.append("Mode: ").append(ability.mode.getChatColor()).append(ability.mode.getName()).append(ChatColor.white).append("\n");
        s.append("Description: ").append(ChatColor.lightGray).append(ability.desc).append(ChatColor.white).append("\n");
        s.append("Cost: ").append(ChatColor.red).append(ability.cost).append(ChatColor.white);
        return s.toString();
    }
}
