/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_framework.common.config;

import java.util.HashMap;

public class AWClientConfig
{

private static HashMap<String, ClientConfigOption> clientOptions = new HashMap<String, ClientConfigOption>();

public static void setIntValue(String name, int value)
  {
  ClientConfigOption option = new ClientConfigOption();
  option.dataClass = int.class;
  option.dataValue = new Integer(value);
  clientOptions.put(name, option);
  }

public static void setBooleanValue(String name, boolean value)
  {
  ClientConfigOption option = new ClientConfigOption();
  option.dataClass = boolean.class;
  option.dataValue = new Boolean(value);
  clientOptions.put(name, option);
  }

public static boolean getBooleanValue(String name)
  {
  ClientConfigOption option = clientOptions.get(name);
  if(option!=null && option.dataClass==boolean.class)
    {
    return (Boolean)option.dataValue;
    }  
  return false;
  }

public static int getIntValue(String name)
  {
  ClientConfigOption option = clientOptions.get(name);
  if(option!=null && option.dataClass==int.class)
    {
    return (Integer)option.dataValue;
    }  
  return 0;
  }

private static class ClientConfigOption
  {
  String optionName;  
  Class dataClass;
  Object dataValue;
  }

}
