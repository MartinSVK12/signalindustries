package sunsetsatellite.signalindustries;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sunsetsatellite.energyapi.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.energyapi.util.Config;
import sunsetsatellite.fluidapi.FluidAPI;
import turniplabs.halplibe.helper.BlockHelper;

import java.util.ArrayList;
import java.util.HashMap;


public class SignalIndustries implements ModInitializer {
    public static final String MOD_ID = "signalindustries";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static HashMap<String, ArrayList<Class<?>>> nameToGuiMap = new HashMap<>();
    public static int[] energyTex = new int[0];
    public static Block energyFlowing;
    public static Block energyStill;


    @Override
    public void onInitialize() {
        LOGGER.info("Signal Industries initialized.");
    }

    public SignalIndustries(){
        Config.init();
        energyTex = FluidAPI.registerFluidTexture(MOD_ID,"signalumenergy.png");

        energyFlowing = BlockHelper.createBlock(MOD_ID,new BlockFluidFlowing(Config.getFromConfig("signalumEnergy",1200),Material.water),"oilFlowing","signalumenergy.png",Block.soundPowderFootstep,1.0f,1.0f,0).setPlaceOverwrites().setTexCoords(energyTex[0],energyTex[1],energyTex[2],energyTex[3],energyTex[4],energyTex[5],energyTex[6],energyTex[7],energyTex[8],energyTex[9],energyTex[10],energyTex[11]);
        energyStill = BlockHelper.createBlock(MOD_ID,new BlockFluidStill(Config.getFromConfig("signalumEnergy",1200)+1,Material.water),"oilStill","signalumenergy.png",Block.soundPowderFootstep,1.0f,1.0f,0).setPlaceOverwrites().setTexCoords(energyTex[0],energyTex[1],energyTex[2],energyTex[3],energyTex[4],energyTex[5],energyTex[6],energyTex[7],energyTex[8],energyTex[9],energyTex[10],energyTex[11]);
    }

    public static void displayGui(EntityPlayer entityplayer, GuiScreen guiScreen, Container container, IInventory tile) {
        if(entityplayer instanceof EntityPlayerMP) {
            ((IEntityPlayerMP)entityplayer).displayGuiScreen(guiScreen,container,tile);
        } else {
            Minecraft.getMinecraft().displayGuiScreen(guiScreen);
        }
    }

    public static void addToNameGuiMap(String name, Class<? extends Gui> guiClass, Class<? extends TileEntity> tileEntityClass){
        ArrayList<Class<?>> list = new ArrayList<>();
        list.add(guiClass);
        list.add(tileEntityClass);
        nameToGuiMap.put(name,list);
    }
}
