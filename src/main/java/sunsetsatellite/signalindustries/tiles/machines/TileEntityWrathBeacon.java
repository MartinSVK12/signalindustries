package sunsetsatellite.signalindustries.tiles.machines;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.monster.MobCreeper;
import net.minecraft.core.entity.monster.MobSkeleton;
import net.minecraft.core.entity.monster.MobSpider;
import net.minecraft.core.entity.monster.MobZombie;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.Difficulty;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkPosition;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.tiles.base.TileEntityWrathBeaconBase;
import sunsetsatellite.signalindustries.util.Wave;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

public class TileEntityWrathBeacon extends TileEntityWrathBeaconBase {
    public Random random = new Random();
    public boolean intermission = false;
    public int wave = 0;
    public int currentMaxAmount = 0;
    public boolean started = false;
    public ArrayList<Mob> enemiesLeft = new ArrayList<>();
    public static ArrayList<Wave> waves = new ArrayList<>();
    public TickTimer spawnTimer = new TickTimer(this, this::spawn, 20, true);
    public TickTimer intermissionTimer = new TickTimer(this, this::startWave, 300, false);

    {
        spawnTimer.pause();
        intermissionTimer.pause();
    }

    public TileEntityWrathBeacon() {
        ArrayList<Class<? extends Mob>> mobList = new ArrayList<>();
        mobList.add(MobZombie.class);
        mobList.add(MobSkeleton.class);
        waves.add(new Wave(mobList, 3, 6, 20));
        waves.add(new Wave(mobList, 5, 10, 20));
        waves.add(new Wave(mobList, 6, 12, 20));
        mobList = new ArrayList<>();
        mobList.add(MobCreeper.class);
        waves.add(new Wave(mobList, 2, 4, 20));
        mobList = new ArrayList<>();
        mobList.add(MobZombie.class);
        mobList.add(MobSkeleton.class);
        mobList.add(MobSpider.class);
        waves.add(new Wave(mobList, 8, 10, 20));
        mobList = new ArrayList<>();
        mobList.add(MobZombie.class);
        mobList.add(MobSkeleton.class);
        mobList.add(MobSpider.class);
        mobList.add(MobCreeper.class);
        waves.add(new Wave(mobList, 10, 16, 20));
        //final wave, boss not included
        waves.add(new Wave(mobList, 10, 16, 20));

    }

    @Override
    public void tick() {
        if (worldObj == null) return;
        worldObj.markBlocksDirty(tilePos.x, tilePos.y, tilePos.z, tilePos.x, tilePos.y, tilePos.z);
        if (active) {
            spawnTimer.tick();
            intermissionTimer.tick();
        }

        enemiesLeft.removeIf((E) -> !E.isAlive());
        if (active && worldObj.getDifficulty() == Difficulty.PEACEFUL) {
            for (Player player : worldObj.players) {
                if (player.distanceToSqr(tilePos.x, tilePos.y, tilePos.z) > 64) continue;
                player.sendMessage("The wrath beacon loses all its strength suddenly..");
            }
            worldObj.setBlockWithNotify(tilePos.x, tilePos.y, tilePos.z, 0);
        }
        if (active && started && enemiesLeft.isEmpty() && wave < 5) {
            for (Player player : worldObj.players) {
                if (player.distanceToSqr(tilePos.x, tilePos.y, tilePos.z) > 64) continue;
                player.sendMessage("Wave " + wave + " complete! Next wave in: " + (intermissionTimer.max / 20) + "s.");
            }
            started = false;
            intermissionTimer.unpause();
            intermission = true;
            wave++;
        } else if (active && started && enemiesLeft.isEmpty() && wave == 5) {
            for (Player player : worldObj.players) {
                if (player.distanceToSqr(tilePos.x, tilePos.y, tilePos.z) > 64) continue;
                player.sendMessage("Challenge complete!!");
                //player.triggerAchievement(SIAchievements.VICTORY);
            }
            active = false;
            started = false;
            intermission = false;
            spawnTimer.pause();
            intermissionTimer.pause();
            wave = 0;
            currentMaxAmount = 0;
            worldObj.setBlockWithNotify(tilePos.x, tilePos.y, tilePos.z, 0);
            worldObj.spawnParticle("signalindustries.shockwave", tilePos.x, tilePos.y, tilePos.z, 0.0, 0.0, 0.0, 0, true);
            EntityItem entityitem = new EntityItem(worldObj, (float) tilePos.x, (float) tilePos.y, (float) tilePos.z, new ItemStack(SIItems.clearKey, 1));
            worldObj.entityJoinedWorld(entityitem);
        }
        if (active) {
            for (float y1 = tilePos.y; y1 < 256; y1 += 0.1f) {
                worldObj.spawnParticle("reddust", tilePos.x + 0.5, y1, tilePos.z + 0.5, 0, 0, 0, 0, true);
            }
        }
        if (worldObj != null && getBlock() != null) {
            tier = ((ITiered) getBlock().getLogic()).getTier();
        }
        //SignalIndustries.LOGGER.info(String.valueOf(enemiesLeft.size()));
        //SignalIndustries.LOGGER.info(String.valueOf(intermissionTimer.value));

    }

