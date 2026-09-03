package sunsetsatellite.signalindustries.entities;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.projectile.Projectile;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;

import java.util.List;

public class ProjectileEnergyOrb extends Projectile {
    public ProjectileEnergyOrb(World world) {
        super(world);
    }

    public ProjectileEnergyOrb(World world, Mob owner) {
        super(world, owner);
    }

    public ProjectileEnergyOrb(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    protected void initProjectile() {
        super.initProjectile();
        this.defaultGravity = 0.0F;
        this.defaultProjectileSpeed = 1.0F;
        this.damage = 4;
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hit = getHitResult();
        if (hit != null) {
            if (this.world != null && !this.world.isClientSide) {
				AABB bb2 = AABB.fromPool(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
				List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, bb2.expand(xd, yd, zd).grow(1.0D, 1.0D, 1.0D).asJomlAABB());

                for (Entity entity : list) {
                    if (entity instanceof Mob) {
                        if (!(owner == entity)) {
                            entity.hurt(this.owner, this.damage, DamageType.COMBAT);
                        }
                    }
                }
            }
        }
    }
}
