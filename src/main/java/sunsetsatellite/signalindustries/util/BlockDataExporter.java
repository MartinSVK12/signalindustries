package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.signalindustries.SignalIndustries;
import turniplabs.halplibe.helper.TextureHelper;

import java.io.DataOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BlockDataExporter {
    public static void export() {
        List<Field> fields = new ArrayList<>(Arrays.asList(SignalIndustries.class.getDeclaredFields()));
        fields.removeIf((F)->F.getType() != Block.class);
        CompoundTag data = new CompoundTag();
        for (Field field : fields) {
            Block block;
            try {
               block = (Block) field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            CompoundTag tag = new CompoundTag();
            tag.putString("name",field.getName());
            tag.putString("mod",SignalIndustries.class.getName());
            CompoundTag uv = new CompoundTag();
            for (Side side : Side.values()) {
                if(side == Side.NONE) continue;
                int index = block.atlasIndices[side.getId()];
                String file = "";
                for (Map.Entry<String, int[]> entry : TextureHelper.registeredBlockTextures.entrySet()) {
                    String K = entry.getKey();
                    int[] V = entry.getValue();
                    int texIndex = Block.texCoordToIndex(V[0],V[1]);
                    if(texIndex == index){
                        file = K;
                    }
                }
                uv.putString(side.name(),file.split(":")[1]);
            }
            tag.putCompound("uv",uv);
            tag.putString("main_uv",uv.getString("SOUTH"));
            tag.putString("draw_type","normal");
            tag.putBoolean("tile_entity", BlockTileEntity.class.isAssignableFrom(block.getClass()));
            data.putCompound(field.getName(),tag);
        }
        String s = String.format("%s\\%s.nbt", Minecraft.getMinecraft(Minecraft.class).getMinecraftDir(), "blockInfo");
        File file = new File(s);
        try {
            try (DataOutputStream output = new DataOutputStream(Files.newOutputStream(file.toPath()))) {
                NbtIo.write(data, output);
                SignalIndustries.LOGGER.info("exported block data");
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
