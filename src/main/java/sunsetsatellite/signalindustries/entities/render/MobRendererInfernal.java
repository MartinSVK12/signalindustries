package sunsetsatellite.signalindustries.entities.render;

import net.minecraft.client.render.entity.MobRendererBiped;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.useless.dragonfly.models.entity.StaticEntityModel;
import sunsetsatellite.signalindustries.entities.MobInfernal;

public class MobRendererInfernal extends MobRendererBiped<MobInfernal> {
	public MobRendererInfernal(float shadowSize) {
		super(shadowSize);
	}

	@Override
	protected @Nullable StaticEntityModel getActiveModel(@NonNull MobInfernal mobInfernal) {
		return this.getModel("main");
	}
}
