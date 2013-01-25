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
package shadowmage.ancient_warfare.client.aw_core.render;

import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import shadowmage.ancient_warfare.client.aw_structure.render.BoundingBoxRender;
import shadowmage.ancient_warfare.common.aw_structure.item.ItemBuilderBase;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class AWRenderHelper implements ITickHandler
{

private static AWRenderHelper INSTANCE;
private AWRenderHelper()
  {  
  }
public static AWRenderHelper instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new AWRenderHelper();
    }
  return INSTANCE;
  }

@Override
public void tickStart(EnumSet<TickType> type, Object... tickData)
  {
  
  }

@Override
public void tickEnd(EnumSet<TickType> type, Object... tickData)
  {
  //System.out.println("rendering from tick");
  //this.renderStructureBB();
  //System.out.println("END rendering from tick");
  }

private void renderStructureBB(float partialTick)
  {
  Minecraft mc = Minecraft.getMinecraft();
  if(mc==null)
    {
    return;
    }
  EntityPlayer player = mc.thePlayer;
  if(player==null)
    {
    return;
    } 
  ItemStack stack = player.inventory.getCurrentItem();
  if(stack==null || stack.getItem()==null)
    {
    return;
    }
  if(stack.getItem() instanceof ItemBuilderBase)
    {
    if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("structData") || !stack.getTagCompound().getCompoundTag("structData").hasKey("name"))
      {
      return;
      }
    List<AxisAlignedBB> bbs =  ((ItemBuilderBase)stack.getItem()).getBBForStructure(player, stack.getTagCompound().getCompoundTag("structData").getString("name"));
    if(bbs!=null)
      {
      for(AxisAlignedBB bb : bbs)
        {
        BoundingBoxRender.drawOutlinedBoundingBox(adjustBBForPlayerPos(bb, player, partialTick).contract(.02D, .02D, .02D));
        }
      }
    } 
  }

@SideOnly(Side.CLIENT)
protected AxisAlignedBB adjustBBForPlayerPos(AxisAlignedBB bb, EntityPlayer player, float partialTick)
  {
  double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTick;
  double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTick;
  double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTick;  
  return bb.offset(-x, -y, -z);
  }

@Override
public EnumSet<TickType> ticks()
  {
  return EnumSet.of(TickType.RENDER);
  }

@Override
public String getLabel()
  {
  return "AWRenderTicker";
  }

@ForgeSubscribe
public void handleRenderLastEvent(RenderWorldLastEvent evt)
  {
  this.renderStructureBB(evt.partialTicks);
  }
}
