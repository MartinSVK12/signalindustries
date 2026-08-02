package sunsetsatellite.signalindustries.entities;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;

import java.util.List;

public class EntityShockwave extends Entity {

	public EntityShockwave(@Nullable World world) {
		super(world);
	}

    public EntityShockwave(@Nullable World world, Vec3f start) {
        super(world);
        setSize(10, 10);
        setPos(start.x, start.y, start.z);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= 40) {
            remove();
        }
        if (world == null) return;
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, bb);
        for (Entity entity : entities) {
            if (entity instanceof Mob) {
                entity.hurt(null, 30, DamageType.BLAST);
                double d = x - entity.x;

                double d1;
                for (d1 = z - entity.z; d * d + d1 * d1 < 1.0E-4; d1 = (Math.random() - Math.random()) * 0.01) {
                    d = (Math.random() - Math.random()) * 0.01;
                }

                ((Mob) entity).attackedAtYaw = (float) (Math.atan2(d1, d) * 180.0 / 3.1415927410125732) - entity.yRot;
                ((Mob) entity).knockBack(entity, 30, d, d1);
            }
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }
}
