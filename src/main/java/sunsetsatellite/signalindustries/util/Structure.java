package sunsetsatellite.signalindustries.util;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.Sys;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.tiles.TileEntityRecipeMaker;
import sunsetsatellite.sunsetutils.util.Direction;
import sunsetsatellite.sunsetutils.util.Vec3i;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Objects;

public class Structure {
    public String modId;
    public String translateKey;
    public String filePath;
    public NBTTagCompound data;
    public boolean placeAir;
    public boolean replaceBlocks;

    public static HashMap<String,Structure> internalStructures = new HashMap<>();

    static {
        reloadInternalStructures();
    }

    public Structure(String modId, String translateKey, NBTTagCompound data, boolean placeAir, boolean replaceBlocks){
        this.modId = modId;
        this.translateKey = "structure."+modId+"."+translateKey+".name";
        this.data = data;
        this.filePath = null;
        this.placeAir = placeAir;
        this.replaceBlocks = replaceBlocks;
    }

    public Structure(String modId, String translateKey, String filePath, boolean placeAir, boolean replaceBlocks){
        this.modId = modId;
        this.translateKey = "structure."+modId+"."+translateKey+".name";
        this.placeAir = placeAir;
        this.replaceBlocks = replaceBlocks;
        loadFromNBT(filePath);
    }

    public static void reloadInternalStructures() {
        SignalIndustries.LOGGER.info(String.format("Loaded %d internal structures.",internalStructures.size()));
    }

    public String getTranslatedName(){
        return StringTranslate.getInstance().translateNamedKey(this.translateKey);
    }

    public String getFullFilePath(){
        if(filePath != null){
            return "/assets/"+modId+"/structures/"+filePath+".nbt";
        } else {
            return null;
        }
    }

    //data.getCompoundTag("Data").func_28110_c()

    public boolean placeStructure(World world, int originX, int originY, int originZ){
        for (Object o : data.getCompoundTag("Data").func_28110_c()) {
            NBTTagCompound block = (NBTTagCompound) o;
            if (!replaceBlocks && world.getBlockId(block.getInteger("x") + originX, block.getInteger("y") + originY, block.getInteger("z") + originZ) != 0) {
                return false;
            }
        }
        for (Object o : data.getCompoundTag("Data").func_28110_c()) {
            NBTTagCompound block = (NBTTagCompound) o;
            int id = getBlockId(block);
            if(id != 0 || placeAir){
                world.setBlockAndMetadataWithNotify(block.getInteger("x")+originX,block.getInteger("y")+originY, block.getInteger("z")+originZ, id, block.getInteger("meta"));
            }
        }
        return true;
    }

    public static Structure saveStructureAroundOrigin(World world, Vec3i origin, Vec3i size, String filePath, boolean placeAir, boolean replaceBlocks){
        if(size.x >= 0 && size.y >= 0 && size.z >= 0){
            int n = 0;
            NBTTagCompound data = new NBTTagCompound();
            NBTTagCompound struct = new NBTTagCompound();
            for (int x = origin.x-size.x; x <= origin.x+size.x; x++) {
                for (int y = origin.y-size.y; y <= origin.y+size.y; y++) {
                    for (int z = origin.z-size.z; z <= origin.z+size.z; z++) {
                        if(world.getBlockId(x,y,z) != 0 || placeAir){
                            NBTTagCompound block = new NBTTagCompound();
                            block.setInteger("x",x-origin.x);
                            block.setInteger("y",y-origin.y);
                            block.setInteger("z",z-origin.z);
                            String s = TileEntityRecipeMaker.getBlockFieldName(Block.getBlock(world.getBlockId(x,y,z)));
                            if(!s.contains("Block.")){
                                s = s.replace(".",":");
                                block.setString("id",s);
                            } else {
                                block.setInteger("id",world.getBlockId(x,y,z));
                            }
                            block.setInteger("meta",world.getBlockMetadata(x,y,z));
                            data.setCompoundTag(String.valueOf(n),block);
                            n++;
                        }
                    }
                }
            }
            struct.setCompoundTag("Data",data);
            SignalIndustries.LOGGER.info(n+" blocks saved.");
            Structure structure = new Structure(SignalIndustries.MOD_ID,filePath,struct,placeAir,replaceBlocks);
            structure.filePath = filePath;
            return structure;
        } else {
            SignalIndustries.LOGGER.error("Invalid parameters!");
            return null;
        }
    }

