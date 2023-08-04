package sunsetsatellite.signalindustries.entities;




import java.util.ArrayList;
import java.util.List;

public class EntityCrystal extends Entity {
    private int field_20056_b = -1;
    private int field_20055_c = -1;
    private int field_20054_d = -1;
    private int field_20053_e = 0;
    private boolean field_20052_f = false;
    public int field_20057_a = 0;
    public EntityLiving field_20051_g;
    private int field_20050_h;
    private int field_20049_i = 0;

    public EntityCrystal(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit() {
    }

    public boolean isInRangeToRenderDist(double d) {
        double d1 = this.boundingBox.getAverageEdgeLength() * 4.0;
        d1 *= 64.0;
        return d < d1 * d1;
    }

    public EntityCrystal(World world, EntityLiving entityliving) {
        super(world);
        this.field_20051_g = entityliving;
        this.setSize(0.25F, 0.25F);
        this.setLocationAndAngles(entityliving.posX, entityliving.posY + (double)entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * 0.16F;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * 0.16F;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        float f = 0.4F;
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F) * f;
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F) * f;
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * 3.141593F) * f;
        this.setCrystalHeading(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
    }

    public EntityCrystal(World world, double d, double d1, double d2) {
        super(world);
        this.field_20050_h = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(d, d1, d2);
        this.yOffset = 0.0F;
    }

    public void setCrystalHeading(double d, double d1, double d2, float f, float f1) {
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
        this.field_20050_h = 0;
    }

