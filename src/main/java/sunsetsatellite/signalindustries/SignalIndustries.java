package sunsetsatellite.signalindustries;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.util.collection.NamespaceID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.event.defs.CommonEvents;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;

public class SignalIndustries implements ModInitializer {
	public static final String MOD_ID = HalpLibe.registerMod("signalindustries", true);
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@SuppressWarnings("InstantiationOfUtilityClass")
	@Override
	public void onInitialize() {
		LOGGER.info("Signal Industries is loading... Shine!");
		LOGGER.info("Loading SI config...");
		new SIConfig();
		new SIArt().init();
		CommonEvents.BEFORE_GAME_START.listen(this::beforeGameStart);
		CommonEvents.AFTER_GAME_START.listen(this::afterGameStart);
		CommonEvents.AFTER_BLOCK_INIT.listen(()->new SIBlocks().afterBlockInit());
		CommonEvents.AFTER_ITEM_INIT.listen(()->new SIItems().afterItemInit());
	}

	public void beforeGameStart() {
		LOGGER.info("Beginning core pre-init.");
	}

	public void afterGameStart() {
		LOGGER.info("Beginning core post-init.");
	}

	public static NamespaceID id(String id) {
		return NamespaceID.fromPool(MOD_ID, id);
	}

	public static String key(String key) {
		return MOD_ID + ":" + key;
	}

	public static String langKey(String key) {
		return MOD_ID + "." + key;
	}

}
