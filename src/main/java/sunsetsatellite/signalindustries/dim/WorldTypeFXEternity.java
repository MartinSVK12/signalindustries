package sunsetsatellite.signalindustries.dim;

import net.minecraft.client.render.worldtype.WorldTypeFX;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.core.world.type.WorldType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.NonNull;

public class WorldTypeFXEternity extends WorldTypeFX {
    public WorldTypeFXEternity(WorldType worldType) {
        super(worldType);
    }

    @Override
    public @NotNull Vector3fc getFogColor(@NonNull World world, double x, double y, double z, float celestialAngle, float partialTick) {
        return new Vector3f(0.7f, 0.7f, 0.7f);
    }
}
