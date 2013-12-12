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
package shadowmage.ancient_warfare.common.plugins;

import shadowmage.ancient_framework.common.config.Config;
import shadowmage.ancient_warfare.common.plugins.bc.BCProxy;
import shadowmage.ancient_warfare.common.plugins.bc.BCProxyBase;

public class PluginProxy
{

private PluginProxy(){}
private static final PluginProxy INSTANCE = new PluginProxy();
public static PluginProxy instance(){return INSTANCE;}

public static boolean bcLoaded = false;

public static BCProxyBase bcProxy = new BCProxyBase();

public void detectAndLoadPlugins()
  {
  this.tryLoadBCProxy();
  }

protected void tryLoadBCProxy()
  {  
  try
    {
    Class clz = Class.forName("buildcraft.BuildCraftCore");
    if(clz!=null)
      {
      Config.log("Initializing BC Plugin");
      bcProxy = new BCProxy();      
      bcLoaded = true;
      }
    else
      {
      Config.log("Skipping BC Plugin loading, BuildCraft not detected...");
      }
    } 
  catch (ClassNotFoundException e)
    {
    Config.log("Skipping BC Plugin loading, BuildCraft not detected...");
    return;
    }  
  }


}
