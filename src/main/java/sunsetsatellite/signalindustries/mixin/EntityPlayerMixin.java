package sunsetsatellite.signalindustries.mixin;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.enums.EnumSleepStatus;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.ItemSignalumPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

@Mixin(
        value = EntityPlayer.class,
        remap = false
)
public abstract class EntityPlayerMixin extends EntityLiving implements IPlayerPowerSuit {

    @Unique
    public SignalumPowerSuit powerSuit = null;

    @Shadow public abstract void addChatMessage(String s);

    @Shadow public InventoryPlayer inventory;

    public EntityPlayerMixin(World world) {
        super(world);
    }


    @Inject(
            method = "sleepInBedAt",
            at = @At("HEAD"),
            cancellable = true
    )
    public void sleepInBedAt(int x, int y, int z, CallbackInfoReturnable<EnumSleepStatus> cir) {
        if (!world.isClientSide) {
            if(world.getCurrentWeather() == SignalIndustries.weatherBloodMoon){
                addChatMessage("bed.bloodMoon");
                cir.setReturnValue(EnumSleepStatus.NOT_POSSIBLE_NOW);
            }
        }
    }

    @Override
    public SignalumPowerSuit signalIndustries$getPowerSuit() {
        return powerSuit;
    }

    @Inject(
            method = "onLivingUpdate",
            at = @At("HEAD")
    )
    public void powerSuitUpdate(CallbackInfo ci) {
        ItemStack[] armorInventory = inventory.armorInventory;
        for (ItemStack itemStack : armorInventory) {
            if(itemStack == null){
                powerSuit = null;
                return;
            } else if(!(itemStack.getItem() instanceof ItemSignalumPowerSuit)){
                powerSuit = null;
                return;
            }
        }
        if(powerSuit == null){
            powerSuit = new SignalumPowerSuit(armorInventory,((EntityPlayer) (Object) this));
        } else {
            powerSuit.tick();
        }
    }

    @Inject(
            method = "addAdditionalSaveData",
            at = @At("HEAD")
    )
    public void saveSuitData(CompoundTag nbttagcompound, CallbackInfo ci) {
        if(powerSuit != null){
            powerSuit.saveToStacks();
        }
    }
}
