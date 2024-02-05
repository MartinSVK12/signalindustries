package sunsetsatellite.signalindustries.entities.mob;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.EntityMonster;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;

public class EntityInfernal extends EntityMonster {
    public EntityInfernal(World world) {
        super(world);
        this.skinName = "infernal";
        this.moveSpeed = 0.5F;
        this.attackStrength = 5;
        this.scoreValue = 300;
        this.fireImmune = true;
    }

    public void onLivingUpdate() {
        //this.remainingFireTicks = 300;
        super.onLivingUpdate();
    }

    @Override
    protected void attackEntity(Entity entity, float distance) {
        if (this.attackTime <= 0 && distance < 2.0F && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
            entity.remainingFireTicks = 100;
            entity.maxFireTicks = 100;
        }
        this.attackStrength = 10;
        super.attackEntity(entity, distance);
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