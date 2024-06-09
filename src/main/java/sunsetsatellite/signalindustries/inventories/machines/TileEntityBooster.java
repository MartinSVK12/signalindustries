package sunsetsatellite.signalindustries.inventories.machines;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockFluid;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.Connection;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.Vec3f;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidItemContainer;
import sunsetsatellite.catalyst.fluids.impl.tiles.TileEntityFluidPipe;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;
import sunsetsatellite.signalindustries.interfaces.IActiveForm;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.util.IOPreview;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TileEntityBooster extends TileEntityFluidItemContainer implements IHasIOPreview, IActiveForm {

    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;
    public int efficiency = 1;
    public int speedMultiplier = 1;
    public int cost = 160;
    public Random random = new Random();
    public IOPreview preview = IOPreview.NONE;
    public Tier tier = Tier.REINFORCED;

    public TileEntityBooster(){
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 4000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add((BlockFluid) SIBlocks.energyFlowing);
        itemContents = new ItemStack[1];
        //acceptedFluids.get(1).add((BlockFluid) Block.fluidWaterFlowing);
    }
    @Override
    public String getInvName() {
        return "Dilithium Booster";
    }

    @Override
    public void tick() {
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        extractFluids();
        if(getBlockType() != null){
            BlockContainerTiered block = (BlockContainerTiered) getBlockType();
            tier = block.tier;

        }
        boolean update = false;
        if(fuelBurnTicks > 0){
            fuelBurnTicks--;
        }
        if(!worldObj.isClientSide){
            /*if (progressTicks >= 0 && canProcess()){
                update = fuel();
            }*/
            if(isBurning() && canProcess()){
                if(progressTicks > 0){
                    progressTicks--;
                    Vec3f color = new Vec3f();
                    if(tier == Tier.BASIC){
                        color.x = 1.0f;
                    } else if (tier == Tier.REINFORCED) {
                        color.x = 1.0f;
                        color.z = 1.0f;
                    } else if (tier == Tier.AWAKENED) {
                        color.x = 1.0f;
                        color.y = 165f/255f;
                    }
                    SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,x+random.nextFloat(),y+random.nextFloat(),z+random.nextFloat(),0,0,0,1.0f, (float) color.x, (float) color.y, (float) color.z));
                    SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,x+random.nextFloat(),y+random.nextFloat(),z+random.nextFloat(),0,0,0,1.0f, (float) color.x, (float) color.y, (float) color.z));
                    int meta = worldObj.getBlockMetadata(x,y,z);
                    TileEntity tileEntity = Direction.getDirectionFromSide(meta).getTileEntity(worldObj,this);
                    if(tileEntity instanceof IBoostable){
                        if(tier == Tier.BASIC){
                            color.x = 1.0f;
                            color.y = 0.5f;
                            color.z = 0.5f;
                        } else if (tier == Tier.REINFORCED) {
                            color.x = 1.0f;
                            color.z = 0.5f;
                        } else if (tier == Tier.AWAKENED) {
                            color.x = 1.0f;
                            color.y = 165f/255f;
                            color.z = 0.5f;
                        }
                        SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,tileEntity.x+random.nextFloat(),tileEntity.y+random.nextFloat(),tileEntity.z+random.nextFloat(),0,0,0, 1.0f, (float) color.x, (float) color.y, (float) color.z));
                    }
                }
                if(progressTicks <= 0){
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

    }

    public int getProgressScaled(int paramInt) {
        return this.progressTicks * paramInt / progressMaxTicks;
    }

    public int getBurnTimeRemainingScaled(int paramInt) {
        if(this.fuelMaxBurnTicks == 0) {
            this.fuelMaxBurnTicks = 400;
        }
        return this.fuelBurnTicks * paramInt / this.fuelMaxBurnTicks;
    }

    public boolean fuel(){
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if(burn > 0 && canProcess() && fluidContents[0].amount >= cost){
            setFuel();
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= cost;
            if(fluidContents[0].amount == 0) {
                fluidContents[0] = null;
            }
            return true;
        }
        return false;
    }

    public void processItem(){
        if(canProcess() && progressTicks <= 0){
           setFuel();
            itemContents[0].stackSize -= 1;
            if(itemContents[0].stackSize <= 0){
                itemContents[0] = null;
            }
        }
    }

    public void setFuel(){
        if(canProcess() && progressTicks <= 0) {
            if (tier == Tier.BASIC) {
                if (itemContents[0].getItem() == Item.dustRedstone) {
                    progressMaxTicks = 20 * speedMultiplier;
                } else if (itemContents[0].itemID == Block.blockRedstone.id) {
                    progressMaxTicks = 200 * speedMultiplier;
                }
            } else {
                if (itemContents[0].getItem() == SIItems.dilithiumShard) {
                    progressMaxTicks = 300 * speedMultiplier;
                } else if (itemContents[0].itemID == SIBlocks.dilithiumBlock.id) {
                    progressMaxTicks = 3000 * speedMultiplier;
                }

            }
            progressTicks = progressMaxTicks;
        }
    }

    private boolean canProcess() {
        if(itemContents[0] == null) {
            return false;
        } else {
            if(tier == Tier.BASIC){
                return (itemContents[0].getItem() == Item.dustRedstone || itemContents[0].itemID == Block.blockRedstone.id) && itemContents[0].stackSize > 0;
            } else {
                return (itemContents[0].getItem() == SIItems.dilithiumShard || itemContents[0].itemID == SIBlocks.dilithiumBlock.id) && itemContents[0].stackSize > 0;
            }

        }
        /*if(itemContents[0] == null) {
            return false;
        } else {
            ArrayList<Object> list = new ArrayList<>();
            list.add(this.fluidContents[1]);
            list.add(this.itemContents[0]);
            list.add(this.itemContents[1]);
            ItemStack stack = recipes.getResult(list);
            return stack != null && (itemContents[2] == null || (itemContents[2].isItemEqual(stack) && (itemContents[2].stackSize < getInventoryStackLimit() && itemContents[2].stackSize < itemContents[2].getMaxStackSize() || itemContents[2].stackSize < stack.getMaxStackSize())));
        }*/
    }

    @Override
    public boolean isBurning(){
        return fuelBurnTicks > 0;
    }

    public void extractFluids(){
        for (Map.Entry<Direction, Connection> e : fluidConnections.entrySet()) {
            Direction dir = e.getKey();
            Connection connection = e.getValue();
            TileEntity tile = dir.getTileEntity(worldObj,this);
            if (tile instanceof TileEntityFluidPipe) {
                pressurizePipes((TileEntityFluidPipe) tile, new ArrayList<>());
                moveFluids(dir, (TileEntityFluidPipe) tile);
                ((TileEntityFluidPipe) tile).rememberTicks = 100;
            }
        }
    }

    public void pressurizePipes(TileEntityFluidPipe pipe, ArrayList<HashMap<String,Integer>> already){
        pipe.isPressurized = true;
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj,pipe);
            if (tile instanceof TileEntityFluidPipe) {
                for (HashMap<String, Integer> V2 : already) {
                    if (V2.get("x") == tile.x && V2.get("y") == tile.y && V2.get("z") == tile.z) {
                        return;
                    }
                }
                HashMap<String,Integer> list = new HashMap<>();
                list.put("x",tile.x);
                list.put("y",tile.y);
                list.put("z",tile.z);
                already.add(list);
                ((TileEntityFluidPipe) tile).isPressurized = true;
                pressurizePipes((TileEntityFluidPipe) tile,already);
            }
        }
    }

    public void unpressurizePipes(TileEntityFluidPipe pipe,ArrayList<HashMap<String,Integer>> already){
        pipe.isPressurized = false;
        for (Direction dir : Direction.values()) {
            TileEntity tile = dir.getTileEntity(worldObj,pipe);
            if (tile instanceof TileEntityFluidPipe) {
                for (HashMap<String, Integer> V2 : already) {
                    if (V2.get("x") == tile.x && V2.get("y") == tile.y && V2.get("z") == tile.z) {
                        return;
                    }
                }
                HashMap<String,Integer> list = new HashMap<>();
                list.put("x",tile.x);
                list.put("y",tile.y);
                list.put("z",tile.z);
                already.add(list);
                ((TileEntityFluidPipe) tile).isPressurized = false;
                unpressurizePipes((TileEntityFluidPipe) tile,already);
            }
        }
    }

    @Override
    public void writeToNBT(CompoundTag nBTTagCompound1) {
        super.writeToNBT(nBTTagCompound1);
        nBTTagCompound1.putShort("BurnTime", (short)this.fuelBurnTicks);
        nBTTagCompound1.putShort("ProcessTime", (short)this.progressTicks);
        nBTTagCompound1.putShort("MaxBurnTime", (short)this.fuelMaxBurnTicks);
        nBTTagCompound1.putInt("MaxProcessTime",this.progressMaxTicks);
    }

    @Override
    public void readFromNBT(CompoundTag tag) {
        super.readFromNBT(tag);
        fuelBurnTicks = tag.getShort("BurnTime");
        progressTicks = tag.getShort("ProcessTime");
        progressMaxTicks = tag.getInteger("MaxProcessTime");
        fuelMaxBurnTicks = tag.getShort("MaxBurnTime");

    }


    @Override
    public IOPreview getPreview() {
        return preview;
    }

    @Override
    public void setPreview(IOPreview preview) {
        this.preview = preview;
    }
}
