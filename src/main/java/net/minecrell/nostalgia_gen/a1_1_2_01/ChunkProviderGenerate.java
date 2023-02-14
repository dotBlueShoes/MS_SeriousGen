package net.minecrell.nostalgia_gen.a1_1_2_01;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;
import net.minecrell.nostalgia_gen.a1_1_2_01.populator.WorldGenBigTree;
import net.minecrell.nostalgia_gen.a1_1_2_01.populator.WorldGenClay;
import net.minecrell.nostalgia_gen.a1_1_2_01.populator.WorldGenTrees;
import net.minecrell.nostalgia_gen.a1_1_2_01.populator.WorldGenerator;

import static net.minecrell.nostalgia_gen.NostalgiaGenMod.logger;

public class ChunkProviderGenerate implements IChunkGenerator {
	//private static final byte STANDARD_BIOME = (byte)Biome.getIdForBiome(Biomes.FOREST_HILLS);
	private static final byte STANDARD_BIOME = (byte)Biome.getIdForBiome(Biomes.JUNGLE);
	private static final byte BEACH_BIOME = (byte)Biome.getIdForBiome(Biomes.BEACH);
	private static final byte OCEAN_BIOME = (byte)Biome.getIdForBiome(Biomes.OCEAN);
	private Random rand;
	private NoiseGeneratorOctaves
		minLimitPerlinNoise,
		maxLimitPerlinNoise,
		mainPerlinNoise,
		surfaceNoise,
		stonePatchesNoise,
		unknownNoise,
		depthNoise;
	public NoiseGeneratorOctaves mobSpawnerNoise;
	private World worldObj;
	private double[] heightMap;
	private double[] sandNoise = new double[256];
	private double[] gravelNoise = new double[256];
	private double[] stoneNoise = new double[256];
	private MapGenBase caveGenerator = new MapGenCaves();
	double[] field_919_d;
	double[] field_918_e;
	double[] field_917_f;
	double[] field_916_g;
	double[] field_915_h;
	private final byte[] blockBiomes = new byte[256];

	public ChunkProviderGenerate(World world, long seed) {
		this.worldObj = world;
		this.rand = new Random(seed);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.surfaceNoise = new NoiseGeneratorOctaves(this.rand, 4);
        this.stonePatchesNoise = new NoiseGeneratorOctaves(this.rand, 4);
        this.unknownNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
	}

	public void generateTerrain(int chunkX, int chunkZ, ChunkPrimer chunk) {

		byte baseLevel = 64, times = 4;
		int var6 = times + 1;
		byte var7 = 17;

		this.heightMap = this.generateHeightMap(this.heightMap, chunkX * times, 0, chunkZ * times, var6, var7, var6);

		for (int x4 = 0; x4 < times; ++x4) {
			for (int z4 = 0; z4 < times; ++z4) {
				for (int height = 0; height < 16; ++height) {
					double var12 = 0.125;

                    double var14 = this.heightMap[((x4 + 0) * var6 + z4 + 0) * var7 + height + 0];
                    double var16 = this.heightMap[((x4 + 0) * var6 + z4 + 1) * var7 + height + 0];
                    double var18 = this.heightMap[((x4 + 1) * var6 + z4 + 0) * var7 + height + 0];
                    double var20 = this.heightMap[((x4 + 1) * var6 + z4 + 1) * var7 + height + 0];
                    double var22 = (this.heightMap[((x4 + 0) * var6 + z4 + 0) * var7 + height + 1] - var14) * var12;
                    double var24 = (this.heightMap[((x4 + 0) * var6 + z4 + 1) * var7 + height + 1] - var16) * var12;
                    double var26 = (this.heightMap[((x4 + 1) * var6 + z4 + 0) * var7 + height + 1] - var18) * var12;
                    double var28 = (this.heightMap[((x4 + 1) * var6 + z4 + 1) * var7 + height + 1] - var20) * var12;

					for (int var30 = 0; var30 < 8; ++var30) {
                        double var31 = 0.25;
                        double var33 = var14;
                        double var35 = var16;
                        double var37 = (var18 - var14) * var31;
                        double var39 = (var20 - var16) * var31;

                        for (int var41 = 0; var41 < 4; ++var41) {
                            int x = var41 + x4 * 4;
                            int y = height * 8 + var30;
                            int z = z4 * 4;
                            double var44 = 0.25;
                            double var46 = var33;
                            double var48 = (var35 - var33) * var44;

                            for (int var50 = 0; var50 < 4; ++var50) {
                                Block block = null;

                                if (height * 8 + var30 < baseLevel) {
                                   block = Blocks.WATER;
                                }

                                if (var46 > 0.0) {
                                   block = Blocks.STONE;
                                }

                                if (block != null) {
                                   chunk.setBlockState(x, y, z, ((Block)block).getDefaultState());
                                }

                                ++z;
                                var46 += var48;
							}

                        var33 += var37;
                        var35 += var39;
                        }

                    var14 += var22;
                    var16 += var24;
                    var18 += var26;
                    var20 += var28;
                    }
				}
            }
        }

	}

