package sunsetsatellite.signalindustries.inventories;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;
import sunsetsatellite.signalindustries.interfaces.IStabilizable;

import java.util.ArrayList;
import java.util.List;

public class TileEntityDimensionalAnchor extends TileEntityTieredMachine implements IMultiblock, IStabilizable {

    public Multiblock multiblock;
    public List<TileEntityStabilizer> stabilizers = new ArrayList<>();
    public TileEntityDimensionalAnchor(){
        progressMaxTicks = 6000;
        cost = 240;
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 8000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);
        itemContents = new ItemStack[1];
        multiblock = Multiblock.multiblocks.get("dimensionalAnchor");
    }

    @Override
    public void tick() {
        speedMultiplier = 1;
        extractFluids();
        stabilizers.clear();
        Direction dir = Direction.getDirectionFromSide(getMovedData());
        ArrayList<BlockInstance> tileEntities = multiblock.getTileEntities(worldObj,new Vec3i(x,y,z),dir);
        for (BlockInstance tileEntity : tileEntities) {
            if(tileEntity.tile instanceof TileEntityStabilizer){
                ((TileEntityStabilizer) tileEntity.tile).connectedTo = this;
                stabilizers.add((TileEntityStabilizer) tileEntity.tile);
            }
        }
       /* for (Direction value : Direction.values()) {
            if(value == Direction.Y_NEG || value == Direction.Y_POS) continue;
            Vec3i v = value.getVec().multiply(2);
            Vec3i tileVec = new Vec3i(x,y,z);
            TileEntity tile = value.getTileEntity(worldObj,tileVec.add(v));
            if(tile instanceof TileEntityStabilizer){
                ((TileEntityStabilizer) tile).connectedTo = this;
                stabilizers.add((TileEntityStabilizer) tile);

            }
        }*/

        boolean update = false;
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(itemContents[0] == null){
            progressTicks = 0;
        } else if(canProcess()) {
            progressMaxTicks = 6000 / speedMultiplier;
        }
        if(!worldObj.isClientSide){
            if (progressTicks == 0 && canProcess()){
                update = fuel();
            }
            if(isBurning() && canProcess()){
                for (float y1 = y; y1 < 256; y1+=0.1f) {
                    SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,x+0.5,y1,z+0.5,0,0,0,1.0f,0.5f,0.0f,1.0f,16));
                }
                progressTicks++;
                if(progressTicks >= progressMaxTicks){
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            } else if(canProcess()){
                fuel();
                if(fuelBurnTicks > 0){
                    fuelBurnTicks++;
                }
            }
        }

        if(update) {
            this.onInventoryChanged();
        }
        super.tick();
    }

    public boolean canProcess() {
        if(itemContents[0] == null) {
            return false;
        } else {
            for (TileEntityStabilizer stabilizer : stabilizers) {
                if(!stabilizer.canProcess()){
                    if(progressTicks > 0){
                        progressTicks--;
                    }
                    return false;
                }
            }
            return itemContents[0].getItem() == SignalIndustries.warpOrb && !itemContents[0].getData().containsKey("position");
        }
    }

    public void processItem() {
        if (canProcess()) {
            ItemStack stack = itemContents[0];
            CompoundTag pos = new CompoundTag();
            pos.putInt("x",x);
            pos.putInt("y",y+1);
            pos.putInt("z",z);
            stack.getData().put("position",pos);
        }
    }

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if(burn > 0 && canProcess() && fluidContents[0].amount >= cost){
            progressMaxTicks = 6000 / speedMultiplier;//(itemContents[0].getItemData().getInteger("saturation") / speedMultiplier) == 0 ? 200 : (itemContents[0].getItemData().getInteger("saturation") / speedMultiplier);
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= cost;
            if(fluidContents[0].amount == 0) {
                fluidContents[0] = null;
            }
            return true;
        }
        return false;
    }

    @Override
    public Multiblock getMultiblock() {
        return multiblock;
    }

    @Override
    public int getProgressScaled(int paramInt) {
        return (int) ((((double) progressTicks / progressMaxTicks)) * paramInt);
    }

    @Override
    public boolean isActive() {
        return isBurning();
    }

    @Override
    public boolean isReady() {
        if(itemContents[0] == null) {
            return false;
        } else {
            return itemContents[0].getItem() == SignalIndustries.warpOrb && !itemContents[0].getData().containsKey("Data");
        }
    }
}
