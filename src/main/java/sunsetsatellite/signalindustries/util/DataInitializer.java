package sunsetsatellite.signalindustries.util;

public class DataInitializer {
    protected boolean initialized;
    {
        setInitialized(false);
    }

    public boolean isInitialized() {
        return initialized;
    }

    protected void setInitialized(boolean init) {
        if(initialized) return;
        initialized = init;
    }
}
