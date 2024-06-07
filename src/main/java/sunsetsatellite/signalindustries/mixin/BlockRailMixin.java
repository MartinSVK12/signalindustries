package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockRail;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.util.RailLogic;

@Debug(
        export = true
)

@Mixin(
        value = BlockRail.class,
        remap = false
)
public abstract class BlockRailMixin extends Block {
    @Shadow @Final private boolean isPowered;

    public BlockRailMixin(String key, int id, Material material) {
        super(key, id, material);
    }

    @Shadow protected abstract boolean func_27044_a(World world, int i, int j, int k, int l, boolean flag, int i1);

    @Shadow protected abstract void func_4031_h(World world, int i, int j, int k, boolean flag);

    @Inject(method = "isRailBlockAt", at = @At("HEAD"), cancellable = true)
    private static void isRailBlockAt(World world, int i, int j, int k, CallbackInfoReturnable<Boolean> cir) {
        int l = world.getBlockId(i, j, k);
        if(l == SIBlocks.dilithiumRail.id){
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isRailBlock", at = @At("HEAD"), cancellable = true)
    private static void isRailBlock(int i, CallbackInfoReturnable<Boolean> cir) {
        if(i == SIBlocks.dilithiumRail.id){
            cir.setReturnValue(true);
        }
    }

    @Inject(
            method = "onNeighborBlockChange",
            at = @At("HEAD"),
            cancellable = true
    )
    public void onNeighborBlockChange(World world, int i, int j, int k, int l, CallbackInfo ci) {
        if (this.id == SIBlocks.dilithiumRail.id) {
            if (world.isClientSide){
                ci.cancel();
            } else {
                int i1 = world.getBlockMetadata(i, j, k);
                int j1 = i1;
                if (this.isPowered)
                    j1 &= 0x7;
                boolean flag = !world.canPlaceOnSurfaceOfBlock(i, j - 1, k);
                if (j1 == 2 && !world.canPlaceOnSurfaceOfBlock(i + 1, j, k))
                    flag = true;
                if (j1 == 3 && !world.canPlaceOnSurfaceOfBlock(i - 1, j, k))
                    flag = true;
                if (j1 == 4 && !world.canPlaceOnSurfaceOfBlock(i, j, k - 1))
                    flag = true;
                if (j1 == 5 && !world.canPlaceOnSurfaceOfBlock(i, j, k + 1))
                    flag = true;
                if (flag) {
                    dropBlockWithCause(world, EnumDropCause.WORLD, i, j, k, world.getBlockMetadata(i, j, k),null);
                    world.setBlockWithNotify(i, j, k, 0);
                } else {
                    boolean flag1 = (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k));
                    flag1 = (flag1 || func_27044_a(world, i, j, k, i1, true, 0) || func_27044_a(world, i, j, k, i1, false, 0));
                    boolean flag2 = false;
                    if (flag1 && (i1 & 0x8) == 0) {
                        world.setBlockMetadataWithNotify(i, j, k, j1 | 0x8);
                        flag2 = true;
                    } else if (!flag1 && (i1 & 0x8) != 0) {
                        world.setBlockMetadataWithNotify(i, j, k, j1);
                        flag2 = true;
                    }
                    if (flag2) {
                        world.notifyBlocksOfNeighborChange(i, j - 1, k, this.id);
                        if (j1 == 2 || j1 == 3 || j1 == 4 || j1 == 5)
                            world.notifyBlocksOfNeighborChange(i, j + 1, k, this.id);
                    }
                }
            }
        }
    }

    @Inject(
            method = "func_27043_a",
            at = @At("HEAD"),
            cancellable = true
    )
    private void func_27043_a(World world, int i, int j, int k, boolean flag, int l, int i1, CallbackInfoReturnable<Boolean> cir) {
        int j1 = world.getBlockId(i, j, k);
        if (j1 == SIBlocks.dilithiumRail.id) {
            int k1 = world.getBlockMetadata(i, j, k);
            int l1 = k1 & 0x7;
            if (i1 == 1 && (l1 == 0 || l1 == 4 || l1 == 5)){
                cir.setReturnValue(false);
            } else if (i1 == 0 && (l1 == 1 || l1 == 2 || l1 == 3)){
                cir.setReturnValue(false);
            } else if ((k1 & 0x8) != 0) {
                if (world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k)){
                    cir.setReturnValue(true);
                } else {
                    cir.setReturnValue(func_27044_a(world, i, j, k, k1, flag, l + 1));
                }
            }
        }
    }

    @Inject(
            method = "func_4031_h",
            at = @At("HEAD"),
            cancellable = true
    )
    private void func_4031_h(World world, int i, int j, int k, boolean flag, CallbackInfo ci) {
        if (world.isClientSide){
            ci.cancel();
        } else {
            if(this.id == SIBlocks.dilithiumRail.id){
                (new RailLogic((BlockRail) ((Object)this), world, i, j, k)).func_792_a(world.isBlockIndirectlyGettingPowered(i, j, k), flag);
            }
        }
    }
}
