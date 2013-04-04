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

import java.util.ArrayList;
import java.util.PriorityQueue;

import net.minecraft.util.MathHelper;

public class PathFinderAStarClassic
{

private PathWorldAccess world;
int tx, ty, tz;
private PriorityQueue<Node> openList = new PriorityQueue<Node>();
private ArrayList<Node> closedNodes = new ArrayList<Node>();
private ArrayList<Node> searchNodes = new ArrayList<Node>();

private void findPath(PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz)
  {
  Node current;
  Node start = new Node(x,y,z);
  start.g = 0;
  start.f = start.getDistanceFrom(tx, ty, tz);
  openList.offer(start);
  while(!openList.isEmpty())
    {
    current = openList.poll();
    closedNodes.add(current);
    }
  }


private void findNeighbors(Node n)
  {
  this.searchNodes.clear();  
  tryAddSearchNode(n.x-1, n.y, n.z, n);
  tryAddSearchNode(n.x+1, n.y, n.z, n);
  tryAddSearchNode(n.x, n.y, n.z-1, n);
  tryAddSearchNode(n.x, n.y, n.z+1, n);

  /**
   * diagonals
   */    
  tryAddSearchNode( n.x-1, n.y, n.z+1, n);
  tryAddSearchNode( n.x-1, n.y, n.z-1, n);
  tryAddSearchNode( n.x+1, n.y, n.z+1, n);
  tryAddSearchNode( n.x+1, n.y, n.z-1, n);

  /**
   * up/down (in case of ladder/water)
   */
  tryAddSearchNode( n.x, n.y+1, n.z, n);
  tryAddSearchNode( n.x, n.y-1, n.z, n);

  /**
   * and the NSEW +/- 1 jumpable blocks..
   */
  tryAddSearchNode( n.x-1, n.y+1, n.z, n);
  tryAddSearchNode( n.x+1, n.y+1, n.z, n);
  tryAddSearchNode( n.x, n.y+1, n.z-1, n);
  tryAddSearchNode( n.x, n.y+1, n.z+1, n);

  tryAddSearchNode( n.x-1, n.y-1, n.z, n);
  tryAddSearchNode( n.x+1, n.y-1, n.z, n);
  tryAddSearchNode( n.x, n.y-1, n.z-1, n);
  tryAddSearchNode( n.x, n.y-1, n.z+1, n);  
  }

private void tryAddSearchNode(int x, int y, int z, Node p)
  {
  if(world.isWalkable(x, y, z))
    {
    //searchNodes.add(getOrMakeNode(x, y, z, p));
    }
  }








private class Node implements Comparable
{
public float goalLenght;
public float travelCost = 10;
public Node parentNode = null;
public float g = Float.POSITIVE_INFINITY;
public float f;
public int x;
public int y;
public int z;
public boolean closed = false;
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

protected float getH(Node b)
  {
  return getDistanceFrom(b);
  }

protected float getH(int tx, int ty, int tz)
  {
  return getDistanceFrom(tx, ty, tz);      
  }

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

public float getDistanceFrom(int x, int y, int z)
  {
  float x1 = this.x - x;
  float y1 = this.y - y;
  float z1 = this.z - z;
  return MathHelper.sqrt_float(x1*x1+y1*y1+z1*z1);
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
}
