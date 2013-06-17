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

public class ServerTickTimer implements ITickHandler
{

long[] tickTimes = new long[20];
long[] tickIntervals = new long[20];

int index = 0;

long startTime = System.nanoTime();
long prevStartTime = System.nanoTime();

int ticks = 0;
long perSecondReceived = 0;
long perSecondSent = 0;
long[] packetSizesReceived = new long[100];
long prevTotalSizeReceived = 0;
long[] packetSizesSent = new long[100];
long prevTotalSizeSent = 0;

@Override
public void tickStart(EnumSet<TickType> type, Object... tickData)
  {
  if(index==20)
    {
    index = 0;    
    }  
  this.count();
  prevStartTime = startTime;
  startTime = System.nanoTime();
  this.tickIntervals[index] = startTime-prevStartTime;  
  
  
  this.packetSizesReceived[(int) (ticks%100)]=Packet.receivedSize-prevTotalSizeReceived;
  this.prevTotalSizeReceived = Packet.receivedSize;
  this.packetSizesSent[(int)(ticks%100)]=Packet.sentSize-prevTotalSizeSent;
  this.prevTotalSizeSent = Packet.sentSize;
  int tickDiv = ticks/20;
  tickDiv = tickDiv==0? 1 : tickDiv;
  this.perSecondReceived = this.prevTotalSizeReceived / tickDiv;
  this.perSecondSent = this.prevTotalSizeSent / tickDiv;
  this.ticks++;  
//  Config.logDebug("sps: "+this.perSecondSent  + " rps: "+this.perSecondReceived  + " rrs: "+Packet.receivedSize + " rss: "+Packet.sentSize);
  }

@Override
public void tickEnd(EnumSet<TickType> type, Object... tickData)
  {  
  tickTimes[index] = System.nanoTime() - startTime;
//  Config.logDebug("t: "+tickTimes[index]);
  index++;
  }

public void count()
  {
  long total = 0;
  long totalInterval = 0;
  for(int i = 0; i < this.tickTimes.length; i++)
    {
    total += this.tickTimes[i];
    totalInterval += this.tickIntervals[i];
    //Config.logDebug("tickTime: "+tickTimes[i]+" I: "+tickIntervals[i]);
    } 
  long avg = total/this.tickTimes.length;
  long avgInterval = totalInterval/this.tickIntervals.length;
  long tms = (avg/1000000)+1;
  long tmsI = (avgInterval/1000000)+1;
  
  
  int tps = (int)(1000/tms);  
  if(Config.DEBUG)
    {
    Packet01ModData pkt = new Packet01ModData();
    pkt.setTickTimes(avg, tps);
//    pkt.setPacketSizes(this.perSecondSent, this.perSecondReceived);
    pkt.sendPacketToAllPlayers();
    
    }
  
//  Config.logDebug("avg: " + avg + "  TPS: "+ (1000/tms)+" avgI: "+avgInterval);  
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
