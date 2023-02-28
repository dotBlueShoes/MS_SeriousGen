package dotblueshoes.serious_gen;

import java.util.Random;

public class NoiseGenerator {

	private int[] permutations;
	public double xCoord, yCoord, zCoord;

	public NoiseGenerator(Random rand) {
		this.permutations = new int[512];
		this.xCoord = rand.nextDouble() * 256.0;
		this.yCoord = rand.nextDouble() * 256.0;
		this.zCoord = rand.nextDouble() * 256.0;

		// Fill first half of permutations array with sequential numbers 0 to 255.
		for(int i = 0; i < 256; this.permutations[i] = i++);

		// Randomly rearrange the numbers 0 to 255 and repeat the sequence.
		for(int i = 0; i < 256; ++i) {
			int j = rand.nextInt(256 - i) + i;

			// SWAP values at positions i and j.
			int value = this.permutations[i];
			this.permutations[i] = this.permutations[j];
			this.permutations[j] = value;

			// Repeat permutations in second half.
			this.permutations[i + 256] = this.permutations[i];
		}

	}

	public double lerp(double endValue, double startValue, double precentage) {
		return startValue + endValue * (precentage - startValue);
	}

	public double grad(int var1, double var2, double var4, double var6) {
		int var8 = var1 & 15;
		double var9 = var8 < 8 ? var2 : var4;
		double var11 = var8 < 4 ? var4 : (var8 != 12 && var8 != 14 ? var6 : var2);
		return ((var8 & 1) == 0 ? var9 : -var9) + ((var8 & 2) == 0 ? var11 : -var11);
	}

	//public double[] populateNoiseArrayXZ(
	//	double x, double z,
	//	int numX, int numY, int numZ,
	//	double waveLengthX, double waveLengthY, double waveLengthZ,
	//	double amplitude
	//) {
//
	//	double[] result = new double[numX * numZ];
	//	int index = 0;
//
	//	//double var20 = 1.0 / amplitude;
	//	int var22 = -1;
//
	//	double var29 = 0.0;
	//	double var31 = 0.0;
	//	double var33 = 0.0;
	//	double var35 = 0.0;
//
	//	for (int i = 0; i < numX; ++i) {
	//		double u = (x + (double) i) * waveLengthX + this.xCoord;
//
	//		int var40 = (int) u;
	//		if (u < (double) var40) {
	//			--var40;
	//		}
//
	//		int var41 = var40 & 255;
	//		u -= (double) var40;
	//		double var42 = u * u * u * (u * (u * 6.0 - 15.0) + 10.0);
//
	//		for (int j = 0; j < numZ; ++j) {
	//			double v = (z + (double) j) * waveLengthZ + this.zCoord;
//
	//			int var47 = (int) v;
	//			if (v < (double) var47) {
	//				--var47;
	//			}
//
	//			int var48 = var47 & 255;
	//			v -= (double) var47;
	//			double var49 = v * v * v * (v * (v * 6.0 - 15.0) + 10.0);
//
	//			for (int k = 0; k < numY; ++k) {
	//				double w = ((double) k) * waveLengthY + this.yCoord;
	//				int var54 = (int) w;
	//				if (w < (double) var54) {
	//					--var54;
	//				}
//
	//				int var55 = var54 & 255;
	//				w -= (double) var54;
	//				double var56 = w * w * w * (w * (w * 6.0 - 15.0) + 10.0);
//
	//				if (k == 0 || var55 != var22) {
//
	//					int perm1 = this.permutations[var41] + var55;
	//					int perm2 = this.permutations[perm1] + var48;
	//					int perm3 = this.permutations[perm1 + 1] + var48;
	//					int perm4 = this.permutations[var41 + 1] + var55;
	//					int perm5 = this.permutations[perm4] + var48;
	//					int perm6 = this.permutations[perm4 + 1] + var48;
//
	//					var22 = var55;
//
	//					var29 = this.lerp(var42, this.grad(this.permutations[perm2], u, w, v), this.grad(this.permutations[perm5], u - 1.0, w, v));
	//					var31 = this.lerp(var42, this.grad(this.permutations[perm3], u, w - 1.0, v), this.grad(this.permutations[perm6], u - 1.0, w - 1.0, v));
	//					var33 = this.lerp(var42, this.grad(this.permutations[perm2 + 1], u, w, v - 1.0), this.grad(this.permutations[perm5 + 1], u - 1.0, w, v - 1.0));
	//					var35 = this.lerp(var42, this.grad(this.permutations[perm3 + 1], u, w - 1.0, v - 1.0), this.grad(this.permutations[perm6 + 1], u - 1.0, w - 1.0, v - 1.0));
	//				}
//
	//				double var58 = this.lerp(var56, var29, var31);
	//				double var60 = this.lerp(var56, var33, var35);
	//				double var62 = this.lerp(var49, var58, var60);
	//				int var10001 = index++;
//
	//				result[var10001] += var62 * amplitude;
	//			}
	//		}
	//	}
//
	//	return result;
	//}

