package sunsetsatellite.signalindustries.util;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.data.gamerule.GameRules;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.IArmorWearing;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.joml.primitives.AABBd;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class ExplosionNoDrops {

    protected final @NotNull World world;
    public final @Nullable Entity exploder;
    public boolean isFlaming;
    protected Random ExplosionRNG;
    public double explosionX;
    public double explosionY;
    public double explosionZ;
    public float explosionSize;
    public Set<ChunkPosition> destroyedBlockPositions;
    public boolean destroyBlocks;

    public ExplosionNoDrops(@NotNull World world, @Nullable Entity exploder, double x, double y, double z, float explosionSize) {
        this.isFlaming = false;
        this.ExplosionRNG = new Random();
        this.destroyedBlockPositions = new HashSet<>();
        this.world = world;
        this.exploder = exploder;
        this.explosionSize = explosionSize;
        this.explosionX = x;
        this.explosionY = y;
        this.explosionZ = z;
        this.destroyBlocks = true;
        if (!world.getGameRuleValue(GameRules.MOB_GRIEFING)) {
            this.destroyBlocks = exploder == null || exploder instanceof Player;
        }
    }

    public void explode() {
        calculateBlocksToDestroy();

        damageEntities();

        if (this.destroyBlocks && this.isFlaming) createFire();
    }

    public void addEffects(boolean particles) {
        if (!this.world.isClientSide) {
            this.world.playSoundEffect(null, SoundCategory.WORLD_SOUNDS, this.explosionX, this.explosionY, this.explosionZ, "random.explode", 4F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        }
        List<ChunkPosition> arraylist = new ArrayList<>(this.destroyedBlockPositions);
        for (int i = arraylist.size() - 1; i >= 0; i--) {
            ChunkPosition chunkposition = arraylist.get(i);

            int x = chunkposition.x;
            int y = chunkposition.y;
            int z = chunkposition.z;

            TileEntity tileEntity = this.world.getTileEntity(x, y, z);
            if (particles) {
                double xPos = (double) x + this.world.rand.nextFloat();
                double yPos = (double) y + this.world.rand.nextFloat();
                double zPos = (double) z + this.world.rand.nextFloat();
                double d3 = xPos - this.explosionX;
                double d4 = yPos - this.explosionY;
                double d5 = zPos - this.explosionZ;
                double d6 = MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
                d3 /= d6;
                d4 /= d6;
                d5 /= d6;
                double d7 = 0.5D / (d6 / (double) this.explosionSize + 0.1D);
                d7 *= this.world.rand.nextFloat() * this.world.rand.nextFloat() + 0.3F;
                d3 *= d7;
                d4 *= d7;
                d5 *= d7;
                this.world.spawnParticle("explode", (xPos + this.explosionX) / 2D, (yPos + this.explosionY) / 2D, (zPos + this.explosionZ) / 2D, d3, d4, d5, 0, false);
                this.world.spawnParticle("smoke", xPos, yPos, zPos, d3, d4, d5, 0, false);
            }
            if (this.destroyBlocks) {
                Block<?> block = this.world.getBlock(x, y, z);
                if (block != null) {
                    //block.dropBlockWithCause(this.world, EnumDropCause.EXPLOSION, x, y, z, this.world.getBlockMetadata(x, y, z), tileEntity, null);
                    this.world.setBlockWithNotify(x, y, z, 0);
                    //block.onBlockDestroyedByExplosion(this.world, x, y, z);
                }
            }
        }
    }

    protected void calculateBlocksToDestroy() {
        int i = 16;
        for (int j = 0; j < i; j++) {
            for (int l = 0; l < i; l++) {
                label0:
                for (int j1 = 0; j1 < i; j1++) {
                    if (j != 0 && j != i - 1 && l != 0 && l != i - 1 && j1 != 0 && j1 != i - 1) {
                        continue;
                    }
                    double d = ((float) j / ((float) i - 1.0F)) * 2.0F - 1.0F;
                    double d1 = ((float) l / ((float) i - 1.0F)) * 2.0F - 1.0F;
                    double d2 = ((float) j1 / ((float) i - 1.0F)) * 2.0F - 1.0F;
                    double d3 = java.lang.Math.sqrt(d * d + d1 * d1 + d2 * d2);
                    d /= d3;
                    d1 /= d3;
                    d2 /= d3;
                    float f1 = this.explosionSize * (0.7F + this.world.rand.nextFloat() * 0.6F);
                    double d5 = this.explosionX;
                    double d7 = this.explosionY;
                    double d9 = this.explosionZ;
                    float f2 = 0.3F;
                    while (true) {
                        if (f1 <= 0.0F) {
                            continue label0;
                        }
                        int j4 = MathHelper.floor(d5);
                        int k4 = MathHelper.floor(d7);
                        int l4 = MathHelper.floor(d9);
                        int i5 = this.world.getBlockId(j4, k4, l4);
                        if (i5 > 0) {
                            f1 -= (Blocks.blocksList[i5].getBlastResistance(this.exploder) + 0.3F) * f2;
                        }
                        if (f1 > 0.0F) {
                            this.destroyedBlockPositions.add(new ChunkPosition(j4, k4, l4));
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
        float explosionSize2 = this.explosionSize * 2.0f;

        int x1 = MathHelper.floor(this.explosionX - (double) explosionSize2 - 1.0D);
        int x2 = MathHelper.floor(this.explosionX + (double) explosionSize2 + 1.0D);
        int y1 = MathHelper.floor(this.explosionY - (double) explosionSize2 - 1.0D);
        int y2 = MathHelper.floor(this.explosionY + (double) explosionSize2 + 1.0D);
        int z1 = MathHelper.floor(this.explosionZ - (double) explosionSize2 - 1.0D);
        int z2 = MathHelper.floor(this.explosionZ + (double) explosionSize2 + 1.0D);

        List<Entity> list = new ArrayList<>(this.world.getEntitiesWithinAABBExcludingEntity(this.exploder, new AABBd(x1, y1, z1, x2, y2, z2)));
        Vector3dc vec3 = new Vector3d(this.explosionX, this.explosionY, this.explosionZ);
        for (Entity entity : list) {
            double d4 = entity.distanceTo(this.explosionX, this.explosionY, this.explosionZ) / (double) explosionSize2;
            if (d4 <= 1.0D) {
                double xComp = entity.x - this.explosionX;
                double yComp = entity.y - this.explosionY;
                double zComp = entity.z - this.explosionZ;
                double distance = MathHelper.sqrt(xComp * xComp + yComp * yComp + zComp * zComp);
                xComp /= distance;
                yComp /= distance;
                zComp /= distance;
                double d12 = this.world.getSeenPercent(vec3, entity.bb);
                double d13 = (1.0D - d4) * d12;
                entity.hurt(this.exploder, (int) (((d13 * d13 + d13) / 2D) * 8D * (double) explosionSize2 + 1.0D), DamageType.BLAST);
                double flingForce = d13 * 2;
                if (entity instanceof IArmorWearing) {
                    float proc = 1 - (((IArmorWearing<?>) entity).getTotalProtectionAmount(DamageType.BLAST) / 2f);
                    flingForce *= proc;
                }
                if (!entity.noPhysics) {
                    entity.fling(xComp * flingForce, yComp * flingForce, zComp * flingForce, 1);
                }
            }
        }
    }

    protected void createFire() {
        List<ChunkPosition> arraylist = new ArrayList<>(this.destroyedBlockPositions);
        for (int l2 = arraylist.size() - 1; l2 >= 0; l2--) {
            ChunkPosition chunkposition = arraylist.get(l2);

            int x1 = chunkposition.x;
            int y1 = chunkposition.y;
            int z1 = chunkposition.z;

            if (this.world.getBlockId(x1, y1, z1) == Blocks.AIR.id() && Blocks.solid[this.world.getBlockId(x1, y1 - 1, z1)] && this.ExplosionRNG.nextInt(3) == 0) {
                this.world.setBlockWithNotify(x1, y1, z1, Blocks.FIRE.id());
            }
        }
    }
}
