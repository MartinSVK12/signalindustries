package sunsetsatellite.signalindustries.util;


import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Color;

@Deprecated
public enum Mode {
    NONE(TextFormatting.GRAY,"None",0xFF808080),
    NORMAL(TextFormatting.RED,"Normal", 0xFFFF2020),
    AWAKENED(TextFormatting.ORANGE,"Awakened", 0xFFFF8C00);
    /*ATTACK(TextFormatting.RED,"Attack", 0xFFFF2020),
    DEFENSE(TextFormatting.BLUE,"Defense", 0xFF4040FF),
    PURSUIT(TextFormatting.PURPLE,"Pursuit", 0xFFFF40FF),*/


    private final TextFormatting chatColor;
    private final String name;
    private final int color;
    Mode(TextFormatting chatColor, String name, int color){
        this.chatColor = chatColor;
        this.name = name;
        this.color = color;
    }

    public TextFormatting getChatColor(){
        return chatColor;
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

    public String getName() {
        return name;
    }
}
