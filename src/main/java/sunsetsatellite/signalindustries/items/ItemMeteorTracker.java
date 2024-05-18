package sunsetsatellite.signalindustries.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.dynamictexture.DynamicTexture;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.ChunkCoordinates;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.SignalIndustries;


import static java.lang.Math.PI;

public class ItemMeteorTracker extends Item implements ICustomDescription {


    public ItemMeteorTracker(String name, int id) {
        super(name, id);
    }

    /*@Override
    public int getIconFromDamage(int id) {
        if(id == 1){
            int[] a = TextureHelper.getOrCreateItemTexture(SignalIndustries.MOD_ID,"meteor_tracker.png");
            return Item.iconCoordToIndex(a[0],a[1]);
        }
        return iconIndex;
    }*/

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(itemstack.getMetadata() == 0){
            itemstack.setMetadata(1);
        } else {

            ChunkCoordinates chunk = null;
            double distance = Double.MAX_VALUE;
            Minecraft mc = Minecraft.getMinecraft(this);
            for (ChunkCoordinates meteorLocation : SignalIndustries.meteorLocations) {
                if(meteorLocation.getSqDistanceTo((int) mc.thePlayer.x, (int) mc.thePlayer.y, (int) mc.thePlayer.z) < distance){
                    distance = meteorLocation.getSqDistanceTo((int) mc.thePlayer.x, (int) mc.thePlayer.y, (int) mc.thePlayer.z);
                    chunk = meteorLocation;
                }
            }
            if(chunk != null){
                if(entityplayer.isSneaking() && distance < 5){
                    mc.ingameGUI.addChatMessage("This meteor will no longer be tracked.");
                    SignalIndustries.meteorLocations.remove(chunk);
                } else {
                    mc.ingameGUI.addChatMessage(String.format("Distance: %.0f blocks", distance));
                }
            } else {
                mc.ingameGUI.addChatMessage("No meteors detected nearby.");
            }
        }
        return super.onItemRightClick(itemstack, world, entityplayer);
    }

    @Override
    public String getDescription(ItemStack stack) {
        if(stack.getMetadata() != 1){
            return "Uncalibrated!\n"+ TextFormatting.GRAY +"Right-click while holding in your hand to calibrate.";
        }
        return "Calibrated.";
    }
}
