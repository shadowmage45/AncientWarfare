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

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_framework.common.registry.DescriptionRegistry;
import shadowmage.ancient_framework.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.item.ItemLoader;

public class CivicItemRenderer implements IItemRenderer
{

public CivicItemRenderer()
  {
  
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
   
  Description d = DescriptionRegistry.instance().getDescriptionFor(ItemLoader.civicPlacer.itemID);
  Icon ico = blk.getIcon(0, item.getItemDamage()%16);//d.getIconFor(item.getItemDamage()*3);
   
  Tessellator tessellator = Tessellator.instance;
  blk.setBlockBoundsForItemRender();
  render.setRenderBoundsFromBlock(blk);
  GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
  GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
  
  
  tessellator.startDrawingQuads();
  tessellator.setNormal(0.0F, -1.0F, 0.0F);
  render.renderFaceYNeg(blk, 0.0D, 0.0D, 0.0D, ico);
  tessellator.draw();

  ico = blk.getIcon(1, item.getItemDamage()%16);
  tessellator.startDrawingQuads();
  tessellator.setNormal(0.0F, 1.0F, 0.0F);
  render.renderFaceYPos(blk, 0.0D, 0.0D, 0.0D, ico);
  tessellator.draw();

  ico = blk.getIcon(2, item.getItemDamage()%16);
  tessellator.startDrawingQuads();
  tessellator.setNormal(0.0F, 0.0F, -1.0F);
  render.renderFaceZNeg(blk, 0.0D, 0.0D, 0.0D, ico);
  tessellator.draw();
  tessellator.startDrawingQuads();
  tessellator.setNormal(0.0F, 0.0F, 1.0F);
  render.renderFaceZPos(blk, 0.0D, 0.0D, 0.0D, ico);
  tessellator.draw();
  tessellator.startDrawingQuads();
  tessellator.setNormal(-1.0F, 0.0F, 0.0F);
  render.renderFaceXNeg(blk, 0.0D, 0.0D, 0.0D, ico);
  tessellator.draw();
  tessellator.startDrawingQuads();
  tessellator.setNormal(1.0F, 0.0F, 0.0F);
  render.renderFaceXPos(blk, 0.0D, 0.0D, 0.0D, ico);
  tessellator.draw();
  GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  GL11.glPopMatrix();
  }

}
