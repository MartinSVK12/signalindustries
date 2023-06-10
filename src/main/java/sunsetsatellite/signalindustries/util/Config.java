package sunsetsatellite.signalindustries.util;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;

public class Config {

    public static void init() {
        if (!configFile.exists()) {
            writeConfig();
        }
    }

    public static void writeConfig() {
        try {
            BufferedWriter configWriter = new BufferedWriter(new FileWriter(configFile));
            configWriter.write("// Signal Industries configuration file. Configure options here.");
            configWriter.write(System.getProperty("line.separator") +"GuiID=9");
            configWriter.write(System.getProperty("line.separator") +"//Configure ID's here. Note: 'null' means a default value will be used.");

            for (Field field : SignalIndustries.class.getFields()) {
                if (field.getType() == Block.class) {
                    try {
                        configWriter.write(System.getProperty("line.separator") + field.getName()
                                + "=null");// + ((Block) field.get(null)).blockID);
                        //idMap.put(field.getName(),((Block) field.get(null)).blockID);
                    } catch (Exception exception) {
                        configWriter.write(System.getProperty("line.separator") + field.getName()
                                + "=null");
                    }
                } else if (field.getType().getSuperclass() == Item.class || field.getType() == Item.class) {
                    try {
                        configWriter.write(System.getProperty("line.separator") + field.getName()
                                + "=null");// + ((Item) field.get(null)).itemID);
                        //idMap.put(field.getName(),((Item) field.get(null)).shiftedIndex);
                    } catch (Exception exception) {
                        configWriter.write(System.getProperty("line.separator") + field.getName()
                                + "=null");
                    }
                }
            }
            configWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }




    public static Integer getFromConfig(String s2, Integer base){
        try {
            SignalIndustries.LOGGER.info("Getting id for: "+s2+" (base: "+base+")");
            BufferedReader configReader = new BufferedReader(new FileReader(configFile));
            for (String s : Files.readAllLines(configFile.toPath())) {
                if (s.contains("=") && !s.startsWith("//")) {
                    String[] as = s.split("=");
                    String name = as[0];
                    int id;
                    try {
                        id = Integer.parseInt(as[1]);
                    } catch (NumberFormatException e){
                        continue;
                    }
                    if (id > 16384){
                        id -= 16384;
                    }
                    if (name.equalsIgnoreCase(s2)){
                        SignalIndustries.LOGGER.info("Id: "+id);
                        return id;
                    }
                }
            }

            configReader.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        SignalIndustries.LOGGER.info("No id defined, returning base: "+base);
        return base;
    }

    private static final File configFile = new File((Minecraft.getMinecraftDir()) + "/config/SignalIndustries.cfg");
}
