package net.minecrell.nostalgia_gen.a1_1_2_01.populator;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenReed extends WorldGenerator {
   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      for(int i = 0; i < 20; ++i) {
         int x = baseX + random.nextInt(4) - random.nextInt(4);
         int z = baseZ + random.nextInt(4) - random.nextInt(4);
         if (NostalgiaGenHelper.getBlock(world, x, baseY, z) == Blocks.AIR && (NostalgiaGenHelper.getBlockMaterial(world, x - 1, baseY - 1, z) == Material.WATER || NostalgiaGenHelper.getBlockMaterial(world, x + 1, baseY - 1, z) == Material.WATER || NostalgiaGenHelper.getBlockMaterial(world, x, baseY - 1, z - 1) == Material.WATER || NostalgiaGenHelper.getBlockMaterial(world, x, baseY - 1, z + 1) == Material.WATER)) {
            int var10 = 2 + random.nextInt(random.nextInt(3) + 1);

            for(int var11 = 0; var11 < var10; ++var11) {
               if (Blocks.REEDS.canBlockStay(world, new BlockPos(x, baseY + var11, z))) {
                  NostalgiaGenHelper.setBlock(world, x, baseY + var11, z, Blocks.REEDS);
               }
            }
         }
      }

      return true;
   }
}
