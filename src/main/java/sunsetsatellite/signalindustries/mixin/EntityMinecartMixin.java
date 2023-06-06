package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.*;
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
            at = @At(value = "INVOKE",target = "Lnet/minecraft/src/BlockRail;getIsPowered()Z", shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void collectLocals(CallbackInfo ci, int i, int j, int k, double d2, boolean flag, double d5, int l, Vec3D vec3d, int i1, boolean flag1, boolean flag2){
        this.l = l;
        this.i1 = i1;
        this.i = i;
        this.j = j;
        this.k = k;
        if(l == SignalIndustries.dilithiumRail.blockID && (i1 & 0x8) != 0) {
            for (int m = 0; m < 24; m++) {
                SignalIndustries.spawnParticle(new EntityColorParticleFX(worldObj, posX, posY, posZ, 0, 0, 0, 1.0f, 1.0f, 0.0f, 1.0f));
            }
        }
    }

    @Inject(
            method = "onUpdate2",
            at = @At(value = "INVOKE",target = "Ljava/lang/Math;sqrt(D)D",shift = At.Shift.BEFORE,ordinal = 5),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void dilithiumRailBoost(CallbackInfo ci){
        if(l == SignalIndustries.dilithiumRail.blockID){
            double d31 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d31 > 0.01D) {
                double d32 = 0.12D;
                this.motionX += this.motionX / d31 * d32;
                this.motionZ += this.motionZ / d31 * d32;
            } else if (i1 == 1) {
                if (this.worldObj.isBlockNormalCube(i - 1, j, k)) {
                    this.motionX = 0.02D;
                } else if (this.worldObj.isBlockNormalCube(i + 1, j, k)) {
                    this.motionX = -0.02D;
                }
            } else if (i1 == 0) {
                if (this.worldObj.isBlockNormalCube(i, j, k - 1)) {
                    this.motionZ = 0.02D;
                } else if (this.worldObj.isBlockNormalCube(i, j, k + 1)) {
                    this.motionZ = -0.02D;
                }
            }
        }
    }

    @ModifyVariable(method = "onUpdate2", at = @At("STORE"), ordinal = 1)
    private boolean injected(boolean flag) {
        if(l == SignalIndustries.dilithiumRail.blockID || l == Block.railPowered.blockID){
            return ((i1 & 0x8) != 0);
        }
        return flag;
    }
}
