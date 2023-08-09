package sunsetsatellite.signalindustries.interfaces.mixins;

import sunsetsatellite.signalindustries.mp.packets.PacketOpenMachineGUI;

public interface INetClientHandler {
    void handleOpenMachineGUI(PacketOpenMachineGUI p);

    //void handlePipeItemSpawn(PacketPipeItemSpawn packetPipeItemSpawn);

}
