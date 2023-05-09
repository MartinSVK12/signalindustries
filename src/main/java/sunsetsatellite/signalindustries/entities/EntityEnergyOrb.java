package sunsetsatellite.signalindustries.entities;

import net.minecraft.src.*;
import net.minecraft.src.helper.DamageType;

import java.util.List;

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
        this.setPosition(d, d1, d2);
        this.yOffset = 0.0F;
    }

    public EntityEnergyOrb(World world, EntityLiving entityliving, boolean doesArrowBelongToPlayer, int arrowType) {
        super(world);
        this.xTile = -1;
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
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * 3.141593F);
        this.setArrowHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    protected void entityInit() {
        this.arrowGravity = 0.00F;
        this.arrowSpeed = 1F;
        this.arrowDamage = 5;
        if (!(this.owner instanceof EntityPlayer)) {
            this.doesArrowBelongToPlayer = false;
        }

    }

    public void setArrowHeading(double d, double d1, double d2, float f, float f1) {
        float f2 = MathHelper.sqrt_double(d * d + d1 * d1 + d2 * d2);
        d /= f2;
        d1 /= f2;
        d2 /= f2;
        d += this.rand.nextGaussian() * 0.007499999832361937 * (double)f1;
        d1 += this.rand.nextGaussian() * 0.007499999832361937 * (double)f1;
        d2 += this.rand.nextGaussian() * 0.007499999832361937 * (double)f1;
        d *= f;
        d1 *= f;
        d2 *= f;
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
        float f3 = MathHelper.sqrt_double(d * d + d2 * d2);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0 / 3.1415927410125732);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, f3) * 180.0 / 3.1415927410125732);
        this.ticksInGround = 0;
    }

    public void setVelocity(double d, double d1, double d2) {
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
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
        super.onUpdate();
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.1415927410125732);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, f) * 180.0 / 3.1415927410125732);
        }

        int i = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
        if (i > 0) {
            Block.blocksList[i].setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB axisalignedbb = Block.blocksList[i].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);
            if (axisalignedbb != null && axisalignedbb.isVecInside(Vec3D.createVector(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            int j = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            int k = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
            if (j == this.inTile && k == this.field_28019_h) {
                ++this.ticksInGround;
                if (this.ticksInGround == 1200) {
                    this.setEntityDead();
                }

            } else {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2F;
                this.motionY *= this.rand.nextFloat() * 0.2F;
                this.motionZ *= this.rand.nextFloat() * 0.2F;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            /*if (this instanceof EntityArrowGolden) {
                this.worldObj.spawnParticle("arrowtrail", this.posX, this.posY, this.posZ, this.motionX * 0.05000000074505806, this.motionY * 0.05000000074505806 - 0.10000000149011612, this.motionZ * 0.05000000074505806);
                this.worldObj.spawnParticle("arrowtrail", this.posX + this.motionX * 0.5, this.posY + this.motionY * 0.5, this.posZ + this.motionZ * 0.5, this.motionX * 0.05000000074505806, this.motionY * 0.05000000074505806 - 0.10000000149011612, this.motionZ * 0.05000000074505806);
            }*/

            ++this.ticksInAir;
            Vec3D vec3d = Vec3D.createVector(this.posX, this.posY, this.posZ);
            Vec3D vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_28105_a(vec3d, vec3d1, false, true);
            vec3d = Vec3D.createVector(this.posX, this.posY, this.posZ);
            vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
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

                        this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                        //if (!(this instanceof EntityArrowGolden)) {
                            this.setEntityDead();
                        //}
                    } else if (true) {
                        this.motionX *= -0.10000000149011612;
                        this.motionY *= -0.10000000149011612;
                        this.motionZ *= -0.10000000149011612;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                } else {
                    this.xTile = movingobjectposition.blockX;
                    this.yTile = movingobjectposition.blockY;
                    this.zTile = movingobjectposition.blockZ;
                    this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
                    this.field_28019_h = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (float)(movingobjectposition.hitVec.xCoord - this.posX);
                    this.motionY = (float)(movingobjectposition.hitVec.yCoord - this.posY);
                    this.motionZ = (float)(movingobjectposition.hitVec.zCoord - this.posZ);
                    f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)f1 * 0.05000000074505806;
                    this.posY -= this.motionY / (double)f1 * 0.05000000074505806;
                    this.posZ -= this.motionZ / (double)f1 * 0.05000000074505806;
                    this.inGroundAction();
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.1415927410125732);

            for(this.rotationPitch = (float)(Math.atan2(this.motionY, f1) * 180.0 / 3.1415927410125732); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f6, this.posY - this.motionY * (double)f6, this.posZ - this.motionZ * (double)f6, this.motionX, this.motionY, this.motionZ);
                }

                f3 = 0.8F;
            }

            this.motionX *= f3;
            this.motionY *= f3;
            this.motionZ *= f3;
            this.motionY -= f5;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    protected void inGroundAction() {
        this.worldObj.playSoundAtEntity(this, "random.drr", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
        this.inGround = true;
        this.arrowShake = 7;
        this.setEntityDead();
    }

    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short)this.xTile);
        nbttagcompound.setShort("yTile", (short)this.yTile);
        nbttagcompound.setShort("zTile", (short)this.zTile);
        nbttagcompound.setShort("inTile", (byte)this.inTile);
        nbttagcompound.setByte("inData", (byte)this.field_28019_h);
        nbttagcompound.setByte("shake", (byte)this.arrowShake);
        nbttagcompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        nbttagcompound.setBoolean("player", this.doesArrowBelongToPlayer);
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
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
        /*if (!this.worldObj.isMultiplayerAndNotHost) {
            if (this.inGround && this.doesArrowBelongToPlayer && this.arrowShake <= 0 && entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.ammoArrow, 1))) {
                this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityplayer.onItemPickup(this, Item.ammoArrow.itemID);
                this.setEntityDead();
            }

        }*/
    }

    public float getShadowSize() {
        return 0.0F;
    }
}
