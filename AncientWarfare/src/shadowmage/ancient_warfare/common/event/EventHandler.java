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
package shadowmage.ancient_warfare.common.event;

import java.util.List;

import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.tracker.GameDataTracker;

public class EventHandler
{
private static EventHandler INSTANCE;
private EventHandler(){}
public static EventHandler instance()
  {
  if(INSTANCE==null){INSTANCE = new EventHandler();}
  return INSTANCE;
  }



/************************************** WORLD LOAD/SAVE HANDLERS **************************************/
@ForgeSubscribe
public void onWorldLoad(WorldEvent.Load evt)
  {
  if(evt.world instanceof WorldServer)
    {
    GameDataTracker.instance().handleWorldLoad(evt.world);
    }  
  }

@ForgeSubscribe
public void onWorldUnload(WorldEvent.Unload evt)
  {
  }

@ForgeSubscribe
public void onWorldSave(WorldEvent.Save evt)
  {
  if(evt.world instanceof WorldServer)
    {
    GameDataTracker.instance().handleWorldSave(evt.world);
    }
  }

@ForgeSubscribe
public void onPlayerAttack(AttackEntityEvent evt)
  {
  if(evt.entityPlayer==null || evt.target==null || evt.entityPlayer.worldObj.isRemote)
    {
    return;
    }
  List<NpcBase> npcs = evt.entityPlayer.worldObj.getEntitiesWithinAABB(NpcBase.class, AxisAlignedBB.getBoundingBox(evt.entityPlayer.posX-20, evt.entityPlayer.posY-10, evt.entityPlayer.posZ-20, evt.entityPlayer.posX+20, evt.entityPlayer.posY+10, evt.entityPlayer.posZ+20));
  if(npcs!=null && !npcs.isEmpty())
    {
    for(NpcBase npc : npcs)
      {
      if(npc == evt.target || npc.isAggroTowards(evt.entityPlayer))
        {
        continue;
        }
      if(npc.getPlayerTarget()!=null && npc.getPlayerTarget().getEntity(evt.entityPlayer.worldObj)==evt.entityPlayer)
        {
        npc.handleBroadcastAttackTarget(evt.target);
        }
      }
    }
  }

@ForgeSubscribe
public void onEntitySpawn(EntityJoinWorldEvent evt)
  {
  if(evt.entity instanceof EntityMob)
    {
    EntityMob zomb = (EntityMob)evt.entity;
    Config.logDebug("setting entity attack tasks for: "+zomb);
    zomb.tasks.addTask(3, new EntityAIAttackOnCollide(zomb, NpcBase.class, zomb.getMoveHelper().getSpeed(), true));
    zomb.targetTasks.addTask(2, new EntityAINearestAttackableTarget(zomb, NpcBase.class, 16.0F, 0, true));
    }
  }


}
