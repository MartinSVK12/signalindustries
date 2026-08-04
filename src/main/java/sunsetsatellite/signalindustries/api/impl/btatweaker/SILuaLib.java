package sunsetsatellite.signalindustries.api.impl.btatweaker;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import sunsetsatellite.signalindustries.SignalIndustries;
import turing.btatweaker.api.ModLibrary;
import turing.docs.Library;

import java.util.List;

@Library(value = "mods.signalindustries", className = "Signal Industries")
public class SILuaLib extends ModLibrary {
	@Override
	public void setupLib(LuaTable t, LuaValue env) {

	}

	@Override
	public List<String> getAliases() {
		return List.of(SignalIndustries.MOD_ID, "si");
	}
}
