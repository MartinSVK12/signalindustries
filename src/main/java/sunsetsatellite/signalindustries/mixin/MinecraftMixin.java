package sunsetsatellite.signalindustries.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.PlayerLocal;
import net.minecraft.client.player.controller.PlayerController;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.util.phys.HitResult;
import net.minecraft.core.world.World;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.catalyst.core.util.vector.Vec2f;
import sunsetsatellite.catalyst.core.util.vector.Vec3f;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.KeyboardHandler;

@Mixin(
        value = Minecraft.class,
        remap = false
)
public abstract class MinecraftMixin {


    @Shadow public PlayerLocal thePlayer;

    @Shadow public HitResult objectMouseOver;

    @Shadow protected abstract void clickMouse(int par1, boolean par2, boolean par3);

    @Shadow public abstract void resize();

    @Inject(
            method = "runTick",
            at = @At(value = "INVOKE",target = "Lorg/lwjgl/input/Keyboard;next()Z",shift = At.Shift.AFTER)
    )
    public void handleKeyboard(CallbackInfo ci){
        KeyboardHandler.handleKeyboard((Minecraft) (Object)this, ci);
        /*SignalIndustries.LOGGER.info(String.format("Shift: %s | Control: %S",shift,control));
        SignalIndustries.LOGGER.info(String.format("Key: %s | Char: %s | State: %s",Keyboard.getEventKey(),Keyboard.getKeyName(Keyboard.getEventKey()),Keyboard.getEventKeyState()));*/

        //0-82 1-79 2-80 3-81 4-75 5-76 6-77 7-71 8-72 9-73

        /*SignalIndustries.LOGGER.info(Keyboard.getKeyName(key));
        SignalIndustries.LOGGER.info(String.valueOf(key));*/
    }

    @ModifyExpressionValue(method = "runTick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/entity/player/PlayerLocal;noPhysics:Z"))
    public boolean modifyWingsFlightSpeed(boolean original){
        SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>)thePlayer).getPowerSuit();
        if(ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)) {
            return original || ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active");
        }
        return original;
    }

    @ModifyExpressionValue(method = "runTick", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/Minecraft;toggleFlyPressed:Z"))
    public boolean modifyWingsFlightSpeed2(boolean original){
        boolean control = Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL);
        SignalumPowerSuit ps = ((IPlayerPowerSuit<SignalumPowerSuit>)thePlayer).getPowerSuit();
        if(ps != null && ps.active && ps.hasAttachment(SIItems.crystalWings)) {
            return original || (ps.getAttachment(SIItems.crystalWings).getData().getBoolean("active") && control);
        }
        return original;
    }

    @WrapOperation(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/controller/PlayerController;useOrPlaceItemStackOnTile(Lnet/minecraft/core/entity/player/Player;Lnet/minecraft/core/world/World;Lnet/minecraft/core/item/ItemStack;IIILnet/minecraft/core/util/helper/Side;DD)Z"))
    private boolean fixUsagePosition(PlayerController instance, Player player, World world, ItemStack itemstack, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced, Operation<Boolean> original){
        HitResult hit = objectMouseOver;
        if (hit.hitType == HitResult.HitType.TILE) {
            Vec3f vec3f = new Vec3f(hit.location.x,hit.location.y,hit.location.z);
            Vec2f clickPosition = vec3f.subtract(vec3f.copy().floor()).abs().set(hit.side.getAxis(),0).toVec2f();
            switch (hit.side) {
                case NORTH:
                    clickPosition.x = 1 - clickPosition.x;
                    break;
                case EAST: {
                    double temp1 = clickPosition.y;
                    double temp2 = clickPosition.x;
                    clickPosition.x = 1 - temp1;
                    clickPosition.y = temp2;
                    break;
                }
                case SOUTH:
                    //no change needed
                    break;
                case WEST: {
                    double temp1 = clickPosition.y;
                    double temp2 = clickPosition.x;
                    clickPosition.x = temp1;
                    clickPosition.y = temp2;
                    break;
                }
            }
            return original.call(instance, player, world, itemstack, blockX, blockY, blockZ, side, clickPosition.x, clickPosition.y);
        }
        return original.call(instance, player, world, itemstack, blockX, blockY, blockZ, side, xPlaced, yPlaced);
    }
}
