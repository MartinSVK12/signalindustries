package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.gui.guidebook.GuiGuidebook;
import net.minecraft.client.gui.guidebook.PageManager;
import net.minecraft.client.gui.guidebook.search.SearchPage;
import net.minecraft.core.data.registry.recipe.SearchQuery;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sunsetsatellite.signalindustries.SignalIndustries;

@Debug(
        export = true
)
@Mixin(
        value = SearchPage.class,
        remap = false
)
public class SearchPageMixin {
    @Inject(method = "keyTyped",at = @At("HEAD"))
    public void keyTyped(char c, int key, int x, int y, int mouseX, int mouseY, CallbackInfo ci) {
        SignalIndustries.LOGGER.info(String.format("char: %c | key: %d | x: %d | y: %d | mx: %d | my: %d",c,key,x,y,mouseX,mouseY));
    }
}
