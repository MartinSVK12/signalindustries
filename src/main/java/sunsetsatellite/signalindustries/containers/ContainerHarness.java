package sunsetsatellite.signalindustries.containers;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.catalyst.fluids.impl.ContainerItemFluid;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.fluids.util.SlotFluid;
import sunsetsatellite.signalindustries.inventories.item.InventoryHarness;
import sunsetsatellite.signalindustries.items.ItemSignalumPrototypeHarness;
import sunsetsatellite.catalyst.fluids.util.NBTHelper;

public class ContainerHarness extends ContainerItemFluid {

    ItemStack armor;

    public ContainerHarness(InventoryPlayer inventoryPlayer, ItemStack armor){
        super(inventoryPlayer,new InventoryHarness(armor));
        this.armor = armor;

        if(armor.getItem() instanceof ItemSignalumPrototypeHarness){
            NBTHelper.loadInvFromNBT(armor,inv,0,1);
        }

        addFluidSlot(new SlotFluid(inv,0,80,33));

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(inventoryPlayer, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(inventoryPlayer, k, 8 + k * 18, 142));
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
