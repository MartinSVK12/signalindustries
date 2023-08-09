package sunsetsatellite.signalindustries.interfaces;

import sunsetsatellite.signalindustries.util.Tier;
import sunsetsatellite.sunsetutils.util.ICustomDescription;

public interface ITiered extends ICustomDescription {

    Tier getTier();
}
