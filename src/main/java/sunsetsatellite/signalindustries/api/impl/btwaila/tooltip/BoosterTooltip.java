package sunsetsatellite.signalindustries.api.impl.btwaila.tooltip;

import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import sunsetsatellite.signalindustries.tiles.machines.TileEntityBooster;
import sunsetsatellite.signalindustries.util.Tier;
import toufoumaster.btwaila.gui.components.AdvancedInfoComponent;
import toufoumaster.btwaila.util.ProgressBarOptions;
import toufoumaster.btwaila.util.TextureOptions;

public class BoosterTooltip extends SIBaseTooltip<TileEntityBooster> {
    @Override
    public void initTooltip() {
        addClass(TileEntityBooster.class);
    }

    @Override
    public void drawAdvancedTooltip(TileEntityBooster tile, AdvancedInfoComponent c) {
        ProgressBarOptions options = new ProgressBarOptions()
                .setBackgroundOptions(new TextureOptions(0xFFFFFF, TextureRegistry.getTexture("signalindustries:block/reality_fabric")))
                .setText("Fuel: ");
        if(tile.tier == Tier.BASIC){
            options.setForegroundOptions(new TextureOptions(0xFFFFFF, TextureRegistry.getTexture("minecraft:block/block_redstone")));
        } else {
            options.setForegroundOptions(new TextureOptions(0xFFFFFF, TextureRegistry.getTexture("signalindustries:block/dilithium_crystal_block")));
        }
        c.drawProgressBarTextureWithText(tile.progressTicks,tile.progressMaxTicks,options,0);
        drawFluids(tile,c,false);
        c.drawInventory(tile, 0);
    }

}
