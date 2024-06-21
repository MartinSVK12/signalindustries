package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.gui.GuiSwitchCoverConfig;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.inventories.base.TileEntityCoverable;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineBase;
import sunsetsatellite.signalindustries.inventories.base.TileEntityTieredMachineSimple;
import sunsetsatellite.signalindustries.inventories.base.TileEntityWithName;
import sunsetsatellite.signalindustries.items.covers.ItemCover;

public class SwitchCover extends CoverBase {

    protected final IconCoordinate on = TextureRegistry.getTexture("signalindustries:block/switch_cover_on");
    protected final IconCoordinate off = TextureRegistry.getTexture("signalindustries:block/switch_cover_off");

    @Override
    public void openConfiguration(EntityPlayer player) {
        if(machine instanceof IInventory && machine instanceof TileEntity){
            TileEntity tile = (TileEntity) machine;
            IInventory inv = (IInventory) machine;
            SignalIndustries.displayGui(player, ()->new GuiSwitchCoverConfig(player, tile,this),null,inv,tile.x,tile.y,tile.z);
        } else if (machine instanceof TileEntityWithName) {
            TileEntityWithName tile = (TileEntityWithName) machine;
            SignalIndustries.displayGui(player, ()->new GuiSwitchCoverConfig(player, tile,this),tile,tile.x,tile.y,tile.z);
        }
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
    public IconCoordinate getTexture() {
        if(machine instanceof TileEntityTieredMachineBase){
            return ((TileEntityTieredMachineBase) machine).disabled ? off : on;
        }

        return on;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.switchCover;
    }

    @Override
    public void onInstalled(Direction dir, IAcceptsCovers machine, EntityPlayer player) {
        player.sendMessage("Cover installed!");
        super.onInstalled(dir, machine, player);
    }

    @Override
    public void onRemoved(EntityPlayer player) {
        if(machine instanceof TileEntityTieredMachineBase){
            ((TileEntityTieredMachineBase) machine).disabled = false;
        }
        player.sendMessage("Cover removed!");
        super.onRemoved(player);
    }
}
