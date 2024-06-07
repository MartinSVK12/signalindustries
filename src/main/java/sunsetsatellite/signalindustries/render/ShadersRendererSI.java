package sunsetsatellite.signalindustries.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.shader.ShaderProvider;
import net.minecraft.client.render.shader.ShaderProviderInternal;
import net.minecraft.client.render.shader.ShadersRenderer;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

public class ShadersRendererSI extends ShadersRenderer {

    public String currentShaderDir;
    private final SignalumPowerSuit powerSuit;

    public ShadersRendererSI(Minecraft minecraft, String dir) {
        super(minecraft);
        this.currentShaderDir = dir;
        this.powerSuit = null;
    }

    public ShadersRendererSI(Minecraft minecraft, String dir, SignalumPowerSuit powerSuit) {
        super(minecraft);
        this.currentShaderDir = dir;
        this.powerSuit = powerSuit;
    }

    @Override
    public void beginRenderGame(float partialTicks) {
        if(powerSuit != null){
            if(!powerSuit.hasAttachment((ItemAttachment) SIItems.nightVisionLens) || !powerSuit.active){
                mc.setRenderer(new ShadersRenderer(mc));
                mc.renderer.reload();
                mc.fullbright = false;
                mc.renderGlobal.loadRenderers();
                return;
            }
        }
        super.beginRenderGame(partialTicks);
    }

    @Override
    public ShaderProvider getShader() {
        return new ShaderProviderInternal("/assets/signalindustries/shaders/"+currentShaderDir);
    }
}
