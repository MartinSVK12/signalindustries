package sunsetsatellite.signalindustries.items.tools;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.Font;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolSword;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
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
    public boolean hitEntity(ItemStack itemstack, Mob target, Mob attacker) {
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
    public int getDamageVsEntity(Entity entity, ItemStack stack) {
        if (stack.getData().getBoolean("active")) {
            return 10;
        } else {
            return 4;
        }
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
        if (itemstack.getData().getInteger("energy") > 0) {
            itemstack.getData().putBoolean("active", !itemstack.getData().getBoolean("active"));
        }
        return super.onUseItem(itemstack, world, entityplayer);
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getTextColor() + tier.getRank() + "\n" + "Energy: " + TextFormatting.RED + stack.getData().getInteger("energy") + TextFormatting.WHITE;
    }

    @Override
    public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {
        ContainerInventory inv = player.inventory;
        ItemStack saber = inv.getCurrentItem();
        int i = (inv.armorItemInSlot(2) != null && inv.armorItemInSlot(2).getItem() instanceof ItemSignalumPowerHarness) ? height - 128 : height - 64;
        fontRenderer.drawStringWithShadow("Signalite Saber", 4, i += 16, 0xFFFF0000);
        fontRenderer.drawStringWithShadow("Energy: ", 4, i += 16, 0xFFFFFFFF);
        fontRenderer.drawStringWithShadow(String.valueOf(saber.getData().getInteger("energy")), 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
        fontRenderer.drawStringWithShadow(saber.getData().getBoolean("active") ? "Active" : "Inactive", 4, i += 10, saber.getData().getBoolean("active") ? 0xFF00FF00 : 0xFF808080);
    }

    @Override
    public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Font fontRenderer, EntityRendererItem itemRenderer) {

    }

    @Override
    public boolean onBlockDestroyed(World world, ItemStack itemstack, int i, int x, int y, int z, Side side, Mob mob) {
        return true;
    }

    @Override
    public void onUseByActivator(ItemStack itemStack, TileEntityActivator activatorBlock, World world, Random random, int blockX, int blockY, int blockZ, double offX, double offY, double offZ, Direction direction) {
        blockX += direction.getOffsetX();
        blockY += direction.getOffsetY();
        blockZ += direction.getOffsetZ();
        Block<?> b = world.getBlock(blockX, blockY, blockZ);
        if (b == Blocks.PUMPKIN) {
            world.setBlockAndMetadata(blockX, blockY, blockZ, Blocks.PUMPKIN_CARVED_IDLE.id(), direction.getOpposite().getId());
        }
    }
}
