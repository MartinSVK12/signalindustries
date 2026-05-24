package sunsetsatellite.signalindustries;

import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignalIndustriesServer implements DedicatedServerModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("signalindustries|server");

	@Override
	public void onInitializeServer() {
		LOGGER.info("SI Server is being initialized...");
	}
}
