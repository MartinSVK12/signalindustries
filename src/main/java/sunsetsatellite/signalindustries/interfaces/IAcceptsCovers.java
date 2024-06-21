package sunsetsatellite.signalindustries.interfaces;

import net.minecraft.core.entity.player.EntityPlayer;
import sunsetsatellite.catalyst.core.util.Direction;
import sunsetsatellite.signalindustries.covers.CoverBase;

import java.util.Map;

public interface IAcceptsCovers {

    boolean installCover(Direction dir, CoverBase cover, EntityPlayer player);

    boolean installCover(Direction dir, CoverBase cover);

    boolean removeCover(Direction dir, CoverBase cover, EntityPlayer player);

    boolean removeCover(Direction dir, CoverBase cover);

    boolean removeCover(Direction dir, EntityPlayer player);

    boolean removeCover(Direction dir);

    boolean hasCover(Direction dir, Class<? extends CoverBase> cover);

    boolean hasCoverAnywhere(Class<? extends CoverBase> cover);

    Map<Direction, CoverBase> getCovers();
}
