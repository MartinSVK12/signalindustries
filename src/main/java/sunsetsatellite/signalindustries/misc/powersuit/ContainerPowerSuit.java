package sunsetsatellite.signalindustries.misc.powersuit;





import sunsetsatellite.fluidapi.api.ContainerItemFluid;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.ItemInventoryFluid;
import sunsetsatellite.fluidapi.api.SlotFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.SlotAttachment;

public class ContainerPowerSuit extends ContainerItemFluid {
    public int xSize = 176;
    public int ySize = 166;

    public ContainerPowerSuit(IInventory inventoryPlayer, ItemInventoryFluid inv) {
        super(inventoryPlayer, inv);
        ItemArmor armorPiece = (ItemArmor) inv.item.getItem();
        switch (armorPiece.armorPiece){
            case 0:
                addSlot(new SlotAttachment(inv,0,xSize/2-8, ySize/3-20, AttachmentPoint.HEAD_TOP));
                break;
            case 1:
                addFluidSlot(new SlotFluid(inv,0,xSize/2-8, ySize/3-20));

                addSlot(new SlotAttachment(inv,0,xSize/2-8, ySize/3-44, AttachmentPoint.CORE_MODULE));

                addSlot(new SlotAttachment(inv,1,xSize/2-8,ySize/3+4, AttachmentPoint.CORE_BACK));

                addSlot(new SlotAttachment(inv,2,xSize/3-8, ySize/3-20,AttachmentPoint.ARM_FRONT));
                addSlot(new SlotAttachment(inv,3,xSize/3-20-8, ySize/3-20, AttachmentPoint.ARM_BACK));
                addSlot(new SlotAttachment(inv,4,xSize/3-40-8, ySize/3-20, AttachmentPoint.ARM_SIDE));

                addSlot(new SlotAttachment(inv,5,xSize-27, ySize/3-20, AttachmentPoint.ARM_SIDE));
                addSlot(new SlotAttachment(inv,6,xSize-20-27, ySize/3-20, AttachmentPoint.ARM_BACK));
                addSlot(new SlotAttachment(inv,7,xSize-40-27, ySize/3-20, AttachmentPoint.ARM_FRONT));
                break;
            case 2:
                addSlot(new SlotAttachment(inv,0,xSize/3-8, ySize/3-20,AttachmentPoint.LEG_SIDE));
                addSlot(new SlotAttachment(inv,1,xSize-40-27, ySize/3-20, AttachmentPoint.LEG_SIDE));
                break;
            case 3:
                addSlot(new SlotAttachment(inv,0,xSize/3-8, ySize/3-20,AttachmentPoint.BOOT_BACK));
                addSlot(new SlotAttachment(inv,1,xSize-40-27, ySize/3-20, AttachmentPoint.BOOT_BACK));
                break;
        }

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
    public void quickMoveItems(int i, EntityPlayer entityPlayer, boolean bl, boolean bl2) {

    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }
}
