package sunsetsatellite.signalindustries.items.tools;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.hud.HudIngame;
import net.minecraft.client.render.entity.EntityRendererItem;
import net.minecraft.client.render.font.FontRenderer;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolPickaxe;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Collections;
import java.util.List;

public class ItemSignalumDrill extends ItemToolPickaxe implements ITiered, IItemFluidContainer, IHasOverlay {
    public Tier tier;
    public int blockDestroyCost = 1;

    public ItemSignalumDrill(String lang, String namespaceId, int id, ToolMaterial enumtoolmaterial, Tier tier) {
        super(lang, namespaceId, id, enumtoolmaterial);
        this.tier = tier;
    }

	@Override
	public void renderOverlay(HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {
		ContainerInventory inv = player.inventory;
		ItemStack drill = inv.getCurrentItem();
		int i = (inv.armorItemInSlot(HumanArmorShape.CHEST) != null && inv.armorItemInSlot(HumanArmorShape.CHEST).getItem() instanceof ItemSignalumPowerHarness) ? height - 128 : height - 64;
		gui.drawStringShadow(fontRenderer,"Signalite Drill", 4, i += 16, 0xFFFF0000);
		gui.drawStringShadow(fontRenderer,"Energy: ", 4, i += 16, 0xFFFFFFFF);
		gui.drawStringShadow(fontRenderer,String.valueOf(drill.getData().getInteger("energy")), 4 + fontRenderer.stringWidth("Energy: "), i, 0xFFFF8080);
		gui.drawStringShadow(fontRenderer,getModeString(drill), 4, i += 10, 0xFFFFFFFF);
	}

	@Override
	public void renderOverlay(ItemStack stack, IPowerSuit signalumPowerSuit, HudIngame guiIngame, Player player, int height, int width, int mouseX, int mouseY, Gui gui, FontRenderer fontRenderer, EntityRendererItem itemRenderer) {

	}

	public enum DrillMode {
        NORMAL,
        X3,
        X3_UNSAFE,
		DISASSEMBLE
    }

    public String getModeString(ItemStack stack) {
        DrillMode mode = getMode(stack);
		return switch (mode) {
		    case NORMAL -> TextFormatting.GRAY + "Normal";
		    case X3 -> TextFormatting.YELLOW + "3x3";
		    case X3_UNSAFE -> TextFormatting.RED + "3x3 (Unsafe)";
			case DISASSEMBLE -> TextFormatting.ORANGE + "/!\\ Disassemble /!\\";
	    };
	}

    public DrillMode getMode(ItemStack stack) {
        try {
            return DrillMode.valueOf(stack.getData().getString("mode"));
        } catch (IllegalArgumentException e) {
            return DrillMode.NORMAL;
        }
    }

    public void setMode(ItemStack stack, DrillMode mode) {
        stack.getData().putString("mode", mode.toString());
    }

	@Override
	public boolean onBlockDestroyed(@NotNull ItemStack stack, @NotNull World world, @NotNull Mob mob, @NotNull Block<?> removedBlock, @NotNull TilePosc blockPos, @NotNull Side side) {
		if (world.isClientSide) return true;
		int energy = stack.getData().getInteger("energy");
		DrillMode mode = getMode(stack);

		int ox = blockPos.x();
		int oy = blockPos.y();
		int oz = blockPos.z();
		switch (mode) {
			case NORMAL: {
				if (energy >= blockDestroyCost) {
					stack.getData().putInt("energy", energy - blockDestroyCost);
				}
				break;
			}
			case X3_UNSAFE:
			case X3: {
				//todo: change to normal (1)
				int size = 1;
				blockDestroyCost = 0;
				if (side.axis() == Axis.Y) {
					for (int x = -size; x <= size; x++) {
						for (int z = -size; z <= size; z++) {
							Block<?> block = world.getBlock(ox + x, oy, oz + z);
							if (energy >= blockDestroyCost) {
								stack.getData().putInt("energy", energy - blockDestroyCost);
								energy = stack.getData().getInteger("energy");
								int meta = world.getBlockMetadata(ox + x, oy, oz + z);
								TileEntity tile = world.getTileEntity(ox + x, oy, oz + z);
								if ((((Player) mob)).canHarvestBlock(block) && (tile == null || getMode(stack) == DrillMode.X3_UNSAFE)) {
									block.harvestBlock(world, (Player) mob, ox + x, oy, oz + z, meta, null);
									world.setBlockWithNotify(ox + x, oy, oz + z, 0);
								}
							}
						}
					}
				} else if (side.axis() == Axis.Z) {
					for (int x = -size; x <= size; x++) {
						for (int y = -size; y <= size; y++) {
							Block<?> block = world.getBlock(ox + x, oy + y, oz);
							if (energy >= blockDestroyCost) {
								stack.getData().putInt("energy", energy - blockDestroyCost);
								energy = stack.getData().getInteger("energy");
								int meta = world.getBlockMetadata(ox + x, oy + y, oz);
								TileEntity tile = world.getTileEntity(ox + x, oy + y, oz);
								if ((((Player) mob)).canHarvestBlock(block) && (tile == null || getMode(stack) == DrillMode.X3_UNSAFE)) {
									block.harvestBlock(world, (Player) mob, ox + x, oy + y, oz, meta, null);
									world.setBlockWithNotify(ox + x, oy + y, oz, 0);
								}
							}
						}
					}
				} else if (side.axis() == Axis.X) {
					for (int z = -size; z <= size; z++) {
						for (int y = -size; y <= size; y++) {
							Block<?> block = world.getBlock(ox, oy + y, oz + z);
							if (energy >= blockDestroyCost) {
								stack.getData().putInt("energy", energy - blockDestroyCost);
								energy = stack.getData().getInteger("energy");
								int meta = world.getBlockMetadata(ox, oy + y, oz + z);
								TileEntity tile = world.getTileEntity(ox, oy + y, oz + z);
								if ((((Player) mob)).canHarvestBlock(block) && (tile == null || getMode(stack) == DrillMode.X3_UNSAFE)) {
									block.harvestBlock(world, (Player) mob, ox, oy + y, oz + z, meta, null);
									world.setBlockWithNotify(ox, oy + y, oz + z, 0);
								}
							}
						}
					}
				}
				break;
			}
		}

		return true;
	}

	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player) {
		if(getMode(selfStack) == DrillMode.DISASSEMBLE) {
			if(player.isSneaking()) {
				switch (tier){
					case BASIC -> {
						player.inventory.setItem(player.inventory.getCurrentSlot(),SIItems.basicDrillBit.getDefaultStack());
						player.inventory.insertItem(SIItems.basicDrillCasing.getDefaultStack(), false);
						player.sendMessage("Drill disassembled!");
					}
					case REINFORCED -> {
						player.inventory.setItem(player.inventory.getCurrentSlot(),SIItems.reinforcedDrillBit.getDefaultStack());
						player.inventory.insertItem(SIItems.reinforcedDrillCasing.getDefaultStack(), false);
						player.sendMessage("Drill disassembled!");
					}
				}

			}
		}
		return super.onUse(selfStack, world, player);
	}

