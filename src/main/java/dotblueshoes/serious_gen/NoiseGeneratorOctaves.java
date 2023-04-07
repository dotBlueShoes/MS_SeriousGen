package dotblueshoes.serious_gen;
import java.util.Random;

public class NoiseGeneratorOctaves {

	private NoiseGenerator[] generatorCollection;
	private int numOctaves;

	public NoiseGeneratorOctaves(final Random rand, final int numOctaves) {
		this.numOctaves = numOctaves;
		this.generatorCollection = new NoiseGenerator[numOctaves];

		for (int j = 0; j < numOctaves; ++j) {
			this.generatorCollection[j] = new NoiseGenerator(rand);
		}
	}

	// This method is called with every chunk.
	//public double[][] generateNoiseOctaves(
	//	int chunkX, int chunkZ,
	//	int numX, int numZ,
	//	double firstWaveLength,
	//	double amplitude
	//) {
	//	double[][] result = new double[this.numOctaves][numX * numZ];
//
	//	double wavelength = firstWaveLength;
	//	for (int i = 0; i < this.numOctaves; ++i) {
	//		result[i] = this.generatorCollection[i].populateNoiseArrayXZ(chunkX, chunkZ, numX, 0, numZ, wavelength, wavelength, wavelength, 1 / amplitude);
	//		wavelength *= 2;
	//	}
//
	//	return result;
	//}

	public void generateNoiseOctavesXZ(
		double[] outArray,
		int chunkX, int chunkZ,
		int numX, int numZ,
		double scaleX, double scaleZ
	) {
		double oscillation = 1.0;

		for (int i = 0; i < this.numOctaves; ++i) {
			this.generatorCollection[i].populateNoiseArrayXZ(
				outArray,
				chunkX, chunkZ,
				numX, numZ,
				scaleX * oscillation, scaleZ * oscillation,
				oscillation
			);
			oscillation /= 2.0;
		}
	}

	// I have no idea what's scale A and B are.
	public void generateNoiseOctavesXZ(
		double[] outArray,
		int chunkX, int chunkZ,
		int numX, int numZ,
		double scaleX, double scaleZ,
		double scaleA, double scaleB
	) {
		double oscillationA = 1.0, oscillationB = 1.0;;

		for(int i = 0; i < this.numOctaves; ++i) {

			this.generatorCollection[i].populateNoiseArrayXZ(
				outArray,
				chunkX, chunkZ,
				numX, numZ,
				scaleX * oscillationA,
				scaleZ * oscillationA,
				0.55 / oscillationB
			);

			oscillationA *= scaleA;
			oscillationB *= scaleB;
		}
	}

	public void generateNoiseOctavesXYZ(
		double[] outArray,
		int chunkX, int chunkY, int chunkZ,
		int numX, int numY, int numZ,
		double scaleX, double scaleY, double scaleZ
	) {
		double oscillation = 1.0;

		for (int i = 0; i < this.numOctaves; ++i) {
			this.generatorCollection[i].populateNoiseArrayXYZ(
				outArray,
				chunkX, chunkY, chunkZ,
				numX, numY, numZ,
				scaleX * oscillation, scaleY * oscillation, scaleZ * oscillation,
				oscillation
			);
			oscillation /= 2.0;
		}
	}

}
