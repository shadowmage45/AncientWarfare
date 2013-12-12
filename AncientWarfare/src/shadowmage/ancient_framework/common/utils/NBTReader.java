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
package shadowmage.ancient_framework.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;

public class NBTReader
{

public static NBTTagCompound readTagFromLines(List<String> lines)
  {  
  NBTTagCompound tag = null;
  Iterator<String> it = lines.iterator();
  List<String> tagLines;
  if(it.hasNext())
    {
    tagLines = getLinesForNextTag(it);
    byte type = getTagType(tagLines);
    if(type==10)
      {
      String name = getTagName(tagLines);
      scrubShell(tagLines);
      tag = (NBTTagCompound)parseTag(name, type, tagLines);
      }      
    }
  return tag;
  }

/**
 * 0-END
 * 1-BYTE
 * 2-SHORT
 * 3-INT
 * 4-LONG
 * 5-FLOAT
 * 6-DOUBLE
 * 7-BYTE-ARRAY
 * 8-STRING
 * 9-TAG-LIST
 * 10-TAG-COMPOUND
 * 11-INT-ARRAY
 */
private static NBTBase parseTag(String name, byte type, List<String> lines)
  {
  switch(type)
  {
  case 0:
  break;//end tag
  case 1:
  return parseTagByte(name, lines);
  case 2:
  return parseTagShort(name, lines);
  case 3:
  return parseTagInt(name, lines);
  case 4://long
  return parseTagLong(name, lines);
  case 5://float
  return parseTagFloat(name, lines);
  case 6://double
  return parseTagDouble(name, lines);
  case 7://byte array
  return parseTagByteArray(name, lines);
  case 8://string
  return parseTagString(name, lines);
  case 9://tag list
  return parseTagList(name, lines);
  case 10://tag compound
  return parseTagCompound(name, lines);
  case 11://int array
  return parseTagIntArray(name, lines);
  default:
  break;
  }
  return null;
  }

private static NBTTagList parseTagList(String name, List<String> lines)
  { 
  NBTTagList tag = new NBTTagList(name);
  tag.setName(name); 
  Iterator<String> it = lines.iterator();
  String line;
  String tagname;
  while(it.hasNext())
    {
    List<String> tagLines = getLinesForNextTag(it);
    byte type = getTagType(tagLines);
    if(type>0)
      {
      tagname = getTagName(tagLines);
      scrubShell(tagLines);
      tag.appendTag(parseTag(tagname, type, tagLines));
      } 
    }
  return tag;
  }

private static NBTTagString parseTagString(String name, List<String> lines)
  {
  NBTTagString tag = new NBTTagString(name);
  tag.data = lines.get(0);
  return tag;
  }

private static NBTTagIntArray parseTagIntArray(String name, List<String> lines)
  {
  NBTTagIntArray tag = new NBTTagIntArray(name);
  tag.intArray = StringTools.parseIntArray(lines.get(0));
  return tag;
  }

private static NBTTagByteArray parseTagByteArray(String name, List<String> lines)
  {
  NBTTagByteArray tag = new NBTTagByteArray(name);
  tag.byteArray = StringTools.parseByteArray(lines.get(0));
  return tag;
  }

private static NBTTagDouble parseTagDouble(String name, List<String> lines)
  {
  NBTTagDouble tag = new NBTTagDouble(name);
  tag.data = StringTools.safeParseDouble(lines.get(0));
  return tag;
  }

private static NBTTagFloat parseTagFloat(String name, List<String> lines)
  { 
  NBTTagFloat tag = new NBTTagFloat(name);
  tag.data = StringTools.safeParseFloat(lines.get(0));
  return tag;
  }

private static NBTTagLong parseTagLong(String name, List<String> lines)
  {
  NBTTagLong tag = new NBTTagLong(name);
  tag.data = StringTools.safeParseLong(lines.get(0));
  return tag;
  }

private static NBTTagInt parseTagInt(String name, List<String> lines)
  {
  NBTTagInt tag = new NBTTagInt(name);
  tag.data = StringTools.safeParseInt(lines.get(0));
  return tag;
  }

private static NBTTagShort parseTagShort(String name, List<String> lines)
  { 
  NBTTagShort tag = new NBTTagShort(name);
  tag.data = (short)StringTools.safeParseInt(lines.get(0));
  return tag;
  }

private static NBTTagByte parseTagByte(String name, List<String> lines)
  {
  NBTTagByte tag = new NBTTagByte(name);
  tag.data = StringTools.safeParseByte(lines.get(0));
  return tag;
  }

private static NBTTagCompound parseTagCompound(String name, List<String> lines)
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setName(name); 
  Iterator<String> it = lines.iterator();
  String line;
  String tagname;
  while(it.hasNext())
    {
    List<String> tagLines = getLinesForNextTag(it);
    byte type = getTagType(tagLines);
    if(type>0)
      {
      tagname = getTagName(tagLines);
      scrubShell(tagLines);
      tag.setTag(tagname, parseTag(tagname, type, tagLines));
      }   
    }
  return tag;
  }

