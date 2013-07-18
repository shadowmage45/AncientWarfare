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
package shadowmage.ancient_warfare.common.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.nbt.CompressedStreamTools;
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

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

public class NBTWriter
{


/**
 * Writes a compressed NBTTagCompound to the OutputStream
 */
public static void writeNBTTagCompound(NBTTagCompound tag, DataOutputStream data) throws IOException
  {
  if (tag == null)
    {
    data.writeShort(-1);
    }
  else
    {
    byte[] var2 = CompressedStreamTools.compress(tag);
    data.writeShort((short)var2.length);
    data.write(var2);
    }
  }

/**
 * Reads a compressed NBTTagCompound from the InputStream
 */
public static NBTTagCompound readNBTTagCompound(DataInputStream data) throws IOException
  {
  short var1 = data.readShort();
  
  if (var1 < 0)
    {
    return null;
    }
  else
    {
    byte[] var2 = new byte[var1];
    data.readFully(var2);
    return CompressedStreamTools.decompress(var2);
    }
  }

/**
 * read a tag from a datastream, using google iowrapper
 * @param data
 * @return
 */
public static NBTTagCompound readTagFromStream(ByteArrayDataInput data)
  {
  short var1 = data.readShort();  
  if (var1 < 0)
    {
    return null;
    }
  else
    {
    byte[] var2 = new byte[var1];
    data.readFully(var2);
    try
      {
      return CompressedStreamTools.decompress(var2);
      } 
    catch (IOException e)
      {
      e.printStackTrace();
      }
    }
  return new NBTTagCompound();
  }

public static void writeTagToStream(NBTTagCompound tag, ByteArrayDataOutput data)
  {
  if (tag == null)
    {
    data.writeShort(-1);
    }
  else
    {
    byte[] var2;
    try
      {
      var2 = CompressedStreamTools.compress(tag);
      data.writeShort((short)var2.length);
      data.write(var2);
      } 
    catch (IOException e)
      {
      e.printStackTrace();      
      }
    }
  
  }

/**
 * return an NBTTagCompound as a list of strings, for human editing/etc.  WILL make for larger file-sizes...
 * @param tag
 * @return
 */
public static List<String> writeNBTToStrings(NBTTagCompound tag)
  {
  ArrayList<String> lines = new ArrayList<String>();
  writeCompoundTag(tag, lines);
  return lines;
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
/**
 * 
 * @param tag
 * @param lines
 */
private static void writeCompoundTag(NBTTagCompound tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  Collection<NBTBase> cl = tag.getTags();
  for(NBTBase bit : cl)
    {
    writeTag(bit, lines);
    }
  writeTagExit(tag, lines);
  }

private static void writeTag(NBTBase bit, List<String> lines)
  {
  switch(bit.getId())
  {
  case 0:
  //END TAG...
  break;
  case 1:
  writeByteTag(bit, lines);
  break;
  case 2:
  writeShortTag(bit, lines);
  break;
  case 3:
  writeIntTag(bit, lines);
  break;
  case 4:
  writeLongTag(bit, lines);
  break;
  case 5:
  writeFloatTag(bit, lines);
  break;
  case 6:
  writeDoubleTag(bit, lines);
  break;
  case 7:
  writeByteArrayTag(bit, lines);
  break;
  case 8:
  writeStringTag(bit, lines);
  break;
  case 9:
  writeListTag(bit, lines);
  break;
  case 10:
  writeCompoundTag((NBTTagCompound)bit, lines);
  break;
  case 11:
  writeIntArrayTag(bit, lines);
  break;
  default:
  break;
  }
  }

private static void writeStringTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  lines.add(((NBTTagString)tag).data);
  writeTagExit(tag, lines);
  }

private static void writeShortTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  lines.add(String.valueOf( ((NBTTagShort)tag).data ));
  writeTagExit(tag, lines);
  }

private static void writeLongTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  lines.add(String.valueOf( ((NBTTagLong)tag).data ));
  writeTagExit(tag, lines);
  }

private static void writeListTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  NBTTagList list = (NBTTagList)tag;
  for(int i = 0; i < list.tagCount(); i++)
    {
    writeTag(list.tagAt(i), lines);
    }
  writeTagExit(tag, lines);
  }

private static void writeIntArrayTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  lines.add(StringTools.getCSVStringForArray(  ((NBTTagIntArray)tag).intArray  ));
  writeTagExit(tag, lines);
  }

private static void writeIntTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  lines.add(String.valueOf( ((NBTTagInt)tag).data ));
  writeTagExit(tag, lines);
  }

private static void writeFloatTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  lines.add(String.valueOf( ((NBTTagFloat)tag).data ));
  writeTagExit(tag, lines);
  }

private static void writeDoubleTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  lines.add(String.valueOf( ((NBTTagDouble)tag).data ));
  writeTagExit(tag, lines);
  }

private static void writeByteArrayTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  lines.add(StringTools.getCSVStringForArray(  ((NBTTagByteArray)tag).byteArray  ));
  writeTagExit(tag, lines);
  }

private static void writeByteTag(NBTBase tag, List<String> lines)
  {
  writeTagLeader(tag, lines);
  lines.add(String.valueOf( ((NBTTagByte)tag).data ));
  writeTagExit(tag, lines);
  }

private static void writeTagLeader(NBTBase tag, List<String> lines)
  {
  lines.add("TAG="+tag.getId()+"="+tag.getName());
  lines.add("{");
  }

private static void writeTagExit(NBTBase tag, List<String> lines)
  {
  lines.add("}");
  }

}