	@Override
    public Tier getTier() {
        return tier;
    }

    @Override
    public int getCapacity(ItemStack stack) {
        return 4000 * tier.ordinal();
    }

    @Override
    public int getRemainingCapacity(ItemStack stack) {
        return getCapacity(stack) - stack.getData().getInteger("energy");
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
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getTextColor() + tier.getRank() + "\n" + TextFormatting.WHITE + "Mode: " + getModeString(stack) + "\n" + TextFormatting.WHITE + "Energy: " + TextFormatting.RED + stack.getData().getInteger("energy") + "/" + getCapacity(stack) + TextFormatting.WHITE;
    }

    @Override
    public float getStrVsBlock(@NonNull ItemStack itemstack, @NonNull Block<?> block) {
        float superValue = super.getStrVsBlock(itemstack, block);
        if (block.hasTag(BlockTags.MINEABLE_BY_SHOVEL)) {
            superValue = material.getEfficiency(false);
        }
        if (superValue == 1.0f) return 1.0f;
        if (itemstack.getData().getInteger("energy") >= blockDestroyCost) {
            return this.material.getEfficiency(false);
        } else {
            return 1.0f;
        }
    }

	@Override
	public boolean canHarvestBlock(@NotNull ItemStack selfStack, @NotNull Mob mob, @NotNull Block<?> block) {
		int miningLevel = miningLevels.getOrDefault(block, -1);
		if (miningLevel != -1) return material.getMiningLevel() >= miningLevel;

		return block.hasTag(BlockTags.MINEABLE_BY_PICKAXE) || block.hasTag(BlockTags.MINEABLE_BY_SHOVEL);
	}

    @Override
    public boolean hitEntity(@NonNull ItemStack itemstack, @NonNull Mob target, @NonNull Mob attacker) {
        return true;
    }
}
