package sunsetsatellite.signalindustries.util;


import net.minecraft.core.net.command.TextFormatting;

public enum Tier {
    PROTOTYPE(TextFormatting.LIGHT_GRAY,"0 (Prototype)"),
    BASIC(TextFormatting.WHITE,"I (Basic)"),
    REINFORCED(TextFormatting.RED,"II (Reinforced)"),
    AWAKENED(TextFormatting.ORANGE,"III (Awakened)"),
    INFINITE(TextFormatting.MAGENTA,"INF (Infinite)");

    private final TextFormatting color;
    private final String rank;
    Tier(TextFormatting color, String rank){
        this.color = color;
        this.rank = rank;
    }

    public TextFormatting getColor(){
        return this.color;
    }

    public String getRank() {
        return rank;
    }
}
