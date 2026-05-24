package sunsetsatellite.signalindustries.items.tools;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolSword;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePos;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.interfaces.IVariableDamageWeapon;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ItemSignalumSaber extends ItemToolSword implements ITiered, IItemFluidContainer, IVariableDamageWeapon, IHasOverlay {

    public Tier tier;

    public ItemSignalumSaber(String name, String namespaceId, int id, ToolMaterial enumtoolmaterial, Tier tier) {
        super(name, namespaceId, id, enumtoolmaterial);
        this.tier = tier;
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return 500;
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return 500 - stack.getData().getInteger("energy");
    }

    @Override
    public int getFluidAmount(ItemStack stack) {
        return stack.getData().getInteger("energy");
    }

    @Override
    public boolean canFill(ItemStack stack) {
        return getRemainingCapacity(stack) > 0;
    }

    @Override
    public boolean canDrain(ItemStack stack) {
        return false;
    }

    @Override
    public FluidStack getCurrentFluid(ItemStack stack) {
        return new FluidStack(SIFluids.ENERGY, getCapacity(stack));
    }

    @Override
    public void setCurrentFluid(FluidStack fluidStack, ItemStack stack) {
        if (fluidStack.fluid != SIFluids.ENERGY) return;
        stack.getData().putInt("saturation", fluidStack.amount);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack) {
        if (fluidStack == null) {
            return null;
        }
        if (fluidStack.fluid == SIFluids.ENERGY) {
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("energy");
            int amount = fluidStack.amount;
            if (remaining == 0) {
                return null;
            }
            if (amount > remaining) {
                fluidStack.amount -= remaining;
                CompoundTag data = new CompoundTag();
                data.putInt("energy", getCapacity(stack));
                stack.setData(data);
                return stack;
            } else {
                CompoundTag data = new CompoundTag();
                data.putInt("energy", saturation + amount);
                fluidStack.amount -= amount;
                stack.setData(data);
                return stack;
            }
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IFluidInventory tile) {
        return fill(fluidStack, stack);
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IFluidInventory tile, int maxAmount) {
        if (fluidStack == null) {
            return null;
        }
        if (fluidStack.fluid == SIFluids.ENERGY) {
            int remaining = getRemainingCapacity(stack);
            int saturation = stack.getData().getInteger("energy");
            int amount = Math.min(fluidStack.amount, maxAmount);
            if (remaining == 0) return null;
            int result = Math.min(amount, remaining);
            if (result == 0) return null;
            fluidStack.amount -= result;
            CompoundTag data = new CompoundTag();
            data.putInt("energy", saturation + result);
            stack.setData(data);
            return stack;
        }
        return null;
    }

    @Override
    public ItemStack fill(FluidStack fluidStack, ItemStack stack, IItemFluidContainer inv) {
        return fill(fluidStack, stack);
    }

    @Override
    public void drain(ItemStack stack, int slot, IFluidInventory tile) {

    }

    @Override
    public void drain(ItemStack stack, ItemStack other, int slot, IItemFluidContainer inv) {

    }

    @Override
    public FluidStack drain(ItemStack stack, int amount) {
        return null;
    }

    @Override
    public List<Fluid> getAllowedFluids(ItemStack stack) {
        return Collections.singletonList(SIFluids.ENERGY);
    }

    @Override
    public ItemStack getFilled(ItemStack stack, FluidStack fluidStack) {
        return stack;
    }

    @Override
    public Tier getTier() {
        return tier;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, @NonNull Mob target, @NonNull Mob attacker) {
        int energy = itemstack.getData().getInteger("energy");
        if (itemstack.getData().getBoolean("active")) {
            if (energy > 0) {
                itemstack.getData().putInt("energy", energy - 1);
                target.remainingFireTicks = 60;
            }
        }
        if (energy <= 0) {
            itemstack.getData().putBoolean("active", false);
        }
        return true;
    }

	@Override
	public int getDamageVsEntity(@NotNull ItemStack stack, @NotNull Entity entity) {
		if (stack.getData().getBoolean("active")) {
			return 10;
		} else {
			return 4;
		}
	}

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player) {
		if (stack.getData().getInteger("energy") > 0) {
			stack.getData().putBoolean("active", !stack.getData().getBoolean("active"));
		}
		return super.onUse(stack, world, player);
	}

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getTextColor() + tier.getRank() + "\n" + "Energy: " + TextFormatting.RED + stack.getData().getInteger("energy") + TextFormatting.WHITE;
    }

	@Override
	public boolean onBlockDestroyed(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Mob mob, @NotNull Block<?> removedBlock, @NotNull TilePosc blockPos, @NotNull Side side) {
		return true;
	}

	@Override
	public void onUseByActivator(@NotNull ItemStack selfStack, @NotNull World world, @NotNull TileEntityActivator activator, @NotNull Random random, @NotNull TilePosc blockPos, @NotNull Direction direction, double offX, double offY, double offZ) {
		blockPos = blockPos.add(direction, new TilePos());
		Block<?> b = world.getBlockType(blockPos);
		if (b == Blocks.PUMPKIN) {
			world.setBlockTypeData(blockPos, Blocks.PUMPKIN_CARVED_IDLE, direction.opposite().id);
		}
	}

	@Override
	public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {
		ContainerInventory inv = player.inventory;
		ItemStack saber = inv.getCurrentItem();
		int i = (inv.armorItemInSlot(HumanArmorShape.CHEST) != null && inv.armorItemInSlot(HumanArmorShape.CHEST).getItem() instanceof ItemSignalumPowerHarness) ? height - 128 : height - 64;
		gui.drawStringShadow(fontRenderer,"Signalite Saber", 4, i += 16, 0xFFFF0000);
		gui.drawStringShadow(fontRenderer,"Energy: ", 4, i += 16, 0xFFFFFFFF);
		gui.drawStringShadow(fontRenderer, String.valueOf(saber.getData().getInteger("energy")), 4 + fontRenderer.stringWidth("Energy: "), i, 0xFFFF8080);
		gui.drawStringShadow(fontRenderer, saber.getData().getBoolean("active") ? "Active" : "Inactive", 4, i += 10, saber.getData().getBoolean("active") ? 0xFF00FF00 : 0xFF808080);
	}

	@Override
	public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {

	}
}
