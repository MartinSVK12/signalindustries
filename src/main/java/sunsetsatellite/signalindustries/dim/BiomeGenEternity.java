package sunsetsatellite.signalindustries.dim;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Weather;
import sunsetsatellite.signalindustries.SignalIndustries;

public class BiomeGenEternity extends BiomeGenBase {
    public BiomeGenEternity(int id) {
        super(id);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.topBlock = (short) SignalIndustries.realityFabric.blockID;
        setBiomeName("Eternity");
        setColor(0x2b2b2b);
        setBlockedWeathers(new Weather[]{Weather.weatherRain, Weather.weatherSnow, Weather.weatherStorm, Weather.weatherFog});
    }

    public int getSkyColorByTemp(float f) {
        return 0xb2b2b2;
    }
}
