package sunsetsatellite.signalindustries.entities;

import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobMonster;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SIWeather;

public class MobInfernal extends MobMonster {
    public MobInfernal(World world) {
        super(world);
        this.textureIdentifier = NamespaceID.fromPool("signalindustries", "infernal");
        this.moveSpeed = 0.5F;
        this.attackStrength = 5;
        this.scoreValue = 1000;
        this.fireImmune = true;
        this.setHealthRaw(40);
        mobDrops.add(new WeightedRandomLootObject(SIItems.infernalFragment.getDefaultStack(), 1, 2));
    }

    private int beamsLaunched = 0;
    private int beamCooldown = 0;
    private DamageType lastDamageType = null;
    public boolean eclipseImmune = false;

    @Override
    protected void attackEntity(@NotNull Entity entity, float distance) {
        if (beamCooldown > 0) {
            this.moveSpeed = 0.75f;
        } else {
            this.moveSpeed = 0.40f;
        }
        //SignalIndustries.LOGGER.info(String.valueOf(moveSpeed));
        double d = entity.x - x;
        double d1 = entity.z - z;
        if (attackTime <= 0) {
            if (!world.isClientSide) {
                if (beamCooldown > 0 && distance < 2.0F && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
                    entity.remainingFireTicks = 100;
                    entity.maxFireTicks = 100;
                    entity.hurt(this, attackStrength, DamageType.COMBAT);
                    yRot = (float) ((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
                    hasAttacked = true;
                } else if (beamCooldown <= 0 && distance < 10F) {
                    ProjectileSunbeam entityarrow = new ProjectileSunbeam(world, this);//new EntityArrow(world, this, false, 0);
                    entityarrow.y += 0.3999999761581421D;
                    double d2 = (entity.y + (double) entity.getHeadHeight()) - 0.20000000298023224D - entityarrow.y;
                    float f1 = MathHelper.sqrt(d * d + d1 * d1) * 0.2F;
                    world.playSoundAtEntity(this, this, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
                    world.entityJoinedWorld(entityarrow);
                    entityarrow.setHeading(d, d2, d1, 0.6F, 12F);
                    beamsLaunched++;
                    yRot = (float) ((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
                    hasAttacked = true;
                }
            }
            if (beamsLaunched < 10) {
                if (beamCooldown > 0) {
                    attackTime = 30;
                } else {
                    attackTime = 15;
                }
            } else {
                beamsLaunched = 0;
                beamCooldown = 240;
            }
        }
    }

    @Override
    public boolean hurt(Entity attacker, int i, DamageType type) {
        if (attacker instanceof MobInfernal) {
            return false;
        }
        lastDamageType = type;
        return super.hurt(attacker, i, type);
    }

    @Override
    public int getMaxHealth() {
        return 40;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (beamCooldown > 0) {
            beamCooldown--;
        }
        if (target instanceof MobInfernal) {
            target = null;
        }
        if (isInWaterOrRain()) {
            hurt(null, 1, DamageType.DROWN);
        }
        if (world.getCurrentWeather() != SIWeather.weatherEclipse && !eclipseImmune) {
            hurt(null, 4, DamageType.DROWN);
        }
    }

    @Override
    public void onDeath(Entity entity) {
        if (scoreValue >= 0 && entity != null) {
            entity.awardKillScore(this, scoreValue);
        }
        if (entity != null) {
            entity.killed(this);
        }
        dead = true;
        if (!world.isClientSide) {
            if ((lastDamageType == null || !lastDamageType.equals(DamageType.DROWN)) && !eclipseImmune) {
                dropDeathItems();
            }
        }
        world.sendTrackedEntityStatusUpdatePacket(this, (byte) 3);
    }

    @Override
    public @NotNull String getDefaultEntityTexture() {
        return "/assets/signalindustries/textures/entity/infernal/0.png";
    }

    @Override
    public @NonNull String getEntityTexture() {
        return "/assets/signalindustries/textures/entity/infernal/0.png";
    }

    public String getLivingSound() {
        return "fire.fire";
    }

    @Override
    protected String getHurtSound() {
        return "random.fizz";
    }

    protected String getDeathSound() {
        return "random.fizz";
    }
}
