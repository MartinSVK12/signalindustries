package sunsetsatellite.signalindustries.interfaces;

import sunsetsatellite.signalindustries.util.MultiblockPart;

public interface IMultiblockPartBlock {
    MultiblockPart.Type getType();
    MultiblockPart.IO getIO();
}