	public void populateNoiseArrayXZ(
		double[] array,
		int chunkX, int chunkZ,
		int numX, int numZ,
		double scaleX, double scaleZ,
		double oscillation
	) {

		int chunkY = 0;
		int numY = 1;
		double scaleY = 0;


		int index = 0;
		double amplitude = 1.0 / oscillation;
		int previousWFractionalPart = -1;

		double var29 = 0.0;
		double var31 = 0.0;
		double var33 = 0.0;
		double var35 = 0.0;

		for (int x = 0; x < numX; ++x) {
			double v = (chunkX + (double) x) * scaleX + this.xCoord;

			// Separate integer and fractional parts
			int vIntegralPart = (int)v;
			if (v < (double)vIntegralPart) --vIntegralPart;

			int vFractionalPart = vIntegralPart & 255;
			v -= vIntegralPart;

			// Smoothing function
			double tv = v * v * v * (v * (v * 6.0 - 15.0) + 10.0);

			// Is this how it should be written mr. Notch ?
			//double vFractionalPart = v - vIntegralPart;
			//vIntegralPart &= 255;
			//double tv = vFractionalPart * vFractionalPart * vFractionalPart * (vFractionalPart * (vFractionalPart * 6.0D - 15.0D) + 10.0D);

			for (int z = 0; z < numZ; ++z) {
				double u = (chunkZ + (double) z) * scaleZ + this.zCoord;

				// Separate integer and fractional parts
				int uIntegralPart = (int)u;
				if (u < (double)uIntegralPart) --uIntegralPart;
				int uFractionalPart = uIntegralPart & 255;
				u -= uIntegralPart;

				// Smoothing function
				double tu = u * u * u * (u * (u * 6.0 - 15.0) + 10.0);

				for (int y = 0; y < numY; ++y) {
					double w = (chunkY + (double) y) * scaleY + this.yCoord;

					// Separate integer and fractional parts
					int wIntegralPart = (int) w;
					if (w < (double)wIntegralPart) --wIntegralPart;
					int wFractionalPart = wIntegralPart & 255;
					w -= wIntegralPart;

					// Smoothing function
					double tw = w * w * w * (w * (w * 6.0 - 15.0) + 10.0);

					if (y == 0 || wFractionalPart != previousWFractionalPart) {
						previousWFractionalPart = wFractionalPart;

						// LOGGING
						//SeriousGenMod.logger.info((int)vFractionalPart);

						// (int)vFractionalPart is some random number.
						// others are sequential indexes in array.
						// those values are random.
						// But why does it matter to pick right permutation ???

						int perm1 = this.permutations[(int)vFractionalPart] + wFractionalPart;
						int perm2 = this.permutations[perm1] + uFractionalPart;
						int perm3 = this.permutations[perm1 + 1] + uFractionalPart;

						int perm4 = this.permutations[(int)vFractionalPart + 1] + wFractionalPart;
						int perm5 = this.permutations[perm4] + uFractionalPart;
						int perm6 = this.permutations[perm4 + 1] + uFractionalPart;

						var29 = this.lerp(tv, this.grad(this.permutations[perm2], v, w, u), this.grad(this.permutations[perm5], v - 1.0, w, u));
						var31 = this.lerp(tv, this.grad(this.permutations[perm3], v, w - 1.0, u), this.grad(this.permutations[perm6], v - 1.0, w - 1.0, u));
						var33 = this.lerp(tv, this.grad(this.permutations[perm2 + 1], v, w, u - 1.0), this.grad(this.permutations[perm5 + 1], v - 1.0, w, u - 1.0));
						var35 = this.lerp(tv, this.grad(this.permutations[perm3 + 1], v, w - 1.0, u - 1.0), this.grad(this.permutations[perm6 + 1], v - 1.0, w - 1.0, u - 1.0));
					}

					// tri-linear interpolation
					double valFirst = this.lerp(tw, var29, var31);
					double valSecond = this.lerp(tw, var33, var35);
					double value = this.lerp(tu, valFirst, valSecond);

					array[index++] += value * amplitude;
				}
			}
		}
	}

