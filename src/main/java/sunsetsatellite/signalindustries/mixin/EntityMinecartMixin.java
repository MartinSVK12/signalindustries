package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityColorParticleFX;

@Debug(
        export = true
)
@Mixin(
        value = EntityMinecart.class,
        remap = false
)
public abstract class EntityMinecartMixin extends Entity implements IInventory {
    private int i1;

    public EntityMinecartMixin(World world) {
        super(world);
    }

    private int l = 0;
    private int i = 0;
    private int j = 0;
    private int k = 0;

    @Inject(
            method = "onUpdate2",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/core/block/BlockRail;getIsPowered()Z", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void collectLocals(CallbackInfo ci, int i, int j, int k, double d2, boolean flag, double d5, int l, Vec3d vec3d, int i1, boolean flag1, boolean flag2){
        this.l = l;
        this.i1 = i1;
        this.i = i;
        this.j = j;
        this.k = k;
        if(l == SignalIndustries.dilithiumRail.id && (i1 & 0x8) != 0) {
            for (int m = 0; m < 24; m++) {
                SignalIndustries.spawnParticle(new EntityColorParticleFX(world, x, y, z, 0, 0, 0, 1.0f, 1.0f, 0.0f, 1.0f));
            }
        }
    }

    @Inject(
            method = "onUpdate2",
            at = @At(value = "INVOKE",target = "Ljava/lang/Math;sqrt(D)D",shift = At.Shift.BEFORE,ordinal = 5),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void dilithiumRailBoost(CallbackInfo ci){
        if(l == SignalIndustries.dilithiumRail.id){
            double d31 = Math.sqrt(this.xd * this.xd + this.zd * this.zd);
            if (d31 > 0.01D) {
                double d32 = 0.12D;
                this.xd += this.xd / d31 * d32;
                this.zd += this.zd / d31 * d32;
            } else if (i1 == 1) {
                if (this.world.isBlockNormalCube(i - 1, j, k)) {
                    this.xd = 0.02D;
                } else if (this.world.isBlockNormalCube(i + 1, j, k)) {
                    this.xd = -0.02D;
                }
            } else if (i1 == 0) {
                if (this.world.isBlockNormalCube(i, j, k - 1)) {
                    this.zd = 0.02D;
                } else if (this.world.isBlockNormalCube(i, j, k + 1)) {
                    this.zd = -0.02D;
                }
            }
        }
    }

    @ModifyVariable(method = "onUpdate2", at = @At("STORE"), ordinal = 1)
    private boolean injected(boolean flag) {
        if(l == SignalIndustries.dilithiumRail.id || l == Block.railPowered.id){
            return ((i1 & 0x8) != 0);
        }
        return flag;
    }
}
