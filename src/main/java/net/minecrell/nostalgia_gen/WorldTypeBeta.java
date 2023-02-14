package net.minecrell.nostalgia_gen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecrell.nostalgia_gen.b1_7_3.ChunkProviderGenerate;

public class WorldTypeBeta extends WorldType {
   public static void register() {
      new WorldTypeBeta();
   }

   private WorldTypeBeta() {
      super("beta_1.7.3");
   }

   protected WorldTypeBeta(String name) {
      super(name);
   }

   public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
      return new ChunkProviderGenerate(world, world.getSeed());
   }
}