   public void replaceBlocks(int chunkX, int chunkZ, ChunkPrimer chunk) {
      byte baseLevel = 64;
      double var5 = 0.03125;

      this.sandNoise = this.surfaceNoise.generateNoiseOctaves(this.sandNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0, 16, 16, 1, var5, var5, 1.0);
      this.gravelNoise = this.surfaceNoise.generateNoiseOctaves(this.gravelNoise, (double)(chunkZ * 16), 109.0134, (double)(chunkX * 16), 16, 1, 16, var5, 1.0, var5);
      this.stoneNoise = this.stonePatchesNoise.generateNoiseOctaves(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0, 16, 16, 1, var5 * 2.0, var5 * 2.0, var5 * 2.0);

      for (int x = 0; x < 16; ++x) {
         for (int z = 0; z < 16; ++z) {
            boolean sand = this.sandNoise[x + z * 16] + this.rand.nextDouble() * 0.2 > 0.0;
            boolean gravel = this.gravelNoise[x + z * 16] + this.rand.nextDouble() * 0.2 > 3.0;
            int stone = (int)(this.stoneNoise[x + z * 16] / 3.0 + 3.0 + this.rand.nextDouble() * 0.25);
            int var12 = -1;
            Block topBlock = Blocks.GRASS;
            Block fillerBlock = Blocks.DIRT;
            boolean top = true;
            boolean water = false;

            for (int y = 127; y >= 0; --y) {
               if (y <= 0 + this.rand.nextInt(6) - 1) {
                  chunk.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
               } else {
                  Block block = chunk.getBlockState(x, y, z).getBlock();
                  if (block == Blocks.AIR) {
                     var12 = -1;
                  } else if (block != Blocks.STONE) {
                     if (block == Blocks.WATER) {
                        water = true;
                     }
                  } else if (var12 == -1) {
                     if (stone <= 0) {
                        topBlock = Blocks.AIR;
                        fillerBlock = Blocks.STONE;
                     } else if (y >= baseLevel - 4 && y <= baseLevel + 1) {
                        topBlock = Blocks.GRASS;
                        fillerBlock = Blocks.DIRT;
                        if (gravel) {
                           topBlock = Blocks.AIR;
                        }

                        if (gravel) {
                           fillerBlock = Blocks.GRAVEL;
                        }

                        if (sand) {
                           topBlock = Blocks.SAND;
                        }

                        if (sand) {
                           fillerBlock = Blocks.SAND;
                        }
                     }

                     if (y < baseLevel && topBlock == Blocks.AIR) {
                        topBlock = Blocks.WATER;
                     }

                     if (top) {
                        top = false;
                        if (fillerBlock != Blocks.SAND && fillerBlock != Blocks.GRAVEL) {
                           if (water) {
                              if (y >= baseLevel - 4) {
                                 this.blockBiomes[z << 4 | x] = BEACH_BIOME;
                              } else {
                                 this.blockBiomes[z << 4 | x] = OCEAN_BIOME;
                              }
                           } else {
                              this.blockBiomes[z << 4 | x] = STANDARD_BIOME;
                           }
                        } else {
                           this.blockBiomes[z << 4 | x] = BEACH_BIOME;
                        }
                     }

                     var12 = stone;
                     if (y >= baseLevel - 1) {
                        chunk.setBlockState(x, y, z, ((Block)topBlock).getDefaultState());
                     } else {
                        chunk.setBlockState(x, y, z, ((Block)fillerBlock).getDefaultState());
                     }
                  } else if (var12 > 0) {
                     --var12;
                     chunk.setBlockState(x, y, z, ((Block)fillerBlock).getDefaultState());
                  }
               }
            }
         }
      }

   }

