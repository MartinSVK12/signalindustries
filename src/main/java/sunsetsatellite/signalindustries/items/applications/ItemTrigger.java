package sunsetsatellite.signalindustries.items.applications;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.abilities.trigger.*;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;
import sunsetsatellite.signalindustries.items.ItemSignalumPrototypeHarness;

import java.util.HashMap;

public class ItemTrigger extends Item implements ICustomDescription {

    public static final HashMap<String, TriggerBaseAbility> abilities = new HashMap<>();

    public ItemTrigger(String name, int id) {
        super(name, id);
        abilities.put("projectile",new ProjectileAbility("Projectlie",50,1));
        abilities.put("boost",new BoostAbility("Boost",150,5));
        abilities.put("shield",new ShieldAbility("Shield",300,15,10,5));
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
    public boolean onUseItemOnBlock(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        TriggerBaseAbility ability = getAbility(itemstack);
        if(ability != null) {
            if (entityplayer.inventory.armorItemInSlot(2) != null && entityplayer.inventory.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness) {
                ItemStack harness = entityplayer.inventory.armorItemInSlot(2);
                if (harness.getData().getInteger("cooldown_" + getAbilityName(itemstack)) <= 0) {
                    CompoundTag energy = ((ItemSignalumPrototypeHarness) harness.getItem()).getFluidStack(0, harness);
                    int amount = energy.getInteger("amount");
                    if (amount >= ability.cost) {
                        entityplayer.triggerAchievement(SIAchievements.TRIGGER);
                        if(ability instanceof TriggerBaseEffectAbility){
                            boolean active = harness.getData().getBoolean("active_" + getAbilityName(itemstack));
                            if(active) {
                                harness.getData().putBoolean("active_" + getAbilityName(itemstack),false);
                                ((INBTCompound) harness.getData()).removeTag("effectTime_" + getAbilityName(itemstack));
                                harness.getData().putInt("cooldown_" + getAbilityName(itemstack), ability.cooldown);
                                ((TriggerBaseEffectAbility) ability).deactivate(blockX, blockY, blockZ, entityplayer, world, itemstack, harness);
                            } else {
                                harness.getData().putBoolean("active_" + getAbilityName(itemstack),true);
                                harness.getData().putInt("effectTime_" + getAbilityName(itemstack),((TriggerBaseEffectAbility) ability).effectTime);
                                energy.putInt("amount", amount - ability.cost);
                                ability.activate(blockX, blockY, blockZ, entityplayer, world, itemstack, harness);
                            }
                        } else {
                            energy.putInt("amount", amount - ability.cost);
                            harness.getData().putInt("cooldown_" + getAbilityName(itemstack), ability.cooldown);
                            ability.activate(blockX, blockY, blockZ, entityplayer, world, itemstack, harness);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        super.inventoryTick(itemstack, world, entity, i, flag);
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            if (player.inventory.armorItemInSlot(2) != null && player.inventory.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness) {
                ItemStack harness = player.inventory.armorItemInSlot(2);
                boolean active = harness.getData().getBoolean("active_" + getAbilityName(itemstack));
                CompoundTag energy = ((ItemSignalumPrototypeHarness) harness.getItem()).getFluidStack(0, harness);
                int amount = energy.getInteger("amount");
                TriggerBaseAbility trigger = getAbility(itemstack);
                if(trigger instanceof TriggerBaseEffectAbility && active){
                    TriggerBaseEffectAbility ability = (TriggerBaseEffectAbility) trigger;
                    if(amount >= ability.costPerTick){
                        energy.putInt("amount", amount - ability.costPerTick);
                        ability.tick(player, world, itemstack, harness);
                    } else {
                        harness.getData().putBoolean("active_" + getAbilityName(itemstack),false);
                        ((INBTCompound) harness.getData()).removeTag("effectTime_" + getAbilityName(itemstack));
                        harness.getData().putInt("cooldown_" + getAbilityName(itemstack), ability.cooldown);
                        ability.deactivate(player, world, itemstack, harness);
                    }
                    if (harness.getData().getInteger("effectTime_" + getAbilityName(itemstack)) <= 0) {
                        harness.getData().putBoolean("active_" + getAbilityName(itemstack),false);
                        ((INBTCompound) harness.getData()).removeTag("effectTime_" + getAbilityName(itemstack));
                        harness.getData().putInt("cooldown_" + getAbilityName(itemstack), ability.cooldown);
                        ability.deactivate(player, world, itemstack, harness);
                    }
                }
            }
        }
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        TriggerBaseAbility ability = getAbility(itemstack);
        if(ability != null){
            if(entityplayer.inventory.armorItemInSlot(2) != null && entityplayer.inventory.armorItemInSlot(2).getItem() instanceof ItemSignalumPrototypeHarness){
                ItemStack harness = entityplayer.inventory.armorItemInSlot(2);
                if(harness.getData().getInteger("cooldown_"+getAbilityName(itemstack)) <= 0){
                    CompoundTag energy = ((ItemSignalumPrototypeHarness)harness.getItem()).getFluidStack(0,harness);
                    int amount = energy.getInteger("amount");
                    if(amount >= ability.cost){
                        entityplayer.triggerAchievement(SIAchievements.TRIGGER);
                        if(ability instanceof TriggerBaseEffectAbility){
                            boolean active = harness.getData().getBoolean("active_" + getAbilityName(itemstack));
                            if(active) {
                                harness.getData().putBoolean("active_" + getAbilityName(itemstack),false);
                                ((INBTCompound) harness.getData()).removeTag("effectTime_" + getAbilityName(itemstack));
                                harness.getData().putInt("cooldown_" + getAbilityName(itemstack), ability.cooldown);
                                ((TriggerBaseEffectAbility) ability).deactivate(entityplayer, world, itemstack, harness);
                            } else {
                                harness.getData().putBoolean("active_" + getAbilityName(itemstack),true);
                                harness.getData().putInt("effectTime_" + getAbilityName(itemstack),((TriggerBaseEffectAbility) ability).effectTime);
                                energy.putInt("amount", amount - ability.cost);
                                ability.activate(entityplayer, world, itemstack, harness);
                            }
                        } else {
                            energy.putInt("amount",amount- ability.cost);
                            harness.getData().putInt("cooldown_"+getAbilityName(itemstack), ability.cooldown);
                            ability.activate(entityplayer, world, itemstack,harness);
                        }
                    }
                }
            }
        }
        return super.onUseItem(itemstack, world, entityplayer);
    }
}
