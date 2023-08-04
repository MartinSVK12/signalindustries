package sunsetsatellite.signalindustries.mixin;



import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.api.impl.itempipes.misc.EntityPipeItem;
import sunsetsatellite.signalindustries.entities.EntityCrystal;

@Mixin(
        value = EntityTracker.class,
        remap = false
)
public abstract class EntityTrackerMixin {
    @Shadow public abstract void trackEntity(Entity entity, int i, int j, boolean flag);

    @Inject(
            method = "trackEntity(Lnet/minecraft/src/Entity;)V",
            at = @At("HEAD")
    )
    public void trackEntity(Entity entity, CallbackInfo ci){
        if(entity instanceof EntityCrystal){
            this.trackEntity(entity,64,10,true);
        } else if (entity instanceof EntityPipeItem) {
            if(((EntityPipeItem) entity).canRender()){
                this.trackEntity(entity,64,10,true);
            }
        }
    }

}
