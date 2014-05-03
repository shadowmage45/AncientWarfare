/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
   Please see COPYING for precise license information.

   This file is part of Ancient Warfare.

   Ancient Warfare is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Ancient Warfare is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Ancient Warfare.  If not, see <http://www.gnu.org/licenses/>.
 */
package shadowmage.ancient_structures.common.template.build.validation;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID;
import net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockMeta;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBuilder;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class StructureBuilderWorldGen extends StructureBuilder
{

public StructureBuilderWorldGen(World world, StructureTemplate template, int face, int x, int y, int z)
  {
  super(world, template, face, x, y, z);
  }

@Override
protected void placeAir()
  {
  if(!template.getValidationSettings().isPreserveBlocks())
    {
    template.getValidationSettings().handleClearAction(world, destination.x, destination.y, destination.z, template, bb);    
    }
  }

@Override
public void placeBlock(int x, int y, int z, Block block, int meta, int priority)
  {
  if(template.getValidationSettings().isBlockSwap())
    {
    BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
    BiomeEvent.GetVillageBlockID evt1 = new GetVillageBlockID(biome, block.blockID, meta);
    MinecraftForge.EVENT_BUS.post(evt1);
    if(evt1.getResult() == Result.DENY && evt1.replacement!=block.blockID)
      {
      block = Block.blocksList[evt1.replacement];
      }   
    else
      {
      block = getBiomeSpecificBlock(block, meta, biome);
      }
    BiomeEvent.GetVillageBlockMeta evt2 = new GetVillageBlockMeta(biome, block.blockID, meta);
    MinecraftForge.EVENT_BUS.post(evt2);
    if(evt2.getResult()==Result.DENY)
      {
      meta = evt2.replacement;
      }  
    else
      {
      meta = getBiomeSpecificBlockMetadata(block.blockID, meta, biome);
      }
    }
  super.placeBlock(x, y, z, block, meta, priority);
  }

protected Block getBiomeSpecificBlock(Block par1, int par2, BiomeGenBase biome)
  {
  if(biome == BiomeGenBase.desert || biome == BiomeGenBase.desertHills || biome.topBlock==Block.sand.blockID)
    {
    if (par1 == Block.wood)
      {
      return Block.sandStone;
      }

    if (par1 == Block.cobblestone)
      {
      return Block.sandStone;
      }

    if (par1 == Block.planks)
      {
      return Block.sandStone;
      }

    if (par1 == Block.stairsWoodOak)
      {
      return Block.stairsSandStone;
      }

    if (par1 == Block.stairsCobblestone)
      {
      return Block.stairsSandStone;
      }

    if (par1 == Block.gravel)
      {
      return Block.sandStone;
      }
    
    if(par1 == Block.woodSingleSlab && par2==0)
      {
      return Block.stoneSingleSlab;
      }
    }

  return par1;
  }

/**
 * Gets the replacement block metadata for the current biome
 */
protected int getBiomeSpecificBlockMetadata(int par1, int par2, BiomeGenBase biome)
  {
  if(biome == BiomeGenBase.desert || biome == BiomeGenBase.desertHills || biome.topBlock==Block.sand.blockID)
    {
    if (par1 == Block.wood.blockID)
      {
      return 0;
      }
    if (par1 == Block.cobblestone.blockID)
      {
      return 0;
      }
    if (par1 == Block.planks.blockID)
      {
      return 2;
      }
    if(par1 == Block.stoneSingleSlab.blockID && par2==0)
      {
      return 1;
      }
    }
  return par2;
  }

public void instantConstruction()
  {
  template.getValidationSettings().preGeneration(world, buildOrigin.x, buildOrigin.y, buildOrigin.z, buildFace, template, bb);
  super.instantConstruction();
  if(template.getValidationSettings().validationType==StructureValidationType.GROUND)
    {
    BiomeGenBase biome = world.getBiomeGenForCoords(buildOrigin.x, buildOrigin.z);
    if(biome!=null && biome.getEnableSnow())
      {
      sprinkleSnow();
      }
    }
  }

private void sprinkleSnow()
  {
  Block block;
  int y = 0;
  int border = template.getValidationSettings().borderSize;
  BlockPosition p1 = bb.min.copy();
  BlockPosition p2 = bb.max.copy();
  p1.offset(-border, 0, -border);
  p2.offset(border, 0, border);
  for(int x = p1.x; x <=p2.x; x++)
    {
    for(int z = p1.z; z<=p2.z; z++)
      {
      y = p2.y;
      while(y>=p1.y)
        {
        block = Block.blocksList[world.getBlockId(x, y, z)];
        if(block!=null && block.isBlockSolidOnSide(world, x, y, z, ForgeDirection.UP))
          {
          y++;
          if(world.getBlockId(x, y, z)==0 && world.canBlockSeeTheSky(x, y, z))
            {
            world.setBlock(x, y, z, Block.snow.blockID);            
            }
          break;
          }
        y--;
        }
      }
    }
  }

}
