package sunsetsatellite.signalindustries.abilities.trigger;





public abstract class TriggerBaseAbility {
    public String name;
    public int cost;
    public int cooldown;
    public int effectTime;

    public TriggerBaseAbility(String name, int cost, int cooldown) {
        this.name = name;
        this.cost = cost;
        this.cooldown = cooldown;
        this.effectTime = 0;
    }

    public TriggerBaseAbility(String name, int cost, int cooldown, int effectTime) {
        this.name = name;
        this.cost = cost;
        this.cooldown = cooldown;
        this.effectTime = effectTime;
    }

    public abstract void activate(int x, int y, int z, EntityPlayer player, World world, ItemStack stack);
    public abstract void activate(EntityPlayer player, World world, ItemStack stack);
}