	public Chunk generateChunk(int x, int z) {
		ChunkPrimer chunkPrimer = new ChunkPrimer();

		this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
		this.generateTerrain(x, z, chunkPrimer);
		this.replaceBlocks(x, z, chunkPrimer);
		this.caveGenerator.generate(this.worldObj, x, z, chunkPrimer);

		Chunk chunk = new Chunk(this.worldObj, chunkPrimer, x, z);
		System.arraycopy(this.blockBiomes, 0, chunk.getBiomeArray(), 0, this.blockBiomes.length);
		chunk.generateSkylightMap();

		return chunk;
   }

   private double[] generateHeightMap(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {

      if (var1 == null) {
         var1 = new double[var5 * var6 * var7];
      }

      double var8 = 684.412;
      double var10 = 684.412;

      this.field_916_g = this.unknownNoise.generateNoiseOctaves(this.field_916_g, (double)var2, (double)var3, (double)var4, var5, 1, var7, 1.0, 0.0, 1.0);
      this.field_915_h = this.depthNoise.generateNoiseOctaves(this.field_915_h, (double)var2, (double)var3, (double)var4, var5, 1, var7, 100.0, 0.0, 100.0);
      this.field_919_d = this.mainPerlinNoise.generateNoiseOctaves(this.field_919_d, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8 / 80.0, var10 / 160.0, var8 / 80.0);
      this.field_918_e = this.minLimitPerlinNoise.generateNoiseOctaves(this.field_918_e, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8, var10, var8);
      this.field_917_f = this.maxLimitPerlinNoise.generateNoiseOctaves(this.field_917_f, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8, var10, var8);

	  int var12 = 0;
      int var13 = 0;

      for(int var14 = 0; var14 < var5; ++var14) {
         for(int var15 = 0; var15 < var7; ++var15) {
            double var16 = (this.field_916_g[var13] + 256.0) / 512.0;
            if (var16 > 1.0) {
               var16 = 1.0;
            }

            double var18 = 0.0;
            double var20 = this.field_915_h[var13] / 8000.0;
            if (var20 < 0.0) {
               var20 = -var20;
            }

            var20 = var20 * 3.0 - 3.0;
            if (var20 < 0.0) {
               var20 /= 2.0;
               if (var20 < -1.0) {
                  var20 = -1.0;
               }

               var20 /= 1.4;
               var20 /= 2.0;
               var16 = 0.0;
            } else {
               if (var20 > 1.0) {
                  var20 = 1.0;
               }

               var20 /= 6.0;
            }

            var16 += 0.5;
            var20 = var20 * (double)var6 / 16.0;
            double var22 = (double)var6 / 2.0 + var20 * 4.0;
            ++var13;

            for(int var24 = 0; var24 < var6; ++var24) {
               double var25 = 0.0;
               double var27 = ((double)var24 - var22) * 12.0 / var16;
               if (var27 < 0.0) {
                  var27 *= 4.0;
               }

               double var29 = this.field_918_e[var12] / 512.0;
               double var31 = this.field_917_f[var12] / 512.0;
               double var33 = (this.field_919_d[var12] / 10.0 + 1.0) / 2.0;
               if (var33 < 0.0) {
                  var25 = var29;
               } else if (var33 > 1.0) {
                  var25 = var31;
               } else {
                  var25 = var29 + (var31 - var29) * var33;
               }

               var25 -= var27;
               double var45;
               if (var24 > var6 - 4) {
                  var45 = (double)((float)(var24 - (var6 - 4)) / 3.0F);
                  var25 = var25 * (1.0 - var45) + -10.0 * var45;
               }

               if ((double)var24 < var18) {
                  var45 = (var18 - (double)var24) / 4.0;
                  if (var45 < 0.0) {
                     var45 = 0.0;
                  }

                  if (var45 > 1.0) {
                     var45 = 1.0;
                  }

                  var25 = var25 * (1.0 - var45) + -10.0 * var45;
               }

               var1[var12] = var25;
               ++var12;
            }
         }
      }

      return var1;
   }

    public void populate(int chunkX, int chunkZ) {

        //logger.info("ChunkProviderGenerate:populate()");

        BlockFalling.fallInstantly = true;
        int minX = chunkX * 16;
        int minZ = chunkZ * 16;

        this.rand.setSeed(this.worldObj.getSeed());
        long var6 = this.rand.nextLong() / 2L * 2L + 1L;
        long var8 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)chunkX * var6 + (long)chunkZ * var8 ^ this.worldObj.getSeed());
        double var10 = 0.25;

