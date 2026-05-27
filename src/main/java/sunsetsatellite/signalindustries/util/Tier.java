package sunsetsatellite.signalindustries.util;


import net.minecraft.core.net.command.TextFormatting;
import net.minecraft.core.util.helper.Color;

public enum Tier {
    PROTOTYPE(TextFormatting.LIGHT_GRAY, 0xFF808080, 0xFFFFFFFF, "0 (Prototype)"),
    BASIC(TextFormatting.WHITE, 0xFFFFFFFF, 0xFFFF8080, "I (Basic)"),
    REINFORCED(TextFormatting.RED, 0xFFFF2020, 0xFFFF0000, "II (Reinforced)"),
    AWAKENED(TextFormatting.ORANGE, 0xFFFF8C00, 0xFFFFA500, "III (Awakened)"),
    INFINITE(TextFormatting.MAGENTA, 0xFFFF00FF, 0xFFFF00FF, "INF (Infinite)");

    private final TextFormatting textColor;
    private final int color;
	private final int altColor;
    private final String rank;

    Tier(TextFormatting textColor, int color, int altColor, String rank) {
        this.textColor = textColor;
        this.color = color;
		this.altColor = altColor;
        this.rank = rank;
    }

	public int getAltColor() {
		return altColor;
	}

	public int getAltColor(int alpha) {
		Color a = new Color().setRGBA(0, 0, 0, alpha);
		Color c = new Color().setARGB(altColor);
		a.setRGB(c);
		return a.getARGB();
	}

    public int getColor() {
        return color;
    }

    public int getColor(int alpha) {
        Color a = new Color().setRGBA(0, 0, 0, alpha);
        Color c = new Color().setARGB(color);
        a.setRGB(c);
        return a.getARGB();
    }

    public TextFormatting getTextColor() {
        return this.textColor;
    }

    public String getRank() {
        return rank;
    }
}
