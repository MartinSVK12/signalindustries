package sunsetsatellite.signalindustries.powersuit;

import net.minecraft.core.item.ItemStack;
import org.jetbrains.annotations.Nullable;
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

    private ItemStack armor;
    private int armorPiece;

    public InventoryPowerSuit(ItemStack item) {
        super(item);
        if (item.getItem() instanceof ItemSignalumPowerSuit) {
            this.armor = item;
            ItemSignalumPowerSuit armorPiece = (ItemSignalumPowerSuit) item.getItem();
            this.armorPiece = armorPiece.getArmorPiece();
            switch (armorPiece.getArmorPiece()) {
                case 2:
                    fluidContents = new FluidStack[1];
                    fluidCapacity = new int[1];
                    fluidCapacity[0] = 32000;
                    contents = new ItemStack[9];
                    break;
                case 0:
                case 1:
                case 3:
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
        if (armorPiece == 2 && index == 1) {
            fluidCapacity[0] = 32000;
        }
        if (locked(index)) return null;
        return super.removeItem(index, takeAmount);
    }

    @Override
    public void setItem(int index, @Nullable ItemStack itemstack) {
        super.setItem(index, itemstack);
        if (locked(index)) return;
        if (armorPiece == 2 && index == 1 && itemstack == null) {
            fluidCapacity[0] = 32000;
        } else if (armorPiece == 2 && index == 1 && itemstack.getItem() instanceof ItemExtendedEnergyPackAttachment) {
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
        InventorySerializer.loadInvFromNBT(armor, this, contents.length, fluidContents.length);
    }

    public void saveToNBT() {
        InventorySerializer.saveInvToNBT(armor, this);
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.powerSuit";
    }
}
