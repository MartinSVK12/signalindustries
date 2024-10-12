package sunsetsatellite.signalindustries.util;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.block.model.BlockModel;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockTileEntity;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.signalindustries.SignalIndustries;

import java.io.DataOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockDataExporter {
    public static void export(Class<?> clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
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
            tag.putString("mod", clazz.getName());
            tag.putString("draw_type","normal");
            CompoundTag uv = new CompoundTag();
            for (Side side : Side.values()) {
                if(side == Side.NONE) continue;
                BlockModel<?> blockModel = BlockModelDispatcher.getInstance().getDispatch(block);
                IconCoordinate texture = blockModel.getBlockTextureFromSideAndMetadata(side, 2);
                String file = String.valueOf(texture.namespaceId).split(":")[1];
                uv.putString(side.name(),file+".png");
            }
            tag.putCompound("uv",uv);
            tag.putString("main_uv",uv.getString("SOUTH"));
            tag.putBoolean("tile_entity", BlockTileEntity.class.isAssignableFrom(block.getClass()));
            data.putCompound(field.getName(),tag);
            String s = String.format("%s\\%s.nbt", Minecraft.getMinecraft(Minecraft.class).getMinecraftDir(), "blockInfo");
            File file = new File(s);
            try {
                try (DataOutputStream output = new DataOutputStream(Files.newOutputStream(file.toPath()))) {
                    NbtIo.write(data, output);
                }
            } catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        SignalIndustries.LOGGER.info("Exported "+fields.size()+" blocks!");
    }
}
