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

public class StringTools
{

/**
 * splits test at regex, returns parsed int array from csv value of remaining string
 * returns size 1 int array if no valid split is found
 * @param regex
 * @param test
 * @return
 */
public static int[] safeParseIntArray(String regex, String test)
  {
  String[] splits = test.split(regex);
  if(splits.length>1)
    {
    return parseIntArray(splits[1]);
    }
  return new int[1];
  }

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

/**
 * splits test at regex, returns parsed byte array from csv value of remaining string
 * returns size 1 byte array if no valid split is found
 * @param regex
 * @param test
 * @return
 */
public static byte[] safeParseByteArray(String regex, String test)
  {
  String[] splits = test.split(regex);
  if(splits.length>1)
    {
    return parseByteArray(splits[1]);
    }
  return new byte[1];
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

public static String[] safeParseStringArray(String regex, String test)
  {
  String[] splits = test.split(regex);
  if(splits.length>1)
    {
    return parseStringArray(splits[1]);
    }  
  splits = new String[1];
  splits[0]="";
  return splits;
  }

public static String[] parseStringArray(String csv)
  {
  String[] splits = csv.split(",");
  return splits;
  }

/**
 * returns beginning of string up to len
 * @param in
 * @param len
 * @return
 */
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

public static boolean safeParseIntAsBoolean(String regex, String test)
  {
  String[] split = test.split(regex);
  if(split.length>1 && Integer.parseInt(split[1])==1)
    {
    return true;
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

/**
 * returns a value after a split at regex, or zero (0)
 * @param regex
 * @param test
 * @return
 */
public static byte safeParseByte(String regex, String test)
  {
  String[] split = test.split(regex);
  if(split.length>1)
    {
    return Byte.parseByte(split[1]);
    }  
  return 0;
  }

/**
 * returns a value after a split at regex, or zero (0)
 * @param regex
 * @param test
 * @return
 */
public static short safeParseShort(String regex, String test)
  {
  String[] split = test.split(regex);
  if(split.length>1)
    {
    return Short.parseShort(split[1]);
    }  
  return 0;
  }

public static boolean isNumber(String test)
  {
  try
    {
    Integer.parseInt(test);
    }
  catch(NumberFormatException e)
    {
    return false;
    }
  return false;
  }

}
