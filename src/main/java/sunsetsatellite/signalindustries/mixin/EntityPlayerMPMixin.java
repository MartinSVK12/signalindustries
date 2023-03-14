package sunsetsatellite.signalindustries.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import sunsetsatellite.signalindustries.util.Config;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;

@Mixin(
        value = EntityPlayerMP.class,
        remap = false
)
public abstract class EntityPlayerMPMixin extends EntityPlayer implements IEntityPlayerMP, ICrafting {

    public EntityPlayerMPMixin(World world) {
        super(world);
    }

    @Shadow protected abstract void getNextWindowId();

    @Shadow public NetServerHandler playerNetServerHandler;

    @Shadow private int currentWindowId;

    @Override
    public void displayGuiScreen(GuiScreen guiScreen, Container container, IInventory inventory) {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, Config.getFromConfig("GuiID",8), inventory.getInvName(), inventory.getSizeInventory()));
        this.craftingInventory = container;
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.onContainerInit(((EntityPlayerMP)((Object)this)));
    }
}
