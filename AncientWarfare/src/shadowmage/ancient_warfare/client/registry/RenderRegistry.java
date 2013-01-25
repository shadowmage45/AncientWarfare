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
package shadowmage.ancient_warfare.client.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;


/**
 * client-side rendering registry
 * @author Shadowmage
 *
 */
public class RenderRegistry
{
private RenderRegistry(){}
public static RenderRegistry instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new RenderRegistry();
    }
  return INSTANCE;
  }
private static RenderRegistry INSTANCE;

private Map<Class<? extends Entity>, Render> renderMap = new HashMap<Class<? extends Entity>, Render>();

public void load()
  {
  /**
   * add renders here
   */
  }

private void registerRender(Class<? extends Entity> clz, Render render)
  {
  
  }

public Render getRenderFor(Entity ent)
  {
  return this.renderMap.get(ent.getClass());
  }

}
