package sunsetsatellite.signalindustries.blocks;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockRail;
import net.minecraft.core.block.entity.TileEntity;
import sunsetsatellite.signalindustries.util.RailLogic;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import sunsetsatellite.signalindustries.SignalIndustries;


public class BlockDilithiumRail extends BlockRail {

    public boolean isPowered;

    public BlockDilithiumRail(String key, int id, boolean isPowered) {
        super(key, id, isPowered);
        this.isPowered = isPowered;
        hasOverbright = true;
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world,x,y,z);
        this.onNeighborBlockChange(world, x, y, z, this.id);
    }

    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
        if (!world.isClientSide) {
            int i1 = world.getBlockMetadata(x, y, z);
            int j1 = i1;
            if (this.isPowered) {
                j1 = i1 & 7;
            }

            boolean flag = !world.canPlaceOnSurfaceOfBlock(x, y - 1, z);
            if (j1 == 2 && !world.canPlaceOnSurfaceOfBlock(x + 1, y, z)) {
                flag = true;
            }

            if (j1 == 3 && !world.canPlaceOnSurfaceOfBlock(x - 1, y, z)) {
                flag = true;
            }

            if (j1 == 4 && !world.canPlaceOnSurfaceOfBlock(x, y, z - 1)) {
                flag = true;
            }

            if (j1 == 5 && !world.canPlaceOnSurfaceOfBlock(x, y, z + 1)) {
                flag = true;
            }

            if (flag) {
                this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, world.getBlockMetadata(x, y, z), (TileEntity)null);
                world.setBlockWithNotify(x, y, z, 0);
            } else if (this.id == SignalIndustries.dilithiumRail.id) {
                boolean flag1 = world.isBlockIndirectlyGettingPowered(x, y, z) || world.isBlockIndirectlyGettingPowered(x, y + 1, z);
                flag1 = flag1 || this.func_27044_a(world, x, y, z, i1, true, 0) || this.func_27044_a(world, x, y, z, i1, false, 0);
                boolean flag2 = false;
                if (flag1 && (i1 & 8) == 0) {
                    world.setBlockMetadataWithNotify(x, y, z, j1 | 8);
                    flag2 = true;
                } else if (!flag1 && (i1 & 8) != 0) {
                    world.setBlockMetadataWithNotify(x, y, z, j1);
                    flag2 = true;
                }

                if (flag2) {
                    world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id);
                    if (j1 == 2 || j1 == 3 || j1 == 4 || j1 == 5) {
                        world.notifyBlocksOfNeighborChange(x, y + 1, z, this.id);
                    }
                }
            } else if (blockId > 0 && Block.blocksList[blockId].canProvidePower() && !this.isPowered && RailLogic.getNAdjacentTracks(new RailLogic(this, world, x, y, z)) == 3) {
                this.func_4031_h(world, x, y, z, false);
            }

        }
    }

    private void func_4031_h(World world, int i, int j, int k, boolean flag) {
        if (!world.isClientSide) {
            (new RailLogic(this, world, i, j, k)).func_792_a(world.isBlockIndirectlyGettingPowered(i, j, k), flag);
        }
    }


    private boolean func_27044_a(World world, int i, int j, int k, int l, boolean flag, int i1) {
        if (i1 >= 8) {
            return false;
        } else {
            int j1 = l & 7;
            boolean flag1 = true;
            switch (j1) {
                case 0:
                    if (flag) {
                        ++k;
                    } else {
                        --k;
                    }
                    break;
                case 1:
                    if (flag) {
                        --i;
                    } else {
                        ++i;
                    }
                    break;
                case 2:
                    if (flag) {
                        --i;
                    } else {
                        ++i;
                        ++j;
                        flag1 = false;
                    }

                    j1 = 1;
                    break;
                case 3:
                    if (flag) {
                        --i;
                        ++j;
                        flag1 = false;
                    } else {
                        ++i;
                    }

                    j1 = 1;
                    break;
                case 4:
                    if (flag) {
                        ++k;
                    } else {
                        --k;
                        ++j;
                        flag1 = false;
                    }

                    j1 = 0;
                    break;
                case 5:
                    if (flag) {
                        ++k;
                        ++j;
                        flag1 = false;
                    } else {
                        --k;
                    }

                    j1 = 0;
            }

            if (this.func_27043_a(world, i, j, k, flag, i1, j1)) {
                return true;
            } else {
                return flag1 && this.func_27043_a(world, i, j - 1, k, flag, i1, j1);
            }
        }
    }

    private boolean func_27043_a(World world, int i, int j, int k, boolean flag, int l, int i1) {
        int j1 = world.getBlockId(i, j, k);
        if (j1 == SignalIndustries.dilithiumRail.id) {
            int k1 = world.getBlockMetadata(i, j, k);
            int l1 = k1 & 7;
            if (i1 == 1 && (l1 == 0 || l1 == 4 || l1 == 5)) {
                return false;
            }

            if (i1 == 0 && (l1 == 1 || l1 == 2 || l1 == 3)) {
                return false;
            }

            if ((k1 & 8) != 0) {
                if (!world.isBlockIndirectlyGettingPowered(i, j, k) && !world.isBlockIndirectlyGettingPowered(i, j + 1, k)) {
                    return this.func_27044_a(world, i, j, k, k1, flag, l + 1);
                }

                return true;
            }
        }

        return false;
    }
}
