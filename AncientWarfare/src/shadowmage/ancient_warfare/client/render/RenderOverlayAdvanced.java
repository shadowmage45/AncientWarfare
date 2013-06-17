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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import shadowmage.ancient_warfare.common.registry.VehicleRegistry;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.Pos3f;
import shadowmage.ancient_warfare.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.VehicleMovementType;
import shadowmage.ancient_warfare.common.vehicles.missiles.Ammo;
import shadowmage.ancient_warfare.common.vehicles.missiles.AmmoHwachaRocket;

public class RenderOverlayAdvanced
{

public static void renderAdvancedVehicleOverlay(VehicleBase vehicle, EntityPlayer player, float partialTick)
  { 
  if(vehicle.vehicleType == VehicleRegistry.BATTERING_RAM)
    {
    renderBatteringRamOverlay(vehicle, player, partialTick);
    }
  else if(vehicle.ammoHelper.getCurrentAmmoType()!=null && vehicle.ammoHelper.getCurrentAmmoType().isRocket())
    {
    renderRocketFlightPath(vehicle, player, partialTick);
    }
  else
    {
    renderNormalVehicleOverlay(vehicle, player, partialTick);
    }  
  }

public static void renderRocketFlightPath(VehicleBase vehicle, EntityPlayer player, float partialTick)
  {  
  GL11.glPushMatrix();
  GL11.glEnable(GL11.GL_BLEND);
  GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
  GL11.glDisable(GL11.GL_TEXTURE_2D);
  GL11.glColor4d(1, 1, 1, 0.6d);
  
  double x1 = vehicle.posX - player.posX;
  double y1 = vehicle.posY - player.posY;
  double z1 = vehicle.posZ - player.posZ;
  
  /**
   * vectors for a straight line
   */
  double x2 = x1 - 20 * Trig.sinDegrees(vehicle.rotationYaw + partialTick*vehicle.moveHelper.strafeMotion);
  double y2 = y1;
  double z2 = z1 - 20 * Trig.cosDegrees(vehicle.rotationYaw + partialTick*vehicle.moveHelper.strafeMotion);
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
  double speed = vehicle.localLaunchPower * 0.05d;
  double angle = 90 - vehicle.localTurretPitch - vehicle.moveHelper.airPitch;
  double yaw = vehicle.localTurretRotation + partialTick * vehicle.moveHelper.strafeMotion;
  
  double vH = -Trig.sinDegrees((float) angle)*speed;
  double vY = Trig.cosDegrees((float) angle)*speed ;
  double vX = Trig.sinDegrees((float) yaw)*vH ;
  double vZ = Trig.cosDegrees((float) yaw)*vH ;
  int rocketBurnTime = (int) (speed * 20.f*AmmoHwachaRocket.burnTimeFactor);
  
  if(vehicle.vehicleType.getMovementType()==VehicleMovementType.AIR1 || vehicle.vehicleType.getMovementType()==VehicleMovementType.AIR2)
    {
    vY +=vehicle.motionY;
    vX +=vehicle.motionX;
    vZ +=vehicle.motionZ;
    y1 = -player.posY;
    }
  
  float xAcc = (float) (vX/speed) * AmmoHwachaRocket.accelerationFactor;;
  float yAcc = (float) (vY/speed) * AmmoHwachaRocket.accelerationFactor;;
  float zAcc = (float) (vZ/speed) * AmmoHwachaRocket.accelerationFactor;;
  vX = xAcc;
  vY = yAcc;
  vZ = zAcc;
   
  while(y2>=y1)
    {
    GL11.glVertex3d(x2, y2, z2);   
    x2+=vX;
    z2+=vZ;
    y2+=vY;  
    if(rocketBurnTime>0)
      {      
      rocketBurnTime--;
      vX += xAcc;
      vY += yAcc;
      vZ += zAcc;
      }
    else
      {
      vY -= gravity;
      }    
    GL11.glVertex3d(x2, y2, z2);
    }
  GL11.glEnd();
  
  GL11.glPopMatrix();
  GL11.glDepthMask(true);  
  GL11.glDisable(GL11.GL_BLEND);
  GL11.glEnable(GL11.GL_TEXTURE_2D);
  }

public static void renderBatteringRamOverlay(VehicleBase vehicle, EntityPlayer player, float partialTick)
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
  
