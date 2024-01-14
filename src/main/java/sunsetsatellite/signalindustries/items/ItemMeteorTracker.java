package sunsetsatellite.signalindustries.items;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.helper.TextureHelper;

public class ItemMeteorTracker extends Item implements ICustomDescription {

    public ItemMeteorTracker(int id) {
        super(id);
    }

    @Override
    public int getIconFromDamage(int id) {
        if(id == 1){
            int[] a = TextureHelper.getOrCreateItemTexture(SignalIndustries.MOD_ID,"meteor_tracker.png");
            return Item.iconCoordToIndex(a[0],a[1]);
        }
        return iconIndex;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(itemstack.getMetadata() == 0){
            itemstack.setMetadata(1);
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
