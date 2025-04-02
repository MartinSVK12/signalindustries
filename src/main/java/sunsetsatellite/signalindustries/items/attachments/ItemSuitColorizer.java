package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.List;

public class ItemSuitColorizer extends ItemTieredAttachment {

    public final String path;

    public ItemSuitColorizer(String translationKey, String namespaceId, int id, List<AttachmentPoint> attachmentPoints, Tier tier, String path) {
        super(translationKey, namespaceId, id, attachmentPoints, tier);
        this.path = path;
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
        List<ItemSuitColorizer> list = new ArrayList<>();
        list.add(SIItems.suitColorizerWhite);
        list.add(SIItems.suitColorizerBlue);
        list.add(SIItems.suitColorizerPurple);
        list.add(SIItems.suitColorizerInverted);
        list.add(SIItems.suitColorizerTransparent);
        int i = list.indexOf((ItemSuitColorizer) itemstack.getItem());
        if(i != -1){
            ItemSuitColorizer colorizer = list.get((i + 1) % list.size());
            itemstack.itemID = colorizer.id;
        }
        return super.onUseItem(itemstack, world, entityplayer);
    }

    @Override
    public void tick(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, int slot) {

    }

    @Override
   public void activate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {

    }

    @Override
    public void altActivate(ItemStack stack, IPowerSuit signalumPowerSuit, Player player, World world, boolean shift, boolean ctrl, boolean alt) {

    }

    @Override
    public void renderWhenAttached(Player player, IPowerSuit signalumPowerSuit, ModelBiped modelBipedMain, ItemStack stack) {

    }
}
