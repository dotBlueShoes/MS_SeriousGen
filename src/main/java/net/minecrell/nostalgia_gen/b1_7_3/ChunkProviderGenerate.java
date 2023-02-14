package net.minecrell.nostalgia_gen.b1_7_3;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;
import net.minecrell.nostalgia_gen.b1_7_3.populator.*;

import static net.minecrell.nostalgia_gen.NostalgiaGenMod.logger;

public class ChunkProviderGenerate implements IChunkGenerator {
   private Random rand;
   private NoiseGeneratorOctaves field_912_k;
   private NoiseGeneratorOctaves field_911_l;
   private NoiseGeneratorOctaves field_910_m;
   private NoiseGeneratorOctaves field_909_n;
   private NoiseGeneratorOctaves field_908_o;
   public NoiseGeneratorOctaves field_922_a;
   public NoiseGeneratorOctaves field_921_b;
   public NoiseGeneratorOctaves mobSpawnerNoise;
   private World worldObj;
   private double[] field_4180_q;
   private double[] sandNoise = new double[256];
   private double[] gravelNoise = new double[256];
   private double[] stoneNoise = new double[256];
   private MapGenBase caveGenerator = new MapGenCaves();
   private BiomeGenBase[] biomesForGeneration;
   double[] field_4185_d;
   double[] field_4184_e;
   double[] field_4183_f;
   double[] field_4182_g;
   double[] field_4181_h;
   private double[] generatedTemperatures;
   private final WorldChunkManager chunkManager;

	WorldGenTrees worldGenTrees = new WorldGenTrees();
	WorldGenBigTree worldGenBigTree = new WorldGenBigTree();

	public ChunkProviderGenerate(World world, long seed) {
		this.worldObj = world;
		this.rand = new Random(seed);
		this.field_912_k = new NoiseGeneratorOctaves(this.rand, 16);
		this.field_911_l = new NoiseGeneratorOctaves(this.rand, 16);
		this.field_910_m = new NoiseGeneratorOctaves(this.rand, 8);
		this.field_909_n = new NoiseGeneratorOctaves(this.rand, 4);
		this.field_908_o = new NoiseGeneratorOctaves(this.rand, 4);
		this.field_922_a = new NoiseGeneratorOctaves(this.rand, 10);
		this.field_921_b = new NoiseGeneratorOctaves(this.rand, 16);
		this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.chunkManager = new WorldChunkManager(world);
	}

   public void generateTerrain(int chunkX, int chunkZ, ChunkPrimer chunk, double[] temperatures) {
      byte var6 = 4;
      byte var7 = 64;
      int var8 = var6 + 1;
      byte var9 = 17;
      int var10 = var6 + 1;
      this.field_4180_q = this.func_4061_a(this.field_4180_q, chunkX * var6, 0, chunkZ * var6, var8, var9, var10);

      for(int var11 = 0; var11 < var6; ++var11) {
         for(int var12 = 0; var12 < var6; ++var12) {
            for(int var13 = 0; var13 < 16; ++var13) {
               double var14 = 0.125;
               double var16 = this.field_4180_q[((var11 + 0) * var10 + var12 + 0) * var9 + var13 + 0];
               double var18 = this.field_4180_q[((var11 + 0) * var10 + var12 + 1) * var9 + var13 + 0];
               double var20 = this.field_4180_q[((var11 + 1) * var10 + var12 + 0) * var9 + var13 + 0];
               double var22 = this.field_4180_q[((var11 + 1) * var10 + var12 + 1) * var9 + var13 + 0];
               double var24 = (this.field_4180_q[((var11 + 0) * var10 + var12 + 0) * var9 + var13 + 1] - var16) * var14;
               double var26 = (this.field_4180_q[((var11 + 0) * var10 + var12 + 1) * var9 + var13 + 1] - var18) * var14;
               double var28 = (this.field_4180_q[((var11 + 1) * var10 + var12 + 0) * var9 + var13 + 1] - var20) * var14;
               double var30 = (this.field_4180_q[((var11 + 1) * var10 + var12 + 1) * var9 + var13 + 1] - var22) * var14;

               for(int var32 = 0; var32 < 8; ++var32) {
                  double var33 = 0.25;
                  double var35 = var16;
                  double var37 = var18;
                  double var39 = (var20 - var16) * var33;
                  double var41 = (var22 - var18) * var33;

                  for(int var43 = 0; var43 < 4; ++var43) {
                     int x = var43 + var11 * 4;
                     int y = var13 * 8 + var32;
                     int z = var12 * 4;
                     double var46 = 0.25;
                     double var48 = var35;
                     double var50 = (var37 - var35) * var46;

                     for(int var52 = 0; var52 < 4; ++var52) {
                        double var53 = temperatures[(var11 * 4 + var43) * 16 + var12 * 4 + var52];
                        Block block = null;
                        if (var13 * 8 + var32 < var7) {
                           if (var53 < 0.5 && var13 * 8 + var32 >= var7 - 1) {
                              block = Blocks.ICE;
                           } else {
                              block = Blocks.WATER;
                           }
                        }

                        if (var48 > 0.0) {
                           block = Blocks.STONE;
                        }

                        if (block != null) {
                           chunk.setBlockState(x, y, z, ((Block)block).getDefaultState());
                        }

                        ++z;
                        var48 += var50;
                     }

                     var35 += var39;
                     var37 += var41;
                  }

                  var16 += var24;
                  var18 += var26;
                  var20 += var28;
                  var22 += var30;
               }
            }
         }
      }

   }

