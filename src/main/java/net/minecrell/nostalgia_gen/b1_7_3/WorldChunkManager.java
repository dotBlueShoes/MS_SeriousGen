package net.minecrell.nostalgia_gen.b1_7_3;

import java.util.Random;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

public class WorldChunkManager {
   private NoiseGeneratorOctaves2 temperatureNoise;
   private NoiseGeneratorOctaves2 humidityNoise;
   private NoiseGeneratorOctaves2 field_4192_g;
   public double[] temperature;
   public double[] humidity;
   public double[] field_4196_c;
   public BiomeGenBase[] field_4195_d;

   protected WorldChunkManager() {
   }

   public WorldChunkManager(World world) {
      this.temperatureNoise = new NoiseGeneratorOctaves2(new Random(world.getSeed() * 9871L), 4);
      this.humidityNoise = new NoiseGeneratorOctaves2(new Random(world.getSeed() * 39811L), 4);
      this.field_4192_g = new NoiseGeneratorOctaves2(new Random(world.getSeed() * 543321L), 2);
   }

   public BiomeGenBase getBiomeGenAtChunkCoord(ChunkPos var1) {
      return this.getBiomeGenAt(var1.x << 4, var1.z << 4);
   }

   public BiomeGenBase getBiomeGenAt(int var1, int var2) {
      return this.func_4069_a(var1, var2, 1, 1)[0];
   }

   public double getTemperature(int var1, int var2) {
      this.temperature = this.temperatureNoise.func_4112_a(this.temperature, (double)var1, (double)var2, 1, 1, 0.02500000037252903, 0.02500000037252903, 0.5);
      return this.temperature[0];
   }

   public BiomeGenBase[] func_4069_a(int var1, int var2, int var3, int var4) {
      this.field_4195_d = this.loadBlockGeneratorData(this.field_4195_d, var1, var2, var3, var4);
      return this.field_4195_d;
   }

   public double[] getTemperatures(double[] var1, int var2, int var3, int var4, int var5) {
      if (var1 == null || var1.length < var4 * var5) {
         var1 = new double[var4 * var5];
      }

      var1 = this.temperatureNoise.func_4112_a(var1, (double)var2, (double)var3, var4, var5, 0.02500000037252903, 0.02500000037252903, 0.25);
      this.field_4196_c = this.field_4192_g.func_4112_a(this.field_4196_c, (double)var2, (double)var3, var4, var5, 0.25, 0.25, 0.5882352941176471);
      int var6 = 0;

      for(int var7 = 0; var7 < var4; ++var7) {
         for(int var8 = 0; var8 < var5; ++var8) {
            double var9 = this.field_4196_c[var6] * 1.1 + 0.5;
            double var11 = 0.01;
            double var13 = 1.0 - var11;
            double var15 = (var1[var6] * 0.15 + 0.7) * var13 + var9 * var11;
            var15 = 1.0 - (1.0 - var15) * (1.0 - var15);
            if (var15 < 0.0) {
               var15 = 0.0;
            }

            if (var15 > 1.0) {
               var15 = 1.0;
            }

            var1[var6] = var15;
            ++var6;
         }
      }

      return var1;
   }

   public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] var1, int var2, int var3, int var4, int var5) {

      if (var1 == null || var1.length < var4 * var5) {
         var1 = new BiomeGenBase[var4 * var5];
      }

      this.temperature = this.temperatureNoise.func_4112_a(this.temperature, (double)var2, (double)var3, var4, var4, 0.02500000037252903, 0.02500000037252903, 0.25);
      this.humidity = this.humidityNoise.func_4112_a(this.humidity, (double)var2, (double)var3, var4, var4, 0.05000000074505806, 0.05000000074505806, 0.3333333333333333);
      this.field_4196_c = this.field_4192_g.func_4112_a(this.field_4196_c, (double)var2, (double)var3, var4, var4, 0.25, 0.25, 0.5882352941176471);
      int var6 = 0;

      for(int var7 = 0; var7 < var4; ++var7) {
         for(int var8 = 0; var8 < var5; ++var8) {
            double var9 = this.field_4196_c[var6] * 1.1 + 0.5;
            double var11 = 0.01;
            double var13 = 1.0 - var11;
            double var15 = (this.temperature[var6] * 0.15 + 0.7) * var13 + var9 * var11;
            var11 = 0.002;
            var13 = 1.0 - var11;
            double var17 = (this.humidity[var6] * 0.15 + 0.5) * var13 + var9 * var11;
            var15 = 1.0 - (1.0 - var15) * (1.0 - var15);

            if (var15 < 0.0) {
               var15 = 0.0;
            }

            if (var17 < 0.0) {
               var17 = 0.0;
            }

            if (var15 > 1.0) {
               var15 = 1.0;
            }

            if (var17 > 1.0) {
               var17 = 1.0;
            }

            this.temperature[var6] = var15;
            this.humidity[var6] = var17;
            var1[var6++] = BiomeGenBase.getBiomeFromLookup(var15, var17);
         }
      }

      return var1;
   }
}
