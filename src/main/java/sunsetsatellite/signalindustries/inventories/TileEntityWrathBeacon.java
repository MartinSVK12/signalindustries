package sunsetsatellite.signalindustries.inventories;

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
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.ExplosionEnergy;
import sunsetsatellite.signalindustries.util.Wave;
import sunsetsatellite.sunsetutils.util.TickTimer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class TileEntityWrathBeacon extends TileEntity {
    public Random random = new Random();
    public boolean active = false;
    public boolean intermission = false;
    public int wave = 0;
    public int currentMaxAmount = 0;
    public boolean started = false;
    public ArrayList<EntityLiving> enemiesLeft = new ArrayList<>();
    public static ArrayList<Wave> waves = new ArrayList<>();
    public TickTimer spawnTimer = new TickTimer(this,"spawn",20,true);
    public TickTimer intermissionTimer = new TickTimer(this,"startWave",300,false);
    {
            spawnTimer.pause();
            intermissionTimer.pause();
    }

    public TileEntityWrathBeacon(){
        ArrayList<Class<? extends EntityMonster>> mobList = new ArrayList<>();
        mobList.add(EntityZombie.class);
        mobList.add(EntitySkeleton.class);
        waves.add(new Wave(mobList,3,6,20));
        waves.add(new Wave(mobList,5,10,20));
        waves.add(new Wave(mobList,6,12,20));
        mobList = new ArrayList<>();
        mobList.add(EntityCreeper.class);
        waves.add(new Wave(mobList,2,4,20));
        mobList = new ArrayList<>();
        mobList.add(EntityZombie.class);
        mobList.add(EntitySkeleton.class);
        mobList.add(EntitySpider.class);
        waves.add(new Wave(mobList,8,10,20));
        mobList = new ArrayList<>();
        mobList.add(EntityZombie.class);
        mobList.add(EntitySkeleton.class);
        mobList.add(EntitySpider.class);
        mobList.add(EntityCreeper.class);
        waves.add(new Wave(mobList,10,16,20));
        //final wave, boss not included
        waves.add(new Wave(mobList,10,16,20));

    }

    @Override
    public void updateEntity() {
        worldObj.markBlocksDirty(xCoord,yCoord,zCoord,xCoord,yCoord,zCoord);
        if(active){
            spawnTimer.tick();
            intermissionTimer.tick();
        }
        enemiesLeft.removeIf((E)-> !E.isAlive());
        if(active && worldObj.difficultySetting == Difficulty.PEACEFUL.id()){
            Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("The wrath beacon loses all its strength suddenly..");
            worldObj.setBlockWithNotify(xCoord,yCoord,zCoord,0);
        }
        if(active && started && enemiesLeft.size() == 0 && wave < 5){
            for (EntityPlayer player : worldObj.players) {
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("Wave "+wave+" complete! Next wave in: "+(intermissionTimer.max/20)+"s.");
            }
            started = false;
            intermissionTimer.unpause();
            intermission = true;
            wave++;
        } else if (active && started && enemiesLeft.size() == 0 && wave == 5) {
            for (EntityPlayer player : worldObj.players) {
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("Challenge complete!!");
            }
            active = false;
            started = false;
            intermission = false;
            spawnTimer.pause();
            intermissionTimer.pause();
            wave = 0;
            currentMaxAmount = 0;
            worldObj.setBlockWithNotify(xCoord,yCoord,zCoord,0);
            ExplosionEnergy explosion = new ExplosionEnergy(worldObj, null, xCoord, yCoord, zCoord, 3);
            explosion.doExplosionA();
            explosion.doExplosionB(true);
            EntityItem entityitem = new EntityItem(worldObj, (float) xCoord, (float) yCoord, (float) zCoord, new ItemStack(SignalIndustries.energyCatalyst, 1));
            worldObj.entityJoinedWorld(entityitem);
        }
        if(active){
            for (float y = yCoord; y < 256; y+=0.1) {
                worldObj.spawnParticle("reddust",xCoord+0.5,y,zCoord+0.5,0,0,0);
            }
        }
        //SignalIndustries.LOGGER.info(String.valueOf(enemiesLeft.size()));
        //SignalIndustries.LOGGER.info(String.valueOf(intermissionTimer.value));

    }

    public void activate(){
        if(!active){
            if(worldObj.difficultySetting == Difficulty.PEACEFUL.id()){
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("This world is far too peaceful..");
                return;
            }
            if(worldObj.isDaytime()){
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("Now is not the time..");
                return;
            }
            for (int x = xCoord-7; x < xCoord+7; x++) {
                for (int y = yCoord; y < yCoord+8; y++) {
                    for (int z = zCoord-7; z < zCoord+7; z++) {
                        int id = worldObj.getBlockId(x,y,z);
                        int idUnder = worldObj.getBlockId(x,yCoord-1,z);
                        if (id != 0 && (x != xCoord || y != yCoord || z != zCoord)) {
                            Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("The wrath beacon desires more space..");
                            return;
                        }
                    }
                }
            }
            if(Minecraft.getMinecraft(Minecraft.class).thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft(Minecraft.class).thePlayer.inventory.getCurrentItem().getItem().id == SignalIndustries.evilCatalyst.id){
                Minecraft.getMinecraft(Minecraft.class).thePlayer.inventory.getCurrentItem().consumeItem(Minecraft.getMinecraft(Minecraft.class).thePlayer);
                for (EntityPlayer player : worldObj.players) {
                    player.addChatMessage("event.signalindustries.wrathBeaconActivated");
                }
                active = true;
                startWave();
            } else {
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("The wrath beacon needs a catalyst..");
            }
        }
    }

    public void startWave(){
        if(active){
            for (EntityPlayer player : worldObj.players) {
                Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("WAVE "+wave);
                if(wave == 5){
                    Minecraft.getMinecraft(Minecraft.class).ingameGUI.addChatMessage("FINAL WAVE!");
                }
            }
            intermission = false;
            intermissionTimer.pause();
            spawnTimer.unpause();
            spawnTimer.max = waves.get(wave).spawnFrequency;
            currentMaxAmount = waves.get(wave).lowerBound + random.nextInt(waves.get(wave).upperBound-waves.get(wave).lowerBound);
        }
    }

    public void spawn(){
        if(enemiesLeft.size() < currentMaxAmount){
            started = true;
            ChunkPosition randomPos = getRandomSpawningPointInChunk(worldObj, this.xCoord, this.zCoord);
            EntityMonster mob;
            try {
                mob = waves.get(wave).chooseRandomMob().getConstructor(World.class).newInstance(worldObj);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            mob.setPos(randomPos.x, randomPos.y, randomPos.z);
            mob.setRot(worldObj.rand.nextFloat() * 360.0F, 0.0F);
            mob.spawnInit();
            worldObj.entityJoinedWorld(mob);
            enemiesLeft.add(mob);
        } else {
            spawnTimer.pause();
        }

    }

    public ChunkPosition getRandomSpawningPointInChunk(World worldObj, int i, int j) {
        int k = i + worldObj.rand.nextInt(8);
        int l = this.yCoord;
        int i1 = j + worldObj.rand.nextInt(8);
        return new ChunkPosition(k, l, i1);
    }

}