    public static Structure saveStructure(World world, Vec3i pos1, Vec3i pos2, String filePath, boolean placeAir, boolean replaceBlocks){
        NBTTagCompound struct = new NBTTagCompound();
        NBTTagCompound data = new NBTTagCompound();
        Vec3i diff = new Vec3i(pos1.x-pos2.x,pos1.y-pos2.y,pos1.z-pos2.z);
        int n = 0;
        SignalIndustries.LOGGER.info(diff.toString());
        if(pos1.x < pos2.x){
            int temp = pos1.x;
            pos1.x = pos2.x;
            pos2.x = temp;
        }
        if(pos1.y < pos2.y){
            int temp = pos1.y;
            pos1.y = pos2.y;
            pos2.y = temp;
        }
        if(pos1.z < pos2.z){
            int temp = pos1.z;
            pos1.z = pos2.z;
            pos2.z = temp;
        }
        int i = 0,j = 0,k = 0;
        for (int x = pos2.x; x <= pos1.x; x++) {
            for(int y = pos2.y; y <= pos1.y; y++){
                for(int z = pos2.z; z <= pos1.z; z++){
                    if(world.getBlockId(x,y,z) != 0 || placeAir){
                        i = x - pos1.x;
                        j = y - pos1.y;
                        k = z - pos1.z;
                        NBTTagCompound block = new NBTTagCompound();
                        block.setInteger("x",i);
                        block.setInteger("y",j);
                        block.setInteger("z",k);
                        String s = TileEntityRecipeMaker.getBlockFieldName(Block.getBlock(world.getBlockId(x,y,z)));
                        if(!s.contains("Block.")){
                            s = s.replace(".",":");
                            block.setString("id",s);
                        } else {
                            block.setInteger("id",world.getBlockId(x,y,z));
                        }
                        block.setInteger("meta",world.getBlockMetadata(x,y,z));
                        data.setCompoundTag(String.valueOf(n),block);
                        n++;
                    }
                }
            }
        }
        struct.setCompoundTag("Data",data);
        SignalIndustries.LOGGER.info(n+" blocks saved.");
        Structure structure = new Structure(SignalIndustries.MOD_ID,filePath,struct,placeAir,replaceBlocks);
        structure.filePath = filePath;
        return structure;
    }

    public boolean placeStructure(World world, int originX, int originY, int originZ, String direction){

        for (Object o : data.getCompoundTag("Data").func_28110_c()) {
            Vec3i pos;
            NBTTagCompound block = (NBTTagCompound) o;
            Direction dir = Direction.getFromName(direction);
            switch (dir){
                case X_POS:
                    pos = new Vec3i(block.getInteger("z") + originX, block.getInteger("y") + originY, block.getInteger("x") + originZ);
                    break;
                case X_NEG:
                    pos = new Vec3i(-block.getInteger("z") + originX, block.getInteger("y") + originY, -block.getInteger("x") + originZ);
                    break;
                case Z_NEG:
                    pos = new Vec3i(-block.getInteger("x") + originX, block.getInteger("y") + originY, -block.getInteger("z") + originZ);
                    break;
                default:
                    pos = new Vec3i(block.getInteger("x") + originX, block.getInteger("y") + originY, block.getInteger("z") + originZ);
                    break;
            }
            if (!replaceBlocks && world.getBlockId(pos.x, pos.y, pos.z) != 0) {
                return false;
            }
        }
        for (Object o : data.getCompoundTag("Data").func_28110_c()) {
            Vec3i pos;
            Direction dir = Direction.getFromName(direction);
            NBTTagCompound block = (NBTTagCompound) o;
            switch (dir){
                case X_POS:
                    pos = new Vec3i(block.getInteger("z") + originX, block.getInteger("y") + originY, block.getInteger("x") + originZ);
                    break;
                case X_NEG:
                    pos = new Vec3i(-block.getInteger("z") + originX, block.getInteger("y") + originY, -block.getInteger("x") + originZ);
                    break;
                case Z_NEG:
                    pos = new Vec3i(-block.getInteger("x") + originX, block.getInteger("y") + originY, -block.getInteger("z") + originZ);
                    break;
                default:
                    pos = new Vec3i(block.getInteger("x") + originX, block.getInteger("y") + originY, block.getInteger("z") + originZ);
                    break;
            }
            int id = getBlockId(block);
            if(id != 0 || placeAir){
                world.setBlockAndMetadataWithNotify(pos.x, pos.y, pos.z, id, block.getInteger("meta"));
            }
        }
        return true;
    }

    public static int getBlockId(NBTTagCompound block){
        NBTBase nbt = block.getTag("id");
        if(nbt instanceof NBTTagInt){
            return ((NBTTagInt) nbt).intValue;
        } else if (nbt instanceof NBTTagString) {
            String[] args = ((NBTTagString) nbt).stringValue.split(":");

            try {
                Class<?> clazz = Class.forName(args[0]);
                Field field = clazz.getDeclaredField(args[1]);
                Block b = (Block) field.get(null);
                return b.blockID;
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | ClassCastException e) {
               e.printStackTrace();
               return 0;
            }

        }
        return 0;
    }
    
    protected void loadFromNBT(String name) {
        try {
            File file;
            file = new File(Objects.requireNonNull(this.getClass().getResource("/assets/"+modId+"/structures/"+name+".nbt")).toURI());
            FileInputStream fileinputstream = new FileInputStream(file);
            this.data = CompressedStreamTools.func_1138_a(fileinputstream);
            SignalIndustries.LOGGER.info(String.format("Structure '%s' contains %d blocks.",name,this.data.getCompoundTag("Data").func_28110_c().size()));
        } catch (IOException | URISyntaxException e){
            e.printStackTrace();
        }

    }

    protected void saveToNBT(){
        File file;
        String s = String.format("%s\\%s.nbt", Minecraft.getMinecraftDir(), this.filePath);
        file = new File(s);
        try {
            if(file.createNewFile()){
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    CompressedStreamTools.writeGzippedCompoundToOutputStream(this.data,fileOutputStream);
                    SignalIndustries.LOGGER.info(String.format("Structure '%s' saved to %s",this.filePath,s));
                }
            } else {
                try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                    CompressedStreamTools.writeGzippedCompoundToOutputStream(this.data,fileOutputStream);
                    SignalIndustries.LOGGER.info(String.format("Structure '%s' saved to %s",this.filePath,s));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