/**
 * scans until it finds a line with TAG.
 * opens on the next open brace, closes on that tags closing brace
 * returns all lines, including the TAG line
 * @param it
 * @return
 */
private static List<String> getLinesForNextTag(Iterator<String> it)
  {
  List<String> tagLines = new ArrayList<String>();
  String line;
  boolean started = false;//have we found the TAG line
  int openCount = 0;
  int closeCount = 0;
  boolean foundOpen = false;//have we found the opening brace of the TAG
  boolean closeNext = false;//should we close on the next close brace?
  while(it.hasNext())
    {
    line = it.next();
    it.remove();
    tagLines.add(line);
    if(line.startsWith("TAG"))
      {
      started = true;
      continue;
      }
    if(started)
      {
      if(line.equals("{"))
        {        
        openCount++;
        }      
      else if(line.equals("}"))
        {
        closeCount++;
        }
      if(openCount==closeCount && openCount>0 && closeCount>0)
        {
        break;
        }
      }     
    }
//  Config.logDebug("found tag lines----------------------------------------------");
//  for(String st : tagLines)
//    {
//    Config.logDebug(st);
//    }
//  Config.logDebug("END tag lines----------------------------------------------");
  return tagLines;
  }

/**
 * peeks at the first line of TAGLINES and examines the tag type
 * @param tagLines
 * @return
 */
private static  byte getTagType(List<String> tagLines)
  {
  String line;
  if(tagLines.size()>=1)
    {
    line = tagLines.get(0);
    String[] sp = line.split("=");
    if(sp.length>=2)
      {
      return StringTools.safeParseByte(sp[1]);
      }
    }
  return 0;
  }

/**
 * peeks at the first line of TAGLINES and examines the tag name, if any
 * @param tagLines
 * @return
 */
private static String getTagName(List<String> tagLines)
  {
  String line = "";
  if(tagLines.size()>=1)
    {
    line = tagLines.get(0);
    String[] sp = line.split("=");
    if(sp.length>=3)
      {
      line = sp[2];
      }
    }
  return line;
  }

/**
 * scrubs the TAG, open, and close brace lines off of a TAGLINES array
 */
private static  void scrubShell(List<String> tagLines)
  {
//  Config.logDebug("scrubbing lines");
  Iterator<String> it = tagLines.iterator();
  String line;
  if(it.hasNext())
    {
    line = it.next();    
    it.remove();
    if(it.hasNext())
      {
      line = it.next();
      it.remove();
      if(tagLines.size()>0)
        {
        tagLines.remove(tagLines.size()-1);
        }
      }    
    }  
//  Config.logDebug("post scrub tag lines----------------------------------------------");
//  for(String st : tagLines)
//    {
//    Config.logDebug(st);
//    }
//  Config.logDebug("END post scrub tag lines----------------------------------------------");
  }

}
