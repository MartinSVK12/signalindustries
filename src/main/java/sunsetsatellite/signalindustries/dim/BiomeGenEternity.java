package sunsetsatellite.signalindustries.dim;



import sunsetsatellite.signalindustries.SignalIndustries;

public class BiomeGenEternity extends BiomeGenPublic {
    public BiomeGenEternity(int id) {
        super(id);
        this.spawnableMonsterList.clear();
        this.spawnableCreatureList.clear();
        this.spawnableWaterCreatureList.clear();
        this.topBlock = (short) SignalIndustries.realityFabric.blockID;
        setBiomeName("Eternity");
        setColor(0xFFFFFF);
        setBlockedWeathers(new Weather[]{Weather.weatherRain, Weather.weatherSnow, Weather.weatherStorm, Weather.weatherFog});
    }

    public int getSkyColorByTemp(float f) {
        return 0xFFFFFF;
    }
}