	@Override
	public void readAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	@Override
	public void writeAdditionalData(@NotNull CompoundTag compoundTag) {

	}

	public void activate(Player activator) {
        if (!active && worldObj != null) {
            if (worldObj.getDifficulty() == Difficulty.PEACEFUL) {
                activator.sendMessage("This world is far too peaceful..");
                return;
            }
            if (worldObj.isDaytime()) {
                activator.sendMessage("Now is not the time..");
                return;
            }
            /*for (int x1 = x-7; x < x+7; x++) {
                for (int y1 = y; y1 < y+8; y1++) {
                    for (int z1 = z-7; z < z+7; z++) {
                        int id = worldObj.getBlockId(x1,y1,z1);
                        int idUnder = worldObj.getBlockId(x1,y1-1,z1);
                        if (id != 0 && (x1 != x || y1 != y || z1 != z)) {
                            activator.sendMessage("The wrath beacon desires more space..");
                            return;
                        }
                    }
                }
            }*/
            if (activator.getHeldItem() != null && activator.getHeldItem().itemID == SIItems.evilEye.id) {
                activator.getHeldItem().consumeItem(activator);
                for (Player player : worldObj.players) {
                    if (player.distanceToSqr(tilePos.x, tilePos.y, tilePos.z) > 64) continue;
                    player.sendTranslatedChatMessage("event.signalindustries.wrathBeaconActivated");
                    //player.triggerAchievement(SIAchievements.CHALLENGE);
                }
                active = true;
                startWave();
            } else {
                activator.sendMessage("The wrath beacon needs a catalyst..");
            }
        }
    }

    public void startWave() {
        if (active && worldObj != null) {
            for (Player player : worldObj.players) {
                if (player.distanceToSqr(tilePos.x, tilePos.y, tilePos.z) > 64) continue;
                player.sendMessage("Wave " + wave);
                if (wave == 5) {
                    player.sendMessage("FINAL WAVE!");
                }
            }
            intermission = false;
            intermissionTimer.pause();
            spawnTimer.unpause();
            spawnTimer.max = waves.get(wave).spawnFrequency;
            currentMaxAmount = waves.get(wave).lowerBound + random.nextInt(waves.get(wave).upperBound - waves.get(wave).lowerBound);
        }
    }

    public void spawn() {
        if (enemiesLeft.size() < currentMaxAmount && worldObj != null) {
            started = true;
            ChunkPosition randomPos = getRandomSpawningPointInChunk(worldObj, this.tilePos.x, this.tilePos.z);
            Mob mob;
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
        } else {
            spawnTimer.pause();
        }

    }

    public ChunkPosition getRandomSpawningPointInChunk(World worldObj, int i, int j) {
        int k = i + worldObj.rand.nextInt(8);
        int l = tilePos.y;
        int i1 = j + worldObj.rand.nextInt(8);
        return new ChunkPosition(k, l, i1);
    }

    @Override
    public boolean isDisabled() {
        return false;
    }
}
