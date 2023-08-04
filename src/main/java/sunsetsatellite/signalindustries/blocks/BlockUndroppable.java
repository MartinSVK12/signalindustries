package sunsetsatellite.signalindustries.blocks;




import java.util.Random;

public class BlockUndroppable extends Block {
    public BlockUndroppable(int i, Material material) {
        super(i, material);
    }

    @Override
    public int idDropped(int i, Random random) {
        return 0;
    }
}
