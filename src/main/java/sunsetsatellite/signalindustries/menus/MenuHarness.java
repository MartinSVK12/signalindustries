package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.invs.InventoryHarness;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerHarness;
import sunsetsatellite.signalindustries.util.InventorySerializer;

public class MenuHarness extends MenuFluid {

    public int slotIndex;

    public MenuHarness(ContainerInventory playerInv, int slotIndex, boolean isArmor) {
        super(new InventoryHarness(playerInv.getItem(slotIndex)));

        this.slotIndex = slotIndex;

        ItemStack armor = playerInv.getItem(slotIndex);

        if (armor != null && armor.getItem() instanceof ItemSignalumPowerHarness) {
            InventorySerializer.loadInvFromNBT(armor, itemInventory, 0, 1);

            addFluidSlot(new SlotFluid(fluidInventory, 0, 80, 33));

            for (int j = 0; j < 3; j++) {
                for (int i1 = 0; i1 < 9; i1++) {
                    addSlot(new Slot(playerInv, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
                }

            }

            for (int k = 0; k < 9; k++) {
                addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
            }
        }

    }

    @Override
    public void onCraftGuiClosed(Player player) {
        super.onCraftGuiClosed(player);
        InventorySerializer.saveInvToNBT(player.inventory.getItem(slotIndex), itemInventory);
        for (int i = 0; i < slots.size(); i++) {
            for (ContainerListener crafter : containerListeners) {
                ItemStack stack = slots.get(i).getItemStack();
                stack = stack != null ? stack.copy() : null;
                crafter.updateInventorySlot(this, i, stack);
            }
        }
    }
}
