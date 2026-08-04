package sunsetsatellite.signalindustries.api.impl.btatweaker;

import turing.btatweaker.BTATweakerEntrypoint;
import turing.btatweaker.IBTATweaker;
import turing.tmb.plugin.TMBLib;

public class BTATweakerSIPlugin implements BTATweakerEntrypoint {
	public static final SILuaLib lib = new SILuaLib();

	@Override
	public void initPlugin(IBTATweaker registry) {
		registry.addModLibrary(lib);
	}
}
