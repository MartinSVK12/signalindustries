package sunsetsatellite.signalindustries.mp.message;

import com.mojang.nbt.tags.CompoundTag;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.util.MeteorLocation;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageMeteorLocationSync implements NetworkMessage {

    public MeteorLocation location;

    public NetworkMessageMeteorLocationSync(MeteorLocation location) {
        this.location = location;
    }

    public NetworkMessageMeteorLocationSync() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        CompoundTag tag = new CompoundTag();
        location.write(tag);
        packet.writeCompoundTag(tag);

    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        location = MeteorLocation.read(packet.readCompoundTag());
    }

    @Override
    public void handle(NetworkContext context) {
        SignalIndustries.meteorLocations.add(location);
    }
}
