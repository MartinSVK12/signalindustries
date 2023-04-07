package sunsetsatellite.signalindustries.api.impl.itempipes.misc;

import net.minecraft.src.Vec3D;
import sunsetsatellite.signalindustries.util.Direction;
import sunsetsatellite.signalindustries.util.Vec3f;

public class PipeItem {

    public EntityPipeItem entity;
    public Direction inDir;
    public Direction outDir;
    public Vec3f offset;
    public boolean goingToCenter = true;
    public boolean atEnd = false;

    public PipeItem(EntityPipeItem item, Direction inDir, Direction outDir, Vec3f pos) {
        this.entity = item;
        this.inDir = inDir;
        this.outDir = outDir;
        this.offset = pos;
    }
}
