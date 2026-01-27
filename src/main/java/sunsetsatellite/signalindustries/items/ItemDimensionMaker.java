package sunsetsatellite.signalindustries.items;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.render.worldtype.WorldTypeFXDispatcher;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.core.world.type.WorldTypes;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.dim.custom.CustomDimensionData;
import sunsetsatellite.signalindustries.dim.custom.DimensionCustom;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class ItemDimensionMaker extends Item {
    public ItemDimensionMaker(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

    @Override
    public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
        if(EnvironmentHelper.isSinglePlayer()){
            int customDimId = 100;
            ItemStack warpOrb = new ItemStack(SIItems.warpOrb);
            CompoundTag pos = new CompoundTag();
            pos.putInt("x", 0);
            pos.putInt("y", 100);
            pos.putInt("z", 0);
            warpOrb.getData().put("position", pos);
            warpOrb.getData().putInt("dim", customDimId);
            if (Dimension.getDimensionList().containsKey(customDimId)) {
                return warpOrb;
            }
            CustomDimensionData data = new CustomDimensionData("custom", customDimId);
            data.properties.test();
            WorldTypes.register(SignalIndustries.key("custom/custom"), data.getWorldType());
            WorldTypeFXDispatcher.getInstance().addDispatch(data.getWorldType(), data.properties.worldTypeFX);
            DimensionCustom dim = new DimensionCustom(data);
            Dimension.registerDimension(customDimId, dim);
        }

        return itemstack; //warpOrb;
    }
}
