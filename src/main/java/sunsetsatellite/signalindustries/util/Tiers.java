package sunsetsatellite.signalindustries.util;

import net.minecraft.src.command.ChatColor;

public enum Tiers {
    PROTOTYPE(ChatColor.lightGray,"0 (Prototype)"),
    BASIC(ChatColor.white,"I (Basic)"),
    REINFORCED(ChatColor.red,"II (Reinforced)"),
    AWAKENED(ChatColor.orange,"III (Awakened)"),
    INFINITE(ChatColor.magenta,"INF (Infinite)");

    private final ChatColor color;
    private final String rank;
    Tiers(ChatColor color, String rank){
        this.color = color;
        this.rank = rank;
    }

    public ChatColor getColor(){
        return this.color;
    }

    public String getRank() {
        return rank;
    }
}
