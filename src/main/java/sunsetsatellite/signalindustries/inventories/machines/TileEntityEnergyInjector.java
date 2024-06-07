package sunsetsatellite.signalindustries.inventories.machines;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.fluids.api.IItemFluidContainer;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;
import sunsetsatellite.signalindustries.interfaces.IInjectable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;

public class TileEntityEnergyInjector extends TileEntityTieredMachineBase {

    public int injectSpeed = 5;

    public TileEntityEnergyInjector(){
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[1];
        fluidCapacity[0] = 4000;
        acceptedFluids.get(0).add((BlockFluid) SIBlocks.energyFlowing);
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
                if(stack.getItem() instanceof IItemFluidContainer){
                    IItemFluidContainer item = (IItemFluidContainer) getStackInSlot(0).getItem();
                    if(item.canFill(stack)){
                        ItemStack itemStack = item.fill(getFluidInSlot(0),stack,this,injectSpeed);
                        if(itemStack != null){
                            setInventorySlotContents(0,itemStack);
                        }
                    }
                } else if (stack.getItem() instanceof IInjectable){
                    IInjectable item = (IInjectable) getStackInSlot(0).getItem();
                    if(item.canFill(stack)){
                        item.fill(getFluidInSlot(0),stack,this,injectSpeed);
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
        ItemStack stack = getStackInSlot(0);
        if (getFluidInSlot(0) != null
                && getFluidInSlot(0).amount >= transferSpeed
                && stack != null
                && (stack.getItem() instanceof IItemFluidContainer || stack.getItem() instanceof IInjectable))
        {
            if(stack.getItem() instanceof IItemFluidContainer){
                return ((IItemFluidContainer) stack.getItem()).canFill(stack);
            } else {
                return ((IInjectable) stack.getItem()).canFill(stack);
            }
        } else return false;
    }
}
