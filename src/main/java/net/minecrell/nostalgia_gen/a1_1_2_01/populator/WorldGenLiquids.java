package net.minecrell.nostalgia_gen.a1_1_2_01.populator;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenLiquids extends WorldGenerator {
   private final IBlockState liquidState;

   public WorldGenLiquids(Block liquidBlock) {
      this.liquidState = liquidBlock.getDefaultState();
   }

   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      if (NostalgiaGenHelper.getBlock(world, baseX, baseY + 1, baseZ) != Blocks.STONE) {
         return false;
      } else if (NostalgiaGenHelper.getBlock(world, baseX, baseY - 1, baseZ) != Blocks.STONE) {
         return false;
      } else if (NostalgiaGenHelper.getBlock(world, baseX, baseY, baseZ) != Blocks.AIR && NostalgiaGenHelper.getBlock(world, baseX, baseY, baseZ) != Blocks.STONE) {
         return false;
      } else {
         int var6 = 0;
         if (NostalgiaGenHelper.getBlock(world, baseX - 1, baseY, baseZ) == Blocks.STONE) {
            ++var6;
         }

         if (NostalgiaGenHelper.getBlock(world, baseX + 1, baseY, baseZ) == Blocks.STONE) {
            ++var6;
         }

         if (NostalgiaGenHelper.getBlock(world, baseX, baseY, baseZ - 1) == Blocks.STONE) {
            ++var6;
         }

         if (NostalgiaGenHelper.getBlock(world, baseX, baseY, baseZ + 1) == Blocks.STONE) {
            ++var6;
         }

         int var7 = 0;
         if (NostalgiaGenHelper.getBlock(world, baseX - 1, baseY, baseZ) == Blocks.AIR) {
            ++var7;
         }

         if (NostalgiaGenHelper.getBlock(world, baseX + 1, baseY, baseZ) == Blocks.AIR) {
            ++var7;
         }

         if (NostalgiaGenHelper.getBlock(world, baseX, baseY, baseZ - 1) == Blocks.AIR) {
            ++var7;
         }

         if (NostalgiaGenHelper.getBlock(world, baseX, baseY, baseZ + 1) == Blocks.AIR) {
            ++var7;
         }

         if (var6 == 3 && var7 == 1) {
            BlockPos pos = new BlockPos(baseX, baseY, baseZ);
            NostalgiaGenHelper.setBlockStateWithNotify(world, pos, this.liquidState);
            world.immediateBlockTick(pos, this.liquidState, random);
         }

         return true;
      }
   }
}
