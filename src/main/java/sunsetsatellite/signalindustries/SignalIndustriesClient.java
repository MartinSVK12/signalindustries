package sunsetsatellite.signalindustries;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.event.defs.ClientEvents;
import turniplabs.halplibe.util.ClientStartEntrypoint;

public class SignalIndustriesClient implements ClientModInitializer {

	public static final Logger LOGGER = LoggerFactory.getLogger("signalindustries|client");

	@Override
	public void onInitializeClient() {
		LOGGER.info("SI Client is being initialized...");
		ClientEvents.BEFORE_CLIENT_START.listen(this::beforeClientStart);
		ClientEvents.AFTER_CLIENT_START.listen(this::afterClientStart);
		ClientEvents.BLOCK_MODEL_RELOAD.listen((t)->new SIModels().initBlockModels(t));
		ClientEvents.ITEM_MODEL_RELOAD.listen((t)->new SIModels().initItemModels(t));
	}

	public void beforeClientStart() {
		LOGGER.info("Beginning client pre-init.");
	}

	public void afterClientStart() {
		LOGGER.info("Beginning client post-init.");
	}
}