   public void replaceBlocksForBiome(int chunkX, int chunkZ, ChunkPrimer chunk, BiomeGenBase[] biomes) {
      byte var5 = 64;
      double var6 = 0.03125;
      this.sandNoise = this.field_909_n.generateNoiseOctaves(this.sandNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0, 16, 16, 1, var6, var6, 1.0);
      this.gravelNoise = this.field_909_n.generateNoiseOctaves(this.gravelNoise, (double)(chunkX * 16), 109.0134, (double)(chunkZ * 16), 16, 1, 16, var6, 1.0, var6);
      this.stoneNoise = this.field_908_o.generateNoiseOctaves(this.stoneNoise, (double)(chunkX * 16), (double)(chunkZ * 16), 0.0, 16, 16, 1, var6 * 2.0, var6 * 2.0, var6 * 2.0);

      for(int z = 0; z < 16; ++z) {
         for(int x = 0; x < 16; ++x) {
            BiomeGenBase biome = biomes[z + x * 16];
            boolean var11 = this.sandNoise[z + x * 16] + this.rand.nextDouble() * 0.2 > 0.0;
            boolean var12 = this.gravelNoise[z + x * 16] + this.rand.nextDouble() * 0.2 > 3.0;
            int var13 = (int)(this.stoneNoise[z + x * 16] / 3.0 + 3.0 + this.rand.nextDouble() * 0.25);
            int var14 = -1;
            Block topBlock = biome.topBlock;
            Block fillerBlock = biome.fillerBlock;

            for(int y = 127; y >= 0; --y) {
               if (y <= 0 + this.rand.nextInt(5)) {
                  chunk.setBlockState(x, y, z, Blocks.BEDROCK.getDefaultState());
               } else {
                  Block block = chunk.getBlockState(x, y, z).getBlock();
                  if (block == Blocks.AIR) {
                     var14 = -1;
                  } else if (block == Blocks.STONE) {
                     if (var14 == -1) {
                        if (var13 <= 0) {
                           topBlock = Blocks.AIR;
                           fillerBlock = Blocks.STONE;
                        } else if (y >= var5 - 4 && y <= var5 + 1) {
                           topBlock = biome.topBlock;
                           fillerBlock = biome.fillerBlock;
                           if (var12) {
                              topBlock = Blocks.AIR;
                           }

                           if (var12) {
                              fillerBlock = Blocks.GRAVEL;
                           }

                           if (var11) {
                              topBlock = Blocks.SAND;
                           }

                           if (var11) {
                              fillerBlock = Blocks.SAND;
                           }
                        }

                        if (y < var5 && topBlock == Blocks.AIR) {
                           topBlock = Blocks.WATER;
                        }

                        var14 = var13;
                        if (y >= var5 - 1) {
                           chunk.setBlockState(x, y, z, ((Block)topBlock).getDefaultState());
                        } else {
                           chunk.setBlockState(x, y, z, ((Block)fillerBlock).getDefaultState());
                        }
                     } else if (var14 > 0) {
                        --var14;
                        chunk.setBlockState(x, y, z, ((Block)fillerBlock).getDefaultState());
                        if (var14 == 0 && fillerBlock == Blocks.SAND) {
                           var14 = this.rand.nextInt(4);
                           fillerBlock = Blocks.SANDSTONE;
                        }
                     }
                  }
               }
            }
         }
      }

   }

