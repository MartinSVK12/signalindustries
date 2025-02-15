package sunsetsatellite.signalindustries;

import net.minecraft.core.item.Item;
import net.minecraft.core.util.collection.NamespaceID;
import sunsetsatellite.catalyst.core.util.DataInitializer;
import turniplabs.halplibe.helper.ItemBuilder;
import turniplabs.halplibe.util.ItemInitEntrypoint;

import java.util.HashMap;

import static sunsetsatellite.signalindustries.SIConfig.config;
import static sunsetsatellite.signalindustries.SignalIndustries.*;

public class SIItems extends DataInitializer implements ItemInitEntrypoint {

    public static HashMap<Item, String> itemTextures = new HashMap<>();

    public static Item rawSignalumCrystal;

    @Override
    public void init() {
        if (initialized) return;
        LOGGER.info("Initializing items...");
        rawSignalumCrystal = simpleItem("raw_signalum_crystal", "rawSignalumCrystal","rawsignalumcrystal");
        setInitialized(true);
    }

    @Override
    public void afterItemInit() {
        init();
    }

    public static Item simpleItem(String name, String lang, String texture) {
        Item item = new Item(NamespaceID.getPermanent(MOD_ID, name), config.getInt("ItemIDs." + lang));
        item.setKey(lang);
        itemTextures.put(item, texture);
        LOGGER.info("Registering item '" + item.namespaceID.toString() + "' with texture 'signalindustries:item/" + texture + "'.");
        return new ItemBuilder(MOD_ID).build(item);
    }
}
