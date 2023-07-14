package sunsetsatellite.signalindustries.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;

import java.util.Random;

@Debug(
        export = true
)
@Mixin(
        value = GuiIngame.class,
        remap = false
)
public abstract class GuiIngameMixin extends Gui {

    @Shadow private FontRenderer fontrenderer;

    @Shadow private Minecraft mc;

    @Shadow private Random rand;

    @Shadow private int updateCounter;

    @Shadow protected abstract void renderInventorySlot(int itemIndex, int x, int y, float delta, float alpha);

    @Shadow private static RenderItem itemRenderer;

    @Inject(
            method = "renderGameOverlay",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/src/EntityRenderer;setupScaledResolution()V", shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderAfterGameOverlay(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci, StringTranslate stringtranslate, int width, int height, int sp, FontRenderer fontrenderer) {
        ItemStack headSlotItem = this.mc.thePlayer.inventory.armorItemInSlot(3);
        if(headSlotItem != null){
            if(headSlotItem.getItem().itemID == SignalIndustries.signalumPrototypeHarnessGoggles.itemID){
                InventoryPlayer inv = this.mc.thePlayer.inventory;
                if(this.mc.thePlayer.inventory.getCurrentItem() != null) {
                    if(this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof IHasOverlay){
                        ((IHasOverlay)inv.getCurrentItem().getItem()).renderOverlay((GuiIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,fontrenderer, itemRenderer);
                    }
                }
                if (inv.armorItemInSlot(2) != null && inv.armorItemInSlot(2).getItem() instanceof IHasOverlay) {
                    ((IHasOverlay)inv.armorItemInSlot(2).getItem()).renderOverlay((GuiIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,fontrenderer, itemRenderer);
                }
            } else if (headSlotItem.getItem() instanceof IHasOverlay) {
                InventoryPlayer inv = this.mc.thePlayer.inventory;
                ((IHasOverlay)inv.armorItemInSlot(3).getItem()).renderOverlay((GuiIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,fontrenderer, itemRenderer);
                drawHotbar(partialTicks);
            }
        }
    }

    private void drawHotbar(float partialTicks){
        if (this.mc.isometricMode)
            return;
        GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/icons.png"));
        StringTranslate stringtranslate = StringTranslate.getInstance();
        int width = this.mc.resolution.scaledWidth;
        int height = this.mc.resolution.scaledHeight;
        int sp = (int)(this.mc.gameSettings.screenPadding.value.floatValue() * height / 8.0F);
        FontRenderer fontrenderer = this.mc.fontRenderer;
        this.mc.entityRenderer.setupScaledResolution();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        if (this.mc.gameSettings.immersiveMode.drawHotbar()) {
            boolean heartsFlash = (this.mc.thePlayer.heartsFlashTime / 3 % 2 == 1);
            if (this.mc.thePlayer.heartsFlashTime < 10)
                heartsFlash = false;
            int health = this.mc.thePlayer.health;
            int prevHealth = this.mc.thePlayer.prevHealth;
            this.rand.setSeed(this.updateCounter * 312871L);
            if (this.mc.playerController.shouldDrawHUD()) {
                int armorValue = this.mc.thePlayer.getPlayerProtectionAmount();
                for (int index = 0; index < 10; index++) {
                    int y = height - 32 - sp;
                    if (armorValue > 0) {
                        int x = width / 2 + 91 - index * 8 - 9;
                        if (index * 2 + 1 < armorValue)
                            drawTexturedModalRect(x, y, 34, 9, 9, 9);
                        if (index * 2 + 1 == armorValue)
                            drawTexturedModalRect(x, y, 25, 9, 9, 9);
                        if (index * 2 + 1 > armorValue)
                            drawTexturedModalRect(x, y, 16, 9, 9, 9);
                    }
                    if (!(this.mc.thePlayer.getGamemode()).isPlayerInvulnerable) {
                        int heartOffset = 0;
                        if (heartsFlash)
                            heartOffset = 1;
                        int x = width / 2 - 91 + index * 8;
                        if (health <= 4)
                            y += this.rand.nextInt(2);
                        drawTexturedModalRect(x, y, 16 + heartOffset * 9, 0, 9, 9);
                        if (heartsFlash) {
                            if (index * 2 + 1 < prevHealth)
                                drawTexturedModalRect(x, y, 70, 0, 9, 9);
                            if (index * 2 + 1 == prevHealth)
                                drawTexturedModalRect(x, y, 79, 0, 9, 9);
                        }
                        if (index * 2 + 1 < health)
                            drawTexturedModalRect(x, y, 52, 0, 9, 9);
                        if (index * 2 + 1 == health)
                            drawTexturedModalRect(x, y, 61, 0, 9, 9);
                        if (this.mc.thePlayer.inventory.getCurrentItem() != null && this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemFood && this.mc.gameSettings.foodHealthRegenOverlay.value.booleanValue()) {
                            int healing = ((ItemFood)this.mc.thePlayer.inventory.getCurrentItem().getItem()).getHealAmount();
                            if (index * 2 + 1 >= health)
                                if (index * 2 + 1 == health) {
                                    drawTexturedModalRect(x, y, 106, 0, 9, 9);
                                } else if (index * 2 + 1 < health + healing) {
                                    drawTexturedModalRect(x, y, 88, 0, 9, 9);
                                } else if (index * 2 + 1 == health + healing) {
                                    drawTexturedModalRect(x, y, 97, 0, 9, 9);
                                }
                        }
                    }
                }
                if (this.mc.thePlayer.isInsideOfMaterial(Material.water) && !(this.mc.thePlayer.getGamemode()).isPlayerInvulnerable) {
                    int k2 = (int)Math.ceil((this.mc.thePlayer.air - 2) * 10.0D / 300.0D);
                    int l3 = (int)Math.ceil(this.mc.thePlayer.air * 10.0D / 300.0D) - k2;
                    for (int l5 = 0; l5 < k2 + l3; l5++) {
                        if (l5 < k2) {
                            drawTexturedModalRect(width / 2 - 91 + l5 * 8, height - 32 - 9 - sp, 16, 18, 9, 9);
                        } else {
                            drawTexturedModalRect(width / 2 - 91 + l5 * 8, height - 32 - 9 - sp, 25, 18, 9, 9);
                        }
                    }
                }
                if (this.mc.thePlayer.fire > 0) {
                    int x = width / 2 - 91;
                    int y = height - 42 - sp;
                    float fire = this.mc.thePlayer.fire / this.mc.thePlayer.maxFire;
                    int fireBar = (int)(fire * 20.0F);
                    for (int k = 0; k < fireBar / 2; k++)
                        drawTexturedModalRect(x + 8 * k, y, 16, 27, 9, 9);
                    if (fireBar % 2 == 1)
                        drawTexturedModalRect(x + 8 * fireBar / 2, y, 25, 27, 9, 9);
                }
            }
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(32826);
            GL11.glPushMatrix();
            GL11.glRotatef(120.0F, 1.0F, 0.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            GL11.glPopMatrix();
            int lastHotbarStart = (this.mc.thePlayer.inventory.hotbarOffset + 27) % 36;
            int nextHotbarStart = (this.mc.thePlayer.inventory.hotbarOffset + 9) % 36;
            if (this.mc.hotbarSwapAnimationProgress > 0.0F) {
                GL11.glEnable(3089);
                GL11.glScissor((width / 2 - 90 + 2) * this.mc.resolution.scale, (3 + sp) * this.mc.resolution.scale, 182 * this.mc.resolution.scale, 16 * this.mc.resolution.scale);
                this.mc.hotbarSwapAnimationProgress -= partialTicks * this.mc.hotbarSwapAnimationProgress * 2.0F / 16.0F;
                for (int i = 0; i < 9; i++) {
                    int x = width / 2 - 90 + i * 20 + 2;
                    int y = height - 16 - 3 - 22 + (int)(22.0F * this.mc.hotbarSwapAnimationProgress);
                    renderInventorySlot(i + lastHotbarStart, x, y - sp, partialTicks, 1.0F);
                }
                if (this.mc.hotbarSwapAnimationProgress < 0.05F)
                    this.mc.hotbarSwapAnimationProgress = 0.0F;
            } else if (this.mc.hotbarSwapAnimationProgress < 0.0F) {
                GL11.glEnable(3089);
                GL11.glScissor((width / 2 - 90 + 2) * this.mc.resolution.scale, (3 + sp) * this.mc.resolution.scale, 182 * this.mc.resolution.scale, 16 * this.mc.resolution.scale);
                this.mc.hotbarSwapAnimationProgress += partialTicks * -this.mc.hotbarSwapAnimationProgress * 2.0F / 16.0F;
                for (int i = 0; i < 9; i++) {
                    int x = width / 2 - 90 + i * 20 + 2;
                    int y = height - 16 - 3 + 22 + (int)(22.0F * this.mc.hotbarSwapAnimationProgress);
                    renderInventorySlot(i + nextHotbarStart, x, y - sp, partialTicks, 1.0F);
                }
                if (this.mc.hotbarSwapAnimationProgress > -0.05F)
                    this.mc.hotbarSwapAnimationProgress = 0.0F;
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            for (int itemIndex = 0; itemIndex < 9; itemIndex++) {
                int x = width / 2 - 90 + itemIndex * 20 + 2;
                int y = height - 16 - 3 + (int)(22.0F * this.mc.hotbarSwapAnimationProgress);
                renderInventorySlot(itemIndex + this.mc.thePlayer.inventory.hotbarOffset, x, y - sp, partialTicks, 1.0F);
            }
            GL11.glDisable(3089);
        }
    }

    /*@Inject(
            method = "renderGameOverlay",
            at = @At(value = "INVOKE",target = "Lnet/minecraft/src/option/ImmersiveModeOption;drawHotbar()Z", ordinal = 1,shift = At.Shift.BEFORE)
    )
    public void renderBeforeGameOverlay(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci){
        ItemStack headSlotItem = this.mc.thePlayer.inventory.armorItemInSlot(3);
        int width = this.mc.resolution.scaledWidth;
        int height = this.mc.resolution.scaledHeight;
        if (headSlotItem.getItem() instanceof IHasOverlay) {
            InventoryPlayer inv = this.mc.thePlayer.inventory;
            ((IHasOverlay)inv.armorItemInSlot(3).getItem()).renderBeforeOverlay((GuiIngame) ((Object)this),this.mc.thePlayer,height,width,mouseX,mouseY,fontrenderer );
        }
    }*/

}