   public Chunk generateChunk(int x, int z) {
      this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
      this.biomesForGeneration = this.chunkManager.loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
      double[] temperatures = this.chunkManager.temperature;
      ChunkPrimer primer = new ChunkPrimer();
      this.generateTerrain(x, z, primer, temperatures);
      this.replaceBlocksForBiome(x, z, primer, this.biomesForGeneration);
      this.caveGenerator.generate(this.worldObj, x, z, primer);
      Chunk chunk = new Chunk(this.worldObj, primer, x, z);
      byte[] biomes = chunk.getBiomeArray();

      for(int i = 0; i < biomes.length; ++i) {
         biomes[i] = (byte)Biome.getIdForBiome(this.biomesForGeneration[(i & 15) << 4 | i >> 4 & 15].handle);
      }

      chunk.generateSkylightMap();
      return chunk;
   }

   private double[] func_4061_a(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (var1 == null) {
         var1 = new double[var5 * var6 * var7];
      }

      double var8 = 684.412;
      double var10 = 684.412;
      double[] var12 = this.chunkManager.temperature;
      double[] var13 = this.chunkManager.humidity;
      this.field_4182_g = this.field_922_a.func_4109_a(this.field_4182_g, var2, var4, var5, var7, 1.121, 1.121, 0.5);
      this.field_4181_h = this.field_921_b.func_4109_a(this.field_4181_h, var2, var4, var5, var7, 200.0, 200.0, 0.5);
      this.field_4185_d = this.field_910_m.generateNoiseOctaves(this.field_4185_d, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8 / 80.0, var10 / 160.0, var8 / 80.0);
      this.field_4184_e = this.field_912_k.generateNoiseOctaves(this.field_4184_e, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8, var10, var8);
      this.field_4183_f = this.field_911_l.generateNoiseOctaves(this.field_4183_f, (double)var2, (double)var3, (double)var4, var5, var6, var7, var8, var10, var8);
      int var14 = 0;
      int var15 = 0;
      int var16 = 16 / var5;

      for(int var17 = 0; var17 < var5; ++var17) {
         int var18 = var17 * var16 + var16 / 2;

         for(int var19 = 0; var19 < var7; ++var19) {
            int var20 = var19 * var16 + var16 / 2;
            double var21 = var12[var18 * 16 + var20];
            double var23 = var13[var18 * 16 + var20] * var21;
            double var25 = 1.0 - var23;
            var25 *= var25;
            var25 *= var25;
            var25 = 1.0 - var25;
            double var27 = (this.field_4182_g[var15] + 256.0) / 512.0;
            var27 *= var25;
            if (var27 > 1.0) {
               var27 = 1.0;
            }

            double var29 = this.field_4181_h[var15] / 8000.0;
            if (var29 < 0.0) {
               var29 = -var29 * 0.3;
            }

            var29 = var29 * 3.0 - 2.0;
            if (var29 < 0.0) {
               var29 /= 2.0;
               if (var29 < -1.0) {
                  var29 = -1.0;
               }

               var29 /= 1.4;
               var29 /= 2.0;
               var27 = 0.0;
            } else {
               if (var29 > 1.0) {
                  var29 = 1.0;
               }

               var29 /= 8.0;
            }

            if (var27 < 0.0) {
               var27 = 0.0;
            }

            var27 += 0.5;
            var29 = var29 * (double)var6 / 16.0;
            double var31 = (double)var6 / 2.0 + var29 * 4.0;
            ++var15;

            for(int var33 = 0; var33 < var6; ++var33) {
               double var34 = 0.0;
               double var36 = ((double)var33 - var31) * 12.0 / var27;
               if (var36 < 0.0) {
                  var36 *= 4.0;
               }

               double var38 = this.field_4184_e[var14] / 512.0;
               double var40 = this.field_4183_f[var14] / 512.0;
               double var42 = (this.field_4185_d[var14] / 10.0 + 1.0) / 2.0;
               if (var42 < 0.0) {
                  var34 = var38;
               } else if (var42 > 1.0) {
                  var34 = var40;
               } else {
                  var34 = var38 + (var40 - var38) * var42;
               }

               var34 -= var36;
               if (var33 > var6 - 4) {
                  double var44 = (double)((float)(var33 - (var6 - 4)) / 3.0F);
                  var34 = var34 * (1.0 - var44) + -10.0 * var44;
               }

               var1[var14] = var34;
               ++var14;
            }
         }
      }

      return var1;
   }

