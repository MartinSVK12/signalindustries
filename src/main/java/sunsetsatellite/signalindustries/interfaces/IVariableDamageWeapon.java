package sunsetsatellite.signalindustries.interfaces;

import net.minecraft.src.Entity;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public interface IVariableDamageWeapon {

    int getDamageVsEntity(Entity entity, ItemStack stack);
}
