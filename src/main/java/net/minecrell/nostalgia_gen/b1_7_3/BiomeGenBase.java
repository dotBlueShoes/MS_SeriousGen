package net.minecrell.nostalgia_gen.b1_7_3;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecrell.nostalgia_gen.NostalgiaGenBiome;
import net.minecrell.nostalgia_gen.b1_7_3.populator.WorldGenBigTree;
import net.minecrell.nostalgia_gen.b1_7_3.populator.WorldGenForest;
import net.minecrell.nostalgia_gen.b1_7_3.populator.WorldGenTaiga1;
import net.minecrell.nostalgia_gen.b1_7_3.populator.WorldGenTaiga2;
import net.minecrell.nostalgia_gen.b1_7_3.populator.WorldGenTrees;
import net.minecrell.nostalgia_gen.b1_7_3.populator.WorldGenerator;

public enum BiomeGenBase implements NostalgiaGenBiome {
   rainforest(Biomes.JUNGLE) {
      public WorldGenerator getRandomWorldGenForTrees(Random random) {
         return (WorldGenerator)(random.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
      }
   },
   swampland(Biomes.SWAMPLAND),
   seasonalForest(Biomes.FOREST),
   forest(Biomes.FOREST) {
      public WorldGenerator getRandomWorldGenForTrees(Random random) {
         return (WorldGenerator)(random.nextInt(5) == 0 ? new WorldGenForest() : (random.nextInt(3) == 0 ? new WorldGenBigTree() : new WorldGenTrees()));
      }
   },
   savanna(Biomes.SAVANNA),
   shrubland(Biomes.SAVANNA),
   taiga(Biomes.COLD_TAIGA) {
      public WorldGenerator getRandomWorldGenForTrees(Random random) {
         return (WorldGenerator)(random.nextInt(3) == 0 ? new WorldGenTaiga1() : new WorldGenTaiga2());
      }
   },
   desert(Biomes.DESERT, Blocks.SAND, Blocks.SAND),
   plains(Biomes.PLAINS),
   tundra(Biomes.ICE_PLAINS);

   public Biome handle;
   public final Block topBlock;
   public final Block fillerBlock;
   private static final BiomeGenBase[] biomeLookupTable = new BiomeGenBase[4096];

   private BiomeGenBase(Biome handle) {
      this(handle, Blocks.GRASS, Blocks.DIRT);
   }

   private BiomeGenBase(Biome handle, Block topBlock, Block fillerBlock) {
      this.handle = handle;
      this.topBlock = topBlock;
      this.fillerBlock = fillerBlock;
   }

   public Biome getHandle() {
      return this.handle;
   }

   public void setHandle(Biome handle) {
      this.handle = handle;
   }

   public WorldGenerator getRandomWorldGenForTrees(Random random) {
      //return (WorldGenerator)(random.nextInt(10) == 0 ? new WorldGenBigTree() : new WorldGenTrees());
	   return new WorldGenTrees();
   }

   public static void generateBiomeLookup() {
      for(int var0 = 0; var0 < 64; ++var0) {
         for(int var1 = 0; var1 < 64; ++var1) {
            biomeLookupTable[var0 + var1 * 64] = getBiome((float)var0 / 63.0F, (float)var1 / 63.0F);
         }
      }

   }

   public static BiomeGenBase getBiomeFromLookup(double var0, double var2) {
      int var4 = (int)(var0 * 63.0);
      int var5 = (int)(var2 * 63.0);
      return biomeLookupTable[var4 + var5 * 64];
   }

   public static BiomeGenBase getBiome(float var0, float var1) {
      var1 *= var0;
      return var0 < 0.1F ? tundra : (var1 < 0.2F ? (var0 < 0.5F ? tundra : (var0 < 0.95F ? savanna : desert)) : (var1 > 0.5F && var0 < 0.7F ? swampland : (var0 < 0.5F ? taiga : (var0 < 0.97F ? (var1 < 0.35F ? shrubland : forest) : (var1 < 0.45F ? plains : (var1 < 0.9F ? seasonalForest : rainforest))))));
   }

   // $FF: synthetic method
   BiomeGenBase(Biome x2, Object x3) {
      this(x2);
   }

   static {
      generateBiomeLookup();
   }
}
