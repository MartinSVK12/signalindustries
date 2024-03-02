package sunsetsatellite.signalindustries.inventories.machines;

import net.minecraft.client.Minecraft;
import net.minecraft.client.option.enums.Difficulty;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.monster.*;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;
import sunsetsatellite.catalyst.core.util.*;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.blocks.base.BlockContainerTiered;
import sunsetsatellite.signalindustries.entities.ExplosionEnergy;
import sunsetsatellite.signalindustries.entities.fx.EntityColorParticleFX;
import sunsetsatellite.signalindustries.entities.mob.EntityInfernal;
import sunsetsatellite.signalindustries.inventories.base.TileEntityWrathBeaconBase;
import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.signalindustries.util.Wave;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class TileEntityReinforcedWrathBeacon extends TileEntityWrathBeaconBase implements IMultiblock {
    public Random random = new Random();
    public Multiblock multiblock;
    public boolean intermission = false;
    public int wave = 0;
    public int currentMaxAmount = 0;
    public boolean started = false;
    public int ticksSinceStart = 0;
    public int enemiesSpawned = 0;
    public boolean suddenDeath = false;
    public ArrayList<EntityLiving> enemiesLeft = new ArrayList<>();
    public ArrayList<Wave> waves = new ArrayList<>();
    public EntityPlayer player;
    public TickTimer spawnTimer = new TickTimer(this,this::spawn,20,true);
    public TickTimer intermissionTimer = new TickTimer(this,this::startWave,300,false);
    public TickTimer checkTimer = new TickTimer(this,this::check,20,true);
    public TickTimer suddenDeathSpawnTImer = new TickTimer(this,this::suddenDeathSpawn,20,true);
    {
            suddenDeathSpawnTImer.pause();
            spawnTimer.pause();
            intermissionTimer.pause();
            checkTimer.pause();
    }

    public TileEntityReinforcedWrathBeacon(){
        //TODO: increase mobs in waves
        tier = Tier.REINFORCED;
        multiblock = Multiblock.multiblocks.get("wrathTree");
        ArrayList<Class<? extends EntityMonster>> mobList = new ArrayList<>();
        mobList.add(EntityCreeper.class);
        waves.add(new Wave(mobList,4,6,20));
        mobList = new ArrayList<>();
        mobList.add(EntityZombie.class);
        mobList.add(EntitySkeleton.class);
        waves.add(new Wave(mobList,10,16,20));
        waves.add(new Wave(mobList,10,16,20));
        waves.add(new Wave(mobList,10,16,20));
        mobList = new ArrayList<>();
        mobList.add(EntityZombie.class);
        mobList.add(EntitySkeleton.class);
        mobList.add(EntitySpider.class);
        waves.add(new Wave(mobList,10,16,20));
        mobList = new ArrayList<>();
        mobList.add(EntityCreeper.class);
        mobList.add(EntityInfernal.class);
        waves.add(new Wave(mobList,10,16,20));
        mobList = new ArrayList<>();
        mobList.add(EntityZombie.class);
        mobList.add(EntitySkeleton.class);
        mobList.add(EntitySpider.class);
        waves.add(new Wave(mobList,16,24,20));
        waves.add(new Wave(mobList,16,24,20));
        mobList = new ArrayList<>();
        mobList.add(EntityZombie.class);
        mobList.add(EntitySkeleton.class);
        mobList.add(EntitySpider.class);
        mobList.add(EntityCreeper.class);
        waves.add(new Wave(mobList,16,24,20));
        //final wave, boss not included
        mobList.add(EntityInfernal.class);
        waves.add(new Wave(mobList,20,32,20));
    }


    @Override
    public void tick() {
        worldObj.markBlocksDirty(x,y,z,x,y,z);
        if(active){
            spawnTimer.tick();
            intermissionTimer.tick();
            checkTimer.tick();
            if(suddenDeath){
                suddenDeathSpawnTImer.tick();
            }
            ticksSinceStart++;

            if(player.health <= 0){
                worldObj.setBlockWithNotify(x,y,z,0);
            }
        }

        enemiesLeft.removeIf((E)-> !E.isAlive());
        if(active && worldObj.difficultySetting == Difficulty.PEACEFUL.id()){
            Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("The wrath beacon loses all its strength suddenly..");
            worldObj.setBlockWithNotify(x,y,z,0);
            EntityItem entityitem2 = new EntityItem(worldObj, (float) x, (float) y, (float) z, new ItemStack(SignalIndustries.reinforcedWrathBeacon, 1));
            worldObj.entityJoinedWorld(entityitem2);
        }
        if(active && started && enemiesLeft.isEmpty() && enemiesSpawned == currentMaxAmount && wave < waves.size()-1){
            for (EntityPlayer player : worldObj.players) {
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("Wave "+wave+" complete! Next wave in: "+(intermissionTimer.max/20)+"s.");
            }
            started = false;
            intermissionTimer.unpause();
            intermission = true;
            enemiesSpawned = 0;
            wave++;
        } else if (active && started && enemiesLeft.isEmpty() && enemiesSpawned == currentMaxAmount && wave == waves.size()-1) {
            for (EntityPlayer player : worldObj.players) {
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("Challenge complete!!");
            }
            for (BlockInstance bi : multiblock.getBlocks(new Vec3i(x, y, z), Direction.Z_POS)) {
                if(worldObj.getBlockId(bi.pos.x,bi.pos.y,bi.pos.z) == SignalIndustries.fueledEternalTreeLog.id){
                    worldObj.setBlockWithNotify(bi.pos.x, bi.pos.y, bi.pos.z, bi.block.id);
                }
            }
            active = false;
            started = false;
            intermission = false;
            spawnTimer.pause();
            intermissionTimer.pause();
            wave = 0;
            currentMaxAmount = 0;
            enemiesSpawned = 0;
            worldObj.setBlockWithNotify(x,y,z,0);
            ExplosionEnergy explosion = new ExplosionEnergy(worldObj, null, x, y, z, 3);
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            EntityItem entityitem = new EntityItem(worldObj, (float) x, (float) y, (float) z, new ItemStack(SignalIndustries.energyCatalyst, 1));
            EntityItem entityitem2 = new EntityItem(worldObj, (float) x, (float) y, (float) z, new ItemStack(SignalIndustries.reinforcedWrathBeacon, 1));
            worldObj.entityJoinedWorld(entityitem);
            worldObj.entityJoinedWorld(entityitem2);
        }
        if(!suddenDeath && active && ticksSinceStart % 30 == 0){
            ArrayList<BlockInstance> blocks = multiblock.getBlocks(new Vec3i(x, y, z), Direction.Z_POS);
            int i = random.nextInt(blocks.size());
            BlockInstance block = blocks.get(i);
            while (worldObj.getBlockId(block.pos.x, block.pos.y, block.pos.z) == SignalIndustries.fueledEternalTreeLog.id && !readyForSuddenDeath())
            {
                i = random.nextInt(blocks.size());
                block = blocks.get(i);
            }
            worldObj.setBlockWithNotify(block.pos.x, block.pos.y, block.pos.z, SignalIndustries.fueledEternalTreeLog.id);
        }
//        if(active){
//            for (float y1 = y; y < 256; y+=0.1f) {
//                worldObj.spawnParticle("reddust",x+0.5,y1,z+0.5,0,0,0);
//            }
//        }
        if(worldObj != null && getBlockType() != null){
            tier = ((BlockContainerTiered)getBlockType()).tier;
        }
        //SignalIndustries.LOGGER.info(String.valueOf(enemiesLeft.size()));
        //SignalIndustries.LOGGER.info(String.valueOf(intermissionTimer.value));

    }

    public void check(){
        if(getBlockType() != null && active){
            if(worldObj.getCurrentWeather() == SignalIndustries.weatherBloodMoon && !suddenDeath){
                for (BlockInstance bi : multiblock.getSubstitutions(new Vec3i(x, y, z), Direction.Z_POS)) {
                    if(worldObj.getBlockId(bi.pos.x,bi.pos.y,bi.pos.z) == SignalIndustries.eternalTreeLog.id){
                        worldObj.setBlockWithNotify(bi.pos.x, bi.pos.y, bi.pos.z, bi.block.id);
                    }
                }
            }
            if(!suddenDeath){
                if(readyForSuddenDeath()){
                    suddenDeath = true;
                    suddenDeathSpawnTImer.unpause();
                    Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("Time has ran out... Brace yourself!");
                }
            }
            if(!multiblock.isValidAt(worldObj, new BlockInstance(getBlockType(), new Vec3i(x, y, z), this), Direction.getDirectionFromSide(worldObj.getBlockMetadata(x, y, z)))){
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("The wrath beacon loses all its strength suddenly..");
                worldObj.setBlockWithNotify(x,y,z,0);
                EntityItem entityitem2 = new EntityItem(worldObj, (float) x, (float) y, (float) z, new ItemStack(SignalIndustries.reinforcedWrathBeacon, 1));
                worldObj.entityJoinedWorld(entityitem2);
            }
        }
    }

    public void activate(EntityPlayer activator){
        if(!active){
            if(worldObj.difficultySetting == Difficulty.PEACEFUL.id()){
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("This world is far too peaceful..");
                return;
            }
            if(worldObj.isDaytime()){
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("Now is not the time..");
                return;
            }
            if(!multiblock.isValidAt(worldObj, new BlockInstance(getBlockType(), new Vec3i(x, y, z), this), Direction.getDirectionFromSide(worldObj.getBlockMetadata(x,y,z)))){
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessageTranslate("event.signalindustries.invalidMultiblock");
                return;
            }
            if(Minecraft.getMinecraft(Minecraft.class).thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft(Minecraft.class).thePlayer.inventory.getCurrentItem().getItem().id == SignalIndustries.infernalEye.id){
                Minecraft.getMinecraft(Minecraft.class).thePlayer.inventory.getCurrentItem().consumeItem(Minecraft.getMinecraft(Minecraft.class).thePlayer);
                for (EntityPlayer player : worldObj.players) {
                    player.addChatMessage("event.signalindustries.reinforcedWrathBeaconActivated");
                }
                active = true;
                player = activator;
                checkTimer.unpause();
                startWave();
            } else {
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("The wrath beacon needs a catalyst..");
            }
        }
    }

    public void suddenDeathSpawn(){
        if(suddenDeath){
            if(getBlockType() != null) {
                started = true;
                ChunkPosition randomPos = getRandomSpawningPointInChunk(worldObj, this.x, this.z);
                EntityMonster mob;
                try {
                    mob = waves.get(wave).chooseRandomMob().getConstructor(World.class).newInstance(worldObj);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                mob.setPos(randomPos.x, randomPos.y, randomPos.z);
                mob.setRot(worldObj.rand.nextFloat() * 360.0F, 0.0F);
                mob.spawnInit();
                worldObj.entityJoinedWorld(mob);
                if(mob instanceof EntityInfernal){
                    ((EntityInfernal) mob).eclipseImmune = true;
                }
            } else {
                suddenDeathSpawnTImer.pause();
            }
        } else {
            suddenDeathSpawnTImer.pause();
        }
    }

    public void startWave(){
        if(active){
            for (EntityPlayer player : worldObj.players) {
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("WAVE "+wave);
                if(wave == waves.size()-1){
                    Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("FINAL WAVE!");
                }
            }
            intermission = false;
            intermissionTimer.pause();
            spawnTimer.unpause();
            spawnTimer.max = waves.get(wave).spawnFrequency;
            int diff = waves.get(wave).upperBound-waves.get(wave).lowerBound;
            if(diff > 0){
                currentMaxAmount = waves.get(wave).lowerBound + random.nextInt(diff);
            } else {
                currentMaxAmount = waves.get(wave).lowerBound;
            }
        }
    }

    public void spawn(){
        if(getBlockType() != null) {
            if (enemiesSpawned < currentMaxAmount) {
                started = true;
                ChunkPosition randomPos = getRandomSpawningPointInChunk(worldObj, this.x, this.z);
                EntityMonster mob;
                try {
                    mob = waves.get(wave).chooseRandomMob().getConstructor(World.class).newInstance(worldObj);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                mob.setPos(randomPos.x, randomPos.y, randomPos.z);
                mob.setRot(worldObj.rand.nextFloat() * 360.0F, 0.0F);
                mob.spawnInit();
                worldObj.entityJoinedWorld(mob);
                enemiesLeft.add(mob);
                if(mob instanceof EntityInfernal){
                    ((EntityInfernal) mob).eclipseImmune = true;
                }
                enemiesSpawned++;
            } else {
                spawnTimer.pause();
            }
        }
    }

    public ChunkPosition getRandomSpawningPointInChunk(World worldObj, int i, int j) {
        int k = (i - 8) + worldObj.rand.nextInt(16);
        int l = this.y;
        int i1 = (j - 8) + worldObj.rand.nextInt(16);
        return new ChunkPosition(k, l, i1);
    }

    public boolean readyForSuddenDeath(){
        for (BlockInstance substitution : multiblock.getSubstitutions(new Vec3i(x, y, z), Direction.Z_POS)) {
            if(worldObj.getBlockId(substitution.pos.x,substitution.pos.y,substitution.pos.z) != SignalIndustries.fueledEternalTreeLog.id){
                return false;
            }
        }
        return true;
    }

    @Override
    public Multiblock getMultiblock() {
        return multiblock;
    }
}
