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
package shadowmage.ancient_warfare.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.client.render.AWRenderHelper;

public class ModelFlag extends ModelBase
{

ModelRenderer flagPole;
ModelRenderer flagCloth;

public ModelFlag()
  {
  flagPole = new ModelRenderer(this,"flagPole");
  flagPole.setTextureOffset(19,78);
  flagPole.setTextureSize(256,256);
  flagPole.addBox(5.0f,-16.0f,0.0f, 1, 16, 1);
  
  flagCloth = new ModelRenderer(this,"flagCloth");
  flagCloth.setTextureOffset(24,78);
  flagCloth.setTextureSize(256,256);
  flagCloth.addBox(5.f, -16.f, 1.f ,1,8,11);
  }

public void render(int team)
  {
  GL11.glColor4f(0.7f, 0.7f, 0.7f, 1.f);
  flagPole.render(0.0625f);
  AWRenderHelper.instance().setTeamRenderColor(team);
  flagCloth.render(0.0625f);
  GL11.glColor4f(1.f, 1.f, 1.f, 1.f);
  }

}
