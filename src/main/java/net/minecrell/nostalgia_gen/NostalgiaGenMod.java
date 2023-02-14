package net.minecrell.nostalgia_gen;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = "nostalgia_gen",
    name = "NostalgiaGen",
    acceptedMinecraftVersions = "[1.12.2]"
)
public class NostalgiaGenMod {

    public static Logger logger;

    @EventHandler
    public void onPreInitialize(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        WorldTypeAlpha.register();
        WorldTypeBeta.register();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        //logger.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName()); // some example code
    }

}
