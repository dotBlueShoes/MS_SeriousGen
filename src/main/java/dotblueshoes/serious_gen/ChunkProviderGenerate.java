package dotblueshoes.serious_gen;

import net.minecraft.block.Block;
import net.minecraft.entity.EnumCreatureType;
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

	final int
		xLength = 5, zLength = 5, yLength = 17, // for some reason cannot be lower than 17.
		xzLength = xLength * zLength,
		xyzLength = xLength * yLength * zLength;
	private final double[] heightMap = new double[xLength * zLength * yLength]; // = new double[256];

	final private NoiseGeneratorOctaves
		minLimitPerlinNoise,
		maxLimitPerlinNoise,
		mainPerlinNoise,
		//surfaceNoise,
		//stonePatchesNoise,
		unknownNoise,
		depthNoise;

	private final double[]
		aaa = new double[xzLength],
		bbb = new double[xzLength],
		ccc = new double[xyzLength],
		ddd = new double[xyzLength],
		eee = new double[xyzLength];

	public ChunkProviderGenerate(final World world, final long seed) {
		this.world                      = world;
		this.randomNumberGenerator      = new Random(seed);
		this.minLimitPerlinNoise        = new NoiseGeneratorOctaves(this.randomNumberGenerator, 16);
		this.maxLimitPerlinNoise        = new NoiseGeneratorOctaves(this.randomNumberGenerator, 16);
		this.mainPerlinNoise            = new NoiseGeneratorOctaves(this.randomNumberGenerator, 8);
		//this.surfaceNoise               = new NoiseGeneratorOctaves(this.randomNumberGenerator, 4);
		//this.stonePatchesNoise          = new NoiseGeneratorOctaves(this.randomNumberGenerator, 4);
		this.unknownNoise               = new NoiseGeneratorOctaves(this.randomNumberGenerator, 10);
		this.depthNoise                 = new NoiseGeneratorOctaves(this.randomNumberGenerator, 16);
		//this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
	}


	/*
	  We're generally simply calling this method from the start.
	  MAIN of chunk gen. Is called with every chunk we're generating.
	  generation of chunk also happens when we "load" them.
	 */
	@Override
	public @NotNull Chunk generateChunk(final int x, final int z) {

		// No idea.
		ChunkPrimer chunkPrimer = new ChunkPrimer();

		//logger.info(world.getSeed());
		this.randomNumberGenerator.setSeed(x * 341873128712L + z * 132897987541L);
		this.generateTerrain(x, z, chunkPrimer);

		Chunk chunk = new Chunk(this.world, chunkPrimer, x, z);
		//System.arraycopy(this.blockBiomes, 0, chunk.getBiomeArray(), 0, this.blockBiomes.length);
		chunk.generateSkylightMap();

		return chunk;
	}

	private void generateTerrain(final int chunkX, final int chunkZ, final ChunkPrimer chunkPrimer) {

		final int seaLevel = 64;

		//byte baseLevel = 64, times = 4;
		//final int var6 = times + 1;
		//final byte var7 = 17;

		// Entire chunk is 16 x 256 x 16 = 256 * 256 = 65536

		// get noise values for the whole chunk
		this.generateHeightMap(chunkX, chunkZ);
		//logger.info("Hello There!");

		for (int x4 = 0; x4 < 4; ++x4) {
			for (int z4 = 0; z4 < 4; ++z4) {
				for (int height = 0; height < 16; ++height) {
					double var12 = 0.125;

					final int i1 = (x4 * xLength + z4) * yLength;
					final int i2 = (x4 * xLength + z4 + 1) * yLength;
					final int i3 = ((x4 + 1) * xLength + z4) * yLength;
					final int i4 = ((x4 + 1) * xLength + z4 + 1) * yLength;

					double var14 = this.heightMap[i1 + height];
					double var16 = this.heightMap[i2 + height];
					double var18 = this.heightMap[i3 + height];
					double var20 = this.heightMap[i4 + height];
					double var22 = (this.heightMap[i1 + height + 1] - var14) * var12;
					double var24 = (this.heightMap[i2 + height + 1] - var16) * var12;
					double var26 = (this.heightMap[i3 + height + 1] - var18) * var12;
					double var28 = (this.heightMap[i4 + height + 1] - var20) * var12;

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

								// PLACE WATER
								//if (height * 8 + var30 < seaLevel) {
								//	block = Blocks.WATER;
								//}

								if (var46 > 0.0) {
									block = Blocks.STONE;
								}

								if (block != null) {
									chunkPrimer.setBlockState(x, y, z, block.getDefaultState());
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

		//this.heightMap = this.generateHeightMap(this.heightMap, chunkX * times, 0, chunkZ * times, var6, var7, var6);
	}

	private void generateHeightMap(final int chunkX, final int chunkZ) {

		final int subChunkX = chunkX * 4;
		final int subChunkY = 0;
		final int subChunkZ = chunkZ * 4;

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
				valueB = valueB * (double) yLength / 16.0;

				double var22 = (double) yLength / 2.0 + valueB * 4.0;
				SeriousGenMod.logger.info(var22);

				// ... not sure how dents are being created.
				// how noise is being represented in y-axis
				// does heightMap hold information about y-axis?

				//2 final double example_i = 0.0; // it appears that values [1-16] leave world empty ?
				//2 double var27 = (example_i - var22) * 12.0 / valueA; // in general negative numbers.
				//2 for (int i = 0; i < yLength; ++i) {
				//2 	this.heightMap[mainIndex] -= var27;
				//2 	++mainIndex;
				//2 }

				// UNCOMMENT ME !
				//SeriousGenMod.logger.info("loop");
				//1 for (int i = 0; i < yLength; ++i) { // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16
				//1
				//1 	double result = 0;
				//1 	double var27 = ((double)0 - var22) * 12.0 / valueA; // in general negative numbers.
				//1 	final double flatness = 4.0f;
				//1
				//1 	//SeriousGenMod.logger.info("value: " + var27);
				//1
				//1 	// It means higher low grounds.
				//1 	//  high grounds stay unaffected.
				//1 	//  everything that would generate below somewhere around ~[8.0 - -64.0]
				//1 	//  will be raised.
				//1 	if (var27 < 0.0) var27 *= flatness;
				//1
				//1 	// Those add the 3rd noise effect.
				//1 	//  all the grumpiness, gigantic and smaller hills, depths.
				//1 	double valueC = (this.ccc[mainIndex] / 10.0 + 1.0) / 2.0; // in range of ~[-10.00 - 10.00]
				//1 	double min = this.ddd[mainIndex] / 512.0; // in range of ~[-64.00 - 64.00]
				//1 	double max = this.eee[mainIndex] / 512.0; // in range of ~[-64.00 - 64.00]
				//1
				//1 	//SeriousGenMod.logger.info("min: " + min + ", max: " + max + ", value: " + valueC);
				//1
				//1 	if (valueC < 0.0) result = min;
				//1 	else if (valueC > 1.0) result = max;
				//1 	else result = min + (max - min) * valueC;
				//1
				//1 	result -= var27;
				//1
				//1 	// No idea, but works without.
				//1 	//if (i > yLength - 4) {
				//1 	//	double var45 = (double)((float)(i - (yLength - 4)) / 3.0F);
				//1 	//	result = result * (1.0 - var45) + -10.0 * var45;
				//1 	//}
				//1
				//1 	//if ((double)i < 0.0) {
				//1 	//	SeriousGenMod.logger.info("what-here");
				//1 	//	double var45 = (0.0 - (double)i) / 4.0;
				//1 	//	if (var45 < 0.0) var45 = 0.0;
				//1 	//	if (var45 > 1.0) var45 = 1.0;
				//1 	//	result = result * (1.0 - var45) + -10.0 * var45;
				//1 	//}
				//1
				//1 	this.heightMap[mainIndex] = result;
				//1 	++mainIndex;
				//1 }
				++index;
			}
		}
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
