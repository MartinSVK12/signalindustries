package sunsetsatellite.signalindustries.tiles.machines;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.Block;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.fluids.util.FluidStack;
import sunsetsatellite.catalyst.multiblocks.StructureSaver;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIFluids;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IBoostable;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import turniplabs.halplibe.helper.EnvironmentHelper;

import java.util.ArrayList;
import java.util.UUID;

public class TileEntityEncapsulator extends TileEntityTieredMachineBase implements IBoostable {
    public BlockInstance originMarker = null;
    public BlockInstance heightMarker = null;
    public BlockInstance widthMarker = null;
    public BlockInstance depthMarker = null;
    public ArrayList<BlockInstance> structure = new ArrayList<>();
    public Vec3i size = new Vec3i();

    public int energySlot;
    public int cost = 0;
    public int duration = 200;

    public State state = State.NONE;

    public enum State {
        NONE,
        STORING,
        CUTTING
    }

    @Override
    public String getNameTranslationKey() {
        return "container.signalindustries.encapsulator";
    }

    public TileEntityEncapsulator() {
        itemContents = new ItemStack[2];
        fluidContents = new FluidStack[1];
        fluidCapacity = new int[1];
        fluidCapacity[0] = (Short.MAX_VALUE * 2) + 1;
        acceptedFluids.get(0).add(SIFluids.ENERGY);
        energySlot = 0;
    }

