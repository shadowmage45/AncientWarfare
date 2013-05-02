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
package shadowmage.ancient_warfare.common.civics.worksite.te.mine;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.civics.worksite.WorkSitePoint;
import shadowmage.ancient_warfare.common.targeting.TargetType;

/**
 * slightly different layout of mine-level, more like the vanilla mine-shafts (for both main shaft and branches)
 * @author Shadowmage
 *
 */
public class MineLevelClassic extends MineLevel
{

/**
 * @param xPos
 * @param yPos
 * @param zPos
 * @param xSize
 * @param ySize
 * @param zSize
 */
public MineLevelClassic(int xPos, int yPos, int zPos, int xSize, int ySize, int zSize)
  {
  super(xPos, yPos, zPos, xSize, ySize, zSize);
  this.levelSize = 4;
  }

@Override
protected void scanLevel(TEWorkSiteMine mine, World world)
  {
  this.scanShaft(mine, world);
  this.scanTunnels(mine, world);
  this.scanBranches(mine, world);
  }

/************************************************SHAFT*************************************************/

protected void scanShaft(TEWorkSiteMine mine, World world)
  {
  for(int y = minY + ySize-1; y >= minY; y--)//start at the top...
    {
    for(int x = shaftX; x<=shaftX+1; x++)
      {
      for(int z = shaftZ; z<=shaftZ+1; z++)
        {
        if(z==shaftZ)
          {
          this.addSouthShaft(mine, world, x,y,z);
          //check shaftZ-1 for resources
          }
        else
          {
          this.addNorthShaft(mine, world, x, y, z);
          }        
        }
      }    
    }
  }

protected void addNorthShaft(TEWorkSiteMine mine, World world, int x, int y, int z)
  {
  int id = world.getBlockId(x, y, z);
  int id2 = world.getBlockId(x, y, z+1);
  //scan N block, add clear/fill if needed
  if(isValidResource(id2) || needsFilledFloor(id2))
    {  
    if(mine.inventory.containsAtLeast(mine.fillerFilter, 1))
      {
      addNewPoint(x, y, z+1,  TargetType.MINE_FILL);
      }
    }
  if(id!=Block.ladder.blockID )
    {
    if(mine.inventory.containsAtLeast(mine.ladderFilter, 1))
      {
      addNewPoint(x, y, z,  (byte)2, TargetType.MINE_LADDER);
      }
    else
      {
      addNewPoint(x, y, z,  TargetType.MINE_CLEAR);
      }    
    }
  }

protected void addSouthShaft(TEWorkSiteMine mine, World world, int x, int y, int z)
  {
  int id = world.getBlockId(x, y, z);
  int id2 = world.getBlockId(x, y, z-1);  
  if(isValidResource(id2) || needsFilledFloor(id2))
    {  
    if(mine.inventory.containsAtLeast(mine.fillerFilter, 1))
      {
      addNewPoint(x, y, z-1,  TargetType.MINE_FILL);
      }
    }
  if(id!=Block.ladder.blockID )
    {
    if(mine.inventory.containsAtLeast(mine.ladderFilter, 1))
      {
      addNewPoint(x, y, z,  (byte)3, TargetType.MINE_LADDER);
      }
    else
      {
      addNewPoint(x, y, z,  TargetType.MINE_CLEAR);
      }    
    }
  }

/************************************************TUNNELS*************************************************/
protected void scanTunnels(TEWorkSiteMine mine, World world)
  {
  int id = 0;
  for(int x = shaftX-1; x>= minX ; x--)//add west tunnel
    {
    for(int z = shaftZ; z<= shaftZ+1; z++)      
      {
      for(int y = minY; y<= minY+1; y++)
        {
        if(y==minY)
          {
          //add left/bottom
          this.addTunnelPiece(mine, x, y, z,  true, false);
          }
        else
          {
          this.addTunnelPiece(mine, x, y, z,  true, true);
          //add left/top
          }
        }
      }
    }
  for(int x = shaftX+2; x <= minX + xSize-1; x++)//add west tunnel
    {
    for(int z = shaftZ; z<= shaftZ+1; z++)      
      {
      for(int y = minY; y<= minY+1; y++)
        {
        if(y==minY)
          {
          //add right/bottom
          this.addTunnelPiece(mine, x, y, z, false, false);
          }
        else
          {
          this.addTunnelPiece(mine, x, y, z, false, true);
          //add right/top
          }
        }
      }
    }
  }

protected void addTunnelPiece(TEWorkSiteMine mine, int x, int y, int z, boolean left, boolean top)
  {  
  int id1 = mine.worldObj.getBlockId(x, y, z);
  boolean addTorch = !top && x%4==0;
  if(addTorch)
    {
    if(id1!=Block.torchWood.blockID)
      {
      if(mine.hasTorch())
        {
        addNewPoint(x,y,z, TargetType.MINE_TORCH);
        }
      else
        {
        addNewPoint(x, y, z, TargetType.MINE_CLEAR);
        }      
      }
    }
  else if(id1!=0)
    {
    addNewPoint(x, y, z,  TargetType.MINE_CLEAR);      
    }
  int id = top? mine.worldObj.getBlockId(x, y+1, z) : mine.worldObj.getBlockId(x, y-1, z);
  int y1 = top? y+1 : y-1;
  if(isValidResource(id))
    {     
    if(!top)
      {
      if(mine.hasFiller())
        {
        addNewPoint(x, y1, z,  TargetType.MINE_FILL);
        }
      else
        {
        addNewPoint(x, y1, z,  TargetType.MINE_CLEAR);
        }         
      } 
    else
      {
      addNewPoint(x, y1, z,  TargetType.MINE_CLEAR);
      }
    }
  else if((top && needsFilled(id)) || (!top && needsFilledFloor(id)) )
    {
    if(mine.hasFiller())
      {
      addNewPoint(x, y1, z,  TargetType.MINE_FILL);
      }
    }
  }
/************************************************BRANCHES*************************************************/
protected void scanBranches(TEWorkSiteMine mine, World world)
  {
  for(int x = shaftX-1; x>=minX; x-=3)
    {
    for(int z = shaftZ+2; z <=minZ+zSize-1; z++)//do n/w side branches
      {
      for(int y = minY; y<= minY+1; y++)
        {
        this.addNodeToBranch(mine, x, y, z, y!=minY);
        }
      }
    for(int z = shaftZ-1; z >=minZ; z--)//do s/w side branches
      {
      for(int y = minY; y<= minY+1; y++)
        {
        this.addNodeToBranch(mine, x, y, z, y!=minY);
        }
      }
    }  
  for(int x = shaftX+2; x <= minX+xSize-1; x+=3)
    {
    for(int z = shaftZ+2; z <=minZ+zSize-1; z++)//do n/e side branches
      {
      for(int y = minY; y<= minY+1; y++)
        {
         this.addNodeToBranch(mine, x, y, z, y!=minY);
        }
      }
    for(int z = shaftZ-1; z >=minZ; z--)//do s/e side branches
      {
      for(int y = minY; y<= minY+1; y++)
        {
         this.addNodeToBranch(mine, x, y, z, y!=minY);
        }
      }
    }
  }

protected void addNodeToBranch(TEWorkSiteMine mine, int x, int y, int z, boolean top)
  {
  int id1 = mine.worldObj.getBlockId(x, y, z);
  int id2 = top? mine.worldObj.getBlockId(x, y+1, z) : mine.worldObj.getBlockId(x, y-1, z);
  int id3 = mine.worldObj.getBlockId(x+1, y, z);
  int id4 = mine.worldObj.getBlockId(x-1, y, z);
  int y1 = top? y+1 : y-1; 
  boolean last = z%4!=0;
  boolean addTorch = !top && z%4==0;
  /**
   * check main block
   */
  if(addTorch)
    {
    if(id1 != Block.torchWood.blockID)
      {
      if(mine.hasTorch())
        {
        addNewPoint(x,y,z, TargetType.MINE_TORCH);
        }
      else
        {
        addNewPoint(x, y, z,  TargetType.MINE_CLEAR);
        }
      }
    }
  else if(id1!=0)
    {
    addNewPoint(x, y, z,  TargetType.MINE_CLEAR);
    }
  /**
   * check top or bottom block
   */
  if(isValidResource(id2))
    {
    if(!top)//fill floor
      {
      if(mine.hasFiller())
        {
        addNewPoint(x, y1, z,  TargetType.MINE_FILL);
        }
      else
        {
        addNewPoint(x, y1, z,  TargetType.MINE_CLEAR);
        }
      }
    else
      {
      addNewPoint(x, y1, z,  TargetType.MINE_CLEAR);
      }
    }
  else if((top && needsFilled(id2)) || (!top && needsFilledFloor(id2)))//only fill water/lava above, or missing floor blocks
    {
    if(mine.hasFiller())
      {
      addNewPoint(x, y1, z,  TargetType.MINE_FILL);
      }
    }
  /**
   * check wall x+1
   */
  if(isValidResource(id3))
    {
    addNewPoint(x+1, y, z,  TargetType.MINE_CLEAR);
    }
  else if(needsFilled(id3))
    {
    if(mine.hasFiller())
      {
      addNewPoint(x+1, y, z,  TargetType.MINE_FILL);
      }    
    }
  /**
   * check wall x-1
   */
  if(isValidResource(id4))
    {
    addNewPoint(x-1, y, z,  TargetType.MINE_CLEAR);
    }
  else if(needsFilled(id4))
    {
    if(mine.hasFiller())
      {
      addNewPoint(x-1, y, z,  TargetType.MINE_FILL);
      }
    }  
  }

/************************************************UTILITY*************************************************/
protected void addNewPoint(int x, int y, int z, byte meta, TargetType type)
  {
  this.workList.add(new WorkSitePoint(x,y,z, meta,type));
  }

protected void addNewPoint(int x, int y, int z, TargetType type)
  {
  this.addNewPoint(x, y, z, (byte)0, type);
  }

}
