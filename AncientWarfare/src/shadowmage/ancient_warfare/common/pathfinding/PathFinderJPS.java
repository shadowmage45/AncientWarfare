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
import java.util.LinkedList;
import java.util.PriorityQueue;

import shadowmage.ancient_warfare.common.config.Config;


/**
 * one more try at a clean implementation of JPS...
 * some aspects loosely based off of AtomicStryker's MagicYarn code
 * (such as some of the node internals, and open-list handling)
 * working towards a hybrid system that uses JPS for level-path-lines, but
 * defaults to regular A* type for anything vertical
 * 
 * JPS Algorithm breakdown:
 * 1. Set start node, goal node.  Start node g = 0, h = distance to goal; f=g+h;
 * 2. Add start node to open list;
 * 3. Loop
 *      while(!openList.isEmpty())
 *        {
 *        node = lowest f score node from openList;
 *        node.closed = true;
 *        if node==goal return finished;
 *        look for natural and forced neighbors for that node;
 *            for each neighbor, attempt to jump until obstructed or past max jump length;
 *            if successfull jump, set jump point parent and score, and add jump-point to open list            
 *        }
 *        
 * 3.A identifySuccessors
 *      get all neighbors, natural and forced, for the current examining node
 *      for each neighbor, call jump.  
 *        if jump returns a node && !node.closed add to openList and update scores and parentNode
 *        
 * @author Shadowmage
 * 
 */
