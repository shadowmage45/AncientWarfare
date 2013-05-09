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
package shadowmage.ancient_warfare.common.npcs.helpers.targeting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.targeting.TargetType;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class AITargetEntryRepairableVehicle extends AITargetEntry
{

/**
 * @param owner
 * @param typeName
 * @param clz
 * @param priority
 * @param isEntityTarget
 * @param maxTargetRange
 */
public AITargetEntryRepairableVehicle(NpcBase owner,  float maxTargetRange)
  {
  super(owner, TargetType.REPAIR, VehicleBase.class, 0, true, maxTargetRange);
  }

@Override
public boolean isTarget(Entity ent)
  {
//  Config.logDebug("checking entity: "+ent + " for vehicle repairability");
  if(ent==null || ent.isDead)
    {
    return false;
    }
  if(ent instanceof VehicleBase)
    {
    VehicleBase v = (VehicleBase)ent;
//    Config.logDebug("found vehicle. range: "+npc.getDistanceToEntity(v) + " health: "+v.getHealth() + "/"+v.baseHealth);    
    if(v.getHealth() < v.baseHealth && !TeamTracker.instance().isHostileTowards(npc.worldObj, npc.teamNum, v.teamNum) && !TeamTracker.instance().isHostileTowards(npc.worldObj, v.teamNum, npc.teamNum))
      {
//      Config.logDebug("returning target is valid" );
//      new Exception().printStackTrace();
      return true;
      } 
    }  
//  Config.logDebug("returning invalid repair target "+ent);
  return false;
  }

}
