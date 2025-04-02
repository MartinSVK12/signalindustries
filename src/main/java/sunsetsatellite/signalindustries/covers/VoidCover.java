package sunsetsatellite.signalindustries.covers;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.popup.PopupBuilder;
import net.minecraft.client.gui.popup.PopupScreen;
import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.container.Container;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.fluids.api.IFluidInventory;
import sunsetsatellite.catalyst.fluids.api.IFluidTransfer;
import sunsetsatellite.catalyst.fluids.impl.tile.TileEntityFluidItemContainer;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IAcceptsCovers;
import sunsetsatellite.signalindustries.items.covers.ItemCover;
import sunsetsatellite.signalindustries.tiles.base.TileEntityWithName;

public class VoidCover extends CoverBase {

    protected final String texture = "signalindustries:block/void_cover";

    public int voidingItemSlot = -1;
    public int voidingFluidSlot = -1;
    public boolean active = false;

    @Override
    public void openConfiguration(Player player, Direction dir) {
        if(machine instanceof Container && machine instanceof TileEntity){
            TileEntity tile = (TileEntity) machine;
            Container inv = (Container) machine;
            Catalyst.displayGui(player, tile, SignalIndustries.key("gui/void_cover"), Catalyst.compoundOf(new String[]{"side"}, dir.ordinal()));
        } else if (machine instanceof TileEntityWithName) {
            TileEntityWithName tile = (TileEntityWithName) machine;
            Catalyst.displayGui(player, tile, SignalIndustries.key("gui/void_cover"), Catalyst.compoundOf(new String[]{"side"}, dir.ordinal()));
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
                    Container inv = (Container) machine;
                    for (int i = 0; i < inv.getContainerSize(); i++) {
                        inv.setItem(i,null);
                    }
                } else {
                    Container inv = (Container) machine;
                    inv.setItem(voidingItemSlot,null);
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
    public String getTexture() {
        return texture;
    }

    @Override
    public ItemCover getItem() {
        return SIItems.voidCover;
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
        if(machine instanceof TileEntityFluidItemContainer){
            TileEntityFluidItemContainer tile = (TileEntityFluidItemContainer) machine;
            if(button == 0){
                switch (id){
                    case 0: {
                        active = !active;
                        break;
                    }
                    case 1: {
                        int max = tile.getContainerSize()-1;
                        if(voidingItemSlot < max){
                            voidingItemSlot++;
                        }
                        break;
                    }
                    case 2: {
                        int max = tile.getFluidInventorySize()-1;
                        if(voidingFluidSlot < max){
                            voidingFluidSlot++;
                        }
                        break;
                    }
                }
            } else if (button == 1) {
                switch (id){
                    case 1: {
                        if(voidingItemSlot > -2){
                            voidingItemSlot--;
                            if(voidingItemSlot == -2){
                                active = false;
                            }
                        }
                        break;
                    }
                    case 2: {
                        if(voidingFluidSlot > -2){
                            voidingFluidSlot--;
                            if(voidingFluidSlot == -2){
                                active = false;
                            }
                        }
                        break;
                    }
                }
            }
        }

    }
}
