package sunsetsatellite.signalindustries.items.tools;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.primitives.AABBd;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.entities.EntityShockwave;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IInjectable;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.interfaces.mixins.IWarpPlayer;
import sunsetsatellite.signalindustries.invs.InventoryPulsar;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;
import sunsetsatellite.signalindustries.items.base.ItemTiered;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.Tier;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.List;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class ItemPulsar extends ItemTiered implements IHasOverlay, IInjectable {
    public ItemPulsar(String translationKey, String namespaceId, int id, Tier tier) {
        super(translationKey, namespaceId, id, tier);
    }

	@Override
	public boolean onUseOnBlock(@NotNull ItemStack selfStack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		if (world.isClientSide) {
			return true;
		} else {
			if (player.isSneaking() && !selfStack.getData().getBoolean("charging")) {
				Catalyst.displayGui(player, new InventoryPulsar(selfStack), player.inventory.getCurrentSlot(), false, key("gui/pulsar"));
				return true;
			}
			return false;
		}
	}

    @Override
    public ItemStack onUse(ItemStack itemstack, @NonNull World world, @NonNull Player player) {
        if (!itemstack.getData().getBoolean("charging") && itemstack.getData().getByte("charge") < 100 && !player.isSneaking() && getFluidStack(0, itemstack).getInteger("amount") > 0) {
            itemstack.getData().putBoolean("charging", true);
            return itemstack;
        }
        if (itemstack.getData().getByte("charge") >= 100) {
            itemstack.getData().putByte("charge", (byte) 0);
            if (getAbility(itemstack).contains("Warp")) {
                CompoundTag data = getItemFromSlot(0, itemstack).getCompound("Data");
                CompoundTag warpPosition = data.getCompound("position");
                if (warpPosition.containsKey("x") && warpPosition.containsKey("y") && warpPosition.containsKey("z")) {
					player.triggerAchievement(SIAchievements.TELEPORT_SUCCESS);
                    if (data.getInteger("dim") != player.dimension) {
						if(world.dimension.id == SIDimensions.ETERNITY.id){
							player.triggerAchievement(SIAchievements.FALSE_ETERNITY);
						}
                        ((IWarpPlayer) player).warp(data.getInteger("dim"));
                    }
                    player.setPos(warpPosition.getInteger("x"), warpPosition.getInteger("y"), warpPosition.getInteger("z"));
                } else {
					player.triggerAchievement(SIAchievements.TELEPORT_FAIL);
                    ((IWarpPlayer) player).warp(SIDimensions.ETERNITY.id);
                }
                itemstack.getData().getCompound("inventory").getValue().remove(String.valueOf(0));
            } else {
                world.spawnParticle("signalindustries.shockwave", player.x, player.y - 1, player.z, 0.0, 0.0, 0.0, 0,32,true);
                player.triggerAchievement(SIAchievements.PULSE);
				if (EnvironmentHelper.isMultiplayerServer() || EnvironmentHelper.isSingleplayerClient()) {
                    /*AABBd bb = new AABBd(player.x - 5, player.y - 1, player.z - 5, player.x + 5, player.y + 1, player.z + 5);
                    List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, bb);
                    for (Entity entity : list) {
                        if (entity instanceof Mob) {
                            entity.hurt(player, 15, DamageType.BLAST);
                            double d = player.x - entity.x;

                            double d1;
                            for (d1 = player.z - entity.z; d * d + d1 * d1 < 1.0E-4; d1 = (Math.random() - Math.random()) * 0.01) {
                                d = (Math.random() - Math.random()) * 0.01;
                            }

                            ((Mob) entity).attackedAtYaw = (float) (Math.atan2(d1, d) * 180.0 / 3.1415927410125732) - entity.yRot;
                            ((Mob) entity).knockBack(entity, 15, d, d1);
                        }
                    }*/
					EntityShockwave s = new EntityShockwave(world, new Vec3f(player.x, player.y-1, player.z));
					world.entityJoinedWorld(s);
                }
            }
        }
        return itemstack;
    }

    @Override
    public void inventoryTick(ItemStack itemstack, @NonNull World world, @NonNull Entity entity, int slotId, boolean flag) {
        boolean charging = itemstack.getData().getBoolean("charging");
        byte charge = itemstack.getData().getByte("charge");
        int energy = getFluidStack(0, itemstack).getInteger("amount");
        if (itemstack.getData().getBoolean("charging")) {
            if (charge < 100) {
                if (energy <= 0) {
                    getFluidStack(0, itemstack).putInt("amount", 0);
                    itemstack.getData().putBoolean("charging", false);
                    ((Player) entity).sendMessage(TextFormatting.RED + "[Pulsar] Ran out of energy while charging!");
                    return;
                }
                if (getItemIdFromSlot(0, itemstack) == SIItems.warpOrb.id) {
                    getFluidStack(0, itemstack).putInt("amount", energy - 80); //charging takes 100 ticks
                } else {
                    getFluidStack(0, itemstack).putInt("amount", energy - 40); //charging takes 100 ticks
                }
                itemstack.getData().putByte("charge", (byte) (charge + 1));
            } else {
                itemstack.getData().putBoolean("charging", false);
            }

        }
        super.inventoryTick(itemstack, world, entity, slotId, flag);
    }

    /*@Override
    public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {

    }

    @Override
    public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {

    }*/

    @Override
    public void fill(FluidStack fluidStack, ItemStack stack, TileEntityFluidContainer tile, int maxAmount) {
        InventoryPulsar inv = new InventoryPulsar(stack);
        InventorySerializer.loadInvFromNBT(stack, inv, 1, 1);
        inv.insertFluid(0, fluidStack.splitStack(Math.min(maxAmount, fluidStack.amount)));
        InventorySerializer.saveInvToNBT(stack, inv);
    }

    @Override
    public String getDescription(ItemStack stack) {
        String text = super.getDescription(stack);
        String ability = getAbility(stack);
        text += TextFormatting.WHITE+"\nCharge: " + (stack.getData().getByte("charge") >= 100 ? TextFormatting.RED : TextFormatting.LIGHT_GRAY) + stack.getData().getByte("charge") + "%" + TextFormatting.WHITE + " | Ability: " + ability;
        return text;
    }

    @Override
    public boolean canFill(ItemStack stack) {
        InventoryPulsar inv = new InventoryPulsar(stack);
        InventorySerializer.loadInvFromNBT(stack, inv, 1, 1);
        return inv.fluidContents[0] == null || inv.fluidContents[0].amount < inv.getFluidCapacityForSlot(0);
    }

    public int getItemIdFromSlot(int id, ItemStack stack) {
        return stack.getData().getCompound("inventory").getCompound(String.valueOf(id)).getShort("id");
    }

    public CompoundTag getItemFromSlot(int id, ItemStack stack) {
        return stack.getData().getCompound("inventory").getCompound(String.valueOf(id));
    }

    public CompoundTag getFluidStack(int id, ItemStack stack) {
        return stack.getData().getCompound("fluidInventory").getCompound(String.valueOf(id));
    }

    public String getAbility(ItemStack stack) {
        return getItemIdFromSlot(0, stack) == SIItems.warpOrb.id ? TextFormatting.PURPLE + "Warp" : TextFormatting.RED + "Pulse";
    }

	@Override
	public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {
		ContainerInventory inv = player.inventory;
		ItemStack pulsar = inv.getCurrentItem();
		int i = (inv.armorItemInSlot(HumanArmorShape.CHEST) != null && inv.armorItemInSlot(HumanArmorShape.CHEST).getItem() instanceof ItemSignalumPowerHarness) ? height - 128 : height - 64;
		gui.drawStringShadow(fontRenderer, "The Pulsar", 4, i += 16, 0xFFFF0000);
		gui.drawStringShadow(fontRenderer, "Ability: ", 4, i += 16, 0xFFFFFFFF);
		gui.drawStringShadow(fontRenderer, ((ItemPulsar) pulsar.getItem()).getAbility(pulsar), 4 + fontRenderer.stringWidth("Ability: "), i, 0xFFFF0000);
		gui.drawStringShadow(fontRenderer, "Charge: ", 4, i += 10, 0xFFFFFFFF);
		gui.drawStringShadow(fontRenderer, String.valueOf(pulsar.getData().getByte("charge")) + "%", 4 + fontRenderer.stringWidth("Charge: "), i, pulsar.getData().getByte("charge") >= 100 ? 0xFFFF0000 : 0xFFFFFFFF);
		gui.drawStringShadow(fontRenderer, "Energy: ", 4, i += 10, 0xFFFFFFFF);
		gui.drawStringShadow(fontRenderer, String.valueOf(((ItemPulsar) pulsar.getItem()).getFluidStack(0, pulsar).getInteger("amount")), 4 + fontRenderer.stringWidth("Energy: "), i, 0xFFFF8080);
	}

	@Override
	public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {

	}
}
