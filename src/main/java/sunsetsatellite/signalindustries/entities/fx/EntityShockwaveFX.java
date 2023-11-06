package sunsetsatellite.signalindustries.entities.fx;


import net.minecraft.client.render.Tessellator;
import net.minecraft.core.HitResult;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.fx.EntityFX;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;

import java.util.List;

public class EntityShockwaveFX extends EntityFX {
    private int timeSinceStart = 0;
    private int maximumTime = 0;
    private final EntityLiving creator;

    public EntityShockwaveFX(World world1, double d2, double d4, double d6, double d8, double d10, double d12, EntityLiving creator) {
        super(world1, d2, d4, d6, 0.0D, 0.0D, 0.0D);
        this.creator = creator;
        this.maximumTime = 8;
    }

    public void renderParticle(Tessellator tessellator1, float f2, float f3, float f4, float f5, float f6, float f7) {
    }

    @Override
    public void tick() {
        for(int i1 = 0; i1 < 120; ++i1) {
            double d2 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * timeSinceStart;
            double d4 = this.y - 0.3;
            double d6 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * timeSinceStart;
            this.world.spawnParticle("reddust", d2, d4, d6, (float)this.timeSinceStart / (float)this.maximumTime, 0.0D, 0.0D);
        }

        Vec3d vec3d = Vec3d.createVector(this.x, this.y, this.z);
        Vec3d vec3d1 = Vec3d.createVector(this.x + this.xd, this.y + this.yd, this.z + this.zd);
        HitResult movingobjectposition = this.world.checkBlockCollisionBetweenPoints(vec3d, vec3d1);
        vec3d = Vec3d.createVector(this.x, this.y, this.z);
        vec3d1 = Vec3d.createVector(this.x + this.xd, this.y + this.yd, this.z + this.zd);
        if (movingobjectposition != null) {
            vec3d1 = Vec3d.createVector(movingobjectposition.location.xCoord, movingobjectposition.location.yCoord, movingobjectposition.location.zCoord);
        }

        if (!this.world.isClientSide) {
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.bb.addCoord(this.xd, this.yd, this.zd).expand(timeSinceStart, timeSinceStart, timeSinceStart));
            for (Entity entity : list) {
                if(entity instanceof EntityLiving && entity != creator){
                    entity.hurt(this,15, DamageType.BLAST);
                    double d = this.x - entity.x;

                    double d1;
                    for(d1 = this.z - entity.z; d * d + d1 * d1 < 1.0E-4; d1 = (Math.random() - Math.random()) * 0.01) {
                        d = (Math.random() - Math.random()) * 0.01;
                    }

                    ((EntityLiving) entity).attackedAtYaw = (float)(Math.atan2(d1, d) * 180.0 / 3.1415927410125732) - entity.yRot;
                    ((EntityLiving) entity).knockBack(entity, 15, d, d1);
                }
            }
        }

        ++this.timeSinceStart;
        if(this.timeSinceStart == this.maximumTime) {
            this.remove();
        }

    }

    public int getFXLayer() {
        return 1;
    }
}
