package shadowmage.ancient_structures.api;

import net.minecraft.block.Block;

public interface IStructureBuilder
{

public void placeBlock(int x, int y, int z, Block block, int meta, int priority);
}
