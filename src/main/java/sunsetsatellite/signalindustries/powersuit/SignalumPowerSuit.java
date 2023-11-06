package sunsetsatellite.signalindustries.powersuit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Color;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import sunsetsatellite.fluidapi.api.ContainerItemFluid;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseAbility;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseEffectAbility;
import sunsetsatellite.signalindustries.interfaces.mixins.IKeybinds;
import sunsetsatellite.signalindustries.inventories.InventoryAbilityModule;
import sunsetsatellite.signalindustries.items.ItemAbilityModule;
import sunsetsatellite.signalindustries.items.abilities.ItemWithAbility;
import sunsetsatellite.signalindustries.util.DrawUtil;
import sunsetsatellite.signalindustries.util.Mode;
import sunsetsatellite.sunsetutils.util.TickTimer;

import java.util.HashMap;
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
    public Mode mode;
    public EntityPlayer player;
    public int selectedAbilitySlot = 0;
    public TickTimer saveTimer = new TickTimer(this, "saveToStacks",60,true);;
    public HashMap<SuitBaseAbility,Integer> cooldowns = new HashMap<>();
    public HashMap<SuitBaseEffectAbility,Integer> effectTimes = new HashMap<>();

    public float temperature;

    public SignalumPowerSuit(ItemStack[] armor, EntityPlayer player){
        this.armor = armor;
        this.player = player;
        helmet = new InventoryPowerSuit(armor[3]);
        chestplate = new InventoryPowerSuit(armor[2]);
        leggings = new InventoryPowerSuit(armor[1]);
        boots = new InventoryPowerSuit(armor[0]);
        temperature = 20.0f;
    }

    public void openCoreUi() {
        ContainerItemFluid container = new ContainerPowerSuit(player.inventory,chestplate);
        GuiPowerSuit guiCore = new GuiPowerSuit(container,player.inventory);
        guiCore.powerSuit = this;
        guiCore.name = "Signalum Power Suit | Core";
        SignalIndustries.displayGui(player,guiCore,container,chestplate,armor[2]);
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
        if(temperature > 1000){
            status = Status.OVERHEAT;
        }
        if(getEnergyPercent() == 0){
            status = Status.NO_ENERGY;
        } else if (getEnergyPercent() < 15) {
            status = Status.LOW_ENERGY;
        } else {
            status = Status.OK;
        }
        saveTimer.tick();

        //get module
        if(getModule() != null){
            module = new InventoryAbilityModule(getModule());
            mode = getModuleMode();
        } else {
            module = null;
            mode = Mode.NONE;
        }

        //count down cooldowns
        for (Map.Entry<SuitBaseAbility, Integer> entry : cooldowns.entrySet()) {
            entry.setValue(entry.getValue()-1);
            if(entry.getValue() <= 0){
                cooldowns.remove(entry.getKey());
            }
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
            if(itemStack.isItemDamaged() && getEnergy() > 0){
                decrementEnergy(1);
                itemStack.damageItem(-1,player);
            }
        }
    }

    public void activateSelectedAbility(){
        if(module.contents[selectedAbilitySlot] != null) {
            SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedAbilitySlot].getItem()).getAbility();
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
        }

    }

    public void activateSelectedAbility(Entity entity){
        if(module.contents[selectedAbilitySlot] != null) {
            SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedAbilitySlot].getItem()).getAbility();
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
        }
    }

    public void activateSelectedAbility(int x, int y, int z){
        if(module.contents[selectedAbilitySlot] != null) {
            SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedAbilitySlot].getItem()).getAbility();
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

    public Mode getModuleMode(){
        if(getModule() != null){
            return ((ItemAbilityModule)getModule().getItem()).mode;
        } else {
            return null;
        }
    }

    public void renderOverlay(GuiIngame guiIngame, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer, EntityPlayer player, int height, int width, int mouseX, int mouseY) {
        DrawUtil drawUtil = new DrawUtil();
        if(!active){
            KeyBinding openSuitKey = ((IKeybinds) Minecraft.getMinecraft(Minecraft.class).gameSettings).signalIndustries$getKeyOpenSuit();
            fontRenderer.drawCenteredString(String.format("%s | Press Shift+%s",TextFormatting.GRAY+"OFFLINE"+TextFormatting.WHITE, Keyboard.getKeyName(openSuitKey.key)),width/2,height-64,0xFFFFFFFF);
            return;
        }
        if(status == Status.NO_ENERGY){
            fontRenderer.drawCenteredString(String.format("%s | %s %s/%s | %s C", status,TextFormatting.RED+String.format("%.2f",getEnergyPercent())+"%","("+getEnergy(), getMaxEnergy() +")"+TextFormatting.WHITE,temperature),width/2,height-64,0xFFFFFFFF);
            return;
        }

        fontRenderer.drawCenteredString(String.format("%s | %s %s/%s | %s C",status.toString(),TextFormatting.RED+String.format("%.2f",getEnergyPercent())+"%","("+getEnergy(), getMaxEnergy() +")"+TextFormatting.WHITE,temperature),width/2,height-64,0xFFFFFFFF);

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
            fontRenderer.drawCenteredString(String.format("%s","No Module!"),width/2,25,color2);
        } else {
            if(module.contents[selectedAbilitySlot] != null){
                SuitBaseAbility selectedAbility = ((ItemWithAbility)module.contents[selectedAbilitySlot].getItem()).getAbility();
                I18n t = I18n.getInstance();
                if(selectedAbility instanceof SuitBaseEffectAbility){
                    if(effectTimes.containsKey(selectedAbility)){
                        fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.mode.getChatColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.LIME+String.valueOf(effectTimes.get(selectedAbility)/20)+"s"),width/2,25,color2);
                    } else if(cooldowns.containsKey(selectedAbility)){
                        fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.mode.getChatColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.RED+String.valueOf(cooldowns.get(selectedAbility)/20)+"s"),width/2,25,color2);
                    } else {
                        fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.mode.getChatColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.LIME+"READY"),width/2,25,color2);
                    }
                } else {
                    if(cooldowns.containsKey(selectedAbility)){
                        fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.mode.getChatColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.RED+String.valueOf(cooldowns.get(selectedAbility)/20)+"s"),width/2,25,color2);
                    } else {
                        fontRenderer.drawCenteredString(String.format("%s | %s | %s",selectedAbility.mode.getChatColor()+t.translateKey(selectedAbility.name)+TextFormatting.WHITE,TextFormatting.RED+"-"+selectedAbility.cost+TextFormatting.WHITE, TextFormatting.LIME+"READY"),width/2,25,color2);
                    }
                }

            } else {
                fontRenderer.drawCenteredString(String.format("%s","No ability selected."),width/2,25,color2);
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
            Mode mode = getModuleMode();
            Color c = new Color().setARGB(mode.getColor());
            GL11.glColor4f((float) c.getRed() /255, (float) c.getGreen() /255, (float) c.getBlue() /255, (float) c.getAlpha() /255);
            //GL11.glColor4b((byte) c.getRed(), (byte) c.getGreen(), (byte) c.getBlue(), (byte) c.getAlpha());
            GL11.glBindTexture(3553, mc.renderEngine.getTexture("/gui/gui.png"));
            int x = width / 2 - 91;
            int y = 0;
            drawUtil.drawTexturedModalRect(x, y, 0, 0, 182, 22);
            int i = x;
            int j = y;
            for (int i1 = 0; i1 < module.contents.length; i1++) {
                itemRenderer.renderItemIntoGUI(fontRenderer, this.mc.renderEngine, module.contents[i1], i+3, j+3, 1.0F);
                GL11.glDisable(3042);
                GL11.glDisable(2896);
                i+=20;
            }
            //selected ability
            GL11.glBindTexture(3553, mc.renderEngine.getTexture("/gui/gui.png"));
            GL11.glColor4f(1F, 1F, 1F, 1.0F);
            drawUtil.drawTexturedModalRect(x - 1 + selectedAbilitySlot % 9 * 20, y - 1, 0, 22, 24, 22 + 2);
            GL11.glBindTexture(3553, this.mc.renderEngine.getTexture("/gui/icons.png"));
        }


        //draw armor pieces and attachments
        for (int i = 0; i < 4; i++) {
            ItemStack stack = this.player.inventory.armorInventory[3 - i];
            if (stack != null) {
                int x = 2 ;
                int y = height - 64 + i * 16;
                itemRenderer.renderItemIntoGUI(fontRenderer, this.mc.renderEngine, stack, x, y, 1.0F);
                GL11.glDisable(3042);
                GL11.glDisable(2896);
                InventoryPowerSuit pieceInv = getPieceInventory(i);
                if (!pieceInv.isEmpty()) {
                    int k = 16;
                    for (ItemStack content : pieceInv.contents) {
                        if(content != null){
                            itemRenderer.renderItemIntoGUI(fontRenderer, this.mc.renderEngine, content, x+k, y, 1.0F);
                            k+=16;
                        }
                    }
                }
            }
        }
        GL11.glDisable(3042);
        GL11.glDisable(2896);

    }
}