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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StringTools
{

public static String getCSVValueFor(String[] values)
  {
  String line = "";
  for(int i = 0; i < values.length; i++)
    {
    if(i >=1 )
      {
      line = line + ",";
      }
    line = line + values[i];    
    }
  return "";
  }

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
    array[i]=Integer.parseInt(splits[i].trim());
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
    return parseByteArray(splits[1].trim());
    }
  return new byte[1];
  }

public static byte[] parseByteArray(String csv)
  {
  String[] splits = csv.split(",");
  byte[] array = new byte[splits.length];
  for(int i = 0; i< splits.length; i++)
    {
    array[i]=Byte.parseByte(splits[i].trim());
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
    return Boolean.parseBoolean(split[1].trim());
    }  
  return false;
  }

public static boolean safeParseIntAsBoolean(String regex, String test)
  {
  String[] split = test.split(regex);
  if(split.length>1 && Integer.parseInt(split[1].trim())==1)
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

public static int safeParseInt(String num)
  {  
  try
    {
    return Integer.parseInt(num.trim());
    }
  catch(NumberFormatException e)
    {
    
    }
  return 0;
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
    return Integer.parseInt(split[1].trim());
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
    return Byte.parseByte(split[1].trim());
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
    return Short.parseShort(split[1].trim());
    }  
  return 0;
  }

public static boolean isNumber(String test)
  {
  try
    {
    Integer.parseInt(test.trim());
    }
  catch(NumberFormatException e)
    {
    return false;
    }
  return true;
  }

/**
 * write a list of lines to a byte[] as UTF-8 encoded chars
 * @param lines
 * @return
 * @throws UnsupportedEncodingException
 * @throws IOException
 */
public static byte[] getByteArray(List<String> lines) throws UnsupportedEncodingException, IOException
  {
  ByteArrayOutputStream baos = new ByteArrayOutputStream();
  for(String line : lines)
    {
    line = line +"\n";
    baos.write(line.getBytes("UTF-8"));
    }
  byte[] allBytes = baos.toByteArray();
  return allBytes;
  }

/**
 * read a list of lines from a byte[] as UTF-8 encoded chars
 * @param bytes
 * @return
 */
public static List<String> getLines(byte[] bytes)
  {
  ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
  List<String> lines = new ArrayList<String>();
  Scanner scan = new Scanner(bais, "UTF-8");
  while(scan.hasNext())
    {    
    lines.add(scan.nextLine());
    }
  return lines;
  }

}
