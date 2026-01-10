package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.invs.InventoryPulsar;
import sunsetsatellite.signalindustries.items.tools.ItemPulsar;
import sunsetsatellite.signalindustries.util.InventorySerializer;

import java.util.List;

public class MenuPulsar extends MenuFluid {

    public int pulsarSlotIndex;
    public boolean isArmor;

    public MenuPulsar(@NotNull ContainerInventory playerInv, int pulsarSlotIndex, boolean isArmor) {
        super(new InventoryPulsar(/*isArmor ? ((IPlayerPowerSuit<?>) playerInv.player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class) : */playerInv.getItem(pulsarSlotIndex)));

        this.pulsarSlotIndex = pulsarSlotIndex;
        this.isArmor = isArmor;

        ItemStack pulsar;

        /*if(isArmor){
            pulsar = ((IPlayerPowerSuit<?>) playerInv.player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {*/
        pulsar = playerInv.getItem(pulsarSlotIndex);
        //}

        if (pulsar != null && pulsar.getItem() instanceof ItemPulsar) {

            InventorySerializer.loadInvFromNBT(pulsar, itemInventory, 1, 1);

            addSlot(new Slot(itemInventory, 0, 80, 33));

            addFluidSlot(new SlotFluid(fluidInventory, 0, 80, 55));

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
    public FluidStack clickFluidSlot(int slotID, int button, boolean shift, boolean control, Player entityplayer) {
        return super.clickFluidSlot(slotID, button, shift, control, entityplayer);
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        int lastDeviceSlot = (slots.size() - 36) - 1;
        if (slot.index <= lastDeviceSlot) {
            return getSlots(lastDeviceSlot + 1, 36, true);
        }
        return getSlots(0, Math.max(lastDeviceSlot + 1, 1), false);
    }

    @Override
    public void onCraftGuiClosed(Player player) {
        super.onCraftGuiClosed(player);

        ItemStack pulsar;

        /*if(isArmor){
            backpack = ((IPlayerPowerSuit<?>) player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {*/
        pulsar = player.inventory.getItem(pulsarSlotIndex);
        //}

        InventorySerializer.saveInvToNBT(pulsar, itemInventory);
        for (int i = 0; i < slots.size(); i++) {
            for (ContainerListener crafter : containerListeners) {
                ItemStack stack = slots.get(i).getItemStack();
                stack = stack != null ? stack.copy() : null;
                crafter.updateInventorySlot(this, i, stack);
            }
        }
    }
}
