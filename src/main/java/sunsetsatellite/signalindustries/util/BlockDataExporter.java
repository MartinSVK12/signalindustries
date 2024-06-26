package sunsetsatellite.signalindustries.util;

import sunsetsatellite.signalindustries.SignalIndustries;

public class BlockDataExporter {
    //FIXME
    public static void export(Class<?> clazz) {
        SignalIndustries.LOGGER.error("Attempted to call a method with unfinished implementation.");
//        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));
//        fields.removeIf((F)->F.getType() != Block.class);
//        CompoundTag data = new CompoundTag();
//        for (Field field : fields) {
//            Block block;
//            try {
//               block = (Block) field.get(null);
//            } catch (IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//            CompoundTag tag = new CompoundTag();
//            tag.putString("name",field.getName());
//            tag.putString("mod", clazz.getName());
//            CompoundTag uv = new CompoundTag();
//            for (Side side : Side.values()) {
//                if(side == Side.NONE) continue;
//                //int index = block.atlasIndices[side.getId()];
//                String file = "";
//                /*for (Map.Entry<String, int[]> entry : TextureHelper.registeredBlockTextures.entrySet()) {
//                    String K = entry.getKey();
//                    int[] V = entry.getValue();
//                    int texIndex = Block.texCoordToIndex(V[0],V[1]);
//                    if(texIndex == index){
//                        file = K;
//                    }
//                }*/
//                if(file.isEmpty()){
//                    uv.putString(side.name(),"no_tex.png");
//                } else {
//                    uv.putString(side.name(),file.split(":")[1]);
//                }
//
//            }
//            tag.putCompound("uv",uv);
//            tag.putString("main_uv",uv.getString("SOUTH"));
//            tag.putString("draw_type","normal");
//            tag.putBoolean("tile_entity", BlockTileEntity.class.isAssignableFrom(block.getClass()));
//            data.putCompound(field.getName(),tag);
//        }
//        String s = String.format("%s\\%s.nbt", Minecraft.getMinecraft(Minecraft.class).getMinecraftDir(), "blockInfo");
//        File file = new File(s);
//        try {
//            try (DataOutputStream output = new DataOutputStream(Files.newOutputStream(file.toPath()))) {
//                NbtIo.write(data, output);
//            }
//        } catch (Exception e){
//            throw new RuntimeException(e);
//        }
    }
}
