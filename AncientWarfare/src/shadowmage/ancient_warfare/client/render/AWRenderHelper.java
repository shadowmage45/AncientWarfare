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

import shadowmage.ancient_warfare.common.config.Settings;
import shadowmage.ancient_warfare.common.interfaces.IScannerItem;
import shadowmage.ancient_warfare.common.item.ItemBuilderBase;
import shadowmage.ancient_warfare.common.item.ItemBuilderDirect;
import shadowmage.ancient_warfare.common.item.ItemStructureScanner;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;
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

protected void renderScanningBB()
  {

  }

/**
 * @param bb
 * @param player
 * @param partialTick
 * @return
 */
protected AxisAlignedBB adjustBBForPlayerPos(AxisAlignedBB bb, EntityPlayer player, float partialTick)
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

  if(Settings.getRenderAdvOverlay() && player.ridingEntity instanceof VehicleBase && mc.currentScreen==null)
    {
    this.renderAdvancedVehicleOverlay((VehicleBase)player.ridingEntity, player, evt.partialTicks);
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
  if(ItemBuilderBase.hasScanBB(id))
    {
    this.renderScannerBB(player, stack, (ItemBuilderDirect)stack.getItem(), evt.partialTicks);
    }
  if(ItemStructureScanner.isScannerItem(id))
    {
    this.renderScannerBB(player, stack, (ItemStructureScanner)stack.getItem(), evt.partialTicks);
    }


  }

public void renderAdvancedVehicleOverlay(VehicleBase vehicle, EntityPlayer player, float partialTick)
  {  
  GL11.glPushMatrix();
  GL11.glEnable(GL11.GL_BLEND);
  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  GL11.glDisable(GL11.GL_TEXTURE_2D);
//  GL11.glDisable(GL11.GL_LIGHTING);
//  GL11.glDisable(GL11.GL_DEPTH_TEST);
  //GL11.glDepthMask(false);
  GL11.glColor4d(1, 1, 1, 0.6d);
  
  double x1 = vehicle.posX - player.posX;
  double y1 = vehicle.posY - player.posY;
  double z1 = vehicle.posZ - player.posZ;
  
  double x2 = x1 - 20 * Trig.sinDegrees(vehicle.rotationYaw);
  double y2 = y1;
  double z2 = z1 - 20 * Trig.cosDegrees(vehicle.rotationYaw);

  GL11.glLineWidth(3f);
  GL11.glBegin(GL11.GL_LINES);
    
  GL11.glVertex3d(x1, y1+0.12d, z1);
  GL11.glVertex3d(x2, y2+0.12d, z2);
  
  GL11.glEnd();
  
  
  GL11.glLineWidth(4f);
  
  
  GL11.glColor4f(1.f, 0.4f, 0.4f, 0.4f);
  GL11.glBegin(GL11.GL_LINES);
  Pos3f offset = vehicle.getMissileOffset();
  x2 = x1+offset.x;
  y2 = y1+offset.y;
  z2 = z1+offset.z;
   
  double gravity = 9.81d * 0.05d *0.05d;
  double speed = vehicle.firingHelper.clientLaunchSpeed * 0.05d;
  double angle = 90 - vehicle.firingHelper.clientTurretPitch;
  double yaw = vehicle.firingHelper.clientTurretYaw;
  
  double vH = -Trig.sinDegrees((float) angle)*speed;
  double vY = Trig.cosDegrees((float) angle)*speed ;
  double vX = Trig.sinDegrees((float) yaw)*vH ;
  double vZ = Trig.cosDegrees((float) yaw)*vH ;
  
  
  while(y2>=y1)
    {
    GL11.glVertex3d(x2, y2, z2);
    vY -= gravity;
    x2+=vX;
    z2+=vZ;
    y2+=vY;  
    GL11.glVertex3d(x2, y2, z2);
    }
  GL11.glEnd();
  
  GL11.glPopMatrix();

  GL11.glDepthMask(true);
  
  GL11.glDisable(GL11.GL_BLEND);
  GL11.glEnable(GL11.GL_TEXTURE_2D);
  }





}
