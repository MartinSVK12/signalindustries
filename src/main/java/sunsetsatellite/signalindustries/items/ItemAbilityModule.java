package sunsetsatellite.signalindustries.items;


import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.fluids.impl.ContainerItemFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerAbilityModule;
import sunsetsatellite.signalindustries.gui.GuiAbilityModule;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.inventories.item.InventoryAbilityModule;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemAbilityModule extends ItemAttachment implements ITiered {

    public Tier tier;

    public ItemAbilityModule(String name, int id, Tier tier) {
        super(name, id, SignalIndustries.listOf(AttachmentPoint.CORE_MODULE));
        this.tier = tier;
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        InventoryAbilityModule inv = new InventoryAbilityModule(entityplayer.inventory.getCurrentItem());
        ContainerItemFluid container = new ContainerAbilityModule(entityplayer.inventory,inv);
        GuiAbilityModule gui = new GuiAbilityModule(container,entityplayer.inventory);
        SignalIndustries.displayGui(entityplayer,gui,container,inv,entityplayer.inventory.getCurrentItem());
        return true;
    }

    @Override
    public Tier getTier() {
        return tier;
    }
}
