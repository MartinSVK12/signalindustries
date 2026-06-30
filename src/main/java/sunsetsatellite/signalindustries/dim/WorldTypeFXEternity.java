package sunsetsatellite.signalindustries.dim;

import net.minecraft.client.render.worldtype.WorldTypeFX;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.core.world.type.WorldType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;

public class WorldTypeFXEternity extends WorldTypeFX {
    public WorldTypeFXEternity(WorldType worldType) {
        super(worldType);
		setCloudHeight(-1);
    }

    @Override
    public @NotNull Vector3fc getFogColor(@NonNull World world, double x, double y, double z, float celestialAngle, float partialTick) {
        return new Vector3f(0.8f, 0.8f, 0.8f);
    }

	@Override
	public boolean hasClouds() {
		return false;
	}

	@Override
	public boolean hasSky() {
		return false;
	}

	@Override
	public boolean hasAurora() {
		return false;
	}

	@Override
	public boolean hasGround() {
		return true;
	}
}
