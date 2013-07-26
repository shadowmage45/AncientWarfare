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
package shadowmage.ancient_warfare.client.render.redstone;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.common.machine.redstone.TERedstoneLogic;
import shadowmage.ancient_warfare.common.machine.redstone.logic.IRedstoneLogicTile;
import shadowmage.ancient_warfare.common.machine.redstone.logic.RedstoneCable;

public class RenderRedstoneHelper extends TileEntitySpecialRenderer
{

protected HashMap<Class, RenderLogic> tileRenders = new HashMap<Class, RenderLogic>();

/**
 * 
 */
public RenderRedstoneHelper()
  {
  this.tileRenders.put(RedstoneCable.class, new RenderCable());
  }


@Override
public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
  {
  GL11.glPushMatrix();
  GL11.glTranslated(d0+0.5d, d1+0.5d, d2+0.5d);
  TERedstoneLogic te = (TERedstoneLogic)tileentity;
  Minecraft.getMinecraft().renderEngine.bindTexture("foo");
  for(IRedstoneLogicTile t : te.tiles)
    {
    if(t==null){continue;}
    if(this.tileRenders.containsKey(t.getClass()))
      {
      GL11.glPushMatrix();
      this.tileRenders.get(t.getClass()).renderTile(t, te, f);
      GL11.glPopMatrix();
      }
    }
  GL11.glPopMatrix();
  }



}
