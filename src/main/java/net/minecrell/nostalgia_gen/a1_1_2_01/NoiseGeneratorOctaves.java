package net.minecrell.nostalgia_gen.a1_1_2_01;

import java.util.Random;

public class NoiseGeneratorOctaves extends NoiseGenerator {
   private NoiseGeneratorPerlin[] generatorCollection;
   private int field_1191_b;

   public NoiseGeneratorOctaves(Random random, int var2) {
      this.field_1191_b = var2;
      this.generatorCollection = new NoiseGeneratorPerlin[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.generatorCollection[var3] = new NoiseGeneratorPerlin(random);
      }

   }

   public double func_806_a(double var1, double var3) {
      double var5 = 0.0;
      double var7 = 1.0;

      for(int var9 = 0; var9 < this.field_1191_b; ++var9) {
         var5 += this.generatorCollection[var9].func_801_a(var1 * var7, var3 * var7) / var7;
         var7 /= 2.0;
      }

      return var5;
   }

   public double[] generateNoiseOctaves(double[] outArray, double x, double y, double z, int var8, int var9, int var10, double var11, double var13, double var15) {
      if (outArray == null) {
         outArray = new double[var8 * var9 * var10];
      } else {
         for(int var17 = 0; var17 < outArray.length; ++var17) {
            outArray[var17] = 0.0;
         }
      }

      double var20 = 1.0;

      for(int var19 = 0; var19 < this.field_1191_b; ++var19) {
         this.generatorCollection[var19].func_805_a(outArray, x, y, z, var8, var9, var10, var11 * var20, var13 * var20, var15 * var20, var20);
         var20 /= 2.0;
      }

      return outArray;
   }
}
