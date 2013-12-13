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
package shadowmage.ancient_warfare.common.container;

import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.tracker.GameDataTracker;
import shadowmage.ancient_warfare.common.tracker.entry.NpcDataList;

public class ContainerCivicTownHallInfo extends ContainerBase
{

TECivic teBase;
public NpcDataList datas;
public NpcDataList deadDatas;
int dataLength = 0;
int deadDataLength = 0;
int updateTicks = 0;

/**
 * @param openingPlayer
 * @param synch
 */
public ContainerCivicTownHallInfo(EntityPlayer openingPlayer, TECivic te)
  {
  super(openingPlayer, null);
  this.teBase = te;
  if(!openingPlayer.worldObj.isRemote)
    {
    this.datas = GameDataTracker.instance().getListFor(te.getTeamNum());    
    this.deadDatas = GameDataTracker.instance().getDeadListFor(te.getTeamNum());
    }
  }

@Override
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("data"))
    {
    this.datas = new NpcDataList();
    this.datas.readFromNBT(tag.getCompoundTag("data"));
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    }
  if(tag.hasKey("deadData"))
    {
    this.deadDatas = new NpcDataList();
    this.deadDatas.readFromNBT(tag.getCompoundTag("deadData"));
    if(this.gui!=null)
      {
      this.gui.refreshGui();
      }
    } 
  if(tag.hasKey("clearLiving"))
    {
    GameDataTracker.instance().clearLivingEntries(teBase.getTeamNum());
    }
  if(tag.hasKey("clearDead"))
    {
    GameDataTracker.instance().clearDeadEntries(teBase.getTeamNum());
    }
  }

@Override
public void handleInitData(NBTTagCompound tag)
  {
  }

@Override
public List<NBTTagCompound> getInitData()
  {
  return Collections.emptyList();
  }

@Override
public void detectAndSendChanges()
  {
  super.detectAndSendChanges();
  if(!player.worldObj.isRemote)
    {
    this.datas = GameDataTracker.instance().getListFor(teBase.getTeamNum());    
    this.deadDatas = GameDataTracker.instance().getDeadListFor(teBase.getTeamNum());
    updateTicks++;
    if(datas!=null && (dataLength!=datas.getDataLength() || updateTicks>=20 ))
      {
      dataLength = datas.getDataLength();
      NBTTagCompound tag = new NBTTagCompound();
      tag.setCompoundTag("data", datas.getNBTTag());
      this.sendDataToPlayer(tag);
      }
    if(deadDatas!=null && deadDataLength!=deadDatas.getDataLength())
      {
      deadDataLength = deadDatas.getDataLength();
      NBTTagCompound tag = new NBTTagCompound();
      tag.setCompoundTag("deadData", deadDatas.getNBTTag());
      this.sendDataToPlayer(tag);
      }
    if(updateTicks>=20)
      {
      updateTicks = 0;
      }
    }
  }
}
