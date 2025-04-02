package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicRail;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.logic.RailDirection;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIBlocks;

import static net.minecraft.core.block.BlockLogicRail.MASK_POWERED;

@Mixin(value = BlockLogicRail.class,remap = false)
public abstract class BlockLogicRailMixin extends BlockLogic {

    private BlockLogicRailMixin(Block<?> block, Material material) {
        super(block, material);
    }

    @Shadow public abstract RailDirection getRailDirection(WorldSource world, int x, int y, int z);

    @Shadow protected abstract boolean isConnectedPoweredRail1(World world, int x, int y, int z, boolean forward, int distance);

    @Inject(method = "isConnectedPoweredRail2", at = @At("HEAD"), cancellable = true)
    private void isConnectedPoweredRail2(World world, int x, int y, int z, boolean forward, int distance, Axis axis, CallbackInfoReturnable<Boolean> cir) {
        int blockId = world.getBlockId(x, y, z);
        if(blockId == SIBlocks.dilithiumRail.id()) {
            int meta = world.getBlockMetadata(x, y, z);
            boolean isPoweredFlagSet = (meta & MASK_POWERED) != 0;
            RailDirection direction = getRailDirection(world, x, y, z);

            // Return false for perpendicular powered rails
            if(axis == Axis.X && (
                    direction == RailDirection.STRAIGHT_NS ||
                            direction == RailDirection.SLOPE_N ||
                            direction == RailDirection.SLOPE_S)) {
                cir.setReturnValue(false);
                return;
            }
            if(axis == Axis.Z && (
                    direction == RailDirection.STRAIGHT_EW ||
                            direction == RailDirection.SLOPE_E ||
                            direction == RailDirection.SLOPE_W)) {
                cir.setReturnValue(false);
                return;
            }

            if(isPoweredFlagSet) {
                // If rail powered return true
                if(world.hasNeighborSignal(x, y, z) || world.hasNeighborSignal(x, y + 1, z)) {
                    cir.setReturnValue(true);
                    return;
                } else { // else check 1 block further down if powered
                    cir.setReturnValue(isConnectedPoweredRail1(world, x, y, z, forward, distance + 1));
                    return;
                }
            }
        }
    }

    @Inject(method = "onNeighborBlockChange", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/Blocks;getBlock(I)Lnet/minecraft/core/block/Block;",shift = At.Shift.AFTER), cancellable = true)
    public void onNeighborBlockChange(World world, int x, int y, int z, int blockId, CallbackInfo ci) {
        int meta = world.getBlockMetadata(x, y, z);
        boolean isPoweredFlagSet = (meta & MASK_POWERED) != 0;
        RailDirection railDirection = getRailDirection(world, x, y, z);

        if(this.block == SIBlocks.dilithiumRail) {
            boolean gettingPower =
                    world.hasNeighborSignal(x, y, z) ||
                            world.hasNeighborSignal(x, y + 1, z) ||
                            isConnectedPoweredRail1(world, x, y, z, true, 0) ||
                            isConnectedPoweredRail1(world, x, y, z, false, 0);

            boolean changedMeta = false;
            if(gettingPower && !isPoweredFlagSet) {
                // Set powered bit
                world.setBlockMetadataWithNotify(x, y, z, railDirection.meta | MASK_POWERED);
                changedMeta = true;
            } else if(!gettingPower && isPoweredFlagSet) {
                // Remove powered bit
                world.setBlockMetadataWithNotify(x, y, z, railDirection.meta);
                changedMeta = true;
            }

            if(changedMeta) {
                world.notifyBlocksOfNeighborChange(x, y - 1, z, id());
                if(railDirection.isSloped()) {
                    world.notifyBlocksOfNeighborChange(x, y + 1, z, id());
                }
            }

            ci.cancel();
        }
    }
}
