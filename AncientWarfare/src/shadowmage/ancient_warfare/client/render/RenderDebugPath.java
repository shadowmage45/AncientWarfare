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
package shadowmage.ancient_warfare.client.render;

import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;

public class RenderDebugPath
{

static float range = 30;
public static void renderPaths(World world, EntityPlayer player, float partialTick)
  {  
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(player.posX-range, player.posY-range, player.posZ-range, player.posX+range, player.posY+range, player.posZ+range);
  List<NpcBase> entList = world.getEntitiesWithinAABB(NpcBase.class, bb);
  List<Node> path;
  NpcBase npc;
  if(entList!=null && !entList.isEmpty())
    {
    Iterator<NpcBase> it = entList.iterator();
    while(it.hasNext())
      {
      npc = it.next();
      path = npc.nav.getCurrentPath();
      for(Node nd : path)
        {
        bb = AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(nd.x, nd.y, nd.z, nd.x+1, nd.y+1, nd.z+1);
        bb = AWRenderHelper.instance().adjustBBForPlayerPos(bb, player, partialTick);
        BoundingBoxRender.drawOutlinedBoundingBox(bb, 1.0f, 0.2f, 1.f);
        }
      }
    }
  
  }

}
