package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.monster.MobSkeleton;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.entities.MobInfernal;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.mixins.IWarpPlayer;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

import static net.minecraft.core.entity.player.Player.deathMsgColor;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin extends Mob implements IWarpPlayer, IPlayerPowerSuit<SignalumPowerSuit> {

    private PlayerMixin(World world) {
        super(world);
    }

    @Override
    public void warp(int dim) {

    }

    @Inject(method = "getDeathMessage", at = @At("TAIL"), cancellable = true)
    public void getDeathMessage(Entity entityKilledBy, CallbackInfoReturnable<String> cir){
        if (entityKilledBy instanceof MobInfernal)
        {
            cir.setReturnValue(getDisplayName() + deathMsgColor + " found out that the sun is a deadly laser.");
        }
    }


    @Override
    public SignalumPowerSuit getPowerSuit() {
        return null;
    }

    @Override
    public CompoundTag getPowerSuitData() {
        return null;
    }
}
