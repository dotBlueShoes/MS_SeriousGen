package net.minecrell.nostalgia_gen.a1_1_2_01;

import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenBase {
   protected int field_1306_a = 8;
   protected Random rand = new Random();

   public void generate(World world, int chunkX, int chunkZ, ChunkPrimer chunk) {
      int var6 = this.field_1306_a;
      this.rand.setSeed(world.getSeed());
      long var7 = this.rand.nextLong() / 2L * 2L + 1L;
      long var9 = this.rand.nextLong() / 2L * 2L + 1L;

      for(int x = chunkX - var6; x <= chunkX + var6; ++x) {
         for(int z = chunkZ - var6; z <= chunkZ + var6; ++z) {
            this.rand.setSeed((long)x * var7 + (long)z * var9 ^ world.getSeed());
            this.func_868_a(world, x, z, chunkX, chunkZ, chunk);
         }
      }

   }

   protected void func_868_a(World world, int x, int z, int chunkX, int chunkZ, ChunkPrimer chunk) {
   }
}
