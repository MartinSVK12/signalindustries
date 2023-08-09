package sunsetsatellite.signalindustries.items;


import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.material.ArmorMaterial;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemSignalumPowerSuit extends ItemArmorTiered implements IHasOverlay {
    public ItemSignalumPowerSuit(String key, int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(key, id, material, armorPiece, tier);
    }

    @Override
    public void renderOverlay(GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {
        SignalumPowerSuit ps = ((IPlayerPowerSuit)player).signalIndustries$getPowerSuit();
        if(ps != null && armorPiece == 0){
            ps.renderOverlay(guiIngame, fontRenderer, itemRenderer, player, height, width, mouseX, mouseY);
        }
    }
}
