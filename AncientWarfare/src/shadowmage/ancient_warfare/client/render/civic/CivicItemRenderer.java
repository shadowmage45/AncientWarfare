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
package shadowmage.ancient_warfare.client.render.civic;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.common.block.BlockLoader;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class CivicItemRenderer implements IItemRenderer
{

/**
 * 
 */
public CivicItemRenderer()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public boolean handleRenderType(ItemStack item, ItemRenderType type)
  {
  if(type==ItemRenderType.FIRST_PERSON_MAP)
    {
    return false;
    }
  return true;
  }

@Override
public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
  {
  if(type==ItemRenderType.FIRST_PERSON_MAP)
    {
    return false;
    }
  return true;
  }

@Override
public void renderItem(ItemRenderType type, ItemStack item, Object... data)
  {
  if(type==ItemRenderType.FIRST_PERSON_MAP)
    {
    return;
    }
  RenderBlocks render = (RenderBlocks)data[0];
  int blockNum = item.getItemDamage()/16;
  Minecraft.getMinecraft().renderEngine.bindTexture("/terrain.png");
  Block blk = null;
  switch(blockNum)
  {
  case 0:
  blk = BlockLoader.civicBlock1;
  break;
  
  case 1:
  blk = BlockLoader.civicBlock2;
  break;
  
  case 2:
  blk = BlockLoader.civicBlock3;
  break;
  
  case 3:
  blk = BlockLoader.civicBlock4;
  break;
  
  default:
  blk = BlockLoader.civicBlock1;
  break;
  }
  GL11.glPushMatrix();
  if(type!=ItemRenderType.ENTITY)
    {
    GL11.glTranslatef(0.5f, 0.5f, 0.5f);
    GL11.glRotatef(180, 0, 1, 0);
    }
  else
    {
    GL11.glScalef(0.5f, 0.5f, 0.5f);
    }
  render.renderBlockAsItem(blk, item.getItemDamage()%16, 1.f);
  GL11.glPopMatrix();
  }

}
