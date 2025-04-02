package sunsetsatellite.signalindustries.menus;

import net.minecraft.core.InventoryAction;
import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.invs.InventoryBackpack;
import sunsetsatellite.signalindustries.items.attachments.ItemBackpackAttachment;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.SlotBackpack;

import java.util.List;

public class MenuBackpack extends MenuFluid {

    public int backpackSlotIndex;
    public boolean isArmor;

    public MenuBackpack(ContainerInventory playerInv, int backpackSlotIndex, boolean isArmor) {
        super(new InventoryBackpack(isArmor ? ((IPlayerPowerSuit<?>) playerInv.player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class) : playerInv.getItem(backpackSlotIndex)));

        this.backpackSlotIndex = backpackSlotIndex;
        this.isArmor = isArmor;

        ItemStack backpack;

        if(isArmor){
            backpack = ((IPlayerPowerSuit<?>) playerInv.player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {
            backpack = playerInv.getItem(backpackSlotIndex);
        }

        if (backpack != null && backpack.getItem() instanceof ItemBackpackAttachment) {
            switch (((ItemBackpackAttachment) backpack.getItem()).getTier()) {
                case BASIC: {
                    InventorySerializer.loadInvFromNBT(backpack, itemInventory, 27, 2);

                    for (int y = 0; y < 2; y++) {
                        addFluidSlot(new SlotFluid(fluidInventory, y, 174, 36 + 18 * y));
                    }

                    int numberOfRows = 27 / 9;
                    int i = (numberOfRows - 4) * 18;
                    for (int j = 0; j < numberOfRows; j++) {
                        for (int i1 = 0; i1 < 9; i1++) {
                            addSlot(new SlotBackpack(itemInventory, i1 + j * 9, 8 + i1 * 18, 18 + j * 18));
                        }

                    }

                    for (int k = 0; k < 3; k++) {
                        for (int j1 = 0; j1 < 9; j1++) {
                            addSlot(new Slot(playerInv, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
                        }

                    }

                    for (int l = 0; l < 9; l++) {
                        addSlot(new Slot(playerInv, l, 8 + l * 18, 161 + i));
                    }
                    break;
                }
                case REINFORCED: {
                    InventorySerializer.loadInvFromNBT(backpack, itemInventory, 27 * 2, 4);

                    for (int y = 0; y < 4; y++) {
                        addFluidSlot(new SlotFluid(fluidInventory, y, 174, 36 + 18 * y));
                    }

                    int numberOfRows = (27 * 2) / 9;
                    int i = (numberOfRows - 4) * 18;
                    for (int j = 0; j < numberOfRows; j++) {
                        for (int i1 = 0; i1 < 9; i1++) {
                            addSlot(new SlotBackpack(itemInventory, i1 + j * 9, 8 + i1 * 18, 18 + j * 18));
                        }

                    }

                    for (int k = 0; k < 3; k++) {
                        for (int j1 = 0; j1 < 9; j1++) {
                            addSlot(new Slot(playerInv, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
                        }

                    }

                    for (int l = 0; l < 9; l++) {
                        addSlot(new Slot(playerInv, l, 8 + l * 18, 161 + i));
                    }
                    break;
                }
            }
        }

    }


    @Override
    public FluidStack clickFluidSlot(int slotID, int button, boolean shift, boolean control, Player entityplayer) {
        return super.clickFluidSlot(slotID, button, shift, control, entityplayer);
    }

    @Override
    public List<Integer> getTargetSlots(InventoryAction inventoryAction, Slot slot, int i, Player entityPlayer) {
        int lastDeviceSlot = slots.size() - 1;
        if (slot.index <= lastDeviceSlot) {
            return getSlots(lastDeviceSlot+1, 36, true);
        }
        return getSlots(0, Math.max(lastDeviceSlot+1,1), false);
    }

    @Override
    public void onCraftGuiClosed(Player player) {
        super.onCraftGuiClosed(player);

        ItemStack backpack;

        if(isArmor){
            backpack = ((IPlayerPowerSuit<?>) player).getPowerSuit().getAttachmentClass(ItemBackpackAttachment.class);
        } else {
            backpack = player.inventory.getItem(backpackSlotIndex);
        }

        InventorySerializer.saveInvToNBT(backpack,itemInventory);
        for(int i = 0; i < slots.size(); i++)
        {
            for (ContainerListener crafter : containerListeners) {
                ItemStack stack = slots.get(i).getItemStack();
                stack = stack != null ? stack.copy() : null;
                crafter.updateInventorySlot(this, i, stack);
            }
        }
    }
}
