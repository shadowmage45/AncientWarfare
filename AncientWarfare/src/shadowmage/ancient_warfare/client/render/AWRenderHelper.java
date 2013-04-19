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

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.config.Settings;
import shadowmage.ancient_warfare.common.interfaces.IScannerItem;
import shadowmage.ancient_warfare.common.item.ItemBuilderBase;
import shadowmage.ancient_warfare.common.item.ItemBuilderDirect;
import shadowmage.ancient_warfare.common.item.ItemCivicPlacer;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.item.ItemStructureScanner;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AWRenderHelper
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


private void renderScannerBB(EntityPlayer player, ItemStack stack, IScannerItem item, float partialTick)
  {
  if(player==null || stack==null || item== null)
    {
    return;
    }    
  BlockPosition p1 = item.getScanPos1(stack);
  if(p1==null)
    {
    return;
    }  
  int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
  BlockPosition p2 = item.getScanPos2(stack);
  if(p2==null)
    {
    p2 = BlockTools.getBlockClickedOn(player, player.worldObj, player.isSneaking());
    if(p2==null)
      {   
      return;
      }
    }
  adjustPositionsForScanBB(p1, p2);
  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z);
  BoundingBoxRender.drawOutlinedBoundingBox(adjustBBForPlayerPos(bb, player, partialTick).contract(.02D, .02D, .02D), 0.8f, 0.2f, 0.8f);
  }

/**
 * uhh..yah......wonderful manual correction...
 * @param p1
 * @param p2
 */
private void adjustPositionsForScanBB(BlockPosition p1, BlockPosition p2)
  {
  if(p2.x<p1.x)
    {
    p1.x++;
    }
  else
    {
    p2.x++;
    }
  if(p2.z<p1.z)
    {
    p1.z++;
    }
  else
    {
    p2.z++;
    }
  if(p1.y>p2.y)
    {
    p1.y++;
    }
  else
    {
    p2.y++;
    }
  }

private void renderStructureBB(EntityPlayer player, ItemStack stack, ItemBuilderBase item, float partialTick)
  {
  if(player==null || stack==null || item== null)
    {
    return;
    }  
  StructureClientInfo info = item.getStructureForStack(stack);    
  if(info==null)
    {
    return;
    }
  BlockPosition hit = BlockTools.getBlockClickedOn(player, player.worldObj, true);
  if(hit==null)
    {
    return;
    }
  int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
  BlockPosition originalHit = hit.copy();
  hit.y-=info.yOffset;
  hit = this.offsetForWorldRender(hit, face);  

  if(item.renderBuilderBlockBB())
    {
    hit.moveForward(face, -info.zOffset + 1 + info.clearingBuffer);
    } 

  AxisAlignedBB bb = info.getBBForRender(hit, face);  
  BoundingBoxRender.drawOutlinedBoundingBox(adjustBBForPlayerPos(bb, player, partialTick).contract(.02D, .02D, .02D), 0.8f, 0.2f, 0.8f);
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

public BlockPosition offsetForWorldRender(BlockPosition hit, int face)
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
 * @param bb
 * @param player
 * @param partialTick
 * @return
 */
public static AxisAlignedBB adjustBBForPlayerPos(AxisAlignedBB bb, EntityPlayer player, float partialTick)
  {
  double x = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTick;
  double y = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTick;
  double z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTick;  
  return bb.offset(-x, -y, -z);
  }

@ForgeSubscribe
public void handleRenderLastEvent(RenderWorldLastEvent evt)
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
  if(Config.DEBUG)
    {
    RenderDebugPath.renderPaths(player.worldObj, player, evt.partialTicks);
    }
  if(Settings.getRenderAdvOverlay() && player.ridingEntity instanceof VehicleBase && mc.currentScreen==null)
    {
    RenderOverlayAdvanced.renderAdvancedVehicleOverlay((VehicleBase)player.ridingEntity, player, evt.partialTicks);
    }
  ItemStack stack = player.inventory.getCurrentItem();
  if(stack==null || stack.getItem()==null)
    {
    return;
    }
  int id = stack.itemID;

  if(ItemBuilderBase.isBuilderItem(id))
    {
    this.renderStructureBB(player, stack, (ItemBuilderBase)stack.getItem(), evt.partialTicks);
    }  
  else if(ItemBuilderBase.hasScanBB(id))
    {
    this.renderScannerBB(player, stack, (ItemBuilderDirect)stack.getItem(), evt.partialTicks);
    }
  else if(ItemStructureScanner.isScannerItem(id))
    {
    this.renderScannerBB(player, stack, (ItemStructureScanner)stack.getItem(), evt.partialTicks);
    }
  else if(id==ItemLoader.civicPlacer.itemID)
    {
    this.renderScannerBB(player, stack, (ItemCivicPlacer)stack.getItem(), evt.partialTicks);
    }
  }


public static void setTeamRenderColor(int teamNum)
  {
  switch(teamNum)
  {
  case 0:
  GL11.glColor3ub((byte)221,(byte)221,(byte)221); 
  break;
  case 1:  
  GL11.glColor3ub((byte)219,(byte)125,(byte)62);  
  break;     
  case 2:
  GL11.glColor3ub((byte)179,(byte)80,(byte)188); 
  break;      
  case 3:
  GL11.glColor3ub((byte)107,(byte)138,(byte)201); 
  break;      
  case 4:
  GL11.glColor3ub((byte)177,(byte)166,(byte)39); 
  break;
  case 5:
  GL11.glColor3ub((byte)65,(byte)174,(byte)56); 
  break;
  case 6:
  GL11.glColor3ub((byte)208,(byte)132,(byte)153); 
  break;
  case 7:
  GL11.glColor3ub((byte)64,(byte)64,(byte)64); 
  break;
  case 8:
  GL11.glColor3ub((byte)154,(byte)161,(byte)161); 
  break;  
  case 9:
  GL11.glColor3ub((byte)46,(byte)110,(byte)137); 
  break;
  case 10:
  GL11.glColor3ub((byte)126,(byte)61,(byte)181); 
  break;
  case 11:
  GL11.glColor3ub((byte)46,(byte)56,(byte)141); 
  break;
  case 12:
  GL11.glColor3ub((byte)79,(byte)50,(byte)31); 
  break;
  case 13:
  GL11.glColor3ub((byte)53,(byte)70,(byte)27); 
  break;
  case 14:
  GL11.glColor3ub((byte)150,(byte)52,(byte)48); 
  break;
  case 15:
  GL11.glColor3ub((byte)25,(byte)22,(byte)22); 
  break;
  default:
  GL11.glColor4f(1, 1, 1, 1);  
  break;
  }    
  }


}
