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
package shadowmage.ancient_warfare.client.proxy;

import net.minecraftforge.common.MinecraftForge;
import shadowmage.ancient_framework.client.input.TickHandlerClientKeyboard;
import shadowmage.ancient_framework.client.proxy.ClientProxyBase;
import shadowmage.ancient_warfare.client.registry.RenderRegistry;
import shadowmage.ancient_warfare.client.render.AWRenderHelper;
import shadowmage.ancient_warfare.client.render.RenderOverlay;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxyCore extends ClientProxyBase
{

public ClientProxyCore()
  {
  
  }

@Override
public void registerClientData()
  {  
  /**
   * register single render for all vehicles, local renderManager handles from there...
   */
  RenderRegistry.instance().loadRenders();
  TickRegistry.registerTickHandler(new RenderOverlay(), Side.CLIENT);
    
  /**
   * event helper for world-render ticks for BB rendering
   */
  MinecraftForge.EVENT_BUS.register(AWRenderHelper.instance());  
  }

}
