package net.minecrell.nostalgia_gen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecrell.nostalgia_gen.a1_1_2_01.ChunkProviderGenerate;

public class WorldTypeAlpha extends WorldType {
   public static void register() {
      new WorldTypeAlpha();
   }

   private WorldTypeAlpha() {
      super("alpha_1.1.2_01");
   }

   protected WorldTypeAlpha(String name) {
      super(name);
   }

   public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
      return new ChunkProviderGenerate(world, world.getSeed());
   }
}
