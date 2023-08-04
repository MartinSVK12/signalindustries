package sunsetsatellite.signalindustries.util;




public enum Mode {
    NORMAL(ChatColor.gray,"Normal", 0xFF808080),
    ATTACK(ChatColor.red,"Attack", 0xFFFF2020),
    DEFENSE(ChatColor.blue,"Defense", 0xFF4040FF),
    PURSUIT(ChatColor.purple,"Pursuit", 0xFFFF40FF),
    AWAKENED(ChatColor.orange,"Awakened", 0xFFFF8C00);

    private final ChatColor chatColor;
    private final String name;
    private final int color;
    Mode(ChatColor chatColor, String name, int color){
        this.chatColor = chatColor;
        this.name = name;
        this.color = color;
    }

    public ChatColor getChatColor(){
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
