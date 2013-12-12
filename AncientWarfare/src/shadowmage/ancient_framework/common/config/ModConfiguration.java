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

import java.io.File;
import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;

/**
 * static-data configuration class.
 * Each mod will need to construct its own subclass of this, adding static fields for necessary config items
 * @author Shadowmage
 *
 */
public class ModConfiguration
{

public Configuration config;
public Logger logger;
private boolean debug = false;

public ModConfiguration(File configFile, Logger log)
  {
  this.setConfig(configFile);
  this.setLogger(log);
  }

private void setConfig(File configFile)
  {
  this.config = new Configuration(configFile);
  }

private void setLogger(Logger log)
  {
  logger = log;
  }

public Configuration getConfig()
  {
  return this.config;
  }

public void log(String info)
  {
  if(logger!=null)
    {
    logger.info(info);
    }  
  }

public void logDebug(String info)
  {
  if(logger!=null && debug)
    {    
    logger.info(String.valueOf("[DEBUG] "+info));        
    }
  }

public void logError(String info)
  {
  if(logger!=null)
    {
    logger.severe(info);
    }
  }

}
