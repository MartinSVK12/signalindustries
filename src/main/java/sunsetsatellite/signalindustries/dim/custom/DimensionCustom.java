package sunsetsatellite.signalindustries.dim.custom;

import net.minecraft.core.world.Dimension;

import static sunsetsatellite.signalindustries.SignalIndustries.langKey;

public class DimensionCustom extends Dimension {

    public CustomDimensionData data;

    public DimensionCustom(CustomDimensionData data) {
        super(langKey("custom"), Dimension.OVERWORLD, 1, null, data.getWorldType());
        this.data = data;
    }
}
