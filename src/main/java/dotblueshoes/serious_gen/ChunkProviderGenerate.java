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
import net.minecrell.nostalgia_gen.a1_1_2_01.NoiseGeneratorOctaves;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static dotblueshoes.serious_gen.SeriousGenMod.logger;

public class ChunkProviderGenerate implements IChunkGenerator {

	private final World world;
	private final Random randomNumberGenerator;

	final int idk1 = 5, idk2 = 5, idk3 = 17;
	private final double[] heightMap = new double[idk1 * idk2 * idk3]; // = new double[256];

	final private NoiseGeneratorOctaves
		minLimitPerlinNoise,
		maxLimitPerlinNoise,
		mainPerlinNoise,
		//surfaceNoise,
		//stonePatchesNoise,
		unknownNoise,
		depthNoise;

	private double[]
		aaa,
		bbb,
		ccc,
		ddd,
		eee;

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

		this.generateHeightMap(chunkX, chunkZ);
		//logger.info("Hello There!");

		for (int x4 = 0; x4 < 4; ++x4) {
			for (int z4 = 0; z4 < 4; ++z4) {
				for (int height = 0; height < 16; ++height) {
					double var12 = 0.125;

					final int i1 = (x4 * idk1 + z4) * idk3;
					final int i2 = (x4 * idk1 + z4 + 1) * idk3;
					final int i3 = ((x4 + 1) * idk1 + z4) * idk3;
					final int i4 = ((x4 + 1) * idk1 + z4 + 1) * idk3;

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

								if (height * 8 + var30 < seaLevel) {
									block = Blocks.WATER;
								}

								if (var46 > 0.0) {
									block = Blocks.STONE;
								}

								if (block != null) {
									chunkPrimer.setBlockState(x, y, z, ((Block)block).getDefaultState());
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

		final double chunkXMultiplied = chunkX * 4;
		final double chunkZMultiplied = chunkZ * 4;

		final double var8 = 684.412;
		final double var10 = 684.412;

		final double wtf = 0.0;

		// Their size is quite random from what i can tell.
		this.aaa = this.unknownNoise.generateNoiseOctaves        (this.aaa, chunkXMultiplied, 0, chunkZMultiplied, idk1, 1, idk2, 1.0, 0.0, 1.0);
		this.bbb = this.depthNoise.generateNoiseOctaves          (this.bbb, chunkXMultiplied, 0, chunkZMultiplied, idk1, 1, idk2, 100.0, 0.0, 100.0);
		this.ccc = this.mainPerlinNoise.generateNoiseOctaves     (this.ccc, chunkXMultiplied, 0, chunkZMultiplied, idk1, idk3, idk2, var8 / 80.0, var10 / 160.0, var8 / 80.0);
		this.ddd = this.minLimitPerlinNoise.generateNoiseOctaves (this.ddd, chunkXMultiplied, 0, chunkZMultiplied, idk1, idk3, idk2, var8, var10, var8);
		this.eee = this.maxLimitPerlinNoise.generateNoiseOctaves (this.eee, chunkXMultiplied, 0, chunkZMultiplied, idk1, idk3, idk2, var8, var10, var8);

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

		for (int x = 0; x < idk1; ++x) {
			for (int y = 0; y < idk2; ++y) {

				double valueA = (this.aaa[index] + 256.0) / 512.0;
				if (valueA > 1.0) valueA = 1.0;

				double valueB = this.bbb[index] / 8000.0;
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
				valueB = valueB * (double)idk3 / 16.0;
				double var22 = (double)idk3 / 2.0 + valueB * 4.0;
				++index;

				for (int i = 0; i < idk3; ++i) {

					double result = 0.0;
					double var27 = ((double)i - var22) * 12.0 / valueA;

					if (var27 < 0.0) {
						var27 *= 4.0;
					}

					double valueC = (this.ccc[mainIndex] / 10.0 + 1.0) / 2.0;
					double valueD = this.ddd[mainIndex] / 512.0;
					double valueE = this.eee[mainIndex] / 512.0;


					if (valueC < 0.0) result = valueD;
					else if (valueC > 1.0) result = valueE;
					else result = valueD + (valueE - valueD) * valueC;

					result -= var27;
					double var45;

					if (i > idk3 - 4) {
						var45 = (double)((float)(i - (idk3 - 4)) / 3.0F);
						result = result * (1.0 - var45) + -10.0 * var45;
					}

					if ((double)i < wtf) {
						var45 = (wtf - (double)i) / 4.0;
						if (var45 < 0.0) {
							var45 = 0.0;
						}
						if (var45 > 1.0) {
							var45 = 1.0;
						}
						result = result * (1.0 - var45) + -10.0 * var45;
					}

					this.heightMap[mainIndex] = result;
					++mainIndex;
				}
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
