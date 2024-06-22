package sunsetsatellite.signalindustries.items;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.signalindustries.interfaces.IAcceptsPosition;
import sunsetsatellite.signalindustries.interfaces.mixins.INBTCompound;

public class ItemPositionChip extends Item implements ICustomDescription {
    public ItemPositionChip(String name, int id) {
        super(name, id);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(entityplayer.isSneaking()){
            ((INBTCompound)itemstack.getData()).removeTag("position");
            Minecraft.getMinecraft(this).ingameGUI.addChatMessage("Position cleared!");
        }
        return super.onUseItem(itemstack, world, entityplayer);
    }

    @Override
    public boolean onUseItemOnBlock(ItemStack itemStack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        TileEntity tile = world.getBlockTileEntity(blockX,blockY,blockZ);
        if(tile instanceof IAcceptsPosition){
            if(itemStack.getData().containsKey("position")){
                CompoundTag position = itemStack.getData().getCompound("position");
                if(position.containsKey("x") && position.containsKey("y") && position.containsKey("z") && position.containsKey("side") && position.containsKey("dim")){
                    ((IAcceptsPosition) tile).receivePosition(position.getInteger("x"),position.getInteger("y"),position.getInteger("z"), Side.getSideById(position.getInteger("side")),position.getInteger("dim"));
                    return true;
                }
            }
        }
        CompoundTag position = new CompoundTag();
        position.putInt("x",blockX);
        position.putInt("y",blockY);
        position.putInt("z",blockZ);
        position.putInt("dim",world.dimension.id);
        position.putInt("side", side.getId());
        itemStack.getData().put("position",position);
        Minecraft.getMinecraft(this).ingameGUI.addChatMessage(String.format("Position set to XYZ: %d, %d, %d!",blockX,blockY,blockZ));
        return true;
    }

    @Override
    public String getDescription(ItemStack stack) {
        CompoundTag position = stack.getData().getCompound("position");
        if(position.containsKey("x") && position.containsKey("y") && position.containsKey("z") && position.containsKey("dim") && position.containsKey("side")){
            return String.format("XYZ: %s%d, %s%d, %s%d%s | Side: %s%s%s | Dim: %s%d%s",TextFormatting.RED,position.getInteger("x"),TextFormatting.LIME,position.getInteger("y"),TextFormatting.BLUE,position.getInteger("z"),TextFormatting.WHITE,TextFormatting.YELLOW,Side.getSideById(position.getInteger("side")),TextFormatting.WHITE,TextFormatting.MAGENTA,position.getInteger("dim"),TextFormatting.WHITE);
        }
        return TextFormatting.GRAY+"No position stored."+TextFormatting.WHITE;
    }
}
