package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.net.packet.Packet;
import net.minecraft.core.net.packet.Packet23VehicleSpawn;
import net.minecraft.server.entity.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.entities.EntityCrystal;

@Mixin(
        value = EntityTrackerEntry.class,
        remap = false
)
public class EntityTrackerEntryMixin {

    @Shadow public Entity trackedEntity;

    @Inject(
            method = "getSpawnPacket",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getSpawnPacket(CallbackInfoReturnable<Packet> cir){
        if(this.trackedEntity instanceof EntityCrystal){
            EntityLiving entityliving = ((EntityCrystal)this.trackedEntity).thrower;
            cir.setReturnValue(new Packet23VehicleSpawn(this.trackedEntity, 47, entityliving == null ? this.trackedEntity.id : entityliving.id));
        }
        /*if(this.trackedEntity instanceof EntityPipeItem){
            cir.setReturnValue(new PacketPipeItemSpawn((EntityPipeItem) this.trackedEntity));
        }*/
    }
}
