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
package shadowmage.ancient_warfare.client.render.machine;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import shadowmage.ancient_warfare.client.model.ModelEngineWheel;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.machine.TEEngine;
import shadowmage.ancient_warfare.common.plugins.bc.TEEngineWaterwheelBC;

public class RenderTEEngineWaterwheel extends RenderTEMotor
{
ModelEngineWheel wheel = new ModelEngineWheel();

public RenderTEEngineWaterwheel()
  {
  
  }

@Override
public void renderExtras(TEEngine te, float f)
  {
  Minecraft.getMinecraft().renderEngine.bindTexture(Config.texturePath+"models/"+"engineWheel.png");
//  GL11.glRotatef(180, 0, 1, 0);
  TEEngineWaterwheelBC engine = (TEEngineWaterwheelBC)te;
  wheel.setDirection(engine.getFacing());
  wheel.setWheelRotation(engine.getWheelRotation(), (1-f) * engine.getWheelSpeed());
  wheel.renderModel();  
  }
}