public class PathFinderJPS
{

private static final float MAX_JUMP = 25.f;

private PathWorldAccess world;
private Node startNode;
private Node goalNode;
private Node currentNode;

PriorityQueue<Node> qNodes = new PriorityQueue<Node>();
private ArrayList<Node> oldNodes = new ArrayList<Node>();//closed nodes....
private LinkedList<Node> nodeCache = new LinkedList<Node>();//just a cache of unused nodes to hopefully reduce new-object creation...may need to trim this if it starts getting too big..?

private ArrayList<Node> searchingNodes = new ArrayList<Node>();


public void findPath(PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1)
  {
  this.world = world;
  this.startNode = getOrMakeNode(x, y, z, null);  
  this.goalNode = getOrMakeNode(x1, y1, z1, null);
  startNode.g = 0;
  startNode.f = startNode.getH(goalNode);
  goalNode.g = goalNode.getDistanceFrom(startNode);
  goalNode.f = goalNode.g;
  this.qNodes.add(startNode);
  this.search();  
  if(this.currentNode.equals(goalNode))
    {
    Node n = this.currentNode;
    while(n!=null)
      {
      Config.logDebug(n.toString());
      n = n.parentNode;
      }
    }
  else
    {
    Config.logDebug("did not hit goal...??!!??");
    }
  this.world = null;
  this.startNode = null;
  this.goalNode = null;
  this.currentNode = null;
  this.nodeCache.addAll(oldNodes);
  this.nodeCache.addAll(qNodes);
  this.qNodes.clear();
  this.oldNodes.clear();  
  }

private void search()
  {
  while(!this.qNodes.isEmpty())
    {    
    this.currentNode = this.qNodes.poll();
    this.oldNodes.add(currentNode);
    if(this.currentNode.equals(goalNode))
      {
      this.currentNode = this.goalNode;
      Config.logDebug("goal hit!");
      break;
      }
    identifySuccessors(currentNode);
    }
  }

/**
 * from n, which directions, from those of candidate neighbors, have jump destinations at the end of them?
 * @param n
 */
private void identifySuccessors(Node n)
  {
  this.searchingNodes.clear();
  this.findNeighbors(n);
  int dx;
  int dy;
  int dz;
  for(Node nb : searchingNodes)
    {
    dx = nb.x - n.x;
    dy = nb.y - n.y;
    dz = nb.z - n.z;
    dx = dx <= -1 ? -1 : dx >= 1 ? 1 : 0;
    dy = dy <= -1 ? -1 : dy >= 1 ? 1 : 0;
    dz = dz <= -1 ? -1 : dz >= 1 ? 1 : 0;
    Node jp = this.jump(nb.x, nb.y, nb.z, dx, dy, dz, n);
    if(!oldNodes.contains(jp))
      {
      this.addOrUpdateNode(jp);
      }
    }
  }

private Node jump(int x, int y, int z, int dx, int dy, int dz, Node p)
  {
  if(!isWalkable(x+dx, y+dy, z+dz))
    {
    return getOrMakeNode(x, y, z, p);
    }
  else if(x == goalNode.x && y == goalNode.y && z == goalNode.z)
    {
    goalNode.parentNode = p;
    goalNode.g = p.g + p.getDistanceFrom(goalNode);
    return goalNode;
    }
  else if(p.getDistanceFrom(x, y, z)>MAX_JUMP)
    {
    return getOrMakeNode(x, y, z, p);
    }
  if(dy!=0)//if we're moving vertical, default to A* and only search one node at a time at any given 'jump'
    {    
    if(isWalkable(x+dx, y+dy, z+dz))
      {
      return getOrMakeNode(x, y, z, p);
      }
    return null;
    }
  if(dx!=0 && dz!=0)//diagonal jump check
    {
    if(diagonalStopCheck(x, y, z, dx, dy, dz, p))
      {
      return getOrMakeNode(x, y, z, p);
      }    
    }  
  else
    {
    if(dx==0)
      {
      if(zStopCheck(x, y, z, dx, dy, dz, p))
        {
        return getOrMakeNode(x, y, z, p);
        }
      }
    else
      {
      if(xStopCheck(x, y, z, dx, dy, dz, p))
        {
        return getOrMakeNode(x, y, z, p);
        }
      }
    }
  if(dx!=0 && dz!=0)//diagonal special case jump check
    {
    Node j1 = jump(x,y,z,dx,dy,0,p);
    Node j2 = jump(x,y,z,0,dy,dz,p);
    if(j1 != null || j2!= null)
      {
      return getOrMakeNode(x, y, z, p); 
      }
    }
  return jump(x,y,z,dx,dy,dz,p);
  }

/**
 * if n.parent is null, grabs ALL nearby neighbor nodes,
 * else trims neighbor selection based on direction from parent to n
 * through forcedNeighbor methods
 * @param n
 */
private void findNeighbors(Node n)
  {
  if(n.parentNode!=null)
    {
    int dx = n.x - n.parentNode.x;
    int dy = n.y - n.parentNode.y;
    int dz = n.z - n.parentNode.z;
    dx = dx <= -1 ? -1 : dx >= 1 ? 1 : 0;
    dy = dy <= -1 ? -1 : dy >= 1 ? 1 : 0;
    dz = dz <= -1 ? -1 : dz >= 1 ? 1 : 0;
    this.findForcedNeighbors(n.x, n.y, n.z, dx, dy, dz, n);
    }
  else
    {
    findAllHorizontalNeighbors(n.x, n.y, n.z, 0, 0, 0, n);
    findVerticalNeighbors(n.x, n.y, n.z, 0, 0, 0, n);
    }  
  }

private void findForcedNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {  
  if(dy!=0)//moving vertically
    {
    findAllHorizontalNeighbors(n.x, n.y, n.z, 0, 0, 0, n);
    findVerticalNeighbors(n.x, n.y, n.z, 0, 0, 0, n);
    }
  else if(dx!=0 && dz!=0)//diagonal
    {
    this.findDiagonalForcedNeighbors(x, y, z, dx, dy, dz, n);
    }
  else if(dx==0)//moving on Z axis
    {
    this.findZNeighbors(x, y, z, dx, dy, dz, n);
    }
  else//moving on X axis
    {
    this.findXNeighbors(x, y, z, dx, dy, dz, n);
    }
  }


/**
 * returns true if a jump of the given parameters should stop because of a nearby forced neighbor when moving diagonally
 */
private boolean diagonalStopCheck(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  if(isWalkable(x, y+dy, z+dz) && !isWalkable(x-dx, y+dy, z+dz))
    {
    return true;
    }
  if(isWalkable(x+dz, y+dy, z) && !isWalkable(x+dx, y+dy, z-dz))
    {
    return true;
    }
  return false;
  }

private boolean xStopCheck(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  if(!isWalkable(x, y+dy, z-1) && isWalkable(x+dx, y+dy, z-1))
    {
    return true;
    }
  if(!isWalkable(x, y+dy, z+1) && isWalkable(x+dx, y+dy, z+1))
    {
    return true;
    }
  return false;
  }

private boolean zStopCheck(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  if(!isWalkable(x-1, y+dy, z) && isWalkable(x-1, y+dy, z+dz))
    {
    return true;
    }
  if(!isWalkable(x+1, y+dy, z) && isWalkable(x+1, y+dy, z+dz))
    {
    return true;
    }
  return false;
  }

private void findDiagonalForcedNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {  
  if(isWalkable(x+dx, y, z+dz))//the continuation of diagonal path
    {
    searchingNodes.add(getOrMakeNode(x+dx, y, z+dz, n));
    }
  if(isWalkable(x+dx, y, z))//add L/R neighbors
    {
    searchingNodes.add(getOrMakeNode(x+dx, y, z, n));
    if(isWalkable(x+dx, y, z-dz))//diagonal cut-corner check
      {
      searchingNodes.add(getOrMakeNode(x+dx, y, z-dz, n));
      }
    }
  if(isWalkable(x, y, z+dz))//add NS neighbors
    {
    searchingNodes.add(getOrMakeNode(x, y, z+dz, n));
    if(isWalkable(x-dx, y, z+dz))//diagonal corner forced check
      {
      searchingNodes.add(getOrMakeNode(x-dx, y, z+dz, n));
      }
    }
  }

private void findXNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  if(isWalkable(x+dx, y, z))
    {
    searchingNodes.add(getOrMakeNode(x+dx, y, z, n));
    if(!isWalkable(x, y, z-1) && isWalkable(x+dx,y,z-1))
      {
      searchingNodes.add(getOrMakeNode(x+dx, y, z-1, n));
      }
    if(!isWalkable(x, y, z+1) && isWalkable(x+dx,y,z+1))
      {
      searchingNodes.add(getOrMakeNode(x+dx, y, z-1, n));
      }
    }
  }

