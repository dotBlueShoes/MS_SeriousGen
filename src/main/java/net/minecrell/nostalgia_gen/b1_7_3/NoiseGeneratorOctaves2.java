package net.minecrell.nostalgia_gen.b1_7_3;

import java.util.Random;

public class NoiseGeneratorOctaves2 extends NoiseGenerator {
   private NoiseGenerator2[] generatorCollection;
   private int field_4233_b;

   public NoiseGeneratorOctaves2(Random random, int var2) {
      this.field_4233_b = var2;
      this.generatorCollection = new NoiseGenerator2[var2];

      for(int var3 = 0; var3 < var2; ++var3) {
         this.generatorCollection[var3] = new NoiseGenerator2(random);
      }

   }

   public double[] func_4112_a(double[] var1, double var2, double var4, int var6, int var7, double var8, double var10, double var12) {
      return this.func_4111_a(var1, var2, var4, var6, var7, var8, var10, var12, 0.5);
   }

   public double[] func_4111_a(double[] var1, double var2, double var4, int var6, int var7, double var8, double var10, double var12, double var14) {
      var8 /= 1.5;
      var10 /= 1.5;
      if (var1 != null && var1.length >= var6 * var7) {
         for(int var16 = 0; var16 < var1.length; ++var16) {
            var1[var16] = 0.0;
         }
      } else {
         var1 = new double[var6 * var7];
      }

      double var23 = 1.0;
      double var18 = 1.0;

      for(int var20 = 0; var20 < this.field_4233_b; ++var20) {
         this.generatorCollection[var20].func_4157_a(var1, var2, var4, var6, var7, var8 * var18, var10 * var18, 0.55 / var23);
         var18 *= var12;
         var23 *= var14;
      }

      return var1;
   }
}
