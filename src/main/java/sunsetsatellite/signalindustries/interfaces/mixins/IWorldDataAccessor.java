package sunsetsatellite.signalindustries.interfaces.mixins;

import net.minecraft.core.world.save.LevelStorage;
import org.spongepowered.asm.mixin.Unique;

public interface IWorldDataAccessor {
    LevelStorage getSaveHandler();
}
