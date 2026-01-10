package sunsetsatellite.signalindustries.util;

import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.Catalyst;

import java.util.Arrays;
import java.util.HashMap;

public class MachineTextures {
    public HashMap<Side, String> defaultTextures;
    public HashMap<Side, String> activeTextures;
    public HashMap<Side, String> overbrightTextures;

    public MachineTextures() {
        this.defaultTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).filter((s) -> s != Side.NONE).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length - 1], "minecraft:block/texture_unassigned"));
        this.activeTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).filter((s) -> s != Side.NONE).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length - 1], "minecraft:block/texture_unassigned"));
        this.overbrightTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).filter((s) -> s != Side.NONE).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length - 1], null));
    }

    public MachineTextures(Tier tier) {
        this.defaultTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).filter((s) -> s != Side.NONE).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length - 1], "minecraft:block/texture_unassigned"));
        this.activeTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).filter((s) -> s != Side.NONE).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length - 1], "minecraft:block/texture_unassigned"));
        this.overbrightTextures = (HashMap<Side, String>) Catalyst.mapOf(Arrays.stream(Side.values()).filter((s) -> s != Side.NONE).toArray(Side[]::new), Catalyst.arrayFill(new String[Side.values().length - 1], null));

        switch (tier) {
            case PROTOTYPE:
                withDefaultTexture("prototype_blank");
                withActiveTexture("prototype_blank");
                break;
            case BASIC:
                withDefaultTexture("basic_blank");
                withActiveTexture("basic_blank");
                break;
            case REINFORCED:
                withDefaultTexture("reinforced_blank");
                withActiveTexture("reinforced_blank");
                break;
            case AWAKENED:
                withDefaultTexture("awakened_blank");
                withActiveTexture("awakened_blank");
                break;
        }
    }

    public MachineTextures withDefaultTexture(String texture) {
        defaultTextures.replaceAll((S, I) -> "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withActiveTexture(String texture) {
        activeTextures.replaceAll((S, I) -> "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightTextures(String texture) {
        overbrightTextures.replaceAll((S, I) -> "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultSideTextures(String texture) {
        defaultTextures.replaceAll((S, I) -> {
            if (S.isHorizontal()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withActiveSideTextures(String texture) {
        activeTextures.replaceAll((S, I) -> {
            if (S.isHorizontal()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withOverbrightSideTextures(String texture) {
        overbrightTextures.replaceAll((S, I) -> {
            if (S.isHorizontal()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withDefaultTopBottomTextures(String texture) {
        defaultTextures.replaceAll((S, I) -> {
            if (S.isVertical()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withActiveTopBottomTextures(String texture) {
        activeTextures.replaceAll((S, I) -> {
            if (S.isVertical()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withOverbrightTopBottomTextures(String texture) {
        overbrightTextures.replaceAll((S, I) -> {
            if (S.isVertical()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public MachineTextures withDefaultTopTexture(String texture) {
        defaultTextures.replace(Side.TOP, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withActiveTopTexture(String texture) {
        activeTextures.replace(Side.TOP, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightTopTexture(String texture) {
        overbrightTextures.replace(Side.TOP, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultBottomTexture(String texture) {
        defaultTextures.replace(Side.BOTTOM, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withActiveBottomTexture(String texture) {
        activeTextures.replace(Side.BOTTOM, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightBottomTexture(String texture) {
        overbrightTextures.replace(Side.BOTTOM, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultNorthTexture(String texture) {
        defaultTextures.replace(Side.NORTH, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withActiveNorthTexture(String texture) {
        activeTextures.replace(Side.NORTH, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightNorthTexture(String texture) {
        overbrightTextures.replace(Side.NORTH, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultSouthTexture(String texture) {
        defaultTextures.replace(Side.SOUTH, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withActiveSouthTexture(String texture) {
        activeTextures.replace(Side.SOUTH, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightSouthTexture(String texture) {
        overbrightTextures.replace(Side.SOUTH, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultWestTexture(String texture) {
        defaultTextures.replace(Side.WEST, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withActiveWestTexture(String texture) {
        activeTextures.replace(Side.WEST, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightWestTexture(String texture) {
        overbrightTextures.replace(Side.WEST, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withDefaultEastTexture(String texture) {
        defaultTextures.replace(Side.EAST, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withActiveEastTexture(String texture) {
        activeTextures.replace(Side.EAST, "signalindustries:block/" + texture);
        return this;
    }

    public MachineTextures withOverbrightEastTexture(String texture) {
        overbrightTextures.replace(Side.EAST, "signalindustries:block/" + texture);
        return this;
    }
}