package sunsetsatellite.signalindustries.inventories.machines;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachine;

public class TileEntityEnergyInjector extends TileEntityTieredMachine {

    public int injectSpeed = 5;

    public TileEntityEnergyInjector(){
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[1];
        fluidCapacity[0] = 4000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }

    @Override
    public String getInvName() {
        return "Energy Injector";
    }

    @Override
    public void tick() {
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        extractFluids();
        if(isBurning()){
            ItemStack stack = getStackInSlot(0);
            if(stack != null){
                IItemFluidContainer item = (IItemFluidContainer) getStackInSlot(0).getItem();
                if(item.canFill(stack)){
                    ItemStack itemStack = item.fill(getFluidInSlot(0),stack,this,injectSpeed);
                    if(itemStack != null){
                        setInventorySlotContents(0,itemStack);
                    }
                }
            }
            for (float i = 0; i < 0.5; i+=0.01f) {
                SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,x+0.5,y+i,z+0.5,0,0,0,1.0f,1.0f,0.0f,0.0f,2));
            }
        }
    }

    @Override
    public boolean isBurning() {
        return getFluidInSlot(0) != null
                && getFluidInSlot(0).amount >= transferSpeed
                && getStackInSlot(0) != null
                && getStackInSlot(0).getItem() instanceof IItemFluidContainer
                && ((IItemFluidContainer) getStackInSlot(0).getItem()).canFill(getStackInSlot(0));
    }
}
