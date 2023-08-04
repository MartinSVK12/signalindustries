package sunsetsatellite.signalindustries.inventories;

import net.minecraft.client.Minecraft;


import sunsetsatellite.fluidapi.api.ItemInventoryFluid;
import sunsetsatellite.signalindustries.SignalIndustries;

public class InventoryPulsar extends ItemInventoryFluid {

    private final ItemStack pulsar;

    public InventoryPulsar(ItemStack pulsar) {
        super(pulsar);
        this.pulsar = pulsar;
        fluidCapacity[0] = 16000;//acceptedFluids.get(0).add((BlockFluid) SignalIndustries.energyFlowing);*/
    }

    /*public void setInventorySlotContents(int i, ItemStack itemstack) {
        if(itemstack != null && itemstack.itemID == SignalIndustries.warpOrb.itemID){
            if(!itemstack.tag.hasKey("position")){
                 Minecraft.getMinecraft().ingameGUI.addChatMessage(ChatColor.white+"The Pulsar> "+ChatColor.orange+"WARNING:"+ChatColor.white+" This "+ChatColor.magenta+"Warp Orb"+ChatColor.white+" isn't encoded! There is no telling where this might lead you to. You might die or not be able to get back if you're unprepared. Proceed with caution.");
            }
        }
        super.setInventorySlotContents(i,itemstack);
    }*/


    public String getInvName() {
        return "The Pulsar";
    }


}
