package sunsetsatellite.signalindustries.entities;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.HitResult;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3d;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;

import java.util.List;

public class EntityEnergyOrb extends Entity {
    protected int xTile = -1;
    protected int yTile = -1;
    protected int zTile = -1;
    protected int inTile = 0;
    protected int field_28019_h = 0;
    protected boolean inGround = false;
    public boolean doesArrowBelongToPlayer;
    public int arrowShake;
    public EntityLiving owner;
    protected int ticksInGround;
    protected int ticksInAir;
    protected float arrowSpeed;
    protected float arrowGravity;
    protected int arrowDamage;
    protected int arrowType;

    public EntityEnergyOrb(World world) {
        this(world, 0);
    }

    public EntityEnergyOrb(World world, int arrowType) {
        super(world);
        this.doesArrowBelongToPlayer = false;
        this.arrowType = arrowType;
        this.arrowShake = 0;
        this.ticksInAir = 0;
        this.setSize(0.5f, 0.5f);
    }

    public EntityEnergyOrb(World world, double d, double d1, double d2, int arrowType) {
        super(world);
        this.doesArrowBelongToPlayer = false;
        this.arrowType = arrowType;
        this.arrowShake = 0;
        this.ticksInAir = 0;
        this.setSize(0.5f, 0.5f);
        this.setPos(d, d1, d2);
        this.heightOffset = 0.0f;
    }

    public EntityEnergyOrb(World world, EntityLiving entityliving, boolean doesArrowBelongToPlayer, int arrowType) {
        super(world);
        this.doesArrowBelongToPlayer = doesArrowBelongToPlayer;
        this.arrowType = arrowType;
        this.arrowShake = 0;
        this.ticksInAir = 0;
        this.owner = entityliving;
        this.setSize(0.5f, 0.5f);
        this.moveTo(entityliving.x, entityliving.y + (double)entityliving.getHeadHeight() - 0.5f, entityliving.z, entityliving.yRot, entityliving.xRot);
        this.x -= MathHelper.cos(this.yRot / 180.0f * 3.141593f) * 0.16f;
        this.y -= 0.1f;
        this.z -= MathHelper.sin(this.yRot / 180.0f * 3.141593f) * 0.16f;
        this.setPos(this.x, this.y, this.z);
        this.heightOffset = 0.0f;
        this.xd = -MathHelper.sin(this.yRot / 180.0f * 3.141593f) * MathHelper.cos(this.xRot / 180.0f * 3.141593f);
        this.zd = MathHelper.cos(this.yRot / 180.0f * 3.141593f) * MathHelper.cos(this.xRot / 180.0f * 3.141593f);
        this.yd = -MathHelper.sin(this.xRot / 180.0f * 3.141593f);
        this.setArrowHeading(this.xd, this.yd, this.zd, 1.5f, 1.0f);
    }

    @Override
    protected void init() {
        this.arrowGravity = 0.00F;
        this.arrowSpeed = 1.0F;
        this.arrowDamage = 4;
        if (!(this.owner instanceof EntityPlayer)) {
            this.doesArrowBelongToPlayer = false;
        }
    }

    public void setArrowHeading(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += this.random.nextGaussian() * (double)0.0075f * (double)f1;
        d1 += this.random.nextGaussian() * (double)0.0075f * (double)f1;
        d2 += this.random.nextGaussian() * (double)0.0075f * (double)f1;
        this.xd = d *= f;
        this.yd = d1 *= f;
        this.zd = d2 *= f;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.yRotO = this.yRot = (float)(Math.atan2(d, d2) * 180.0 / 3.1415927410125732);
        this.xRotO = this.xRot = (float)(Math.atan2(d1, f3) * 180.0 / 3.1415927410125732);
        this.ticksInGround = 0;
    }

    @Override
    public void lerpMotion(double xd, double yd, double zd) {
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        if (this.xRotO == 0.0f && this.yRotO == 0.0f) {
            float f = MathHelper.sqrt_double(xd * xd + zd * zd);
            this.yRotO = this.yRot = (float)(Math.atan2(xd, zd) * 180.0 / 3.1415927410125732);
            this.xRotO = this.xRot = (float)(Math.atan2(yd, f) * 180.0 / 3.1415927410125732);
            this.xRotO = this.xRot;
            this.yRotO = this.yRot;
            this.moveTo(this.x, this.y, this.z, this.yRot, this.xRot);
            this.ticksInGround = 0;
        }
    }

