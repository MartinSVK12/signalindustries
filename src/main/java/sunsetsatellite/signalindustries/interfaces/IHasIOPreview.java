package sunsetsatellite.signalindustries.interfaces;

import sunsetsatellite.signalindustries.util.IO;

public interface IHasIOPreview {
    IO getPreview();

    void setPreview(IO preview);

    void setTemporaryIOPreview(IO preview, int ticks);

    void disableIOPreview();
}
