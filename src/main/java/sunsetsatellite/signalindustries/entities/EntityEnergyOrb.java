package sunsetsatellite.signalindustries.entities;


import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;

public class EntityEnergyOrb extends Entity {
    protected int xTile;
    protected int yTile;
    protected int zTile;
    protected int inTile;
    protected int field_28019_h;
    protected boolean inGround;
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

    @Override
    protected void init() {
        this.arrowGravity = 0.00F;
        this.arrowSpeed = 1F;
        this.arrowDamage = 5;
        if (!(this.owner instanceof EntityPlayer)) {
            this.doesArrowBelongToPlayer = false;
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    public EntityEnergyOrb(World world, int arrowType) {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.field_28019_h = 0;
        this.inGround = false;
        this.doesArrowBelongToPlayer = false;
        this.arrowType = arrowType;
        this.arrowShake = 0;
        this.ticksInAir = 0;
        this.setSize(0.5F, 0.5F);
    }

    public EntityEnergyOrb(World world, double d, double d1, double d2, int arrowType) {
        super(world);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.field_28019_h = 0;
        this.inGround = false;
        this.doesArrowBelongToPlayer = false;
        this.arrowType = arrowType;
        this.arrowShake = 0;
        this.ticksInAir = 0;
        this.setSize(0.5F, 0.5F);
        /*this.setPosition(d, d1, d2);
        this.yOffset = 0.0F;*/
    }

    public EntityEnergyOrb(World world, EntityLiving entityliving, boolean doesArrowBelongToPlayer, int arrowType) {
        super(world);
        /*this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.inTile = 0;
        this.field_28019_h = 0;
        this.inGround = false;
        this.doesArrowBelongToPlayer = doesArrowBelongToPlayer;
        this.arrowType = arrowType;
        this.arrowShake = 0;
        this.ticksInAir = 0;
        this.owner = entityliving;
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(entityliving.posX, entityliving.posY - (entityliving.height*0.30), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * 0.16F;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.xd = -MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F);
        this.zd = MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F);
        this.yd = -MathHelper.sin(this.rotationPitch / 180.0F * 3.141593F);
        this.setArrowHeading(this.xd, this.yd, this.zd, 1.5F, 1.0F);*/
    }

    /*public void setArrowHeading(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += this.random.nextGaussian() * 0.007499999832361937 * (double)f1;
        d1 += this.random.nextGaussian() * 0.007499999832361937 * (double)f1;
        d2 += this.random.nextGaussian() * 0.007499999832361937 * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        this.xd = d;
        this.yd = d1;
        this.zd = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0 / 3.1415927410125732);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, f3) * 180.0 / 3.1415927410125732);
        this.ticksInGround = 0;
    }

    public void setVelocity(double d, double d1, double d2) {
        this.xd = d;
        this.yd = d1;
        this.zd = d2;
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0 / 3.1415927410125732);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, f) * 180.0 / 3.1415927410125732);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }

    }

    public void onUpdate() {
        ++this.ticksInAir;
        if(ticksInAir > 1200){
            this.setEntityDead();
        }
        super.onUpdate();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.xd * this.xd + this.zd * this.zd);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.xd, this.zd) * 180.0 / 3.1415927410125732);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.yd, f) * 180.0 / 3.1415927410125732);
        }

        int i = this.world.getBlockId(this.xTile, this.yTile, this.zTile);
        if (i > 0) {
            Block.blocksList[i].setBlockBoundsBasedOnState(this.world, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB axisalignedbb = Block.blocksList[i].getCollisionBoundingBoxFromPool(this.world, this.xTile, this.yTile, this.zTile);
            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3d.createVector(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            int j = this.world.getBlockId(this.xTile, this.yTile, this.zTile);
            int k = this.world.getBlockMetadata(this.xTile, this.yTile, this.zTile);
            if (j == this.inTile && k == this.field_28019_h) {
                ++this.ticksInGround;
                if (this.ticksInGround > 1200) {
                    this.setEntityDead();
                }

            } else {
                this.inGround = false;
                this.xd *= this.random.nextFloat() * 0.2F;
                this.yd *= this.random.nextFloat() * 0.2F;
                this.zd *= this.random.nextFloat() * 0.2F;
            }
        } else {
            Vec3d vec3d = Vec3d.createVector(this.posX, this.posY, this.posZ);
            Vec3d vec3d1 = Vec3d.createVector(this.posX + this.xd, this.posY + this.yd, this.posZ + this.zd);
            MovingObjectPosition movingobjectposition = this.world.func_28105_a(vec3d, vec3d1, false, true);
            vec3d = Vec3d.createVector(this.posX, this.posY, this.posZ);
            vec3d1 = Vec3d.createVector(this.posX + this.xd, this.posY + this.yd, this.posZ + this.zd);
            if (movingobjectposition != null) {
                vec3d1 = Vec3d.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.xd, this.yd, this.zd).expand(1.0, 1.0, 1.0));
            double d = 0.0;

            float f5;
            for(int l = 0; l < list.size(); ++l) {
                Entity entity1 = (Entity)list.get(l);
                if (entity1.canBeCollidedWith() && (entity1 != this.owner || this.ticksInAir >= 5)) {
                    f5 = 0.3F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand(f5, f5, f5);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.func_1169_a(vec3d, vec3d1);
                    if (movingobjectposition1 != null) {
                        double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
                        if (d1 < d || d == 0.0) {
                            entity = entity1;
                            d = d1;
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            float f1;
            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null) {
                    if (movingobjectposition.entityHit.attackEntityFrom(this.owner, this.arrowDamage, DamageType.COMBAT)) {

                        this.world.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                        //if (!(this instanceof EntityArrowGolden)) {
                            this.setEntityDead();
                        //}
                    } else if (true) {
                        this.xd *= -0.10000000149011612;
                        this.yd *= -0.10000000149011612;
                        this.zd *= -0.10000000149011612;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                    }
                } else {
                    this.xTile = movingobjectposition.blockX;
                    this.yTile = movingobjectposition.blockY;
                    this.zTile = movingobjectposition.blockZ;
                    this.inTile = this.world.getBlockId(this.xTile, this.yTile, this.zTile);
                    this.field_28019_h = this.world.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.xd = (float)(movingobjectposition.hitVec.xCoord - this.posX);
                    this.yd = (float)(movingobjectposition.hitVec.yCoord - this.posY);
                    this.zd = (float)(movingobjectposition.hitVec.zCoord - this.posZ);
                    f1 = MathHelper.sqrt_double(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd);
                    this.posX -= this.xd / (double)f1 * 0.05000000074505806;
                    this.posY -= this.yd / (double)f1 * 0.05000000074505806;
                    this.posZ -= this.zd / (double)f1 * 0.05000000074505806;
                    this.inGroundAction();
                }
            }

            this.posX += this.xd;
            this.posY += this.yd;
            this.posZ += this.zd;
            f1 = MathHelper.sqrt_double(this.xd * this.xd + this.zd * this.zd);
            this.rotationYaw = (float)(Math.atan2(this.xd, this.zd) * 180.0 / 3.1415927410125732);

            for(this.rotationPitch = (float)(Math.atan2(this.yd, f1) * 180.0 / 3.1415927410125732); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            }

            while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f3 = this.arrowSpeed;
            f5 = this.arrowGravity;
            if (this.isInWater()) {
                for(int i1 = 0; i1 < 4; ++i1) {
                    float f6 = 0.25F;
                    this.world.spawnParticle("bubble", this.posX - this.xd * (double)f6, this.posY - this.yd * (double)f6, this.posZ - this.zd * (double)f6, this.xd, this.yd, this.zd);
                }

                f3 = 0.8F;
            }

            this.xd *= f3;
            this.yd *= f3;
            this.zd *= f3;
            this.yd -= f5;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    protected void inGroundAction() {
        this.world.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.arrowShake = 7;
        this.setEntityDead();
    }

    public void writeEntityToNBT(CompoundTag nbttagcompound) {
        nbttagcompound.putShort("xTile", (short)this.xTile);
        nbttagcompound.putShort("yTile", (short)this.yTile);
        nbttagcompound.putShort("zTile", (short)this.zTile);
        nbttagcompound.putShort("inTile", (byte)this.inTile);
        nbttagcompound.putByte("inData", (byte)this.field_28019_h);
        nbttagcompound.putByte("shake", (byte)this.arrowShake);
        nbttagcompound.putByte("inGround", (byte)(this.inGround ? 1 : 0));
        nbttagcompound.putBoolean("player", this.doesArrowBelongToPlayer);
    }

    public void readEntityFromNBT(CompoundTag nbttagcompound) {
        this.xTile = nbttagcompound.getShort("xTile");
        this.yTile = nbttagcompound.getShort("yTile");
        this.zTile = nbttagcompound.getShort("zTile");
        this.inTile = nbttagcompound.getShort("inTile") & 16383;
        this.field_28019_h = nbttagcompound.getByte("inData") & 255;
        this.arrowShake = nbttagcompound.getByte("shake") & 255;
        this.inGround = nbttagcompound.getByte("inGround") == 1;
        this.doesArrowBelongToPlayer = nbttagcompound.getBoolean("player");
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer) {
    }

    public float getShadowSize() {
        return 0.0F;
    }*/
}
