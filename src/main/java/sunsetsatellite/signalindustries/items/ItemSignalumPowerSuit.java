package sunsetsatellite.signalindustries.items;

import net.minecraft.src.*;
import net.minecraft.src.material.ArmorMaterial;
import sunsetsatellite.signalindustries.interfaces.IAttachable;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.mixins.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.misc.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Arrays;
import java.util.List;

public class ItemSignalumPowerSuit extends ItemArmorTiered implements IHasOverlay {
    public ItemSignalumPowerSuit(int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(id, material, armorPiece, tier);
    }

    @Override
    public void renderOverlay(GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, RenderItem itemRenderer) {
        SignalumPowerSuit ps = ((IPlayerPowerSuit)player).signalIndustries$getPowerSuit();
        if(ps != null && armorPiece == 0){
            ps.renderOverlay(guiIngame, fontRenderer, itemRenderer, player, height, width, mouseX, mouseY);
        }
    }
}
