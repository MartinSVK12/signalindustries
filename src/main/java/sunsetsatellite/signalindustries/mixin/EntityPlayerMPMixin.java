package sunsetsatellite.signalindustries.mixin;


import net.minecraft.client.gui.GuiScreen;
import net.minecraft.core.crafting.ICrafting;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.Container;
import net.minecraft.core.player.inventory.IInventory;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.EntityPlayerMP;
import net.minecraft.server.net.handler.NetServerHandler;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import sunsetsatellite.signalindustries.interfaces.mixins.IEntityPlayerMP;
import sunsetsatellite.signalindustries.inventories.TileEntityWithName;
import sunsetsatellite.signalindustries.mp.packets.PacketOpenMachineGUI;

@Debug(
        export = true
)
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


    public void displayGuiScreen_si(GuiScreen guiScreen, Container container, IInventory inventory, int x, int y, int z) {
        this.getNextWindowId();
        //this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, Config.getFromConfig("GuiID",8), inventory.getInvName(), inventory.getSizeInventory()));
        this.playerNetServerHandler.sendPacket(new PacketOpenMachineGUI(this.currentWindowId,inventory.getInvName(),inventory.getSizeInventory(),x,y,z));
        this.craftingInventory = container;
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.onContainerInit(((EntityPlayerMP)((Object)this)));
    }

    public void displayGuiScreen_si(GuiScreen guiScreen, TileEntityWithName inventory, int x, int y, int z){
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new PacketOpenMachineGUI(this.currentWindowId,inventory.getName(),0,x,y,z));
    }

    public void displayItemGuiScreen_si(GuiScreen guiScreen, Container container, IInventory inventory, ItemStack stack){
        this.getNextWindowId();
        //this.playerNetServerHandler.sendPacket(new Packet100OpenWindow(this.currentWindowId, Config.getFromConfig("GuiID",8), inventory.getInvName(), inventory.getSizeInventory()));
        this.playerNetServerHandler.sendPacket(new PacketOpenMachineGUI(this.currentWindowId,inventory.getInvName(),inventory.getSizeInventory(),stack));
        this.craftingInventory = container;
        this.craftingInventory.windowId = this.currentWindowId;
        this.craftingInventory.onContainerInit(((EntityPlayerMP)((Object)this)));
    }


}
