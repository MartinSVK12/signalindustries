package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.items.covers.ItemCover;

public class RedstoneCover extends CoverBase {

    protected final IconCoordinate on = TextureRegistry.getTexture("signalindustries:block/redstone_cover_on");
    protected final IconCoordinate off = TextureRegistry.getTexture("signalindustries:block/redstone_cover_off");

    @Override
    public void openConfiguration(EntityPlayer player) {

    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        super.writeToNbt(tag);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        super.readFromNbt(tag);
    }

    @Override
    public void tick() {
        //worldObj.notifyBlocksOfNeighborChange(x,y,z,(insert condition here) ? 15 : 0); //redstone activation
    }

    @Override
    public IconCoordinate getTexture() {
        return off;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.redstoneCover;
    }

    @Override
    public void onInstalled(Direction dir, IAcceptsCovers machine, EntityPlayer player) {
        player.sendMessage("Cover installed!");
        super.onInstalled(dir, machine, player);
    }

    @Override
    public void onRemoved(EntityPlayer player) {
        player.sendMessage("Cover removed!");
        super.onRemoved(player);
    }
}
