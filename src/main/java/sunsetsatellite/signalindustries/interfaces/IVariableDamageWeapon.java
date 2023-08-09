package sunsetsatellite.signalindustries.interfaces;


import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;

public interface IVariableDamageWeapon {

    int getDamageVsEntity(Entity entity, ItemStack stack);
}
