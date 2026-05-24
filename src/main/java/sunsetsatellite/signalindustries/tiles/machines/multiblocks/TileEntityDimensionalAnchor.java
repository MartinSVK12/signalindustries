package sunsetsatellite.signalindustries.tiles.machines.multiblocks;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.mixin.interfaces.ITileEntityInit;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IStabilizable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityStabilizer;

import java.util.ArrayList;
import java.util.List;

public class TileEntityDimensionalAnchor extends TileEntityTieredMachineBase implements IMultiblock, IStabilizable, ITileEntityInit {

    public MultiblockInstance multiblock;
    public List<TileEntityStabilizer> stabilizers = new ArrayList<>();
    public int cost;

    public TileEntityDimensionalAnchor() {
        progressMaxTicks = 6000;
        cost = 240;
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 8000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        itemContents = new ItemStack[1];
        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("dimensionalAnchor"));
    }

    @Override
    public void init(Block<?> block) {
        super.init(block);
        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("dimensionalAnchor"));
    }

    @Override
    public void tick() {
        if (multiblock == null) {
            return;
        }
        speedMultiplier = 1;
        extractFluids();
        stabilizers.clear();
        if (!multiblock.isValid()) {
            return;
        }
        Direction dir = Direction.getDirectionFromSide(getBlockMeta());
        ArrayList<BlockInstance> tileEntities = multiblock.data.getTileEntities(worldObj, new Vec3i(tilePos), dir);
        for (BlockInstance tileEntity : tileEntities) {
            if (tileEntity.tile instanceof TileEntityStabilizer) {
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
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (itemContents[0] == null) {
            progressTicks = 0;
        } else if (canProcess()) {
            progressMaxTicks = (int) (6000 / speedMultiplier);
        }
        if (!worldObj.isClientSide) {
            if (progressTicks == 0 && canProcess()) {
                update = fuel();
            }
            if (isBurning() && canProcess()) {
                for (float y1 = tilePos.y; y1 < 256; y1 += 0.1f) {
                    //SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,x+0.5,y1,z+0.5,0,0,0,1.0f,0.5f,0.0f,1.0f,16));
                }
                progressTicks++;
                if (progressTicks >= progressMaxTicks) {
                    progressTicks = 0;
                    processItem();
                    update = true;
                }
            } else if (canProcess()) {
                fuel();
                if (fuelBurnTicks > 0) {
                    fuelBurnTicks++;
                }
            }
        }

        if (update) {
            this.setChanged();
        }
        super.tick();
    }

    public boolean canProcess() {
        if (itemContents[0] == null) {
            return false;
        } else {
            for (TileEntityStabilizer stabilizer : stabilizers) {
                if (!stabilizer.canProcess()) {
                    if (progressTicks > 0) {
                        progressTicks--;
                    }
                    return false;
                }
            }
            return itemContents[0].getItem() == SIItems.warpOrb && !itemContents[0].getData().containsKey("position");
        }
    }

    public void processItem() {
        if (canProcess()) {
            ItemStack stack = itemContents[0];
            CompoundTag pos = new CompoundTag();
            pos.putInt("x", tilePos.x);
            pos.putInt("y", tilePos.y + 1);
            pos.putInt("z", tilePos.z);
            stack.getData().put("position", pos);
            stack.getData().putInt("dim", worldObj.dimension.id);
            //Minecraft.getMinecraft().//thePlayer.triggerAchievement(SIAchievements.ANCHOR);
        }
    }

    public boolean fuel() {
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if (burn > 0 && canProcess() && fluidContents[0].amount >= cost) {
            progressMaxTicks = (int) (6000 / speedMultiplier);//(itemContents[0].getItemData().getInteger("saturation") / speedMultiplier) == 0 ? 200 : (itemContents[0].getItemData().getInteger("saturation") / speedMultiplier);
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= cost;
            if (fluidContents[0].amount == 0) {
                fluidContents[0] = null;
            }
            return true;
        }
        return false;
    }

    @Override
    public MultiblockInstance getMultiblock() {
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
    public boolean isBurning() {
        return super.isBurning() && multiblock.isValid();
    }

    @Override
    public boolean isReady() {
        if (itemContents[0] == null) {
            return false;
        } else {
            return itemContents[0].getItem() == SIItems.warpOrb && !itemContents[0].getData().containsKey("Data");
        }
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.dimensionalAnchor";
    }
}
