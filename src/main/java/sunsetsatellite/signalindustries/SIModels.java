package sunsetsatellite.signalindustries;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.EntityRenderDispatcher;
import net.minecraft.client.render.TextureManager;
import net.minecraft.client.render.TileEntityRenderDispatcher;
import net.minecraft.client.render.block.color.BlockColorDispatcher;
import net.minecraft.client.render.block.model.BlockModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.item.model.ItemModelStandard;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.ModelHelper;
import turniplabs.halplibe.util.ModelEntrypoint;

import static sunsetsatellite.signalindustries.SignalIndustriesClient.LOGGER;
import static sunsetsatellite.signalindustries.SignalIndustriesClient.MOD_ID;

@Environment(EnvType.CLIENT)
public class SIModels implements ModelEntrypoint {

    @Override
    public void initBlockModels(BlockModelDispatcher dispatcher) {
        LOGGER.info("Initializing block models...");
    }

    @Override
    public void initItemModels(ItemModelDispatcher dispatcher) {
        LOGGER.info("Initializing item models...");
        SIItems.itemTextures.forEach((item,texture)->{
            ModelHelper.setItemModel(item,()->{
                ItemModelStandard model = new ItemModelStandard(item, null);
                model.icon = TextureRegistry.getTexture(NamespaceID.getTemp(SignalIndustries.MOD_ID,"item/"+texture));
                return model;
            });
        });
    }

    @Override
    public void initEntityModels(EntityRenderDispatcher dispatcher) {
        LOGGER.info("Initializing entity models...");
    }

    @Override
    public void initTileEntityModels(TileEntityRenderDispatcher dispatcher) {
        LOGGER.info("Initializing tile entity renderers...");
    }

    @Override
    public void initBlockColors(BlockColorDispatcher dispatcher) {

    }
}
