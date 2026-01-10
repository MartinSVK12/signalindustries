package sunsetsatellite.signalindustries.mp.entity.entry;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.net.entity.EntityTracker;
import net.minecraft.core.net.entity.EntityTrackerEntry;
import net.minecraft.core.net.entity.ITrackedEntry;
import net.minecraft.core.net.entity.IVehicleEntry;
import net.minecraft.core.net.packet.PacketAddEntity;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.entities.ProjectileCrystal;

public class NetEntryVolatileCrystal implements IVehicleEntry<ProjectileCrystal>, ITrackedEntry<ProjectileCrystal> {
    @Override
    public @NotNull Class<ProjectileCrystal> getAppliedClass() {
        return ProjectileCrystal.class;
    }

    @Override
    public int getTrackingDistance() {
        return 64;
    }

    @Override
    public int getPacketDelay() {
        return 10;
    }

    @Override
    public boolean sendMotionUpdates() {
        return true;
    }

    @Override
    public void onEntityTracked(EntityTracker tracker, EntityTrackerEntry trackerEntry, ProjectileCrystal trackedObject) {

    }

    @Override
    public Entity getEntity(World world, double x, double y, double z, int metadata, boolean hasVelocity, double xd, double yd, double zd, Entity owner, @Nullable CompoundTag tag) {
        return new ProjectileCrystal(world, x, y, z);
    }

    @Override
    public PacketAddEntity getSpawnPacket(EntityTrackerEntry tracker, ProjectileCrystal trackedObject) {
        return new PacketAddEntity(trackedObject);
    }
}
