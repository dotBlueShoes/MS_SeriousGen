package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenTaiga2 extends WorldGenerator {
   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      int var6 = random.nextInt(4) + 6;
      int var7 = 1 + random.nextInt(2);
      int var8 = var6 - var7;
      int var9 = 2 + random.nextInt(2);
      boolean var10 = true;
      if (baseY >= 1 && baseY + var6 + 1 <= 128) {
         int var24;
         int var23;
         for(int y = baseY; y <= baseY + 1 + var6 && var10; ++y) {
            //int var12 = true;
            if (y - baseY < var7) {
               var23 = 0;
            } else {
               var23 = var9;
            }

            for(var24 = baseX - var23; var24 <= baseX + var23 && var10; ++var24) {
               for(int z = baseZ - var23; z <= baseZ + var23 && var10; ++z) {
                  if (y >= 0 && y < 128) {
                     Block block = NostalgiaGenHelper.getBlock(world, var24, y, z);
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
            Block var21 = NostalgiaGenHelper.getBlock(world, baseX, baseY - 1, baseZ);
            if ((var21 == Blocks.GRASS || var21 == Blocks.DIRT) && baseY < 128 - var6 - 1) {
               NostalgiaGenHelper.setBlock(world, baseX, baseY - 1, baseZ, Blocks.DIRT);
               var23 = random.nextInt(2);
               var24 = 1;
               byte var25 = 0;

               int y;
               int var27;
               for(var27 = 0; var27 <= var8; ++var27) {
                  y = baseY + var6 - var27;

                  for(int x = baseX - var23; x <= baseX + var23; ++x) {
                     int var18 = x - baseX;

                     for(int z = baseZ - var23; z <= baseZ + var23; ++z) {
                        int var20 = z - baseZ;
                        if ((Math.abs(var18) != var23 || Math.abs(var20) != var23 || var23 <= 0) && !NostalgiaGenHelper.getBlockState(world, x, y, z).isOpaqueCube()) {
                           NostalgiaGenHelper.setBlock(world, x, y, z, Blocks.LEAVES, EnumType.SPRUCE);
                        }
                     }
                  }

                  if (var23 >= var24) {
                     var23 = var25;
                     var25 = 1;
                     ++var24;
                     if (var24 > var9) {
                        var24 = var9;
                     }
                  } else {
                     ++var23;
                  }
               }

               var27 = random.nextInt(3);

               for(y = 0; y < var6 - var27; ++y) {
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
