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
package shadowmage.ancient_warfare.common.structures.data.rules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumArt;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.ScannedEntityEntry;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.NBTReader;
import shadowmage.ancient_warfare.common.utils.NBTWriter;
import shadowmage.ancient_warfare.common.utils.StringTools;

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

/**
 * if a minecart entity, this will be set to >=0
 */
public boolean isMinecart = false;
public boolean isPainting = false;
public boolean isItemFrame = false;
public int mineCartType = -1;
public int paintingType = -1;
public int hangingDirection = -1;
public int itemFrameItemID = 0;
public int itemFrameItemDamage = 0;
NBTTagCompound itemFrameItemTag = null;

public Entity getEntityToSpawn(World world, int facing, ProcessedStructure struct, BlockPosition buildPos)
  {
  Entity ent = null;
  int rotAmt = BlockTools.getRotationAmt(facing);
  BlockPosition target = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(bx-struct.xOffset,by-struct.verticalOffset, bz-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  float ax = target.x;
  float ay = target.y;
  float az = target.z;    
  float ar = rot + 90*rotAmt;    
  if(this.isMinecart)
    {
    ent = EntityMinecart.createMinecart(world, ax, ay, az, mineCartType);
//    ent = new EntityMinecart(world, mineCartType);
    ax+= getRotatedXOffset(oX, oZ, facing);
    az+= getRotatedZOffset(oX, oZ, facing);  
    ent.setLocationAndAngles(ax, ay, az, ar, pitch);
    ent.prevPosX = ax;
    ent.prevPosY = ay;
    ent.prevPosZ = az;
    ent.prevRotationYaw = ent.rotationYaw = ar;
    }
  else if(this.isPainting)
    {
    int ox;
    int oz;
    ent = new EntityPainting(world, (int)ax+getPaintingXOffset(facing), (int)ay, (int)az+getPaintingZOffset(facing), (BlockTools.getRotationAmount(hangingDirection, facing)+hangingDirection+2)%4);
    ((EntityPainting)ent).art = EnumArt.values()[paintingType];
    }
  else if(this.isItemFrame)
    {    
    ent = new EntityItemFrame(world, (int)ax+getPaintingXOffset(facing), (int)ay, (int)az+getPaintingZOffset(facing), (BlockTools.getRotationAmount(hangingDirection, facing)+hangingDirection+2)%4);
    ItemStack stack = new ItemStack(itemFrameItemID, 1, itemFrameItemDamage);
    if(itemFrameItemTag!=null)
      {
      stack.setTagCompound(itemFrameItemTag);
      }
    ((EntityItemFrame)ent).setDisplayedItem(stack);
    }
  else
    {
    ent = EntityList.createEntityByName(entityClassName, world);
    ax+= getRotatedXOffset(oX, oZ, facing);
    az+= getRotatedZOffset(oX, oZ, facing);
    ent.setLocationAndAngles(ax, ay, az, ar, pitch);
    ent.prevPosX = ax;
    ent.prevPosY = ay;
    ent.prevPosZ = az;
    ent.prevRotationYaw = ent.rotationYaw = ar;
    }  
  return ent;
  }

private int getPaintingXOffset(int facing)
  {
  switch(facing)
  {
  case 0:
  return 0;
  case 1:
  return -1;
  case 2:
  return 0;
  case 3:
  return 1;
  default:
  return facing;
  }
  }

private int getPaintingZOffset(int facing)
  {
  switch(facing)
  {
  case 0:
  return 1;
  case 1:
  return 0;
  case 2:
  return -1;
  case 3:
  return 0;
  default:
  return facing;
  }
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
  rule.oX = entry.ox;
  rule.oY = entry.oy;
  rule.oZ = entry.oz;
  rule.entityClassName = EntityList.getEntityString(entry.ent);
  rule.pitch = entry.p;
  rule.rot = entry.r;
  Class clz = entry.ent.getClass();
  if(clz==EntityPainting.class)
    {
    rule.isPainting = true;
    rule.hangingDirection = entry.hangingDirection;
    rule.paintingType = ((EntityPainting)entry.ent).art.ordinal();
    }
  else if(clz==EntityMinecart.class)
    {
    rule.isMinecart = true;
    rule.mineCartType = ((EntityMinecart)entry.ent).getMinecartType();
    }
  else if(clz==EntityItemFrame.class)
    {
    EntityItemFrame frame = (EntityItemFrame)entry.ent;
    rule.isItemFrame = true;
    rule.hangingDirection = entry.hangingDirection;
    if(frame.getDisplayedItem()!=null)
      {
      rule.itemFrameItemID = frame.getDisplayedItem().itemID;
      rule.itemFrameItemDamage = frame.getDisplayedItem().getItemDamage();
      if(frame.getDisplayedItem().hasTagCompound())
        {
        rule.itemFrameItemTag = frame.getDisplayedItem().getTagCompound();
        }
      }
    
    }
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
    else if(line.toLowerCase().startsWith("rotation"))
      {
      rule.rot = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("pitch"))
      {
      rule.pitch = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("hangdir"))
      {
      rule.hangingDirection = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("isminecart"))
      {
      rule.isMinecart = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("ispainting"))
      {
      rule.isPainting = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("isitemframe"))
      {
      rule.isItemFrame = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("minecarttype"))
      {
      rule.mineCartType = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("paintingtype"))
      {
      rule.paintingType = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("itemframeitemid"))
      {
      rule.itemFrameItemID = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("itemframeitemdamage"))
      {
      rule.itemFrameItemDamage = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("datas:"))
      {
      ArrayList<String> tagLines = new ArrayList<String>();
      while(it.hasNext())
        {
        line = it.next();
        if(line.startsWith(":enddatas"))
          {
          break;
          }
        tagLines.add(line);
        }
      rule.itemFrameItemTag = NBTReader.readTagFromLines(tagLines);
      }
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
  lines.add("rotation="+rot);
  lines.add("pitch="+pitch);
  if(this.isItemFrame)
    {
    lines.add("isitemframe=true");
    lines.add("itemframeitemid="+itemFrameItemID);
    lines.add("itemframeitemdamage="+itemFrameItemDamage);
    lines.add("hangdir="+hangingDirection);
    if(this.itemFrameItemTag!=null)
      {
      lines.add("datas:");
      lines.addAll(NBTWriter.writeNBTToStrings(itemFrameItemTag));
      lines.add(":enddatas");
      }
    }
  else if(this.isMinecart)
    {
    lines.add("isminecart=true");
    lines.add("minecarttype="+this.mineCartType);
    if(this.itemFrameItemTag!=null)//minecart chest NBT datas...
      {
      lines.add("datas:");
      lines.addAll(NBTWriter.writeNBTToStrings(itemFrameItemTag));
      lines.add(":enddatas");
      }
    }
  else if(this.isPainting)
    {
    lines.add("ispainting=true");
    lines.add("hangdir="+hangingDirection);
    lines.add("paintingtype="+this.paintingType);
    }
  lines.add(":endentity");
  return lines;
  }


}
