package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.core.entity.Entity;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SignalIndustriesClient;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessagePowerSuitAction implements NetworkMessage {

    public static final int ACTIVATE = 100;
    public static final int ACTIVATE_APP = 101;
    public static final int ACTIVATE_ATTACHMENT = 102;

    public int action = -1;
    public Vec3i pos = null;
    public Class<? extends Entity> entityClass = null;
    public String attachmentKeybind = "null";
    public boolean shift = false;
    public boolean alt = false;
    public boolean ctrl = false;

    public NetworkMessagePowerSuitAction(int action) {
        this.action = action;
    }

    public NetworkMessagePowerSuitAction setPos(Vec3i pos) {
        this.pos = pos;
        return this;
    }

    public NetworkMessagePowerSuitAction setEntityClass(Class<? extends Entity> entity) {
        this.entityClass = entity;
        return this;
    }

    public NetworkMessagePowerSuitAction setKeybind(String attachmentKeybind) {
        this.attachmentKeybind = attachmentKeybind == null ? "null" : attachmentKeybind;
        return this;
    }

    public NetworkMessagePowerSuitAction setShift(boolean shift) {
        this.shift = shift;
        return this;
    }

    public NetworkMessagePowerSuitAction setAlt(boolean alt) {
        this.alt = alt;
        return this;
    }

    public NetworkMessagePowerSuitAction setCtrl(boolean ctrl) {
        this.ctrl = ctrl;
        return this;
    }

    public NetworkMessagePowerSuitAction(){

    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeBoolean(shift);
        packet.writeBoolean(alt);
        packet.writeBoolean(ctrl);
        packet.writeInt(this.action);
        packet.writeBoolean(pos != null);
        if(pos != null){
            CompoundTag nbt = new CompoundTag();
            pos.writeToNBT(nbt);
            packet.writeCompoundTag(nbt);
        }
        packet.writeString(entityClass == null ? "null" : entityClass.getName());
        packet.writeString(attachmentKeybind == null ? "null" : attachmentKeybind);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        this.shift = packet.readBoolean();
        this.alt = packet.readBoolean();
        this.ctrl = packet.readBoolean();
        action = packet.readInt();
        boolean posExists = packet.readBoolean();
        if(posExists) pos = new Vec3i(packet.readCompoundTag());
        String entityClass = packet.readString();
        if (!"null".equals(entityClass)) {
            try {
                this.entityClass = (Class<? extends Entity>) Class.forName(entityClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        this.attachmentKeybind = packet.readString();
    }

    @Override
    public void handle(NetworkContext context) {
        SignalumPowerSuit suit = ((IPlayerPowerSuit<SignalumPowerSuit>) context.player).getPowerSuit();
        if(suit != null && context.player.world != null){
            if(action >= 0 && action < 10){
                suit.selectedApplicationSlot = action;
                return;
            }
            switch (action) {
                case ACTIVATE: {
                    suit.active = !suit.active;
                    break;
                }
                case ACTIVATE_APP: {
                    if(pos != null && entityClass == null){
                        suit.activateApplication(pos.x, pos.y, pos.z);
                    } else if (pos != null) {
                        suit.activateApplication(context.player.world.getClosestPlayer(pos.x, pos.y, pos.z, 1));
                    } else {
                        suit.activateApplication();
                    }
                    break;
                }
                case ACTIVATE_ATTACHMENT: {
                    //KeyBinding keyBinding = SignalIndustriesClient.attachmentKeybinds.get(attachmentKeybind);
                    //if(keyBinding != null){
                        suit.activateAttachment(attachmentKeybind, shift, alt, ctrl);
                    //}
                    break;
                }
            }
        }
    }
}
