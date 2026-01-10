package sunsetsatellite.signalindustries.dim;

import net.minecraft.client.render.worldtype.WorldTypeFX;
import net.minecraft.core.util.phys.Vec3;
import net.minecraft.core.world.World;
import net.minecraft.core.world.type.WorldType;

public class WorldTypeFXEternity extends WorldTypeFX {
    public WorldTypeFXEternity(WorldType worldType) {
        super(worldType);
    }

    @Override
    public Vec3 getFogColor(World world, double x, double y, double z, float celestialAngle, float partialTick) {
        return Vec3.getTempVec3(0.7, 0.7, 0.7);
    }
}
