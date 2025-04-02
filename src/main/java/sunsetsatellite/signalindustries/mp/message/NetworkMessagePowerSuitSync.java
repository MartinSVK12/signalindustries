package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessagePowerSuitSync implements NetworkMessage {

    public CompoundTag data;

    public NetworkMessagePowerSuitSync(CompoundTag data) {
        this.data = data;
    }

    public NetworkMessagePowerSuitSync() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeCompoundTag(data);
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        data = packet.readCompoundTag();
    }

    @Override
    public void handle(NetworkContext context) {
        IPowerSuit suit = ((IPlayerPowerSuit<?>) context.player).getPowerSuit();
        if(suit != null){
            suit.loadData(data.getCompound("PowerSuit"));
        }
    }
}
