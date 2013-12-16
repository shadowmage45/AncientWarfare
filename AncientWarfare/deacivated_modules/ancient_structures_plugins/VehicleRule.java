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
package shadowmage.ancient_structures.common.structures.data.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.config.AWLog;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.structures.data.ProcessedStructure;
import shadowmage.ancient_structures.common.structures.data.ScannedEntityEntry;

public class VehicleRule
{

int bx;
int by;
int bz;
float ox;
float oy;
float oz;
float rotation;
float pitch;
byte teamNum;
private short vehicleType;


public short vehicleRank;
public byte[] armorTypes;
public byte[] upgradeTypes;
public byte[] ammoTypes;

private VehicleRule()
  {  
  }

public static VehicleRule populateRule(ScannedEntityEntry entry, VehicleBase vehicle)
  {
  VehicleRule rule = new VehicleRule();
  rule.bx = entry.bx;
  rule.by = entry.by;
  rule.bz = entry.bz;
  rule.ox = entry.ox;
  rule.oy = entry.oy;
  rule.oz = entry.oz;
  rule.rotation = entry.r;
  rule.pitch = entry.p;
  rule.teamNum = (byte) vehicle.teamNum;
  rule.setVehicleType((short) vehicle.vehicleType.getGlobalVehicleType());
  rule.vehicleRank = (short) vehicle.vehicleMaterialLevel;
  rule.armorTypes = new byte[vehicle.inventory.armorInventory.getSizeInventory()];
  Arrays.fill(rule.armorTypes, (byte)-1);
  rule.upgradeTypes = new byte[vehicle.inventory.upgradeInventory.getSizeInventory()];
  Arrays.fill(rule.upgradeTypes, (byte)-1);
  rule.ammoTypes = new byte[vehicle.inventory.ammoInventory.getSizeInventory()];
  Arrays.fill(rule.ammoTypes, (byte)-1);
  ItemStack stack;
  IVehicleArmorType armor;
  IVehicleUpgradeType upgrade;
  IAmmoType ammo;
  int i;
  for(i = 0; i < vehicle.inventory.armorInventory.getSizeInventory(); i++)
    {
    if(i<rule.armorTypes.length)
      {
      stack = vehicle.inventory.armorInventory.getStackInSlot(i);
      armor = ArmorRegistry.instance().getArmorForStack(stack);
      if(armor!=null)
        {
        rule.armorTypes[i] = (byte) armor.getGlobalArmorType();//(byte) vehicle.upgradeHelper.getLocalArmorType(armor);
        }
      }
    }
  for(i = 0; i < vehicle.inventory.upgradeInventory.getSizeInventory(); i++)
    {
    if(i<rule.upgradeTypes.length)
      {
      stack = vehicle.inventory.upgradeInventory.getStackInSlot(i);
      upgrade = VehicleUpgradeRegistry.instance().getUpgrade(stack);
      if(upgrade!=null)
        {
        rule.upgradeTypes[i] = (byte) upgrade.getUpgradeGlobalTypeNum();//vehicle.upgradeHelper.getLocalUpgradeType(upgrade);
        }
      }
    }
  for(i = 0; i < vehicle.inventory.ammoInventory.getSizeInventory(); i++)
    {
    if(i < rule.ammoTypes.length)
      {
      stack = vehicle.inventory.ammoInventory.getStackInSlot(i);
      ammo = AmmoRegistry.instance().getAmmoForStack(stack);
      if(ammo!=null)
        {      
        rule.ammoTypes[i] = (byte)ammo.getAmmoType();
        }
      }   
    }
  return rule;
  }

public static VehicleRule parseRule(List<String> ruleLines)
  {
  String line;
  VehicleRule rule = new VehicleRule();
  Iterator<String> it = ruleLines.iterator();
  while(it.hasNext())
    {
    line = it.next();   
    if(line.toLowerCase().startsWith("type"))
      {
      rule.setVehicleType(StringTools.safeParseShort("=", line));
      }
    else if(line.toLowerCase().startsWith("rank"))
      {
      rule.vehicleRank = StringTools.safeParseShort("=", line);     
      }
    else if(line.toLowerCase().startsWith("team"))
      {
      rule.teamNum = StringTools.safeParseByte("=", line);
      }
    else if(line.toLowerCase().startsWith("armors"))
      {
      rule.armorTypes = StringTools.safeParseByteArray("=", line);      
      }    
    else if(line.toLowerCase().startsWith("upgrades"))
      {
      rule.upgradeTypes = StringTools.safeParseByteArray("=", line);
      }   
    else if(line.toLowerCase().startsWith("ammos"))
      {
      rule.ammoTypes = StringTools.safeParseByteArray("=", line);      
      }  
    else if(line.toLowerCase().startsWith("bx"))
      {
      rule.bx = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("by"))
      {
      rule.by = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("bz"))
      {
      rule.bz = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("ox"))
      {
      rule.ox = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("oy"))
      {
      rule.oy = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("oz"))
      {
      rule.oz = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("rotation"))
      {
      rule.rotation = StringTools.safeParseFloat("=", line);
      }
    else if(line.toLowerCase().startsWith("pitch"))
      {
      rule.pitch = StringTools.safeParseFloat("=", line);
      }
    } 
  return rule;
  }

public VehicleBase getVehicleToSpawn(World world, int facing, ProcessedStructure struct, BlockPosition buildPos,  int teamOverride)
  {  
  int team = teamOverride >= 0? teamOverride : teamNum;
  VehicleBase vehicle = getVehicleForRule(world, team, getVehicleType(), vehicleRank, armorTypes, ammoTypes, upgradeTypes);
  if(vehicle==null)
    {
    return null;
    }
  int rotAmt = BlockTools.getRotationAmt(facing);
  BlockPosition target = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(bx-struct.xOffset,by-struct.verticalOffset, bz-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  float ax = target.x;
  float ay = target.y;
  float az = target.z;    
  float ar = rotation - 90*rotAmt;   
  
  ax+= getRotatedXOffset(ox, oz, facing);
  az+= getRotatedZOffset(ox, oz, facing); 
  vehicle.setLocationAndAngles(ax, ay, az, ar, pitch);
  vehicle.prevPosX = ax;
  vehicle.prevPosY = ay;
  vehicle.prevPosZ = az;
  vehicle.prevRotationYaw = vehicle.rotationYaw = ar;
  return vehicle;
  }

protected float getRotatedXOffset(float xOff, float zOff, int face)
  {
  switch(face)
  {
  case 0:
  return 1-xOff;
  case 1:
  return zOff;
  case 2:
  return xOff;
  case 3:
  return 1-zOff;
  }  
  return xOff;
  }

protected float getRotatedZOffset(float xOff, float zOff, int face)
  {
  switch(face)
  {
  case 0:
  return 1-zOff;
  case 1:
  return xOff;
  case 2:
  return zOff;
  case 3:
  return 1-xOff;
  }  
  return zOff;
  }

private VehicleBase getVehicleForRule(World world, int teamNum, int vehicleType, int vehicleRank, byte[] armorTypes, byte[] ammoTypes, byte[] upgradeTypes)
  {
  VehicleBase vehicle = VehicleType.getVehicleForType(world, vehicleType, vehicleRank);
  if(vehicle==null)
    {
    String name = String.valueOf(vehicleType);
    if(VehicleType.getVehicleType(vehicleType)!=null)
      {
      name = VehicleType.getVehicleType(vehicleType).getDisplayName();
      }
    AWLog.logError("Attempt to get invalid vehicle for type: "+name+". It might be disabled via config, please update your templates.");
    return null;
    }
  vehicle.teamNum = teamNum;  
  byte type = (byte) 0;
  int i;
  IAmmoType ammo;
  IVehicleUpgradeType upgrade;
  IVehicleArmorType armor;
  for(i = 0; i < armorTypes.length; i++)
    {
    if(i<vehicle.inventory.armorInventory.getSizeInventory())
      {
      type = (byte) (armorTypes[i]);
      if(type>-1)
        {
        armor = ArmorRegistry.instance().getArmorType(type);//vehicle.upgradeHelper.getArmorFromLocal(type);
        if(vehicle.vehicleType.isArmorValid(armor))
          {
          vehicle.inventory.armorInventory.setInventorySlotContents(i, armor.getArmorStack(1));
          }
        }
      }
    }
  for(i = 0; i < upgradeTypes.length; i++)
    {
    if(i < vehicle.inventory.upgradeInventory.getSizeInventory())
      {
      type = (byte)(upgradeTypes[i]);      
      if(type>=0)
        {
        upgrade = VehicleUpgradeRegistry.instance().getUpgrade(type);//vehicle.upgradeHelper.getUpgradeFromLocal(type);
        if(vehicle.vehicleType.isUpgradeValid(upgrade))
          {
          vehicle.inventory.upgradeInventory.setInventorySlotContents(i, upgrade.getUpgradeStack(1));
          }
        }
      }
    }
  for(i = 0; i < ammoTypes.length; i++)
    {
    if(i< vehicle.inventory.ammoInventory.getSizeInventory())
      {
      type = (byte) ammoTypes[i];      
      ammo = AmmoRegistry.instance().getAmmoEntry(type);
      if(ammo!=null && vehicle.vehicleType.isAmmoValidForInventory(ammo))
        {
        vehicle.inventory.ammoInventory.setInventorySlotContents(i, ammo.getAmmoStack(64));
        }
      }
    }
  return vehicle;
  }

public List<String> getRuleLines()
  {
  ArrayList<String> lines = new ArrayList<String>();
  lines.add("vehicle:");
  lines.add("type="+this.getVehicleType());
  lines.add("rank="+this.vehicleRank);
  lines.add("team="+this.teamNum);
  lines.add("ammos="+StringTools.getCSVStringForArray(ammoTypes));
  lines.add("upgrades="+StringTools.getCSVStringForArray(upgradeTypes));
  lines.add("armors="+StringTools.getCSVStringForArray(armorTypes));
  lines.add("bx="+bx);
  lines.add("by="+by);
  lines.add("bz="+bz);
  lines.add("ox="+ox);
  lines.add("oy="+oy);
  lines.add("oz="+oz);
  lines.add("rotation="+rotation);
  lines.add("pitch="+pitch);
  lines.add(":endvehicle");
  return lines;
  }

/**
 * @return the vehicleType
 */
public short getVehicleType()
  {
  return vehicleType;
  }

/**
 * @param vehicleType the vehicleType to set
 */
public void setVehicleType(short vehicleType)
  {
  this.vehicleType = vehicleType;
  }
}
