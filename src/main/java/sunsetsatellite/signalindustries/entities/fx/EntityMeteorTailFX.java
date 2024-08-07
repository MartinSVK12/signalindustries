package sunsetsatellite.signalindustries.entities.fx;


import net.minecraft.client.entity.fx.EntityFX;

import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SIBlocks;


import java.awt.*;

public class EntityMeteorTailFX extends EntityFX {
    public float red;
    public float blue;
    public float green;

    public int ticks = 0;
    public int ticksMax = 0;

    public boolean fullbright = false;

    public int blockId = 0;

    public final IconCoordinate tex = TextureRegistry.getTexture("signalindustries:particle/meteor_tail.png");

    public EntityMeteorTailFX(World world, double x, double y, double z, double xd, double yd, double zd, float f) {
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
        this.ticksMax = 9;
        this.noPhysics = false;
    }

    public EntityMeteorTailFX(World world, double x, double y, double z, double xd, double yd, double zd, float f, int maxAge) {
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
        this.ticksMax = maxAge;
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

    @Override
    public float getBrightness(float partialTick) {
        if(fullbright) return 1f;
        return super.getBrightness(partialTick);
    }

    public EntityMeteorTailFX setFullbright(boolean fullbright) {
        this.fullbright = fullbright;
        return this;
    }

    public EntityMeteorTailFX setBlockId(int blockId) {
        this.blockId = blockId;
        return this;
    }

    public void tick() {
        ticks++;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.particleAge++ >= this.particleMaxAge)
            remove();
        this.particleTexture = tex;
        //this.particleTextureIndex = TextureHelper.getOrCreateParticleTextureIndex(SignalIndustries.MOD_ID,"meteor_tail.png");//7 - this.particleAge * 8 / this.particleMaxAge;
        move(this.xd, this.yd, this.zd);
        if (this.y == this.yo) {
            this.xd *= 1.1D;
            this.zd *= 1.1D;
        }
        if(blockId == SIBlocks.signalumOre.id){
            this.particleRed = 0.9f;
            this.particleGreen = 1 - (float) particleAge / particleMaxAge;
            this.particleBlue = 1 - (((float) particleAge / particleMaxAge) * 2);
        } else {
            this.particleRed = 1 - (((float) particleAge / particleMaxAge) * 2);
            this.particleGreen = 1 - (float) particleAge / particleMaxAge;
            this.particleBlue = 0.7f;
        }
       /* this.xd *= 0.9599999785423279D;
        this.yd *= 0.9599999785423279D;
        this.zd *= 0.9599999785423279D;*/
        if (this.onGround) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }
    }

    public Color mixColors(Color color1, Color color2, double percent){
        double inverse_percent = 1.0 - percent;
        int redPart = (int) (color1.getRed()*percent + color2.getRed()*inverse_percent);
        int greenPart = (int) (color1.getGreen()*percent + color2.getGreen()*inverse_percent);
        int bluePart = (int) (color1.getBlue()*percent + color2.getBlue()*inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }
}
