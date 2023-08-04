package sunsetsatellite.signalindustries.blocks;




public class BlockTransparent extends Block {
    public BlockTransparent(int i, Material material) {
        super(i, material);
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }
}
