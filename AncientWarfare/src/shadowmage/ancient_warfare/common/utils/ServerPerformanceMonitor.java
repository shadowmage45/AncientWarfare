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

import java.util.EnumSet;

import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.network.Packet01ModData;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class ServerPerformanceMonitor implements ITickHandler
{

long[] tickTimes = new long[20];
long[] tickIntervals = new long[20];
long[] pathTickTimes = new long[20];

int index = 0;
long startTime = System.nanoTime();
long prevStartTime = System.nanoTime();

public static long pathFindTimeThisTick = 0;
public static long tickTime;
public static long tickPerSecond;
public static long pathTimeOneSecond;
public static long pathTimeTickAverage;

@Override
public void tickStart(EnumSet<TickType> type, Object... tickData)
  {
  if(!Config.enablePerformanceMonitor){return;}
  if(index==20)
    {
    index = 0;    
    }  
  this.count();
  prevStartTime = startTime;
  startTime = System.nanoTime();
  this.tickIntervals[index] = startTime-prevStartTime;     
 
//  Config.logDebug("sps: "+this.perSecondSent  + " rps: "+this.perSecondReceived  + " rrs: "+Packet.receivedSize + " rss: "+Packet.sentSize);
  }

@Override
public void tickEnd(EnumSet<TickType> type, Object... tickData)
  {  
  if(!Config.enablePerformanceMonitor){return;}
  tickTimes[index] = System.nanoTime() - startTime;
  pathTickTimes[index] = pathFindTimeThisTick;
  pathFindTimeThisTick = 0;
  index++;
  }

public void count()
  {
  if(!Config.enablePerformanceMonitor){return;}
  long total = 0;
  long totalInterval = 0;
  long totalPathTime = 0;
  for(int i = 0; i < this.tickTimes.length; i++)
    {
    total += this.tickTimes[i];
    totalInterval += this.tickIntervals[i];
    //Config.logDebug("tickTime: "+tickTimes[i]+" I: "+tickIntervals[i]);
    } 
  for(int i = 0; i < this.pathTickTimes.length; i++)
    {
    totalPathTime += this.pathTickTimes[i];
    }
  long avg = total/20;
  long avgInterval = totalInterval/20;
  long tms = (avg/1000000)+1;
  long tmsI = (avgInterval/1000000)+1; 
  int tps = (int)(1000/tms);  
  tickTime = avg;
  tickPerSecond = tps;
  pathTimeOneSecond = totalPathTime;
  pathTimeTickAverage = totalPathTime / 20;
  }

@Override
public EnumSet<TickType> ticks()
  {
  return EnumSet.of(TickType.SERVER);
  }

@Override
public String getLabel()
  {
  return "AW.TPS";
  }

}
