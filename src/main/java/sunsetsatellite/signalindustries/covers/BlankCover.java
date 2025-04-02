package sunsetsatellite.signalindustries.covers;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.entity.player.Player;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.items.covers.ItemCover;

public class BlankCover extends CoverBase {

    protected final String texture = "signalindustries:block/blank_cover";

    @Override
    public void openConfiguration(Player player, Direction dir) {

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

    }

    @Override
    public String getTexture() {
        return texture;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.blankCover;
    }

    @Override
    public void onInstalled(Direction dir, IAcceptsCovers machine, Player player) {
        player.sendMessage("Cover installed!");
        super.onInstalled(dir, machine, player);
    }

    @Override
    public void onRemoved(Player player) {
        player.sendMessage("Cover removed!");
        super.onRemoved(player);
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {

    }
}
