/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
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
package shadowmage.ancient_warfare.common.structures.data.rules;

import net.minecraft.util.MathHelper;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class ScannedGateEntry
{

EntityGate g;
byte facing;
BlockPosition pos1;
BlockPosition pos2;

public ScannedGateEntry(EntityGate g, BlockPosition pos, int face)
  {
  this.g = g;
  this.pos1 = g.pos1.copy();
  this.pos2 = g.pos2.copy();
  pos1.x -= pos.x;
  pos1.z -= pos.z;
  pos1.y -= pos.y;
  pos2.x -= pos.x;
  pos2.y -= pos.y;
  pos2.z -= pos.z;
  facing = (byte) ((g.gateOrientation + BlockTools.getRotationAmount(face, 2)) %4);
  }

public void normalizeForNorthFacing(int currentFacing, int xSize, int zSize)
  {   
  Config.logDebug("normalizing gate scan entry: "+this.pos1 + " :: "+this.pos2);
  /**
   * corners of block bounds relative to TL corner of scanned stucture
   */  
  BlockPosition c1 = BlockTools.getNorthRotatedPosition(this.pos1.x, this.pos1.y, this.pos1.z, currentFacing, xSize, zSize);
  
  BlockPosition c2 = BlockTools.getNorthRotatedPosition(this.pos2.x, this.pos2.y, this.pos2.z, currentFacing, xSize, zSize);
  
  this.pos1 = c1;
  this.pos2 = c2;
  Config.logDebug("normalized gate scan entry: "+this.pos1 + " :: "+this.pos2);  
  }
}
