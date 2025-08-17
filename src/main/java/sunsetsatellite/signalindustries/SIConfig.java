package sunsetsatellite.signalindustries;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static sunsetsatellite.signalindustries.SignalIndustries.MOD_ID;

public class SIConfig {
    private static final int blockIdStart = 1200;
    private static final int itemIdStart = 17100;
    public static final TomlConfigHandler config;

    static {
        List<Field> blockFields = Arrays.stream(SIBlocks.class.getDeclaredFields()).filter((F) -> Block.class.isAssignableFrom(F.getType())).collect(Collectors.toList());
        List<Field> itemFields = Arrays.stream(SIItems.class.getDeclaredFields()).filter((F) -> Item.class.isAssignableFrom(F.getType())).collect(Collectors.toList());

        Toml defaultConfig = new Toml("Signal Industries configuration file.");
        defaultConfig.addCategory("BlockIDs");
        defaultConfig.addCategory("ItemIDs");
        defaultConfig.addCategory("EntityIDs");
        defaultConfig.addCategory("Other");
        defaultConfig.addCategory("Experimental");
        defaultConfig.addCategory("These options modify the world generation, the values for chances here are interpreted by the game as 1 in x. A config having the value of 10 would mean 1 in 10.","WorldGen");
        defaultConfig.addEntry("Experimental.enableDynamicChunkProvider","Switches the vanilla BTA static provider with a new dynamic one, required for chunkloading to work.",false);
        defaultConfig.addEntry("Other.enableQuests",true);
        defaultConfig.addEntry("Other.totemsRequireOP",false);
        defaultConfig.addEntry("Other.eternityDimId", 3);
        defaultConfig.addEntry("Other.GuiId", 10);
        defaultConfig.addEntry("Other.machinePacketId", 113);
        defaultConfig.addEntry("Other.dilithiumMiningLevel", 4);
        defaultConfig.addEntry("Other.awakenedMiningLevel", 5);
        defaultConfig.addEntry("EntityIDs.infernalId", 100);
        defaultConfig.addEntry("EntityIDs.volatileCrystalId",50);
        defaultConfig.addEntry("EntityIDs.energyOrbId",51);
        defaultConfig.addEntry("EntityIDs.fallingMeteorId",52);
        defaultConfig.addEntry("EntityIDs.sunbeamId",53);
        defaultConfig.addEntry("WorldGen.signaliteGeodeChance","Default is 10",10);
        defaultConfig.addEntry("WorldGen.ironMeteorChance","Default is 256",256);
        defaultConfig.addEntry("WorldGen.signaliteMeteorChance","Default is 512",512);
        defaultConfig.addEntry("WorldGen.dilithiumMeteorChance","Default is 1024",1024);
        defaultConfig.addEntry("WorldGen.obeliskMeteorChance","Default is 2048",2048);

        int blockId = blockIdStart;
        int itemId = itemIdStart;
        for (Field blockField : blockFields) {
            defaultConfig.addEntry("BlockIDs." + blockField.getName(), blockId++);
        }
        for (Field itemField : itemFields) {
            defaultConfig.addEntry("ItemIDs." + itemField.getName(), itemId++);
        }

        config = new TomlConfigHandler(MOD_ID, new Toml("Signal Industries configuration file."),false);

        File configFile = config.getConfigFile();

        if (config.getConfigFile().exists()) {
            config.loadConfig();
            config.setDefaults(config.getRawParsed());
            Toml rawConfig = config.getRawParsed();
            Toml blockToml = (Toml) rawConfig.get(".BlockIDs");
            Toml itemToml = (Toml) rawConfig.get(".ItemIDs");
            int maxBlocks = 0;
            int maxItems = 0;
            if(blockToml != null) {
                maxBlocks = blockToml.getOrderedKeys().size();
            }
            if(itemToml != null) {
                maxItems = itemToml.getOrderedKeys().size();
            }
            int newNextBlockId = blockIdStart + maxBlocks;
            int newNextItemId = itemIdStart + maxItems;
            boolean changed = false;

            for (Field F : blockFields) {
                if (!rawConfig.contains("BlockIDs." + F.getName())) {
                    rawConfig.addEntry("BlockIDs." + F.getName(), newNextBlockId++);
                    changed = true;
                }
            }
            for (Field F : itemFields) {
                if (!rawConfig.contains("ItemIDs." + F.getName())) {
                    rawConfig.addEntry("ItemIDs." + F.getName(), newNextItemId++);
                    changed = true;
                }
            }

            if(!rawConfig.contains("WorldGen")){
                rawConfig.addCategory("These options modify the world generation, the values for chances here are interpreted by the game as 1 in x. A config having the value of 10 would mean 1 in 10.","WorldGen");
                changed = true;
            }

            if(!rawConfig.contains("WorldGen.signaliteGeodeChance")){
                rawConfig.addEntry("WorldGen.signaliteGeodeChance","Default is 10",10);
                changed = true;
            }

            if(!rawConfig.contains("WorldGen.ironMeteorChance")){
                rawConfig.addEntry("WorldGen.ironMeteorChance","Default is 256",256);
                changed = true;
            }

            if(!rawConfig.contains("WorldGen.signaliteMeteorChance")){
                rawConfig.addEntry("WorldGen.signaliteMeteorChance","Default is 512",512);
                changed = true;
            }

            if(!rawConfig.contains("WorldGen.dilithiumMeteorChance")){
                rawConfig.addEntry("WorldGen.dilithiumMeteorChance","Default is 1024",1024);
                changed = true;
            }

            if(!rawConfig.contains("WorldGen.obeliskChance")){
                rawConfig.addEntry("WorldGen.obeliskMeteorChance","Default is 2048",2048);
                changed = true;
            }

            if(!rawConfig.contains("EntityIDs.infernalId")){
                rawConfig.addEntry("EntityIDs.infernalId", 100);
                changed = true;
            }

            if(!rawConfig.contains("EntityIDs.volatileCrystalId")){
                rawConfig.addEntry("EntityIDs.volatileCrystalId", 50);
                changed = true;
            }

            if(!rawConfig.contains("EntityIDs.energyOrbId")){
                rawConfig.addEntry("EntityIDs.energyOrbId", 51);
                changed = true;
            }

            if(!rawConfig.contains("EntityIDs.fallingMeteorId")){
                rawConfig.addEntry("EntityIDs.fallingMeteorId", 52);
                changed = true;
            }


            if(!rawConfig.contains("EntityIDs.sunbeamId")){
                rawConfig.addEntry("EntityIDs.sunbeamId", 53);
                changed = true;
            }

            try {
                if(!rawConfig.contains("Experimental.enableDynamicChunkProvider")){
                    rawConfig.addEntry("Experimental.enableDynamicChunkProvider", false);
                    changed = true;
                }
            } catch (NullPointerException e){
                rawConfig.addEntry("Experimental.enableDynamicChunkProvider", false);
                changed = true;
            }

            try {
                if(!rawConfig.contains("Other.enableQuests")){
                    rawConfig.addEntry("Other.enableQuests", true);
                    changed = true;
                }
            } catch (NullPointerException e){
                rawConfig.addEntry("Other.enableQuests", true);
                changed = true;
            }

            try {
                if(!rawConfig.contains("Other.totemsRequireOP")){
                    rawConfig.addEntry("Other.totemsRequireOP", false);
                    changed = true;
                }
            } catch (NullPointerException e){
                rawConfig.addEntry("Other.totemsRequireOP", false);
                changed = true;
            }

            if(!rawConfig.contains("Other.eternityDimId")){
                rawConfig.addEntry("Other.eternityDimId", 3);
                changed = true;
            }

            if(!rawConfig.contains("Other.GuiId")){
                rawConfig.addEntry("Other.GuiId", 10);
                changed = true;
            }

            if(!rawConfig.contains("Other.machinePacketId")){
                rawConfig.addEntry("Other.machinePacketId", 113);
                changed = true;
            }

            if(!rawConfig.contains("Other.dilithiumMiningLevel")){
                rawConfig.addEntry("Other.dilithiumMiningLevel", 4);
                changed = true;
            }

            if(!rawConfig.contains("Other.awakenedMiningLevel")){
                rawConfig.addEntry("Other.awakenedMiningLevel", 5);
                changed = true;
            }

            if (changed) {
                config.setDefaults(rawConfig);
                config.writeConfig();
                config.loadConfig();
            }
        } else {
            config.setDefaults(defaultConfig);
            try {
                //noinspection ResultOfMethodCallIgnored
                configFile.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
                config.writeConfig();
                config.loadConfig();
            } catch (IOException e) {
                throw new RuntimeException("Failed to generate config!", e);
            }
        }
    }

    public static int item(String cfgId) {
        return config.getInt("ItemIDs." + cfgId);
    }

    public static int block(String cfgId) {
        return config.getInt("BlockIDs." + cfgId);
    }
}
