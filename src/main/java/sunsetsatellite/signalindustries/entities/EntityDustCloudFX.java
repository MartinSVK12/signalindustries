package sunsetsatellite.signalindustries.entities;

import net.minecraft.src.EntityFX;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class EntityDustCloudFX extends EntityFX {
	private int timeSinceStart = 0;
	private int maximumTime = 0;

	public EntityDustCloudFX(World world1, double d2, double d4, double d6, double d8, double d10, double d12) {
		super(world1, d2, d4, d6, 0.0D, 0.0D, 0.0D);
		this.maximumTime = 8;
	}

	public void renderParticle(Tessellator tessellator1, float f2, float f3, float f4, float f5, float f6, float f7) {
	}

	public void onUpdate() {
		for(int i1 = 0; i1 < 30; ++i1) {
			double d2 = this.posX + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
			double d4 = this.posY + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
			double d6 = this.posZ + (this.rand.nextDouble() - this.rand.nextDouble()) * 4.0D;
			this.worldObj.spawnParticle("reddust", d2, d4, d6, (float)this.timeSinceStart / (float)this.maximumTime, 0.0D, 0.0D);
		}

		++this.timeSinceStart;
		if(this.timeSinceStart == this.maximumTime) {
			this.setEntityDead();
		}

	}

	public int getFXLayer() {
		return 1;
	}
}
