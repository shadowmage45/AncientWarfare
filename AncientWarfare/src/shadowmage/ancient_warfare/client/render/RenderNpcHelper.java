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

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.EntityLiving;
import shadowmage.ancient_warfare.common.config.Settings;
import shadowmage.ancient_warfare.common.npcs.NpcBase;

public class RenderNpcHelper extends RenderBiped
{

/**
 * @param par1ModelBiped
 * @param par2
 */
public RenderNpcHelper(ModelBiped par1ModelBiped, float par2)
  {
  super(par1ModelBiped, par2);
  }

@Override
public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
  {
  super.doRenderLiving(par1EntityLiving, par2, par4, par6, par8, par9);
  if(Settings.getRenderNpcNameplates())
    {
    NpcBase npc = (NpcBase) par1EntityLiving;
    String displayLabel = npc.npcType.getDisplayName()+" "+npc.getHealth()+"/"+npc.getMaxHealth();
    this.renderLivingLabel(par1EntityLiving, displayLabel, par2, par4, par6, 64);
    }
  }



}
