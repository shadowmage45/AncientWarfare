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
package shadowmage.ancient_warfare.common.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import shadowmage.ancient_warfare.common.config.Config;

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
  if(lines.size()>=2)
    {
    lines.remove(lines.size()-1);
    }
  Iterator<String> it = lines.iterator();
  if(it.hasNext())
    {
    String headerLine = it.next();
    String[] headerBits = headerLine.split("=");
    if(headerBits[0].equals("TAG"))
      {
      it.remove();
      it.next();
      it.remove();
      byte tagType = StringTools.safeParseByte(headerBits[1]);      
      String name = "";
      if(headerBits.length>=3)
        {
        name = headerBits[2];
        }      
      tag = (NBTTagCompound)parseTag(name, (byte)10, lines);
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
  while(it.hasNext())
    {
    List<String> tagLines = getLinesForNextTag(it);  
    if(tagLines.size()>=2)
      {
      tagLines.remove(tagLines.size()-1);
      }
    Iterator<String> tagIt = tagLines.iterator();
    if(tagIt.hasNext())
      {
      line = tagIt.next();
      if(line.startsWith("TAG"))
        {
        tagIt.remove();
        tagIt.next();
        tagIt.remove();
        String[] headerBits = line.split("=");
        String tagName = "";
        if(headerBits.length>=3)
          {
          tagName = headerBits[2];
          }
        byte tagType = StringTools.safeParseByte(headerBits[1]);
        tag.appendTag(parseTag(tagName, tagType, tagLines));
        }
      }    
    }
  return tag;
  }

private static NBTTagString parseTagString(String name, List<String> lines)
  {
  NBTTagString tag = new NBTTagString(name);
  tag.data = lines.get(0);
  Config.logDebug("creating tag :"+tag+" datas: "+tag.data);
  return tag;
  }

private static NBTTagIntArray parseTagIntArray(String name, List<String> lines)
  {
  NBTTagIntArray tag = new NBTTagIntArray(name);
  tag.intArray = StringTools.parseIntArray(lines.get(0));
  Config.logDebug("creating tag :"+tag+" datas: "+tag.intArray);
  return tag;
  }

private static NBTTagByteArray parseTagByteArray(String name, List<String> lines)
  {
  NBTTagByteArray tag = new NBTTagByteArray(name);
  tag.byteArray = StringTools.parseByteArray(lines.get(0));
  Config.logDebug("creating tag :"+tag+" datas: "+tag.byteArray);
  return tag;
  }

private static NBTTagDouble parseTagDouble(String name, List<String> lines)
  {
  NBTTagDouble tag = new NBTTagDouble(name);
  tag.data = StringTools.safeParseDouble(lines.get(0));
  Config.logDebug("creating tag :"+tag+" datas: "+tag.data);
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
  while(it.hasNext())
    {
    List<String> tagLines = getLinesForNextTag(it);
    if(tagLines.size()>=2)
      {
      tagLines.remove(tagLines.size()-1);
      }
    Iterator<String> tagIt = tagLines.iterator();
    if(tagIt.hasNext())
      {
      line = tagIt.next();
      if(line.startsWith("TAG"))
        {
        tagIt.remove();
        tagIt.next();
        tagIt.remove();
        String[] headerBits = line.split("=");
        String tagName = "";
        if(headerBits.length>=3)
          {
          tagName = headerBits[2];
          }
        byte tagType = StringTools.safeParseByte(headerBits[1]);
        NBTBase newTag = parseTag(tagName, tagType, tagLines);
        tag.setTag(newTag.getName(), newTag);        
        }
      }    
    }
  return tag;
  }

private static List<String> getLinesForNextTag(Iterator<String> it)
  {
  LinkedList<String> tagLines = new LinkedList<String>();
  String line;
  boolean foundOpen = false;
  boolean closeNext = false;
  while(it.hasNext())
    {
    line = it.next();
    it.remove();
    tagLines.add(line);
    if(line.equals("{"))
      {      
      if(!foundOpen)
        {
        foundOpen = true;
        closeNext = true;
        }
      else
        {
        closeNext = false;
        }
      }
    if(line.equals("}"))
      {
      if(closeNext)
        {
        break;
        }
      else
        {
        closeNext = true;
        }
      }    
    }
//  tagLines.removeFirst();
//  tagLines.removeLast();
//  Config.logDebug("found tag lines----------------------------------------------");
//  for(String st : tagLines)
//    {
//    Config.logDebug(st);
//    }
//  Config.logDebug("END tag lines----------------------------------------------");
  return tagLines;
  }

}