    @Override
    public void tick() {
        int i;
        super.tick();
        if(ticksInAir >= 120 || ticksInGround >= 120){
            this.remove();
        }
        if (this.xRotO == 0.0f && this.yRotO == 0.0f) {
            float f = MathHelper.sqrt_double(this.xd * this.xd + this.zd * this.zd);
            this.yRotO = this.yRot = (float)(Math.atan2(this.xd, this.zd) * 180.0 / 3.1415927410125732);
            this.xRotO = this.xRot = (float)(Math.atan2(this.yd, f) * 180.0 / 3.1415927410125732);
        }
        if ((i = this.world.getBlockId(this.xTile, this.yTile, this.zTile)) > 0) {
            Block.blocksList[i].setBlockBoundsBasedOnState(this.world, this.xTile, this.yTile, this.zTile);
            AABB axisalignedbb = Block.blocksList[i].getCollisionBoundingBoxFromPool(this.world, this.xTile, this.yTile, this.zTile);
            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3d.createVector(this.x, this.y, this.z))) {
                this.inGround = true;
            }
        }
        if (this.arrowShake > 0) {
            --this.arrowShake;
        }
        if (this.inGround) {
            int j = this.world.getBlockId(this.xTile, this.yTile, this.zTile);
            int k = this.world.getBlockMetadata(this.xTile, this.yTile, this.zTile);
            if (j != this.inTile || k != this.field_28019_h) {
                this.inGround = false;
                this.xd *= this.random.nextFloat() * 0.2f;
                this.yd *= this.random.nextFloat() * 0.2f;
                this.zd *= this.random.nextFloat() * 0.2f;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
                return;
            }
            ++this.ticksInGround;
            if (this.ticksInGround == 1200) {
                this.remove();
            }
            return;
        }

        SignalIndustries.spawnParticle(new EntityColorParticleFX(this.world,this.x, this.y, this.z, this.xd * (double)0.05f, this.yd * (double)0.05f - (double)0.1f, this.zd * (double)0.05f,1,1f,0f,0f));
        SignalIndustries.spawnParticle(new EntityColorParticleFX(this.world,this.x + this.xd * 0.5, this.y + this.yd * 0.5, this.z + this.zd * 0.5, this.xd * (double)0.05f, this.yd * (double)0.05f - (double)0.1f, this.zd * (double)0.05f,1,1f,0,0));
        ++this.ticksInAir;
        Vec3d oldPos = Vec3d.createVector(this.x, this.y, this.z);
        Vec3d newPos = Vec3d.createVector(this.x + this.xd, this.y + this.yd, this.z + this.zd);
        HitResult movingobjectposition = this.world.checkBlockCollisionBetweenPoints(oldPos, newPos, false, true);
        oldPos = Vec3d.createVector(this.x, this.y, this.z);
        newPos = Vec3d.createVector(this.x + this.xd, this.y + this.yd, this.z + this.zd);
        if (movingobjectposition != null) {
            newPos = Vec3d.createVector(movingobjectposition.location.xCoord, movingobjectposition.location.yCoord, movingobjectposition.location.zCoord);
        }
        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.bb.addCoord(this.xd, this.yd, this.zd).expand(1.0, 1.0, 1.0));
        double d = 0.0;
        for (int l = 0; l < list.size(); ++l) {
            double d1;
            float f4;
            AABB axisalignedbb1;
            HitResult movingobjectposition1;
            Entity entity1 = list.get(l);
            if (!entity1.isPickable() || entity1 == this.owner && this.ticksInAir < 5 || (movingobjectposition1 = (axisalignedbb1 = entity1.bb.expand(f4 = 0.3f, f4, f4)).func_1169_a(oldPos, newPos)) == null || !((d1 = oldPos.distanceTo(movingobjectposition1.location)) < d) && d != 0.0) continue;
            entity = entity1;
            d = d1;
        }
        if (entity != null) {
            movingobjectposition = new HitResult(entity);
        }
        if (movingobjectposition != null) {
            if (movingobjectposition.entity != null) {
                if (movingobjectposition.entity.hurt(this.owner, this.arrowDamage, DamageType.COMBAT)) {
                    if (this.isOnFire()) {
                        movingobjectposition.entity.fireHurt();
                    }
                    this.world.playSoundAtEntity(this, this, "random.drr", 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
                    this.remove();
                } else {
                    this.remove();
                    /*this.xd *= -0.1f;
                    this.yd *= -0.1f;
                    this.zd *= -0.1f;
                    this.yRot += 180.0f;
                    this.yRotO += 180.0f;
                    this.ticksInAir = 0;*/
                }
            } else {
                this.remove();
                /*this.xTile = movingobjectposition.x;
                this.yTile = movingobjectposition.y;
                this.zTile = movingobjectposition.z;
                this.inTile = this.world.getBlockId(this.xTile, this.yTile, this.zTile);
                this.field_28019_h = this.world.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                this.xd = (float)(movingobjectposition.location.xCoord - this.x);
                this.yd = (float)(movingobjectposition.location.yCoord - this.y);
                this.zd = (float)(movingobjectposition.location.zCoord - this.z);
                float f1 = MathHelper.sqrt_double(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
                this.x -= this.xd / (double)f1 * (double)0.05f;
                this.y -= this.yd / (double)f1 * (double)0.05f;
                this.z -= this.zd / (double)f1 * (double)0.05f;
                this.inGroundAction();*/
            }
        }
        this.x += this.xd;
        this.y += this.yd;
        this.z += this.zd;
        float f2 = MathHelper.sqrt_double(this.xd * this.xd + this.zd * this.zd);
        this.yRot = (float)(Math.atan2(this.xd, this.zd) * 180.0 / 3.1415927410125732);
        this.xRot = (float)(Math.atan2(this.yd, f2) * 180.0 / 3.1415927410125732);
        while (this.xRot - this.xRotO < -180.0f) {
            this.xRotO -= 360.0f;
        }
        while (this.xRot - this.xRotO >= 180.0f) {
            this.xRotO += 360.0f;
        }
        while (this.yRot - this.yRotO < -180.0f) {
            this.yRotO -= 360.0f;
        }
        while (this.yRot - this.yRotO >= 180.0f) {
            this.yRotO += 360.0f;
        }
        this.xRot = this.xRotO + (this.xRot - this.xRotO) * 0.2f;
        this.yRot = this.yRotO + (this.yRot - this.yRotO) * 0.2f;
        float f3 = this.arrowSpeed;
        float f5 = this.arrowGravity;
        if (this.isInWater()) {
            for (int i1 = 0; i1 < 4; ++i1) {
                float f6 = 0.25f;
                this.world.spawnParticle("bubble", this.x - this.xd * (double)f6, this.y - this.yd * (double)f6, this.z - this.zd * (double)f6, this.xd, this.yd, this.zd);
            }
            f3 = 0.8f;
        }
        this.xd *= f3;
        this.yd *= f3;
        this.zd *= f3;
        this.yd -= f5;
        this.setPos(this.x, this.y, this.z);
    }

    protected void inGroundAction() {
        this.world.playSoundAtEntity(this, this, "random.drr", 1.0f, 1.2f / (this.random.nextFloat() * 0.2f + 0.9f));
        this.inGround = true;
        this.arrowShake = 7;
    }

    public int getArrowType() {
        return this.arrowType;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        tag.putShort("xTile", (short)this.xTile);
        tag.putShort("yTile", (short)this.yTile);
        tag.putShort("zTile", (short)this.zTile);
        tag.putShort("inTile", (byte)this.inTile);
        tag.putByte("inData", (byte)this.field_28019_h);
        tag.putByte("shake", (byte)this.arrowShake);
        tag.putByte("inGround", (byte)(this.inGround ? 1 : 0));
        tag.putBoolean("player", this.doesArrowBelongToPlayer);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        this.xTile = tag.getShort("xTile");
        this.yTile = tag.getShort("yTile");
        this.zTile = tag.getShort("zTile");
        this.inTile = tag.getShort("inTile") & 0x3FFF;
        this.field_28019_h = tag.getByte("inData") & 0xFF;
        this.arrowShake = tag.getByte("shake") & 0xFF;
        this.inGround = tag.getByte("inGround") == 1;
        this.doesArrowBelongToPlayer = tag.getBoolean("player");
    }

    @Override
    public void playerTouch(EntityPlayer player) {
        if (this.world.isClientSide) {
            return;
        }
        if (this.inGround && this.doesArrowBelongToPlayer && this.arrowShake <= 0) {
            this.remove();
        }
    }

    @Override
    public float getShadowHeightOffs() {
        return 0.0f;
    }
}
