package sunsetsatellite.signalindustries.util;

import net.minecraft.src.EntityMob;

import java.util.ArrayList;
import java.util.Random;

public class Wave {
    public ArrayList<Class<? extends EntityMob>> mobList = new ArrayList<>();
    public int lowerBound = 0;
    public int upperBound = 0;
    public int spawnFrequency = 0;
    public boolean isBoss = false;
    public Random random = new Random();

    public Wave(ArrayList<Class<? extends EntityMob>> mobList, int lowerBound, int upperBound, int spawnFrequency) {
        this.mobList = mobList;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.spawnFrequency = spawnFrequency;
        this.isBoss = false;
    }

    public Wave(ArrayList<Class<? extends EntityMob>> mobList, int lowerBound, int upperBound, int spawnFrequency, boolean boss) {
        this.mobList = mobList;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.spawnFrequency = spawnFrequency;
        this.isBoss = boss;
    }

    public Class<? extends EntityMob> chooseRandomMob(){
        return mobList.get(random.nextInt(mobList.size()));
    }

}
