package sunsetsatellite.signalindustries.interfaces;

import sunsetsatellite.signalindustries.util.IOPreview;

public interface IHasIOPreview {
    IOPreview getPreview();

    void setPreview(IOPreview preview);

    void setTemporaryIOPreview(IOPreview preview, int ticks);

    void disableIOPreview();
}
