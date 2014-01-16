/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_strategy.common.structure;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.DamageType;
import shadowmage.ancient_strategy.common.network.Packet08Strategy;
import shadowmage.ancient_structures.common.manager.StructureTemplateManager;
import shadowmage.ancient_structures.common.template.StructureTemplate;
import shadowmage.ancient_structures.common.template.build.StructureBB;

public class StrategyStructure
{

UUID id;
String templateName;
int buildFace;
StructureBB bb;
StrategyStructureDefinition structureType;

float currentHealth;
float perBlockDamage;


public boolean isPositionInStructure(int x, int y, int z)
  {
  return bb.isPositionInBoundingBox(x, y, z);
  }

/**
 * handle any actions necessary when a block is attempted to break in the structure area
 * input coords are already validated for being within min/max bounds of the structure
 * @param world
 * @param x
 * @param y
 * @param z
 * @return true if block should be broken (was not part of the template)
 *         false to cancel block-break event and prevent block from being broken
 */
public boolean handleAttemptedBlockBreak(World world, int x, int y, int z)
  {  
  BlockPosition t = bb.getPositionInTemplate(x, y, z, buildFace);
  StructureTemplate template = StructureTemplateManager.instance().getTemplate(templateName);
  if(template.getRuleAt(t.x, t.y, t.z)!=null)
    {
    handleDamageToStructure(world, x, y, z, DamageType.generic, perBlockDamage);
    return false;
    }  
  return true;
  }

protected void handleDamageToStructure(World world, int x, int y, int z, DamageSource dmg, float amount)
  {
  this.currentHealth-=amount;
  if(this.currentHealth<=0)
    {
    this.setDead(world);
    }
  NBTTagCompound tag = new NBTTagCompound();
  tag.setFloat("health", this.currentHealth);
  this.sendDataToClients(tag);
  }

public void setDead(World world)
  {
  /**
   * go through the template, zero/airing any blocks that are part of the template
   * perhaps create a special builder class that does this instead of building... (an inverse builder of sorts)
   */
  }

public void writeToNBT(NBTTagCompound tag){}

public void readFromNBT(NBTTagCompound tag){}

public void writeClientNBT(NBTTagCompound tag){}

public void readClientNBT(NBTTagCompound tag){}

/**
 * server-> client comms channel
 * @param tag
 */
public void handlePacketData(NBTTagCompound tag)
  {
  if(tag.hasKey("health"))
    {
    this.currentHealth = tag.getFloat("health");
    }
  }

protected void sendDataToClients(NBTTagCompound tag)
  {
  Packet08Strategy pkt = new Packet08Strategy();
  pkt.setSendingID(id);
  pkt.packetData.setCompoundTag("structureUpdate", tag);  
  }

}
