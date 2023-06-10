package sunsetsatellite.signalindustries.entities;

import net.minecraft.src.*;
import net.minecraft.src.helper.DamageType;

import java.util.*;

//TODO: Merge with ExplosionEnergy to make a customizable Explosion class
public class ExplosionNoDrops {
    public boolean isFlaming;

    protected Random ExplosionRNG;

    protected World worldObj;

    public double explosionX;

    public double explosionY;

    public double explosionZ;

    public Entity exploder;

    public float explosionSize;

    public Set<ChunkPosition> destroyedBlockPositions;

    public boolean destroyBlocks;

    public ExplosionNoDrops(World world, Entity entity, double d, double d1, double d2, float f) {
        this.isFlaming = false;
        this.ExplosionRNG = new Random();
        this.destroyedBlockPositions = new HashSet<>();
        this.worldObj = world;
        this.exploder = entity;
        this.explosionSize = f;
        this.explosionX = d;
        this.explosionY = d1;
        this.explosionZ = d2;
        this.destroyBlocks = true;
        if (!world.mobGriefing)
            this.destroyBlocks = (entity == null || entity instanceof EntityPlayer);
    }

    public void doExplosionA() {
        if (this.destroyBlocks)
            calculateBlocksToDestroy();
        damageEntities();
        if (this.destroyBlocks && this.isFlaming)
            createFire();
    }

