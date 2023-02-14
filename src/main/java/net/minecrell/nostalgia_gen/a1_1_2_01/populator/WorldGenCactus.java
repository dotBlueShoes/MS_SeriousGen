package net.minecrell.nostalgia_gen.a1_1_2_01.populator;

import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenCactus extends WorldGenerator {
   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      for(int i = 0; i < 10; ++i) {
         int x = baseX + random.nextInt(8) - random.nextInt(8);
         int var8 = baseY + random.nextInt(4) - random.nextInt(4);
         int z = baseZ + random.nextInt(8) - random.nextInt(8);
         if (NostalgiaGenHelper.getBlock(world, x, var8, z) == Blocks.AIR) {
            int var10 = 1 + random.nextInt(random.nextInt(3) + 1);

            for(int y = 0; y < var10; ++y) {
               if (Blocks.CACTUS.canBlockStay(world, new BlockPos(x, var8 + y, z))) {
                  NostalgiaGenHelper.setBlock(world, x, var8 + y, z, Blocks.CACTUS);
               }
            }
         }
      }

      return true;
   }
}