	//int chunkX, int chunkY, int chunkZ,
	//int numX, int numY, int numZ,
	//double scaleX, double scaleY, double scaleZ

	public void populateNoiseArrayXYZ(
		double[] array,
		int chunkX, int chunkY, int chunkZ,
		int numX, int numY, int numZ,
		double scaleX, double scaleY, double scaleZ,
		double oscillation
	) {

		int index = 0;
		double amplitude = 1.0 / oscillation;
		int previousWFractionalPart = -1;

		double var29 = 0.0;
		double var31 = 0.0;
		double var33 = 0.0;
		double var35 = 0.0;

		for (int x = 0; x < numX; ++x) {
			double v = (chunkX + (double) x) * scaleX + this.xCoord;

			// Separate integer and fractional parts
			int vIntegralPart = (int)v;
			if (v < (double)vIntegralPart) --vIntegralPart;

			int vFractionalPart = vIntegralPart & 255;
			v -= vIntegralPart;

			// Smoothing function
			double tv = v * v * v * (v * (v * 6.0 - 15.0) + 10.0);

			// Is this how it should be written mr. Notch ?
			//double vFractionalPart = v - vIntegralPart;
			//vIntegralPart &= 255;
			//double tv = vFractionalPart * vFractionalPart * vFractionalPart * (vFractionalPart * (vFractionalPart * 6.0D - 15.0D) + 10.0D);

			for (int z = 0; z < numZ; ++z) {
				double u = (chunkZ + (double) z) * scaleZ + this.zCoord;

				// Separate integer and fractional parts
				int uIntegralPart = (int)u;
				if (u < (double)uIntegralPart) --uIntegralPart;
				int uFractionalPart = uIntegralPart & 255;
				u -= uIntegralPart;

				// Smoothing function
				double tu = u * u * u * (u * (u * 6.0 - 15.0) + 10.0);

				for (int y = 0; y < numY; ++y) {
					double w = (chunkY + (double) y) * scaleY + this.yCoord;

					// Separate integer and fractional parts
					int wIntegralPart = (int) w;
					if (w < (double)wIntegralPart) --wIntegralPart;
					int wFractionalPart = wIntegralPart & 255;
					w -= wIntegralPart;

					// Smoothing function
					double tw = w * w * w * (w * (w * 6.0 - 15.0) + 10.0);

					if (y == 0 || wFractionalPart != previousWFractionalPart) {
						previousWFractionalPart = wFractionalPart;

						// LOGGING
						//SeriousGenMod.logger.info((int)vFractionalPart);

						// (int)vFractionalPart is some random number.
						// others are sequential indexes in array.
						// those values are random.
						// But why does it matter to pick right permutation ???

						int perm1 = this.permutations[(int)vFractionalPart] + wFractionalPart;
						int perm2 = this.permutations[perm1] + uFractionalPart;
						int perm3 = this.permutations[perm1 + 1] + uFractionalPart;

						int perm4 = this.permutations[(int)vFractionalPart + 1] + wFractionalPart;
						int perm5 = this.permutations[perm4] + uFractionalPart;
						int perm6 = this.permutations[perm4 + 1] + uFractionalPart;

						var29 = this.lerp(tv, this.grad(this.permutations[perm2], v, w, u), this.grad(this.permutations[perm5], v - 1.0, w, u));
						var31 = this.lerp(tv, this.grad(this.permutations[perm3], v, w - 1.0, u), this.grad(this.permutations[perm6], v - 1.0, w - 1.0, u));
						var33 = this.lerp(tv, this.grad(this.permutations[perm2 + 1], v, w, u - 1.0), this.grad(this.permutations[perm5 + 1], v - 1.0, w, u - 1.0));
						var35 = this.lerp(tv, this.grad(this.permutations[perm3 + 1], v, w - 1.0, u - 1.0), this.grad(this.permutations[perm6 + 1], v - 1.0, w - 1.0, u - 1.0));
					}

					// tri-linear interpolation
					double valFirst = this.lerp(tw, var29, var31);
					double valSecond = this.lerp(tw, var33, var35);
					double value = this.lerp(tu, valFirst, valSecond);

					array[index++] += value * amplitude;
				}
			}
		}
	}
}
