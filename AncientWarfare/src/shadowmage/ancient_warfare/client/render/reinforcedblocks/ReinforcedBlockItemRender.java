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
package shadowmage.ancient_warfare.client.render.reinforcedblocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.client.render.AWTextureManager;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;

public class ReinforcedBlockItemRender implements IItemRenderer
{

/**
 * 
 */
public ReinforcedBlockItemRender()
  {
  
  }

@Override
public boolean handleRenderType(ItemStack item, ItemRenderType type)
  {
  return true;
  }

@Override
public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
  {
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
  AWTextureManager.bindTexture("/terrain.png");
  Block par1Block = BlockLoader.reinforced; 
    
  Description d = DescriptionRegistry2.instance().getDescriptionFor(par1Block.blockID);
  Icon ico = d.getIconFor(item.getItemDamage());
  
  Tessellator tessellator = Tessellator.instance;
  int j = 0;
  int par2 = 0;
  int par1;
  float par3 = 1.f;  
  boolean flag = false;
  int k;
  float f2;
  float f3;
  
  
  
  if (j == 16)
    {
        par2 = 1;
    }

    par1Block.setBlockBoundsForItemRender();
    render.setRenderBoundsFromBlock(par1Block);
    GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, -1.0F, 0.0F);
    render.renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, ico);
    tessellator.draw();

    if (flag && render.useInventoryTint)
    {
        k = par1Block.getRenderColor(par2);
        f2 = (float)(k >> 16 & 255) / 255.0F;
        f3 = (float)(k >> 8 & 255) / 255.0F;
        float f7 = (float)(k & 255) / 255.0F;
        GL11.glColor4f(f2 * par3, f3 * par3, f7 * par3, 1.0F);
    }

    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 1.0F, 0.0F);
    render.renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, ico);
    tessellator.draw();

    if (flag && render.useInventoryTint)
    {
        GL11.glColor4f(par3, par3, par3, 1.0F);
    }

    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, -1.0F);
    render.renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, ico);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(0.0F, 0.0F, 1.0F);
    render.renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, ico);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
    render.renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, ico);
    tessellator.draw();
    tessellator.startDrawingQuads();
    tessellator.setNormal(1.0F, 0.0F, 0.0F);
    render.renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, ico);
    tessellator.draw();
    GL11.glTranslatef(0.5F, 0.5F, 0.5F);
  }

}
