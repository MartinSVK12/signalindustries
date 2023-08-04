package sunsetsatellite.signalindustries.entities;





public class EntityColorParticleFX extends EntityFX {
    public float red;
    public float blue;
    public float green;

    public EntityColorParticleFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float f, float red, float green, float blue) {
        super(world, x, y, z, 0.0D, 0.0D, 0.0D);
        this.motionX = motionX + ((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.motionY = motionY + ((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        this.motionZ = motionZ + ((float)(Math.random() * 2.0D - 1.0D) * 0.05F);
        float f4 = (float)Math.random() * 0.4F + 0.6F;
        this.particleRed = red;
        this.particleGreen = green;
        this.particleBlue = blue;
        this.particleScale *= 0.75F;
        this.particleScale *= f;
        this.particleMaxAge = (int)(8.0D / (Math.random() * 0.8D + 0.2D));
        this.particleMaxAge = (int)(this.particleMaxAge * f);
        this.noClip = false;
    }

    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
        float f6 = (this.particleAge + f) / this.particleMaxAge * 32.0F;
        if (f6 < 0.0F)
            f6 = 0.0F;
        if (f6 > 1.0F)
            f6 = 1.0F;
        super.renderParticle(tessellator, f, f1, f2, f3, f4, f5);
    }

    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge)
            setEntityDead();
        this.particleTextureIndex = 7 - this.particleAge * 8 / this.particleMaxAge;
        moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }
        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;
        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }
}