        // Dungeons
        //for (int i = 0; i < 8; ++i) {
        //    int x = minX + this.rand.nextInt(16) + 8;
        //    int y = this.rand.nextInt(128);
        //    int z = minZ + this.rand.nextInt(16) + 8;
        //    //(new WorldGenDungeons()).generate(this.worldObj, this.rand, x, y, z);
        //}
//
        // Clay , that's rare...
        for(int i = 0; i < 10; ++i) {
            int x = minX + this.rand.nextInt(16);
            int y = this.rand.nextInt(128);
            int z = minZ + this.rand.nextInt(16);
            (new WorldGenClay(32)).generate(this.worldObj, this.rand, x, y, z);
        }
//
      //for(var27 = 0; var27 < 20; ++var27) {
      //   x = minX + this.rand.nextInt(16);
      //   i = this.rand.nextInt(128);
      //    something = maxX + this.rand.nextInt(16);
      //   (new WorldGenMinable(Blocks.DIRT, 32)).generate(this.worldObj, this.rand, x, i, something);
      //}
//
      //for(var27 = 0; var27 < 10; ++var27) {
      //   x = minX + this.rand.nextInt(16);
      //   i = this.rand.nextInt(128);
      //    something = maxX + this.rand.nextInt(16);
      //   (new WorldGenMinable(Blocks.GRAVEL, 32)).generate(this.worldObj, this.rand, x, i, something);
      //}
//
      //for(var27 = 0; var27 < 20; ++var27) {
      //   x = minX + this.rand.nextInt(16);
      //   i = this.rand.nextInt(128);
      //    something = maxX + this.rand.nextInt(16);
      //   (new WorldGenMinable(Blocks.COAL_ORE, 16)).generate(this.worldObj, this.rand, x, i, something);
      //}
//
      //for(var27 = 0; var27 < 20; ++var27) {
      //   x = minX + this.rand.nextInt(16);
      //   i = this.rand.nextInt(64);
      //    something = maxX + this.rand.nextInt(16);
      //   (new WorldGenMinable(Blocks.IRON_ORE, 8)).generate(this.worldObj, this.rand, x, i, something);
      //}
//
      //for(var27 = 0; var27 < 2; ++var27) {
      //   x = minX + this.rand.nextInt(16);
      //   i = this.rand.nextInt(32);
      //    something = maxX + this.rand.nextInt(16);
      //   (new WorldGenMinable(Blocks.GOLD_ORE, 8)).generate(this.worldObj, this.rand, x, i, something);
      //}
//
      //for(var27 = 0; var27 < 8; ++var27) {
      //   x = minX + this.rand.nextInt(16);
      //   i = this.rand.nextInt(16);
      //    something = maxX + this.rand.nextInt(16);
      //   (new WorldGenMinable(Blocks.REDSTONE_ORE, 7)).generate(this.worldObj, this.rand, x, i, something);
      //}
//
      //for(var27 = 0; var27 < 1; ++var27) {
      //   x = minX + this.rand.nextInt(16);
      //   i = this.rand.nextInt(16);
      //    something = maxX + this.rand.nextInt(16);
      //   (new WorldGenMinable(Blocks.DIAMOND_ORE, 7)).generate(this.worldObj, this.rand, x, i, something);
      //}

	    // That part is crazy.
	    //  It does not generate mob spawners...
	    //  It seems that all this magic was done to get tree_count per chunk
	    final float constA =  0.5f;
	    int count = (int)((
			this.mobSpawnerNoise.func_806_a((double)minX * constA, (double)minZ * constA) / 8.0 +
			this.rand.nextDouble() * 4.0 + 4.0
	        ) / 3.0);
		if (count < 0) {
		    count = 0;
		}
	    if (this.rand.nextInt(10) == 0) {
		    ++count;
	    }

