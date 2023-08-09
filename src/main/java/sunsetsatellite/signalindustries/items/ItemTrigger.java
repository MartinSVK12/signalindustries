package sunsetsatellite.signalindustries.items;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.abilities.trigger.BoostAbility;
import sunsetsatellite.signalindustries.abilities.trigger.ProjectileAbility;
import sunsetsatellite.signalindustries.abilities.trigger.TriggerBaseAbility;
import sunsetsatellite.sunsetutils.util.ICustomDescription;

import java.util.HashMap;

public class ItemTrigger extends Item implements ICustomDescription {

    public static final HashMap<String, TriggerBaseAbility> abilities = new HashMap<>();
    public ItemTrigger(int i) {
        super(i);
        abilities.put("Projectile",new ProjectileAbility("Projectlie",50,1));
        abilities.put("Boost",new BoostAbility("Boost",150,5));
    }


    @Override
    public String getDescription(ItemStack stack) {
        if(getAbility(stack) != null){
            return "Ability: "+ TextFormatting.RED+getAbility(stack).name+TextFormatting.WHITE+" | Cost: "+TextFormatting.RED+getAbility(stack).cost+TextFormatting.WHITE+" | Cooldown: "+TextFormatting.RED+getAbility(stack).cooldown;
        }
        return "Unconfigured!";
    }

    public TriggerBaseAbility getAbility(ItemStack stack){
        if(stack.tag.containsKey("ability") && abilities.containsKey(stack.tag.getString("ability"))){
            return abilities.get(stack.tag.getString("ability"));
        }
        return null;
    }
    public String getAbilityName(ItemStack stack){
        return stack.tag.getString("ability");
    }


    @Override
    public String getLanguageKey(ItemStack stack) {
        if(getAbility(stack) != null){
            return "item.signalindustries.trigger"+stack.tag.getString("ability");
        } else {
            return "item.signalindustries.triggerNull";
        }
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if(getAbility(itemstack) != null) {
            if (entityplayer.inventory.armorItemInSlot(2) != null && entityplayer.inventory.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness) {
                ItemStack harness = entityplayer.inventory.armorItemInSlot(2);
                if (harness.tag.getInteger("cooldown" + getAbilityName(itemstack)) <= 0) {
                    CompoundTag energy = ((ItemSignalumPrototypeHarness) harness.getItem()).getFluidStack(0, harness);
                    int amount = energy.getInteger("amount");
                    if (amount >= getAbility(itemstack).cost) {
                        getAbility(itemstack).activate(blockX, blockY, blockZ, entityplayer, world, itemstack);
                        energy.putInt("amount", amount - getAbility(itemstack).cost);
                        harness.tag.putInt("cooldown" + getAbilityName(itemstack), getAbility(itemstack).cooldown);
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
                if(harness.tag.getInteger("cooldown"+getAbilityName(itemstack)) <= 0){
                    CompoundTag energy = ((ItemSignalumPrototypeHarness)harness.getItem()).getFluidStack(0,harness);
                    int amount = energy.getInteger("amount");
                    if(amount >= getAbility(itemstack).cost){
                        getAbility(itemstack).activate(entityplayer,world,itemstack);
                        energy.putInt("amount",amount-getAbility(itemstack).cost);
                        harness.tag.putInt("cooldown"+getAbilityName(itemstack),getAbility(itemstack).cooldown);
                    }
                }
            }
        }
        return super.onItemRightClick(itemstack, world, entityplayer);
    }
}
