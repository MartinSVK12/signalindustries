package sunsetsatellite.signalindustries.gui.guidebook.pages.wiki;

import net.minecraft.client.gui.guidebook.GuidebookPage;
import net.minecraft.client.gui.guidebook.GuidebookSection;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.client.render.RenderEngine;
import net.minecraft.core.lang.I18n;

import java.util.ArrayList;
import java.util.List;

public class IntroPage extends GuidebookPage {
    private String[] stringLines;
    public final String string;

    public IntroPage(GuidebookSection section, String string) {
        super(section);
        this.string = string;
    }

    @Override
    protected void renderForeground(RenderEngine re, FontRenderer fr, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (stringLines == null) {
            stringLines = createDescLines(fr, string);
        }

        int yOffset = y + 8;

        // Display description
        for (String descLine : stringLines) {
            drawStringNoShadow(fr, descLine, x + 8, yOffset, 0x505050);
            yOffset += 10;
        }
    }

    private static String[] createDescLines(FontRenderer fr, String languageKey) {
        String[] words = I18n.getInstance().translateKey(languageKey).split(" ");
        List<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if (fr.getStringWidth(line + " " + word) > 142) {
                lines.add(line.toString());
                line = new StringBuilder();
            }
            if (word.contains("\n")) {
                String safeWord = word.replace("\r", "");
                String[] wordParts = safeWord.split("\n");
                for (int i = 0; i < wordParts.length; i++) {
                    if (i > 0) {
                        lines.add(line.toString());
                        line = new StringBuilder();
                    }
                    line.append(wordParts[i]).append(" ");
                }
            } else {
                line.append(word).append(" ");
            }
        }
        lines.add(line.toString());
        return lines.toArray(new String[0]);
    }
}
