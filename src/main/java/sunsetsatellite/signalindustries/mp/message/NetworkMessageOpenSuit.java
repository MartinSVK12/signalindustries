package sunsetsatellite.signalindustries.mp.message;

import net.minecraft.core.enums.HumanArmorShape;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class NetworkMessageOpenSuit implements NetworkMessage {

    public HumanArmorShape part;

    public NetworkMessageOpenSuit(HumanArmorShape part) {
        this.part = part;
    }

    public NetworkMessageOpenSuit() {

    }

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(part.ordinal());
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        part = HumanArmorShape.values()[packet.readInt()];
    }

    @Override
    public void handle(NetworkContext context) {
        if (EnvironmentHelper.isServerEnvironment()) {
            if (context.player.world != null) {
                Catalyst.displayGui(context.player, new InventoryPowerSuit(context.player.inventory.armorItemInSlot(part)), part.getSlotIndex(), true, key("gui/power_suit"));
            }
        }
    }
}
