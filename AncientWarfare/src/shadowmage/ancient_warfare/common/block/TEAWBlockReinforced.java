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
package shadowmage.ancient_warfare.common.block;

import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class TEAWBlockReinforced extends TileEntity
{

public int baseBlockID;
int damageRemaining = 16;
public int ownerTeam = 0;

/**
 * 
 */
public TEAWBlockReinforced()
  {
  // TODO Auto-generated constructor stub
  }

public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z)
  {
  float strength = ForgeHooks.blockStrength(BlockLoader.reinforced, player, world, x, y, z);
  int team = TeamTracker.instance().getTeamForPlayer(player);
  if(team==this.ownerTeam)
    {
    return strength;
    }
  return strength*0.1f;
  }

@Override
public Packet getDescriptionPacket()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setInteger("block", baseBlockID);  
  tag.setInteger("dmg", this.damageRemaining);
  Packet132TileEntityData pkt = new Packet132TileEntityData(xCoord, yCoord, zCoord, 0, tag);  
  return pkt;
  }

@Override
public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
  {
  super.onDataPacket(net, pkt);
  this.baseBlockID = pkt.customParam1.getInteger("block");
  this.damageRemaining = pkt.customParam1.getInteger("dmg");
  }

@Override
public void readFromNBT(NBTTagCompound tag)
  {  
  super.readFromNBT(tag);
  this.baseBlockID = tag.getInteger("block");
  this.damageRemaining = tag.getInteger("dmg");
  this.ownerTeam = tag.getByte("team");
  }

@Override
public void writeToNBT(NBTTagCompound tag)
  {
  super.writeToNBT(tag);
  tag.setInteger("block", this.baseBlockID);
  tag.setInteger("dmg", damageRemaining);
  tag.setByte("team", (byte)this.ownerTeam);
  }

public void onExploded(Explosion expl)
  {
  Config.logDebug("dmg: "+this.damageRemaining + " size: "+expl.explosionSize);
  this.damageRemaining -= expl.explosionSize;
  Config.logDebug("newdmg: "+this.damageRemaining);
  if(this.damageRemaining<=0)
    {
    worldObj.setBlockToAir(xCoord, yCoord, zCoord);    
    }
  }


}
