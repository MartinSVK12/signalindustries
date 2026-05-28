package sunsetsatellite.signalindustries.tiles.machines;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.interfaces.IBooster;
import sunsetsatellite.signalindustries.interfaces.IHasIOPreview;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredContainer;
import sunsetsatellite.signalindustries.util.IO;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.Random;

public class TileEntityBooster extends TileEntityTieredContainer implements IHasIOPreview, IBooster {

    public int fuelBurnTicks = 0;
    public int fuelMaxBurnTicks = 0;
    public int progressTicks = 0;
    public int progressMaxTicks = 200;
    public int efficiency = 1;
    public int speedMultiplier = 1;
    public int cost = 40;
    public Random random = new Random();
    public IO preview = IO.NONE;
    public TickTimer IOPreviewTimer = new TickTimer(this, this::disableIOPreview, 20, false);

    @Override
    public void disableIOPreview() {
        preview = IO.NONE;
    }

    @Override
    public void setTemporaryIOPreview(IO preview, int ticks) {
        IOPreviewTimer.value = ticks;
        IOPreviewTimer.max = ticks;
        IOPreviewTimer.unpause();
        this.preview = preview;
    }

    public TileEntityBooster() {
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = 4000;
        for (FluidStack ignored : fluidContents) {
            acceptedFluids.add(new ArrayList<>());
        }
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        itemContents = new ItemStack[1];
        //acceptedFluids.get(1).add((BlockFluid) Block.fluidWaterFlowing);
    }

    @Override
    public void tick() {
        worldObj.markBlockDirty(tilePos);
        IOPreviewTimer.tick();
        extractFluids();
        boolean update = false;
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (!worldObj.isClientSide) {
            /*if (progressTicks >= 0 && canProcess()){
                update = fuel();
            }*/
            if (isBurning() && canProcess()) {
                if (progressTicks > 0) {
                    progressTicks--;
                    Vec3f color = new Vec3f();
                    if (tier == Tier.BASIC) {
                        color.x = 1.0f;
                    } else if (tier == Tier.REINFORCED) {
                        color.x = 1.0f;
                        color.z = 1.0f;
                    } else if (tier == Tier.AWAKENED) {
                        color.x = 1.0f;
                        color.y = 165f / 255f;
                    }
                    //SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,x+random.nextFloat(),y+random.nextFloat(),z+random.nextFloat(),0,0,0,1.0f, (float) color.x, (float) color.y, (float) color.z));
                    //SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,x+random.nextFloat(),y+random.nextFloat(),z+random.nextFloat(),0,0,0,1.0f, (float) color.x, (float) color.y, (float) color.z));
                    int meta = worldObj.getBlockData(tilePos);
                    TileEntity tileEntity = Direction.getDirectionFromSide(meta).getTileEntity(worldObj, this);
                    if (tileEntity instanceof IBoostable) {
                        if (tier == Tier.BASIC) {
                            color.x = 1.0f;
                            color.y = 0.5f;
                            color.z = 0.5f;
                        } else if (tier == Tier.REINFORCED) {
                            color.x = 1.0f;
                            color.z = 0.5f;
                        } else if (tier == Tier.AWAKENED) {
                            color.x = 1.0f;
                            color.y = 165f / 255f;
                            color.z = 0.5f;
                        }
                        //SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj,tileEntity.x+random.nextFloat(),tileEntity.y+random.nextFloat(),tileEntity.z+random.nextFloat(),0,0,0, 1.0f, (float) color.x, (float) color.y, (float) color.z));
                    }
                }
                if (progressTicks <= 0) {
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

    }

    public int getProgressScaled(int paramInt) {
        return this.progressTicks * paramInt / progressMaxTicks;
    }

    public int getBurnTimeRemainingScaled(int paramInt) {
        if (this.fuelMaxBurnTicks == 0) {
            this.fuelMaxBurnTicks = 400;
        }
        return this.fuelBurnTicks * paramInt / this.fuelMaxBurnTicks;
    }

    public boolean fuel() {
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[0]);
        if (burn > 0 && canProcess() && fluidContents[0].amount >= cost) {
            setFuel();
            fuelMaxBurnTicks = fuelBurnTicks = burn;
            fluidContents[0].amount -= cost;
            if (fluidContents[0].amount == 0) {
                fluidContents[0] = null;
            }
            return true;
        }
        return false;
    }

    public void processItem() {
        if (canProcess() && progressTicks <= 0) {
            setFuel();
            itemContents[0].stackSize -= 1;
            if (itemContents[0].stackSize <= 0) {
                itemContents[0] = null;
            }
            progressTicks = progressMaxTicks;
        }
    }

    public void setFuel() {
        if (canProcess() && progressTicks <= 0) {
            if (tier == Tier.BASIC) {
                if (itemContents[0].getItem().equals(Items.DUST_REDSTONE)) {
                    progressMaxTicks = 150 * speedMultiplier;
                } else if (itemContents[0].itemID == Blocks.BLOCK_REDSTONE.id()) {
                    progressMaxTicks = 1500 * speedMultiplier;
                }
            } else {
                if (itemContents[0].getItem().equals(SIItems.dilithiumShard)) {
                    progressMaxTicks = 300 * speedMultiplier;
                } else if (itemContents[0].itemID == SIBlocks.dilithiumBlock.id()) {
                    progressMaxTicks = 3000 * speedMultiplier;
                }
            }
        }
    }

    private boolean canProcess() {
        if (itemContents[0] == null) {
            return false;
        } else {
            if (tier == Tier.BASIC) {
                return (itemContents[0].getItem().equals(Items.DUST_REDSTONE) || itemContents[0].itemID == Blocks.BLOCK_REDSTONE.id()) && itemContents[0].stackSize > 0;
            } else {
                return (itemContents[0].getItem().equals(SIItems.dilithiumShard) || itemContents[0].itemID == SIBlocks.dilithiumBlock.id()) && itemContents[0].stackSize > 0;
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
            return stack != null && (itemContents[2] == null || (itemContents[2].isItemEqual(stack) && (itemContents[2].stackSize < getMaxStackSize() && itemContents[2].stackSize < itemContents[2].getMaxStackSize() || itemContents[2].stackSize < stack.getMaxStackSize())));
        }*/
    }

    @Override
    public boolean isBurning() {
        return fuelBurnTicks > 0;
    }

    @Override
    public boolean isDisabled() {
        //TODO:
        return false;
    }

	@Override
	public void writeAdditionalData(@NonNull CompoundTag tag) {
		super.writeAdditionalData(tag);
		tag.putShort("BurnTime", (short) this.fuelBurnTicks);
		tag.putShort("ProcessTime", (short) this.progressTicks);
		tag.putShort("MaxBurnTime", (short) this.fuelMaxBurnTicks);
		tag.putInt("MaxProcessTime", this.progressMaxTicks);
	}

	@Override
	public void readAdditionalData(@NonNull CompoundTag tag) {
		super.readAdditionalData(tag);
		fuelBurnTicks = tag.getShort("BurnTime");
		progressTicks = tag.getShort("ProcessTime");
		progressMaxTicks = tag.getInteger("MaxProcessTime");
		fuelMaxBurnTicks = tag.getShort("MaxBurnTime");
	}


    @Override
    public IO getPreview() {
        return preview;
    }

    @Override
    public void setPreview(IO preview) {
        this.preview = preview;
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.booster";
    }

	@Override
	public void sort() {

	}
}
