package sunsetsatellite.signalindustries.mixin;

import net.minecraft.core.net.packet.PacketRespawn;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = PacketRespawn.class,remap = false)
public abstract class PacketRespawnMixin {

}
