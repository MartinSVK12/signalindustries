package sunsetsatellite.signalindustries.tiles.machines.multiblocks;

import net.minecraft.core.block.Block;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.multiblocks.Multiblock;
import sunsetsatellite.catalyst.multiblocks.MultiblockInstance;
import sunsetsatellite.signalindustries.SIRecipes;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMultiblock;
import sunsetsatellite.signalindustries.util.Tier;

public class TileEntityGreenhouse extends TileEntityTieredMultiblock {

    @Override
    public void init(Block<?> block) {
        super.init(block);
        usesEnergy = true;
        usesItemInput = true;
        usesItemOutput = true;
        minimumEnergyTier = Tier.BASIC;
        minimumItemInputTier = Tier.BASIC;
        minimumItemOutputTier = Tier.BASIC;

        recipeGroup = SIRecipes.GREENHOUSE;

        multiblock = new MultiblockInstance(this, Multiblock.multiblocks.get("greenhouse"));

        baseParallel = 24;
    }

    @Override
    public void tick() {
        super.tick();
        /*Vec3i middle = getPosition().add(Direction.getDirectionFromSide(getBlockMeta()).getOpposite().getVec().multiply(3));
        if(isBurning() && currentRecipe instanceof RecipeEntryMachineMultiOutput && worldObj != null){
            Block<?> cropBlock = ((ICrop) ((RecipeEntryMachineMultiOutput) currentRecipe).getInput()[0].resolve().get(0).getItem()).getCropBlock();
            int growthStage = getProgressScaled(7);
            Vec3i offset = middle.copy().add(new Vec3i(2,0,2));
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if(offset.x-i == middle.x && offset.z-j == middle.z) continue;
                    int blockId = worldObj.getBlockId(offset.x - i, offset.y, offset.z - j);
                    if(blockId == 0 || (blockId == cropBlock.id() && worldObj.getBlockMetadata(offset.x-i,offset.y,offset.z-j) < growthStage)){
                        worldObj.setBlockAndMetadata(offset.x-i,offset.y,offset.z-j,cropBlock.id(),growthStage);
                    }
                }
            }
        } else if(worldObj != null){
            Vec3i offset = middle.copy().add(new Vec3i(2,0,2));
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if(offset.x-i == middle.x && offset.z-j == middle.z) continue;
                    int blockId = worldObj.getBlockId(offset.x - i, offset.y, offset.z - j);
                    if(blockId != 0){
                        worldObj.setBlock(offset.x-i,offset.y,offset.z-j,0);
                    }
                }
            }
        }*/
    }

    @Override
    public @NonNull String getNameTranslationKey() {
        return "container.signalindustries.greenhouse";
    }
}
