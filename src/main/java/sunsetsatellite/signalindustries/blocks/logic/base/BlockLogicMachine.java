package sunsetsatellite.signalindustries.blocks.logic.base;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.multiblocks.IMultiblock;
import sunsetsatellite.signalindustries.SIAchievements;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.function.Supplier;

import static sunsetsatellite.signalindustries.SignalIndustries.key;

public class BlockLogicMachine extends BlockLogicMachineBase {

    public final String guiId;
    private boolean solid = false;

    public BlockLogicMachine(Block<?> block, Material material, Tier tier, Supplier<TileEntity> tileEntitySupplier, String guiId) {
        super(block, material, tier, tileEntitySupplier);
        this.guiId = guiId;
    }

    @Override
    public void onBlockRemoved(World world, int x, int y, int z, int data) {
        world.getTileEntity(x,y,z).dropContents(world,x,y,z);
        super.onBlockRemoved(world, x, y, z, data);
    }

    @Override
    public boolean onBlockRightClicked(World world, int i, int j, int k, Player entityplayer, Side side, double xHit, double yHit) {
        if (world.isClientSide) {
            return true;
        }

        if (super.onBlockRightClicked(world, i, j, k, entityplayer, side, xHit, yHit)) {
            return true;
        }

        TileEntity tile = world.getTileEntity(i, j, k);
        if (tile != null && guiId != null) {
            if(tile instanceof IMultiblock){
                if (((IMultiblock) tile).getMultiblock() != null && ((IMultiblock) tile).getMultiblock().isValid()) {
                    Catalyst.displayGui(entityplayer, tile, key("gui/"+guiId));
                    entityplayer.triggerAchievement(SIAchievements.HORIZONS);
                    return true;
                } else {
                    entityplayer.sendTranslatedChatMessage("event.signalindustries.invalidMultiblock");
                    return true;
                }
            }
            Catalyst.displayGui(entityplayer, tile, key("gui/"+guiId));
        } else {
            return false;
        }
        return true;
    }

    public BlockLogicMachine setSolid(boolean solid) {
        this.solid = solid;
        return this;
    }

    public boolean isSolid() {
        return solid;
    }

    @Override
    public boolean isSolidRender() {
        return solid;
    }
}
