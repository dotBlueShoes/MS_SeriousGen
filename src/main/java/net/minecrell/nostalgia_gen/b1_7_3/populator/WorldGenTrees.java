package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

import static net.minecrell.nostalgia_gen.NostalgiaGenMod.logger;

public class WorldGenTrees extends WorldGenerator {

	@Override
	public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {

		int pineHeight = random.nextInt(3) + 4;
		final int treeHeight = pineHeight + 1;
		boolean canGenerate = true;

		if (baseY >= 1 && baseY + treeHeight <= 128) { // Trees can only generate above y=1 and below y=128 counting tree height.
			for (int y = baseY; y <= baseY + treeHeight; ++y) {

				// Don't know what yt is...
				// This for-for check doesn't work as intended for sure.
				int yt = 1;
				if (y == baseY) {
					yt = 0;
				}

				if (y >= baseY + treeHeight - 2) {
					yt = 2;
				}

				for (int var19 = baseX - yt; var19 <= baseX + yt && canGenerate; ++var19) {
					for (int var21 = baseZ - yt; var21 <= baseZ + yt && canGenerate; ++var21) {
						//logger.info("1");
						if (y >= 0 && y < 128) {
							Block block = NostalgiaGenHelper.getBlock(world, var19, y, var21);
							if (block != Blocks.AIR && block != Blocks.LEAVES) {
								canGenerate = false;
							}
						} else {
							canGenerate = false;
						}
					}
				}
			}

			if (!canGenerate) {
				return false;
			} else {
				Block var16 = NostalgiaGenHelper.getBlock(world, baseX, baseY - 1, baseZ);
				//logger.info("2");
				if ((var16 == Blocks.GRASS || var16 == Blocks.DIRT) && baseY < 128 - pineHeight - 1) {

					// Set block underneath to dirt.
					NostalgiaGenHelper.setBlock(world, baseX, baseY - 1, baseZ, Blocks.DIRT);

					for (int y = baseY - 3 + pineHeight; y <= baseY + pineHeight; ++y) {
						int var19 = y - (baseY + pineHeight);
						int var21 = 1 - var19 / 2;

						for (int x = baseX - var21; x <= baseX + var21; ++x) {
							int var13 = x - baseX;

							for (int z = baseZ - var21; z <= baseZ + var21; ++z) {
								int var15 = z - baseZ;
								//logger.info("4");
								if ((Math.abs(var13) != var21 || Math.abs(var15) != var21 || random.nextInt(2) != 0 && var19 != 0) && !NostalgiaGenHelper.getBlockState(world, x, y, z).isOpaqueCube()) {
									NostalgiaGenHelper.setBlock(world, x, y, z, Blocks.LEAVES);
								}
							}
						}

					}

					for (int y = 0; y < pineHeight; ++y) {
						//logger.info("3");
						Block block = NostalgiaGenHelper.getBlock(world, baseX, baseY + y, baseZ);
						if (block == Blocks.AIR || block == Blocks.LEAVES) {
							NostalgiaGenHelper.setBlock(world, baseX, baseY + y, baseZ, Blocks.LOG);
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
