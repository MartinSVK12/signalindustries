package sunsetsatellite.signalindustries.containers;


import net.minecraft.core.crafting.ICrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.player.inventory.slot.Slot;
import sunsetsatellite.fluidapi.api.ContainerFluid;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.fluidapi.template.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.inventories.TileEntityPlateFormer;

public class ContainerPlateFormer extends ContainerFluid {

    private final TileEntityPlateFormer machine = ((TileEntityPlateFormer) tile);

    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;


    public ContainerPlateFormer(IInventory iInventory, TileEntityFluidItemContainer tileEntity){
        super(iInventory, tileEntity);
        tile = tileEntity;

        SlotFluid slot = new SlotFluid(tileEntity, 0, 56,53);
        addFluidSlot(slot);

        this.addSlot(new Slot(tileEntity, 0, 56, 17));
        this.addSlot(new Slot(tileEntity, 1, 116, 35));

        for(int j = 0; j < 3; j++)
        {
            for(int i1 = 0; i1 < 9; i1++)
            {
                addSlot(new Slot(iInventory, i1 + j * 9 + 9, 8 + i1 * 18, 84 + j * 18));
            }

        }

        for(int k = 0; k < 9; k++)
        {
            addSlot(new Slot(iInventory, k, 8 + k * 18, 142));
        }
    }

    public void updateInventory() {
        super.updateInventory();

        for (ICrafting crafter : this.crafters) {
            if (this.progressTicks != machine.progressTicks) {
                crafter.updateCraftingInventoryInfo(this, 0, machine.progressTicks);
            }

            if (this.fuelBurnTicks != machine.fuelBurnTicks) {
                crafter.updateCraftingInventoryInfo(this, 1, machine.fuelBurnTicks);
            }

            if (this.progressMaxTicks != machine.progressMaxTicks) {
                crafter.updateCraftingInventoryInfo(this, 2, machine.progressMaxTicks);
            }

            if (this.fuelMaxBurnTicks != machine.fuelMaxBurnTicks) {
                crafter.updateCraftingInventoryInfo(this, 3, machine.fuelMaxBurnTicks);
            }
        }

        this.progressTicks = machine.progressTicks;
        this.fuelBurnTicks = machine.fuelBurnTicks;
        this.progressMaxTicks = machine.progressMaxTicks;
        this.fuelMaxBurnTicks = machine.fuelMaxBurnTicks;
    }

    public void updateClientProgressBar(int id, int value) {
        if (id == 0) {
            machine.progressTicks = value;
        }

        if (id == 1) {
            machine.fuelBurnTicks = value;
        }

        if (id == 2) {
            machine.progressMaxTicks = value;
        }

        if (id == 3) {
            machine.fuelMaxBurnTicks = value;
        }

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer1) {
        return true;
    }
}
