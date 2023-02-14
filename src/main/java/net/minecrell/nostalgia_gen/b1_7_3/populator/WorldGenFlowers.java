package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.BlockBush;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenFlowers extends WorldGenerator {
   private BlockBush plantBlock;

   public WorldGenFlowers(BlockBush plantBlock) {
      this.plantBlock = plantBlock;
   }

   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      for(int i = 0; i < 64; ++i) {
         int x = baseX + random.nextInt(8) - random.nextInt(8);
         int y = baseY + random.nextInt(4) - random.nextInt(4);
         int z = baseZ + random.nextInt(8) - random.nextInt(8);
         if (NostalgiaGenHelper.isAirBlock(world, x, y, z) && NostalgiaGenHelper.canBlockStay(world, x, y, z, this.plantBlock)) {
            NostalgiaGenHelper.setBlock(world, x, y, z, this.plantBlock);
         }
      }

      return true;
   }
}
