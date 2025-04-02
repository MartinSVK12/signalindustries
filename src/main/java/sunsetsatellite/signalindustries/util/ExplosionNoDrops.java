package sunsetsatellite.signalindustries.util;


import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;

import java.util.*;

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

	public ExplosionNoDrops(World world, Entity entity, double x, double y, double z, float explosionSize) {
		isFlaming = false;
		ExplosionRNG = new Random();
		destroyedBlockPositions = new HashSet<>();
		worldObj = world;
		exploder = entity;
		this.explosionSize = explosionSize;
		explosionX = x;
		explosionY = y;
		explosionZ = z;
		destroyBlocks = true;
		if(!world.getGameRuleValue(GameRules.MOB_GRIEFING)) {
		    destroyBlocks = entity == null || entity instanceof Player;
		}
	}

	public void explode() {
	    if(destroyBlocks) calculateBlocksToDestroy();
	    
		damageEntities();
		
		if(destroyBlocks && isFlaming) createFire();
	}

    public void addEffects(boolean particles) {
    	if(!worldObj.isClientSide) {
    		worldObj.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, explosionX, explosionY, explosionZ, "random.explode", 4F, (1.0F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2F) * 0.7F);
    	}
        List<ChunkPosition> arraylist = new ArrayList<>(destroyedBlockPositions);
        for (int i = arraylist.size() - 1; i >= 0; i--) {
            ChunkPosition chunkposition = arraylist.get(i);
            
            int x = chunkposition.x;
            int y = chunkposition.y;
            int z = chunkposition.z;
            
            int id = worldObj.getBlockId(x, y, z);
            TileEntity tileEntity = worldObj.getTileEntity(x, y, z);
            if(particles) {
                double xPos = (double) x + worldObj.rand.nextFloat();
                double yPos = (double) y + worldObj.rand.nextFloat();
                double zPos = (double) z + worldObj.rand.nextFloat();
                double d3 = xPos - explosionX;
                double d4 = yPos - explosionY;
                double d5 = zPos - explosionZ;
                double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                d3 /= d6;
                d4 /= d6;
                d5 /= d6;
                double d7 = 0.5D / (d6 / (double) explosionSize + 0.1D);
                d7 *= worldObj.rand.nextFloat() * worldObj.rand.nextFloat() + 0.3F;
                d3 *= d7;
                d4 *= d7;
                d5 *= d7;
                worldObj.spawnParticle("explode", (xPos + explosionX) / 2D, (yPos + explosionY) / 2D, (zPos + explosionZ) / 2D, d3, d4, d5, 0);
                worldObj.spawnParticle("smoke", xPos, yPos, zPos, d3, d4, d5, 0);
            }
            if(id > 0)
            {
                //Blocks.blocksList[id].dropBlockWithCause(worldObj, EnumDropCause.EXPLOSION, x, y, z, worldObj.getBlockMetadata(x, y, z), tileEntity, null);
                worldObj.setBlockWithNotify(x, y, z, 0);
                //Blocks.blocksList[id].onBlockDestroyedByExplosion(worldObj, x, y, z);
            }
        }
    }
	
	protected void calculateBlocksToDestroy() {
        int i = 16;
        for (int j = 0; j < i; j++) {
            for (int l = 0; l < i; l++) {
                label0: for (int j1 = 0; j1 < i; j1++) {
                    if(j != 0 && j != i - 1 && l != 0 && l != i - 1 && j1 != 0 && j1 != i - 1) {
                        continue;
                    }
                    double d = ((float) j / ((float) i - 1.0F)) * 2.0F - 1.0F;
                    double d1 = ((float) l / ((float) i - 1.0F)) * 2.0F - 1.0F;
                    double d2 = ((float) j1 / ((float) i - 1.0F)) * 2.0F - 1.0F;
                    double d3 = java.lang.Math.sqrt(d * d + d1 * d1 + d2 * d2);
                    d /= d3;
                    d1 /= d3;
                    d2 /= d3;
                    float f1 = explosionSize * (0.7F + worldObj.rand.nextFloat() * 0.6F);
                    double d5 = explosionX;
                    double d7 = explosionY;
                    double d9 = explosionZ;
                    float f2 = 0.3F;
                    while(true) {
                        if(f1 <= 0.0F) {
                            continue label0;
                        }
                        int j4 = MathHelper.floor(d5);
                        int k4 = MathHelper.floor(d7);
                        int l4 = MathHelper.floor(d9);
                        int i5 = worldObj.getBlockId(j4, k4, l4);
                        if(i5 > 0) {
                            f1 -= (Blocks.blocksList[i5].getBlastResistance(exploder) + 0.3F) * f2;
                        }
                        if(f1 > 0.0F) {
                            destroyedBlockPositions.add(new ChunkPosition(j4, k4, l4));
                        }
                        d5 += d * (double) f2;
                        d7 += d1 * (double) f2;
                        d9 += d2 * (double) f2;
                        f1 -= f2 * 0.75F;
                    }
                }
            }
        }
	}
	
	protected void damageEntities() {
        float explosionSize2 = explosionSize * 2.0f;
        
        int x1 = MathHelper.floor(explosionX - (double) explosionSize2 - 1.0D);
        int x2 = MathHelper.floor(explosionX + (double) explosionSize2 + 1.0D);
        int y1 = MathHelper.floor(explosionY - (double) explosionSize2 - 1.0D);
        int y2 = MathHelper.floor(explosionY + (double) explosionSize2 + 1.0D);
        int z1 = MathHelper.floor(explosionZ - (double) explosionSize2 - 1.0D);
        int z2 = MathHelper.floor(explosionZ + (double) explosionSize2 + 1.0D);
        
        List<Entity> list = new ArrayList<>(worldObj.getEntitiesWithinAABBExcludingEntity(exploder, AABB.getTemporaryBB(x1, y1, z1, x2, y2, z2)));
        Vec3 vec3 = Vec3.getTempVec3(explosionX, explosionY, explosionZ);
        for (Entity entity : list) {
            double d4 = entity.distanceTo(explosionX, explosionY, explosionZ) / (double) explosionSize2;
            if (d4 <= 1.0D) {
                double xComp = entity.x - explosionX;
                double yComp = entity.y - explosionY;
                double zComp = entity.z - explosionZ;
                double distance = MathHelper.sqrt(xComp * xComp + yComp * yComp + zComp * zComp);
                xComp /= distance;
                yComp /= distance;
                zComp /= distance;
                double d12 = worldObj.getSeenPercent(vec3, entity.bb);
                double d13 = (1.0D - d4) * d12;
                entity.hurt(exploder, (int) (((d13 * d13 + d13) / 2D) * 8D * (double) explosionSize2 + 1.0D), DamageType.BLAST);
                double flingForce = d13 * 2;
                entity.fling(xComp * flingForce, yComp * flingForce, zComp * flingForce, 1);
            }
        }
	}

	protected void createFire() {
        List<ChunkPosition> arraylist = new ArrayList<>(destroyedBlockPositions);
		for (int l2 = arraylist.size() - 1; l2 >= 0; l2--) {
			ChunkPosition chunkposition = arraylist.get(l2);
			
			int x1 = chunkposition.x;
			int y1 = chunkposition.y;
			int z1 = chunkposition.z;
			
			if(worldObj.getBlockId(x1, y1, z1) == 0 && Blocks.solid[worldObj.getBlockId(x1, y1 - 1, z1)] && ExplosionRNG.nextInt(3) == 0) {
				worldObj.setBlockWithNotify(x1, y1, z1, Blocks.FIRE.id());
			}
		}
	}
}