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
package shadowmage.ancient_warfare.common.vehicles.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.missiles.MissileBase;
import shadowmage.ancient_warfare.common.network.Packet02Vehicle;
import shadowmage.ancient_warfare.common.registry.entry.VehicleAmmoEntry;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class VehicleAmmoHelper  implements INBTTaggable
{

private VehicleBase vehicle;

public int currentAmmoType = 0;

private List<VehicleAmmoEntry> ammoEntries = new ArrayList<VehicleAmmoEntry>();
private HashMap<Integer, VehicleAmmoEntry> ammoTypes = new HashMap<Integer, VehicleAmmoEntry>();//local ammo type to global entry

public VehicleAmmoHelper(VehicleBase vehicle)
  {
  this.vehicle = vehicle;
  }

/**
 * SERVER ONLY relays changes to clients to update a single ammo type, also handles updating underlying inventory...
 * @param num
 */
public void decreaseCurrentAmmo(int num)
  {
  if(vehicle.worldObj.isRemote)
    {
    return;
    }
  if(currentAmmoType>=0 && currentAmmoType<this.ammoEntries.size())
    {    
    num = num - vehicle.inventory.ammoInventory.decreaseCountOf(ItemLoader.ammoItem.itemID, this.getCurrentAmmoType().getAmmoType(), num);
    VehicleAmmoEntry entry = this.ammoEntries.get(this.currentAmmoType);
    int origCount = entry.ammoCount;    
    entry.ammoCount -= num;
    if(entry.ammoCount<0)
      {
      entry.ammoCount = 0;
      }
    if(entry.ammoCount!=origCount)
      {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setInteger("num", this.currentAmmoType);
      tag.setInteger("cnt", entry.ammoCount);    
      Packet02Vehicle pkt = new Packet02Vehicle();
      pkt.setParams(vehicle);
      pkt.setAmmoUpdate(tag);
      pkt.sendPacketToAllTrackingClients(vehicle);
      }    
    }
  }

public int getCurrentAmmoCount()
  {
  if(this.ammoEntries.size()>0 && this.currentAmmoType < this.ammoEntries.size())
    {
    return this.ammoEntries.get(currentAmmoType).ammoCount;
    }
  return 0;
  }

public void addUseableAmmo(IAmmoType ammo)
  {
  VehicleAmmoEntry ent = new VehicleAmmoEntry(ammo);
  this.ammoEntries.add(ent);
  this.ammoTypes.put(ammo.getAmmoType(), ent);
  }

public void handleAmmoSelectInput(int delta)
  {
  Config.logDebug("handling ammo selection INPUT. server: "+!vehicle.worldObj.isRemote+" delta: "+delta);
  if(this.ammoEntries.size()>0)
    {
    boolean updated = false;
    int test = this.currentAmmoType + delta;
    if(test<0)
      {
      while(test<0)
        {
        test += this.ammoEntries.size();
        }    
      }
    else if(test>=this.ammoEntries.size())
      {
      while(test>=this.ammoEntries.size())
        {
        test -= this.ammoEntries.size();
        }
      }
    if(test>=0 && test <this.ammoEntries.size() && test !=this.currentAmmoType)
      {
      NBTTagCompound innerTag = new NBTTagCompound();
      innerTag.setInteger("num", test);    
      Packet02Vehicle pkt = new Packet02Vehicle();
      pkt.setParams(vehicle);
      pkt.setAmmoSelect(innerTag);
      pkt.sendPacketToServer();
      }   
    }
  }

public void handleAmmoSelectPacket(NBTTagCompound tag)
  {  
  int num = tag.getInteger("num");
  Config.logDebug("handling ammo selection packet. server: "+!vehicle.worldObj.isRemote+" ammoNum: "+num);
  if(num>=0 && num < this.ammoEntries.size() && num != this.currentAmmoType)
    {
    this.currentAmmoType = num;
    if(!vehicle.worldObj.isRemote)
      {    
      NBTTagCompound innerTag = new NBTTagCompound();
      innerTag.setInteger("num", num);    
      Packet02Vehicle pkt = new Packet02Vehicle();
      pkt.setParams(vehicle);
      pkt.setAmmoSelect(innerTag);
      pkt.sendPacketToAllTrackingClients(vehicle);
      }
    if(!vehicle.canAimPower())
      {
      vehicle.launchPowerCurrent = vehicle.firingHelper.getAdjustedMaxMissileVelocity();
      }
    } 
  }

/**
 * sent to clients when ammo is used from firing...
 * CLIENT ONLY
 * @param tag
 */
public void handleAmmoCountUpdate(NBTTagCompound tag)
  {
  Config.logDebug("updating single ammo type");
  int num = tag.getInteger("num");
  if(num>=0 && num < this.ammoEntries.size())
    {
    int count = tag.getInteger("cnt");
    this.ammoEntries.get(num).ammoCount = count;
    }
  }

/**
 * SERVER ONLY....
 */
public void updateAmmoCounts()
  {
  if(vehicle.worldObj.isRemote)
    {
    return;
    }
  Config.logDebug("counting ammos!!");
  for(VehicleAmmoEntry ent : this.ammoEntries)
    {
    ent.ammoCount = 0;
    }
  List<VehicleAmmoEntry> counts = vehicle.inventory.getAmmoCounts();
  
  NBTTagCompound tag = new NBTTagCompound();
  NBTTagList tagList = new NBTTagList();
  for(VehicleAmmoEntry count : counts)
    {
    NBTTagCompound entryTag = new NBTTagCompound();
    for(VehicleAmmoEntry ent : this.ammoEntries)
      {
      if(ent.baseAmmoType == count.baseAmmoType)
        {
        ent.ammoCount = count.ammoCount;
        }
      }
    entryTag.setInteger("type", count.baseAmmoType.getAmmoType());
    entryTag.setInteger("count", count.ammoCount);
    tagList.appendTag(entryTag);
    }  
  tag.setTag("list", tagList);
  Packet02Vehicle pkt = new Packet02Vehicle();
  pkt.setParams(vehicle);
  pkt.setAmmoData(tag);
  pkt.sendPacketToAllTrackingClients(vehicle);
  }

/**
 * CLIENT ONLY--version of above
 * @param tag
 */
public void handleAmmoUpdatePacket(NBTTagCompound tag)
  {
  Config.logDebug("receiving ammo count client update");
  for(VehicleAmmoEntry ent : this.ammoEntries)
    {
    ent.ammoCount = 0;
    }
  NBTTagList tagList = tag.getTagList("list");
  for(int i = 0; i <tagList.tagCount(); i++)
    {
    NBTTagCompound entryTag = (NBTTagCompound) tagList.tagAt(i);
    int type = entryTag.getInteger("type");
    int count = entryTag.getInteger("count");
    for(VehicleAmmoEntry ent : this.ammoEntries)
      {
      if(ent.baseAmmoType.getAmmoType()==type)
        {
        ent.ammoCount = count;
        break;
        }
      }
    }
  }

public IAmmoType getCurrentAmmoType()
  {  
  if(currentAmmoType<this.ammoEntries.size() && currentAmmoType>=0)
    {
    VehicleAmmoEntry entry = this.ammoEntries.get(currentAmmoType);
    if(entry!=null)
      {
      return entry.baseAmmoType;
      }
    }
  return null;
  }

public MissileBase getMissile(float x, float y, float z, float mx, float my, float mz)
  {
  IAmmoType ammo = this.getCurrentAmmoType();
  if(ammo!=null)
    {
    MissileBase missile = new MissileBase(vehicle.worldObj);   
    missile.setMissileParams(ammo, x, y, z, mx, my, mz);
    missile.setMissileCallback(vehicle);    
    return missile;
    }
  return null;  
  }

public MissileBase getMissile2(float x, float y, float z, float yaw, float pitch, float velocity)
  {
  IAmmoType ammo = this.getCurrentAmmoType();
  if(ammo!=null)
    {
    MissileBase missile = new MissileBase(vehicle.worldObj);   
    missile.setMissileParams2(ammo, x, y, z, yaw, pitch, velocity);
    missile.setMissileCallback(vehicle);
    return missile;
    }
  return null;
  }

@Override
public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  
  tag.setInteger("am", currentAmmoType);
  
  NBTTagList tagList = new NBTTagList();
  for(VehicleAmmoEntry ent : this.ammoEntries)
    {
    NBTTagCompound entryTag = new NBTTagCompound();
    entryTag.setInteger("num", ent.baseAmmoType.getAmmoType());
    entryTag.setInteger("cnt", ent.ammoCount);
    Config.logDebug("writing ammo count "+entryTag.getInteger("num")+","+entryTag.getInteger("cnt"));
    tagList.appendTag(entryTag);
    }  
  tag.setTag("list", tagList);
  return tag;
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {
  for(VehicleAmmoEntry ent : this.ammoEntries)
    {
    ent.ammoCount = 0;
    } 
  this.currentAmmoType = tag.getInteger("am");
  NBTTagList tagList = tag.getTagList("list");
  for(int i = 0; i < tagList.tagCount(); i++)
    {
    NBTTagCompound entryTag = (NBTTagCompound) tagList.tagAt(i);
    int num = entryTag.getInteger("num");
    int cnt = entryTag.getInteger("cnt");    
    if(this.ammoTypes.containsKey(num))
      {
      Config.logDebug("reading ammo count "+num+","+cnt);
      this.ammoTypes.get(num).ammoCount = cnt;
      }
    }
  }

}
