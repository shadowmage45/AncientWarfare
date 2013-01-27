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

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.item.ItemBuilderBase;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
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
  int id = stack.itemID;
  if(id != ItemLoader.structureBuilderDirect.itemID && id != ItemLoader.structureCreativeBuilder.itemID && id != ItemLoader.structureCreativeBuilderTicked.itemID)
    {    
    return;
    }
  ItemBuilderBase item = (ItemBuilderBase)stack.getItem();
  if(item==null)
    {
    return;
    } 
  StructureClientInfo info = item.getStructureForStack(stack);    
  if(info==null)
    {
    //Config.logDebug("null structInfo");
    return;
    }
  BlockPosition hit = BlockTools.getBlockClickedOn(player, player.worldObj, true);
  if(hit==null)
    {
    //Config.logDebug("null hit");
    return;
    }
  int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
  BlockPosition originalHit = hit.copy();
  hit = this.offsetForWorldRender(hit, face);  
  
  if(item.renderBuilderBlockBB())
    {
    hit.moveForward(face, -info.zOffset + 1);
    } 
  
  AxisAlignedBB bb = info.getBBForRender(hit, face);  
  BoundingBoxRender.drawOutlinedBoundingBox(adjustBBForPlayerPos(bb, player, partialTick).contract(.02D, .02D, .02D), 0.3f, 0.3f, 0.8f);
  //TODO re-render builder block..
  if(item.renderBuilderBlockBB())
    {
    bb = AxisAlignedBB.getBoundingBox(originalHit.x, originalHit.y, originalHit.z, originalHit.x+1, originalHit.y+1, originalHit.z+1);
    BoundingBoxRender.drawOutlinedBoundingBox(adjustBBForPlayerPos(bb, player, partialTick).contract(.02D, .02D, .02D), 0.3f, 0.3f, 0.8f);
    }
  
  if(info.maxClearing > 0|| info.clearingBuffer >0)
    {
    bb = info.getClearingBBForRender(hit, face);
    BoundingBoxRender.drawOutlinedBoundingBox(adjustBBForPlayerPos(bb, player, partialTick).contract(0.1d, 0.1d, 0.1d), 0.8f, 0.f, 0.f);
    }
  
  if(info.maxLeveling >0)
    {
    bb = info.getLevelingBBForRender(hit, face);
    BoundingBoxRender.drawOutlinedBoundingBox(adjustBBForPlayerPos(bb, player, partialTick).contract(0.1d, 0.1d, 0.1d), 0.3f, 0.8f, 0.3f);
    }

  }

protected BlockPosition offsetForWorldRender(BlockPosition hit, int face)
  {
  if(face==0 || face == 1)//south
    {
    hit.moveLeft(face,1);
    }  
  if(face==2 || face==1)
    {
    hit.moveBack(face, 1);
    }
  return hit;
  }
/**
 * 
 *   
  hit = this.offsetForWorldRender(hit, face);
  AxisAlignedBB b = struct.getBBForRender(hit, face);  
  ArrayList<AxisAlignedBB> bbs = new ArrayList<AxisAlignedBB>();
  bbs.add(b);
  return bbs;
  */


/**
 * @param bb
 * @param player
 * @param partialTick
 * @return
 */

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
