package sunsetsatellite.signalindustries.tiles;

import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredEnergyGenerator;

public class TileEntitySignalumDynamo extends TileEntityTieredEnergyGenerator {

    public int cost;

    public TileEntitySignalumDynamo() {
        cost = 5;
        itemContents = new ItemStack[2];
        fluidContents = new FluidStack[1];
        fluidCapacity[0] = 4000;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        capacity = 10000;
        maxReceive = 250;
        maxProvide = 250;
    }

    @Override
    public void tick() {
        super.tick();
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        extractFluids();
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }

        if (!worldObj.isClientSide) {
            if (isBurning() && canProcess()) {
                generateEnergy(25);
            } else if (canProcess()) {
                fuel();
                if (fuelBurnTicks > 0) {
                    fuelBurnTicks++;
                }
            }
        }

        /*if(getItem(1) != null && getItem(1).getItem() instanceof IEnergyItem){
            ItemStack stack = getItem(1);
            provide(stack,getMaxProvide(),false);
            setChanged();
        }
        if(getItem(0) != null && getItem(0).getItem() instanceof IEnergyItem) {
            ItemStack stack = getItem(0);
            receive(stack,getMaxReceive(),false);
            setChanged();
        }*/

    }

    public boolean fuel() {
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if (burn > 0 && canProcess() && fluidContents[0].amount >= cost) {
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= cost;
            if (fluidContents[0].amount == 0) {
                fluidContents[0] = null;
            }
            return true;
        }
        return false;
    }

    private boolean canProcess() {
        if (fluidContents[0] == null) {
            return false;
        } else {
            return fluidContents[0].fluid == SIFluids.ENERGY && fluidContents[0].amount >= cost && getEnergy() < getCapacity();
        }
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.dynamo";
    }
}