		logger.info("Count: " + count);

		//if (this.rand.nextInt(10) == 0) {
		//	++var27;
		//}

	    // Generate normal or big_tree
        WorldGenerator treeGen;
        if (this.rand.nextInt(10) == 0) {
			treeGen = new WorldGenBigTree();
        } else {
	        treeGen = new WorldGenTrees();
        }

		//logger.info(treeGen);

        for(int i = 0; i < count; ++i) {
           int x = minX + this.rand.nextInt(16) + 8;
           int z = minZ + this.rand.nextInt(16) + 8;
           ((WorldGenerator)treeGen).func_517_a(1.0, 1.0, 1.0);
		   //logger.info("hello");
           ((WorldGenerator)treeGen).generate(this.worldObj, this.rand, x, NostalgiaGenHelper.getHeightValue(this.worldObj, x, z), z);
        }

      //int z;
      //for(i = 0; i < 2; ++i) {
      //   x = minX + this.rand.nextInt(16) + 8;
      //   int y = this.rand.nextInt(128);
      //   z = maxX + this.rand.nextInt(16) + 8;
      //   (new WorldGenFlowers(Blocks.YELLOW_FLOWER)).generate(this.worldObj, this.rand, x, y, z);
      //}
//
      //if (this.rand.nextInt(2) == 0) {
      //   i = minX + this.rand.nextInt(16) + 8;
      //   x = this.rand.nextInt(128);
      //   int y = maxX + this.rand.nextInt(16) + 8;
      //   (new WorldGenFlowers(Blocks.RED_FLOWER)).generate(this.worldObj, this.rand, i, x, y);
      //}
//
      //if (this.rand.nextInt(4) == 0) {
      //   i = minX + this.rand.nextInt(16) + 8;
      //   x = this.rand.nextInt(128);
      //   int y = maxX + this.rand.nextInt(16) + 8;
      //   (new WorldGenFlowers(Blocks.BROWN_MUSHROOM)).generate(this.worldObj, this.rand, i, x, y);
      //}
//
      //if (this.rand.nextInt(8) == 0) {
      //   i = minX + this.rand.nextInt(16) + 8;
      //   x = this.rand.nextInt(128);
      //   int y = maxX + this.rand.nextInt(16) + 8;
      //   (new WorldGenFlowers(Blocks.RED_MUSHROOM)).generate(this.worldObj, this.rand, i, x, y);
      //}
//
      //for(i = 0; i < 10; ++i) {
      //   x = minX + this.rand.nextInt(16) + 8;
      //   int y = this.rand.nextInt(128);
      //   z = maxX + this.rand.nextInt(16) + 8;
      //   (new WorldGenReed()).generate(this.worldObj, this.rand, x, y, z);
      //}
//
      //for(i = 0; i < 1; ++i) {
      //   x = minX + this.rand.nextInt(16) + 8;
      //   int y = this.rand.nextInt(128);
      //   z = maxX + this.rand.nextInt(16) + 8;
      //   (new WorldGenCactus()).generate(this.worldObj, this.rand, x, y, z);
      //}
//
      //for(i = 0; i < 50; ++i) {
      //   x = minX + this.rand.nextInt(16) + 8;
      //   int y = this.rand.nextInt(this.rand.nextInt(120) + 8);
      //   z = maxX + this.rand.nextInt(16) + 8;
      //   (new WorldGenLiquids(Blocks.FLOWING_WATER)).generate(this.worldObj, this.rand, x, y, z);
      //}
//
      //for(i = 0; i < 20; ++i) {
      //   x = minX + this.rand.nextInt(16) + 8;
      //   int y = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
      //   z = maxX + this.rand.nextInt(16) + 8;
      //   (new WorldGenLiquids(Blocks.FLOWING_LAVA)).generate(this.worldObj, this.rand, x, y, z);
      //}
//
      BlockFalling.fallInstantly = false;
   }

   public boolean generateStructures(Chunk chunkIn, int x, int z) {
      return false;
   }

   public List getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
      return this.worldObj.getBiome(pos).getSpawnableList(creatureType);
   }

   @Nullable
   public BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored) {
      return null;
   }

   public void recreateStructures(Chunk chunkIn, int x, int z) {
   }

   public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
      return false;
   }

}
