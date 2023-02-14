package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenForest extends WorldGenerator {
   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      int var6 = random.nextInt(3) + 5;
      boolean var7 = true;
      if (baseY >= 1 && baseY + var6 + 1 <= 128) {
         int y;
         int var19;
         int var21;
         for(y = baseY; y <= baseY + 1 + var6; ++y) {
            y = 1;
            if (y == baseY) {
               y = 0;
            }

            if (y >= baseY + 1 + var6 - 2) {
               y = 2;
            }

            for(var19 = baseX - y; var19 <= baseX + y && var7; ++var19) {
               for(var21 = baseZ - y; var21 <= baseZ + y && var7; ++var21) {
                  if (y >= 0 && y < 128) {
                     Block block = NostalgiaGenHelper.getBlock(world, var19, y, var21);
                     if (block != Blocks.AIR && block != Blocks.LEAVES) {
                        var7 = false;
                     }
                  } else {
                     var7 = false;
                  }
               }
            }
         }

         if (!var7) {
            return false;
         } else {
            Block var16 = NostalgiaGenHelper.getBlock(world, baseX, baseY - 1, baseZ);
            if ((var16 == Blocks.GRASS || var16 == Blocks.DIRT) && baseY < 128 - var6 - 1) {
               NostalgiaGenHelper.setBlock(world, baseX, baseY - 1, baseZ, Blocks.DIRT);

               for(y = baseY - 3 + var6; y <= baseY + var6; ++y) {
                  var19 = y - (baseY + var6);
                  var21 = 1 - var19 / 2;

                  for(int x = baseX - var21; x <= baseX + var21; ++x) {
                     int var13 = x - baseX;

                     for(int z = baseZ - var21; z <= baseZ + var21; ++z) {
                        int var15 = z - baseZ;
                        if ((Math.abs(var13) != var21 || Math.abs(var15) != var21 || random.nextInt(2) != 0 && var19 != 0) && !NostalgiaGenHelper.getBlockState(world, x, y, z).isOpaqueCube()) {
                           NostalgiaGenHelper.setBlock(world, x, y, z, Blocks.LEAVES, EnumType.BIRCH);
                        }
                     }
                  }
               }

               for(y = 0; y < var6; ++y) {
                  Block block = NostalgiaGenHelper.getBlock(world, baseX, baseY + y, baseZ);
                  if (block == Blocks.AIR || block == Blocks.LEAVES) {
                     NostalgiaGenHelper.setBlock(world, baseX, baseY + y, baseZ, Blocks.LOG, EnumType.BIRCH);
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }
}
