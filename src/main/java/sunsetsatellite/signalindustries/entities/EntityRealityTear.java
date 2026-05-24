package sunsetsatellite.signalindustries.entities;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.catalyst.core.util.vector.Vec3i;
import sunsetsatellite.signalindustries.SIDimensions;
import sunsetsatellite.signalindustries.interfaces.mixins.IWarpPlayer;

import java.util.List;

public class EntityRealityTear extends Entity {

    public ItemStack orb;

	public EntityRealityTear(@Nullable World world) {
		super(world);
	}

    public EntityRealityTear(@Nullable World world, Vec3i start, ItemStack stack) {
        super(world);
        orb = stack;
        setSize(5, 5);
        setPos(start.x, start.y, start.z);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount >= 40) {
            remove();
        }
        if (world == null || orb == null) return;
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(this, bb);
        for (Entity entity : entities) {
            if (entity instanceof Player) {
                Player player = ((Player) entity);
                CompoundTag data = orb.getData();
                CompoundTag warpPosition = data.getCompound("position");
                if (warpPosition.containsKey("x") && warpPosition.containsKey("y") && warpPosition.containsKey("z")) {
                    if (data.getInteger("dim") != world.dimension.id) {
                        //player.sendMessage("Teleported to dim "+data.getInteger("dim"));
                        ((IWarpPlayer) player).warp(data.getInteger("dim"));
                    }
                    //player.sendMessage("Teleported to "+warpPosition.getInteger("x")+", "+warpPosition.getInteger("y")+", "+warpPosition.getInteger("z"));
                    player.setPos(warpPosition.getInteger("x"), warpPosition.getInteger("y"), warpPosition.getInteger("z"));
                } else {
                    //player.sendMessage("No position data found!");
                    ((IWarpPlayer) player).warp(SIDimensions.ETERNITY.id);
                }
            }
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {

    }
}
