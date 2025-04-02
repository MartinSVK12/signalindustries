package sunsetsatellite.signalindustries.interfaces;

import com.mojang.nbt.tags.CompoundTag;

public interface IPlayerPowerSuit<T extends IPowerSuit> {

    T getPowerSuit();
    CompoundTag getPowerSuitData();

}
