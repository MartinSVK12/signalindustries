package sunsetsatellite.signalindustries.util;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.util.HashMap;

public class Multiblock extends Structure{

    public static final HashMap<String,Multiblock> multiblocks = new HashMap<>();

    static {
        multiblocks.put("dimensionalAnchor",new Multiblock(SignalIndustries.MOD_ID,"dimensionalAnchor","dimensionalAnchor",false));
    }

    public Multiblock(String modId, String translateKey, NBTTagCompound data, boolean includeAir) {
        super(modId, translateKey, data, includeAir, false);
        this.translateKey = "multiblock."+modId+"."+translateKey+".name";
    }

    public Multiblock(String modId, String translateKey, String filePath, boolean includeAir) {
        super(modId, translateKey, filePath, includeAir, false);
        this.translateKey = "multiblock."+modId+"."+translateKey+".name";
    }

    public boolean isValidAt(World world, BlockInstance origin, Direction dir){
        for (Object o : data.func_28110_c()) {
            Vec3i pos;
            NBTTagCompound block = (NBTTagCompound) o;
            int id = getBlockId(block);
            int meta = block.getInteger("meta");
            switch (dir){
                case X_POS:
                    pos = new Vec3i(block.getInteger("z") + origin.pos.x, block.getInteger("y") + origin.pos.y, block.getInteger("x") + origin.pos.z);
                    break;
                case X_NEG:
                    pos = new Vec3i(-block.getInteger("z") + origin.pos.x, block.getInteger("y") + origin.pos.y, -block.getInteger("x") + origin.pos.z);
                    break;
                case Z_NEG:
                    pos = new Vec3i(-block.getInteger("x") + origin.pos.x, block.getInteger("y") + origin.pos.y, -block.getInteger("z") + origin.pos.z);
                    break;
                default:
                    pos = new Vec3i(block.getInteger("x") + origin.pos.x, block.getInteger("y") + origin.pos.y, block.getInteger("z") + origin.pos.z);
                    break;
            }
            if (world.getBlockId(pos.x, pos.y, pos.z) != id || (world.getBlockId(pos.x, pos.y, pos.z) == id && world.getBlockMetadata(pos.x, pos.y, pos.z) != meta && !pos.equals(origin.pos))) {
                SignalIndustries.LOGGER.error(String.format("Failed on %d %d %d (%d:%d)",pos.x,pos.y,pos.z,id,meta));
                return false;
            }
        }
        return true;
    }

}