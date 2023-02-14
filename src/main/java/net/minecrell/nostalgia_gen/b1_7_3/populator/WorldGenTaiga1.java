package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenTaiga1 extends WorldGenerator {
   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      int var6 = random.nextInt(5) + 7;
      int var7 = var6 - random.nextInt(2) - 3;
      int var8 = var6 - var7;
      int var9 = 1 + random.nextInt(var8 + 1);
      boolean var10 = true;
      if (baseY >= 1 && baseY + var6 + 1 <= 128) {
         int y;
         int x;
         int var20;
         for(y = baseY; y <= baseY + 1 + var6 && var10; ++y) {
            //int var12 = true;
            if (y - baseY < var7) {
               var20 = 0;
            } else {
               var20 = var9;
            }

            for(y = baseX - var20; y <= baseX + var20 && var10; ++y) {
               for(x = baseZ - var20; x <= baseZ + var20 && var10; ++x) {
                  if (y >= 0 && y < 128) {
                     Block block = NostalgiaGenHelper.getBlock(world, y, y, x);
                     if (block != Blocks.AIR && block != Blocks.LEAVES) {
                        var10 = false;
                     }
                  } else {
                     var10 = false;
                  }
               }
            }
         }

         if (!var10) {
            return false;
         } else {
            Block var18 = NostalgiaGenHelper.getBlock(world, baseX, baseY - 1, baseZ);
            if ((var18 == Blocks.GRASS || var18 == Blocks.DIRT) && baseY < 128 - var6 - 1) {
               NostalgiaGenHelper.setBlock(world, baseX, baseY - 1, baseZ, Blocks.DIRT);
               var20 = 0;

               for(y = baseY + var6; y >= baseY + var7; --y) {
                  for(x = baseX - var20; x <= baseX + var20; ++x) {
                     int var25 = x - baseX;

                     for(int z = baseZ - var20; z <= baseZ + var20; ++z) {
                        int var17 = z - baseZ;
                        if ((Math.abs(var25) != var20 || Math.abs(var17) != var20 || var20 <= 0) && !NostalgiaGenHelper.getBlockState(world, x, y, z).isOpaqueCube()) {
                           NostalgiaGenHelper.setBlock(world, x, y, z, Blocks.LEAVES, EnumType.SPRUCE);
                        }
                     }
                  }

                  if (var20 >= 1 && y == baseY + var7 + 1) {
                     --var20;
                  } else if (var20 < var9) {
                     ++var20;
                  }
               }

               for(y = 0; y < var6 - 1; ++y) {
                  Block block = NostalgiaGenHelper.getBlock(world, baseX, baseY + y, baseZ);
                  if (block == Blocks.AIR || block == Blocks.LEAVES) {
                     NostalgiaGenHelper.setBlock(world, baseX, baseY + y, baseZ, Blocks.LOG, EnumType.SPRUCE);
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
