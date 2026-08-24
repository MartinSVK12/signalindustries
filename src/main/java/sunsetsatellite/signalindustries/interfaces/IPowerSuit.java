package sunsetsatellite.signalindustries.interfaces;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.powersuit.InventoryPowerSuit;
import sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit;

import java.util.List;

public interface IPowerSuit {
    int getEnergy();

    int getMaxEnergy();

    float getEnergyPercent();

    void decrementEnergy(int amount);

    void tick();

	void reload();

    boolean hasAttachment(ItemAttachment attachment);

    boolean hasAttachmentClass(Class<? extends ItemAttachment> attachment);

    boolean hasAttachment(ItemAttachment attachment, List<SignalumPowerSuit.AttachmentLocation> locations);

    ItemStack getAttachment(ItemAttachment attachment);

    ItemStack getAttachmentClass(Class<? extends ItemAttachment> attachment);

    boolean isActive();

    void loadData(CompoundTag suitTag);

    void saveData(CompoundTag suitTag);

    InventoryPowerSuit getArmorPiece(HumanArmorShape shape);
}
