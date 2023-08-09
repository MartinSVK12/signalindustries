package sunsetsatellite.signalindustries.items;


import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.IntTag;
import com.mojang.nbt.Tag;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.material.ArmorMaterial;
import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerHarness;
import sunsetsatellite.signalindustries.gui.GuiHarness;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.inventories.InventoryHarness;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Collection;
import java.util.Objects;

public class ItemSignalumPrototypeHarness extends ItemArmorTiered implements IHasOverlay {


    public ItemSignalumPrototypeHarness(String key, int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(key, id, material, armorPiece, tier);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int blockX, int blockY, int blockZ, Side side, double xPlaced, double yPlaced) {
        if(entityplayer.isSneaking()){
            SignalIndustries.displayGui(entityplayer,new GuiHarness(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new ContainerHarness(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new InventoryHarness(entityplayer.inventory.getCurrentItem()),itemstack);
            return true;
        }
        return false;
    }

    public CompoundTag getFluidStack(int id, ItemStack stack){
        return stack.tag.getCompound("fluidInventory").getCompound(String.valueOf(id));
    }

    @Override
    public void renderOverlay(GuiIngame guiIngame, EntityPlayer player, int height, int width, int mouseX, int mouseY, FontRenderer fontRenderer, ItemEntityRenderer itemRenderer) {
        InventoryPlayer inv = player.inventory;
        ItemStack armor = inv.armorItemInSlot(2);
        if(armor != null && armor.getItem() instanceof ItemSignalumPrototypeHarness){
            int i = height - 64;
            fontRenderer.drawStringWithShadow( "S. P. Harness", 4, i += 16, 0xFFFF0000);
            if (inv.getCurrentItem() != null && inv.getCurrentItem().getItem() instanceof ItemTrigger){
                ItemStack trigger = inv.getCurrentItem();
                if(!Objects.equals(trigger.tag.getString("ability"), "")){
                    if(armor.tag.getInteger("cooldown"+trigger.tag.getString("ability")) <= 0){
                        fontRenderer.drawStringWithShadow( "Ability: ", 4, i += 16, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( trigger.tag.getString("ability")+ TextFormatting.LIME+" READY", 4 + fontRenderer.getStringWidth("Ability: "), i, 0xFFFF0000);
                    } else {
                        fontRenderer.drawStringWithShadow( "Ability: ", 4, i += 16, 0xFFFFFFFF);
                        fontRenderer.drawStringWithShadow( trigger.tag.getString("ability")+ TextFormatting.RED+" "+armor.tag.getInteger("cooldown"+trigger.tag.getString("ability"))+"s", 4 + fontRenderer.getStringWidth("Ability: "), i, 0xFF808080);
                    }
                    fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 10, 0xFFFFFFFF);
                    fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount"))+TextFormatting.RED+"-"+ ItemTrigger.abilities.get(trigger.tag.getString("ability")).cost, 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                } else {
                    fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 16, 0xFFFFFFFF);
                    fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                }
            } else {
                fontRenderer.drawStringWithShadow( "Energy: ", 4, i += 16, 0xFFFFFFFF);
                fontRenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontRenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
            }
        }
    }


    public int cooldownTicks = 0;


    @Override
    public void inventoryTick(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        super.inventoryTick(itemstack, world, entity, i, flag);
        cooldownTicks++;
        if(cooldownTicks >= 20){
            cooldownTicks = 0;
            Collection<Tag<?>> tags = itemstack.tag.getValues();
            tags.forEach((NBT)->{
                if(NBT instanceof IntTag){
                    if(NBT.getTagName().contains("cooldown")){
                        if(((IntTag) NBT).getValue() > 0){
                            ((IntTag) NBT).setValue(((IntTag) NBT).getValue()-1);
                        }
                    }
                }
            });
        }
    }

}
