package sunsetsatellite.signalindustries.items.attachments;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.client.render.model.ModelBiped;
import net.minecraft.core.Global;
import net.minecraft.core.HitResult;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.lwjgl.opengl.GL11;
import org.useless.dragonfly.helper.ModelHelper;
import org.useless.dragonfly.model.entity.BenchEntityModel;
import sunsetsatellite.catalyst.core.util.Vec3f;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.List;

public class ItemCrownAttachment extends ItemTieredAttachment {

    public ItemCrownAttachment(String name, int id, List<AttachmentPoint> attachmentPoints, Tier tier) {
        super(name, id, attachmentPoints, tier);
    }

    @Override
    public void activate(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world) {
        super.activate(stack, signalumPowerSuit, player, world);
        if(!Global.isServer){
            Minecraft mc = Minecraft.getMinecraft(this);
            if (mc.objectMouseOver != null && mc.objectMouseOver.hitType == HitResult.HitType.ENTITY && mc.objectMouseOver.entity instanceof EntityLiving) {
                if(stack.getData().getBoolean("active")) {
                    List<SignalumPowerSuit.LaserCannon> laserCannons = signalumPowerSuit.laserCannons;
                    for (int i = 0; i < laserCannons.size(); i++) {
                        SignalumPowerSuit.LaserCannon laserCannon = laserCannons.get(i);
                        if(laserCannon.target == null){
                            laserCannon.target = mc.objectMouseOver.entity;
                        } else {
                            if(signalumPowerSuit.getEnergy() > 100){
                                laserCannon.target.hurt(player,2, DamageType.FIRE);
                                signalumPowerSuit.decrementEnergy(100);
                            }
                        }
                    }
                }
            } else {
                if(stack.getData().getBoolean("active")) {
                    List<SignalumPowerSuit.LaserCannon> laserCannons = signalumPowerSuit.laserCannons;
                    for (int i = 0; i < laserCannons.size(); i++) {
                        SignalumPowerSuit.LaserCannon laserCannon = laserCannons.get(i);
                        if(laserCannon.target != null && signalumPowerSuit.getEnergy() > 100){
                            laserCannon.target.hurt(player,2, DamageType.FIRE);
                            signalumPowerSuit.decrementEnergy(100);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void tick(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world, int slot) {
        super.tick(stack, signalumPowerSuit, player, world, slot);
        if(signalumPowerSuit.getEnergy() < 1){
            stack.getData().putBoolean("active",false);
            return;
        }
        if(stack.getData().getBoolean("active") && signalumPowerSuit.laserCannons.isEmpty()){
            signalumPowerSuit.createDefaultLaserCannons();
        }
        if(stack.getData().getBoolean("active")){
            signalumPowerSuit.decrementEnergy(1);
        }
    }

    @Override
    public void altActivate(ItemStack stack, SignalumPowerSuit signalumPowerSuit, EntityPlayer player, World world) {
        super.altActivate(stack, signalumPowerSuit, player, world);
        if(signalumPowerSuit.getEnergy() >= 1) {
            boolean state = stack.getData().getBoolean("active");
            stack.getData().putBoolean("active", !state);
            if(!state) signalumPowerSuit.createDefaultLaserCannons();
            else signalumPowerSuit.laserCannons.clear();
        }
    }

    @Override
    public void renderWhenAttached(EntityPlayer player, SignalumPowerSuit signalumPowerSuit, ModelBiped modelBipedMain, ItemStack stack) {
        if(stack.getData().getBoolean("active")){
            for (SignalumPowerSuit.LaserCannon laserCannon : signalumPowerSuit.laserCannons) {
                if(laserCannon.target == null){
                    renderCannon(laserCannon.position,new Vec3f(),laserCannon.rotationAxis,laserCannon.angle);
                }
            }
        }
        modelBipedMain.bipedHead.postRender(0.0625F);
        loadTexture("/assets/signalindustries/attachments/crown_base.png");
        BenchEntityModel model = ModelHelper.getOrCreateEntityModel(SignalIndustries.MOD_ID, "crown_base.json", BenchEntityModel.class);
        GL11.glTranslatef(0f,-0.1f,0f);
        model.renderModel(0,0,0,0,0,0.0625f);
    }

    public static void renderCannon(Vec3f position, Vec3f posOffset, Vec3f rotationAxis, double angle){
        GL11.glPushMatrix();
        RenderEngine renderEngine = Minecraft.getMinecraft(ItemCrownAttachment.class).renderEngine;
        renderEngine.bindTexture(renderEngine.getTexture("/assets/signalindustries/attachments/crown_cannon.png"));
        BenchEntityModel model = ModelHelper.getOrCreateEntityModel(SignalIndustries.MOD_ID, "crown_cannon.json", BenchEntityModel.class);
        GL11.glTranslated(position.x + posOffset.x,position.y + posOffset.y,position.z + posOffset.z);
        GL11.glRotated(angle,rotationAxis.x,rotationAxis.y,rotationAxis.z);
        model.renderModel(0,0,0,0,0,0.0625f);
        GL11.glPopMatrix();
    }
}