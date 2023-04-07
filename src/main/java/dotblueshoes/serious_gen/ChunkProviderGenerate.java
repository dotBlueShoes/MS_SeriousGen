package dotblueshoes.serious_gen;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
//import net.minecrell.nostalgia_gen.a1_1_2_01.NoiseGeneratorOctaves;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChunkProviderGenerate implements IChunkGenerator {

	private final World world;
	private final Random randomNumberGenerator;

	private final byte[] blockBiomes = new byte[16 * 16]; // x-z per chunk biome assignment.
	private static final byte OTHER_BIOME = (byte)Biome.getIdForBiome(Biomes.DESERT);
	private static final byte BEACH_BIOME = (byte)Biome.getIdForBiome(Biomes.BEACH);
	private static final byte OCEAN_BIOME = (byte)Biome.getIdForBiome(Biomes.OCEAN);

	final int
		xLength = 5, zLength = 5, yLength = 17, // for some reason cannot be lower than 17.
		xzLength = xLength * zLength,
		xyzLength = xLength * yLength * zLength;
	private final double[] heightMap = new double[xLength * zLength * yLength]; // = new double[256];

	final private NoiseGeneratorOctaves
		minLimitPerlinNoise,
		maxLimitPerlinNoise,
		mainPerlinNoise,
		unknownNoise,
		depthNoise;

	private final double[]
		aaa = new double[xzLength],
		bbb = new double[xzLength],
		ccc = new double[xyzLength],
		ddd = new double[xyzLength],
		eee = new double[xyzLength];

	NoiseGeneratorOctaves surfaceNoise, stonePatchesNoise;

	double[]
		gravelNoise = new double[16 * 16],
		stoneNoise = new double[16 * 16],
		sandNoise  = new double[16 * 16];

	private BiomesManager biomesManager;

	public ChunkProviderGenerate(final World world, final long seed) {
		this.world                      = world;
		this.randomNumberGenerator      = new Random(seed);
		this.minLimitPerlinNoise        = new NoiseGeneratorOctaves(this.randomNumberGenerator, 16);
		this.maxLimitPerlinNoise        = new NoiseGeneratorOctaves(this.randomNumberGenerator, 16);
		this.mainPerlinNoise            = new NoiseGeneratorOctaves(this.randomNumberGenerator, 8);
		this.surfaceNoise               = new NoiseGeneratorOctaves(this.randomNumberGenerator, 4);
		this.stonePatchesNoise          = new NoiseGeneratorOctaves(this.randomNumberGenerator, 4);
		this.unknownNoise               = new NoiseGeneratorOctaves(this.randomNumberGenerator, 10);
		this.depthNoise                 = new NoiseGeneratorOctaves(this.randomNumberGenerator, 16);
		//this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
		this.biomesManager = new BiomesManager(world);
	}


	/*
	  We're generally simply calling this method from the start.
	  MAIN of chunk gen. Is called with every chunk we're generating.
	  generation of chunk also happens when we "load" them.
	 */
	@Override
	public @NotNull Chunk generateChunk(final int x, final int z) {

		// No idea. CHUNK_PRIMER IS RESPONSIBLE FOR LAGS AND AN EVENTUAL CRASH AT HIGH SPEED CHUNK GENERATING (OutOfMemory) !!
		ChunkPrimer chunkPrimer = new ChunkPrimer();

		//if (x >= 0 && x < 10 && z >= 0 && z < 10) {
		//if (x == 0 && z == 0) {
			randomNumberGenerator.setSeed(x * 341873128712L + z * 132897987541L);   // No idea... does it do anything ???
			this.biomesManager.generateChunkBiomeData(this.blockBiomes, x * 16, z * 16, 16, 16);

			generateTerrain(x, z, chunkPrimer);                                     // Generates stone-block noise filled with water up to water-level.
			replaceBlocks(x, z, chunkPrimer);                                       // Replaces generated stone with other blocks. - biomes !
		//}

		Chunk chunk = new Chunk(this.world, chunkPrimer, x, z);
		System.arraycopy(this.blockBiomes, 0, chunk.getBiomeArray(), 0, this.blockBiomes.length); // Biome assigning.
		chunk.generateSkylightMap();

		return chunk;
	}

	private Block generateWater() {
		return Blocks.WATER;
	}

	// Because terrain is being generated from bottom to top.
	//  I could have an array per chunk topBlocks[16*16]
	//  that I guess could also have depth maybe like 2-3. it depends.
	//  whenever we generate a stone block in a said y range we would say
	//  that at the following x and z the top block is at current y
	//  then with each new block on y we overwrite it.
	//  it stops when we don't generate e.g. generate air block.
	// It has depth because there could be air pockets between
	//  we would simply check if the previous was a stone or air block
	//  and depending on that we would move in depth.
	// ->
	// having that list we could either generate biomes in replaceBlocks() method
	//  or even generate here I guess...

	private void generateTerrain(final int chunkX, final int chunkZ, final ChunkPrimer chunkPrimer) {

		final double oneEighth = 0.125;
		final double oneFourth = 0.25;
		final int seaLevel = 64;

		//byte baseLevel = 64, times = 4;
		//final int var6 = times + 1;
		//final byte var7 = 17;

		//SeriousGenMod.logger.info("Chunk: " + chunkX + ", " + chunkZ);

		// Entire chunk is 16 x 256 x 16 = 256 * 256 = 65536

		// get noise values for the whole chunk
		this.generateHeightMap(chunkX, chunkZ);
		//logger.info("Hello There!");

		for (int x4 = 0; x4 < 4; ++x4) {                // Divide chunk into 4 subChunks in x direction, index as x4
			for (int z4 = 0; z4 < 4; ++z4) {            // Divide chunk into 4 subChunks in y direction, index as z4

				final int k_x0z0 = (x4 * xLength + z4) * yLength;
				final int k_x0z1 = (x4 * xLength + z4 + 1) * yLength;
				final int k_x1z0 = ((x4 + 1) * xLength + z4) * yLength;
				final int k_x1z1 = ((x4 + 1) * xLength + z4 + 1) * yLength;

				for (int y16 = 0; y16 < 16; ++y16) {    // Divide chunk into 16 subChunks in y direction, index as y16

					final double n_x0y0z0 = this.heightMap[k_x0z0 + y16];
					final double n_x0y0z1 = this.heightMap[k_x0z1 + y16];
					final double n_x1y0z0 = this.heightMap[k_x1z0 + y16];
					final double n_x1y0z1 = this.heightMap[k_x1z1 + y16];
					final double n_x0y1z0 = (this.heightMap[k_x0z0 + y16 + 1] - n_x0y0z0) * oneEighth;
					final double n_x0y1z1 = (this.heightMap[k_x0z1 + y16 + 1] - n_x0y0z1) * oneEighth;
					final double n_x1y1z0 = (this.heightMap[k_x1z0 + y16 + 1] - n_x1y0z0) * oneEighth;
					final double n_x1y1z1 = (this.heightMap[k_x1z1 + y16 + 1] - n_x1y0z1) * oneEighth;

					double noiseStartX0 = n_x0y0z0;
					double noiseStartX1 = n_x0y0z1;
					double noiseEndX0 = n_x1y0z0;
					double noiseEndX1 = n_x1y0z1;

					// SubChunk is 8 blocks high in y direction, index as iy
					for (int iy = 0; iy < 8; ++iy) {

						double noiseStartZ = noiseStartX0;
						double noiseEndZ = noiseStartX1;
						double noiseStepX0 = (noiseEndX0 - noiseStartX0) * oneFourth;
						double noiseStepX1 = (noiseEndX1 - noiseStartX1) * oneFourth;

						// SubChunk is 4 blocks long in x direction, index as ix
						for (int ix = 0; ix < 4; ++ix) {

							final int x = ix + x4 * 4;    // (0-15)
							final int y = y16 * 8 + iy;   // (0-127)
							int z = z4 * 4;         // (0-15

							double noiseStepZ = (noiseEndZ - noiseStartZ) * oneFourth;
							double noiseVal = noiseStartZ;

							// SubChunk is 4 blocks long in x direction, index as iz
							for (int iz = 0; iz < 4; ++iz) {
								Block block;

								//SeriousGenMod.logger.info("x: " + x + ", y: " + y + ", z: " + z);

								// PLACE WATER
								//if (y16 * 8 + iy < seaLevel) {
								//	block = generateWater();
								//	chunkPrimer.setBlockState(x, y, z, block.getDefaultState());
								//	SeriousGenMod.logger.info("noiseVal: " + noiseVal);
								//}

								// DEBUG
								//if (y == 64) {
								//	SeriousGenMod.logger.info(
								//		"x: " + x + "  \ty:" + y + "  \tz: " + z +
								//		"  \tt: " + this.biomesManager.temperatures[x * 16 + z] +
								//		"  \th: " + this.biomesManager.temperatures[x * 16 + z]
								//	);
								//}

								if (noiseVal > 0.0) {
									block = Blocks.STONE;
									chunkPrimer.setBlockState(x, y, z, block.getDefaultState());
									//SeriousGenMod.logger.info("noiseVal: " + noiseVal);
								} else if (y16 * 8 + iy < seaLevel) {
									block = generateWater();
									chunkPrimer.setBlockState(x, y, z, block.getDefaultState());
									//SeriousGenMod.logger.info("noiseVal: " + noiseVal);
								}

								//if (block != null) {
								//	chunkPrimer.setBlockState(x, y, z, block.getDefaultState());
								//}



								++z;
								noiseVal += noiseStepZ;
							}

							noiseStartZ += noiseStepX0;
							noiseEndZ += noiseStepX1;
						}

						noiseStartX0 += n_x0y1z0;
						noiseStartX1 += n_x0y1z1;
						noiseEndX0 += n_x1y1z0;
						noiseEndX1 += n_x1y1z1;
					}
				}
			}
		}

		//this.heightMap = this.generateHeightMap(this.heightMap, chunkX * times, 0, chunkZ * times, var6, var7, var6);
	}

	private void generateHeightMap(final int chunkX, final int chunkZ) {

		final int subChunkX = chunkX * 4;
		final int subChunkY = 0;
		final int subChunkZ = chunkZ * 4;

		// Threes no assignments only arithmetic ops so we need to preinitialize it each call.
		Arrays.fill(this.aaa, 0.0);
		Arrays.fill(this.bbb, 0.0);
		Arrays.fill(this.ccc, 0.0);
		Arrays.fill(this.ddd, 0.0);
		Arrays.fill(this.eee, 0.0);

		// TO BE CONTINUED
		// https://github.com/Glitchfiend/BiomesOPlenty/blob/5ad77116382bec782a023926b8dab17a6ea5599d/src/main/java/biomesoplenty/common/world/ChunkGeneratorOverworldBOP.java#L389
		// https://github.com/Glitchfiend/BiomesOPlenty/blob/5ad77116382bec782a023926b8dab17a6ea5599d/src/main/java/biomesoplenty/common/world/NoiseGeneratorOctavesBOP.java#L42
		// https://github.com/Glitchfiend/BiomesOPlenty/blob/5ad77116382bec782a023926b8dab17a6ea5599d/src/main/java/biomesoplenty/common/world/NoiseGeneratorBOP.java#L91

		// xz noise
		this.unknownNoise.generateNoiseOctavesXZ         (this.aaa, subChunkX, subChunkZ, xLength, zLength, 1.0,  1.0);
		this.depthNoise.generateNoiseOctavesXZ           (this.bbb, subChunkX, subChunkZ, xLength, zLength, 100.0, 100.0);

		// xyz noise
		this.mainPerlinNoise.generateNoiseOctavesXYZ     (this.ccc, subChunkX, subChunkY, subChunkZ, xLength, yLength, zLength, 684.412 / 80.0, 684.412 / 160.0, 684.412 / 80.0);
		this.minLimitPerlinNoise.generateNoiseOctavesXYZ (this.ddd, subChunkX, subChunkY, subChunkZ, xLength, yLength, zLength, 684.412, 684.412, 684.412);
		this.maxLimitPerlinNoise.generateNoiseOctavesXYZ (this.eee, subChunkX, subChunkY, subChunkZ, xLength, yLength, zLength, 684.412, 684.412, 684.412);

		//logger.info (
		//	"call" + "\n" +
		//	Arrays.toString(this.aaa) + "\n" +
		//	Arrays.toString(this.bbb) + "\n" +
		//	Arrays.toString(this.ccc) + "\n" +
		//	Arrays.toString(this.ddd) + "\n" +
		//	Arrays.toString(this.eee) + "\n"
		//);

		int mainIndex = 0;
		int index = 0;

		for (int x = 0; x < xLength; ++x) {
			for (int z = 0; z < zLength; ++z) {

				double valueA = (this.aaa[index] + 256.0) / 512.0;
				double valueB = this.bbb[index] / 8000.0;

				if (valueA > 1.0) valueA = 1.0;
				if (valueB < 0.0) valueB = -valueB;

				valueB = valueB * 3.0 - 3.0;

				if (valueB < 0.0) {
					valueB /= 2.0;
					if (valueB < -1.0) valueB = -1.0;
					valueB /= 1.4;
					valueB /= 2.0;
					valueA = 0.0;
				} else {
					if (valueB > 1.0) valueB = 1.0;
					valueB /= 6.0;
				}

				valueA += 0.5;
				//valueB = valueB * (double) yLength / 16.0; // = 1
				double var22 = (double) yLength / 2.0 + valueB * 4.0;
				//SeriousGenMod.logger.info(var22);

				// ... not sure how dents are being created.
				// how noise is being represented in y-axis
				// does heightMap hold information about y-axis?

				//final double example_i = 0.0; // it appears that values [1-16] leave world empty ?
				//double var27 = (example_i - var22) * 12.0 / valueA; // in general negative numbers.
				//for (int i = 0; i < yLength; ++i) {
				//	this.heightMap[mainIndex] -= var27;
				//	++mainIndex;
				//}

				// UNCOMMENT ME !
				//SeriousGenMod.logger.info("loop");
				for (int i = 0; i < yLength; ++i) { // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16

				 	double result = 0;
				 	double var27 = ((double)i - var22) * 12.0 / valueA; // in general negative numbers.
				 	final double flatness = 4.0f;

				 	//SeriousGenMod.logger.info("value: " + var27);

				 	// It means higher low grounds.
				 	//  high grounds stay unaffected.
				 	//  everything that would generate below somewhere around ~[8.0 - -64.0]
				 	//  will be raised.
				 	if (var27 < 0.0) var27 *= flatness;

				 	// Those add the 3rd noise effect.
				 	//  all the grumpiness, gigantic and smaller hills, depths.
				 	double valueC = (this.ccc[mainIndex] / 10.0 + 1.0) / 2.0; // in range of ~[-10.00 - 10.00]
				 	double min = this.ddd[mainIndex] / 512.0; // in range of ~[-64.00 - 64.00]
				 	double max = this.eee[mainIndex] / 512.0; // in range of ~[-64.00 - 64.00]

				 	//SeriousGenMod.logger.info("min: " + min + ", max: " + max + ", value: " + valueC);

				 	if (valueC < 0.0) result = min;
				 	else if (valueC > 1.0) result = max;
				 	else result = min + (max - min) * valueC;

				 	result -= var27;

				 	// No idea, but works without.
				 	//if (i > yLength - 4) {
				 	//	double var45 = (double)((float)(i - (yLength - 4)) / 3.0F);
				 	//	result = result * (1.0 - var45) + -10.0 * var45;
				 	//}

				 	//if ((double)i < 0.0) {
				 	//	SeriousGenMod.logger.info("what-here");
				 	//	double var45 = (0.0 - (double)i) / 4.0;
				 	//	if (var45 < 0.0) var45 = 0.0;
				 	//	if (var45 > 1.0) var45 = 1.0;
				 	//	result = result * (1.0 - var45) + -10.0 * var45;
				 	//}

				 	this.heightMap[mainIndex] = result;
				 	++mainIndex;
				}
				++index;
			}
		}
	}

	public void replaceBlocks(int chunkX, int chunkZ, ChunkPrimer chunkPrimer) {

		//Block currentBlock;
		//byte baseLevel = 64;
		double var5 = 0.03125;

		//int var12 = -1;

		//Block topBlock = Blocks.GRASS;
		//Block midBlock = Blocks.DIRT;

		//boolean water = false;
		//boolean top = true;

		Arrays.fill(this.stoneNoise, 0.0);
		Arrays.fill(this.gravelNoise, 0.0);
		Arrays.fill(this.sandNoise, 0.0);

		surfaceNoise.generateNoiseOctavesXYZ        (this.stoneNoise   , (chunkX * 16), (chunkZ * 16), 0, 16, 16, 1, var5, var5, 1.0);
		surfaceNoise.generateNoiseOctavesXYZ        (this.gravelNoise  , (chunkZ * 16), (int)109.0134, (chunkX * 16), 16, 1, 16, var5, 1.0, var5);
		stonePatchesNoise.generateNoiseOctavesXYZ   (this.sandNoise    , (chunkX * 16), (chunkZ * 16), 0, 16, 16, 1, var5 * 2.0, var5 * 2.0, var5 * 2.0);

		// Just comment the following and uncomment what's commented.
		//  For previous alpha gen.
		// ->

		Block currentBlock;
		Block midBlock = Blocks.DIRT;

		for (int x = 0; x < 16; x++) {
			perBlock: for (int z = 0; z < 16; z++) {

				// Fill Grass
				for (int y = 128; y >= 64; --y) {
					currentBlock = chunkPrimer.getBlockState(x, y, z).getBlock();

					if (currentBlock != Blocks.AIR) { // TOP_BLOCK - LAND
						//this.blockBiomes[x + z * 16] = STANDARD_BIOME;
						//chunkPrimer.setBlockState(x, y, z, Blocks.GRASS.getDefaultState());

						if (this.blockBiomes[x + z * 16] == OTHER_BIOME)
							chunkPrimer.setBlockState(x, y, z, Blocks.MOSSY_COBBLESTONE.getDefaultState());
						else
							chunkPrimer.setBlockState(x, y, z, Blocks.GRASS.getDefaultState());

						int midBlockRange = randomNumberGenerator.nextInt(3) + 2;

						for (int i = 0; i != midBlockRange; ++i) {
							chunkPrimer.setBlockState(x, y - i - 1, z, midBlock.getDefaultState());
						}

						continue perBlock;
					}
				}

				// Fill Sand // TOP_BLOCK - NEAR_WATER
				if (chunkPrimer.getBlockState(x, 63, z).getBlock() == Blocks.STONE) {
					this.blockBiomes[x + z * 16] = BEACH_BIOME;
					chunkPrimer.setBlockState(x, 63, z, Blocks.SAND.getDefaultState());
					continue perBlock;
				}

				// Fill Gravel
				for (int y = 62; y >= 0; --y) {
					currentBlock = chunkPrimer.getBlockState(x, y, z).getBlock();

					if (currentBlock == Blocks.STONE) { // TOP_BLOCK - UNDER_WATER
						this.blockBiomes[x + z * 16] = OCEAN_BIOME;
						chunkPrimer.setBlockState(x, y, z, Blocks.GRAVEL.getDefaultState());
						continue perBlock;
					}
				}

			}
		}

		//for (int x = 0; x < 16; ++x) {
		//	for (int z = 0; z < 16; ++z) {
		//
		//		int stone = (int)(this.stoneNoise[x + z * 16] / 3.0 + 3.0 + this.randomNumberGenerator.nextDouble() * 0.25);
		//		boolean gravel = this.gravelNoise[x + z * 16] + this.randomNumberGenerator.nextDouble() * 0.2 > 3.0;
		//		boolean sand = this.sandNoise[x + z * 16] + this.randomNumberGenerator.nextDouble() * 0.2 > 0.0;
		//
		//		int var12 = -1;
		//
		//		topBlock = Blocks.GRASS;
		//		midBlock = Blocks.DIRT;
		//		water = false;
		//		top = true;
		//
		//		// First layer of bedrock.
		//		chunk.setBlockState(x, 0, z, Blocks.BEDROCK.getDefaultState());
		//
		//		for (int y = 127; y >= 0; --y) {
		//
		//			// Get currentBlock at position.
		//			currentBlock = chunk.getBlockState(x, y, z).getBlock();
		//
		//			if (currentBlock == Blocks.AIR) {
		//				var12 = -1;
		//			} else if (currentBlock != Blocks.STONE) {
		//				if (currentBlock == Blocks.WATER) water = true;
		//			} else if (var12 == -1) {
		//				if (stone <= 0) {
		//					topBlock = Blocks.AIR;
		//					midBlock = Blocks.STONE;
		//				} else if (y >= baseLevel - 4 && y <= baseLevel + 1) {
		//					topBlock = Blocks.GRASS;
		//					midBlock = Blocks.DIRT;
		//
		//					if (gravel) {
		//						topBlock = Blocks.AIR;
		//					}
		//
		//					if (gravel) {
		//						midBlock = Blocks.GRAVEL;
		//					}
		//
		//					if (sand) {
		//						topBlock = Blocks.SAND;
		//					}
		//
		//					if (sand) {
		//						midBlock = Blocks.SAND;
		//					}
		//				}
		//
		//				if (y < baseLevel && topBlock == Blocks.AIR) {
		//					topBlock = Blocks.WATER;
		//				}
		//
		//				if (top) {
		//					top = false;
		//					if (midBlock != Blocks.SAND && midBlock != Blocks.GRAVEL) {
		//
		//						if (water) {
		//							if (y >= baseLevel - 4) {
		//								this.blockBiomes[z << 4 | x] = BEACH_BIOME;
		//							} else {
		//								this.blockBiomes[z << 4 | x] = OCEAN_BIOME;
		//							}
		//						} else {
		//							this.blockBiomes[z << 4 | x] = STANDARD_BIOME;
		//						}
		//
		//					} else {
		//						this.blockBiomes[z << 4 | x] = BEACH_BIOME;
		//					}
		//				}
		//
		//				var12 = stone;
		//				if (y >= baseLevel - 1) {
		//					chunk.setBlockState(x, y, z, topBlock.getDefaultState());
		//				} else {
		//					chunk.setBlockState(x, y, z, midBlock.getDefaultState());
		//				}
		//			} else if (var12 > 0) {
		//				--var12;
		//				chunk.setBlockState(x, y, z, midBlock.getDefaultState());
		//			}
		//		}
		//	}
		//}

	}

	@Override
	public void populate(int x, int z) {

	}

	@Override
	public boolean generateStructures(@NotNull Chunk chunkIn, int x, int z) {
		return false;
	}

	@Override
	public @NotNull List<Biome.SpawnListEntry> getPossibleCreatures(@NotNull EnumCreatureType creatureType, @NotNull BlockPos pos) {
		return this.world.getBiome(pos).getSpawnableList(creatureType);
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(@NotNull World worldIn, @NotNull String structureName, @NotNull BlockPos position, boolean findUnexplored) {
		return null;
	}

	@Override
	public void recreateStructures(@NotNull Chunk chunkIn, int x, int z) {

	}

	@Override
	public boolean isInsideStructure(@NotNull World worldIn, @NotNull String structureName, @NotNull BlockPos pos) {
		return false;
	}

}