  /**
   * vectors for a straight line
   */
  double x2 = x1 - 20 * Trig.sinDegrees(vehicle.rotationYaw);
  double y2 = y1;
  double z2 = z1 - 20 * Trig.cosDegrees(vehicle.rotationYaw);
  GL11.glLineWidth(3f);
  GL11.glBegin(GL11.GL_LINES);    
  GL11.glVertex3d(x1, y1+0.12d, z1);
  GL11.glVertex3d(x2, y2+0.12d, z2);  
  GL11.glEnd();
  
  
  
  
 
  
  Pos3f offset = vehicle.getMissileOffset();
  x2 = x1+offset.x;
  y2 = y1+offset.y;
  z2 = z1+offset.z;  
  float bx = (float) (vehicle.posX + offset.x);
  float by = (float) (vehicle.posY + offset.y);
  float bz = (float) (vehicle.posZ + offset.z);
  BlockPosition blockHit = new BlockPosition(bx, by, bz);  
  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(blockHit.x-1, blockHit.y, blockHit.z, blockHit.x+2, blockHit.y+1, blockHit.z+1);
  bb = AWRenderHelper.instance().adjustBBForPlayerPos(bb, player, partialTick);
  BoundingBoxRender.drawOutlinedBoundingBox(bb, 1.f, 0.2f, 0.2f);  
  bb = resetBoundingBox(bb, blockHit.x, blockHit.y, blockHit.z-1, blockHit.x+1, blockHit.y+1, blockHit.z+2);
  bb = AWRenderHelper.instance().adjustBBForPlayerPos(bb, player, partialTick);
  BoundingBoxRender.drawOutlinedBoundingBox(bb, 1.f, 0.2f, 0.2f);
  bb = resetBoundingBox(bb, blockHit.x, blockHit.y-1, blockHit.z, blockHit.x+1, blockHit.y+2, blockHit.z+1);
  bb = AWRenderHelper.instance().adjustBBForPlayerPos(bb, player, partialTick);
  BoundingBoxRender.drawOutlinedBoundingBox(bb, 1.f, 0.2f, 0.2f);      
  GL11.glPopMatrix();

  GL11.glDepthMask(true);
  
  GL11.glDisable(GL11.GL_BLEND);
  GL11.glEnable(GL11.GL_TEXTURE_2D);
  }

public static AxisAlignedBB resetBoundingBox(AxisAlignedBB bb, double x1, double y1, double z1, double x2, double y2, double z2)
  {
  bb.minX = x1;
  bb.maxX = x2;
  bb.minY = y1;
  bb.maxY = y2;
  bb.minZ = z1;
  bb.maxZ = z2;
  return bb;
  }

public static void renderNormalVehicleOverlay(VehicleBase vehicle, EntityPlayer player, float partialTick)
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
  
  /**
   * vectors for a straight line
   */
  double x2 = x1 - 20 * Trig.sinDegrees(vehicle.rotationYaw + partialTick*vehicle.moveHelper.strafeMotion);
  double y2 = y1;
  double z2 = z1 - 20 * Trig.cosDegrees(vehicle.rotationYaw + partialTick*vehicle.moveHelper.strafeMotion);
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
  double speed = vehicle.localLaunchPower * 0.05d;
  double angle = 90 - vehicle.localTurretPitch - vehicle.moveHelper.airPitch;;
  double yaw = vehicle.localTurretRotation + partialTick * vehicle.moveHelper.strafeMotion;
  
  double vH = -Trig.sinDegrees((float) angle)*speed;
  double vY = Trig.cosDegrees((float) angle)*speed ;
  double vX = Trig.sinDegrees((float) yaw)*vH ;
  double vZ = Trig.cosDegrees((float) yaw)*vH ;
    
  if(vehicle.vehicleType.getMovementType()==VehicleMovementType.AIR1 || vehicle.vehicleType.getMovementType()==VehicleMovementType.AIR2)
    {
    vY +=vehicle.motionY;
    vX +=vehicle.motionX;
    vZ +=vehicle.motionZ;
    y1 = -player.posY;
    }
  while(y2>=y1)
    {
    GL11.glVertex3d(x2, y2, z2);   
    x2+=vX;
    z2+=vZ;
    y2+=vY;  
    vY -= gravity;
    GL11.glVertex3d(x2, y2, z2);
    }
  GL11.glEnd();
  
  GL11.glPopMatrix();

  GL11.glDepthMask(true);
  
  GL11.glDisable(GL11.GL_BLEND);
  GL11.glEnable(GL11.GL_TEXTURE_2D);
  }

}
