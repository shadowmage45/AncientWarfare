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
package shadowmage.ancient_warfare.common.pathfinding;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;


public class Node implements Comparable
{

public float goalLenght;
public float travelCost = 10;

float g;
float f;

public int x;
public int y;
public int z;
public boolean obstacle = false;;

/**
 * @param bX
 * @param i
 */
public Node(int bX, int bY, int bZ)
  {
  this.x = bX; 
  this.y = bY; 
  this.z = bZ; 
  }

/**
 * calc travel cost of this node, and set to obstacle if completely unpathable (solid)
 * @param world
 */
public void calcTraveCost(PathWorldAccess world, Node parentNode)
  {
  if(world==null)
    {
    return;
    }
  this.travelCost = 10;
  if(parentNode!=null && this.x!=parentNode.x && this.z!=parentNode.z)//is a diagonal
    {
    if(!canCrossDiagonal(world, parentNode))
      {
      this.obstacle = true;
      this.travelCost = 10000;
      }
    }  
  if(world.getBlockId(x, y, z) != 0)//if solid...
    {
    if(world.getBlockId(x, y+1, z)==0 && world.getBlockId(x, y+2, z)==0)//check to see if target could jump up from this block
      {
      this.y++;
      }
    else
      {
      this.obstacle = true;
      this.travelCost = 10000;
      }
    }
  else if(world.getBlockId(x, y-1, z)==0)//or if air below
    {    
    if(world.getBlockId(x, y-2, z)==0)//if air is also below the one below that....
      {
      this.obstacle = true;
      this.travelCost = 10000;
      } 
    else
      {
      this.y--;
      }
    }  
  else if(world.getBlockId(x, y+1, z)!=0)//not enough room to fit through
    {
    this.obstacle = true;
    this.travelCost = 10000;
    }
  }

protected float getH(Node b)
  {
  return getDistanceFrom(b)*10 + travelCost;
  }

protected boolean canCrossDiagonal(PathWorldAccess world, Node parentNode)
  {
  if(parentNode!=null)
    {
    if(this.x < parentNode.x && this.z < parentNode.z)
      {
      if(world.getBlockId(x, y, z+1)!=0 || world.getBlockId(x+1, y, z)!=0)
        {
        return false;
        }
      }
    else if(this.x < parentNode.x && this.z > parentNode.z)
      {
      if(world.getBlockId(x, y, z-1)!=0 || world.getBlockId(x+1, y, z)!=0)
        {
        return false;
        }
      }
    else if(this.x > parentNode.x && this.z > parentNode.z)
      {
      if(world.getBlockId(x, y, z-1)!=0 || world.getBlockId(x-1, y, z)!=0)
        {
        return false;
        }
      }
    else if(this.x > parentNode.x && this.z < parentNode.z)
      {
      if(world.getBlockId(x, y, z+1)!=0 || world.getBlockId(x-1, y, z)!=0)
        {
        return false;
        }
      }
    }
  return true;
  }

///**
// * get the cost of travel across this node
// * @return
// */
//public float getNodeTravelCost(Node parentNode)
//  {
//  float parentCost = 0;
//  if(parentNode!=null)
//    {
//    parentCost = parentNode.travelCost;
//    }
//  return this.travelCost + parentCost;
//  }

public Node parentNode = null;

//public float getNodeValue()
//  {  
//  return this.goalLenght * 10 + getNodeTravelCost(parentNode);
//  }

@Override
public int compareTo(Object o)
  {
  if (o instanceof Node)
    {
    Node other = (Node) o;
    float thisVal = f;
    float otherVal = other.f;
    if (thisVal < otherVal) // lower cost = smaller natural value
      {
      return -1;
      }
    else if (thisVal > otherVal) // higher cost = higher natural value
      {
      return 1;
      }
    }
  return 0;
  }

@Override
public boolean equals(Object checkagainst)
  {
  if (checkagainst instanceof Node)
    {
    Node check = (Node) checkagainst;
    if (check.x == x && check.y == y && check.z == z)
      {
      return true;
      }
    }
  return false;
  }

public boolean equals(int x, int y, int z)
  {
  return this.x==x && this.y==y && this.z==z;
  }

public float getDistanceFrom(Node node)
  {
  if(node==null)
    {
    return 0;
    }
  float x = this.x - node.x;
  float y = this.y - node.y;
  float z = this.z - node.z;
  return MathHelper.sqrt_float(x*x+y*y+z*z);  
  }

@Override
public int hashCode()
  {
  return (x << 16) ^ z ^(y<<24);
  }

@Override
public String toString()
  {
  return "Node: "+x+","+y+","+z+" TC: "+travelCost+ " F: "+f;
  }

}
