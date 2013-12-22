/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_structures.common.world_gen.village;

import net.minecraft.world.gen.structure.ComponentVillageStartPiece;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import shadowmage.ancient_structures.common.structures.data.ProcessedStructure;
import shadowmage.ancient_structures.common.village_gen.VillageGenComponent;

public class AWVCLogCabin extends VillageGenComponent
{

public AWVCLogCabin()
  {
  
  }

/**
 * @param start
 * @param par2
 * @param face
 * @param struct
 * @param box
 */
public AWVCLogCabin(ComponentVillageStartPiece start, Integer par2,
    Integer face, ProcessedStructure struct, StructureBoundingBox box)
  {
  super(start, par2, face, struct, box);
  // TODO Auto-generated constructor stub
  }

}
