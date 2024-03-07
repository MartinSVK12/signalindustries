package sunsetsatellite.signalindustries.interfaces;

import sunsetsatellite.signalindustries.util.ApplicationType;

public interface IApplicationItem<T> extends ITiered {
    ApplicationType getType();

    T getApplication();
}
