package sunsetsatellite.signalindustries.powersuit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.model.ItemModel;
import net.minecraft.client.render.item.model.ItemModelDispatcher;
import net.minecraft.client.render.tessellator.Tessellator;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.catalyst.core.util.TickTimer;
import sunsetsatellite.catalyst.core.util.Vec3f;
import sunsetsatellite.catalyst.fluids.impl.ContainerItemFluid;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseAbility;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseEffectAbility;
import sunsetsatellite.signalindustries.interfaces.IApplicationItem;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;
import sunsetsatellite.signalindustries.inventories.item.InventoryAbilityModule;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithAbility;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithUtility;
import sunsetsatellite.signalindustries.items.attachments.ItemAbilityModule;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.util.ApplicationType;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.DrawUtil;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignalumPowerSuit {

    public ItemStack[] armor;
    public InventoryPowerSuit helmet;
    public InventoryPowerSuit chestplate;
    public InventoryPowerSuit leggings;
    public InventoryPowerSuit boots;
    public InventoryAbilityModule module;
    public Status status = Status.OK;
    public boolean active = false;
    private final Minecraft mc = Minecraft.getMinecraft(Minecraft.class);
    public Tier mode;
    public EntityPlayer player;
    public int selectedApplicationSlot = 0;
    public TickTimer saveTimer = new TickTimer(this, this::saveToStacks,60,true);
    public HashMap<SuitBaseAbility,Integer> cooldowns = new HashMap<>();
    public HashMap<SuitBaseEffectAbility,Integer> effectTimes = new HashMap<>();
    public final List<LaserCannon> laserCannons = new ArrayList<>();

    public final Vec3f[] cannonStartingPositions = new Vec3f[]{
            new Vec3f(0,-2,-1),
            new Vec3f(1,-2,0),
            new Vec3f(-1,-2,0),
            new Vec3f(1.5,-1.5,0),
            new Vec3f(-1.5,-1.5,0),
    };
    public final Vec3f[] cannonStartingRotAxis = new Vec3f[]{
            new Vec3f(0,1,0),
            new Vec3f(-1,1,0),
            new Vec3f(-1,-1,0),
            new Vec3f(-1,1,0),
            new Vec3f(-1,-1,0)
    };
    public final double[] cannonStartingAngles = new double[]{
            90,45,45,45,45
    };

    public float temperature;

    private boolean cooling;

    private static class AttachmentLocation {
        final int slot;
        final InventoryPowerSuit inv;
        final AttachmentPoint point;

        public AttachmentLocation(int slot, InventoryPowerSuit inv, AttachmentPoint point) {
            this.slot = slot;
            this.inv = inv;
            this.point = point;
        }
    }

    public static class LaserCannon {
        public Vec3f position;
        public Vec3f rotationAxis;
        public double angle;
        public Entity target = null;

        public LaserCannon(Vec3f position, Vec3f rotationAxis, double angle) {
            this.position = position;
            this.rotationAxis = rotationAxis;
            this.angle = angle;
        }
    }

    public void createDefaultLaserCannons(){
        if(hasAttachment((ItemAttachment) SIItems.annihilationCrown) && laserCannons.isEmpty()){

            for (int i = 0; i < 5; i++) {
                laserCannons.add(new LaserCannon(cannonStartingPositions[i].copy(),cannonStartingRotAxis[i].copy(),cannonStartingAngles[i]));
            }
        }
    }

    private final Map<String, AttachmentLocation> attachmentLocations = new HashMap<>();

    public SignalumPowerSuit(ItemStack[] armor, EntityPlayer player){
        this.armor = armor;
        this.player = player;
        helmet = new InventoryPowerSuit(armor[3]);
        chestplate = new InventoryPowerSuit(armor[2]);
        leggings = new InventoryPowerSuit(armor[1]);
        boots = new InventoryPowerSuit(armor[0]);
        attachmentLocations.put("headTop", new AttachmentLocation(0, helmet, AttachmentPoint.HEAD_TOP));
        attachmentLocations.put("headLens", new AttachmentLocation(1, helmet, AttachmentPoint.HEAD_LENS));
        attachmentLocations.put("coreBack", new AttachmentLocation(1, chestplate, AttachmentPoint.CORE_BACK));
        attachmentLocations.put("armFrontL", new AttachmentLocation(2, chestplate, AttachmentPoint.ARM_FRONT));
        attachmentLocations.put("armFrontR", new AttachmentLocation(7, chestplate, AttachmentPoint.ARM_FRONT));
        attachmentLocations.put("armBackL", new AttachmentLocation(3, chestplate, AttachmentPoint.ARM_BACK));
        attachmentLocations.put("armBackR", new AttachmentLocation(6, chestplate, AttachmentPoint.ARM_BACK));
        attachmentLocations.put("armSideL", new AttachmentLocation(4, chestplate, AttachmentPoint.ARM_SIDE));
        attachmentLocations.put("armSideR", new AttachmentLocation(5, chestplate, AttachmentPoint.ARM_SIDE));
        attachmentLocations.put("legSideL", new AttachmentLocation(0, leggings, AttachmentPoint.LEG_SIDE));
        attachmentLocations.put("legSideR", new AttachmentLocation(1, leggings, AttachmentPoint.LEG_SIDE));
        attachmentLocations.put("bootBackL", new AttachmentLocation(0, boots, AttachmentPoint.BOOT_BACK));
        attachmentLocations.put("bootBackR", new AttachmentLocation(1, boots, AttachmentPoint.BOOT_BACK));
        temperature = 20.0f;
    }

    public void openCoreUi() {
        ContainerItemFluid container = new ContainerPowerSuit(player.inventory,chestplate);
        GuiPowerSuit guiCore = new GuiPowerSuit(container,player.inventory);
        guiCore.powerSuit = this;
        guiCore.name = "Signalum Power Suit | Core";
        SignalIndustries.displayGui(player,() -> guiCore,container,chestplate,armor[2]);
    }

    public void activateAttachment(KeyBinding attachmentKeybind) {
        boolean shift = (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
        boolean alt = (Keyboard.isKeyDown(Keyboard.KEY_LMENU) || Keyboard.isKeyDown(Keyboard.KEY_RMENU));
        if (attachmentKeybind.isPressed()) {
            int slot = -1;
            InventoryPowerSuit inv = null;
            for (Map.Entry<String, AttachmentLocation> attachment : attachmentLocations.entrySet()) {
                if (attachmentKeybind.getId().contains(attachment.getKey())) {
                    slot = attachment.getValue().slot;
                    inv = attachment.getValue().inv;
                    break;
                }
            }
            if(slot != -1 && inv != null){
                ItemStack stack = inv.getStackInSlot(slot);
                if(stack != null){
                    ItemAttachment attachment = (ItemAttachment) stack.getItem();
                    if(alt){
                        attachment.altActivate(stack,this,player,player.world);
                    } else {
                        attachment.activate(stack,this,player,player.world);
                    }
                }
            }
        }
    }

    public enum Status {
        OK(TextFormatting.LIME),
        LOW_ENERGY(TextFormatting.ORANGE),
        NO_ENERGY(TextFormatting.RED),
        OVERHEAT(TextFormatting.RED),
        CRITICAL_DAMAGE(TextFormatting.RED);

        private final TextFormatting color;
        Status(TextFormatting color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return color+super.toString().replace("_"," ")+TextFormatting.WHITE;
        }
    }

    public void tick(){
        //set status
        if(getEnergyPercent() == 0){
            status = Status.NO_ENERGY;
        } else if (getEnergyPercent() < 15) {
            status = Status.LOW_ENERGY;
        } else {
            status = Status.OK;
        }
        if(temperature > 100){
            status = Status.OVERHEAT;
            for (ItemStack itemStack : armor) {
                itemStack.damageItem(1,player);
            }

        }
        saveTimer.tick();

        //leak excess energy
        if(getEnergy() > getMaxEnergy()){
            if (getEnergy() - 50 >= getMaxEnergy()) {
                decrementEnergy(50);
            }
            decrementEnergy(1);
            if(getEnergy()+1 == getMaxEnergy()){
                decrementEnergy(-1);
            }
        }

        //energy pack attachment bonus
        chestplate.fluidCapacity[0] = hasAttachment((ItemAttachment) SIItems.extendedEnergyPack) ? 64000 : 32000;

        //get module
        if(getModule() != null){
            module = new InventoryAbilityModule(getModule());
            mode = getModuleMode();
        } else {
            module = null;
            mode = Tier.BASIC;
        }

        List<SuitBaseAbility> temp = new ArrayList<>();
        //count down cooldowns
        for (Map.Entry<SuitBaseAbility, Integer> entry : cooldowns.entrySet()) {
            entry.setValue(entry.getValue()-1);
            if(entry.getValue() <= 0){
                temp.add(entry.getKey());
            }
        }

        for (SuitBaseAbility suitBaseAbility : temp) {
            cooldowns.remove(suitBaseAbility);
        }

        for (Map.Entry<SuitBaseEffectAbility, Integer> entry : effectTimes.entrySet()) {
            entry.setValue(entry.getValue()-1);
            switch (entry.getKey().activationType) {
                case POSITION:
                    entry.getKey().tick(player,player.world,this);
                    //TODO: figure out how to do this (should it be the current pos, or the pos at activation?)
                case SELF:
                    entry.getKey().tick(player,player.world,this);
                    break;
                case TARGET:
                    entry.getKey().tick(player,player.world,this);
                    //TODO: later
                    break;
            }
            if(entry.getValue() <= 0){
                switch (entry.getKey().activationType) {
                    case POSITION:
                        entry.getKey().deactivate(player,player.world,this);
                        break;
                    case SELF:
                        entry.getKey().deactivate(player,player.world,this);
                        break;
                    case TARGET:
                        entry.getKey().deactivate(player,player.world,this);
                        break;
                }
                cooldowns.put(entry.getKey(), entry.getKey().cooldown);
                effectTimes.remove(entry.getKey());
            }
        }

        //repair armor
        for (ItemStack itemStack : armor) {
            if(itemStack.isItemDamaged() && getEnergy() > 0 && status != Status.OVERHEAT){
                decrementEnergy(1);
                itemStack.repairItem(1);
            }
        }

        //actual cool down (temperature control)
        if(temperature > 75 && !cooling){
            cooling = true;
        }
        if(temperature <= 20 && cooling){
            cooling = false;
        }
        if(player.isInLava()){
            temperature += 0.25f;
        }
        if(cooling){
            float value = 0.05f;

            if(player.isInWaterOrRain()){
                value += 0.20f;
            }
            if(hasAttachment((ItemAttachment) SIItems.crystalWings)){
                ItemStack wings = getAttachment((ItemAttachment) SIItems.crystalWings);
                if(wings != null && wings.getData().getBoolean("active")){
                    value += 0.80f;
                }
            }
            temperature -= value;
            decrementEnergy(1);
        }
        if(temperature > 20){
            temperature -= 0.01f;
            if(hasAttachment((ItemAttachment) SIItems.crystalWings)){
                ItemStack wings = getAttachment((ItemAttachment) SIItems.crystalWings);
                if(wings != null && wings.getData().getBoolean("active")){
                    temperature -= 0.49f;
                }
            }
        }

        //tick attachments
        ItemStack[] contents = helmet.contents;
        for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
            ItemStack content = contents[i];
            if (content != null) {
                ((ItemAttachment)content.getItem()).tick(content, this, player, player.world, i);
            }
        }
        contents = chestplate.contents;
        for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
            ItemStack content = contents[i];
            if (content != null) {
                ((ItemAttachment)content.getItem()).tick(content, this, player, player.world, i);
            }
        }
        contents = leggings.contents;
        for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
            ItemStack content = contents[i];
            if (content != null) {
                ((ItemAttachment)content.getItem()).tick(content, this, player, player.world, i);
            }
        }
        contents = boots.contents;
        for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
            ItemStack content = contents[i];
            if (content != null) {
                ((ItemAttachment)content.getItem()).tick(content, this, player, player.world, i);
            }
        }

        //cannons of the annihilation crown
        for (int i = 0; i < laserCannons.size(); i++) {
            LaserCannon laserCannon = laserCannons.get(i);
            if (laserCannon.target != null) {
                double dist = laserCannon.target.distanceTo(player);
                /*double d = laserCannon.target.x - player.x;
                double d1 = laserCannon.target.z - player.z;
                double yaw = Math.atan2(d1, d) * 180.0 / 3.1415927410125732 - player.yRot;*/
                if (dist > 16 || laserCannon.target.isRemoved()) {
                    laserCannon.target = null;
                    laserCannon.position = cannonStartingPositions[i].copy();
                    laserCannon.rotationAxis = cannonStartingRotAxis[i].copy();
                    laserCannon.angle = cannonStartingAngles[i];
                    continue;
                }

                Vec3f[] cannonFocusRotAxis = new Vec3f[]{
                        new Vec3f(0,0,0),
                        new Vec3f(0,0,-1),
                        new Vec3f(0,0,1),
                        new Vec3f(1,0,0),
                        new Vec3f(-1,0,0)
                };

                double[] cannonFocusAngles = new double[]{
                        0,45,45,45,45
                };

                Vec3f[] cannonFocusOffsets = new Vec3f[]{
                        new Vec3f(0,0.3,0),
                        new Vec3f(0.3,0,0),
                        new Vec3f(-0.3,0,0),
                        new Vec3f(0,0,0.3),
                        new Vec3f(0,0,-0.3)
                };

                laserCannon.position = cannonFocusOffsets[i].copy();
                laserCannon.position.y += laserCannon.target.getHeadHeight() + 1.5;
                laserCannon.rotationAxis = cannonFocusRotAxis[i].copy();
                laserCannon.angle = cannonFocusAngles[i];
            }
        }
    }

    public void activateApplication(){
        if(module != null && module.contents[selectedApplicationSlot] != null) {
            IApplicationItem<?> app = (IApplicationItem<?>) module.contents[selectedApplicationSlot].getItem();
            if(app.getType() == ApplicationType.ABILITY){
                SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedApplicationSlot].getItem()).getApplication();
                if(selectedAbility instanceof SuitBaseEffectAbility){
                    if(!cooldowns.containsKey(selectedAbility) && !effectTimes.containsKey(selectedAbility)){
                        if(getEnergy() >= selectedAbility.cost){
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(player,player.world,this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.SELF;
                            effectTimes.put((SuitBaseEffectAbility) selectedAbility,((SuitBaseEffectAbility) selectedAbility).effectTime);
                        }
                    }
                }else {
                    if(!cooldowns.containsKey(selectedAbility)){
                        if(getEnergy() >= selectedAbility.cost){
                            cooldowns.put(selectedAbility,selectedAbility.cooldown);
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(player,player.world,this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.SELF;
                        }
                    }
                }
            } else if(app.getType() == ApplicationType.UTILITY){
                ItemWithUtility item = (ItemWithUtility) module.contents[selectedApplicationSlot].getItem();
                item.activate(module.contents[selectedApplicationSlot],this,player,player.world);
            }
        }

    }

    public void activateApplication(Entity entity){
        if(module != null && module.contents[selectedApplicationSlot] != null) {
            IApplicationItem<?> app = (IApplicationItem<?>) module.contents[selectedApplicationSlot].getItem();
            if(app.getType() == ApplicationType.ABILITY){
                SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedApplicationSlot].getItem()).getApplication();
                if(selectedAbility instanceof SuitBaseEffectAbility){
                    if(!cooldowns.containsKey(selectedAbility) && !effectTimes.containsKey(selectedAbility)){
                        if(getEnergy() >= selectedAbility.cost){
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(player, entity, player.world,this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.TARGET;
                            effectTimes.put((SuitBaseEffectAbility) selectedAbility,((SuitBaseEffectAbility) selectedAbility).effectTime);
                        }
                    }
                }else {
                    if (!cooldowns.containsKey(selectedAbility)) {
                        if (getEnergy() >= selectedAbility.cost) {
                            cooldowns.put(selectedAbility, selectedAbility.cooldown);
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(player, entity, player.world, this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.TARGET;
                        }
                    }
                }
            } else if(app.getType() == ApplicationType.UTILITY){
                ItemWithUtility item = (ItemWithUtility) module.contents[selectedApplicationSlot].getItem();
                item.activate(module.contents[selectedApplicationSlot],this,player,player.world);
            }
        }
    }

    public void activateApplication(int x, int y, int z){
        if(module != null && module.contents[selectedApplicationSlot] != null) {
            IApplicationItem<?> app = (IApplicationItem<?>) module.contents[selectedApplicationSlot].getItem();
            if(app.getType() == ApplicationType.ABILITY){
                SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedApplicationSlot].getItem()).getApplication();
                if(selectedAbility instanceof SuitBaseEffectAbility){
                    if(!cooldowns.containsKey(selectedAbility) && !effectTimes.containsKey(selectedAbility)){
                        if(getEnergy() >= selectedAbility.cost){
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(x,y,z,player,player.world,this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.POSITION;
                            effectTimes.put((SuitBaseEffectAbility) selectedAbility,((SuitBaseEffectAbility) selectedAbility).effectTime);
                        }
                    }
                }else {
                    if(!cooldowns.containsKey(selectedAbility)){
                        if(getEnergy() >= selectedAbility.cost){
                            cooldowns.put(selectedAbility,selectedAbility.cooldown);
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(x,y,z,player,player.world,this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.POSITION;
                        }
                    }
                }
            } else if(app.getType() == ApplicationType.UTILITY){
                ItemWithUtility item = (ItemWithUtility) module.contents[selectedApplicationSlot].getItem();
                item.activate(module.contents[selectedApplicationSlot],this,player,player.world);
            }
        }
    }

    public void loadFromStacks(){
        helmet = new InventoryPowerSuit(armor[3]);
        chestplate = new InventoryPowerSuit(armor[2]);
        leggings = new InventoryPowerSuit(armor[1]);
        boots = new InventoryPowerSuit(armor[0]);
    }

    public void saveToStacks(){
        helmet.saveToNBT();
        chestplate.saveToNBT();
        leggings.saveToNBT();
        boots.saveToNBT();
    }

    public int getEnergy(){
        if(chestplate.fluidContents[0] == null){
            return 0;
        }
        return chestplate.fluidContents[0].amount;
    }

    public int getMaxEnergy(){
        return chestplate.fluidCapacity[0];
    }

    public void decrementEnergy(int amount){
        if(chestplate.fluidContents[0] == null){
            return;
        }
        chestplate.fluidContents[0].amount -= amount;
    }

    public float getEnergyPercent(){
        return ((float)getEnergy()/(float)getMaxEnergy()*100);
    }

    public InventoryPowerSuit getPieceInventory(int i){
        switch (i){
            case 0:
                return helmet;
            case 1:
                return chestplate;
            case 2:
                return leggings;
            case 3:
                return boots;
            default:
                return null;
        }
    }

    public ItemStack getModule(){
        return chestplate.contents[0];
    }

    public boolean hasAttachment(ItemAttachment attachment){
        InventoryPowerSuit[] pieces = new InventoryPowerSuit[]{helmet,chestplate,leggings,boots};
        for (InventoryPowerSuit piece : pieces) {
            for (ItemStack content : piece.contents) {
                if(content != null){
                    if(content.getItem() == attachment){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //TODO: replace with enum
    public boolean hasAttachment(ItemAttachment attachment, List<String> locations){
        for (String location : locations) {
            AttachmentLocation attachmentLocation = attachmentLocations.get(location);
            if (attachmentLocation == null || attachmentLocation.inv.contents[attachmentLocation.slot] == null || (attachmentLocation.inv.contents[attachmentLocation.slot].getItem() != null && attachmentLocation.inv.contents[attachmentLocation.slot].getItem() != attachment)) {
                return false;
            }
        }
        return true;
    }


    public ItemStack getAttachment(ItemAttachment attachment){
        InventoryPowerSuit[] pieces = new InventoryPowerSuit[]{helmet,chestplate,leggings,boots};
        for (InventoryPowerSuit piece : pieces) {
            for (ItemStack content : piece.contents) {
                if(content != null){
                    if(content.getItem() == attachment){
                        return content;
                    }
                }
            }
        }
        return null;
    }

    public Tier getModuleMode(){
        if(getModule() != null){
            return ((ItemAbilityModule)getModule().getItem()).getTier();
        } else {
            return null;
        }
    }

    public void renderOverlay(GuiIngame guiIngame, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer, EntityPlayer player, int height, int width, int mouseX, int mouseY) {
        DrawUtil drawUtil = new DrawUtil();
        if(!active){
            KeyBinding openSuitKey = ((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyOpenSuit();
            fontRenderer.drawCenteredString(String.format("%s | Press [Shift+%s]",TextFormatting.GRAY+"OFFLINE"+TextFormatting.WHITE, openSuitKey.getKeyName()),width/2,height-64,0xFFFFFFFF);
            return;
        }
        if(status == Status.NO_ENERGY){
            KeyBinding openSuitKey = ((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyOpenSuit();
            fontRenderer.drawCenteredString(String.format("%s | %s %s/%s | Press [%s] | %s C", status,TextFormatting.RED+String.format("%.2f",getEnergyPercent())+"%","("+getEnergy(), getMaxEnergy() +")"+TextFormatting.WHITE,openSuitKey.getKeyName(),String.format("%.2f",temperature)),width/2,height-64,0xFFFFFFFF);
            return;
        }

        fontRenderer.drawCenteredString(String.format("%s | %s %s/%s | %s C",status.toString(),TextFormatting.RED+String.format("%.2f",getEnergyPercent())+"%","("+getEnergy(), getMaxEnergy() +")"+TextFormatting.WHITE,String.format("%.2f",temperature)),width/2,height-64,0xFFFFFFFF);

        int color = mode.getColor(0x40);//0x40808080;
        int color2 = mode.getColor();//0xFF808080;

        //top
        drawUtil.drawGradientRect(0,0,width,16,color,color);
        drawUtil.drawGradientRect(0,16,width/2-100,20,color,color2);
        drawUtil.drawGradientRect(width/2+100,16,width,20,color,color2);
        drawUtil.drawGradientRect(width/2-100,36,width/2+100,40,color,color2);
        drawUtil.drawGradientRect(width/2-102,20,width/2-100,40,color2,color2);
        drawUtil.drawGradientRect(width/2+100,20,width/2+102,40,color2,color2);

        drawUtil.drawGradientRect(width/2-100,16,width/2+100,36,color,color);

        if(getModule() == null){
            fontRenderer.drawCenteredString(String.format("%s","No module."),width/2,25,color2);
        } else {
            if(module.contents[selectedApplicationSlot] != null){
                IApplicationItem<?> app = (IApplicationItem<?>) module.contents[selectedApplicationSlot].getItem();
                if(app.getType() == ApplicationType.ABILITY){
                    SuitBaseAbility selectedAbility = ((ItemWithAbility)module.contents[selectedApplicationSlot].getItem()).getApplication();
                    I18n t = I18n.getInstance();
                    if(selectedAbility instanceof SuitBaseEffectAbility){
                        if(effectTimes.containsKey(selectedAbility)){
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.LIME+String.valueOf(effectTimes.get(selectedAbility)/20)+"s"),width/2,25,color2);
                        } else if(cooldowns.containsKey(selectedAbility)){
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.RED+String.valueOf(cooldowns.get(selectedAbility)/20)+"s"),width/2,25,color2);
                        } else {
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.LIME+"READY"),width/2,25,color2);
                        }
                    } else {
                        if(cooldowns.containsKey(selectedAbility)){
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.RED+String.valueOf(cooldowns.get(selectedAbility)/20)+"s"),width/2,25,color2);
                        } else {
                            fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.tier.getTextColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.LIME+"READY"),width/2,25,color2);
                        }
                    }
                } else if (app.getType() == ApplicationType.UTILITY) {
                    ItemWithUtility item = (ItemWithUtility) module.contents[selectedApplicationSlot].getItem();
                    fontRenderer.drawCenteredString(String.format("%s%s%s",item.getTier().getTextColor(),item.getTranslatedName(module.contents[selectedApplicationSlot]),TextFormatting.WHITE),width/2,25,color2);
                }
            } else {
                fontRenderer.drawCenteredString(String.format("%s","No application selected."),width/2,25,color2);
            }
        }

        //bottom
        drawUtil.drawGradientRect(0,height-20,width,height,color,color);
        drawUtil.drawGradientRect(width/2-170,height-24,width/2-100,height-20,color2,color);
        drawUtil.drawGradientRect(width/2+100,height-24,width,height-20,color2,color);
        drawUtil.drawGradientRect(width/2-100,height-44,width/2+100,height-40,color2,color);
        drawUtil.drawGradientRect(width/2-102,height-44,width/2-100,height-24,color2,color2);
        drawUtil.drawGradientRect(width/2+100,height-44,width/2+102,height-24,color2,color2);

        drawUtil.drawGradientRect(width/2-100,height-40,width/2+100,height-20,color,color);

        //armor display
        drawUtil.drawGradientRect(0,height-74,width/2-170,height-70,color2,color);
        drawUtil.drawGradientRect(width/2-168,height-24,width/2-170,height-74,color2,color2);
        drawUtil.drawGradientRect(0,height-74,width/2-170,height-20,color,color);

        //ability hotbar
        if(getModule() == null){
            GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
            GL11.glBindTexture(3553, mc.renderEngine.getTexture("/gui/gui.png"));
            int x = width / 2 - 91;
            int y = 0;
            drawUtil.drawTexturedModalRect(x, y, 0, 0, 182, 22);
            //selected ability
            //GL11.glColor4f(1F, 0F, 0F, 1.0F);
            //drawUtil.drawTexturedModalRect(x - 1 + selectedAbilitySlot % 9 * 20, y - 1, 0, 22, 24, 22 + 2);
            //GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/icons.png"));
        } else {
            Tier mode = getModuleMode();
            Color c = new Color().setARGB(mode.getColor());
            GL11.glColor4f((float) c.getRed() /255, (float) c.getGreen() /255, (float) c.getBlue() /255, (float) c.getAlpha() /255);
            //GL11.glColor4b((byte) c.getRed(), (byte) c.getGreen(), (byte) c.getBlue(), (byte) c.getAlpha());
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
            int x = width / 2 - 91;
            int y = 0;
            drawUtil.drawTexturedModalRect(x, y, 0, 0, 182, 22);
            int i = x;
            int j = y;
            for (int i1 = 0; i1 < module.contents.length; i1++) {
                ItemModel model = ItemModelDispatcher.getInstance().getDispatch(module.contents[i1]);
                model.renderItemIntoGui(Tessellator.instance, fontRenderer, this.mc.renderEngine, module.contents[i1], i+3, j+3, 1.0F);
                GL11.glDisable(3042);
                GL11.glDisable(2896);
                i+=20;
            }
            //selected ability
            GL11.glBindTexture(3553, mc.renderEngine.getTexture("/gui/gui.png"));
            GL11.glColor4f(1F, 1F, 1F, 1.0F);
            drawUtil.drawTexturedModalRect(x - 1 + selectedApplicationSlot % 9 * 20, y - 1, 0, 22, 24, 22 + 2);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/icons.png"));
        }


        //draw armor pieces and attachments
        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.player.inventory.armorInventory[3 - i];
            if (stack != null) {
                int x = 2 ;
                int y = height - 64 + i * 16;
                ItemModel model = ItemModelDispatcher.getInstance().getDispatch(stack);
                model.renderItemIntoGui(Tessellator.instance,fontRenderer, this.mc.renderEngine, stack, x, y, 1.0F);
                GL11.glDisable(3042);
                GL11.glDisable(2896);
                InventoryPowerSuit pieceInv = getPieceInventory(i);
                if (!pieceInv.isEmpty()) {
                    int k = 16;
                    for (ItemStack content : pieceInv.contents) {
                        if(content != null){
                            model = ItemModelDispatcher.getInstance().getDispatch(content);
                            model.renderItemIntoGui(Tessellator.instance,fontRenderer, this.mc.renderEngine, content, x+k, y, 1.0F);
                            k+=16;
                        }
                    }
                }
            }
        }
        GL11.glDisable(3042);
        GL11.glDisable(2896);


        //render attachment info
        InventoryPowerSuit[] pieces = new InventoryPowerSuit[]{helmet,chestplate,leggings,boots};
        for (InventoryPowerSuit piece : pieces) {
            for (ItemStack content : piece.contents) {
                if(content != null){
                    if(content.getItem() instanceof IHasOverlay){
                        ((IHasOverlay) content.getItem()).renderOverlay(content, this, guiIngame,player,height,width,mouseX,mouseY,fontRenderer,itemRenderer);
                    }
                }
            }
        }

    }


}
