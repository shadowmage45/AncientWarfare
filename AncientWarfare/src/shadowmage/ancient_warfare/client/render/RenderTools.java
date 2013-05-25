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

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.common.config.Config;

public class RenderTools
{

static float zLevel = 0;
/** Missing texture image */
static TextureMap texMap = new TextureMap(1, "items", "textures/custom_icons/", new BufferedImage(64, 64, 2));

private static Map<String, Icon> iconMap = new HashMap<String, Icon>();

public static Icon registerIcon(String tex)
  {
  Icon ico = texMap.registerIcon(tex);
  iconMap.put(tex, ico);
  return ico;
  }

public static Icon getIcon(String tex)
  {
  Config.logDebug("getting icon from custom icon map: "+tex);
  Icon ico = iconMap.get(tex);
  if(ico==null)
    {
    ico = registerIcon(tex);
    }
  return ico;
  }

/**
 * renders the given icon texture, of given size, at the current render coordinates
 * (pre-translate to position before rendering)
 * @param tex
 * @param width
 * @param height
 */
public static void renderIcon(String tex, int width, int height, int x, int y)
  {
  Tessellator tess = Tessellator.instance;
  Minecraft.getMinecraft().renderEngine.bindTexture(tex);
  int halfW = width/2;
  int halfH = height/2;
  tess.startDrawingQuads();
  tess.addVertexWithUV(x-halfW, y-halfH, 0, 0, 0); 
  tess.addVertexWithUV(x-halfW, y+halfH, 0, 0, 1);
  tess.addVertexWithUV(x+halfW, y+halfH, 0, 1, 1);
  tess.addVertexWithUV(x+halfW, y+-halfH, 0, 1, 0);
  tess.draw();
  }

/**
 * renders the four corners of a texture, from the corner inward (e.g. for size-adaptable elements)
 * origin tex MUST be  256x256
 * @param x renderPosX
 * @param y renderPosY
 * @param w renderWidth
 * @param h renderHeight
 * @param tw textureUsedWidth
 * @param th textureUsedHeight
 * @param tex theTexture
 * @param u textureStartX
 * @param v textureStartY
 */
public static void drawQuadedTexture(int x, int y, int w, int h, int tw, int th, String tex, int u, int v)
  {  
  int halfW = w/2;
  int halfH = h/2;  
  int u1 = u + tw - halfW;
  int v1 = v + th - halfH;
  Minecraft.getMinecraft().renderEngine.bindTexture(tex);
  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
  drawTexturedModalRect(x, y, u, v, halfW, halfH);
  drawTexturedModalRect(x + halfW, y, u1, v, halfW, halfH);
  drawTexturedModalRect(x, y + halfH, u, v1, halfW, halfH);
  drawTexturedModalRect(x + halfW, y + halfH, u1, v1, halfW, halfH);
  }

public static void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6)
  {
  float f = 0.00390625F;
  float f1 = 0.00390625F;
  Tessellator tessellator = Tessellator.instance;
  tessellator.startDrawingQuads();
  tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + par6) * f1));
  tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + par6), (double)zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + par6) * f1));
  tessellator.addVertexWithUV((double)(par1 + par5), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + par5) * f), (double)((float)(par4 + 0) * f1));
  tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)zLevel, (double)((float)(par3 + 0) * f), (double)((float)(par4 + 0) * f1));
  tessellator.draw();
  }

public static void drawTexturedModelRectFromIcon(int par1, int par2, Icon par3Icon, int par4, int par5)
  {
  Tessellator tessellator = Tessellator.instance;
  tessellator.startDrawingQuads();
  tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + par5), (double)zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMaxV());
  tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + par5), (double)zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMaxV());
  tessellator.addVertexWithUV((double)(par1 + par4), (double)(par2 + 0), (double)zLevel, (double)par3Icon.getMaxU(), (double)par3Icon.getMinV());
  tessellator.addVertexWithUV((double)(par1 + 0), (double)(par2 + 0), (double)zLevel, (double)par3Icon.getMinU(), (double)par3Icon.getMinV());
  tessellator.draw();
  }
}
