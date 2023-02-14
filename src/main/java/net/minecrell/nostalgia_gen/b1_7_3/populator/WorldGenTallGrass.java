package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.BlockTallGrass.EnumType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenTallGrass extends WorldGenerator {
   private IBlockState grassBlock;

   public WorldGenTallGrass(EnumType type) {
      this.grassBlock = Blocks.TALLGRASS.getDefaultState().withProperty(BlockTallGrass.TYPE, type);
   }

   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      Block block;
      while(((block = NostalgiaGenHelper.getBlock(world, baseX, baseY, baseZ)) == Blocks.AIR || block == Blocks.LEAVES) && baseY > 0) {
         --baseY;
      }

      for(int i = 0; i < 128; ++i) {
         int x = baseX + random.nextInt(8) - random.nextInt(8);
         int y = baseY + random.nextInt(4) - random.nextInt(4);
         int z = baseZ + random.nextInt(8) - random.nextInt(8);
         if (NostalgiaGenHelper.isAirBlock(world, x, y, z) && NostalgiaGenHelper.canBlockStay(world, x, y, z, Blocks.TALLGRASS, this.grassBlock)) {
            NostalgiaGenHelper.setBlockState(world, x, y, z, this.grassBlock);
         }
      }

      return true;
   }
}
