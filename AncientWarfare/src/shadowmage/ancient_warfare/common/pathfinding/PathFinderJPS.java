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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import shadowmage.ancient_warfare.common.config.Config;


/**
 * one more try at a clean implementation of JPS...
 * some aspects loosely based off of AtomicStryker's MagicYarn code
 * (such as some of the node internals, and open-list handling)
 * working towards a hybrid system that uses JPS for level-path-lines, but
 * defaults to regular A* type for anything vertical.  Works.  Who knows how
 * fast it is compared to a an implementation by someone who knows wtf they 
 * are doing...
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
private static final long interruptTime = 15000000;//15ms

private volatile boolean shouldInterrupt = false;
public boolean threaded = false;

private long runStart = 0;

private int maxPathLength;
private static final int maxSearchIterations = 2000;

private PathWorldAccess world;
private Node startNode;
private Node goalNode;
private Node currentNode;

private Node bestFoundEndNode;//the end node of the best-guess found path
private float bestFoundPathLength;//the path-length of the current best-guess path

PriorityQueue<Node> qNodes = new PriorityQueue<Node>();
private ArrayList<Node> oldNodes = new ArrayList<Node>();//closed nodes....
private LinkedList<Node> nodeCache = new LinkedList<Node>();//just a cache of unused nodes to hopefully reduce new-object creation...may need to trim this if it starts getting too big..?

private ArrayList<Node> searchingNodes = new ArrayList<Node>();

public List<Node> findPath(PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int maxLength)
  {  
//  Config.logDebug("JPS search: "+x+","+y+","+z+" ::TO:: "+x1+","+y1+","+z1+" ML"+maxLength);
  this.runStart = System.nanoTime();
  this.maxPathLength = maxLength;
  this.shouldInterrupt = false;
  this.world = world;
  this.startNode = getOrMakeNode(x, y, z, null);  
  this.goalNode = getOrMakeNode(x1, y1, z1, null);
  startNode.parentNode = null;
  startNode.g = 0;
  startNode.f = startNode.getH(goalNode);
  goalNode.g = goalNode.getDistanceFrom(startNode);
  goalNode.f = goalNode.g;
  this.bestFoundEndNode = this.startNode;
  this.bestFoundPathLength = 0;
  this.qNodes.add(startNode);
  LinkedList<Node> pathNodes = new LinkedList<Node>();
  this.search();  
  if(this.currentNode.equals(goalNode))
    {
    Node n = this.currentNode;    
    Node c = null;
    Node p;
    while(n!=null)//work backwards from the goal node, grabbing its parent at every pass, but we are setting the parents of the new nodes in reverse order (first->goal)
      {
      p = c;
      c = new Node(n.x, n.y, n.z);
      c.parentNode = p;
      pathNodes.push(c);
//      Config.logDebug(c.toString());
      n = n.parentNode;    
      }  
    }
  else
    {
//    Config.logDebug("did not hit goal...??!!??");
    }
  this.world = null;
  this.startNode = null;
  this.goalNode = null;
  this.currentNode = null;
  this.nodeCache.addAll(oldNodes);
  this.nodeCache.addAll(qNodes);
  this.qNodes.clear();
  this.oldNodes.clear();
  return pathNodes;
  }

synchronized public void setInterrupted()
  {
  this.shouldInterrupt = true;
  }

int searchCount = 0;
private void search()
  {
  searchCount = 0;
  while(!this.qNodes.isEmpty())
    {
    if(threaded)
      {
      //Thread.yield();
      }    
    this.currentNode = this.qNodes.poll();
    if(this.oldNodes.contains(currentNode))
      {
//      Config.logDebug("skipping due to in closed list");
      continue;
      }
    else
      {
      this.oldNodes.add(currentNode);
      }
    //this.checkBestPath();
    searchCount++;
//  Config.logDebug("searching. iteration: "+searchCount);
    if(searchCount>=maxSearchIterations )
      {
//      Config.logDebug("breaking due to hitting max node iteration limit of: "+maxSearchIterations);      
      break;
      }
    if(System.nanoTime()- runStart >= interruptTime)
      {
//      Config.logDebug("breaking due to hitting max iteration time of: "+interruptTime);
      break;
      }
    if(this.shouldInterrupt )
      {
//      Config.logDebug("breaking due interrupt requested.  due to path length: " + (this.bestFoundPathLength >= this.maxPathLength) );
      break;
      }
    if(this.currentNode.equals(goalNode) && !shouldInterrupt)
      {
      this.currentNode = this.goalNode;
//      Config.logDebug("goal hit!");
      break;
      }
    identifySuccessors(currentNode);
    }
  }

private void checkBestPath()
  {
  float len = this.currentNode.getPathLength();
  if(this.currentNode.f < this.bestFoundEndNode.f || len > this.bestFoundPathLength)
    {
    this.bestFoundPathLength = len;
    this.bestFoundEndNode = this.currentNode;
    if(this.bestFoundPathLength>=this.maxPathLength)
      {
      this.shouldInterrupt = true;
      this.goalNode = this.bestFoundEndNode;
      }
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
  Iterator<Node> it = this.searchingNodes.iterator();
  Node trim;  
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
    if(jp!=null)
      {
      if(!oldNodes.contains(jp))
        {
        this.addOrUpdateNode(jp);
        }
      }
    }
  }

private Node jump(int x, int y, int z, int dx, int dy, int dz, Node p)
  {
  if(x == goalNode.x && y == goalNode.y && z == goalNode.z)
    {
    goalNode.parentNode = p;
    goalNode.g = p.g + p.getDistanceFrom(goalNode);
    return goalNode;
    }  
  else if(isWalkable(x,y-1,z) || isWalkable(x, y+1,z))
    {
    return getOrMakeNode(x, y, z, p);
    }
  else if(isWalkable(x+dx, y-1,z+dz) || isWalkable(x+dx, y+1,z+dz))
    {
    return getOrMakeNode(x, y, z, p);
    }  
  else if(!isWalkable(x+dx, y+dy, z+dz))
    {
    return getOrMakeNode(x, y, z, p);
    } 
  else if(p.getDistanceFrom(x+dx, y+dy, z+dz)>MAX_JUMP)
    {    
    return getOrMakeNode(x, y, z, p);
    }
  if(dy!=0)//if we're moving vertical, default to A* and only move one node at a time at any given 'jump'
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
      if(!isWalkable(x+dx, y, z+dz))
        {
        return null;
        }
      }
    else
      {
      if(xStopCheck(x, y, z, dx, dy, dz, p))
        {
        return getOrMakeNode(x, y, z, p);
        }
      if(!isWalkable(x+dx, y, z+dz))
        {
        return null;
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
  return jump(x+dx,y+dy,z+dz,dx,dy,dz,p);
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
//    Config.logDebug("parent null, getting all horizontal and vertical neighbors");
    findAllHorizontalNeighbors(n.x, n.y, n.z, 0, 0, 0, n);
    findVerticalNeighbors(n.x, n.y, n.z, 0, 0, 0, n);
    }  
  }

private void findForcedNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {  
  findVerticalNeighbors(n.x, n.y, n.z, 0, 0, 0, n);
  if(dy!=0)//moving vertically
    {
    findAllHorizontalNeighbors(n.x, n.y, n.z, 0, 0, 0, n);    
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
  if(isWalkable(x+dx,y,z) && !isWalkable(x,y,z-dz))
    {
    return true;
    }
  if(isWalkable(x,y,z+dz) && !isWalkable(x-dx, y, z))
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
  if(!isWalkable(x-1, y, z) && isWalkable(x-1, y, z+dz))
    {
    return true;
    }
  if(!isWalkable(x+1, y, z) && isWalkable(x+1, y, z+dz))
    {
    return true;
    }  
//  return z == goalNode.z;
  return false;
  }


private void findDiagonalForcedNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {  
  if(isWalkable(x+dx, y, z+dz))//the continuation of diagonal path
    {
    searchingNodes.add(getOrMakeNode(x+dx, y, z+dz, n));
//    Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
    }
  if(isWalkable(x+dx, y, z))//add L/R neighbors
    {
    searchingNodes.add(getOrMakeNode(x+dx, y, z, n));
//    Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
    if(isWalkable(x+dx, y, z-dz))//diagonal cut-corner check
      {
      searchingNodes.add(getOrMakeNode(x+dx, y, z-dz, n));
//      Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
      }
    }
  if(isWalkable(x, y, z+dz))//add NS neighbors
    {
    searchingNodes.add(getOrMakeNode(x, y, z+dz, n));
//    Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
    if(isWalkable(x-dx, y, z+dz))//diagonal corner forced check
      {
      searchingNodes.add(getOrMakeNode(x-dx, y, z+dz, n));
//      Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
      }
    }
  }

private void findXNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  if(isWalkable(x+dx, y, z))
    {
    searchingNodes.add(getOrMakeNode(x+dx, y, z, n));
//    Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
    if(!isWalkable(x, y, z-1) && isWalkable(x+dx,y,z-1))
      {
      searchingNodes.add(getOrMakeNode(x+dx, y, z-1, n));
//      Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
      }
    if(!isWalkable(x, y, z+1) && isWalkable(x+dx,y,z+1))
      {
      searchingNodes.add(getOrMakeNode(x+dx, y, z-1, n));
//      Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
      }
    }
  }

private void findZNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  if(isWalkable(x, y, z+dz))
    {
    searchingNodes.add(getOrMakeNode(x,y,z+dz, n));
//    Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
    if(!isWalkable(x-1, y, z) && isWalkable(x-1, y, z+dz))
      {
      searchingNodes.add(getOrMakeNode(x-1, y, z+dz, n));
//      Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
      }
    if(!isWalkable(x+1, y, z) && isWalkable(x+1, y, z+dz))
      {
      searchingNodes.add(getOrMakeNode(x+1, y, z+dz, n));
//      Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
      }
    }    
  }

private void findVerticalNeighbors(int x, int y, int z, int dx, int dy, int dz, Node n)
  {
  if(isWalkable(x, y+1, z))//U
    {
    searchingNodes.add(getOrMakeNode(x, y+1, z, n));
//    Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
    }
  if(isWalkable(x, y-1, z))//D
    {
    searchingNodes.add(getOrMakeNode(x, y-1, z, n));
//    Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
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
//  Config.logDebug("checking horizontals");
  for(int bx = x-1; bx <= x+1 ; bx++)
    {
    for(int bz = z-1; bz <= z+1; bz++)
      {
//      Config.logDebug("checking horizontal: "+bx+","+y+","+bz);
      if(bx==x && bz==z)
        {
//        Config.logDebug("horiz skip1");        
        }
      else if(bx==x+dx && bz==z+dz)
        {
//        Config.logDebug("horiz skip2");
        }
      else
        {
        if(isWalkable(bx, y, bz))
          {
          searchingNodes.add(getOrMakeNode(bx, y, bz, n));
//          Config.logDebug("added: "+this.searchingNodes.get(this.searchingNodes.size()-1).toString());
          }
        else
          {
//          Config.logDebug("NOT WALKABLE: "+bx+","+y+","+bz);
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
