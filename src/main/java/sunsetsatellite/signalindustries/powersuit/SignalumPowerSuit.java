package sunsetsatellite.signalindustries.powersuit;

import com.mojang.nbt.tags.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.HumanArmorShape;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.net.command.TextFormatting;
import sunsetsatellite.signalindustries.SIItems;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseAbility;
import sunsetsatellite.signalindustries.abilities.powersuit.SuitBaseEffectAbility;
import sunsetsatellite.signalindustries.interfaces.IApplicationItem;
import sunsetsatellite.signalindustries.interfaces.IPlayerPowerSuit;
import sunsetsatellite.signalindustries.interfaces.IPowerSuit;
import sunsetsatellite.signalindustries.invs.InventoryAbilityModule;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithAbility;
import sunsetsatellite.signalindustries.items.applications.base.ItemWithUtility;
import sunsetsatellite.signalindustries.items.attachments.ItemAttachment;
import sunsetsatellite.signalindustries.mp.message.NetworkMessagePowerSuitRemoteSync;
import sunsetsatellite.signalindustries.mp.message.NetworkMessagePowerSuitSync;
import sunsetsatellite.signalindustries.util.ApplicationType;
import sunsetsatellite.signalindustries.util.AttachmentPoint;
import sunsetsatellite.signalindustries.util.InventorySerializer;
import turniplabs.halplibe.helper.EnvironmentHelper;
import turniplabs.halplibe.helper.network.NetworkHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.core.enums.HumanArmorShape.*;

public class SignalumPowerSuit implements IPowerSuit {
    public final Player player;

    public boolean active = false;
    private boolean cooling = false;
    public float temperature = 20f;
    public Status status = Status.OK;

    public InventoryPowerSuit helmet;
    public InventoryPowerSuit chestplate;
    public InventoryPowerSuit leggings;
    public InventoryPowerSuit boots;

    public InventoryAbilityModule module;
    public int selectedApplicationSlot = 0;

    public HashMap<SuitBaseAbility, Integer> cooldowns = new HashMap<>();
    public HashMap<SuitBaseEffectAbility, Integer> effectTimes = new HashMap<>();

    public SignalumPowerSuit(Player player) {
        this.player = player;

        helmet = new InventoryPowerSuit(player.inventory.armorItemInSlot(HEAD));
        chestplate = new InventoryPowerSuit(player.inventory.armorItemInSlot(CHEST));
        leggings = new InventoryPowerSuit(player.inventory.armorItemInSlot(LEGS));
        boots = new InventoryPowerSuit(player.inventory.armorItemInSlot(BOOTS));

        if (((IPlayerPowerSuit<?>) player).getPowerSuitData() != null) {
            loadData(((IPlayerPowerSuit<?>) player).getPowerSuitData());
        }


    }

    @Override
    public int getEnergy() {
        if (chestplate.fluidContents[0] == null) {
            return 0;
        }
        return chestplate.fluidContents[0].amount;
    }

    @Override
    public int getMaxEnergy() {
        return chestplate.fluidCapacity[0];
    }

    @Override
    public float getEnergyPercent() {
        return ((float) getEnergy() / (float) getMaxEnergy() * 100);
    }


    @Override
    public void decrementEnergy(int amount) {
        if (chestplate.fluidContents[0] == null) {
            return;
        }
        chestplate.fluidContents[0].amount -= amount;
        chestplate.onFluidInventoryChanged();
        InventorySerializer.saveInvToNBT(chestplate.container, chestplate);
    }

