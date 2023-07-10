package sunsetsatellite.signalindustries.items;

import net.minecraft.src.*;
import net.minecraft.src.command.ChatColor;
import net.minecraft.src.material.ArmorMaterial;
import sunsetsatellite.signalindustries.SignalIndustries;
import sunsetsatellite.signalindustries.containers.ContainerHarness;
import sunsetsatellite.signalindustries.gui.GuiHarness;
import sunsetsatellite.signalindustries.interfaces.IHasOverlay;
import sunsetsatellite.signalindustries.tiles.InventoryHarness;
import sunsetsatellite.signalindustries.util.Tier;

import java.util.Collection;
import java.util.Objects;

public class ItemSignalumPrototypeHarness extends ItemArmorTiered implements IHasOverlay {


    public ItemSignalumPrototypeHarness(int id, ArmorMaterial material, int armorPiece, Tier tier) {
        super(id, material, armorPiece, tier);
    }

    @Override
    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, double heightPlaced) {
        if(entityplayer.isSneaking()){
            SignalIndustries.displayGui(entityplayer,new GuiHarness(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new ContainerHarness(entityplayer.inventory,entityplayer.inventory.getCurrentItem()),new InventoryHarness(entityplayer.inventory.getCurrentItem()),itemstack);
            return true;
        }
        return false;
    }

    public NBTTagCompound getFluidStack(int id, ItemStack stack){
        return stack.tag.getCompoundTag("fluidInventory").getCompoundTag(String.valueOf(id));
    }

    @Override
    public void renderOverlay(FontRenderer fontrenderer, EntityPlayer player, int height, int width, int mouseX, int mouseY) {
        InventoryPlayer inv = player.inventory;
        ItemStack armor = inv.armorItemInSlot(2);
        if(armor != null && armor.getItem() instanceof ItemSignalumPrototypeHarness){
            int i = height - 64;
            fontrenderer.drawStringWithShadow( "S. P. Harness", 4, i += 16, 0xFFFF0000);
            if (inv.getCurrentItem() != null && inv.getCurrentItem().getItem() instanceof ItemTrigger){
                ItemStack trigger = inv.getCurrentItem();
                if(!Objects.equals(trigger.tag.getString("ability"), "")){
                    if(armor.tag.getInteger("cooldown"+trigger.tag.getString("ability")) <= 0){
                        fontrenderer.drawStringWithShadow( "Ability: ", 4, i += 16, 0xFFFFFFFF);
                        fontrenderer.drawStringWithShadow( trigger.tag.getString("ability")+ ChatColor.lime+" READY", 4 + fontrenderer.getStringWidth("Ability: "), i, 0xFFFF0000);
                    } else {
                        fontrenderer.drawStringWithShadow( "Ability: ", 4, i += 16, 0xFFFFFFFF);
                        fontrenderer.drawStringWithShadow( trigger.tag.getString("ability")+ChatColor.red+" "+armor.tag.getInteger("cooldown"+trigger.tag.getString("ability"))+"s", 4 + fontrenderer.getStringWidth("Ability: "), i, 0xFF808080);
                    }
                    fontrenderer.drawStringWithShadow( "Energy: ", 4, i += 10, 0xFFFFFFFF);
                    fontrenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount"))+ChatColor.red+"-"+ ItemTrigger.abilities.get(trigger.tag.getString("ability")).cost, 4 + fontrenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                } else {
                    fontrenderer.drawStringWithShadow( "Energy: ", 4, i += 16, 0xFFFFFFFF);
                    fontrenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontrenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
                }
            } else {
                fontrenderer.drawStringWithShadow( "Energy: ", 4, i += 16, 0xFFFFFFFF);
                fontrenderer.drawStringWithShadow( String.valueOf(((ItemSignalumPrototypeHarness) armor.getItem()).getFluidStack(0, armor).getInteger("amount")), 4 + fontrenderer.getStringWidth("Energy: "), i, 0xFFFF8080);
            }
        }
    }

    public int cooldownTicks = 0;

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        super.onUpdate(itemstack, world, entity, i, flag);
        cooldownTicks++;
        if(cooldownTicks >= 20){
            cooldownTicks = 0;
            Collection tags = itemstack.tag.func_28110_c();
            tags.forEach((NBT)->{
                if(NBT instanceof NBTTagInt){
                    if(((NBTTagInt) NBT).getKey().contains("cooldown")){
                        if(((NBTTagInt) NBT).intValue > 0){
                            ((NBTTagInt) NBT).intValue -= 1;
                        }
                    }
                }
            });
        }
    }
}
