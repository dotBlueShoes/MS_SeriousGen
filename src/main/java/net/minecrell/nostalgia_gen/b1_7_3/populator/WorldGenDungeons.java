package net.minecrell.nostalgia_gen.b1_7_3.populator;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecrell.nostalgia_gen.NostalgiaGenHelper;

public class WorldGenDungeons extends WorldGenerator {
   private static final ResourceLocation[] SPAWNERTYPES = new ResourceLocation[]{EntityList.getKey(EntitySkeleton.class), EntityList.getKey(EntityZombie.class), EntityList.getKey(EntityZombie.class), EntityList.getKey(EntitySpider.class)};

   public boolean generate(World world, Random random, int baseX, int baseY, int baseZ) {
      byte var6 = 3;
      int var7 = random.nextInt(2) + 2;
      int var8 = random.nextInt(2) + 2;
      int var9 = 0;

      int x;
      int y;
      //int x;
      for(x = baseX - var7 - 1; x <= baseX + var7 + 1; ++x) {
         for(y = baseY - 1; y <= baseY + var6 + 1; ++y) {
            for(x = baseZ - var8 - 1; x <= baseZ + var8 + 1; ++x) {
               Material var13 = NostalgiaGenHelper.getBlockMaterial(world, x, y, x);
               if (y == baseY - 1 && !var13.isSolid()) {
                  return false;
               }

               if (y == baseY + var6 + 1 && !var13.isSolid()) {
                  return false;
               }

               if ((x == baseX - var7 - 1 || x == baseX + var7 + 1 || x == baseZ - var8 - 1 || x == baseZ + var8 + 1) && y == baseY && NostalgiaGenHelper.isAirBlock(world, x, y, x) && NostalgiaGenHelper.isAirBlock(world, x, y + 1, x)) {
                  ++var9;
               }
            }
         }
      }

      if (var9 >= 1 && var9 <= 5) {
         for(x = baseX - var7 - 1; x <= baseX + var7 + 1; ++x) {
            for(y = baseY + var6; y >= baseY - 1; --y) {
               for(x = baseZ - var8 - 1; x <= baseZ + var8 + 1; ++x) {
                  if (x != baseX - var7 - 1 && y != baseY - 1 && x != baseZ - var8 - 1 && x != baseX + var7 + 1 && y != baseY + var6 + 1 && x != baseZ + var8 + 1) {
                     NostalgiaGenHelper.setBlockWithNotify(world, x, y, x, Blocks.AIR);
                  } else if (y >= 0 && !NostalgiaGenHelper.getBlockMaterial(world, x, y - 1, x).isSolid()) {
                     NostalgiaGenHelper.setBlockWithNotify(world, x, y, x, Blocks.AIR);
                  } else if (NostalgiaGenHelper.getBlockMaterial(world, x, y, x).isSolid()) {
                     if (y == baseY - 1 && random.nextInt(4) != 0) {
                        NostalgiaGenHelper.setBlockWithNotify(world, x, y, x, Blocks.MOSSY_COBBLESTONE);
                     } else {
                        NostalgiaGenHelper.setBlockWithNotify(world, x, y, x, Blocks.COBBLESTONE);
                     }
                  }
               }
            }
         }

         label108:
         for(x = 0; x < 2; ++x) {
            for(y = 0; y < 3; ++y) {
               x = baseX + random.nextInt(var7 * 2 + 1) - var7;
               int z = baseZ + random.nextInt(var8 * 2 + 1) - var8;
               if (NostalgiaGenHelper.isAirBlock(world, x, baseY, z)) {
                  int var15 = 0;
                  if (NostalgiaGenHelper.getBlockMaterial(world, x - 1, baseY, z).isSolid()) {
                     ++var15;
                  }

                  if (NostalgiaGenHelper.getBlockMaterial(world, x + 1, baseY, z).isSolid()) {
                     ++var15;
                  }

                  if (NostalgiaGenHelper.getBlockMaterial(world, x, baseY, z - 1).isSolid()) {
                     ++var15;
                  }

                  if (NostalgiaGenHelper.getBlockMaterial(world, x, baseY, z + 1).isSolid()) {
                     ++var15;
                  }

                  if (var15 == 1) {
                     NostalgiaGenHelper.setBlockWithNotify(world, x, baseY, z, Blocks.CHEST);
                     TileEntityChest chest = (TileEntityChest)NostalgiaGenHelper.getBlockTileEntity(world, x, baseY, z);
                     int count = 0;

                     while(true) {
                        if (count >= 8) {
                           continue label108;
                        }

                        ItemStack item = this.pickCheckLootItem(random);
                        if (item != null) {
                           chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), item);
                        }

                        ++count;
                     }
                  }
               }
            }
         }

         NostalgiaGenHelper.setBlockWithNotify(world, baseX, baseY, baseZ, Blocks.MOB_SPAWNER);
         TileEntityMobSpawner mobSpawner = (TileEntityMobSpawner)NostalgiaGenHelper.getBlockTileEntity(world, baseX, baseY, baseZ);
         mobSpawner.getSpawnerBaseLogic().setEntityId(pickMobSpawner(random));
         return true;
      } else {
         return false;
      }
   }

   private ItemStack pickCheckLootItem(Random var1) {
      int var2 = var1.nextInt(11);
      return var2 == 0 ? new ItemStack(Items.SADDLE) : (var2 == 1 ? new ItemStack(Items.IRON_INGOT, var1.nextInt(4) + 1) : (var2 == 2 ? new ItemStack(Items.BREAD) : (var2 == 3 ? new ItemStack(Items.WHEAT, var1.nextInt(4) + 1) : (var2 == 4 ? new ItemStack(Items.GUNPOWDER, var1.nextInt(4) + 1) : (var2 == 5 ? new ItemStack(Items.STRING, var1.nextInt(4) + 1) : (var2 == 6 ? new ItemStack(Items.BUCKET) : (var2 == 7 && var1.nextInt(100) == 0 ? new ItemStack(Items.GOLDEN_APPLE) : (var2 == 8 && var1.nextInt(2) == 0 ? new ItemStack(Items.REDSTONE, var1.nextInt(4) + 1) : (var2 == 9 && var1.nextInt(10) == 0 ? new ItemStack(Item.getItemById(Item.REGISTRY.getIDForObject(Items.RECORD_13) + var1.nextInt(2))) : (var2 == 10 ? new ItemStack(Items.DYE, 1, 3) : null))))))))));
   }

   private static ResourceLocation pickMobSpawner(Random random) {
      int i = random.nextInt(4);
      return SPAWNERTYPES[i];
   }
}
