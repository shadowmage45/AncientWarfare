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

public ModConfiguration()
  {

  }

public void loadConfig(File configFile)
  {
  this.config = new Configuration(configFile);
  }

public Configuration getConfig()
  {
  return this.config;
  }

}
