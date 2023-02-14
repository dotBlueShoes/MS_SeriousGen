package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenLakes extends WorldGenerator {
   private Block lakeBlock;

   public WorldGenLakes(Block lakeBlock) {
      this.lakeBlock = lakeBlock;
   }

   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      baseX -= 8;

      for(baseZ -= 8; baseY > 0 && NostalgiaGenHelper.isAirBlock(world, baseX, baseY, baseZ); --baseY) {
      }

      baseY -= 4;
      boolean[] var6 = new boolean[2048];
      int var7 = random.nextInt(4) + 4;

      int var38;
      for(var38 = 0; var38 < var7; ++var38) {
         double var9 = random.nextDouble() * 6.0 + 3.0;
         double var11 = random.nextDouble() * 4.0 + 2.0;
         double var13 = random.nextDouble() * 6.0 + 3.0;
         double var15 = random.nextDouble() * (16.0 - var9 - 2.0) + 1.0 + var9 / 2.0;
         double var17 = random.nextDouble() * (8.0 - var11 - 4.0) + 2.0 + var11 / 2.0;
         double var19 = random.nextDouble() * (16.0 - var13 - 2.0) + 1.0 + var13 / 2.0;

         for(int var21 = 1; var21 < 15; ++var21) {
            for(int var22 = 1; var22 < 15; ++var22) {
               for(int var23 = 1; var23 < 7; ++var23) {
                  double var24 = ((double)var21 - var15) / (var9 / 2.0);
                  double var26 = ((double)var23 - var17) / (var11 / 2.0);
                  double var28 = ((double)var22 - var19) / (var13 / 2.0);
                  double var30 = var24 * var24 + var26 * var26 + var28 * var28;
                  if (var30 < 1.0) {
                     var6[(var21 * 16 + var22) * 8 + var23] = true;
                  }
               }
            }
         }
      }

      int var45;
      int var42;
      boolean var47;
      for(var38 = 0; var38 < 16; ++var38) {
         for(var42 = 0; var42 < 16; ++var42) {
            for(var45 = 0; var45 < 8; ++var45) {
               var47 = !var6[(var38 * 16 + var42) * 8 + var45] && (var38 < 15 && var6[((var38 + 1) * 16 + var42) * 8 + var45] || var38 > 0 && var6[((var38 - 1) * 16 + var42) * 8 + var45] || var42 < 15 && var6[(var38 * 16 + var42 + 1) * 8 + var45] || var42 > 0 && var6[(var38 * 16 + (var42 - 1)) * 8 + var45] || var45 < 7 && var6[(var38 * 16 + var42) * 8 + var45 + 1] || var45 > 0 && var6[(var38 * 16 + var42) * 8 + (var45 - 1)]);
               if (var47) {
                  Material material = NostalgiaGenHelper.getBlockMaterial(world, baseX + var38, baseY + var45, baseZ + var42);
                  if (var45 >= 4 && material.isLiquid()) {
                     return false;
                  }

                  if (var45 < 4 && !material.isSolid() && NostalgiaGenHelper.getBlock(world, baseX + var38, baseY + var45, baseZ + var42) != this.lakeBlock) {
                     return false;
                  }
               }
            }
         }
      }

      for(var38 = 0; var38 < 16; ++var38) {
         for(var42 = 0; var42 < 16; ++var42) {
            for(var45 = 0; var45 < 8; ++var45) {
               if (var6[(var38 * 16 + var42) * 8 + var45]) {
                  NostalgiaGenHelper.setBlock(world, baseX + var38, baseY + var45, baseZ + var42, var45 >= 4 ? Blocks.AIR : this.lakeBlock);
               }
            }
         }
      }

      for(var38 = 0; var38 < 16; ++var38) {
         for(var42 = 0; var42 < 16; ++var42) {
            for(var45 = 4; var45 < 8; ++var45) {
               if (var6[(var38 * 16 + var42) * 8 + var45] && NostalgiaGenHelper.getBlock(world, baseX + var38, baseY + var45 - 1, baseZ + var42) == Blocks.DIRT && NostalgiaGenHelper.getSavedLightValue(world, EnumSkyBlock.SKY, baseX + var38, baseY + var45, baseZ + var42) > 0) {
                  NostalgiaGenHelper.setBlock(world, baseX + var38, baseY + var45 - 1, baseZ + var42, Blocks.GRASS);
               }
            }
         }
      }

      if (this.lakeBlock.getMaterial((IBlockState)null) == Material.LAVA) {
         for(var38 = 0; var38 < 16; ++var38) {
            for(var42 = 0; var42 < 16; ++var42) {
               for(var45 = 0; var45 < 8; ++var45) {
                  var47 = !var6[(var38 * 16 + var42) * 8 + var45] && (var38 < 15 && var6[((var38 + 1) * 16 + var42) * 8 + var45] || var38 > 0 && var6[((var38 - 1) * 16 + var42) * 8 + var45] || var42 < 15 && var6[(var38 * 16 + var42 + 1) * 8 + var45] || var42 > 0 && var6[(var38 * 16 + (var42 - 1)) * 8 + var45] || var45 < 7 && var6[(var38 * 16 + var42) * 8 + var45 + 1] || var45 > 0 && var6[(var38 * 16 + var42) * 8 + (var45 - 1)]);
                  if (var47 && (var45 < 4 || random.nextInt(2) != 0) && NostalgiaGenHelper.getBlockMaterial(world, baseX + var38, baseY + var45, baseZ + var42).isSolid()) {
                     NostalgiaGenHelper.setBlock(world, baseX + var38, baseY + var45, baseZ + var42, Blocks.STONE);
                  }
               }
            }
         }
      }

      return true;
   }
}
