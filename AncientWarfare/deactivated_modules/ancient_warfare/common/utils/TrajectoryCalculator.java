package shadowmage.ancient_warfare.common.utils;

import net.minecraft.util.MathHelper;
import shadowmage.ancient_framework.common.utils.Pair;
import shadowmage.ancient_framework.common.utils.Trig;
import shadowmage.ancient_warfare.common.vehicles.missiles.AmmoHwachaRocket;

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

public class TrajectoryCalculator
{
/**
 * 
 * @param x input hit x (horizontal distance)
 * @param y input hit y (vertical distance)
 * @param v velocity per second
 * @param g gravity per second
 * @return
 */
public static Pair<Float, Float> getLaunchAngleToHit(float x, float y, float v)
  {
  float v2 = v*v;
  float v4 = v*v*v*v;
  float x2 = x*x;  
  float sqRtVal = MathHelper.sqrt_float(v4 - Trig.GRAVITY * (Trig.GRAVITY*x2 + 2*y*v2));  
  float h = v2 +sqRtVal;
  float l = v2 -sqRtVal;  
  h /= Trig.GRAVITY*x;
  l /= Trig.GRAVITY*x;  
  h = Trig.toDegrees((float) Math.atan(h));
  l = Trig.toDegrees((float) Math.atan(l));  
  return new Pair<Float, Float>(h, l);  
  }

/**
 * 
 * @param x raw X distance (x2 - x1)
 * @param y vertical distance (y2 - y1)
 * @param z raw Z distance (z2 - z1)
 * @param v initial launch velocity per second
 * @param g gravity per second acceleration
 * @return
 */
public static Pair<Float, Float> getLaunchAngleToHit(float x, float y, float z, float v)
  {
  return getLaunchAngleToHit(MathHelper.sqrt_float(x*x+z*z), y, v);
  }

public static float iterativeSpeedFinder(float x, float y, float z, float angle, int maxIterations, boolean rocket)
  {
  return bruteForceSpeedFinder(MathHelper.sqrt_float(x*x+z*z), y, angle, maxIterations, rocket);
  }

public static float bruteForceRocketFinder(float x, float y, float angle, int maxIterations)
  {
  float bestVelocity = 0.f;
  float velocityIncrement = 5.29f;
  float testVelocity = 1.f;
  float gravityTick = 9.81f *0.05f*0.05f;
  int rocketBurnTime = 0;
  float posX = 0;
  float posY = 0;
  float motX = 0;
  float motY = 0;
  float motX0 = 0;
  float motY0 = 0;
  float hitX = 0;
  float hitY = 0;
  boolean hitGround = true;
  float hitDiffX;
  float hitDiffY;            
  float hitPercent;
//  maxIterations *= 4;
  for(int iter = 0; iter < maxIterations; iter++)
    {
    //reset pos
    //calc initial motion from input angle and current testVelocity
    hitGround = true;
    posX = 0.f;
    posY = 0.f;
    motX = Trig.sinDegrees(angle)*testVelocity*0.05f;
    motY = Trig.cosDegrees(angle)*testVelocity*0.05f;
        
    rocketBurnTime = (int) (testVelocity * AmmoHwachaRocket.burnTimeFactor);     
    motX0 = (motX/ (testVelocity*0.05f)) * AmmoHwachaRocket.accelerationFactor;
    motY0 = (motY/ (testVelocity*0.05f)) * AmmoHwachaRocket.accelerationFactor;
    motX = motX0;
    motY = motY0;
    while(motY>=0 || posY >= y)
      {
      
      //move
      //check hit
      //apply gravity if not hit
      posX+=motX;
      posY+=motY;    
      if(rocketBurnTime >0)
        {
        rocketBurnTime--;
        motX+= motX0;
        motY+= motY0;
        }
      else
        {
        motY-=gravityTick;
        }
      if(posX>x)
        {
        hitGround = false;
        break;//missile went too far
        }  
      }    
    if(hitGround)//if break was triggered by going negative on y axis, get a more precise hit vector
      {
      motY+=gravityTick;
      hitDiffX = motX - posX;
      hitDiffY = motY - posY;            
      hitPercent = (y - posY) / hitDiffY;
      hitX = posX + hitDiffX * hitPercent;
      hitY = posY + + hitDiffY * hitPercent;
      } 
    if(hitGround && hitX < x)// hit was not far enough, increase power
      {
      bestVelocity = testVelocity;
      testVelocity += velocityIncrement;        
      }
    else if(posX<x)//
      {
      bestVelocity = testVelocity;
      testVelocity += velocityIncrement;
      }
    else//it was too far, go back to previous power, decrease increment, increase by new increment
      {
      bestVelocity = testVelocity;
      testVelocity -= velocityIncrement;
      velocityIncrement*=0.5f;
      testVelocity+=velocityIncrement;      
      } 
    }
    
  return bestVelocity;  
  }

public static float bruteForceSpeedFinder(float x, float y, float angle, int maxIterations, boolean rocket)
  {  
  angle = 90-angle;
  if(rocket)
    {
    return bruteForceRocketFinder(x, y, angle, maxIterations);
    }
  float bestVelocity = 0.f;
  float velocityIncrement = 10.f;
  float testVelocity = 10.f;
  float gravityTick = 9.81f *0.05f*0.05f;
  float posX = 0;
  float posY = 0;
  float motX = 0;
  float motY = 0;
  float motX0 = 0;
  float motY0 = 0;
  float hitX = 0;
  float hitY = 0;
  boolean hitGround = true;
  float hitDiffX;
  float hitDiffY;            
  float hitPercent;
  for(int iter = 0; iter < maxIterations; iter++)
    {
    //reset pos
    //calc initial motion from input angle and current testVelocity
    hitGround = true;
    posX = 0.f;
    posY = 0.f;
    motX = Trig.sinDegrees(angle)*testVelocity*0.05f;
    motY = Trig.cosDegrees(angle)*testVelocity*0.05f;   
    while(motY>=0 || posY >= y)
      {
      //move
      //check hit
      //apply gravity if not hit
      posX+=motX;
      posY+=motY;
      if(posX>x)
        {
        hitGround = false;
        break;//missile went too far
        }     
      motY-=gravityTick;        
      }    
    if(hitGround)//if break was triggered by going negative on y axis, get a more precise hit vector
      {
      motY+=gravityTick;
      hitDiffX = motX - posX;
      hitDiffY = motY - posY;            
      hitPercent = (y - posY) / hitDiffY;
      hitX = posX + hitDiffX * hitPercent;
      hitY = posY + + hitDiffY * hitPercent;
      } 
    if(hitGround && hitX < x)// hit was not far enough, increase power
      {
      bestVelocity = testVelocity;
      testVelocity += velocityIncrement;        
      }
    else//it was too far, go back to previous power, decrease increment, increase by new increment
      {
      testVelocity -= velocityIncrement;
      bestVelocity = testVelocity;
      velocityIncrement *= 0.5f;
      testVelocity +=velocityIncrement;
      } 
    } 
  return bestVelocity;
  }

public static float getEffectiveRange(float y, float angle, float velocity, int maxIterations, boolean rocket)
  {
  float motX = Trig.sinDegrees(angle)*velocity*0.05f;
  float motY = Trig.cosDegrees(angle)*velocity*0.05f;
  float rocketX = 0;
  float rocketY = 0;
  if(rocket)
    {
    int rocketBurnTime = (int) (velocity*AmmoHwachaRocket.burnTimeFactor);  
    float motX0 = (motX/ (velocity*0.05f)) * AmmoHwachaRocket.accelerationFactor;
    float motY0 = (motY/ (velocity*0.05f)) * AmmoHwachaRocket.accelerationFactor;
    motX = motX0;
    motY = motY0;
    while(rocketBurnTime>0)
      {
      rocketX += motX;
      rocketY += motY;
      rocketBurnTime--;
      motX+= motX0;
      motY+= motY0;
      }
    y-=rocketY;
    }
  motX *= 20.f;
  motY *= 20.f;
  float gravity = 9.81f;  
  float t = motY/gravity;  
  float tQ = MathHelper.sqrt_float( ((motY*motY) / (gravity*gravity)) - ((2*y)/gravity));
  float tPlus = t + tQ;
  float tMinus = t - tQ; 
  t = tPlus > tMinus? tPlus : tMinus;
  return (motX * t) + rocketX;
  }

}
