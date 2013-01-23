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
package shadowmage.ancient_warfare.common.aw_structure.data;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.aw_structure.data.AWStructure;

public class StructureClientInfo
{
public final String name;
public final int xSize;
public final int ySize;
public final int zSize;
public final int xOffset;
public final int yOffset;
public final int zOffset;
public boolean creative = true;
public boolean worldGen = false;
public boolean survival = false;

public StructureClientInfo(NBTTagCompound tag)
  {
  this.name = tag.getString("name");
  this.xSize = tag.getShort("x");
  this.ySize = tag.getShort("y");
  this.zSize = tag.getShort("z");
  this.xOffset = tag.getShort("xO");
  this.yOffset = tag.getShort("yO");
  this.zOffset = tag.getShort("zO");
  this.creative = tag.getBoolean("creat");
  this.worldGen = tag.getBoolean("world");
  this.survival = tag.getBoolean("surv");
  }

public StructureClientInfo(String name, int x, int y, int z, int xOff, int yOff, int zOff)
  {
  this.name = name;
  this.xSize = x;
  this.ySize = y;
  this.zSize = z;
  this.xOffset = xOff;
  this.yOffset = yOff;
  this.zOffset = zOff;
  }

public static NBTTagCompound getClientTag(AWStructure struct)
 {
 NBTTagCompound structTag = new NBTTagCompound();
 if(struct!=null)
   {
   structTag.setString("name", String.valueOf(struct.name));
   structTag.setShort("x", (short)struct.xSize);
   structTag.setShort("y", (short)struct.ySize);
   structTag.setShort("z", (short)struct.zSize);
   structTag.setShort("xO", (short)struct.xOffset);
   structTag.setShort("yO", (short)struct.verticalOffset);
   structTag.setShort("zO", (short)struct.zOffset);
   structTag.setBoolean("creat", struct.creative);
   structTag.setBoolean("world", struct.worldGen);
   structTag.setBoolean("surv", struct.survival);
   }
 return structTag;
 }

}
