package sunsetsatellite.signalindustries.util;

import net.minecraft.client.render.texture.stitcher.IconCoordinate;
import net.minecraft.client.render.texture.stitcher.TextureRegistry;
import net.minecraft.core.util.helper.Side;
import sunsetsatellite.catalyst.Catalyst;
import sunsetsatellite.signalindustries.blocks.models.BlockModelVerticalMachine;

import java.util.HashMap;

public class VerticalMachineTextures {
    public HashMap<Side, String> defaultVerticalTextures;
    public HashMap<Side, String> activeVerticalTextures;
    public HashMap<Side, String> overbrightVerticalTextures;
    public static int[] orientationLookUpVertical = new int[]{1, 0, 2, 3, 4, 5, 0, 1, 2, 3, 4, 5};

    public VerticalMachineTextures() {
        super();
        this.defaultVerticalTextures = (HashMap<Side, String>) Catalyst.mapOf(Side.values(), Catalyst.arrayFill(new String[Side.values().length], "minecraft:block/texture_unassigned"));
        this.activeVerticalTextures = (HashMap<Side, String>) Catalyst.mapOf(Side.values(), Catalyst.arrayFill(new String[Side.values().length], "minecraft:block/texture_unassigned"));
        this.overbrightVerticalTextures = (HashMap<Side, String>) Catalyst.mapOf(Side.values(), Catalyst.arrayFill(new String[Side.values().length], null));
    }

    public VerticalMachineTextures(Tier tier){
        this.defaultVerticalTextures = (HashMap<Side, String>) Catalyst.mapOf(Side.values(), Catalyst.arrayFill(new String[Side.values().length], "minecraft:block/texture_unassigned"));
        this.activeVerticalTextures = (HashMap<Side, String>) Catalyst.mapOf(Side.values(), Catalyst.arrayFill(new String[Side.values().length], "minecraft:block/texture_unassigned"));
        this.overbrightVerticalTextures = (HashMap<Side, String>) Catalyst.mapOf(Side.values(), Catalyst.arrayFill(new String[Side.values().length], null));

        switch (tier) {
            case PROTOTYPE:
                withVerticalDefaultTexture("prototype_blank");
                withVerticalActiveTexture("prototype_blank");
                break;
            case BASIC:
                withVerticalDefaultTexture("basic_blank");
                withVerticalActiveTexture("basic_blank");
                break;
            case REINFORCED:
                withVerticalDefaultTexture("reinforced_blank");
                withVerticalActiveTexture("reinforced_blank");
                break;
            case AWAKENED:
                withVerticalDefaultTexture("awakened_blank");
                withVerticalActiveTexture("awakened_blank");
                break;
        }
    }

    public VerticalMachineTextures withVerticalDefaultTexture(String texture) {
        defaultVerticalTextures.replaceAll((S, I) -> "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalActiveTexture(String texture) {
        activeVerticalTextures.replaceAll((S, I) -> "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalOverbrightTexture(String texture) {
        overbrightVerticalTextures.replaceAll((S, I) -> "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalDefaultSideTextures(String texture) {
        defaultVerticalTextures.replaceAll((S, I) -> {
            if (S.isHorizontal()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public VerticalMachineTextures withVerticalActiveSideTextures(String texture) {
        activeVerticalTextures.replaceAll((S, I) -> {
            if (S.isHorizontal()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public VerticalMachineTextures withVerticalOverbrightSideTextures(String texture) {
        overbrightVerticalTextures.replaceAll((S, I) -> {
            if (S.isHorizontal()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public VerticalMachineTextures withVerticalDefaultTopBottomTextures(String texture) {
        defaultVerticalTextures.replaceAll((S, I) -> {
            if (S.isVertical()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public VerticalMachineTextures withVerticalActiveTopBottomTextures(String texture) {
        activeVerticalTextures.replaceAll((S, I) -> {
            if (S.isVertical()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public VerticalMachineTextures withVerticalOverbrightTopBottomTextures(String texture) {
        overbrightVerticalTextures.replaceAll((S, I) -> {
            if (S.isVertical()) {
                return "signalindustries:block/" + texture;
            }
            return I;
        });
        return this;
    }

    public VerticalMachineTextures withVerticalDefaultTopTexture(String texture) {
        defaultVerticalTextures.replace(Side.TOP, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalActiveTopTexture(String texture) {
        activeVerticalTextures.replace(Side.TOP, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalOverbrightTopTexture(String texture) {
        overbrightVerticalTextures.replace(Side.TOP, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalDefaultBottomTexture(String texture) {
        defaultVerticalTextures.replace(Side.BOTTOM, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalActiveBottomTexture(String texture) {
        activeVerticalTextures.replace(Side.BOTTOM, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalOverbrightBottomTexture(String texture) {
        overbrightVerticalTextures.replace(Side.BOTTOM, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalDefaultNorthTexture(String texture) {
        defaultVerticalTextures.replace(Side.NORTH, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalActiveNorthTexture(String texture) {
        activeVerticalTextures.replace(Side.NORTH, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalOverbrightNorthTexture(String texture) {
        overbrightVerticalTextures.replace(Side.NORTH, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalDefaultSouthTexture(String texture) {
        defaultVerticalTextures.replace(Side.SOUTH, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalActiveSouthTexture(String texture) {
        activeVerticalTextures.replace(Side.SOUTH, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalOverbrightSouthTexture(String texture) {
        overbrightVerticalTextures.replace(Side.SOUTH, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalDefaultWestTexture(String texture) {
        defaultVerticalTextures.replace(Side.WEST, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalActiveWestTexture(String texture) {
        activeVerticalTextures.replace(Side.WEST, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalOverbrightWestTexture(String texture) {
        overbrightVerticalTextures.replace(Side.WEST, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalDefaultEastTexture(String texture) {
        defaultVerticalTextures.replace(Side.EAST, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalActiveEastTexture(String texture) {
        activeVerticalTextures.replace(Side.EAST, "signalindustries:block/" + texture);
        return this;
    }

    public VerticalMachineTextures withVerticalOverbrightEastTexture(String texture) {
        overbrightVerticalTextures.replace(Side.EAST, "signalindustries:block/" + texture);
        return this;
    }
}