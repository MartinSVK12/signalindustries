package sunsetsatellite.signalindustries.containers;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.ContainerItemFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.inventories.item.InventoryBackpack;
import sunsetsatellite.signalindustries.items.attachments.ItemBackpackAttachment;
import sunsetsatellite.catalyst.fluids.util.NBTHelper;
import sunsetsatellite.signalindustries.util.SlotBackpack;

public class ContainerBackpack extends ContainerItemFluid {
    ItemStack backpack;

    public ContainerBackpack(InventoryPlayer inventoryPlayer, ItemStack backpack){
        super(inventoryPlayer,new InventoryBackpack(backpack));
        this.backpack = backpack;

        if(backpack.getItem() instanceof ItemBackpackAttachment){
            switch (((ItemBackpackAttachment) backpack.getItem()).getTier()) {
                case BASIC: {
                    NBTHelper.loadInvFromNBT(backpack,inv,27,2);

                    for (int y = 0; y < 2; y++) {
                        addFluidSlot(new SlotFluid(inv,y,174,36 + 18 * y));
                    }

                    int numberOfRows = 27 / 9;
                    int i = (numberOfRows - 4) * 18;
                    for (int j = 0; j < numberOfRows; j++) {
                        for (int i1 = 0; i1 < 9; i1++) {
                            addSlot(new SlotBackpack(inv, i1 + j * 9, 8 + i1 * 18, 18 + j * 18));
                        }

                    }

                    for (int k = 0; k < 3; k++) {
                        for (int j1 = 0; j1 < 9; j1++) {
                            addSlot(new Slot(inventoryPlayer, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
                        }

                    }

                    for (int l = 0; l < 9; l++) {
                        addSlot(new Slot(inventoryPlayer, l, 8 + l * 18, 161 + i));
                    }
                    break;
                }
                case REINFORCED: {
                    NBTHelper.loadInvFromNBT(backpack,inv,27*2,4);

                    for (int y = 0; y < 4; y++) {
                        addFluidSlot(new SlotFluid(inv,y,174,36 + 18 * y));
                    }

                    int numberOfRows = (27 * 2) / 9;
                    int i = (numberOfRows - 4) * 18;
                    for (int j = 0; j < numberOfRows; j++) {
                        for (int i1 = 0; i1 < 9; i1++) {
                            addSlot(new SlotBackpack(inv, i1 + j * 9, 8 + i1 * 18, 18 + j * 18));
                        }

                    }

                    for (int k = 0; k < 3; k++) {
                        for (int j1 = 0; j1 < 9; j1++) {
                            addSlot(new Slot(inventoryPlayer, j1 + k * 9 + 9, 8 + j1 * 18, 103 + k * 18 + i));
                        }

                    }

                    for (int l = 0; l < 9; l++) {
                        addSlot(new Slot(inventoryPlayer, l, 8 + l * 18, 161 + i));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public FluidStack clickFluidSlot(int slotID, int button, boolean shift, boolean control, EntityPlayer entityplayer) {
        return super.clickFluidSlot(slotID, button, shift, control, entityplayer);
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }
}
