package sunsetsatellite.signalindustries.dim.custom;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.render.worldtype.WorldTypeFX;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public class WorldTypeFXCustom extends WorldTypeFX {

    public boolean hasClouds = true;
    public boolean hasSky = true;
    public boolean hasGround = true;
    public boolean hasAurora = false;
    public float cloudHeight = 236;
    public Vector3f fogColor = new Vector3f(1,1,1);
    public float[] sunriseColor = new float[4];

    public WorldTypeFXCustom(CustomDimensionData data) {
        super(data.getWorldType());
    }

    public void readFromNbt(CompoundTag tag){

    }

    @Override
    public boolean hasClouds() {
        return hasClouds;
    }

    @Override
    public boolean hasSky() {
        return hasSky;
    }

    @Override
    public boolean hasGround() {
        return hasGround;
    }

    @Override
    public boolean hasAurora() {
        return hasAurora;
    }

	@Override
	public float getCloudHeight(final @NotNull World world) {
		return cloudHeight;
	}

	@Override
    public @NotNull Vector3fc getFogColor(World world, double x, double y, double z, float celestialAngle, float partialTick) {
        return fogColor;
    }

    @Override
    public float[] getSunriseColor(float timeOfDay, float partialTick) {
        return sunriseColor;
    }
}
