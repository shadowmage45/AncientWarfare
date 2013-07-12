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
package shadowmage.ancient_warfare.common.lang;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Scanner;

import cpw.mods.fml.common.registry.LanguageRegistry;

import shadowmage.ancient_warfare.common.config.Config;

public class LanguageLoader
{

private static LanguageLoader INSTANCE = new LanguageLoader();
private LanguageLoader(){}
public static LanguageLoader instance(){return INSTANCE;}

String defaultLanguages[] = new String[]{"en_US.lang"};

public void loadLanguageFiles()
  {
  Properties languageFile = new Properties();
  try
    {
    languageFile.load(this.getClass().getResourceAsStream("/lang/ancientwarfare/en_US.lang"));    
    if(!languageFile.isEmpty())
      {
      LanguageRegistry.instance().addStringLocalization(languageFile, "en_US");
      }
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }
  }
}