    public void work() {
        if (EnvironmentHelper.isClientWorld()) return;
        boolean update = false;
        if (fuelBurnTicks > 0) {
            fuelBurnTicks--;
        }
        if (areAllInputsNull()) {
            state = State.NONE;
            progressTicks = 0;
        } else if (canProcess()) {
            progressMaxTicks = getProgressDuration(duration);
        }
        if (!worldObj.isClientSide) {
            if (progressTicks == 0 && canProcess()) {
                update = fuel();
            }
            if (isBurning() && canProcess()) {
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
    }

    public void processItem() {
        ItemStack item = itemContents[0];
        if (item != null && (item.getItem().equals(SIItems.blueprint) || item.getItem().equals(SIItems.goldprint)) && worldObj != null) {
            itemContents[0] = null;
            itemContents[1] = item;
            if (!structure.isEmpty()) {
                CompoundTag data = StructureSaver.serialize("structure", structure, state == State.CUTTING);
                UUID id = StructureSaver.save(data, worldObj);
                if (id == null) return;
                item.getData().putString("structure", id.toString());
                if (state == State.CUTTING) {
                    structure.forEach(B -> {
                        if (B.block.getHardness() != -1) {
                            worldObj.setBlock(B.pos.x, B.pos.y, B.pos.z, 0);
                        }
                    });
                }
            }
        }
        state = State.NONE;
    }

    public boolean areAllInputsNull() {
        return itemContents[0] == null;
    }

    public boolean fuel() {
        int burn = SignalIndustries.getEnergyBurnTime(fluidContents[energySlot]);
        if (burn > 0 && canProcess() && fuelBurnTicks <= 0) {
            if (fluidContents[energySlot].amount >= cost) {
                progressMaxTicks = getProgressDuration(duration);
                fuelMaxBurnTicks = fuelBurnTicks = burn;
                fluidContents[energySlot].amount -= cost;
                if (fluidContents[energySlot].amount == 0) {
                    fluidContents[energySlot] = null;
                }
                return true;
            }
        }
        return false;
    }

    public boolean canProcess() {
        if (itemContents[0] == null) return false;
        if (itemContents[1] != null) return false;
        if (state == State.NONE) return false;
        if (state == State.STORING) {
            return itemContents[0].getItem().equals(SIItems.blueprint);
        }
        if (state == State.CUTTING) {
            return itemContents[0].getItem().equals(SIItems.goldprint);
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (worldObj == null) return;
        worldObj.markBlocksDirty(x, y, z, x, y, z);
        extractFluids();
        if (!canProcess() && state != State.NONE) state = State.NONE;
        Block<?> block = getBlock();
        if (block != null) {
            if (!disabled) work();
        }
        Direction side = Direction.getDirectionFromSide(getBlockMeta()).getOpposite();
        if (heightMarker == null || widthMarker == null || depthMarker == null || originMarker == null) {
            Block<?> originMarker = side.getBlock(worldObj, this);
            if (originMarker == SIBlocks.basicMarker) {
                this.originMarker = new BlockInstance(originMarker, new Vec3i(x, y, z).add(side.getVec()), -1, null);
                int x = this.originMarker.pos.x;
                int y = this.originMarker.pos.y;
                int z = this.originMarker.pos.z;
                Vec3i origin = new Vec3i(x, y, z);
                for (int i = 0; i < 255; i++) {
                    Vec3i pos = new Vec3i(x, i, z);
                    if (origin.equals(pos)) continue;
                    if (worldObj.getBlock(x, i, z) == originMarker) {
                        heightMarker = new BlockInstance(pos.getBlock(worldObj), pos, -1, null);
                        break;
                    }
                }
                for (int i = -64; i <= 64; i++) {
                    Vec3i heightPos = new Vec3i(x + i, y, z);
                    Vec3i widthPos = new Vec3i(x, y, z + i);
                    if (heightPos.equals(origin) || widthPos.equals(origin)) continue;
                    if (worldObj.getBlock(x + i, y, z) == originMarker) {
                        depthMarker = new BlockInstance(heightPos.getBlock(worldObj), heightPos, -1, null);
                    }
                    if (worldObj.getBlock(x, y, z + i) == originMarker) {
                        widthMarker = new BlockInstance(widthPos.getBlock(worldObj), widthPos, -1, null);
                    }
                    if (depthMarker != null && widthMarker != null) {
                        break;
                    }
                }
                if (!areMarkersValid()) {
                    reset();
                }
            }
        } else {
            if (areMarkersValid()) {
                structure.clear();
                int ox = originMarker.pos.x;
                int oy = originMarker.pos.y;
                int oz = originMarker.pos.z;

                int hy = heightMarker.pos.y;
                int dx = depthMarker.pos.x;
                int wz = widthMarker.pos.z;

                int offsetX = ox - dx;
                int offsetY = oy - hy;
                int offsetZ = oz - wz;

                int startX = ox + 1;
                int endX = ox - offsetX;

                if (endX < startX) {
                    int temp = endX;
                    endX = startX;
                    startX = temp;
                }

                int startZ = oz + 1;
                int endZ = oz - offsetZ;
                if (endZ < startZ) {
                    int temp = endZ;
                    endZ = startZ;
                    startZ = temp;
                }

                int startY = oy + 1;
                int endY = oy - offsetY;
                if (endY < startY) {
                    int temp = endY;
                    endY = startY;
                    startY = temp;
                }

                int centerOffsetX = (endX - startX) / 2;
                int centerOffsetY = (endY - startY) / 2;
                int centerOffsetZ = (endZ - startZ) / 2;

                int centerX = startX + centerOffsetX;
                int centerY = startY + centerOffsetY;
                int centerZ = startZ + centerOffsetZ;

                size = new Vec3i(Math.abs(offsetX), Math.abs(offsetY), Math.abs(offsetZ));

                for (int i = startX; i < endX; i++) {
                    for (int j = startY; j < endY; j++) {
                        for (int k = startZ; k < endZ; k++) {
                            if (worldObj.getBlock(i, j, k) == null) continue;
                            if (worldObj.getBlock(i, j, k).getHardness() == -1) continue;

                            structure.add(new BlockInstance(worldObj.getBlock(i, j, k), new Vec3i(i - centerX, j - centerY, k - centerZ), worldObj.getBlockMetadata(i, j, k), worldObj.getTileEntity(i, j, k)));
                        }
                    }
                }
            } else {
                reset();
            }
        }
    }

    public boolean areMarkersValid() {
        if (widthMarker == null || heightMarker == null || depthMarker == null || originMarker == null || worldObj == null)
            return false;
        return heightMarker.exists(worldObj) && widthMarker.exists(worldObj) && depthMarker.exists(worldObj) && originMarker.exists(worldObj);
    }

    private void reset() {
        heightMarker = null;
        widthMarker = null;
        depthMarker = null;
        originMarker = null;
        structure.clear();
        size = new Vec3i();
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        super.buttonClicked(id, button, channel);

        if (id == 2) {
            state = State.CUTTING;
        } else if (id == 3) {
            state = State.STORING;
        }

    }
}
