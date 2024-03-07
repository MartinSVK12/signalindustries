package sunsetsatellite.signalindustries.util;


import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Color;

public enum Tier {
    PROTOTYPE(TextFormatting.LIGHT_GRAY,0xFF808080 , "0 (Prototype)"),
    BASIC(TextFormatting.WHITE, 0xFFFFFFFF, "I (Basic)"),
    REINFORCED(TextFormatting.RED, 0xFFFF2020, "II (Reinforced)"),
    AWAKENED(TextFormatting.ORANGE, 0xFFFF8C00, "III (Awakened)"),
    INFINITE(TextFormatting.MAGENTA, 0xFFFF00FF, "INF (Infinite)");

    private final TextFormatting textColor;
    private final int color;
    private final String rank;
    Tier(TextFormatting textColor, int color, String rank){
        this.textColor = textColor;
        this.color = color;
        this.rank = rank;
    }

    public int getColor() {
        return color;
    }

    public int getColor(int alpha){
        Color a = new Color().setRGBA(0,0,0,alpha);
        Color c = new Color().setARGB(color);
        a.setRGB(c);
        return a.getARGB();
    }

    public TextFormatting getTextColor(){
        return this.textColor;
    }

    public String getRank() {
        return rank;
    }
}
