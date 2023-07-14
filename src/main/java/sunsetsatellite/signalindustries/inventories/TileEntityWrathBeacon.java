package sunsetsatellite.signalindustries.inventories;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.ExplosionEnergy;
import sunsetsatellite.signalindustries.util.Wave;
import sunsetsatellite.sunsetutils.util.TickTimer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class TileEntityWrathBeacon extends TileEntity {
    public Random rand = new Random();
    public boolean active = false;
    public boolean intermission = false;
    public int wave = 0;
    public int currentMaxAmount = 0;
    public boolean started = false;
    public ArrayList<EntityLiving> enemiesLeft = new ArrayList<>();
    public static ArrayList<Wave> waves = new ArrayList<>();
    public TickTimer spawnTimer;
    public TickTimer intermissionTimer;
    {
        try {
            spawnTimer = new TickTimer(this,this.getClass().getMethod("spawn"),20,true);
            spawnTimer.pause();
            intermissionTimer = new TickTimer(this, getClass().getMethod("startWave"),300,false);
            intermissionTimer.pause();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public TileEntityWrathBeacon(){
        ArrayList<Class<? extends EntityMob>> mobList = new ArrayList<>();
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
        enemiesLeft.removeIf((E)-> !E.isEntityAlive());
        if(active && started && enemiesLeft.size() == 0 && wave < 5){
            for (EntityPlayer player : worldObj.players) {
                Minecraft.getMinecraft().ingameGUI.addChatMessage("Wave "+wave+" complete! Next wave in: "+(intermissionTimer.max/20)+"s.");
            }
            started = false;
            intermissionTimer.unpause();
            intermission = true;
            wave++;
        } else if (active && started && enemiesLeft.size() == 0 && wave == 5) {
            for (EntityPlayer player : worldObj.players) {
                Minecraft.getMinecraft().ingameGUI.addChatMessage("Challenge complete!!");
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
            if(worldObj.isDaytime()){
                Minecraft.getMinecraft().ingameGUI.addChatMessage("Now is not the time..");
                return;
            }
            for (int x = xCoord-7; x < xCoord+7; x++) {
                for (int y = yCoord; y < yCoord+8; y++) {
                    for (int z = zCoord-7; z < zCoord+7; z++) {
                        int id = worldObj.getBlockId(x,y,z);
                        int idUnder = worldObj.getBlockId(x,yCoord-1,z);
                        if (id != 0 && (x != xCoord || y != yCoord || z != zCoord)) {
                            Minecraft.getMinecraft().ingameGUI.addChatMessage("The wrath beacon desires more space..");
                            return;
                        }
                    }
                }
            }
            if(Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem() != null && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem().itemID == SignalIndustries.evilCatalyst.itemID){
                /*if(Minecraft.getMinecraft().gameSettings.difficulty.value == 0){
                    Minecraft.getMinecraft().theMinecraft.getMinecraft().ingameGUI.addChatMessage("This world is too peaceful..");
                    return;
                }*/
                Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().consumeItem(Minecraft.getMinecraft().thePlayer);
                for (EntityPlayer player : worldObj.players) {
                    player.addChatMessage("event.signalindustries.wrathBeaconActivated");
                }
                active = true;
                startWave();
            } else {
                Minecraft.getMinecraft().ingameGUI.addChatMessage("The wrath beacon needs a catalyst..");
            }
        }
    }

    public void startWave(){
        if(active){
            for (EntityPlayer player : worldObj.players) {
                Minecraft.getMinecraft().ingameGUI.addChatMessage("WAVE "+wave);
                if(wave == 5){
                    Minecraft.getMinecraft().ingameGUI.addChatMessage("FINAL WAVE!");
                }
            }
            intermission = false;
            intermissionTimer.pause();
            spawnTimer.unpause();
            spawnTimer.max = waves.get(wave).spawnFrequency;
            currentMaxAmount = waves.get(wave).lowerBound + rand.nextInt(waves.get(wave).upperBound-waves.get(wave).lowerBound);
        }
    }

    public void spawn(){
        if(enemiesLeft.size() < currentMaxAmount){
            started = true;
            ChunkPosition randomPos = getRandomSpawningPointInChunk(worldObj, this.xCoord, this.zCoord);
            EntityMob mob;
            try {
                mob = waves.get(wave).chooseRandomMob().getConstructor(World.class).newInstance(worldObj);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            mob.setLocationAndAngles(randomPos.x, randomPos.y, randomPos.z, worldObj.rand.nextFloat() * 360.0F, 0.0F);
            mob.entityInitOnSpawn();
            worldObj.entityJoinedWorld(mob);
            enemiesLeft.add(mob);
        } else {
            spawnTimer.pause();
        }

    }

    public ChunkPosition getRandomSpawningPointInChunk(World world, int i, int j) {
        int k = i + world.rand.nextInt(8);
        int l = this.yCoord;
        int i1 = j + world.rand.nextInt(8);
        return new ChunkPosition(k, l, i1);
    }

}
