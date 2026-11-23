package sunsetsatellite.signalindustries.gui.menus;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.InventoryAction;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.invs.InventoryDrill;
import sunsetsatellite.signalindustries.invs.InventoryPulsar;
import sunsetsatellite.signalindustries.items.tools.ItemPulsar;
import sunsetsatellite.signalindustries.items.tools.ItemSignalumDrill;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.slot.SlotAttachment;

public class MenuDrill extends MenuFluid {

    public int slotIndex;
    public boolean isArmor;

    public MenuDrill(@NotNull ContainerInventory playerInv, int slotIndex, boolean isArmor) {
        super(new InventoryDrill(/*isArmor ? ((IPlayerPowerSuit<?>) playerInv.player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class) : */playerInv.getItem(slotIndex)));

        this.slotIndex = slotIndex;
        this.isArmor = isArmor;

        ItemStack stack;

        /*if(isArmor){
            stack = ((IPlayerPowerSuit<?>) playerInv.player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {*/
        stack = playerInv.getItem(slotIndex);
        //}

        if (stack != null && stack.getItem() instanceof ItemSignalumDrill drill) {

            InventorySerializer.loadInvFromNBT(stack, itemInventory, 1, 0, ItemSignalumDrill.class);

            addSlot(new SlotAttachment(itemInventory, 0, 80, 33, AttachmentPoint.DRILL, drill.getTier()));

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
    public IntList getTargetSlots(@NonNull InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        int lastDeviceSlot = (slots.size() - 36) - 1;
        if (slot.index <= lastDeviceSlot) {
            return getSlots(lastDeviceSlot + 1, 36, true);
        }
        return getSlots(0, Math.max(lastDeviceSlot + 1, 1), false);
    }

    @Override
    public void onCraftGuiClosed(@NonNull Player player) {
        super.onCraftGuiClosed(player);

        ItemStack drill;

        /*if(isArmor){
            backpack = ((IPlayerPowerSuit<?>) player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {*/
        drill = player.inventory.getItem(slotIndex);
        //}

        InventorySerializer.saveInvToNBT(drill, itemInventory, ItemSignalumDrill.class);
        for (int i = 0; i < slots.size(); i++) {
            for (ContainerListener crafter : containerListeners) {
                ItemStack stack = slots.get(i).getItemStack();
                stack = stack != null ? stack.copy() : null;
                crafter.updateInventorySlot(this, i, stack);
            }
        }
    }
}
