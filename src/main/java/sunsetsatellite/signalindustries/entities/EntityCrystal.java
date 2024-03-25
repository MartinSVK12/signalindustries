package sunsetsatellite.signalindustries.entities;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.HitResult;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.fx.EntityDustCloudFX;

import java.util.List;

public class EntityCrystal extends Entity {
    private int xTileSnowball;
    private int yTileSnowball;
    private int zTileSnowball;
    private int inTileSnowball;
    private boolean inGroundSnowball;
    public int shakeSnowball;
    public EntityLiving thrower;
    private int ticksInGroundSnowball;
    private int ticksInAirSnowball;
    public int damage = 2;

    public EntityCrystal(World world) {
        super(world);
        this.xTileSnowball = -1;
        this.yTileSnowball = -1;
        this.zTileSnowball = -1;
        this.inTileSnowball = 0;
        this.inGroundSnowball = false;
        this.shakeSnowball = 0;
        this.ticksInAirSnowball = 0;
        this.setSize(0.25F, 0.25F);
    }

    @Override
    protected void init() {
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        double d1 = this.bb.getAverageEdgeLength() * 4.0;
        d1 *= 64.0;
        return distance < d1 * d1;
    }

    public EntityCrystal(World world, EntityLiving entityliving) {
        super(world);
        this.xTileSnowball = -1;
        this.yTileSnowball = -1;
        this.zTileSnowball = -1;
        this.inTileSnowball = 0;
        this.inGroundSnowball = false;
        this.shakeSnowball = 0;
        this.ticksInAirSnowball = 0;
        this.thrower = entityliving;
        this.setSize(0.25F, 0.25F);
        this.moveTo(entityliving.x, entityliving.y + (double)entityliving.getHeadHeight(), entityliving.z, entityliving.yRot, entityliving.xRot);
        this.x -= MathHelper.cos(this.yRot / 180.0F * 3.141593F) * 0.16F;
        this.y -= 0.1F;
        this.z -= MathHelper.sin(this.yRot / 180.0F * 3.141593F) * 0.16F;
        this.setPos(this.x, this.y, this.z);
        this.heightOffset = 0.0F;
        float f = 0.4F;
        this.xd = -MathHelper.sin(this.yRot / 180.0F * 3.141593F) * MathHelper.cos(this.xRot / 180.0F * 3.141593F) * f;
        this.zd = MathHelper.cos(this.yRot / 180.0F * 3.141593F) * MathHelper.cos(this.xRot / 180.0F * 3.141593F) * f;
        this.yd = -MathHelper.sin(this.xRot / 180.0F * 3.141593F) * f;
        this.setSnowballHeading(this.xd, this.yd, this.zd, 1.5F, 1.0F);
    }

    public EntityCrystal(World world, double d, double d1, double d2) {
        super(world);
        this.xTileSnowball = -1;
        this.yTileSnowball = -1;
        this.zTileSnowball = -1;
        this.inTileSnowball = 0;
        this.inGroundSnowball = false;
        this.shakeSnowball = 0;
        this.ticksInAirSnowball = 0;
        this.ticksInGroundSnowball = 0;
        this.setSize(0.25F, 0.25F);
        this.setPos(d, d1, d2);
        this.heightOffset = 0.0F;
    }

    public void setSnowballHeading(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += this.random.nextGaussian() * 0.0075F * (double)f1;
        d1 += this.random.nextGaussian() * 0.0075F * (double)f1;
        d2 += this.random.nextGaussian() * 0.0075F * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        this.xd = d;
        this.yd = d1;
        this.zd = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.yRotO = this.yRot = (float)(Math.atan2(d, d2) * 180.0 / (float) Math.PI);
        this.xRotO = this.xRot = (float)(Math.atan2(d1, f3) * 180.0 / (float) Math.PI);
        this.ticksInGroundSnowball = 0;
    }

    public void setSnowballHeadingPrecise(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d *= f;
        d1 *= f;
        d2 *= f;
        this.xd = d;
        this.yd = d1;
        this.zd = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.yRotO = this.yRot = (float)(Math.atan2(d, d2) * 180.0 / (float) Math.PI);
        this.xRotO = this.xRot = (float)(Math.atan2(d1, f3) * 180.0 / (float) Math.PI);
        this.ticksInGroundSnowball = 0;
    }

