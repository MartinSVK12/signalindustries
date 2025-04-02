package sunsetsatellite.signalindustries.entities;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.projectile.Projectile;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SIItems;

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
        this.gravity = 0.0F;
        this.speed = 1.0F;
        this.damage = 4;
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hit = getHitResult();
        if(hit != null){
            if (this.world != null && !this.world.isClientSide) {
                List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(this, bb.expand(xd, yd, zd).grow(1.0D, 1.0D, 1.0D));

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
