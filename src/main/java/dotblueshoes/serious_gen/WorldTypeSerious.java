package dotblueshoes.serious_gen;

import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldTypeSerious extends WorldType {

	public static void register() {
		new WorldTypeSerious();
	}

	private WorldTypeSerious() {
		super("serious");
	}

	protected WorldTypeSerious(String name) {
		super(name);
	}

	public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
		return new ChunkProviderGenerate(world, world.getSeed());
	}

}
