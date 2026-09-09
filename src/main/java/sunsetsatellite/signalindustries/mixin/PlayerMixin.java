package sunsetsatellite.signalindustries.mixin;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityItem;
import net.minecraft.core.entity.Mob;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.player.inventory.container.ContainerInventory;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.primitives.AABBd;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.items.base.ItemToolTiered;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

import java.util.List;

@Mixin(value = Player.class, remap = false)
public abstract class PlayerMixin extends Mob implements IPlayerPowerSuit<SignalumPowerSuit> {
	@Shadow
	@Final
	@NotNull
	public ContainerInventory inventory;

	@Shadow
	protected abstract void collideWithPlayer(Entity entity);

	private PlayerMixin(@NotNull World world) {
		super(world);
	}

	@Override
	public SignalumPowerSuit getPowerSuit() {
		return null;
	}

	@Override
	public CompoundTag getPowerSuitData() {
		return null;
	}

	@Inject(method = "getAcidMeltDamage", at = @At("HEAD"), cancellable = true)
	private void getAcidMeltDamage(ItemStack stack, int acidIntensity, CallbackInfoReturnable<Integer> cir) {
		if(stack != null && stack.getItem() instanceof ITiered){
			cir.setReturnValue(0);
		}
	}

	@Inject(method = "onLivingUpdate", at = @At("HEAD"))
	public void magnet(CallbackInfo ci){
		if(getHealth() > 0){
			for (ItemStack stack : inventory.mainInventory) {
				if(stack == null) continue;
				if(stack.getItem().equals(SIItems.magnet)){
					if(stack.getData().getBoolean("active")){
						List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, MathHelper.aabbGrow(this.bb, 8.0D, 0.5D, 8.0D, new AABBd()));
						for (Entity entity : list) {
							if (!entity.removed && entity instanceof EntityItem) {
								collideWithPlayer(entity);
							}
						}
					}
				}
			}
		}
	}
}
