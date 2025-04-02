package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.entity.player.PlayerRemote;
import net.minecraft.core.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import java.util.Objects;
import java.util.UUID;

public class NetworkMessagePowerSuitRemoteSync implements NetworkMessage {

    public CompoundTag data;
    public UUID uuid;
    public String username;

    public NetworkMessagePowerSuitRemoteSync() {}

    public NetworkMessagePowerSuitRemoteSync(String username, UUID uuid, CompoundTag data) {
        this.data = data;
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeString(username);
        packet.writeString(uuid.toString());
        packet.writeCompoundTag(data);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        this.username = packet.readString();
        this.uuid = UUID.fromString(packet.readString());
        data = packet.readCompoundTag();
    }

    @Override
    public void handle(NetworkContext context) {
        if (context.player.world != null) {
            for (Player player : context.player.world.players) {
                if(player instanceof PlayerRemote){
                    if((player.uuid == null && player.username.equals(username)) || Objects.equals(player.uuid, uuid)){
                        if(player instanceof IPlayerPowerSuit<?>){
                            IPowerSuit suit = ((IPlayerPowerSuit<?>) player).getPowerSuit();
                            if(suit != null){
                                suit.loadData(data.getCompound("PowerSuit"));
                            }
                        }

                    }
                }
            }
        }
    }
}