    @Override
    public void tick() {
        verify();

        if (player.world != null && player.world.isClientSide) return;

        if (EnvironmentHelper.isServerEnvironment()) {
            CompoundTag data = new CompoundTag();
            saveData(data);
            NetworkHandler.sendToPlayer(player, new NetworkMessagePowerSuitSync(data));
            NetworkHandler.sendToAllPlayers(new NetworkMessagePowerSuitRemoteSync(player.username, player.uuid, data));
        }

        // set status based on energy levels
        if (getEnergyPercent() == 0) {
            status = Status.NO_ENERGY;
        } else if (getEnergyPercent() < 15) {
            status = Status.LOW_ENERGY;
        } else {
            status = Status.OK;
        }

        if (temperature > 100) {
            status = Status.OVERHEAT;
            ItemStack[] armorInventory = player.inventory.armorInventory;
            for (int i = 0; i < armorInventory.length; i++) {
                if (i > 3) continue;
                ItemStack itemStack = armorInventory[i];
                itemStack.damageItem(1, player);
            }
        }

        // sadly this can't work anymore
        /*// leak excess energy
        if(getEnergy() > getMaxEnergy()){
            if (getEnergy() - 50 >= getMaxEnergy()) {
                decrementEnergy(50);
            }
            decrementEnergy(1);
            if(getEnergy()+1 == getMaxEnergy()){
                decrementEnergy(-1);
            }
        }*/

        // temperature regulation
        if (temperature > 75 && !cooling) {
            cooling = true;
        }
        if (temperature <= 20 && cooling) {
            cooling = false;
        }
        if (player.isInLava()) {
            temperature += 0.25f;
        }
        if (cooling) {
            float value = 0.05f;

            if (player.isInWaterOrRain()) {
                value += 0.20f;
            }
            if (hasAttachment(SIItems.crystalWings)) {
                ItemStack wings = getAttachment(SIItems.crystalWings);
                if (wings != null && wings.getData().getBoolean("active")) {
                    value += 0.80f;
                }
            }
            temperature -= value;
            decrementEnergy(1);
        }
        if (temperature > 20) {
            temperature -= 0.01f;
            if (hasAttachment(SIItems.crystalWings)) {
                ItemStack wings = getAttachment(SIItems.crystalWings);
                if (wings != null && wings.getData().getBoolean("active")) {
                    temperature -= 0.49f;
                }
            }
        }

        // energy pack attachment bonus
        chestplate.fluidCapacity[0] = hasAttachment(SIItems.extendedEnergyPack) ? 64000 : 32000;

        // tick attachments
        ItemStack[] contents = helmet.contents;
        for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
            ItemStack content = contents[i];
            if (content != null) {
                ((ItemAttachment) content.getItem()).tick(content, this, player, player.world, i);
            }
        }
        contents = chestplate.contents;
        for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
            ItemStack content = contents[i];
            if (content != null) {
                ((ItemAttachment) content.getItem()).tick(content, this, player, player.world, i);
            }
        }
        contents = leggings.contents;
        for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
            ItemStack content = contents[i];
            if (content != null) {
                ((ItemAttachment) content.getItem()).tick(content, this, player, player.world, i);
            }
        }
        contents = boots.contents;
        for (int i = 0, contentsLength = contents.length; i < contentsLength; i++) {
            ItemStack content = contents[i];
            if (content != null) {
                ((ItemAttachment) content.getItem()).tick(content, this, player, player.world, i);
            }
        }

        List<SuitBaseAbility> temp = new ArrayList<>();
        // count down cooldowns
        for (Map.Entry<SuitBaseAbility, Integer> entry : cooldowns.entrySet()) {
            entry.setValue(entry.getValue() - 1);
            if (entry.getValue() <= 0) {
                temp.add(entry.getKey());
            }
        }


        for (Map.Entry<SuitBaseEffectAbility, Integer> entry : effectTimes.entrySet()) {
            entry.setValue(entry.getValue() - 1);
            switch (entry.getKey().activationType) {
                case POSITION:
                    entry.getKey().tick(player, player.world, this);
                    //TODO: figure out how to do this (should it be the current pos, or the pos at activation?)
                case SELF:
                    entry.getKey().tick(player, player.world, this);
                    break;
                case TARGET:
                    entry.getKey().tick(player, player.world, this);
                    //TODO: later
                    break;
            }
            if (entry.getValue() <= 0) {
                switch (entry.getKey().activationType) {
                    case POSITION:
                        entry.getKey().deactivate(player, player.world, this);
                        break;
                    case SELF:
                        entry.getKey().deactivate(player, player.world, this);
                        break;
                    case TARGET:
                        entry.getKey().deactivate(player, player.world, this);
                        break;
                }
                cooldowns.put(entry.getKey(), entry.getKey().cooldown);
                effectTimes.remove(entry.getKey());
            }
        }

        for (SuitBaseAbility suitBaseAbility : temp) {
            cooldowns.remove(suitBaseAbility);
        }

        // repair armor
        ItemStack[] armorInventory = player.inventory.armorInventory;
        for (int i = 0; i < armorInventory.length; i++) {
            if (i > 3) continue;
            ItemStack itemStack = armorInventory[i];
            if (itemStack.isItemDamaged() && getEnergy() > 0 && status != Status.OVERHEAT) {
                decrementEnergy(1);
				if (itemStack.isItemStackDamageable()) {
					if (itemStack.getMetadata() <= itemStack.getMaxDamage() && itemStack.getMetadata() >= 0) {
						itemStack.setMetadata(itemStack.getMetadata()-i);
					}
				}
                //itemStack.repairItem(1);
            }
        }
    }

    public void verify() {
        // Validate that the references have not changed, and if they did update them
        if (helmet.container != player.inventory.armorItemInSlot(HEAD)) {
            helmet = new InventoryPowerSuit(player.inventory.armorItemInSlot(HEAD));
        }
        if (chestplate.container != player.inventory.armorItemInSlot(CHEST)) {
            chestplate = new InventoryPowerSuit(player.inventory.armorItemInSlot(CHEST));
        }
        if (leggings.container != player.inventory.armorItemInSlot(LEGS)) {
            leggings = new InventoryPowerSuit(player.inventory.armorItemInSlot(LEGS));
        }
        if (boots.container != player.inventory.armorItemInSlot(BOOTS)) {
            boots = new InventoryPowerSuit(player.inventory.armorItemInSlot(BOOTS));
        }

        if (getModule() != null) {
            module = new InventoryAbilityModule(getModule());
        } else {
            module = null;
        }
    }

    public void reload() {
        if (player.inventory.armorItemInSlot(HEAD) != null
                && player.inventory.armorItemInSlot(CHEST) != null
                && player.inventory.armorItemInSlot(LEGS) != null
                && player.inventory.armorItemInSlot(BOOTS) != null
        ) {
            helmet = new InventoryPowerSuit(player.inventory.armorItemInSlot(HEAD));
            chestplate = new InventoryPowerSuit(player.inventory.armorItemInSlot(CHEST));
            leggings = new InventoryPowerSuit(player.inventory.armorItemInSlot(LEGS));
            boots = new InventoryPowerSuit(player.inventory.armorItemInSlot(BOOTS));
        }
    }

    public ItemStack getModule() {
        return chestplate.contents[0];
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

    /*public Tier getModuleTier(){
        if(getModule() != null){
            return ((ItemAbilityModule)getModule().getItem()).getTier();
        } else {
            return null;
        }
    }*/

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
        return active;
    }

    public void activateApplication() {
        if (module != null && module.contents[selectedApplicationSlot] != null) {
            IApplicationItem<?> app = (IApplicationItem<?>) module.contents[selectedApplicationSlot].getItem();
            if (app.getType() == ApplicationType.ABILITY) {
                SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedApplicationSlot].getItem()).getApplication();
                if (selectedAbility instanceof SuitBaseEffectAbility) {
                    if (!cooldowns.containsKey(selectedAbility) && !effectTimes.containsKey(selectedAbility)) {
                        if (getEnergy() >= selectedAbility.cost) {
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(player, player.world, this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.SELF;
                            effectTimes.put((SuitBaseEffectAbility) selectedAbility, ((SuitBaseEffectAbility) selectedAbility).effectTime);
                        }
                    }
                } else {
                    if (!cooldowns.containsKey(selectedAbility)) {
                        if (getEnergy() >= selectedAbility.cost) {
                            cooldowns.put(selectedAbility, selectedAbility.cooldown);
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(player, player.world, this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.SELF;
                        }
                    }
                }
            } else if (app.getType() == ApplicationType.UTILITY) {
                ItemWithUtility item = (ItemWithUtility) module.contents[selectedApplicationSlot].getItem();
                item.activate(module.contents[selectedApplicationSlot], this, player, player.world);
            }
        }

    }

    public void activateApplication(Entity entity) {
        if (module != null && module.contents[selectedApplicationSlot] != null) {
            IApplicationItem<?> app = (IApplicationItem<?>) module.contents[selectedApplicationSlot].getItem();
            if (app.getType() == ApplicationType.ABILITY) {
                SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedApplicationSlot].getItem()).getApplication();
                if (selectedAbility instanceof SuitBaseEffectAbility) {
                    if (!cooldowns.containsKey(selectedAbility) && !effectTimes.containsKey(selectedAbility)) {
                        if (getEnergy() >= selectedAbility.cost) {
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(player, entity, player.world, this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.TARGET;
                            effectTimes.put((SuitBaseEffectAbility) selectedAbility, ((SuitBaseEffectAbility) selectedAbility).effectTime);
                        }
                    }
                } else {
                    if (!cooldowns.containsKey(selectedAbility)) {
                        if (getEnergy() >= selectedAbility.cost) {
                            cooldowns.put(selectedAbility, selectedAbility.cooldown);
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(player, entity, player.world, this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.TARGET;
                        }
                    }
                }
            } else if (app.getType() == ApplicationType.UTILITY) {
                ItemWithUtility item = (ItemWithUtility) module.contents[selectedApplicationSlot].getItem();
                item.activate(module.contents[selectedApplicationSlot], this, player, player.world);
            }
        }
    }

    public void activateApplication(int x, int y, int z) {
        if (module != null && module.contents[selectedApplicationSlot] != null) {
            IApplicationItem<?> app = (IApplicationItem<?>) module.contents[selectedApplicationSlot].getItem();
            if (app.getType() == ApplicationType.ABILITY) {
                SuitBaseAbility selectedAbility = ((ItemWithAbility) module.contents[selectedApplicationSlot].getItem()).getApplication();
                if (selectedAbility instanceof SuitBaseEffectAbility) {
                    if (!cooldowns.containsKey(selectedAbility) && !effectTimes.containsKey(selectedAbility)) {
                        if (getEnergy() >= selectedAbility.cost) {
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(x, y, z, player, player.world, this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.POSITION;
                            effectTimes.put((SuitBaseEffectAbility) selectedAbility, ((SuitBaseEffectAbility) selectedAbility).effectTime);
                        }
                    }
                } else {
                    if (!cooldowns.containsKey(selectedAbility)) {
                        if (getEnergy() >= selectedAbility.cost) {
                            cooldowns.put(selectedAbility, selectedAbility.cooldown);
                            decrementEnergy(selectedAbility.cost);
                            selectedAbility.activate(x, y, z, player, player.world, this);
                            selectedAbility.activationType = SuitBaseAbility.ActivationType.POSITION;
                        }
                    }
                }
            } else if (app.getType() == ApplicationType.UTILITY) {
                ItemWithUtility item = (ItemWithUtility) module.contents[selectedApplicationSlot].getItem();
                item.activate(module.contents[selectedApplicationSlot], this, player, player.world);
            }
        }
    }

    public void activateAttachment(String attachmentKeybind, boolean shift, boolean alt, boolean ctrl) {
        int slot = -1;
        InventoryPowerSuit inv = null;
        for (AttachmentLocation attachment : AttachmentLocation.values()) {
            if (attachmentKeybind.contains(attachment.id)) {
                slot = attachment.slot;
                inv = getArmorPiece(attachment.armorPiece);
                break;
            }
        }
        if (slot != -1 && inv != null) {
            ItemStack stack = inv.getItem(slot);
            if (stack != null) {
                ItemAttachment attachment = (ItemAttachment) stack.getItem();
                if (alt) {
                    attachment.altActivate(stack, this, player, player.world, shift, alt, ctrl);
                } else {
                    attachment.activate(stack, this, player, player.world, shift, alt, ctrl);
                }
            }
        }
    }

    public void saveData(CompoundTag tag) {
        CompoundTag suitTag = new CompoundTag();
        suitTag.putFloat("Temperature", temperature);
        suitTag.putBoolean("Active", active);
        suitTag.putInt("Status", status.ordinal());
        suitTag.putInt("SelectedApplicationSlot", selectedApplicationSlot);
        InventorySerializer.saveInvToNBT(helmet.container, helmet);
        InventorySerializer.saveInvToNBT(chestplate.container, chestplate);
        InventorySerializer.saveInvToNBT(leggings.container, leggings);
        InventorySerializer.saveInvToNBT(boots.container, boots);
        CompoundTag helmetTag = new CompoundTag();
        CompoundTag chestplateTag = new CompoundTag();
        CompoundTag leggingsTag = new CompoundTag();
        CompoundTag bootsTag = new CompoundTag();
        helmet.container.writeToNBT(helmetTag);
        chestplate.container.writeToNBT(chestplateTag);
        leggings.container.writeToNBT(leggingsTag);
        boots.container.writeToNBT(bootsTag);
        suitTag.put("Helmet", helmetTag);
        suitTag.put("Chestplate", chestplateTag);
        suitTag.put("Leggings", leggingsTag);
        suitTag.put("Boots", bootsTag);
        tag.putCompound("PowerSuit", suitTag);
    }

    public void loadData(CompoundTag suitTag) {
        active = suitTag.getBoolean("Active");
        temperature = suitTag.getFloat("Temperature");
        status = Status.values()[suitTag.getInteger("Status")];
        selectedApplicationSlot = suitTag.getInteger("SelectedApplicationSlot");

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

    public enum AttachmentLocation {
        HEAD_TOP("headTop", 0, HEAD, AttachmentPoint.HEAD_TOP),
        HEAD_LENS("headLens", 1, HEAD, AttachmentPoint.HEAD_LENS),
        COLORIZER("colorizer", 8, CHEST, AttachmentPoint.COLORIZER),
        CORE_BACK("coreBack", 1, CHEST, AttachmentPoint.CORE_BACK),
        ARM_FRONT_L("armFrontL", 2, CHEST, AttachmentPoint.ARM_FRONT),
        ARM_FRONT_R("armFrontR", 7, CHEST, AttachmentPoint.ARM_FRONT),
        ARM_BACK_L("armBackL", 3, CHEST, AttachmentPoint.ARM_BACK),
        ARM_BACK_R("armBackR", 6, CHEST, AttachmentPoint.ARM_BACK),
        ARM_SIDE_L("armSideL", 4, CHEST, AttachmentPoint.ARM_SIDE),
        ARM_SIDE_R("armSideR", 5, CHEST, AttachmentPoint.ARM_SIDE),
        LEG_SIDE_L("legSideL", 0, LEGS, AttachmentPoint.LEG_SIDE),
        LEG_SIDE_R("legSideR", 1, LEGS, AttachmentPoint.LEG_SIDE),
        BOOT_BACK_L("bootBackL", 0, BOOTS, AttachmentPoint.BOOT_BACK),
        BOOT_BACK_R("bootBackR", 1, BOOTS, AttachmentPoint.BOOT_BACK);

        public final int slot;
        public final HumanArmorShape armorPiece;
        public final AttachmentPoint point;
        public final String id;

        AttachmentLocation(String id, int slot, HumanArmorShape armorPiece, AttachmentPoint point) {
            this.id = id;
            this.slot = slot;
            this.armorPiece = armorPiece;
            this.point = point;
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
            return color + super.toString().replace("_", " ") + TextFormatting.WHITE;
        }
    }
}
