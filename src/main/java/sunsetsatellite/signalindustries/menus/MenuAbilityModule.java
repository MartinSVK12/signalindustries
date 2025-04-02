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
import sunsetsatellite.signalindustries.invs.InventoryAbilityModule;
import sunsetsatellite.signalindustries.items.attachments.ItemAbilityModule;
import sunsetsatellite.signalindustries.items.attachments.ItemBackpackAttachment;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.SlotApplication;
import sunsetsatellite.signalindustries.util.SlotBackpack;

import java.util.List;

public class MenuAbilityModule extends MenuFluid {

    public int slotIndex;
    public boolean isArmor;

    public MenuAbilityModule(ContainerInventory playerInv, int slotIndex, boolean isArmor) {
        super(new InventoryAbilityModule(isArmor ? ((IPlayerPowerSuit<?>) playerInv.player).getPowerSuit().getAttachmentClass(ItemAbilityModule.class) : playerInv.getItem(slotIndex)));

        this.slotIndex = slotIndex;
        this.isArmor = isArmor;

        ItemStack module;

        if(isArmor){
            module = ((IPlayerPowerSuit<?>) playerInv.player).getPowerSuit().getAttachmentClass(ItemAbilityModule.class);
        } else {
            module = playerInv.getItem(slotIndex);
        }

        if (module != null && module.getItem() instanceof ItemAbilityModule) {
            for(int k = 0; k < 9; k++)
            {
                addSlot(new SlotApplication(itemInventory, k, 8 + k * 18, 32, ((ItemAbilityModule) module.getItem()).getTier() ));
            }

            for(int j = 0; j < 3; j++)
            {
                for(int i1 = 0; i1 < 9; i1++)
                {
                    addSlot(new Slot(playerInv, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
                }

            }

            for(int k = 0; k < 9; k++)
            {
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
        int lastDeviceSlot = slots.size() - 1;
        if (slot.index <= lastDeviceSlot) {
            return getSlots(lastDeviceSlot+1, 36, true);
        }
        return getSlots(0, Math.max(lastDeviceSlot+1,1), false);
    }

    @Override
    public void onCraftGuiClosed(Player player) {
        super.onCraftGuiClosed(player);

        ItemStack module;

        if(isArmor){
            module = ((IPlayerPowerSuit<?>) player).getPowerSuit().getAttachmentClass(ItemAbilityModule.class);
        } else {
            module = player.inventory.getItem(slotIndex);
        }

        InventorySerializer.saveInvToNBT(module,itemInventory);
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
