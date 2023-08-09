package sunsetsatellite.signalindustries.containers;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.fluidapi.api.ContainerItemFluid;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.signalindustries.inventories.InventoryPulsar;
import sunsetsatellite.signalindustries.items.ItemPulsar;
import sunsetsatellite.signalindustries.util.NBTHelper;

public class ContainerPulsar extends ContainerItemFluid {

    ItemStack pulsar;

    public ContainerPulsar(InventoryPlayer inventoryPlayer, ItemStack pulsar){
        super(inventoryPlayer,new InventoryPulsar(pulsar));
        this.pulsar = pulsar;

        if(pulsar.getItem() instanceof ItemPulsar){
            NBTHelper.loadInvFromNBT(pulsar,inv,1,1);
        }

        addSlot(new Slot(inv,0,80,33));

        addFluidSlot(new SlotFluid(inv,0,80,55));
        //addSlot(new Slot(inv,1,80,55));

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
