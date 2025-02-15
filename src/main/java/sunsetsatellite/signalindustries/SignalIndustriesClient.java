package sunsetsatellite.signalindustries;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.ClientStartEntrypoint;

@Environment(EnvType.CLIENT)
public class SignalIndustriesClient implements ClientModInitializer, ClientStartEntrypoint {

    public static final String MOD_ID = "signalindustries|client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("SI Client initialized.");
    }

    @Override
    public void beforeClientStart() {
        LOGGER.info("Beginning client pre-init.");
    }

    @Override
    public void afterClientStart() {
        LOGGER.info("Beginning client post-init.");
    }
}
