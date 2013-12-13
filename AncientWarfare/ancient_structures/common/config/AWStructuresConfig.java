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
package shadowmage.ancient_structures.common.config;

import java.io.File;
import java.util.logging.Logger;

import shadowmage.ancient_framework.common.config.ModConfiguration;

public class AWStructuresConfig extends ModConfiguration
{

public static final String VERSION = "1.1.037-beta-MC164";//major version(mc version updates/major revisions), minor version(releases), build version(test releases total)

public static String templateExtension = "aws";

/**
 * @param config
 * @param log
 */
public AWStructuresConfig(File config, Logger log, String version)
  {
  super(config, log, version);
  }

@Override
public void initializeCategories()
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void initializeValues()
  {
  // TODO Auto-generated method stub
  
  }



}
