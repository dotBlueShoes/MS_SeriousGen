package net.minecrell.nostalgia_gen;

import net.minecraft.world.biome.Biome;

public interface NostalgiaGenBiome {
   String name();

   Biome getHandle();

   void setHandle(Biome var1);
}
