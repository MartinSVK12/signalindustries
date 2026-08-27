package sunsetsatellite.signalindustries.items.applications;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.abilities.trigger.*;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;

import java.util.HashMap;

import static net.minecraft.core.enums.HumanArmorShape.CHEST;

public class ItemTrigger extends Item implements ICustomDescription {

    public static final HashMap<String, TriggerBaseAbility> abilities = new HashMap<>();

    public ItemTrigger(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
        abilities.put("projectile", new ProjectileAbility("Projectile", 50, 1));
        abilities.put("jump", new JumpAbility("Jump", 50, 5));
        abilities.put("shield", new ShieldAbility("Shield", 300, 15, 10, 5));
        abilities.put("scan", new ScanAbility("Scan", 150, 3, 15, 1));
    }

    @Override
    public String getDescription(ItemStack stack) {
        if (getAbility(stack) != null) {
            return "Ability: " + TextFormatting.RED + getAbility(stack).name + TextFormatting.WHITE + " | Cost: " + TextFormatting.RED + getAbility(stack).cost + TextFormatting.WHITE + " | Cooldown: " + TextFormatting.RED + getAbility(stack).cooldown;
        }
        return "Unconfigured!";
    }

    public TriggerBaseAbility getAbility(ItemStack stack) {
        if (stack.getData().containsKey("ability") && abilities.containsKey(stack.getData().getString("ability"))) {
            return abilities.get(stack.getData().getString("ability"));
        }
        return null;
    }

    public String getAbilityName(ItemStack stack) {
        return stack.getData().getString("ability");
    }


    @Override
    public @NonNull String getLanguageKey(@NonNull ItemStack stack) {
        if (getAbility(stack) != null) {
            return "item.signalindustries.trigger." + stack.getData().getString("ability");
        } else {
            return "item.signalindustries.trigger.null";
        }
    }

	@Override
	public boolean onUseOnBlock(@NotNull ItemStack stack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		TriggerBaseAbility ability = getAbility(stack);
		if (ability != null) {
			if (player.inventory.armorItemInSlot(CHEST) != null && player.inventory.armorItemInSlot(CHEST).getItem() instanceof ItemSignalumPowerHarness) {
				ItemStack harness = player.inventory.armorItemInSlot(CHEST);
				if (harness.getData().getInteger("cooldown_" + getAbilityName(stack)) <= 0) {
					CompoundTag energy = ((ItemSignalumPowerHarness) harness.getItem()).getFluidStack(0, harness);
					int amount = energy.getInteger("amount");
					if (amount >= ability.cost) {
						player.triggerAchievement(SIAchievements.TRIGGER);
						if (ability instanceof TriggerBaseEffectAbility) {
							boolean active = harness.getData().getBoolean("active_" + getAbilityName(stack));
							if (active) {
								harness.getData().putBoolean("active_" + getAbilityName(stack), false);
								harness.getData().getValue().remove("effectTime_" + getAbilityName(stack));
								harness.getData().putInt("cooldown_" + getAbilityName(stack), ability.cooldown);
								((TriggerBaseEffectAbility) ability).deactivate(blockPos, player, world, stack, harness);
							} else {
								harness.getData().putBoolean("active_" + getAbilityName(stack), true);
								harness.getData().putInt("effectTime_" + getAbilityName(stack), ((TriggerBaseEffectAbility) ability).effectTime);
								energy.putInt("amount", amount - ability.cost);
								ability.activate(blockPos, player, world, stack, harness);
							}
						} else {
							energy.putInt("amount", amount - ability.cost);
							harness.getData().putInt("cooldown_" + getAbilityName(stack), ability.cooldown);
							ability.activate(blockPos, player, world, stack, harness);
						}
					}
				}
			}
		}
		return true;
	}

    @Override
    public void inventoryTick(@NonNull ItemStack itemstack, @NonNull World world, @NonNull Entity entity, int i, boolean flag) {
        super.inventoryTick(itemstack, world, entity, i, flag);
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.inventory.armorItemInSlot(CHEST) != null && player.inventory.armorItemInSlot(CHEST).getItem() instanceof ItemSignalumPowerHarness) {
                ItemStack harness = player.inventory.armorItemInSlot(CHEST);
                boolean active = harness.getData().getBoolean("active_" + getAbilityName(itemstack));
                CompoundTag energy = ((ItemSignalumPowerHarness) harness.getItem()).getFluidStack(0, harness);
                int amount = energy.getInteger("amount");
                TriggerBaseAbility trigger = getAbility(itemstack);
                if (trigger instanceof TriggerBaseEffectAbility ability && active) {
					if (amount >= ability.costPerTick) {
                        energy.putInt("amount", amount - ability.costPerTick);
                        ability.tick(player, world, itemstack, harness);
                    } else {
                        harness.getData().putBoolean("active_" + getAbilityName(itemstack), false);
                        harness.getData().getValue().remove("effectTime_" + getAbilityName(itemstack));
                        harness.getData().putInt("cooldown_" + getAbilityName(itemstack), ability.cooldown);
                        ability.deactivate(player, world, itemstack, harness);
                    }
                    if (harness.getData().getInteger("effectTime_" + getAbilityName(itemstack)) <= 0) {
                        harness.getData().putBoolean("active_" + getAbilityName(itemstack), false);
                        harness.getData().getValue().remove("effectTime_" + getAbilityName(itemstack));
                        harness.getData().putInt("cooldown_" + getAbilityName(itemstack), ability.cooldown);
                        ability.deactivate(player, world, itemstack, harness);
                    }
                }
            }
        }
    }

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
		TriggerBaseAbility ability = getAbility(stack);
		if (ability != null) {
			if (player.inventory.armorItemInSlot(CHEST) != null && player.inventory.armorItemInSlot(CHEST).getItem() instanceof ItemSignalumPowerHarness) {
				ItemStack harness = player.inventory.armorItemInSlot(CHEST);
				if (harness.getData().getInteger("cooldown_" + getAbilityName(stack)) <= 0) {
					CompoundTag energy = ((ItemSignalumPowerHarness) harness.getItem()).getFluidStack(0, harness);
					int amount = energy.getInteger("amount");
					if (amount >= ability.cost) {
						player.triggerAchievement(SIAchievements.TRIGGER);
						if (ability instanceof TriggerBaseEffectAbility) {
							boolean active = harness.getData().getBoolean("active_" + getAbilityName(stack));
							if (active) {
								harness.getData().putBoolean("active_" + getAbilityName(stack), false);
								harness.getData().getValue().remove("effectTime_" + getAbilityName(stack));
								harness.getData().putInt("cooldown_" + getAbilityName(stack), ability.cooldown);
								((TriggerBaseEffectAbility) ability).deactivate(player, world, stack, harness);
							} else {
								harness.getData().putBoolean("active_" + getAbilityName(stack), true);
								harness.getData().putInt("effectTime_" + getAbilityName(stack), ((TriggerBaseEffectAbility) ability).effectTime);
								energy.putInt("amount", amount - ability.cost);
								ability.activate(player, world, stack, harness);
							}
						} else {
							energy.putInt("amount", amount - ability.cost);
							harness.getData().putInt("cooldown_" + getAbilityName(stack), ability.cooldown);
							ability.activate(player, world, stack, harness);
						}
					}
				}
			}
		}
		return super.onUse(stack, world, player);
	}
}
