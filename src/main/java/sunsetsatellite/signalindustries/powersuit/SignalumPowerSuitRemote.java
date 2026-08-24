package sunsetsatellite.signalindustries.powersuit;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.client.entity.player.PlayerRemote;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;

import java.util.List;

import static sunsetsatellite.signalindustries.powersuit.SignalumPowerSuit.*;

public class SignalumPowerSuitRemote implements IPowerSuit {

    public InventoryPowerSuit helmet;
    public InventoryPowerSuit chestplate;
    public InventoryPowerSuit leggings;
    public InventoryPowerSuit boots;

    public PlayerRemote player;

    public SignalumPowerSuitRemote(PlayerRemote player) {
        this.player = player;
        reload();
    }

    @Override
    public int getEnergy() {
        return 0;
    }

    @Override
    public int getMaxEnergy() {
        return 0;
    }

    @Override
    public float getEnergyPercent() {
        return 0;
    }

    @Override
    public void decrementEnergy(int amount) {

    }

    @Override
    public void tick() {

    }

	@Override
    public InventoryPowerSuit getArmorPiece(HumanArmorShape shape) {
		return switch (shape) {
			case HEAD -> helmet;
			case CHEST -> chestplate;
			case LEGS -> leggings;
			case BOOTS -> boots;
		};
    }

    @Override
    public boolean hasAttachment(ItemAttachment attachment) {
        InventoryPowerSuit[] pieces = new InventoryPowerSuit[]{helmet, chestplate, leggings, boots};
        for (InventoryPowerSuit piece : pieces) {
            for (ItemStack content : piece.contents) {
                if (content != null) {
                    if (content.getItem().equals(attachment)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasAttachmentClass(Class<? extends ItemAttachment> attachment) {
        InventoryPowerSuit[] pieces = new InventoryPowerSuit[]{helmet, chestplate, leggings, boots};
        for (InventoryPowerSuit piece : pieces) {
            for (ItemStack content : piece.contents) {
                if (content != null) {
                    if (attachment.isInstance(content.getItem())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasAttachment(ItemAttachment attachment, List<AttachmentLocation> locations) {
        for (AttachmentLocation location : locations) {
            InventoryPowerSuit armorPieceInv = getArmorPiece(location.armorPiece);
            ItemStack armorPieceAttachment = armorPieceInv.getItem(location.slot);
            if (armorPieceAttachment != null && armorPieceAttachment.getItem().equals(attachment)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getAttachment(ItemAttachment attachment) {
        InventoryPowerSuit[] pieces = new InventoryPowerSuit[]{helmet, chestplate, leggings, boots};
        for (InventoryPowerSuit piece : pieces) {
            for (ItemStack content : piece.contents) {
                if (content != null) {
                    if (content.getItem().equals(attachment)) {
                        return content;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack getAttachmentClass(Class<? extends ItemAttachment> attachment) {
        InventoryPowerSuit[] pieces = new InventoryPowerSuit[]{helmet, chestplate, leggings, boots};
        for (InventoryPowerSuit piece : pieces) {
            for (ItemStack content : piece.contents) {
                if (content != null) {
                    if (attachment.isInstance(content.getItem())) {
                        return content;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }

	@Override
    public void reload() {
        helmet = new InventoryPowerSuit(player.inventory.armorItemInSlot(HumanArmorShape.HEAD));
        chestplate = new InventoryPowerSuit(player.inventory.armorItemInSlot(HumanArmorShape.CHEST));
        leggings = new InventoryPowerSuit(player.inventory.armorItemInSlot(HumanArmorShape.LEGS));
        boots = new InventoryPowerSuit(player.inventory.armorItemInSlot(HumanArmorShape.BOOTS));
    }

    @Override
    public void loadData(CompoundTag suitTag) {
        if (suitTag.containsKey("Helmet") && suitTag.containsKey("Chestplate") && suitTag.containsKey("Leggings") && suitTag.containsKey("Boots")) {
            CompoundTag helmetTag = suitTag.getCompound("Helmet");
            CompoundTag chestplateTag = suitTag.getCompound("Chestplate");
            CompoundTag leggingsTag = suitTag.getCompound("Leggings");
            CompoundTag bootsTag = suitTag.getCompound("Boots");
            helmet.container.readFromNBT(helmetTag);
            chestplate.container.readFromNBT(chestplateTag);
            leggings.container.readFromNBT(leggingsTag);
            boots.container.readFromNBT(bootsTag);
            reload();
        }
    }

    @Override
    public void saveData(CompoundTag powerSuit) {

    }
}
