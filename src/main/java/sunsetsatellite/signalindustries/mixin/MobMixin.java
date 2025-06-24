package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.signalindustries.*;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

import java.util.Iterator;
import java.util.List;

@Mixin(value = Mob.class, remap = false)
public abstract class MobMixin extends Entity {

    @Unique
    private final Mob thisAs = (Mob) ((Object)this);

    @Shadow protected abstract List<WeightedRandomLootObject> getMobDrops();

    private MobMixin(@Nullable World world) {
        super(world);
    }

    @Inject(
            method = "getMaxSpawnedInChunk",
            at = @At("HEAD"),
            cancellable = true
    )
    public void bloodMoonSpawning(CallbackInfoReturnable<Integer> cir){
        if (world != null) {
            cir.setReturnValue(world.getCurrentWeather() == SIWeather.weatherBloodMoon ? 16 : 4);
        }
    }

    @Inject(method = "canSpawnHere",at = @At("HEAD"),cancellable = true)
    public void canSpawnHere(CallbackInfoReturnable<Boolean> cir)
    {
        if (world != null && world.dimension == SIDimensions.ETERNITY) {
            cir.setReturnValue(false);
        }

        SignalIndustries.uvLamps.removeIf((B)->world.getBlock(B.pos.x,B.pos.y,B.pos.z) != SIBlocks.uvLamp);
        for (BlockInstance lamp : SignalIndustries.uvLamps) {
            if(world.getBlockMetadata(lamp.pos.x, lamp.pos.y, lamp.pos.z) == 1){
                if(distanceTo(lamp.pos.x,lamp.pos.y,lamp.pos.z) < 20){
                    cir.setReturnValue(false);
                    break;
                }
            }
        }
    }

    @Inject(method = "dropDeathItems", at = @At("HEAD"))
    protected void dropDeathItems(CallbackInfo ci) {
        if (world != null && world.getCurrentWeather() == SIWeather.weatherBloodMoon) {
            List<WeightedRandomLootObject> drops = getMobDrops();
            if (drops != null) {
                for (WeightedRandomLootObject lootObject : drops) {
                    ItemStack stack = lootObject.getItemStack();
                    if (stack == null) continue;
                    for (int i = 0; i < stack.stackSize; i++) {
                        dropItem(new ItemStack(stack.itemID, 1, stack.getMetadata(), stack.getData()), 0f);
                    }
                }
            }
        }
    }

    @ModifyExpressionValue(
            method = "moveEntityWithHeading",
            at = @At(value = "FIELD", target = "Lnet/minecraft/core/entity/Mob;noPhysics:Z",opcode = Opcodes.GETFIELD)
    )
    private boolean flyWithWings(boolean original){
        if(thisAs instanceof Player){
            Player player = ((Player) thisAs);
            SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>)player).getPowerSuit();
            if(ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)){
                return original || ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active");
            } else {
                return original;
            }
        } else {
            return original;
        }
    }

    @Inject(method = "causeFallDamage", at = @At("HEAD"), cancellable = true)
    protected void causeFallDamage(float f, CallbackInfo ci) {
        if(thisAs instanceof Player) {
            Player player = ((Player) thisAs);
            SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>) player).getPowerSuit();
            if (ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)) {
                if(ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active")){
                    ci.cancel();
                }
            }
        }
    }

}
