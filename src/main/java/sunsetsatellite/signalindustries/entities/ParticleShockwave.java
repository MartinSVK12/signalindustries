package sunsetsatellite.signalindustries.entities;

import net.minecraft.client.entity.particle.Particle;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;

import java.util.List;

public class ParticleShockwave extends Particle {

    private int timeSinceStart = 0;
    private final int maximumTime;

    /**
     * Creates a new Particle.
     *
     * @param world The world the particle will join.
     * @param x     The particle's initial X position.
     * @param y     The particle's initial Y position.
     * @param z     The particle's initial Z position.
     * @param xa    The particle's initial X velocity.
     * @param ya    The particle's initial Y velocity.
     * @param za    The particle's initial Z velocity.
     */
    public ParticleShockwave(World world, double x, double y, double z, double xa, double ya, double za, int d) {
        super(world, x, y, z, xa, ya, za);
        this.maximumTime = 8;
    }

    @Override
    public void render(Tessellator t, float partialTick, double xOff, double yOff, double zOff, float xa, float ya, float za, float xa2, float za2) {
        
    }

    @Override
    public void tick() {
        for(int i1 = 0; i1 < 120; ++i1) {
            double d2 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * timeSinceStart;
            double d4 = this.y - 0.3;
            double d6 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * timeSinceStart;
            this.world.spawnParticle("reddust", d2, d4, d6, (float)this.timeSinceStart / (float)this.maximumTime, 0.0D, 0.0D,0);
        }

        if (!this.world.isClientSide) {
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.bb.expand(this.xd, this.yd, this.zd).grow(timeSinceStart, timeSinceStart, timeSinceStart));
            for (Entity entity : list) {
                if(entity instanceof Mob){
                    entity.hurt(this,15, DamageType.BLAST);
                    double d = this.x - entity.x;

                    double d1;
                    for(d1 = this.z - entity.z; d * d + d1 * d1 < 1.0E-4; d1 = (Math.random() - Math.random()) * 0.01) {
                        d = (Math.random() - Math.random()) * 0.01;
                    }

                    ((Mob) entity).attackedAtYaw = (float)(Math.atan2(d1, d) * 180.0 / 3.1415927410125732) - entity.yRot;
                    ((Mob) entity).knockBack(entity, 15, d, d1);
                }
            }
        }

        ++this.timeSinceStart;
        if(this.timeSinceStart == this.maximumTime) {
            this.remove();
        }

    }
}
