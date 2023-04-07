package dotblueshoes.serious_gen;

import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecrell.nostalgia_gen.b1_7_3.MathHelper;

import java.util.Arrays;
import java.util.Random;

public class BiomesManager {

	// colorizing blocks - https://forums.minecraftforge.net/topic/63956-112-make-custom-grass-block-use-the-colour-map/

	private static final byte STANDARD_BIOME = (byte) Biome.getIdForBiome(Biomes.JUNGLE);

	private NoiseGeneratorOctaves mainNoise, temperatureNoise, humidityNoise;
	public double[]
		temperatures = new double[16 * 16],
		rainfalls = new double[16 * 16],
		mains = new double[16 * 16];

	public double
		temperatureMin = 0.5, temperatureMax = 0.5,
		rainfallMin = 0.5, rainfallMax = 0.5;

	public BiomesManager(final World world) {
		this.temperatureNoise = new NoiseGeneratorOctaves(new Random(world.getSeed() * 9871L), 4);
		this.humidityNoise = new NoiseGeneratorOctaves(new Random(world.getSeed() * 39811L), 4);
		this.mainNoise = new NoiseGeneratorOctaves(new Random(world.getSeed() * 543321L), 2);
	}

	public void LogMinMax() {
		SeriousGenMod.logger.info(
			"\nTMin: " + this.temperatureMin +
			"\nTMax: " + this.temperatureMax +
			"\nHMin: " + this.rainfallMin +
			"\nHMax: " + this.rainfallMax
		);
	}

	// We're passing the chuck as if it is from block (32 to +16) in X direction and (64 to +16) in Z direction.
	public void generateChunkBiomeData(byte[] chunkBiomes, final int chunkXInBlocks, final int chunkZInBlocks, final int chunkSizeX, final int chunkSizeZ) {

		Arrays.fill(chunkBiomes, STANDARD_BIOME);

		//if (var1 == null || var1.length < var4 * var5) {
		//	var1 = new BiomeGenBase[var4 * var5];
		//}

		//this.temperatureNoise.generateNoiseOctavesXZ(
		//	this.temperatures,
		//	chunkXInBlocks, chunkZInBlocks,
		//	chunkSizeX, chunkSizeZ,
		//	0.025, 0.025,
		//	0.25, 0.5
		//);

		//this.humidityNoise.generateNoiseOctavesXZ(
		//	this.rainfalls,
		//	chunkXInBlocks, chunkZInBlocks,
		//	chunkSizeX, chunkSizeZ,
		//	0.025, 0.025,
		//	0.25, 0.5
		//);

		this.temperatureNoise.generateNoiseOctavesXZ(
			this.temperatures,
			chunkXInBlocks, chunkZInBlocks,
			chunkSizeX, chunkSizeZ,
			0.025, 0.025,
			0.25, 0.5
		);

		this.humidityNoise.generateNoiseOctavesXZ(
			this.rainfalls,
			chunkXInBlocks, chunkZInBlocks,
			chunkSizeX, chunkSizeZ,
			0.05, 0.05,
			0.3333333333333333, 0.5
		);

		this.mainNoise.generateNoiseOctavesXZ(
			this.mains,
			chunkXInBlocks, chunkZInBlocks,
			chunkSizeX, chunkSizeZ,
			0.25, 0.25,
			0.5882352941176471, 0.5
		);

		// Values are negative without that loop here...

		int index;
		for (int x = 0; x < chunkSizeX; ++x) {
			for (int z = 0; z < chunkSizeZ; ++z) {

				index = x * chunkSizeX + z;

				double main = this.mains[index] * 1.1 + 0.5;
				double temperature = (this.temperatures[index] * 0.15 + 0.7) * (1.0 - 0.01) + (main * 0.01);
				double rainfall = (this.rainfalls[index] * 0.15 + 0.5) * (1.0 - 0.002) + (main * 0.002);

				temperature = 1.0 - (1.0 - temperature) * (1.0 - temperature);
				if (temperature < 0.0) temperature = 0.0;
				else if (temperature > 1.0) temperature = 1.0;

				if (rainfall < 0.0) rainfall = 0.0;
				else if (rainfall > 1.0) rainfall = 1.0;

				this.temperatures[index] = temperature;
				this.rainfalls[index] = rainfall;
			}

				//double var9 = this.field_4196_c[var6] * 1.1 + 0.5;
				//double var11 = 0.01;
				//double var13 = 1.0 - var11;
				//double var15 = (this.temperature[var6] * 0.15 + 0.7) * var13 + var9 * var11;
				//var11 = 0.002;
				//var13 = 1.0 - var11;
				//double var17 = (this.humidity[var6] * 0.15 + 0.5) * var13 + var9 * var11;
				//var15 = 1.0 - (1.0 - var15) * (1.0 - var15);
				//if (var15 < 0.0) {
				//	var15 = 0.0;
				//}
				//if (var17 < 0.0) {
				//	var17 = 0.0;
				//}
				//if (var15 > 1.0) {
				//	var15 = 1.0;
				//}
				//if (var17 > 1.0) {
				//	var17 = 1.0;
				//}
				//this.temperature[var6] = var15;
				//this.humidity[var6] = var17;
				////var1[var6++] = BiomeGenBase.getBiomeFromLookup(var15, var17);
			//}
		}

		for (int i = 0; i < 16*16; i++) {

			if (temperatures[i] < temperatureMin) {
				temperatureMin = temperatures[i];
				LogMinMax();
			} else if (temperatures[i] > temperatureMax) {
				temperatureMax = temperatures[i];
				LogMinMax();
			}

			if (rainfalls[i] < rainfallMin) {
				rainfallMin = rainfalls[i];
				LogMinMax();
			}else if (rainfalls[i] > rainfallMax) {
				rainfallMax = rainfalls[i];
				LogMinMax();
			}

		}

	}

}
