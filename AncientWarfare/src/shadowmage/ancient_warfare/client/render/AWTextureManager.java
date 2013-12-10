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
package shadowmage.ancient_warfare.client.render;

import shadowmage.ancient_warfare.common.config.Config;
import net.minecraft.client.Minecraft;

public class AWTextureManager
{


/**
 * 
 */
public AWTextureManager()
  {
  // TODO Auto-generated constructor stub
  }

public static void bindTexture(String texture)
  {
  texture = "/mods/ancientwarfare/"+texture;
  Minecraft.getMinecraft().renderEngine.bindTexture(texture);
  }


}
