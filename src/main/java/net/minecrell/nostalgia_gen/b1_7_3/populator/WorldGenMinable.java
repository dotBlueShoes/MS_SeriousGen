package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;
import net.minecrell.nostalgia_gen.b1_7_3.MathHelper;

public class WorldGenMinable extends WorldGenerator {
   private Block minableBlock;
   private int numberOfBlocks;

   public WorldGenMinable(Block minableBlock, int numberOfBlocks) {
      this.minableBlock = minableBlock;
      this.numberOfBlocks = numberOfBlocks;
   }

   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      float var6 = random.nextFloat() * 3.1415927F;
      double var7 = (double)((float)(baseX + 8) + MathHelper.sin(var6) * (float)this.numberOfBlocks / 8.0F);
      double var9 = (double)((float)(baseX + 8) - MathHelper.sin(var6) * (float)this.numberOfBlocks / 8.0F);
      double var11 = (double)((float)(baseZ + 8) + MathHelper.cos(var6) * (float)this.numberOfBlocks / 8.0F);
      double var13 = (double)((float)(baseZ + 8) - MathHelper.cos(var6) * (float)this.numberOfBlocks / 8.0F);
      double var15 = (double)(baseY + random.nextInt(3) + 2);
      double var17 = (double)(baseY + random.nextInt(3) + 2);

      for(int var19 = 0; var19 <= this.numberOfBlocks; ++var19) {
         double var20 = var7 + (var9 - var7) * (double)var19 / (double)this.numberOfBlocks;
         double var22 = var15 + (var17 - var15) * (double)var19 / (double)this.numberOfBlocks;
         double var24 = var11 + (var13 - var11) * (double)var19 / (double)this.numberOfBlocks;
         double var26 = random.nextDouble() * (double)this.numberOfBlocks / 16.0;
         double var28 = (double)(MathHelper.sin((float)var19 * 3.1415927F / (float)this.numberOfBlocks) + 1.0F) * var26 + 1.0;
         double var30 = (double)(MathHelper.sin((float)var19 * 3.1415927F / (float)this.numberOfBlocks) + 1.0F) * var26 + 1.0;
         int var32 = MathHelper.floor_double(var20 - var28 / 2.0);
         int var33 = MathHelper.floor_double(var22 - var30 / 2.0);
         int var34 = MathHelper.floor_double(var24 - var28 / 2.0);
         int var35 = MathHelper.floor_double(var20 + var28 / 2.0);
         int var36 = MathHelper.floor_double(var22 + var30 / 2.0);
         int var37 = MathHelper.floor_double(var24 + var28 / 2.0);

         for(int x = var32; x <= var35; ++x) {
            double var39 = ((double)x + 0.5 - var20) / (var28 / 2.0);
            if (var39 * var39 < 1.0) {
               for(int y = var33; y <= var36; ++y) {
                  double var42 = ((double)y + 0.5 - var22) / (var30 / 2.0);
                  if (var39 * var39 + var42 * var42 < 1.0) {
                     for(int z = var34; z <= var37; ++z) {
                        double var45 = ((double)z + 0.5 - var24) / (var28 / 2.0);
                        if (var39 * var39 + var42 * var42 + var45 * var45 < 1.0 && NostalgiaGenHelper.getBlock(world, x, y, z) == Blocks.STONE) {
                           NostalgiaGenHelper.setBlock(world, x, y, z, this.minableBlock);
                        }
                     }
                  }
               }
            }
         }
      }

      return true;
   }
}
