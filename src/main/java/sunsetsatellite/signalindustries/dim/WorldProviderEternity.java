package sunsetsatellite.signalindustries.dim;

import net.minecraft.src.*;
import sunsetsatellite.signalindustries.SignalIndustries;

public class WorldProviderEternity extends WorldProvider {
    public WorldProviderEternity() {
    }

    public void registerWorldChunkManager() {
        this.worldChunkMgr = new WorldChunkManagerEternity(this.worldObj, SignalIndustries.biomeEternity, 0.0, 0.0);
        this.hasNoSky = true;
    }
    public IChunkProvider getChunkProvider() {
        return new ChunkProviderEternity(this.worldObj,this.worldObj.getRandomSeed(), 0, 128, 64);
    }

    public float calculateCelestialAngle(long l, float f) {
        return 0.75F;
    }

    protected void generateLightBrightnessTable() {
        float f = 0.3F;

        for(int i = 0; i <= 15; ++i) {
            float f1 = 1.0F - (float)i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }

    }

    public Vec3D func_4096_a(float f, float f1) {
        float f2 = MathHelper.cos(f * 3.141593F * 2.0F) * 2.0F + 0.5F;
        if (f2 < 0.0F) {
            f2 = 0.0F;
        }

        if (f2 > 1.0F) {
            f2 = 1.0F;
        }

        float f3 = 0.5f;
        float f4 = 0.5f;
        float f5 = 0.5f;
        f3 *= f2 * 0.94F + 0.06F;
        f4 *= f2 * 0.94F + 0.06F;
        f5 *= f2 * 0.91F + 0.09F;
        return Vec3D.createVector(f3, f4, f5);
    }

    @Override
    public float[] calcSunriseSunsetColors(float f, float f1) {
        return new float[]{0.7F,0.7F,0.7F,0F};
    }

    public boolean canRespawnHere() {
        return false;
    }
}

