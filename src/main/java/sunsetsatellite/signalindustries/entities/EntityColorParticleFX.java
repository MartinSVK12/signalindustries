package sunsetsatellite.signalindustries.entities;


import net.minecraft.client.render.Tessellator;
import net.minecraft.core.entity.fx.EntityFX;
import net.minecraft.core.world.World;

public class EntityColorParticleFX extends EntityFX {
    public float red;
    public float blue;
    public float green;

    public EntityColorParticleFX(World world, double x, double y, double z, double xd, double yd, double zd, float f, float red, float green, float blue) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.xd = xd + ((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.yd = yd + ((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.zd = zd + ((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        float f4 = (float)Math.random() * 0.4F + 0.6F;
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.particleScale *= 0.75F;
        this.particleScale *= f;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int)(this.particleMaxAge * f);
        this.noPhysics = false;
    }

    public EntityColorParticleFX(World world, double x, double y, double z, double xd, double yd, double zd, float f, float red, float green, float blue, int maxAge) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.xd = xd + ((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.yd = yd + ((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.zd = zd + ((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        float f4 = (float)Math.random() * 0.4F + 0.6F;
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.particleScale *= 0.75F;
        this.particleScale *= f;
        this.particleMaxAge = maxAge;
        this.particleMaxAge = (int)(this.particleMaxAge * f);
        this.noPhysics = false;
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
        float f6 = (this.particleAge + f) / this.particleMaxAge * 32.0F;
        if (f6 < 0.0F)
            f6 = 0.0F;
        if (f6 > 1.0F)
            f6 = 1.0F;
        super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.particleAge++ >= this.particleMaxAge)
            remove();
        this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
        move(this.xd, this.yd, this.zd);
        if (this.y == this.yo) {
            this.xd *= 1.1D;
            this.zd *= 1.1D;
        }
        this.xd *= 0.9599999785423279D;
        this.yd *= 0.9599999785423279D;
        this.zd *= 0.9599999785423279D;
        if (this.onGround) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }
    }
}
