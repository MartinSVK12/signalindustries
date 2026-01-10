package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.vehicle.EntityMinecart;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SIBlocks;

@Mixin(value = EntityMinecart.class, remap = false)
public abstract class EntityMinecartMixin extends Entity implements Container {
    private EntityMinecartMixin(@Nullable World world) {
        super(world);
    }

    @Inject(method = "motionIteration", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/BlockLogicRail;getRailDirection(Lnet/minecraft/core/world/WorldSource;III)Lnet/minecraft/core/block/logic/RailDirection;"))
    public void dilithiumRail1(CallbackInfo ci, @Local(name = "block") Block<?> block, @Local(name = "onPoweredPoweredRail") LocalBooleanRef onPoweredPoweredRail, @Local(name = "onUnpoweredPoweredRail") LocalBooleanRef onUnpoweredPoweredRail) {
        int blockX = MathHelper.floor(x);
        int blockY = MathHelper.floor(y);
        int blockZ = MathHelper.floor(z);
        if (block == SIBlocks.dilithiumRail && world != null) {
            onPoweredPoweredRail.set((world.getBlockMetadata(blockX, blockY, blockZ) & 0b1000) != 0);
            onUnpoweredPoweredRail.set(!onPoweredPoweredRail.get());
            if (onPoweredPoweredRail.get()) {
                world.spawnParticle("reddust", x + 0.4, y, z + 0.4, 0, 0, 0, 0);
                world.spawnParticle("reddust", x - 0.4, y, z + 0.4, 0, 0, 0, 0);
            }
        }
    }

    @ModifyConstant(method = "motionIteration", constant = @Constant(doubleValue = 0.06D), slice = @Slice(from = @At(value = "INVOKE", target = "Ljava/lang/Math;hypot(DD)D", ordinal = 8), to = @At(value = "INVOKE", target = "Lnet/minecraft/core/world/World;isBlockNormalCube(III)Z", ordinal = 0)))
    public double dilithiumRail2(double constant) {
        int blockX = MathHelper.floor(x);
        int blockY = MathHelper.floor(y);
        int blockZ = MathHelper.floor(z);
        Block<?> block = world.getBlock(blockX, blockY, blockZ);
        return block == SIBlocks.dilithiumRail ? 0.12D : constant;
    }
}
