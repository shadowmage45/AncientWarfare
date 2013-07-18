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
package shadowmage.ancient_warfare.client.render.civic;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import shadowmage.ancient_warfare.client.render.BoundingBoxRender;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.missiles.AmmoHwachaRocket;

public class TESRCivic extends TileEntitySpecialRenderer
{

/**
 * 
 */
public TESRCivic()
  {
  // TODO Auto-generated constructor stub
  
  }

@Override
public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float f)
  {
//  TECivic te = (TECivic)tileentity;
//  if(te.getCivic()==null || !te.getCivic().isWorkSite())
//    {
//    return;
//    }
  //TODO add rendering for if has work/etc...
    
  }


}
