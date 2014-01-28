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
package shadowmage.ancient_vehicles.client.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_framework.client.model.ModelBaseAW;

public class RenderTest
{

float rotation = 0;
String modelsPath = "/assets/ancientwarfare/models";
ResourceLocation testTex = new ResourceLocation("ancientwarfare", "models/test.png");
private ModelBaseAW testModel;

public RenderTest() throws IOException
  {
  InputStream is = this.getClass().getResourceAsStream(modelsPath+"/test.mmf");
  if(is==null)
    {
    throw new IllegalArgumentException("could not load test.mmf from : "+modelsPath+"/test.mmf");
    }
  List<String> lines = new ArrayList<String>();
  String line;
  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
  while((line = reader.readLine())!=null)
    {
    lines.add(line);
    }
  testModel = new ModelBaseAW();
  testModel.parseFromLines(lines);  
  }

@ForgeSubscribe
public void renderWorld(RenderWorldLastEvent evt)
  {
//  GL11.glEnable(GL11.GL_LIGHTING);
//  rotation += 1.f;
  GL11.glPushMatrix();
  GL11.glTranslatef(0, 0, 1);
//  testModel.setPieceRotation("foopiece", rotation*0.1f, 0, 0);
//  testModel.setPieceRotation("foopiece2", 0, rotation*0.1f, 0);
  evt.context.renderEngine.bindTexture(testTex);
  testModel.renderModel();
  GL11.glPopMatrix();
//  GL11.glDisable(GL11.GL_LIGHTING);
  }

}
