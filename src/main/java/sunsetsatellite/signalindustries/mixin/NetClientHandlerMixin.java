package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.net.handler.NetClientHandler;
import net.minecraft.client.world.WorldClient;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet23VehicleSpawn;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.entities.EntityCrystal;
import sunsetsatellite.signalindustries.gui.GuiPulsar;
import sunsetsatellite.signalindustries.interfaces.mixins.INetClientHandler;
import sunsetsatellite.signalindustries.inventories.InventoryPulsar;
import sunsetsatellite.signalindustries.mp.packets.PacketOpenMachineGUI;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

@Debug(
        export = true
)
@Mixin(
        value= NetClientHandler.class,
        remap = false
)
public abstract class NetClientHandlerMixin extends NetHandler implements INetClientHandler {

    @Shadow private Minecraft mc;

    @Shadow private WorldClient worldClient;

    /*@Inject(
            method = "handleOpenWindow",
            at = @At("TAIL")
    )
    public void handleOpenWindow(Packet100OpenWindow packet100openwindow, CallbackInfo ci) {
        if(packet100openwindow.inventoryType == Config.getFromConfig("GuiID",9)){
            TileEntity tile;
            try {
                tile = (TileEntity) SignalIndustries.nameToGuiMap.get(packet100openwindow.windowTitle).get(1).getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
            try {
                this.mc.displayGuiScreen((GuiScreen) SignalIndustries.nameToGuiMap.get(packet100openwindow.windowTitle).get(0).getDeclaredConstructors()[0].newInstance(this.mc.thePlayer.inventory,tile));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            this.mc.thePlayer.craftingInventory.windowId = packet100openwindow.windowId;
        }
    }*/

    @Shadow protected abstract Entity getEntityByID(int i);

    @Inject(
            method = "handleVehicleSpawn",
            at = @At(value = "FIELD",target = "Lnet/minecraft/core/net/packet/Packet23VehicleSpawn;type:I",shift = At.Shift.BEFORE),
            locals = LocalCapture.CAPTURE_FAILHARD,

            cancellable = true)
    public void handleVehicleSpawn(Packet23VehicleSpawn packet23vehiclespawn, CallbackInfo ci, double d, double d1, double d2, Entity obj){
        if(packet23vehiclespawn.type == 47){
            EntityLiving entityLiving = (EntityLiving) getEntityByID(packet23vehiclespawn.entityId);
            if(entityLiving != null){
                obj = new EntityCrystal(this.worldClient,entityLiving);
            } else {
                obj = new EntityCrystal(this.worldClient,d,d1,d2);
            }
            obj.serverPosX = packet23vehiclespawn.xPosition;
            obj.serverPosY = packet23vehiclespawn.yPosition;
            obj.serverPosZ = packet23vehiclespawn.zPosition;
            obj.yRot = packet23vehiclespawn.yaw;
            obj.xRot = packet23vehiclespawn.pitch;
            obj.id = packet23vehiclespawn.entityId;
            this.worldClient.func_712_a(packet23vehiclespawn.entityId, obj);
            if (packet23vehiclespawn.field_28044_i > 0) {
                obj.setPos((double)packet23vehiclespawn.field_28047_e / 8000.0, (double)packet23vehiclespawn.field_28046_f / 8000.0, (double)packet23vehiclespawn.field_28045_g / 8000.0);
            }
            ci.cancel();
        }
    }

    /*@Override
    public void handlePipeItemSpawn(PacketPipeItemSpawn p) {
        TileEntityItemPipe tile = (TileEntityItemPipe) this.worldClient.getBlockTileEntity(p.tileX, p.tileY, p.tileZ);
        if(tile != null){
            if(this.worldClient.func_709_b(p.entityId) == null){
                EntityPipeItem obj = new EntityPipeItem(this.worldClient,p.xPosition+p.offset.x, p.yPosition+p.offset.y, p.zPosition+p.offset.z, p.stack, null);
                obj.entityId = p.entityId;
                obj.linkEntityToPipe(tile,p.offset,p.inDir,p.outDir,p.center, p.end);
                this.worldClient.func_712_a(p.entityId, obj);
            } else {
                EntityPipeItem obj = (EntityPipeItem) this.getEntityByID(p.entityId);
                obj.linkEntityToPipe(tile,p.offset,p.inDir,p.outDir,p.center, p.end);
            }
        }
    }*/

    /*@Override
    public void handlePipeItemPosition(PacketPipeItemPos p){
        EntityPipeItem entity = (EntityPipeItem) getEntityByID(p.entityId);

        if(entity != null){
            SignalIndustries.LOGGER.info(String.format("%f %f %f",entity.posX,entity.posY,entity.posZ));
            entity.setPosition(p.xPosition,p.yPosition,p.zPosition);
        }


    }*/
    /*@ModifyVariable(
            method = "handleVehicleSpawn",
            at = @At("STORE"),
            ordinal = 0
    )
    public Entity handleVehicleSpawn(Entity entity,Packet23VehicleSpawn packet23vehiclespawn){
        if(packet23vehiclespawn.type == 47){
            EntityLiving entityLiving = (EntityLiving) getEntityByID(packet23vehiclespawn.entityId);
            if(entityLiving != null){
                return new EntityCrystal(this.worldClient,entityLiving);
            }
        }
        return entity;
    }*/

    @Override
    public boolean isServerHandler() {
        return false;
    }

    @Override
    public void handleOpenMachineGUI(PacketOpenMachineGUI p) {
        TileEntity tile = worldClient.getBlockTileEntity(p.blockX,p.blockY,p.blockZ);
        if(tile != null){
            try {
                this.mc.displayGuiScreen((GuiScreen) SignalIndustries.nameToGuiMap.get(p.windowTitle).get(0).getDeclaredConstructors()[0].newInstance(this.mc.thePlayer.inventory,tile));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            if(p.stack != null && Objects.equals(p.windowTitle, "The Pulsar")){
                InventoryPulsar inv = new InventoryPulsar(p.stack);
                this.mc.displayGuiScreen(new GuiPulsar(this.mc.thePlayer.inventory,p.stack));
                /*try {
                    this.mc.displayGuiScreen((GuiScreen) SignalIndustries.nameToGuiMap.get(p.windowTitle).get(0).getDeclaredConstructors()[0].newInstance(this.mc.thePlayer.inventory,inv));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }*/
            } else {
                try {
                    tile = (TileEntity) SignalIndustries.nameToGuiMap.get(p.windowTitle).get(1).getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                try {
                    this.mc.displayGuiScreen((GuiScreen) SignalIndustries.nameToGuiMap.get(p.windowTitle).get(0).getDeclaredConstructors()[0].newInstance(this.mc.thePlayer.inventory,tile));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }

        }
        this.mc.thePlayer.craftingInventory.windowId = p.windowId;
    }
}
