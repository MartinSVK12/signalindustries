package sunsetsatellite.signalindustries.misc.powersuit;

import net.minecraft.src.BlockFluid;
import net.minecraft.src.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.ItemInventoryFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerSuit;
import sunsetsatellite.signalindustries.util.NBTHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class InventoryPowerSuit extends ItemInventoryFluid {
    public InventoryPowerSuit(ItemStack item) {
        super(item);
        if(item.getItem() instanceof ItemSignalumPowerSuit){
            ItemSignalumPowerSuit armorPiece = (ItemSignalumPowerSuit) item.getItem();
            switch (armorPiece.armorPiece){
                case 0:
                    fluidContents = new FluidStack[0];
                    fluidCapacity = new int[0];
                    contents = new ItemStack[1];
                    break;
                case 1:
                    fluidContents = new FluidStack[1];
                    //fluidContents[0] = new FluidStack((BlockFluid) SignalIndustries.energyFlowing,8000);
                    fluidCapacity = new int[1];
                    fluidCapacity[0] = 32000;
                    contents = new ItemStack[8];
                    break;
                case 2:
                case 3:
                    fluidContents = new FluidStack[0];
                    fluidCapacity = new int[0];
                    contents = new ItemStack[2];
                    break;
            }
            this.acceptedFluids = new ArrayList<>(this.fluidContents.length);
            for (int i = 0; i < fluidContents.length; i++) {
                ArrayList<BlockFluid> list = new ArrayList<>();
                list.add((BlockFluid) SignalIndustries.energyFlowing);
                acceptedFluids.add(list);
            }
            readFromNBT();
        }
    }

    public boolean isEmpty(){
        return Arrays.stream(contents).allMatch(Objects::isNull);
    }

    public void readFromNBT(){
        NBTHelper.loadInvFromNBT(item,this,contents.length,fluidContents.length);
    }

    public void saveToNBT(){
        NBTHelper.saveInvToNBT(item,this);
    }
}
