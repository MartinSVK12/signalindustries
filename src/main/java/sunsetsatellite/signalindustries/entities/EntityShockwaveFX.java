package sunsetsatellite.signalindustries.entities;

import net.minecraft.src.*;
import net.minecraft.src.helper.DamageType;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.util.List;

public class EntityShockwaveFX extends EntityFX {
    private int timeSinceStart = 0;
    private int maximumTime = 0;
    private EntityLiving creator;

    public EntityShockwaveFX(World world1, double d2, double d4, double d6, double d8, double d10, double d12, EntityLiving creator) {
        super(world1, d2, d4, d6, 0.0D, 0.0D, 0.0D);
        this.creator = creator;
        this.maximumTime = 8;
    }

    public void renderParticle(Tessellator tessellator1, float f2, float f3, float f4, float f5, float f6, float f7) {
    }

    public void onUpdate() {
        for(int i1 = 0; i1 < 120; ++i1) {
            double d2 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * timeSinceStart;
            double d4 = this.posY - 0.3;
            double d6 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * timeSinceStart;
            this.worldObj.spawnParticle("reddust", d2, d4, d6, (double)((float)this.timeSinceStart / (float)this.maximumTime), 0.0D, 0.0D);
        }

        Vec3D vec3d = Vec3D.createVector(this.posX, this.posY, this.posZ);
        Vec3D vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3d, vec3d1);
        vec3d = Vec3D.createVector(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.isMultiplayerAndNotHost) {
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(timeSinceStart, timeSinceStart, timeSinceStart));
            for (Entity entity : list) {
                if(entity instanceof EntityLiving && entity != creator){
                    entity.attackEntityFrom(this,15, DamageType.BLAST);
                    double d = this.posX - entity.posX;

                    double d1;
                    for(d1 = this.posZ - entity.posZ; d * d + d1 * d1 < 1.0E-4; d1 = (Math.random() - Math.random()) * 0.01) {
                        d = (Math.random() - Math.random()) * 0.01;
                    }

                    ((EntityLiving) entity).attackedAtYaw = (float)(Math.atan2(d1, d) * 180.0 / 3.1415927410125732) - entity.rotationYaw;
                    ((EntityLiving) entity).knockBack(entity, 15, d, d1);
                }
            }
        }

        ++this.timeSinceStart;
        if(this.timeSinceStart == this.maximumTime) {
            this.setEntityDead();
        }

    }

    public int getFXLayer() {
        return 1;
    }
}
