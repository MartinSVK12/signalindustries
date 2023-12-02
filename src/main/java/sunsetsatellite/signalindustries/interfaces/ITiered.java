package sunsetsatellite.signalindustries.interfaces;

import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.util.Tier;


public interface ITiered extends ICustomDescription {

    Tier getTier();
}
