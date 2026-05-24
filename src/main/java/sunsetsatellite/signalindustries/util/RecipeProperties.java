package sunsetsatellite.signalindustries.util;

import net.minecraft.core.world.Dimension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeProperties {
    public Tier tier;
    public int cost;
    public int ticks;
    public int id = 0;
    public float chance = 1;

    public boolean thisTierOnly;
    public boolean consumeContainers = false;

    public List<Dimension> allowedDimensions;

    public Map<String, Object> auxData = new HashMap<>();

    public RecipeProperties() {
    }

    public RecipeProperties(Tier tier) {
        this.tier = tier;
    }

    public RecipeProperties(int ticks, int cost, Tier tier, boolean thisTierOnly) {
        this.tier = tier;
        this.ticks = ticks;
        this.cost = cost;
        this.thisTierOnly = thisTierOnly;
    }

    public RecipeProperties(int ticks, int cost, int id, Tier tier, boolean thisTierOnly) {
        this.tier = tier;
        this.ticks = ticks;
        this.cost = cost;
        this.id = id;
        this.thisTierOnly = thisTierOnly;
    }

    public RecipeProperties(int ticks, Tier tier, boolean thisTierOnly) {
        this.tier = tier;
        this.ticks = ticks;
        this.thisTierOnly = thisTierOnly;
    }

    public RecipeProperties setChance(float chance) {
        this.chance = chance;
        return this;
    }

    public RecipeProperties setConsumeContainers() {
        this.consumeContainers = true;
        return this;
    }

    public RecipeProperties setAllowedDimensions(List<Dimension> allowedDimensions) {
        this.allowedDimensions = allowedDimensions;
        return this;
    }

    public RecipeProperties addAuxData(String key, Object value) {
        auxData.put(key, value);
        return this;
    }

    public boolean isCorrectTier(Tier tier) {
        return thisTierOnly ? this.tier == tier : tier.ordinal() >= this.tier.ordinal();
    }
}
