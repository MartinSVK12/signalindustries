package sunsetsatellite.signalindustries.items;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.abilities.trigger.BoostAbility;
import sunsetsatellite.signalindustries.abilities.trigger.ProjectileAbility;
import sunsetsatellite.signalindustries.abilities.trigger.TriggerBaseAbility;

import java.util.HashMap;

public class ItemTrigger extends Item implements ICustomDescription {

    public static final HashMap<String, TriggerBaseAbility> abilities = new HashMap<>();

    public ItemTrigger(String name, int id) {
        super(name, id);
        abilities.put("projectile",new ProjectileAbility("Projectlie",50,1));
        abilities.put("boost",new BoostAbility("Boost",150,5));
    }

    @Override
    public String getDescription(ItemStack stack) {
        if(getAbility(stack) != null){
            return "Ability: "+ TextFormatting.RED+getAbility(stack).name+TextFormatting.WHITE+" | Cost: "+TextFormatting.RED+getAbility(stack).cost+TextFormatting.WHITE+" | Cooldown: "+TextFormatting.RED+getAbility(stack).cooldown;
        }
        return "Unconfigured!";
    }

    public TriggerBaseAbility getAbility(ItemStack stack){
        if(stack.getData().containsKey("ability") && abilities.containsKey(stack.getData().getString("ability"))){
            return abilities.get(stack.getData().getString("ability"));
        }
        return null;
    }
    public String getAbilityName(ItemStack stack){
        return stack.getData().getString("ability");
    }


    @Override
    public String getLanguageKey(ItemStack stack) {
        if(getAbility(stack) != null){
            return "item.signalindustries.trigger."+stack.getData().getString("ability");
        } else {
            return "item.signalindustries.trigger.null";
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if(getAbility(itemstack) != null) {
            if (entityplayer.inventory.armorItemInSlot(2) != null && entityplayer.inventory.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness) {
                ItemStack harness = entityplayer.inventory.armorItemInSlot(2);
                if (harness.getData().getInteger("cooldown" + getAbilityName(itemstack)) <= 0) {
                    CompoundTag energy = ((ItemSignalumPrototypeHarness) harness.getItem()).getFluidStack(0, harness);
                    int amount = energy.getInteger("amount");
                    if (amount >= getAbility(itemstack).cost) {
                        getAbility(itemstack).activate(blockX, blockY, blockZ, entityplayer, world, itemstack);
                        entityplayer.triggerAchievement(SIAchievements.TRIGGER);
                        energy.putInt("amount", amount - getAbility(itemstack).cost);
                        harness.getData().putInt("cooldown" + getAbilityName(itemstack), getAbility(itemstack).cooldown);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(getAbility(itemstack) != null){
            if(entityplayer.inventory.armorItemInSlot(2) != null && entityplayer.inventory.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness){
                ItemStack harness = entityplayer.inventory.armorItemInSlot(2);
                if(harness.getData().getInteger("cooldown"+getAbilityName(itemstack)) <= 0){
                    CompoundTag energy = ((ItemSignalumPrototypeHarness)harness.getItem()).getFluidStack(0,harness);
                    int amount = energy.getInteger("amount");
                    if(amount >= getAbility(itemstack).cost){
                        getAbility(itemstack).activate(entityplayer,world,itemstack);
                        entityplayer.triggerAchievement(SIAchievements.TRIGGER);
                        energy.putInt("amount",amount-getAbility(itemstack).cost);
                        harness.getData().putInt("cooldown"+getAbilityName(itemstack),getAbility(itemstack).cooldown);
                    }
                }
            }
        }
        return super.onItemRightClick(itemstack, world, entityplayer);
    }
}
