package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.items.covers.ItemCover;
import sunsetsatellite.signalindustries.tiles.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.tiles.base.TileEntityWithName;

public class SwitchCover extends CoverBase {

    protected final String on = "signalindustries:block/switch_cover_on";
    protected final String off = "signalindustries:block/switch_cover_off";

    public boolean controlledByRedstone = false;

    @Override
    public void openConfiguration(Player player, Direction dir) {
        if (machine instanceof Container && machine instanceof TileEntity tile) {
			Catalyst.displayGui(player, tile, SignalIndustries.key("gui/switch_cover"), Catalyst.compoundOf(new String[]{"side"}, dir.ordinal()));
        } else if (machine instanceof TileEntityWithName tile) {
			Catalyst.displayGui(player, tile, SignalIndustries.key("gui/switch_cover"), Catalyst.compoundOf(new String[]{"side"}, dir.ordinal()));
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        super.writeToNbt(tag);
        tag.putBoolean("RedstoneControl", controlledByRedstone);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        super.readFromNbt(tag);
        controlledByRedstone = tag.getBoolean("RedstoneControl");
    }

    @Override
    public void tick() {
        if (machine instanceof TileEntityTieredMachineBase && controlledByRedstone && machine.hasCoverAnywhere(RedstoneCover.class)) {
            RedstoneCover cover = machine.getCover(RedstoneCover.class);
            ((TileEntityTieredMachineBase) machine).disabled = cover.sensorActive;
        }
    }

    @Override
    public String getTexture() {
        if (machine instanceof TileEntityTieredMachineBase) {
            return ((TileEntityTieredMachineBase) machine).disabled ? off : on;
        }

        return on;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.switchCover;
    }

    @Override
    public void onInstalled(Direction dir, IAcceptsCovers machine, Player player) {
        player.sendMessage("Cover installed!");
        super.onInstalled(dir, machine, player);
    }

    @Override
    public void onRemoved(Player player) {
        if (machine instanceof TileEntityTieredMachineBase) {
            ((TileEntityTieredMachineBase) machine).disabled = false;
        }
        player.sendMessage("Cover removed!");
        super.onRemoved(player);
    }

    @Override
    public void buttonClicked(int id, int button, int channel) {
        switch (id) {
            case 0:
                if (machine instanceof TileEntityTieredMachineBase) {
                    ((TileEntityTieredMachineBase) machine).disabled = !((TileEntityTieredMachineBase) machine).disabled;
                }
                break;
            case 1:
                controlledByRedstone = !controlledByRedstone;
                break;
        }

    }
}
