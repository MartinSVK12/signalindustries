package sunsetsatellite.signalindustries.entities;

import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.projectile.Projectile;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIBlocks;
import sunsetsatellite.signalindustries.SIItems;

public class ProjectileFallingMeteor extends Projectile {

    public int blockID;

    public ProjectileFallingMeteor(World world) {
        super(world);
        blockID = Blocks.BASALT.id();
        modelItem = Blocks.BASALT.asItem();
    }

    public ProjectileFallingMeteor(World world, double x, double y, double z, int blockID) {
        super(world, x, y, z);
        this.blockID = blockID;
        modelItem = Blocks.getBlock(blockID).asItem();
    }

    @Override
    protected void initProjectile() {
        damage = 10;
        defaultGravity = 0.03f;
        defaultProjectileSpeed = 1f;
    }

    @Override
    public void setHeading(double newMotionX, double newMotionY, double newMotionZ, float speed, float randomness) {
        super.setHeadingPrecise(newMotionX, newMotionY, newMotionZ, speed);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world != null) {
            if (blockID == SIBlocks.signalumOre.id()) {
                for (int j = 0; j < 4; j++) {
                    this.world.spawnParticle("blueflame", this.x + 0.5f, this.y, this.z + 0.5f, this.xd * (double) 0.05f, this.yd * (double) 0.05f - (double) 0.1f, this.zd * (double) 0.05f, 0, 256, true);
                }
                this.world.spawnParticle("blueflame", this.x + 0.5f, this.y, this.z + 0.5f, this.xd * (double) 0.05f, this.yd * (double) 0.05f - (double) 0.1f, this.zd * (double) 0.05f, 0, 256, true);
            } else {
                for (int j = 0; j < 4; j++) {
                    this.world.spawnParticle("flame", this.x + 0.5f, this.y, this.z + 0.5f, this.xd * (double) 0.05f, this.yd * (double) 0.05f - (double) 0.1f, this.zd * (double) 0.05f, 0, 256, true);
                }
                this.world.spawnParticle("flame", this.x + 0.5f, this.y, this.z + 0.5f, this.xd * (double) 0.05f, this.yd * (double) 0.05f - (double) 0.1f, this.zd * (double) 0.05f, 0, 256, true);
            }
        }
    }

    @Override
    public void onHit(@NonNull HitResult hitResult) {
        if (world != null && !world.isClientSide) {
            if (blockID == SIBlocks.signalumOre.id()) {
                EntityItem entityitem = new EntityItem(world, (float) x, (float) y, (float) z, new ItemStack(SIItems.rawSignalumCrystal, random.nextInt(3) + 1));
                if (world != null) {
                    world.entityJoinedWorld(entityitem);
                }
            }
        }

        super.onHit(hitResult);
    }
}
