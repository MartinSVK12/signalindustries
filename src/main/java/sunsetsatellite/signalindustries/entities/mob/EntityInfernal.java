package sunsetsatellite.signalindustries.entities.mob;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.entity.projectile.EntityArrow;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityEnergyOrb;
import sunsetsatellite.signalindustries.entities.EntitySunbeam;

public class EntityInfernal extends EntityMonster {
    public EntityInfernal(World world) {
        super(world);
        this.textureIdentifier = new NamespaceID("signalindustries", "infernal");
        this.moveSpeed = 0.5F;
        this.attackStrength = 5;
        this.scoreValue = 1000;
        this.fireImmune = true;
        this.setHealthRaw(40);
    }
    private int beamsLaunched = 0;
    private int beamCooldown = 0;
    private DamageType lastDamageType = null;
    public boolean eclipseImmune = false;

    public void onLivingUpdate() {
        //this.remainingFireTicks = 300;
        super.onLivingUpdate();
    }

    /*@Override
    protected void attackEntity(Entity entity, float distance) {
        if (this.attackTime <= 0 && distance < 2.0F && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
            entity.remainingFireTicks = 100;
            entity.maxFireTicks = 100;
        }
        this.attackStrength = 10;
        super.attackEntity(entity, distance);
    }*/

    @Override
    protected void attackEntity(Entity entity, float distance)
    {
        if(beamCooldown > 0){
            this.moveSpeed = 0.75f;
        } else {
            this.moveSpeed = 0.40f;
        }
        SignalIndustries.LOGGER.info(String.valueOf(moveSpeed));
        double d = entity.x - x;
        double d1 = entity.z - z;
        if(attackTime <= 0)
        {
            if(!world.isClientSide) {
                if(beamCooldown > 0 && distance < 2.0F && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
                    entity.remainingFireTicks = 100;
                    entity.maxFireTicks = 100;
                    entity.hurt(this, attackStrength, DamageType.COMBAT);
                    yRot = (float)((java.lang.Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
                    hasAttacked = true;
                } else if (beamCooldown <= 0 && distance < 10F){
                    EntitySunbeam entityarrow = new EntitySunbeam(world, this, false);//new EntityArrow(world, this, false, 0);
                    entityarrow.y += 0.3999999761581421D;
                    double d2 = (entity.y + (double) entity.getHeadHeight()) - 0.20000000298023224D - entityarrow.y;
                    float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
                    world.playSoundAtEntity(this, this, "random.bow", 1.0F, 1.0F / (random.nextFloat() * 0.4F + 0.8F));
                    world.entityJoinedWorld(entityarrow);
                    entityarrow.setArrowHeading(d, d2, d1, 0.6F, 12F);
                    beamsLaunched++;
                    yRot = (float)((java.lang.Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
                    hasAttacked = true;
                }
            }
            if(beamsLaunched < 10){
                if(beamCooldown > 0){
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
        if(attacker instanceof EntityInfernal){
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
        if(beamCooldown > 0){
            beamCooldown--;
        }
        if(entityToAttack instanceof EntityInfernal){
            entityToAttack = null;
        }
        if(isInWaterOrRain()){
            hurt(null,1, DamageType.DROWN);
        }
        if(world.getCurrentWeather() != SignalIndustries.weatherEclipse && !eclipseImmune){
            hurt(null,4, DamageType.DROWN);
        }
    }

    @Override
    public void onDeath(Entity entity)
    {
        if(scoreValue >= 0 && entity != null)
        {
            entity.awardKillScore(this, scoreValue);
        }
        if(entity != null)
        {
            entity.killed(this);
        }
        dead = true;
        if(!world.isClientSide)
        {
            if(!lastDamageType.equals(DamageType.DROWN) && !eclipseImmune){
                dropFewItems();
            }
        }
        world.sendTrackedEntityStatusUpdatePacket(this, (byte)3);
    }

    @Override
    public String getDefaultEntityTexture() {
        return "/assets/signalindustries/entity/infernal.png";
    }

    @Override
    public String getEntityTexture() {
        return "/assets/signalindustries/entity/infernal.png";
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

    protected int getDropItemId() {
        return SignalIndustries.infernalFragment.id;
    }
}