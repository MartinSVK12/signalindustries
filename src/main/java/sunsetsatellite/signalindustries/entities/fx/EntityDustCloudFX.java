package sunsetsatellite.signalindustries.entities.fx;


import net.minecraft.client.entity.fx.EntityFX;
import net.minecraft.client.render.Tessellator;
import net.minecraft.core.world.World;
import sunsetsatellite.signalindustries.SignalIndustries;

public class EntityDustCloudFX extends EntityFX {
	private int timeSinceStart = 0;
	private int maximumTime = 0;
	private float red = 1;
	private float green = 0;
	private float blue = 0;

	public EntityDustCloudFX(World world1, double d2, double d4, double d6, double d8, double d10, double d12, float red, float green, float blue) {
		super(world1, d2, d4, d6, 0.0D, 0.0D, 0.0D);
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.maximumTime = 8;
	}

	public EntityDustCloudFX(World world1, double d2, double d4, double d6, double d8, double d10, double d12) {
		super(world1, d2, d4, d6, 0.0D, 0.0D, 0.0D);
		this.maximumTime = 8;
	}

	public void renderParticle(Tessellator tessellator1, float f2, float f3, float f4, float f5, float f6, float f7) {
	}

	@Override
	public void tick() {
		for(int i1 = 0; i1 < 30; ++i1) {
			double d2 = this.x + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
			double d4 = this.y + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
			double d6 = this.z + (this.random.nextDouble() - this.random.nextDouble()) * 4.0D;
			SignalIndustries.spawnParticle(new EntityColorParticleFX(world, d2, d4, d6, 0, 0, 0, 1.0f, red, green, blue));
		}

		++this.timeSinceStart;
		if(this.timeSinceStart == this.maximumTime) {
			this.remove();
		}

	}

	public int getFXLayer() {
		return 1;
	}
}
