package sunsetsatellite.signalindustries.mp.message;

import net.minecraft.core.player.inventory.container.ContainerInventory;
import org.jetbrains.annotations.NotNull;
import sunsetsatellite.signalindustries.items.tools.ItemSignalumDrill;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkMessage;
import turniplabs.halplibe.helper.network.UniversalPacket;

public class NetworkMessageDrillModeChange implements NetworkMessage {

    public ItemSignalumDrill.DrillMode drillMode;

    public NetworkMessageDrillModeChange(ItemSignalumDrill.DrillMode drillMode) {
        this.drillMode = drillMode;
    }

    public NetworkMessageDrillModeChange() {}

    @Override
    public void encodeToUniversalPacket(@NotNull UniversalPacket packet) {
        packet.writeInt(drillMode.ordinal());
    }

    @Override
    public void decodeFromUniversalPacket(@NotNull UniversalPacket packet) {
        drillMode = ItemSignalumDrill.DrillMode.values()[packet.readInt()];
    }

    @Override
    public void handle(NetworkContext context) {
        if(EnvironmentHelper.isServerEnvironment()) {
            if (context.player.world != null) {
                ContainerInventory inv = context.player.inventory;
                if (inv.getCurrentItem() != null && inv.getCurrentItem().getItem() instanceof ItemSignalumDrill) {
                    ((ItemSignalumDrill) inv.getCurrentItem().getItem()).setMode(inv.getCurrentItem(), drillMode);
                }
            }
        }
    }
}

