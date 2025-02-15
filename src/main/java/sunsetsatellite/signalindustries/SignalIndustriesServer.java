package sunsetsatellite.signalindustries;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.SERVER)
public class SignalIndustriesServer implements DedicatedServerModInitializer {

    public static final String MOD_ID = "signalindustries|server";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {
        LOGGER.info("SI Server initialized.");
    }
}
