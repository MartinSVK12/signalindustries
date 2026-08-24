package sunsetsatellite.signalindustries.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sunsetsatellite.signalindustries.SignalIndustriesClient;
import turniplabs.halplibe.helper.EnvironmentHelper;

public class ItemDimensionMaker extends Item {
    public ItemDimensionMaker(String translationKey, String namespaceId, int id) {
        super(translationKey, namespaceId, id);
    }

	@Environment(EnvType.CLIENT)
	@Override
	public @Nullable ItemStack onUse(@NotNull ItemStack selfStack, @NotNull World world, @NotNull Player player) {
		/*if(EnvironmentHelper.isSingleplayerClient()){
			return SignalIndustriesClient.createTestDimension();
		}*/

		return selfStack; //warpOrb;
	}
}
