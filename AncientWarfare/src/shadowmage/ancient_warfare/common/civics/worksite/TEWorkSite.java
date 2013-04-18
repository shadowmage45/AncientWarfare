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
package shadowmage.ancient_warfare.common.civics.worksite;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_warfare.common.civics.TEStructureControl;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.helpers.targeting.AIAggroEntry;
import shadowmage.ancient_warfare.common.pathfinding.waypoints.WayPoint;
import shadowmage.ancient_warfare.common.utils.TargetType;

public abstract class TEWorkSite extends TEStructureControl
{

List<WorkPoint> workPoints = new ArrayList<WorkPoint>();
List<WorkPoint> workingPoints = new ArrayList<WorkPoint>();
List<WorkPoint> workedPoints = new ArrayList<WorkPoint>();
TargetType targetType;

public TEWorkSite setBounds(int minX, int minY, int minZ, int maxX, int maxY, int maxZ)
  {
  this.minX = minX;
  this.minY = minY;
  this.minZ = minZ;
  this.maxX = maxX;
  this.maxY = maxY;
  this.maxZ = maxZ;
  return this;
  }

@Override
public void updateEntity()
  {
  super.updateEntity();
  this.updateWorkPoints();
  }

/**
 * called occasionally to update workable points in the work-bounds
 */
protected abstract void updateWorkPoints();

/**
 * called by NPC AI to get next point of work for that NPC
 * @param npc
 * @return
 */
public WorkPoint getWorkPoint(NpcBase npc)
  {
  return null;
  }

/**
 * called by NPC when he has arrived at work-destination
 * this is for the TE to update the work (remove blocks, add
 * things to NPC inventory, breed animals, etc)
 * @param npc
 * @param workEntry
 */
public abstract void doWork(NpcBase npc, WorkPoint workEntry);

/**
 * called directly after doWork to update the workEntry
 * @param entry
 */
public abstract void onWorkFinished(WorkPoint entry);

}
