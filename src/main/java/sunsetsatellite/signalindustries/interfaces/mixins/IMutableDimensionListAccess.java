package sunsetsatellite.signalindustries.interfaces.mixins;

import net.minecraft.core.world.Dimension;

import java.util.Map;

public interface IMutableDimensionListAccess {

    Map<Integer, Dimension> getMutableDimensionList();
}
