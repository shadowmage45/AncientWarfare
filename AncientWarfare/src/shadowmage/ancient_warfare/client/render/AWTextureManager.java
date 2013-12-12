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

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class AWTextureManager
{

private static HashMap<String, ResourceLocation> textures = new HashMap<String, ResourceLocation>();

/**
 * 
 */
public AWTextureManager()
  {
  // TODO Auto-generated constructor stub
  }

public static void bindTexture(String texture)
  {
  Minecraft.getMinecraft().renderEngine.bindTexture(getResource(texture));
  }

public static ResourceLocation getResource(String texture)
  {
  ResourceLocation resource = textures.get(texture);
  if(resource==null)
    {
    resource = new ResourceLocation("ancientwarfare", texture);
    textures.put(texture, resource);
    }
  return resource;
  }

}
