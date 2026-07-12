package sunsetsatellite.signalindustries.powersuit;

import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.util.Fluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.invs.InventoryItemFluid;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemExtendedEnergyPackAttachment;
import sunsetsatellite.signalindustries.util.InventorySerializer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class InventoryPowerSuit extends InventoryItemFluid {

    private ItemStack armorStack;
    private HumanArmorShape armorPiece;

    public InventoryPowerSuit(ItemStack item) {
        super(item);
        if (item.getItem() instanceof ItemSignalumPowerSuit armor) {
            this.armorStack = item;
			this.armorPiece = armor.getArmorShape();
            switch (armor.getArmorShape()) {
				case CHEST:
                    fluidContents = new FluidStack[1];
                    fluidCapacity = new int[1];
                    fluidCapacity[0] = 32000;
                    contents = new ItemStack[9];
                    break;
				case HEAD:
				case LEGS:
				case BOOTS:
                    fluidContents = new FluidStack[0];
                    fluidCapacity = new int[0];
                    contents = new ItemStack[2];
                    break;
            }
            this.acceptedFluids = new ArrayList<>(this.fluidContents.length);
            for (int i = 0; i < fluidContents.length; i++) {
                ArrayList<Fluid> list = new ArrayList<>();
                list.add(SIFluids.ENERGY);
                acceptedFluids.add(list);
            }
            readFromNBT();
        }

    }

    @Override
    public @Nullable ItemStack removeItem(int index, int takeAmount) {
        if (armorPiece == HumanArmorShape.CHEST && index == 1) {
            fluidCapacity[0] = 32000;
        }
        if (locked(index)) return null;
        return super.removeItem(index, takeAmount);
    }

    @Override
    public void setItem(int index, @Nullable ItemStack itemstack) {
        super.setItem(index, itemstack);
        if (locked(index)) return;
        if (armorPiece == HumanArmorShape.CHEST && index == 1 && itemstack == null) {
            fluidCapacity[0] = 32000;
        } else if (armorPiece == HumanArmorShape.CHEST && index == 1 && itemstack.getItem() instanceof ItemExtendedEnergyPackAttachment) {
            fluidCapacity[0] = 64000;
        }
    }

    @Override
    public boolean locked(int index) {
        if (index == 1 && getItem(1) != null && getItem(1).getItem() instanceof ItemExtendedEnergyPackAttachment && fluidContents[0] != null && fluidContents[0].amount > 32000) {
            return true;
        }
        return super.locked(index);
    }

    public boolean isEmpty() {
        return Arrays.stream(contents).allMatch(Objects::isNull);
    }

    public void readFromNBT() {
        InventorySerializer.loadInvFromNBT(armorStack, this, contents.length, fluidContents.length);
    }

    public void saveToNBT() {
        InventorySerializer.saveInvToNBT(armorStack, this);
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.powerSuit";
    }
}