    public void setVelocity(double d, double d1, double d2) {
        this.motionX = d;
        this.motionY = d1;
        this.motionZ = d2;
        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt_double(d * d + d2 * d2);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(d, d2) * 180.0 / 3.1415927410125732);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(d1, f) * 180.0 / 3.1415927410125732);
        }

    }

    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        if (this.field_20057_a > 0) {
            --this.field_20057_a;
        }

        if (this.field_20052_f) {
            int i = this.worldObj.getBlockId(this.field_20056_b, this.field_20055_c, this.field_20054_d);
            if (i == this.field_20053_e) {
                ++this.field_20050_h;
                if (this.field_20050_h == 1200) {
                    this.setEntityDead();
                }

                return;
            }

            this.field_20052_f = false;
            this.motionX *= this.rand.nextFloat() * 0.2F;
            this.motionY *= this.rand.nextFloat() * 0.2F;
            this.motionZ *= this.rand.nextFloat() * 0.2F;
            this.field_20050_h = 0;
            this.field_20049_i = 0;
        } else {
            ++this.field_20049_i;
        }

        Vec3D vec3d = Vec3D.createVector(this.posX, this.posY, this.posZ);
        Vec3D vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3d, vec3d1);
        vec3d = Vec3D.createVector(this.posX, this.posY, this.posZ);
        vec3d1 = Vec3D.createVector(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (movingobjectposition != null) {
            vec3d1 = Vec3D.createVector(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
        }

        if (!this.worldObj.isMultiplayerAndNotHost) {
            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(3.0, 3.0, 3.0));
            double d = 0.0;

            for(int i1 = 0; i1 < list.size(); ++i1) {
                Entity entity1 = (Entity)list.get(i1);
               if (entity1.canBeCollidedWith() && (entity1 != this.field_20051_g || this.field_20049_i >= 5)) {
                    float f4 = 0.3F;
                    AxisAlignedBB axisalignedbb = entity1.boundingBox.expand(f4, f4, f4);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb.func_1169_a(vec3d, vec3d1);
                    if (movingobjectposition1 != null) {
                        double d1 = vec3d.distanceTo(movingobjectposition1.hitVec);
                        if (d1 < d || d == 0.0) {
                            entity = entity1;
                            d = d1;
                            //entity.attackEntityFrom(this.field_20051_g,20,DamageType.COMBAT);
                        }
                    }
                }
            }

            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
        }

        if (movingobjectposition != null) {
            if (movingobjectposition.entityHit != null && !movingobjectposition.entityHit.attackEntityFrom(this.field_20051_g, 0, DamageType.COMBAT)) {
                if(movingobjectposition.entityHit == this.field_20051_g){
                    movingobjectposition.entityHit.attackEntityFrom(this.field_20051_g, 8, DamageType.COMBAT);
                }
            }



            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(3.0, 3.0, 3.0));
            List<Entity> attacked = new ArrayList<>();
            for (Entity entity : list) {
                if(entity instanceof EntityLiving){
                    if (entity == this.field_20051_g && entity instanceof EntityPlayer) {
                        ((EntityPlayer) entity).heal(4);
                    } else if (entity.canBeCollidedWith() && (entity != this.field_20051_g || this.field_20049_i >= 5) && !attacked.contains(entity)) {
                        entity.attackEntityFrom(this.field_20051_g, 4, DamageType.COMBAT);
                        attacked.add(entity);
                    }
                }
            }

            worldObj.playSoundAtEntity(this, "signalindustries.crystalbreak", 0.5F, 1F / (this.rand.nextFloat() * 0.4F + 0.8F));


            int byte0;
            if (!this.worldObj.isMultiplayerAndNotHost && this.rand.nextInt(8) == 0) {
                byte0 = 1;
                if (this.rand.nextInt(32) == 0) {
                    byte0 = 4;
                }

                for(int k = 0; k < byte0; ++k) {
                    /*EntityChicken entitychicken = new EntityChicken(this.worldObj);
                    entitychicken.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                    this.worldObj.entityJoinedWorld(entitychicken);*/
                }
            }



            for(byte0 = 0; byte0 < 8; ++byte0) {
                this.worldObj.spawnParticle("dustcloud", this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0);
                for (int i = 0; i < 4; i++) {
                    this.worldObj.spawnParticle("crystalbreak", this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0);
                }
            }

            this.setEntityDead();
        }

        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / 3.1415927410125732);

        for(this.rotationPitch = (float)(Math.atan2(this.motionY, f) * 180.0 / 3.1415927410125732); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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
        float f1 = 0.99F;
        float f2 = 0.03F;
        if (this.isInWater()) {
            for(int l = 0; l < 4; ++l) {
                float f3 = 0.25F;
                this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f3, this.posY - this.motionY * (double)f3, this.posZ - this.motionZ * (double)f3, this.motionX, this.motionY, this.motionZ);
            }

            f1 = 0.8F;
        }

        this.motionX *= f1;
        this.motionY *= f1;
        this.motionZ *= f1;
        this.motionY -= f2;
        this.setPosition(this.posX, this.posY, this.posZ);
    }



    public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("xTile", (short)this.field_20056_b);
        nbttagcompound.setShort("yTile", (short)this.field_20055_c);
        nbttagcompound.setShort("zTile", (short)this.field_20054_d);
        nbttagcompound.setShort("inTile", (short)this.field_20053_e);
        nbttagcompound.setByte("shake", (byte)this.field_20057_a);
        nbttagcompound.setByte("inGround", (byte)(this.field_20052_f ? 1 : 0));
    }

    public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        this.field_20056_b = nbttagcompound.getShort("xTile");
        this.field_20055_c = nbttagcompound.getShort("yTile");
        this.field_20054_d = nbttagcompound.getShort("zTile");
        this.field_20053_e = nbttagcompound.getShort("inTile") & 16383;
        this.field_20057_a = nbttagcompound.getByte("shake") & 255;
        this.field_20052_f = nbttagcompound.getByte("inGround") == 1;
    }

    public void onCollideWithPlayer(EntityPlayer entityplayer) {
        if (this.field_20052_f && this.field_20051_g == entityplayer && this.field_20057_a <= 0 && entityplayer.inventory.addItemStackToInventory(new ItemStack(Item.ammoArrow, 1))) {
            this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            entityplayer.onItemPickup(this, 1);
            this.setEntityDead();
        }

    }

    public float getShadowSize() {
        return 0.0F;
    }
}
