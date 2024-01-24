package sunsetsatellite.signalindustries.api.impl.terrainapi;

import sunsetsatellite.signalindustries.SignalIndustries;
import useless.terrainapi.api.TerrainAPI;

public class TerrainAPISignalIndustriesPlugin implements TerrainAPI {
    @Override
    public String getModID() {
        return SignalIndustries.MOD_ID;
    }

    @Override
    public void onInitialize() {
        new WorldGenSI().init();
    }
}