    public void doExplosionB(boolean particles) {
        this.worldObj.playSoundEffect(this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4.0F, (1.0F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
        List<ChunkPosition> arraylist = new ArrayList<>();
        arraylist.addAll(this.destroyedBlockPositions);
        for (int i = arraylist.size() - 1; i >= 0; i--) {
            ChunkPosition chunkposition = arraylist.get(i);
            int j = chunkposition.x;
            int k = chunkposition.y;
            int l = chunkposition.z;
            int id = this.worldObj.getBlockId(j, k, l);
            if (particles) {
                double d = (j + this.worldObj.rand.nextFloat());
                double d1 = (k + this.worldObj.rand.nextFloat());
                double d2 = (l + this.worldObj.rand.nextFloat());
                double d3 = d - this.explosionX;
                double d4 = d1 - this.explosionY;
                double d5 = d2 - this.explosionZ;
                double d6 = MathHelper.sqrt_double(d3 * d3 + d4 * d4 + d5 * d5);
                d3 /= d6;
                d4 /= d6;
                d5 /= d6;
                double d7 = 0.5D / (d6 / this.explosionSize + 0.1D);
                d7 *= (this.worldObj.rand.nextFloat() * this.worldObj.rand.nextFloat() + 0.3F);
                d3 *= d7;
                d4 *= d7;
                d5 *= d7;
                this.worldObj.spawnParticle("explode", (d + this.explosionX * 1.0D) / 2.0D, (d1 + this.explosionY * 1.0D) / 2.0D, (d2 + this.explosionZ * 1.0D) / 2.0D, d3, d4, d5);
                this.worldObj.spawnParticle("smoke", d, d1, d2, d3, d4, d5);
            }
            if (id > 0) {
                //Block.blocksList[id].dropBlockAsItemWithChance(this.worldObj, j, k, l, this.worldObj.getBlockMetadata(j, k, l), 1.0F);
                this.worldObj.setBlockWithNotify(j, k, l, 0);
                Block.blocksList[id].onBlockDestroyedByExplosion(this.worldObj, j, k, l);
            }
        }
    }

    protected void calculateBlocksToDestroy() {
        int i = 16;
        for (int j = 0; j < i; j++) {
            for (int l = 0; l < i; l++) {
                for (int j1 = 0; j1 < i; j1++) {
                    if (j == 0 || j == i - 1 || l == 0 || l == i - 1 || j1 == 0 || j1 == i - 1) {
                        double d = (j / (i - 1.0F) * 2.0F - 1.0F);
                        double d1 = (l / (i - 1.0F) * 2.0F - 1.0F);
                        double d2 = (j1 / (i - 1.0F) * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d * d + d1 * d1 + d2 * d2);
                        d /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f1 = this.explosionSize * (0.7F + this.worldObj.rand.nextFloat() * 0.6F);
                        double d5 = this.explosionX;
                        double d7 = this.explosionY;
                        double d9 = this.explosionZ;
                        float f2 = 0.3F;
                        while (f1 > 0.0F) {
                            int j4 = MathHelper.floor_double(d5);
                            int k4 = MathHelper.floor_double(d7);
                            int l4 = MathHelper.floor_double(d9);
                            int i5 = this.worldObj.getBlockId(j4, k4, l4);
                            if (i5 > 0)
                                f1 -= (Block.blocksList[i5].getExplosionResistance(this.exploder) + 0.3F) * f2;
                            if (f1 > 0.0F)
                                this.destroyedBlockPositions.add(new ChunkPosition(j4, k4, l4));
                            d5 += d * f2;
                            d7 += d1 * f2;
                            d9 += d2 * f2;
                            f1 -= f2 * 0.75F;
                        }
                    }
                }
            }
        }
    }

    protected void damageEntities() {
        float explosionSize2 = this.explosionSize * 2.0F;
        int x1 = MathHelper.floor_double(this.explosionX - explosionSize2 - 1.0D);
        int x2 = MathHelper.floor_double(this.explosionX + explosionSize2 + 1.0D);
        int y1 = MathHelper.floor_double(this.explosionY - explosionSize2 - 1.0D);
        int y2 = MathHelper.floor_double(this.explosionY + explosionSize2 + 1.0D);
        int z1 = MathHelper.floor_double(this.explosionZ - explosionSize2 - 1.0D);
        int z2 = MathHelper.floor_double(this.explosionZ + explosionSize2 + 1.0D);
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder, AxisAlignedBB.getBoundingBoxFromPool(x1, y1, z1, x2, y2, z2));
        Vec3D vec3d = Vec3D.createVector(this.explosionX, this.explosionY, this.explosionZ);
        for (int k2 = 0; k2 < list.size(); k2++) {
            Entity entity = list.get(k2);
            double d4 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / explosionSize2;
            if (d4 <= 1.0D) {
                double d6 = entity.posX - this.explosionX;
                double d8 = entity.posY - this.explosionY;
                double d10 = entity.posZ - this.explosionZ;
                double d11 = MathHelper.sqrt_double(d6 * d6 + d8 * d8 + d10 * d10);
                d6 /= d11;
                d8 /= d11;
                d10 /= d11;
                double d12 = this.worldObj.func_675_a(vec3d, entity.boundingBox);
                double d13 = (1.0D - d4) * d12;
                entity.attackEntityFrom(this.exploder, (int)((d13 * d13 + d13) / 2.0D * 8.0D * explosionSize2 + 1.0D), DamageType.BLAST);
                double d14 = d13;
                entity.motionX += d6 * d14;
                entity.motionY += d8 * d14;
                entity.motionZ += d10 * d14;
            }
        }
    }

    protected void createFire() {
        List<ChunkPosition> arraylist = new ArrayList<>();
        arraylist.addAll(this.destroyedBlockPositions);
        for (int l2 = arraylist.size() - 1; l2 >= 0; l2--) {
            ChunkPosition chunkposition = arraylist.get(l2);
            int x1 = chunkposition.x;
            int y1 = chunkposition.y;
            int z1 = chunkposition.z;
            if (this.worldObj.getBlockId(x1, y1, z1) == 0 && Block.opaqueCubeLookup[this.worldObj.getBlockId(x1, y1 - 1, z1)] && this.ExplosionRNG.nextInt(3) == 0)
                this.worldObj.setBlockWithNotify(x1, y1, z1, Block.fire.blockID);
        }
    }
}
