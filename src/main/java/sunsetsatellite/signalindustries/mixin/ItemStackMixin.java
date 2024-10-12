package sunsetsatellite.signalindustries.mixin;


import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sunsetsatellite.catalyst.core.util.Vec3i;
import sunsetsatellite.signalindustries.commands.StructMakerCommand;
import sunsetsatellite.signalindustries.interfaces.IVariableDamageWeapon;

@Mixin(
        value = ItemStack.class,
        remap = false
)
public class ItemStackMixin {

    @Shadow public int itemID;


    @Inject(
            method = "getDamageVsEntity",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getDamageVsEntity(Entity entity, CallbackInfoReturnable<Integer> cir) {
        if(Item.itemsList[this.itemID] instanceof IVariableDamageWeapon){
            cir.setReturnValue((((IVariableDamageWeapon) Item.itemsList[this.itemID]).getDamageVsEntity(entity,(ItemStack) ((Object)this))));
        }
    }
    
    @Inject(method = "useItem", at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/core/entity/player/EntityPlayer;addStat(Lnet/minecraft/core/achievement/stat/Stat;I)V"))
    public void useItem(EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir){
        if(StructMakerCommand.autoAddRemove){
            Vec3i posVec = new Vec3i(blockX + side.getOffsetX(), blockY + side.getOffsetY(), blockZ + side.getOffsetZ());
            StructMakerCommand.internalAddBlock(Minecraft.getMinecraft(Minecraft.class),posVec);
        }
    }

}
