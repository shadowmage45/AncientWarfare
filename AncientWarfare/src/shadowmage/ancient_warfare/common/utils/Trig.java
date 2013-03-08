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
package shadowmage.ancient_warfare.common.utils;

import shadowmage.ancient_warfare.common.config.Config;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * because I hate it so much...why not make the 
 * computer do it all for me?
 * @author Shadowmage
 *
 */
public class Trig
{
private static final float PI = 3.141592653589793f;
private static final float TORADIANS = PI / 180.f;
private static final float TODEGREES = 180.f / PI;
private static final float GRAVITY = 9.81f;

public static int getPower(int num, int exp)
  {
  return Double.valueOf(Math.floor(Math.pow(num, exp))).intValue();
  }

public static float toRadians(float degrees)
  {
  return degrees * TORADIANS;
  }

public static float toDegrees(float radians)
  {
  return radians * TODEGREES;
  }

public static float cosDegrees(float degrees)
  {   
  return MathHelper.cos(degrees * TORADIANS);
  }

public static float sinDegrees(float degrees)
  {  
  return MathHelper.sin(degrees * TORADIANS);
  }

public static float cos(float radians)
  {
  return MathHelper.cos(radians);
  }

public static float sin(float radians)
  {
  return MathHelper.sin(radians);
  }

public static double wrapTo360(double in)
  {
  while(in>=360)
    {
    in-=360;
    }
  while(in<0)
    {
    in+=360;
    }
  return in;
  }

public static float wrapTo360(float in)
  {
  while(in>=360.f)
    {
    in-=360.f;
    }
  while(in<0)
    {
    in+=360.f;
    }
  return in;  
  }

public static int getAbsDiff(int a, int b)
  {
  if(a<b)
    {
    return b-a;
    }
  return a-b;
  }

public static float getAbsDiff(float a, float b)
  {
  if(a<b)
    {
    return b-a;
    }
  return a-b;
  }

public static double getAbsDiff(double a, double b)
  {
  if(a<b)
    {
    return b-a;
    }
  return a-b;
  }

/**
 * will return a NEW Pos3f containing the translated coordinate
 * ONLY translates on the x/z axis'
 * @param pos
 * @param originYaw
 * @param transYaw
 * @param transLength
 * @return a NEW vector (old is untouched) with the new position
 */
public static Pos3f translatePos(Pos3f pos, float originYaw, float transYaw, float transLength)
  {
  float newX = pos.x + sinDegrees(-originYaw -90 - transYaw) * transLength;
  float newZ = pos.z + cosDegrees(-originYaw -90 - transYaw) * transLength;
  return new Pos3f(newX, pos.y, newZ);
  }

/**
 * translates a position given a pitch and yaw
 * (to translate relative, add current pitch + pitchChange before calling, same for yaw)
 * @param pos
 * @param yaw
 * @param pitch
 * @param transLength
 * @return a NEW vector (old is untouched) with the new position
 */
public static Pos3f translatePosPitch(Pos3f pos, float yaw, float pitch, float transLength)
  {
  float newX = pos.x + sinDegrees(yaw) * sinDegrees(pitch) * transLength;
  float newY = pos.y + cosDegrees(pitch)  * transLength;
  float newZ = pos.z + cosDegrees(yaw) * sinDegrees(pitch) * transLength;  
  return new Pos3f(newX,newY,newZ);
  }

/**
 * 
 * @param test
 * @param target
 * @return
 */
public static float getHorizontalDistance(Entity test, Entity target)
  {  
  double x = test.posX>target.posX? test.posX-target.posX : target.posX-test.posX;
  double z = test.posZ>target.posZ? test.posZ-target.posZ : target.posZ-test.posZ;
  return MathHelper.sqrt_double(z*z+x*x);
  }

public static float getHeightDifference(Entity test, Entity target)
  {
  return (float)test.posY - (float)target.posY;
  }

/**
 * returns the sqrt velocity of the input vectors (asuming base zero)
 * @param x
 * @param y
 * @param z
 * @return
 */
public static float getVelocity(float x, float y, float z)
  {
  return MathHelper.sqrt_float(x*x + y*y + z*z);
  }

public static float getVelocity(double x, double y, double z)
  {
  return Trig.getVelocity((float)x,(float)y,(float)z);
  }

/**
 * get velocity of a 2d vector
 * @param x
 * @param z
 * @return
 */
public static float getVelocity(double x, double z)
  {
  return MathHelper.sqrt_float((float)(x*x + z*z));
  }

/**
 * returns a normalized vector from yaw and pitch
 */
public static Pos3f calcAngles(float yaw, float pitch)
  {
  Pos3f aim = new Pos3f();  
  aim.x = (MathHelper.cos(yaw) * MathHelper.cos(pitch));
  aim.z = (MathHelper.sin(yaw) * MathHelper.cos(pitch));
  aim.y = MathHelper.sin(pitch);
  return aim;
  }

public static float getAngle(float x, float y)
  {
  return toDegrees((float) Math.atan2(y, x));
  }

/**
 * calcs the range of the shot, no drag..
 * @param x
 * @param y
 * @param z
 * @param mx
 * @param my
 * @param mz
 * @return
 */
public static float calcTrajectoryRange3D(float mx, float my, float mz, float gravSecond)
  {
  float distance = MathHelper.sqrt_float(mx*mx + mz*mz);
  return calcTrajectoryRange2D(distance, my, gravSecond);
  }

public static float calcTrajectoryRange2D(float mx, float my, float gravSecond)
  {
  float seconds = (my * 20 * 2)/gravSecond;  
  float distance = mx * seconds;
  return distance;
  }

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
  float sqRtVal = MathHelper.sqrt_float(v4 - GRAVITY * (GRAVITY*x2 + 2*y*v2));  
  float h = v2 +sqRtVal;
  float l = v2 -sqRtVal;  
  h /= GRAVITY*x;
  l /= GRAVITY*x;  
  h = toDegrees((float) Math.atan(h));
  l = toDegrees((float) Math.atan(l));  
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

//public static float getLaunchSpeedToHit(float x, float y, float angle)
//  { 
//  /**
//   * STILL ONLY FUCKING CORRECT FOR FLAT/EVEN (ZERO VALUES OF Y)
//   */
//  float g = GRAVITY;
//  float vOff = x - y;
//  float gx2 = g * x *x;
//  float factor = gx2/vOff;
//  return (float) Math.sqrt(factor);
//  
////  double weirdFactor = 0.6158787133d;//manually found this factor to correct, it was originally +1
////  double firstHalf = Math.sqrt(g)*Math.sqrt(x)*Math.sqrt( (Math.tan(angle)*Math.tan(angle)) + weirdFactor );
////  double secondHalf = Math.sqrt(2 * Math.tan(angle)-(2*g*y)/x);
////  return (float) (firstHalf/secondHalf);
//  }
//
//public static float getLaunchSpeedToHit(float x, float y, float z, float angle)
//  {
//  return getLaunchSpeedToHit(MathHelper.sqrt_float(x*x+z*z), y, angle);
//  }

public static float iterativeSpeedFinder(float x, float y, float z, float angle, int maxIterations)
  {
  return iterativeSpeedFinder(MathHelper.sqrt_float(x*x+z*z), y, angle, maxIterations);
  }

public static float iterativeSpeedFinder(float x, float y, float angle, int maxIterations)
  {
//  Config.logDebug("/********************************************/");
//  long ts1 = System.nanoTime();
  /**
   * get a rough velocity value from an x,0 target
   */
  float g = GRAVITY;
  float vOff = x - 0;
  float gx2 = g * x *x;
  float factor = gx2/vOff;
  float firstTestVel = (float) Math.sqrt(factor);
  
  
  float testFactor = 1.f;
  float bestDiff = 999.f;
  float prevDiff = bestDiff;
  float testVelocity = firstTestVel;
  float bestVelocity = testVelocity;
  float prevVelocity = testVelocity;
  
  boolean wasPreviousNAN = false;
  boolean useUpper = y >= 0? false : true;
  int iter = 1;
  
  if(y<0)//if already over powered,  might as well start in the right direction
    {
    testFactor *= -1;
    }
  
  while(iter<=maxIterations)
    {
      
//    Config.logDebug("/********************************************/");   
//    Config.logDebug("iteration: "+iter+ "testing: "+testVelocity);
//    Config.logDebug("testFactor: "+testFactor);    
    Pair<Float, Float> angles = getLaunchAngleToHit(x, y, testVelocity);

    Float foundAngle = useUpper? angles.key() : angles.value();   
    
    if(foundAngle.isNaN())
      {
      wasPreviousNAN=true;
//      Config.logDebug("incrementing power for NAN angle");
      if(testFactor<0)
        {
        testFactor *= -1;
        }
      if(iter>1 && !wasPreviousNAN)
        {
        testFactor *= 0.5f;
        }
      testVelocity += testFactor;
      bestVelocity = testVelocity;         
//      Config.logDebug("/********************************************/");
      continue;      
      }    
    wasPreviousNAN = false;
    
    float diff = getAbsDiff(foundAngle, 45.f);      
   
    if(diff>prevDiff)//we're going in the wrong direction
      {
//      Config.logDebug("diff: "+diff+ "prevDiff: "+prevDiff);
//      Config.logDebug("bestDiff: " +bestDiff);
//      Config.logDebug("foundAngle: "+foundAngle);  
//      Config.logDebug("should invert direction!!");
      testFactor *= -1;//reverse test factor;
      testFactor *= 0.5f;//reduce test factor 
      testVelocity = prevVelocity;
      }
    else
      {       
      bestDiff = diff;
      bestVelocity = testVelocity;
//      Config.logDebug("diff: "+diff+ "prevDiff: "+prevDiff);
//      Config.logDebug("found new best: "+testVelocity);
//      Config.logDebug("bestDiff: " +bestDiff);
//      Config.logDebug("foundAngle: "+foundAngle);  
      }
    
    prevVelocity = testVelocity;
    testVelocity += testFactor;
    prevDiff = diff;      
    iter++;    
//    Config.logDebug("/********************************************/");
    }
  
//  long ts2 = System.nanoTime();
//  long diff = ts2-ts1;
//  Config.logDebug("traj calc time nanos: "+diff);
  
  testVelocity = prevVelocity;  
  return bestVelocity;
  }

}