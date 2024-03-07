package sunsetsatellite.signalindustries.items;

import net.minecraft.core.entity.Entity;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ToolMaterial;
import net.minecraft.core.item.tool.ItemToolSword;
import sunsetsatellite.signalindustries.interfaces.ITiered;
import sunsetsatellite.signalindustries.interfaces.IVariableDamageWeapon;
import sunsetsatellite.signalindustries.util.Tier;

public class ItemSunriseDawn extends ItemToolSword implements ITiered, IVariableDamageWeapon {
    public Tier tier;

    public ItemSunriseDawn(String name, int id, ToolMaterial material, Tier tier) {
        super(name, id, material);
        this.tier = tier;
    }

    @Override
    public String getDescription(ItemStack stack) {
        return "Tier: " + tier.getTextColor() + tier.getRank();
    }

    @Override
    public Tier getTier() {
        return tier;
    }

    @Override
    public int getDamageVsEntity(Entity entity, ItemStack stack) {
        /*for (Entity entity1 : new ArrayList<>(Minecraft.getMinecraft(this).theWorld.loadedEntityList)) {
            if (entity1 instanceof EntityLiving) {
                if (entity1.distanceTo(Minecraft.getMinecraft(this).thePlayer) < 10 && entity1 != Minecraft.getMinecraft(this).thePlayer) {
                    entity1.hurt(entity1, 999, DamageType.GENERIC);
                }
            }
        }*/
        return 0;
    }
}
