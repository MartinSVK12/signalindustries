package sunsetsatellite.signalindustries.dim.custom;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.render.worldtype.WorldTypeFX;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.core.world.type.WorldType;

public class WorldTypeFXCustom extends WorldTypeFX {

    public boolean hasClouds = true;
    public boolean hasSky = true;
    public boolean hasGround = true;
    public boolean hasAurora = false;
    public float cloudHeight = 128;
    public Vec3 fogColor = Vec3.getPermanentVec3(1,1,1);
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
    public float getCloudHeight() {
        return cloudHeight;
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
    public Vec3 getFogColor(World world, double x, double y, double z, float celestialAngle, float partialTick) {
        return fogColor;
    }

    @Override
    public float[] getSunriseColor(float timeOfDay, float partialTick) {
        return sunriseColor;
    }
}
