package sunsetsatellite.signalindustries.mixin;


import net.minecraft.core.entity.Entity;
import net.minecraft.server.entity.EntityTrackerEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(
        value = EntityTrackerEntry.class,
        remap = false
)
public class EntityTrackerEntryMixin {

    @Shadow public Entity trackedEntity;

    /*@Inject(
            method = "getSpawnPacket",
            at = @At("HEAD"),
            cancellable = true
    )
    private void getSpawnPacket(CallbackInfoReturnable<Packet> cir){
        if(this.trackedEntity instanceof EntityCrystal){
            Entity entity = ((EntityCrystal)this.trackedEntity).thrower;
            cir.setReturnValue(new Packet23VehicleSpawn(this.trackedEntity, 47, entity == null ? this.trackedEntity.id : entity.id));
        }
        /*if(this.trackedEntity instanceof EntityPipeItem){
            cir.setReturnValue(new PacketPipeItemSpawn((EntityPipeItem) this.trackedEntity));
        }
    }*/
}
