package sunsetsatellite.signalindustries.entities;

import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.projectile.Projectile;
import net.minecraft.core.world.World;

public class ProjectileSunbeam extends Projectile {
    public ProjectileSunbeam(World world) {
        super(world);
    }

    public ProjectileSunbeam(World world, Mob owner) {
        super(world, owner);
    }

    public ProjectileSunbeam(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    protected void initProjectile() {
        super.initProjectile();
        this.defaultGravity = 0.00F;
        this.defaultProjectileSpeed = 1.5F;
        this.damage = 5;
    }
}