private void findZNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  if(isWalkable(x, y, z+dz))
    {
    searchingNodes.add(getOrMakeNode(x,y,z+dz, n));
    if(!isWalkable(x-1, y, z) && isWalkable(x-1, y, z+dz))
      {
      searchingNodes.add(getOrMakeNode(x-1, y, z+dz, n));
      }
    if(!isWalkable(x+1, y, z) && isWalkable(x+1, y, z+dz))
      {
      searchingNodes.add(getOrMakeNode(x+1, y, z+dz, n));
      }
    }    
  }

private void findVerticalNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  if(isWalkable(x, y+1, z))//U
    {
    searchingNodes.add(getOrMakeNode(x, y+1, z, n));
    }
  if(isWalkable(x, y-1, z))//D
    {
    searchingNodes.add(getOrMakeNode(x, y+1, z, n));
    }
  
  //up diagonals
  if(isWalkable(x+1, y+1, z)){ searchingNodes.add(getOrMakeNode(x+1, y+1, z, n));}
  if(isWalkable(x-1, y+1, z)){ searchingNodes.add(getOrMakeNode(x-1, y+1, z, n));}
  if(isWalkable(x, y+1, z+1)){ searchingNodes.add(getOrMakeNode(x, y+1, z+1, n));}
  if(isWalkable(x, y+1, z-1)){ searchingNodes.add(getOrMakeNode(x, y+1, z-1, n));}
  
  //down diagonals
  if(isWalkable(x+1, y-1, z)){ searchingNodes.add(getOrMakeNode(x+1, y-1, z, n));}
  if(isWalkable(x-1, y-1, z)){ searchingNodes.add(getOrMakeNode(x-1, y-1, z, n));}
  if(isWalkable(x, y-1, z+1)){ searchingNodes.add(getOrMakeNode(x, y-1, z+1, n));}
  if(isWalkable(x, y-1, z-1)){ searchingNodes.add(getOrMakeNode(x, y-1, z-1, n));}  
  }

/**
 * adds all horizontal neighbors of a node to searchList, aside from the node itself 
 * and the node it came from (if any--determined by x+dx, z+dz)
 * @param x
 * @param y
 * @param z
 * @param dx
 * @param dy
 * @param dz
 */
private void findAllHorizontalNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  for(int bx = x-1; bx>=x+1;bx++)
    {
    for(int bz = z-1; bz>=z+1; bz++)
      {
      if(bx==x && bz==z){}
      else if(bx==x+dx && bz==z+dz){}
      else
        {
        if(isWalkable(bx, y, bz))
          {
          searchingNodes.add(getOrMakeNode(bx, y, bz, n));
          }
        }      
      }
    }
  }

/**
 * return if target node is not an obstacle, and has enough clearance for entity to fit
 * @param x
 * @param y
 * @param z
 * @return
 */
private boolean isWalkable(int x, int y, int z)
  {
  return world.isWalkable(x, y, z);
  }

private Node getOrMakeNode(int x, int y, int z, Node p)
  {
  Node n = null; 
  if(!this.nodeCache.isEmpty())
    {
    n = this.nodeCache.pop();   
    n.x = x;
    n.y = y;
    n.z = z;
    n.f = 0;    
    }
  else
    {
    n = new Node(x,y,z);    
    }   
  if(p!=null)
    {
    n.parentNode = p;
    n.g = p.g + n.getDistanceFrom(p);    
    }
  return n;
  }

/**
 * adds or updates a node in the open-list (qNodes)
 * @param n
 * @return true if updating old node
 */
private boolean addOrUpdateNode(Node n)
  {
  boolean found = false;
  for(Node nd : oldNodes)
    {
    if(nd.equals(n))
      {
      nd.parentNode = n.parentNode;
      nd.f = n.f;
      nd.g = n.g;
      this.nodeCache.add(n);
      return true;
      }
    }
  if(!found)
    {
    n.f = n.g + n.getH(goalNode);
    this.qNodes.offer(n);
    }
  return false;
  }

}
