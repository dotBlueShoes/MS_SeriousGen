package dotblueshoes.serious_gen;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
	modid = SeriousGenMod.MODID,
	name = SeriousGenMod.NAME,
	acceptedMinecraftVersions = SeriousGenMod.MC_VERSION
)
public class SeriousGenMod {

	final static String
		MODID = "serious_gen",
		NAME = "SeriousGen",
		MC_VERSION = "[1.12.2]";

	public static Logger logger;

	@Mod.EventHandler
	public void onPreInitialize(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		WorldTypeSerious.register();
	}
}
