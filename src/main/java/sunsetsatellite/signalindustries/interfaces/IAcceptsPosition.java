package sunsetsatellite.signalindustries.interfaces;

import net.minecraft.core.util.helper.Side;

public interface IAcceptsPosition {
    void receivePosition(int x, int y, int z, Side side, int dim);
}
