package sunsetsatellite.signalindustries.util;

public class RecipeProperties {
    public Tier tier;
    public int cost;
    public int ticks;
    public int id = 0;
    public float chance = 1;

    public boolean thisTierOnly;

    public RecipeProperties(){}

    public RecipeProperties(Tier tier){
        this.tier = tier;
    }

    public RecipeProperties(int ticks, int cost, Tier tier, boolean thisTierOnly){
        this.tier = tier;
        this.ticks = ticks;
        this.cost = cost;
        this.thisTierOnly = thisTierOnly;
    }

    public RecipeProperties(int ticks, int cost, int id, Tier tier, boolean thisTierOnly){
        this.tier = tier;
        this.ticks = ticks;
        this.cost = cost;
        this.id = id;
        this.thisTierOnly = thisTierOnly;
    }

    public RecipeProperties(int ticks, Tier tier, boolean thisTierOnly){
        this.tier = tier;
        this.ticks = ticks;
        this.thisTierOnly = thisTierOnly;
    }

    public RecipeProperties setChance(float chance){
        this.chance = chance;
        return this;
    }

    public boolean isCorrectTier(Tier tier){
        return thisTierOnly ? this.tier == tier : tier.ordinal() >= this.tier.ordinal();
    }
}
