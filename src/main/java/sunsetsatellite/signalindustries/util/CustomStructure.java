package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.NbtIo;
import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.BlockInstance;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.catalyst.multiblocks.Structure;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CustomStructure extends Structure {

    public World world;
    public boolean hasOrigin = false;

    public CustomStructure(String id, World world, boolean placeAir, boolean replaceBlocks) {
        super(SignalIndustries.MOD_ID, new Class[]{}, id, new CompoundTag(), placeAir, replaceBlocks);
        this.world = world;
        this.translateKey = id;
        loadFromNBT(id);
    }

    @Override
    public String getTranslatedName() {
        return data.getString("Name");
    }

    @Override
    public String getFullFilePath() {
        return world.getSaveHandler().getDataFile("struct_" + translateKey).getPath();
    }

    @Override
    protected void loadFromNBT(String id) {
        try {
            File file = world.getSaveHandler().getDataFile("struct_" + id);
            if (file == null) return;
            data = NbtIo.readCompressed(Files.newInputStream(file.toPath()));
            hasOrigin = data.containsKey("Origin");
        } catch (IOException e) {
            SignalIndustries.LOGGER.error("Failed to load structure: {}", id);
            e.printStackTrace();
        }
    }

    @Override
    public BlockInstance getOrigin() {
        if(hasOrigin) super.getOrigin();
        return null;
    }

    @Override
    public BlockInstance getOrigin(Vec3i origin) {
        if(hasOrigin) super.getOrigin(origin);
        return null;
    }

    @Override
    public BlockInstance getOrigin(World world, Vec3i origin) {
        if(hasOrigin) super.getOrigin(world, origin);
        return null;
    }

    @Override
    public BlockInstance getOrigin(Vec3i origin, Direction dir) {
        if(hasOrigin) super.getOrigin(origin, dir);
        return null;
    }
}
