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
package shadowmage.ancient_warfare.common.structures.data;

import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;

public class StructureBuildSettings implements INBTTaggable
{

public String name = "No Selection!";
public boolean spawnVehicle = true;
public boolean spawnNpc = true;
public boolean spawnGate = true;
public int teamOverride = -1;//no override

private StructureBuildSettings(){}

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();  
  tag.setString("name", this.name);
  tag.setBoolean("sveh", spawnVehicle);
  tag.setBoolean("snpc", spawnNpc);
  tag.setBoolean("sgate", spawnGate);
  tag.setInteger("oteam", teamOverride);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  if(tag.hasKey("name"))
    {
    this.name = tag.getString("name");
    }
  if(tag.hasKey("sveh"))this.spawnVehicle = tag.getBoolean("sveh");
  if(tag.hasKey("snpc"))this.spawnNpc = tag.getBoolean("snpc");
  if(tag.hasKey("sgate"))this.spawnGate = tag.getBoolean("sgate");
  if(tag.hasKey("oteam"))this.teamOverride = tag.getInteger("oteam");
  }

public static StructureBuildSettings constructFromNBT(NBTTagCompound tag)
  {
  StructureBuildSettings settings = new StructureBuildSettings();
  settings.readFromNBT(tag);
  return settings;
  }

}
