package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.render.stitcher.IconCoordinate;
import net.minecraft.client.render.stitcher.TextureRegistry;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.player.inventory.IInventory;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.gui.GuiSwitchCoverConfig;
import sunsetsatellite.signalindustries.gui.GuiVoidCoverConfig;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.inventories.base.TileEntityWithName;
import sunsetsatellite.signalindustries.items.covers.ItemCover;

public class VoidCover extends CoverBase {

    protected final IconCoordinate texture = TextureRegistry.getTexture("signalindustries:block/void_cover");

    public int voidingItemSlot = -1;
    public int voidingFluidSlot = -1;
    public boolean active = false;

    @Override
    public void openConfiguration(EntityPlayer player) {
        if(machine instanceof IInventory && machine instanceof TileEntity){
            TileEntity tile = (TileEntity) machine;
            IInventory inv = (IInventory) machine;
            SignalIndustries.displayGui(player, ()->new GuiVoidCoverConfig(player, tile,this),null,inv,tile.x,tile.y,tile.z);
        } else if (machine instanceof TileEntityWithName) {
            TileEntityWithName tile = (TileEntityWithName) machine;
            SignalIndustries.displayGui(player, ()->new GuiVoidCoverConfig(player, tile,this),tile,tile.x,tile.y,tile.z);
        }
    }

    @Override
    public void writeToNbt(CompoundTag tag) {
        super.writeToNbt(tag);
        tag.putInt("VoidingItemSlot",voidingItemSlot);
        tag.putInt("VoidingFluidSlot",voidingFluidSlot);
        tag.putBoolean("Active",active);
    }

    @Override
    public void readFromNbt(CompoundTag tag) {
        super.readFromNbt(tag);
        voidingFluidSlot = tag.getIntegerOrDefault("VoidingItemSlot",-1);
        voidingFluidSlot = tag.getIntegerOrDefault("VoidingFluidSlot",-1);
        active = tag.getBooleanOrDefault("Active",false);
    }

    @Override
    public void tick() {
        if(active){
            if(!(voidingItemSlot == -1)){
                if(voidingItemSlot == -2){
                    IInventory inv = (IInventory) machine;
                    for (int i = 0; i < inv.getSizeInventory(); i++) {
                        inv.setInventorySlotContents(i,null);
                    }
                } else {
                    IInventory inv = (IInventory) machine;
                    inv.setInventorySlotContents(voidingItemSlot,null);
                }
            }
            if(!(voidingFluidSlot == -1)){
                if(voidingFluidSlot == -2){
                    IFluidInventory inv = (IFluidInventory) machine;
                    for (int i = 0; i < inv.getFluidInventorySize(); i++) {
                        inv.setFluidInSlot(i,null);
                    }
                } else {
                    IFluidInventory inv = (IFluidInventory) machine;
                    inv.setFluidInSlot(voidingItemSlot,null);
                }
            }
        }
    }

    @Override
    public IconCoordinate getTexture() {
        return texture;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.voidCover;
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
