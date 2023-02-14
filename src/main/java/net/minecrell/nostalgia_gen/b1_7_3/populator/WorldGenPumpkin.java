package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenPumpkin extends WorldGenerator {
   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      for(int i = 0; i < 64; ++i) {
         int x = baseX + random.nextInt(8) - random.nextInt(8);
         int y = baseY + random.nextInt(4) - random.nextInt(4);
         int z = baseZ + random.nextInt(8) - random.nextInt(8);
         if (NostalgiaGenHelper.isAirBlock(world, x, y, z) && NostalgiaGenHelper.getBlock(world, x, y - 1, z) == Blocks.GRASS && Blocks.PUMPKIN.canPlaceBlockAt(world, new BlockPos(x, y, z))) {
            NostalgiaGenHelper.setBlockState(world, x, y, z, Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, Plane.HORIZONTAL.random(random)));
         }
      }

      return true;
   }
}
