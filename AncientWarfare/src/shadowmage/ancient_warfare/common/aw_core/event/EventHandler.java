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
package shadowmage.ancient_warfare.common.aw_core.event;

import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import shadowmage.ancient_warfare.common.aw_core.tracker.GameDataTracker;

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


}
