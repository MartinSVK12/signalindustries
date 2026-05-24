package sunsetsatellite.signalindustries.items;


import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import net.minecraft.core.world.pos.TilePosc;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import sunsetsatellite.catalyst.core.util.ICustomDescription;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.interfaces.IAcceptsPosition;

public class ItemPositionChip extends Item implements ICustomDescription {

    public ItemPositionChip(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public ItemStack onUse(@NonNull ItemStack itemstack, @NonNull World world, Player entityplayer) {
        if (entityplayer.isSneaking()) {
            itemstack.getData().getValue().remove("position");
            entityplayer.sendMessage("Position cleared!");
        }
        return super.onUse(itemstack, world, entityplayer);
    }

	@Override
	public boolean onUseOnBlock(@NotNull ItemStack stack, @NotNull World world, @Nullable Player player, @NotNull TilePosc blockPos, @NotNull Side side, double xHit, double yHit) {
		TileEntity tile = world.getTileEntity(blockPos);
		if (tile instanceof IAcceptsPosition) {
			if (stack.getData().containsKey("position")) {
				CompoundTag position = stack.getData().getCompound("position");
				if (position.containsKey("x") && position.containsKey("y") && position.containsKey("z") && position.containsKey("side") && position.containsKey("dim")) {
					((IAcceptsPosition) tile).receivePosition(position.getInteger("x"), position.getInteger("y"), position.getInteger("z"), Side.fromId(position.getInteger("side")), position.getInteger("dim"));
					return true;
				}
			}
		}
		CompoundTag position = new CompoundTag();
		position.putInt("x", blockPos.x());
		position.putInt("y", blockPos.y());
		position.putInt("z", blockPos.z());
		position.putInt("dim", world.dimension.id);
		position.putInt("side", side.id);
		stack.getData().put("position", position);
		player.sendMessage(String.format("Position set to XYZ: %d, %d, %d!", blockPos.x(), blockPos.y(), blockPos.z()));
		return true;
	}

    public Vec3i getPosition(ItemStack stack){
        if (stack.getData().containsKey("position")) {
            CompoundTag position = stack.getData().getCompound("position");
            return new Vec3i(position.getInteger("x"), position.getInteger("y"), position.getInteger("z"));
        }
        return null;
    }

    @Override
    public String getDescription(ItemStack stack) {
        CompoundTag position = stack.getData().getCompound("position");
        if (position.containsKey("x") && position.containsKey("y") && position.containsKey("z") && position.containsKey("dim") && position.containsKey("side")) {
            return String.format("XYZ: %s%d, %s%d, %s%d%s | Side: %s%s%s | Dim: %s%d%s", TextFormatting.RED, position.getInteger("x"), TextFormatting.LIME, position.getInteger("y"), TextFormatting.BLUE, position.getInteger("z"), TextFormatting.WHITE, TextFormatting.YELLOW, Side.fromId(position.getInteger("side")), TextFormatting.WHITE, TextFormatting.MAGENTA, position.getInteger("dim"), TextFormatting.WHITE);
        }
        return TextFormatting.GRAY + "No position stored." + TextFormatting.WHITE;
    }
}
