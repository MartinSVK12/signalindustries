package sunsetsatellite.signalindustries.util;

import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import sunsetsatellite.energyapi.util.Vec3;

public enum Direction {
    X_POS (new Vec3(1,0,0)),
    X_NEG (new Vec3(-1,0,0)),
    Y_POS (new Vec3(0,1,0)),
    Y_NEG (new Vec3(0,-1,0)),
    Z_POS (new Vec3(0,0,1)),
    Z_NEG (new Vec3(0,0,-1));

    private final Vec3 vec;
    private Direction opposite;

    Direction(Vec3 vec3) {
        this.vec = vec3;
    }

    public TileEntity getTileEntity(World world, TileEntity tile){
        Vec3 pos = new Vec3(tile.xCoord + vec.x, tile.yCoord + vec.y, tile.zCoord + vec.z);
        return world.getBlockTileEntity(pos.x,pos.y,pos.z);
    }

    public Direction getOpposite(){
        return opposite;
    }

    static {
        X_POS.opposite = X_NEG;
        X_NEG.opposite = X_POS;
        Y_NEG.opposite = Y_POS;
        Y_POS.opposite = Y_NEG;
        Z_NEG.opposite = Z_POS;
        Z_POS.opposite = Z_NEG;
    }

}
