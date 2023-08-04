package sunsetsatellite.signalindustries.mixin;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.EntityPipeItem;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.mp.packets.PacketPipeItemSpawn;

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
            EntityLiving entityliving = ((EntityCrystal)this.trackedEntity).field_20051_g;
            cir.setReturnValue(new Packet23VehicleSpawn(this.trackedEntity, 47, entityliving == null ? this.trackedEntity.entityId : entityliving.entityId));
        }
        if(this.trackedEntity instanceof EntityPipeItem){
            cir.setReturnValue(new PacketPipeItemSpawn((EntityPipeItem) this.trackedEntity));
        }
    }
}
