package sunsetsatellite.signalindustries.powersuit;

import net.minecraft.core.crafting.ContainerListener;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.MenuFluid;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.base.ItemArmorTiered;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import sunsetsatellite.signalindustries.util.SlotAttachment;
import sunsetsatellite.signalindustries.util.Tier;

public class MenuPowerSuit extends MenuFluid {

    public int xSize = 176;
    public int ySize = 166;
    public int slotIndex;
    public Player player;

    public MenuPowerSuit(ContainerInventory playerInv, int slotIndex, boolean isArmor) {
        super(new InventoryPowerSuit(playerInv.armorItemInSlot(slotIndex)));

        this.player = playerInv.player;
        this.slotIndex = slotIndex;

        ItemArmorTiered armor = (ItemArmorTiered) playerInv.armorItemInSlot(slotIndex).getItem();

        switch (armor.getArmorPiece()) {
            case 3:
                addSlot(new SlotAttachment(itemInventory, 0, xSize / 2 - 8, ySize / 3 - 20, AttachmentPoint.HEAD_TOP, Tier.REINFORCED));
                addSlot(new SlotAttachment(itemInventory, 1, xSize / 2 - 8, ySize / 3, AttachmentPoint.HEAD_LENS, Tier.REINFORCED));
                break;
            case 2:
                addFluidSlot(new SlotFluid(fluidInventory, 0, xSize / 2 - 8, ySize / 3 - 20));

                addSlot(new SlotAttachment(itemInventory, 0, xSize / 2 - 8, ySize / 3 - 44, AttachmentPoint.CORE_MODULE, Tier.REINFORCED));

                addSlot(new SlotAttachment(itemInventory, 1, xSize / 2 - 8, ySize / 3 + 4, AttachmentPoint.CORE_BACK, Tier.REINFORCED));

                addSlot(new SlotAttachment(itemInventory, 2, xSize / 3 - 8, ySize / 3 - 20, AttachmentPoint.ARM_FRONT, Tier.REINFORCED));
                addSlot(new SlotAttachment(itemInventory, 3, xSize / 3 - 20 - 8, ySize / 3 - 20, AttachmentPoint.ARM_BACK, Tier.REINFORCED));
                addSlot(new SlotAttachment(itemInventory, 4, xSize / 3 - 40 - 8, ySize / 3 - 20, AttachmentPoint.ARM_SIDE, Tier.REINFORCED));

                addSlot(new SlotAttachment(itemInventory, 5, xSize - 27, ySize / 3 - 20, AttachmentPoint.ARM_SIDE, Tier.REINFORCED));
                addSlot(new SlotAttachment(itemInventory, 6, xSize - 20 - 27, ySize / 3 - 20, AttachmentPoint.ARM_BACK, Tier.REINFORCED));
                addSlot(new SlotAttachment(itemInventory, 7, xSize - 40 - 27, ySize / 3 - 20, AttachmentPoint.ARM_FRONT, Tier.REINFORCED));
                addSlot(new SlotAttachment(itemInventory, 8, xSize - 20 - 27, ySize / 3 - 44, AttachmentPoint.COLORIZER, Tier.REINFORCED));
                break;
            case 1:
                addSlot(new SlotAttachment(itemInventory, 0, xSize / 3 - 8, ySize / 3 - 20, AttachmentPoint.LEG_SIDE, Tier.REINFORCED));
                addSlot(new SlotAttachment(itemInventory, 1, xSize - 40 - 27, ySize / 3 - 20, AttachmentPoint.LEG_SIDE, Tier.REINFORCED));
                break;
            case 0:
                addSlot(new SlotAttachment(itemInventory, 0, xSize / 3 - 8, ySize / 3 - 20, AttachmentPoint.BOOT_BACK, Tier.REINFORCED));
                addSlot(new SlotAttachment(itemInventory, 1, xSize - 40 - 27, ySize / 3 - 20, AttachmentPoint.BOOT_BACK, Tier.REINFORCED));
                break;
        }

        for (int j = 0; j < 3; j++) {
            for (int i1 = 0; i1 < 9; i1++) {
                addSlot(new Slot(playerInv, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for (int k = 0; k < 9; k++) {
            addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

    }

    @Override
    public void onCraftGuiClosed(Player player) {
        super.onCraftGuiClosed(player);
        InventorySerializer.saveInvToNBT(player.inventory.armorItemInSlot(slotIndex), itemInventory);
        for (int i = 0; i < slots.size(); i++) {
            for (ContainerListener crafter : containerListeners) {
                ItemStack stack = slots.get(i).getItemStack();
                stack = stack != null ? stack.copy() : null;
                crafter.updateInventorySlot(this, i, stack);
            }
        }
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        SignalumPowerSuit suit = ((IPlayerPowerSuit<SignalumPowerSuit>) player).getPowerSuit();
        if (suit != null) {
            InventorySerializer.saveInvToNBT(player.inventory.armorItemInSlot(slotIndex), itemInventory);
            suit.reload();

        }
    }
}