	public void populate(int chunkX, int chunkZ) {
		BlockFalling.fallInstantly = true;

		int baseX = chunkX * 16;
		int baseZ = chunkZ * 16;

		BiomeGenBase biome = this.chunkManager.getBiomeGenAt(baseX + 16, baseZ + 16);

		this.rand.setSeed(this.worldObj.getSeed());

		long var7 = this.rand.nextLong() / 2L * 2L + 1L;
		long var9 = this.rand.nextLong() / 2L * 2L + 1L;
		this.rand.setSeed((long)chunkX * var7 + (long)chunkZ * var9 ^ this.worldObj.getSeed());

		if (this.rand.nextInt(4) == 0) {
			int var37 = baseX + this.rand.nextInt(16) + 8;
			int var49 = this.rand.nextInt(128);
			int i = baseZ + this.rand.nextInt(16) + 8;
			(new WorldGenLakes(Blocks.WATER)).generate(this.worldObj, this.rand, var37, var49, i);
		}

		if (this.rand.nextInt(8) == 0) {
			int var37 = baseX + this.rand.nextInt(16) + 8;
			int var49 = this.rand.nextInt(this.rand.nextInt(120) + 8);
			int i = baseZ + this.rand.nextInt(16) + 8;
			if (var49 < 64 || this.rand.nextInt(10) == 0) {
				(new WorldGenLakes(Blocks.LAVA)).generate(this.worldObj, this.rand, var37, var49, i);
			}
		}

      ////int i;
      //for (int var37 = 0; var37 < 8; ++var37) {
      //   int var49 = baseX + this.rand.nextInt(16) + 8;
      //   int i = this.rand.nextInt(128);
      //   int j = baseZ + this.rand.nextInt(16) + 8;
      //   (new WorldGenDungeons()).generate(this.worldObj, this.rand, var49, i, j);
      //}

		for(int var37 = 0; var37 < 10; ++var37) {
			int var49 = baseX + this.rand.nextInt(16);
			int i = this.rand.nextInt(128);
			int j = baseZ + this.rand.nextInt(16);
			(new WorldGenClay(32)).generate(this.worldObj, this.rand, var49, i, j);
		}

		for (int var37 = 0; var37 < 20; ++var37) {
			int var49 = baseX + this.rand.nextInt(16);
			int i = this.rand.nextInt(128);
			int j = baseZ + this.rand.nextInt(16);
			(new WorldGenMinable(Blocks.DIRT, 32)).generate(this.worldObj, this.rand, var49, i, j);
		}

		for (int var37 = 0; var37 < 10; ++var37) {
			int var49 = baseX + this.rand.nextInt(16);
			int i = this.rand.nextInt(128);
			int j = baseZ + this.rand.nextInt(16);
			(new WorldGenMinable(Blocks.GRAVEL, 32)).generate(this.worldObj, this.rand, var49, i, j);
		}

		for (int var37 = 0; var37 < 20; ++var37) {
			int var49 = baseX + this.rand.nextInt(16);
			int i = this.rand.nextInt(128);
			int j = baseZ + this.rand.nextInt(16);
			(new WorldGenMinable(Blocks.COAL_ORE, 16)).generate(this.worldObj, this.rand, var49, i, j);
		}

		for (int var37 = 0; var37 < 20; ++var37) {
			int var49 = baseX + this.rand.nextInt(16);
			int i = this.rand.nextInt(64);
			int j = baseZ + this.rand.nextInt(16);
			(new WorldGenMinable(Blocks.IRON_ORE, 8)).generate(this.worldObj, this.rand, var49, i, j);
		}

		for(int var37 = 0; var37 < 2; ++var37) {
			int var49 = baseX + this.rand.nextInt(16);
			int i = this.rand.nextInt(32);
			int j = baseZ + this.rand.nextInt(16);
			(new WorldGenMinable(Blocks.GOLD_ORE, 8)).generate(this.worldObj, this.rand, var49, i, j);
		}

		for(int var37 = 0; var37 < 8; ++var37) {
			int var49 = baseX + this.rand.nextInt(16);
			int i = this.rand.nextInt(16);
			int j = baseZ + this.rand.nextInt(16);
			(new WorldGenMinable(Blocks.REDSTONE_ORE, 7)).generate(this.worldObj, this.rand, var49, i, j);
		}

		for(int var37 = 0; var37 < 1; ++var37) {
			int var49 = baseX + this.rand.nextInt(16);
			int i = this.rand.nextInt(16);
			int j = baseZ + this.rand.nextInt(16);
			(new WorldGenMinable(Blocks.DIAMOND_ORE, 7)).generate(this.worldObj, this.rand, var49, i, j);
		}

		for(int var37 = 0; var37 < 1; ++var37) {
			int var49 = baseX + this.rand.nextInt(16);
			int i = this.rand.nextInt(16) + this.rand.nextInt(16);
			int j = baseZ + this.rand.nextInt(16);
			(new WorldGenMinable(Blocks.LAPIS_ORE, 6)).generate(this.worldObj, this.rand, var49, i, j);
		}

		{
			final double var11 = 0.5d;
			int var37 = (int)((this.mobSpawnerNoise.func_806_a((double)baseX * var11, (double)baseZ * var11) / 8.0 + this.rand.nextDouble() * 4.0 + 4.0) / 3.0);
			int treesCount = 0;
			if (this.rand.nextInt(10) == 0) {
				++treesCount;
			}

			if (biome == BiomeGenBase.forest) {
				treesCount += var37 + 5;
			}

			if (biome == BiomeGenBase.rainforest) {
				treesCount += var37 + 5;
			}

			if (biome == BiomeGenBase.seasonalForest) {
				treesCount += var37 + 2;
			}

			if (biome == BiomeGenBase.taiga) {
				treesCount += var37 + 5;
			}

			if (biome == BiomeGenBase.desert) {
				treesCount -= 20;
			}

			if (biome == BiomeGenBase.tundra) {
				treesCount -= 20;
			}

			if (biome == BiomeGenBase.plains) {
				treesCount -= 20;
			}

			for (int i = 0; i < treesCount; ++i) {
				//logger.info("ChunkProvider-Tree");
				//logger.info("Count: " + treesCount);
				//logger.info("Current: " + i);
				int x = baseX + this.rand.nextInt(16) + 8;
				int z = baseZ + this.rand.nextInt(16) + 8;
				//WorldGenerator treeGenerator = biome.getRandomWorldGenForTrees(this.rand);
				//
				int y = NostalgiaGenHelper.getHeightValue(this.worldObj, x, z);
				//logger.info("Coords: " + x + ", " + y + ", " + z);
				//treeGenerator.generate(this.worldObj, this.rand, x, y, z);

				if (this.rand.nextInt(10) == 0) {
					worldGenBigTree.func_517_a(1.0, 1.0, 1.0);
					worldGenBigTree.generate(this.worldObj, this.rand, x, y, z);
				} else {
					worldGenTrees.generate(this.worldObj, this.rand, x, y, z);
				}

			}

			//logger.info("called-after?");

			byte var62 = 0;
			if (biome == BiomeGenBase.forest) {
			   var62 = 2;
			}

			if (biome == BiomeGenBase.seasonalForest) {
				var62 = 4;
			}

			if (biome == BiomeGenBase.taiga) {
				var62 = 2;
			}

			if (biome == BiomeGenBase.plains) {
				var62 = 3;
			}

			for (int i = 0; i < var62; ++i) {
				int var84 = baseX + this.rand.nextInt(16) + 8;
				int x = this.rand.nextInt(128);
				int z = baseZ + this.rand.nextInt(16) + 8;
				(new WorldGenFlowers(Blocks.YELLOW_FLOWER)).generate(this.worldObj, this.rand, var84, x, z);
			}

			byte var74 = 0;
			if (biome == BiomeGenBase.forest) {
				var74 = 2;
			}

			if (biome == BiomeGenBase.rainforest) {
				var74 = 10;
			}

			if (biome == BiomeGenBase.seasonalForest) {
				var74 = 2;
			}

			if (biome == BiomeGenBase.taiga) {
				var74 = 1;
			}

			if (biome == BiomeGenBase.plains) {
				var74 = 10;
			}

			for (int var84 = 0; var84 < var74; ++var84) {

				EnumType type = EnumType.GRASS;

				if (biome == BiomeGenBase.rainforest && this.rand.nextInt(3) != 0) {
					type = EnumType.FERN;
				}

				int x = baseX + this.rand.nextInt(16) + 8;
				int y = this.rand.nextInt(128);
				int z = baseZ + this.rand.nextInt(16) + 8;
				(new WorldGenTallGrass(type)).generate(this.worldObj, this.rand, x, y, z);

			}
		}

		int var74 = 0;
		if (biome == BiomeGenBase.desert) {
			var74 = 2;
		}

		for (int i = 0; i < var74; ++i) {
			int x = baseX + this.rand.nextInt(16) + 8;
			int y = this.rand.nextInt(128);
			int z = baseZ + this.rand.nextInt(16) + 8;
			(new WorldGenDeadBush(Blocks.DEADBUSH)).generate(this.worldObj, this.rand, x, y, z);
		}

		if (this.rand.nextInt(2) == 0) {
			int x = baseX + this.rand.nextInt(16) + 8;
			int y = this.rand.nextInt(128);
			int z = baseZ + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Blocks.RED_FLOWER)).generate(this.worldObj, this.rand, x, y, z);
		}

		if (this.rand.nextInt(4) == 0) {
			int x = baseX + this.rand.nextInt(16) + 8;
			int y = this.rand.nextInt(128);
			int z = baseZ + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Blocks.BROWN_MUSHROOM)).generate(this.worldObj, this.rand, x, y, z);
		}

		if (this.rand.nextInt(8) == 0) {
			int x = baseX + this.rand.nextInt(16) + 8;
			int y = this.rand.nextInt(128);
			int z = baseZ + this.rand.nextInt(16) + 8;
			(new WorldGenFlowers(Blocks.RED_MUSHROOM)).generate(this.worldObj, this.rand, x, y, z);
		}

		for(int i = 0; i < 10; ++i) {
			int x = baseX + this.rand.nextInt(16) + 8;
			int y = this.rand.nextInt(128);
			int z = baseZ + this.rand.nextInt(16) + 8;
			(new WorldGenReed()).generate(this.worldObj, this.rand, x, y, z);
		}

		if (this.rand.nextInt(32) == 0) {
			int var84 = baseX + this.rand.nextInt(16) + 8;
			int x = this.rand.nextInt(128);
			int z = baseZ + this.rand.nextInt(16) + 8;
			(new WorldGenPumpkin()).generate(this.worldObj, this.rand, var84, x, z);
		}

		int var84 = 0;
		if (biome == BiomeGenBase.desert) {
			var84 += 10;
		}

        for (int i = 0; i < var84; ++i) {
	        int x = baseX + this.rand.nextInt(16) + 8;
	        int y = this.rand.nextInt(128);
	        int z = baseZ + this.rand.nextInt(16) + 8;
	        (new WorldGenCactus()).generate(this.worldObj, this.rand, x, y, z);
        }

		for (int i = 0; i < 50; ++i) {
			int x = baseX + this.rand.nextInt(16) + 8;
			int y = this.rand.nextInt(this.rand.nextInt(120) + 8);
			int z = baseZ + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Blocks.FLOWING_WATER)).generate(this.worldObj, this.rand, x, y, z);
		}

		for (int i = 0; i < 20; ++i) {
			int x = baseX + this.rand.nextInt(16) + 8;
			int y = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(112) + 8) + 8);
			int z = baseZ + this.rand.nextInt(16) + 8;
			(new WorldGenLiquids(Blocks.FLOWING_LAVA)).generate(this.worldObj, this.rand, x, y, z);
		}

		this.generatedTemperatures = this.chunkManager.getTemperatures(this.generatedTemperatures, baseX + 8, baseZ + 8, 16, 16);

		for (int x = baseX + 8; x < baseX + 8 + 16; ++x) {
			for (int z = baseZ + 8; z < baseZ + 8 + 16; ++z) {
				int xd = x - (baseX + 8);
				int zd = z - (baseZ + 8);
				int y = this.worldObj.getPrecipitationHeight(new BlockPos(x, 0, z)).getY();
				double var23 = this.generatedTemperatures[xd * 16 + zd] - (double)(y - 64) / 64.0 * 0.3;
				if (var23 < 0.5 && y > 0 && y < 128 && NostalgiaGenHelper.isAirBlock(this.worldObj, x, y, z) && NostalgiaGenHelper.getBlockMaterial(this.worldObj, x, y - 1, z).isSolid() && NostalgiaGenHelper.getBlockMaterial(this.worldObj, x, y - 1, z) != Material.ICE) {
					NostalgiaGenHelper.setBlockWithNotify(this.worldObj, x, y, z, Blocks.SNOW_LAYER);
				}
			}
		}

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
