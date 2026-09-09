package sunsetsatellite.signalindustries.items.tools;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicLog;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.data.gamerule.TreecapitatorHelper;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolAxe;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Collections;
import java.util.List;

public class ItemChainsaw extends ItemToolAxe implements ITiered, IItemFluidContainer, IHasOverlay {

	public Tier tier;
	public int blockDestroyCost = 1;

	public ItemChainsaw(String lang, String namespaceId, int id, ToolMaterial enumtoolmaterial, Tier tier) {
		super(lang, namespaceId, id, enumtoolmaterial);
		this.tier = tier;
	}

	@Override
	public String getDescription(ItemStack stack) {
		return "Tier: " + tier.getTextColor() + tier.getRank() + "\n" + TextFormatting.WHITE + "Energy: " + TextFormatting.RED + stack.getData().getInteger("energy") + "/" + getCapacity(stack) + TextFormatting.WHITE;
	}

	@Override
	public int getCapacity(ItemStack stack) {
		return 4000;
	}

	@Override
	public int getRemainingCapacity(ItemStack stack) {
		return 4000 - stack.getData().getInteger("energy");
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
	public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {
		ContainerInventory inv = player.inventory;
		ItemStack saber = inv.getCurrentItem();
		int i = (inv.armorItemInSlot(HumanArmorShape.CHEST) != null && inv.armorItemInSlot(HumanArmorShape.CHEST).getItem() instanceof ItemSignalumPowerHarness) ? height - 128 : height - 64;
		gui.drawStringShadow(fontRenderer,"Chainsaw", 4, i += 16, 0xFFFF0000);
		gui.drawStringShadow(fontRenderer,"Energy: ", 4, i += 16, 0xFFFFFFFF);
		gui.drawStringShadow(fontRenderer, String.valueOf(saber.getData().getInteger("energy")), 4 + fontRenderer.stringWidth("Energy: "), i, 0xFFFF8080);
	}

	@Override
	public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {

	}

	@Override
	public boolean beforeBlockDestroyed(@NotNull ItemStack stack, @NotNull World world, @NotNull Player player, @NotNull Block<?> block, @NotNull TilePosc blockPos, @NotNull Side side) {
		int energy = stack.getData().getInteger("energy");
		if(energy <= 0) return false;
		if(!world.isClientSide && !player.isSneaking()) {
			final @NotNull Block<?> blockType = world.getBlockType(blockPos);
			if(Block.hasLogicClass(blockType, BlockLogicLog.class)) {
				boolean b = !new TreecapitatorHelper(world, blockPos.x(), blockPos.y(), blockPos.z(), player).chopTree();
				int cost = Math.min(energy, stack.getMetadata()*blockDestroyCost);
				stack.getData().putInt("energy", energy - cost*blockDestroyCost);
				return b;
			}
		}
		if (energy >= blockDestroyCost) {
			stack.getData().putInt("energy", energy - blockDestroyCost);
		}
		stack.setMetadata(0);
		return true;
	}

	@Override
	public void inventoryTick(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Entity entity, int slotId, boolean isHeld) {
		selfStack.setMetadata(0);
	}

	@Override
	public boolean onBlockDestroyed(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Mob mob, @NotNull Block<?> removedBlock, @NotNull TilePosc blockPos, @NotNull Side side) {
		return true;
	}

	@Override
	public float getStrVsBlock(@NonNull ItemStack itemstack, @NonNull Block<?> block) {
		float superValue = super.getStrVsBlock(itemstack, block);
		if (superValue == 1.0f) return 1.0f;
		if (itemstack.getData().getInteger("energy") >= blockDestroyCost) {
			return this.material.getEfficiency(false);
		} else {
			return 1.0f;
		}
	}

	@Override
	public boolean hitEntity(@NotNull ItemStack selfStack, @NotNull Mob target, @NotNull Mob attacker) {
		return true;
	}
}
