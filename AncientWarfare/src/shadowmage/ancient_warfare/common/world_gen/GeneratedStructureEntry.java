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
package shadowmage.ancient_warfare.common.world_gen;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;

public class GeneratedStructureEntry implements INBTTaggable
{

byte structureValue;
String name = "";
byte xOff;
byte zOff;
byte face;
byte yPos;

public GeneratedStructureEntry(String name, byte xOff, byte yPos, byte zOff, byte face, byte value)
  {
  this.structureValue = value;
  this.name = name;
  this.xOff = xOff;
  this.zOff = zOff;
  this.face = face;
  this.yPos = yPos;
  }

public GeneratedStructureEntry(NBTTagCompound tag)
  {
  this.readFromNBT(tag);
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setByte("val", this.structureValue);
  tag.setString("name", this.name);
  tag.setByte("xO", this.xOff);
  tag.setByte("yO", this.yPos);
  tag.setByte("zO", this.zOff);
  tag.setByte("f", this.face);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  this.structureValue = tag.getByte("val");
  this.name = tag.getString("name");
  this.xOff = tag.getByte("xO");
  this.yPos = tag.getByte("yO");
  this.zOff = tag.getByte("zO");
  this.face = tag.getByte("f");
  }
}