    @Override
    public void lerpMotion(double xd, double yd, double zd) {
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            float f = MathHelper.sqrt_double(xd * xd + zd * zd);
            this.yRotO = this.yRot = (float)(Math.atan2(xd, zd) * 180.0 / (float) Math.PI);
            this.xRotO = this.xRot = (float)(Math.atan2(yd, f) * 180.0 / (float) Math.PI);
        }
    }

    @Override
    public void tick() {
        this.xOld = this.x;
        this.yOld = this.y;
        this.zOld = this.z;
        super.tick();
        if (this.shakeSnowball > 0) {
            --this.shakeSnowball;
        }

        if (this.inGroundSnowball) {
            int i = this.world.getBlockId(this.xTileSnowball, this.yTileSnowball, this.zTileSnowball);
            if (i == this.inTileSnowball) {
                ++this.ticksInGroundSnowball;
                if (this.ticksInGroundSnowball == 1200) {
                    this.remove();
                }

                return;
            }

            this.inGroundSnowball = false;
            this.xd *= this.random.nextFloat() * 0.2F;
            this.yd *= this.random.nextFloat() * 0.2F;
            this.zd *= this.random.nextFloat() * 0.2F;
            this.ticksInGroundSnowball = 0;
            this.ticksInAirSnowball = 0;
        } else {
            ++this.ticksInAirSnowball;
        }

        Vec3d vec3d = Vec3d.createVector(this.x, this.y, this.z);
        Vec3d vec3d1 = Vec3d.createVector(this.x + this.xd, this.y + this.yd, this.z + this.zd);
        HitResult movingobjectposition = this.world.checkBlockCollisionBetweenPoints(vec3d, vec3d1);
        vec3d = Vec3d.createVector(this.x, this.y, this.z);
        vec3d1 = Vec3d.createVector(this.x + this.xd, this.y + this.yd, this.z + this.zd);
        if (movingobjectposition != null) {
            vec3d1 = Vec3d.createVector(movingobjectposition.location.xCoord, movingobjectposition.location.yCoord, movingobjectposition.location.zCoord);
        }


        if (movingobjectposition != null) {

            if (!this.world.isClientSide) {
                List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.bb.addCoord(this.xd, this.yd, this.zd).expand(3.0, 3.0, 3.0));

                for (Entity entity : list) {
                    if(entity instanceof EntityLiving){
                        if(!(thrower == entity)){
                            entity.hurt(this.thrower,this.damage, DamageType.COMBAT);
                        }
                    }
                }
            }

            for(int j = 0; j < 8; ++j) {
                float red = this.random.nextFloat();
                if(red < 0.25f){
                    red = 0.25f;
                }
                SignalIndustries.spawnParticle(new EntityDustCloudFX(world, this.x, this.y, this.z, 0, 0, 0, red, 0.0f, 0.0f));
                //this.world.spawnParticle("dustcloud", this.x, this.y, this.z, 0.0, 0.0, 0.0);
                for (int i = 0; i < 4; i++) {
                    this.world.spawnParticle("crystalbreak", this.x, this.y, this.z, 0.0, 0.0, 0.0);
                }
            }

            world.playSoundAtEntity(this, this,"signalindustries.crystalbreak", 0.5F, 1F / (this.random.nextFloat() * 0.4F + 0.8F));

            this.remove();
        }

        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
        float f = MathHelper.sqrt_double(this.xd * this.xd + this.zd * this.zd);
        this.yRot = (float)(Math.atan2(this.xd, this.zd) * 180.0 / (float) Math.PI);
        this.xRot = (float)(Math.atan2(this.yd, f) * 180.0 / (float) Math.PI);

        while(this.xRot - this.xRotO < -180.0F) {
            this.xRotO -= 360.0F;
        }

        while(this.xRot - this.xRotO >= 180.0F) {
            this.xRotO += 360.0F;
        }

        while(this.yRot - this.yRotO < -180.0F) {
            this.yRotO -= 360.0F;
        }

        while(this.yRot - this.yRotO >= 180.0F) {
            this.yRotO += 360.0F;
        }

        this.xRot = this.xRotO + (this.xRot - this.xRotO) * 0.2F;
        this.yRot = this.yRotO + (this.yRot - this.yRotO) * 0.2F;
        float f1 = 0.99F;
        float f2 = 0.03F;
        if (this.isInWater()) {
            for(int k = 0; k < 4; ++k) {
                float f3 = 0.25F;
                this.world.spawnParticle("bubble", this.x - this.xd * (double)f3, this.y - this.yd * (double)f3, this.z - this.zd * (double)f3, this.xd, this.yd, this.zd);
            }

            f1 = 0.8F;
        }

        this.xd *= f1;
        this.yd *= f1;
        this.zd *= f1;
        this.yd -= f2;
        this.setPos(this.x, this.y, this.z);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putShort("xTile", (short)this.xTileSnowball);
        tag.putShort("yTile", (short)this.yTileSnowball);
        tag.putShort("zTile", (short)this.zTileSnowball);
        tag.putShort("inTile", (short)this.inTileSnowball);
        tag.putByte("shake", (byte)this.shakeSnowball);
        tag.putByte("inGround", (byte)(this.inGroundSnowball ? 1 : 0));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.xTileSnowball = tag.getShort("xTile");
        this.yTileSnowball = tag.getShort("yTile");
        this.zTileSnowball = tag.getShort("zTile");
        this.inTileSnowball = tag.getShort("inTile") & 16383;
        this.shakeSnowball = tag.getByte("shake") & 255;
        this.inGroundSnowball = tag.getByte("inGround") == 1;
    }

    @Override
    public void playerTouch(EntityPlayer player) {
        if (this.inGroundSnowball && this.thrower == player && this.shakeSnowball <= 0)
        {
            this.world.playSoundAtEntity(this, this,"random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.onItemPickup(this, 1);
            this.remove();
        }
    }

    @Override
    public float getShadowHeightOffs() {
        return 0.0F;
    }
}
