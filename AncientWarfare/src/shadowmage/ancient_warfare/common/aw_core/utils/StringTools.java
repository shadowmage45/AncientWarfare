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
package shadowmage.ancient_warfare.common.aw_core.utils;

public class StringTools
{

public static int[] parseIntArray(String csv)
  {
  String[] splits = csv.split(",");
  int[] array = new int[splits.length];
  for(int i = 0; i< splits.length; i++)
    {
    array[i]=Integer.parseInt(splits[i]);
    }
  return array;
  }

public static byte[] parseByteArray(String csv)
  {
  String[] splits = csv.split(",");
  byte[] array = new byte[splits.length];
  for(int i = 0; i< splits.length; i++)
    {
    array[i]=Byte.parseByte(splits[i]);
    }
  return array;
  }

public static String[] parseStringArray(String csv)
  {
  String[] splits = csv.split(",");
  return splits;
  }

public static String subStringBeginning(String in, int len)
  {  
  return len > in.length() ? in : in.substring(0, len);
  }

/**
 * returns the value after a split at regex, or false
 * @param regex
 * @param test
 * @return
 */
public static boolean safeParseBoolean(String regex, String test)
  {
  String[] split = test.split(regex);
  if(split.length>1)
    {
    return Boolean.parseBoolean(split[1]);
    }  
  return false;
  }

/**
 * returns a value after a split at regex, or an empty string
 * @param regex
 * @param test
 * @return
 */
public static String safeParseString(String regex, String test)
  {
  String[] split = test.split(regex);
  if(split.length>1)
    {
    return split[1];
    }  
  return "";
  }

/**
 * returns a value after a split at regex, or zero (0)
 * @param regex
 * @param test
 * @return
 */
public static int safeParseInt(String regex, String test)
  {
  String[] split = test.split(regex);
  if(split.length>1)
    {
    return Integer.parseInt(split[1]);
    }  
  return 0;
  }


}
