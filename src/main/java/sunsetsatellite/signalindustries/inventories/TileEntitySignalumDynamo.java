package sunsetsatellite.signalindustries.inventories;

import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.fluidapi.api.FluidStack;
import sunsetsatellite.fluidapi.api.IItemFluidContainer;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;

public class TileEntitySignalumDynamo extends TileEntityTieredMachine {

    public int injectSpeed = 5;

    public TileEntitySignalumDynamo(){
        itemContents = new ItemStack[1];
        fluidContents = new FluidStack[1];
        fluidCapacity[0] = 4000;
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
    }

    @Override
    public String getInvName() {
        return "Signalum Dynamo";
    }

    @Override
    public void updateEntity() {
        worldObj.markBlocksDirty(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
        extractFluids();
        if(isBurning()){
            ItemStack stack = getStackInSlot(0);
            if(stack != null){
                IItemFluidContainer item = (IItemFluidContainer) getStackInSlot(0).getItem();
                if(item.canFill(stack)){
                    ItemStack itemStack = item.fill(0,this,stack,injectSpeed);
                    if(itemStack != null){
                        setInventorySlotContents(0,itemStack);
                    }
                }
            }
            for (float i = 0; i < 0.5; i+=0.01f) {
                SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,xCoord+0.5,yCoord+i,zCoord+0.5,0,0,0,1.0f,1.0f,0.0f,0.0f,2));
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
