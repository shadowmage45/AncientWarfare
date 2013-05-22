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
package shadowmage.ancient_warfare.common.gates.types;

import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.gates.TEGateProxy;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;

public class GateRotatingBridge extends Gate
{


/**
 * @param id
 */
public GateRotatingBridge(int id)
  {
  super(id);
  this.displayName = "Rotating drawbridge";
  this.tooltip = "Rotates downwards to open";
  this.moveSpeed = 1.f;
  this.texture = "gateWood1.png";
  }

@Override
public void onUpdate(EntityGate ent)
  {
  // TODO Auto-generated method stub
  }

@Override
public void setCollisionBoundingBox(EntityGate gate)
  {
  if(gate.pos1==null || gate.pos2==null){return;}
  boolean wideOnXAxis = gate.pos1.x!=gate.pos2.x;  
  BlockPosition min = BlockTools.getMin(gate.pos1, gate.pos2);
  BlockPosition max = BlockTools.getMax(gate.pos1, gate.pos2);
  if(gate.edgePosition<gate.edgeMax)
    {
    gate.boundingBox.setBounds(min.x, min.y, min.z, max.x+1, max.y+1, max.z+1);
    }
  else
    {
    int heightAdj = max.y - min.y;
    BlockPosition pos3 = max.copy();
    pos3.y = min.y;
    switch(gate.gateOrientation)
    {
    case 0://z++
    pos3.z+=heightAdj;
    break;
    case 1://x--
    pos3.x -= heightAdj;
    break;
    case 2://z--
    pos3.z -= heightAdj;
    break;
    case 3://x++
    pos3.x += heightAdj;
    break;
    }
    BlockPosition minTemp = min.copy();
    min = BlockTools.getMin(min, pos3);    
    max = BlockTools.getMax(minTemp, pos3);
    gate.boundingBox.setBounds(min.x, min.y, min.z, max.x+1, max.y+1, max.z+1);
    } 
  }

@Override
public boolean arePointsValidPair(BlockPosition pos1, BlockPosition pos2)
  {
  return pos1.x == pos2.x || pos1.z == pos2.z;
  }

@Override
public void setInitialBounds(EntityGate gate, BlockPosition pos1, BlockPosition pos2)
  {
  BlockPosition min = BlockTools.getMin(pos1, pos2);
  BlockPosition max = BlockTools.getMax(pos1, pos2);
  boolean wideOnXAxis = min.x!=max.x;
  float width = wideOnXAxis ? max.x-min.x+1 : max.z-min.z + 1;
  float xOffset = wideOnXAxis ? width*0.5f: 0.5f;
  float zOffset = wideOnXAxis ? 0.5f : width*0.5f;
  gate.pos1 = min;
  gate.pos2 = max;
  gate.edgeMax = 90.f;
  Config.logDebug("setting gate pos to : "+ (min.x+xOffset) +","+(min.z+zOffset));
  gate.setPosition(min.x+xOffset, min.y, min.z+zOffset);
  }

@Override
public void onGateStartOpen(EntityGate gate)
  {
  if(gate.worldObj.isRemote)
    {
    return;
    }
  int id;
  BlockPosition min = BlockTools.getMin(gate.pos1, gate.pos2);
  BlockPosition max = BlockTools.getMax(gate.pos1, gate.pos2);
  for(int x = min.x; x <= max.x; x++)
    {
    for(int y = min.y; y <=max.y; y++)
      {
      for(int z = min.z; z<= max.z; z++)
        {
        id = gate.worldObj.getBlockId(x, y, z);
        if(id==BlockLoader.gateProxy.blockID)
          {
          gate.worldObj.setBlockToAir(x, y, z);
          }
        }
      }
    }
  }

@Override
public void onGateFinishOpen(EntityGate gate)
  {
  if(gate.worldObj.isRemote)
    {
    return;
    }
  int id;
  BlockPosition min = BlockTools.getMin(gate.pos1, gate.pos2);
  BlockPosition max = BlockTools.getMax(gate.pos1, gate.pos2);
  int heightAdj = max.y - min.y;
  BlockPosition pos3 = max.copy();
  pos3.y = min.y;
  switch(gate.gateOrientation)
  {
  case 0://z++
  pos3.z+=heightAdj;
  break;
  case 1://x--
  pos3.x -= heightAdj;
  break;
  case 2://z--
  pos3.z -= heightAdj;
  break;
  case 3://x++
  pos3.x += heightAdj;
  break;
  }
  BlockPosition minTemp = min.copy();
  min = BlockTools.getMin(min, pos3);    
  max = BlockTools.getMax(minTemp, pos3);
  for(int x = min.x; x <= max.x; x++)
    {
    for(int y = min.y; y <=max.y; y++)
      {
      for(int z = min.z; z<= max.z; z++)
        {
        id = gate.worldObj.getBlockId(x, y, z);
        if(id==0)
          {
          gate.worldObj.setBlock(x, y, z, BlockLoader.gateProxy.blockID);
          TileEntity te = gate.worldObj.getBlockTileEntity(x, y, z);
          if(te!=null && te instanceof TEGateProxy)
            {
            TEGateProxy teg = (TEGateProxy)te;
            teg.setOwner(gate);
            }
          }
        }
      }
    }
  }

@Override
public void onGateStartClose(EntityGate gate)
  {
  if(gate.worldObj.isRemote)
    {
    return;
    }
  int id;
  BlockPosition min = BlockTools.getMin(gate.pos1, gate.pos2);
  BlockPosition max = BlockTools.getMax(gate.pos1, gate.pos2);
  int heightAdj = max.y - min.y;
  BlockPosition pos3 = max.copy();
  pos3.y = min.y;
  switch(gate.gateOrientation)
  {
  case 0://z++
  pos3.z+=heightAdj;
  break;
  case 1://x--
  pos3.x -= heightAdj;
  break;
  case 2://z--
  pos3.z -= heightAdj;
  break;
  case 3://x++
  pos3.x += heightAdj;
  break;
  }
  BlockPosition minTemp = min.copy();
  min = BlockTools.getMin(min, pos3);    
  max = BlockTools.getMax(minTemp, pos3);
  for(int x = min.x; x <= max.x; x++)
    {
    for(int y = min.y; y <=max.y; y++)
      {
      for(int z = min.z; z<= max.z; z++)
        {
        id = gate.worldObj.getBlockId(x, y, z);
        if(id==BlockLoader.gateProxy.blockID)
          {
          gate.worldObj.setBlockToAir(x, y, z);
          }
        }
      }
    }
  }

@Override
public void onGateFinishClose(EntityGate gate)
  {
  if(gate.worldObj.isRemote)
    {
    return;
    }
  int id;
  BlockPosition min = BlockTools.getMin(gate.pos1, gate.pos2);
  BlockPosition max = BlockTools.getMax(gate.pos1, gate.pos2);
  for(int x = min.x; x <= max.x; x++)
    {
    for(int y = min.y; y <=max.y; y++)
      {
      for(int z = min.z; z<= max.z; z++)
        {
        id = gate.worldObj.getBlockId(x, y, z);
        if(id==0)
          {
          gate.worldObj.setBlock(x, y, z, BlockLoader.gateProxy.blockID);
          TileEntity te = gate.worldObj.getBlockTileEntity(x, y, z);
          if(te!=null && te instanceof TEGateProxy)
            {
            TEGateProxy teg = (TEGateProxy)te;
            teg.setOwner(gate);
            }
          }
        }
      }
    }
  }

}
