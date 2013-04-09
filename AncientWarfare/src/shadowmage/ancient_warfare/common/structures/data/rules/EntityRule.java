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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.ScannedEntityEntry;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.ByteTools;
import shadowmage.ancient_warfare.common.utils.NBTReader;
import shadowmage.ancient_warfare.common.utils.NBTWriter;
import shadowmage.ancient_warfare.common.utils.StringTools;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class EntityRule
{

String entityClassName = "";

/**
 * block position in template
 */
int bx;
int by;
int bz;

/**
 * offset from block corner...
 */
float oX;
float oY;
float oZ;

float rot;
float pitch;
//NBTTagCompound entityNBT;

public Entity getEntityToSpawn(World world, int facing, ProcessedStructure struct, BlockPosition buildPos)
  {
  Entity ent = EntityList.createEntityByName(entityClassName, world);
  if(ent!=null)
    {
//    if(entityNBT!=null)
//      {
//      entityNBT.removeTag("PersistentIDMSB");//remove UUID from tag, we need a fresh one...
//      entityNBT.removeTag("PersistentIDLSB");
//      ent.readFromNBT(entityNBT);//load the entity from the tag
//      }
//    ent.generatePersistentID();//tell entity to generate fresh UUID
    int rotAmt = getRotationAmt(facing);
    BlockPosition target = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(bx-struct.xOffset,by-struct.verticalOffset, bz-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
    float ax = target.x+getRotatedXOffset(oX, oZ, facing);
    float ay = target.y+oY;
    float az = target.z+getRotatedZOffset(oX, oZ, facing);    
    float ar = rot + 90*rotAmt;    
    Config.logDebug("initial rot: "+rot+" new rot: "+ar);    
    ent.setLocationAndAngles(ax, ay, az, ar, pitch);
    ent.prevRotationYaw = ent.rotationYaw = ar;
    }
  return ent;
  }

protected int getRotationAmt(int facing)
  {
  if(facing==2)
    {
    return 0;
    }
  else if(facing==3)
    {
    return 1;
    }
  else if(facing==0)
    {
    return 2;
    }
  else if(facing==1)
    {
    return 3;
    }
  return 0;
  }

protected float getRotatedXOffset(float xOff, float zOff, int face)
  {
  switch(face)
  {
  case 0:
  return 1-xOff;
  case 1:
  return zOff;
  case 2:
  return xOff;
  case 3:
  return 1-zOff;
  }  
  return xOff;
  }

protected float getRotatedZOffset(float xOff, float zOff, int face)
  {
  switch(face)
  {
  case 0:
  return 1-zOff;
  case 1:
  return xOff;
  case 2:
  return zOff;
  case 3:
  return 1-xOff;
  }  
  return zOff;
  }

public static EntityRule populateRule(ScannedEntityEntry entry)
  {
  EntityRule rule = new EntityRule();
  rule.bx = entry.bx;
  rule.by = entry.by;
  rule.bz = entry.bz;
  rule.oX = entry.xO;
  rule.oY = entry.yO;
  rule.oZ = entry.zO;
  rule.entityClassName = EntityList.getEntityString(entry.ent);
//  rule.entityNBT = new NBTTagCompound();
//  entry.ent.writeToNBT(rule.entityNBT);
  rule.pitch = entry.p;
  rule.rot = entry.r;
  return rule;
  }

public static EntityRule parseRule(List<String> ruleLines)
  {
  String line;
  EntityRule rule = new EntityRule();
  Iterator<String> it = ruleLines.iterator();
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("entityname"))
      {
      rule.entityClassName = StringTools.safeParseString("=", line);
      }
    else if(line.toLowerCase().startsWith("bx"))
      {
      rule.bx = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("by"))
      {
      rule.by = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("bz"))
      {
      rule.bz = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("ox"))
      {
      rule.oX = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("oy"))
      {
      rule.oY = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("oz"))
      {
      rule.oZ = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("rot"))
      {
      rule.rot = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("pitch"))
      {
      rule.pitch = StringTools.safeParseFloat("=", line);
      }
//    else if(line.toLowerCase().startsWith("datas"))
//      {
//      List<String>tagLines = new ArrayList<String>();
//      String tagLine;
//      while(it.hasNext())
//        {
//        tagLine = it.next();
//        if(tagLine.equals(":enddatas"))
//          {
//          break;
//          }
//        tagLines.add(tagLine);
//        }      
//      rule.entityNBT = NBTReader.readTagFromLines(tagLines);
//      List<String> testLines = NBTWriter.writeNBTToStrings(rule.entityNBT);     
//      } 
    }
  if(rule.entityClassName.equals(""))
    {
    return null;
    }
  return rule;
  }

public List<String> getRuleLines()
  {
  ArrayList<String> lines = new ArrayList<String>();
  lines.add("entity:");
  lines.add("entityname="+this.entityClassName);
  lines.add("bx="+bx);
  lines.add("by="+by);
  lines.add("bz="+bz);
  lines.add("ox="+oX);
  lines.add("oy="+oY);
  lines.add("oz="+oZ);
  lines.add("rot="+rot);
  lines.add("pitch="+pitch);
//  if(entityNBT!=null)
//    {
//    lines.add("datas:");
//    lines.addAll(getLinesForNBT(entityNBT));
//    lines.add(":enddatas");
//    }
  lines.add(":endentity");
  return lines;
  }

private List<String> getLinesForNBT(NBTTagCompound tag)
  {
  return NBTWriter.writeNBTToStrings(tag);
  }


}
