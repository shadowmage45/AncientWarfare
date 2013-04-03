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
import shadowmage.ancient_warfare.common.utils.Trig;

public class PathFinderJPS
{

boolean finished = false;
boolean stoppedEarly = false;
PathWorldAccess world;

Node startNode;
Node goalNode;
Node currentNode;

Node bestFoundEndNode;
float bestFoundDistance;
float bestPathLength;

float searchLength;

PriorityQueue<Node> qNodes = new PriorityQueue<Node>();
//List<Node> closedNodes = new ArrayList<Node>();
List<Node> allNodes = new ArrayList<Node>();
List<Node> searchingNodes = new ArrayList<Node>();
private LinkedList<Node> nodeCache = new LinkedList<Node>();

public List<Node> findPath(PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int searchRange)
  {
  this.world = world;
  searchLength = searchRange;
  setupInitialNodes(x, y, z, x1, y1, z1, searchRange);
  long t = System.nanoTime();
  search();
  Config.logDebug("jps time: " + (System.nanoTime()-t));
  this.world = null; 
  if(stoppedEarly)
    {
//    Config.logDebug("stopped early");
    }
  if(finished)
    {
//    Config.logDebug("found complete path");
    }
  LinkedList<Node> path = new LinkedList<Node>();
  Node n = goalNode;  
  while(n!=null)
    {    
    path.push(n);
    n = n.parentNode;
    }  
//  for(Node nd : path)
//    {
//    Config.logDebug("n: "+nd);
//    }
  this.goalNode = null;
  this.currentNode = null;
  this.bestFoundEndNode = null;
  this.bestFoundDistance = 0;
  this.bestPathLength = 0;  
  return path;
  }

public void setupInitialNodes(double x, double y, double z, double x1, double y1, double z1, int searchRange)
  {
  this.qNodes.clear();
  this.flushNodes(); 
  this.startNode = findOrMakeNode((int)x, (int)y, (int)z);
  this.goalNode = findOrMakeNode((int)x1, (int)y1, (int)z1);
  startNode.g = 0;
  startNode.f = startNode.getH(goalNode);
  this.qNodes.add(startNode);
  this.currentNode = startNode;
  if(this.bestFoundEndNode==null)
    {
    this.bestFoundEndNode = currentNode;
    this.bestFoundDistance = this.bestFoundEndNode.getDistanceFrom(goalNode);
    this.bestPathLength = 0;
    }
  this.finished = false;
  this.stoppedEarly = false;
  }

public void search()
  {
  while(!qNodes.isEmpty())
    {
    currentNode = qNodes.poll();  
//    Config.logDebug("examining: "+currentNode);
    if(currentNode.equals(goalNode))
      {
//      Config.logDebug("hit GOAL NODE");
      goalNode = currentNode;
      finished = true;
      return;
      }    
    currentNode.closed = true;
    identifySuccessors(currentNode);
    if(this.stoppedEarly)
      {
      break;
      }
    }
  }

/**
 * get all possible jump points from this node
 * @param node
 */
private void identifySuccessors(Node node)
  {
  List<Node> successors = findNeighbors(node);
//  Config.logDebug("checking successors");
  for(Node n : successors)
    {
//    Config.logDebug(n.toString());
    Node jumpPoint = jump(n.x, n.y, n.z, node.x, node.y, node.z);
//    Config.logDebug("jumping: "+jumpPoint);
    if(jumpPoint!=null && !jumpPoint.closed)
      {
      qNodes.offer(jumpPoint);
      }
    }
  }

private List<Node> findNeighbors(Node n)
  {
  searchingNodes.clear();
  if(n.parentNode!=null)
    {
    int x = n.x;
    int y = n.y;
    int z = n.z;
    int px = n.parentNode.x;
    int py = n.parentNode.y;
    int pz = n.parentNode.z;
    int ny = 0;
    int left = 0;
    int right = 0;
    int dx = (x - px ) / Math.max(Math.abs(x-px), 1);
    int dz = (z - pz ) / Math.max(Math.abs(z-pz), 1);
    Node nd;
    boolean stairs = py != y;
    if(dx!=0 && dz!=0)//diagonal
      {      
      if(stairs)//moving vertical 
        {        
        searchingNodes.add( findOrMakeNode(currentNode.x-1, currentNode.y, currentNode.z));
        searchingNodes.add( findOrMakeNode(currentNode.x+1, currentNode.y, currentNode.z));
        searchingNodes.add( findOrMakeNode(currentNode.x, currentNode.y, currentNode.z-1));
        searchingNodes.add( findOrMakeNode(currentNode.x, currentNode.y, currentNode.z+1));

        /**
         * diagonals
         */    
        searchingNodes.add( findOrMakeNode( currentNode.x-1, currentNode.y, currentNode.z+1));
        searchingNodes.add( findOrMakeNode( currentNode.x-1, currentNode.y, currentNode.z-1));
        searchingNodes.add( findOrMakeNode( currentNode.x+1, currentNode.y, currentNode.z+1));
        searchingNodes.add( findOrMakeNode( currentNode.x+1, currentNode.y, currentNode.z-1));

        /**
         * up/down (in case of ladder/water)
         */
        searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y+1, currentNode.z));
        searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y-1, currentNode.z));

        /**
         * and the NSEW +/- 1 jumpable blocks..
         */
        searchingNodes.add( findOrMakeNode( currentNode.x-1, currentNode.y+1, currentNode.z));
        searchingNodes.add( findOrMakeNode( currentNode.x+1, currentNode.y+1, currentNode.z));
        searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y+1, currentNode.z-1));
        searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y+1, currentNode.z+1));

        searchingNodes.add( findOrMakeNode( currentNode.x-1, currentNode.y-1, currentNode.z));
        searchingNodes.add( findOrMakeNode( currentNode.x+1, currentNode.y-1, currentNode.z));
        searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y-1, currentNode.z-1));
        searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y-1, currentNode.z+1));
        }
      ny = isPathable(x, y, z+dz);
      if(ny>0)
        {
        left = ny;
        searchingNodes.add(nd = findOrMakeNode(x, ny, z+dz));
        nd.parentNode = n;
        nd.g = n.g + nd.getDistanceFrom(n);
        nd.f = nd.g+ nd.getH(goalNode);
        }
      ny = isPathable(x+dx,y,z);
      if(ny>0)
        {
        right = ny;
        searchingNodes.add(nd = findOrMakeNode(x+dx, ny, z));
        nd.parentNode = n;
        nd.g = n.g + nd.getDistanceFrom(n);
        nd.f = nd.g+ nd.getH(goalNode);
        }
      if(left!=0 || right !=0)
        {
        searchingNodes.add(nd = findOrMakeNode(x+dx, Math.max(left, right), z+dz));
        nd.parentNode = n;
        nd.g = n.g + nd.getDistanceFrom(n);
        nd.f = nd.g+ nd.getH(goalNode);
        }    
      if(left!=0)
        {
        if(isPathable(x-dx, py, z)==0)
          {
          searchingNodes.add(nd = findOrMakeNode(x-dx, left, z+dz));
          nd.parentNode = n;
          nd.g = n.g + nd.getDistanceFrom(n);
          nd.f = nd.g+ nd.getH(goalNode);
          }
        }
      if(right!=0)
        {
        if(isPathable(x, py, z-dz)==0)
          {          
          searchingNodes.add(nd = findOrMakeNode(x+dx, left, z-dz));
          nd.parentNode = n;
          nd.g = n.g + nd.getDistanceFrom(n);
          nd.f = nd.g+ nd.getH(goalNode);
          }
        }
      }
    else//going straight
      {
      if(dx==0)
        {
        ny = isPathable(x, y, z+dz);
        if(ny>0)
          {
          addToSearchNodes(x, ny, z+dz, n);
          }
        if(stairs)
          {
          addToSearchNodes(x+1, ny, z+dz, n);
          addToSearchNodes(x-1, ny, z+dz, n);
          }
        else
          {
          int nny = isPathable(x+1, ny, z);
          if(nny==0)
            {
            addToSearchNodes(x+1, ny, z+dz, n);
            }
          nny = isPathable(x-1, ny, z);
          if(nny==0)
            {
            addToSearchNodes(x-1, ny, z+dz, n);
            }
          }
        }
      else
        {
        ny = isPathable(x+dx,y,z);
        if(ny>0)
          {
          addToSearchNodes(x+dx, ny, z, n);
          if(stairs)
            {
            addToSearchNodes(x+dx, ny, z+1, n);
            addToSearchNodes(x+dx, ny, z-1, n);
            }
          else
            {
            int nny = isPathable(x, ny, z+1);
            if(nny==0)
              {
              addToSearchNodes(x+dx, ny, z+1,n);
              }
            nny = isPathable(x,ny,z-1);
            if(nny==0)
              {
              addToSearchNodes(x+dx, ny, z-1,n);
              }
            }
          }
        }
      }
    }
  else//get all neighbors
    {
    /**
     * search n,e,s,w
     */
    searchingNodes.add( findOrMakeNode(currentNode.x-1, currentNode.y, currentNode.z));
    searchingNodes.add( findOrMakeNode(currentNode.x+1, currentNode.y, currentNode.z));
    searchingNodes.add( findOrMakeNode(currentNode.x, currentNode.y, currentNode.z-1));
    searchingNodes.add( findOrMakeNode(currentNode.x, currentNode.y, currentNode.z+1));

    /**
     * diagonals
     */    
    searchingNodes.add( findOrMakeNode( currentNode.x-1, currentNode.y, currentNode.z+1));
    searchingNodes.add( findOrMakeNode( currentNode.x-1, currentNode.y, currentNode.z-1));
    searchingNodes.add( findOrMakeNode( currentNode.x+1, currentNode.y, currentNode.z+1));
    searchingNodes.add( findOrMakeNode( currentNode.x+1, currentNode.y, currentNode.z-1));

    /**
     * up/down (in case of ladder/water)
     */
    searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y+1, currentNode.z));
    searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y-1, currentNode.z));

    /**
     * and the NSEW +/- 1 jumpable blocks..
     */
    searchingNodes.add( findOrMakeNode( currentNode.x-1, currentNode.y+1, currentNode.z));
    searchingNodes.add( findOrMakeNode( currentNode.x+1, currentNode.y+1, currentNode.z));
    searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y+1, currentNode.z-1));
    searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y+1, currentNode.z+1));

    searchingNodes.add( findOrMakeNode( currentNode.x-1, currentNode.y-1, currentNode.z));
    searchingNodes.add( findOrMakeNode( currentNode.x+1, currentNode.y-1, currentNode.z));
    searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y-1, currentNode.z-1));
    searchingNodes.add( findOrMakeNode( currentNode.x, currentNode.y-1, currentNode.z+1));
    }
  Iterator<Node> it = searchingNodes.iterator();
  Node nd;
  while(it.hasNext())
    {
    nd = it.next();
    if(nd.obstacle)
      {
      it.remove();
      }
    }
  return searchingNodes;
  }

private void addToSearchNodes(int x, int y, int z, Node p)
  {
  Node nd= findOrMakeNode(x, y, z);
  searchingNodes.add(nd);
  nd.parentNode = p;
  nd.g = p.g + nd.getDistanceFrom(p);
  nd.f = nd.g+ nd.getH(goalNode);
  }

private Node jump(int nx, int ny, int nz, int px, int py, int pz)
  {
//  Config.logDebug("jumping: "+nx+","+ny+","+nz+","+px+","+py+","+pz);
  int x = nx;
  int y = ny;
  int z = nz;
  //float dist = currentNode.g + Trig.getDistance(x, y, z, currentNode.x, currentNode.y, currentNode.z);
  int dist = ((int)currentNode.g) + (Math.abs(x - currentNode.x) + Math.abs(y - currentNode.y) + Math.abs(z - currentNode.z));
  int dx = x - px;
  int dz = z - pz;
  Node nd;
  py = y;
  y = isPathable(x, py, z);
  if(y==0)
    {
    return null;
    }
  else if(goalNode.x == x && goalNode.z == z && goalNode.y == y)//maxSkip
    {    
    goalNode.parentNode = currentNode;
    return goalNode;
    }
  else if(dist >= 25.f)
    {
    nd = findOrMakeNode(x, y, z);
    nd.g = dist;
    nd.f = nd.g+ nd.getH(goalNode);
    nd.parentNode = currentNode;
    return nd;
    }
  int nxY = isPathable(x+dx, y, z);
  int nzY = isPathable(x,y,z+dz);
  if(dx!=0 && dz !=0)//diagonal
    {
    boolean a = isPathable(x-dx, y, z+dz) != 0 && isPathable(x-dx, py, z)==0;
    boolean b = isPathable(x+dx, y, z-dz) != 0 && isPathable(x, py, z-dz)==0;
    if(a||b)//diagonal forced neighbor check
      {
      nd = findOrMakeNode(x, y, z);
      nd.g = dist;
      nd.f = nd.g+ nd.getH(goalNode);
      nd.parentNode = currentNode;
      return nd;
      }
    }
  else
    {
    if(dx!=0)
      {      
      int check = (nxY!= 0) ? nxY : y;
      boolean a = isPathable(x, y, z+1)==0 && isPathable(x+dx, check, z+1)!=0;
      boolean b = isPathable(x, y, z-1)==0 && isPathable(x+dx, check, z-1)!=0;  
      if(check!=y || a || b)
        {
        nd = findOrMakeNode(x, y, z);
        nd.g = dist;
        nd.f = nd.g+ nd.getH(goalNode);
        nd.parentNode = currentNode;
        return nd;
        }      
      }
    if(dz!=0)
      {
      int check = (nzY != 0) ? nzY : z;///huh?? i think this needs to be y
      boolean a = isPathable(x+1,y,z)==0 && isPathable(x+1,check, z+dz)!=0;
      boolean b = isPathable(x-1,y,z)==0 && isPathable(x-1,check, z+dz)!=0;
      if(check!=y ||a||b)
        {
        nd = findOrMakeNode(x, y, z);
        nd.g = dist;
        nd.f = nd.g+ nd.getH(goalNode);
        nd.parentNode = currentNode;
        return nd;
        }
      }
    }
  
  if(dx!=0 && dz!=0)
    {
    Node jx = jump(x+dx, y, z, x,y,z);
    Node jz = jump(x,y,z+dz,x,y,z);
    if(jx!=null || jz!=null)
      {
      nd = findOrMakeNode(x, y, z);
      nd.g = dist;
      nd.f = nd.g+ nd.getH(goalNode);
      nd.parentNode = currentNode;
      return nd;
      }
    }
  if(nxY!=0 || nzY!=0)
    {
    if(x+dx==x && z+dz==z)
      {
      y += px< nx? 1 : -1;
      if(isPathable(x, y, z)==y)
        {
        nd = findOrMakeNode(x, y, z);
        nd.g = dist;
        nd.f = nd.g+ nd.getH(goalNode);
        nd.parentNode = currentNode;
        return nd;
        }
      else
        {
        return null;
        }       
      }
    return jump(x+dx,y,z+dz,x,y,z);
    }
  return null;
  }

/**
 * checks y-1 -> y+1 to see if any are pathable
 * @param x
 * @param y
 * @param z
 * @return
 */
private int isPathable(int x, int y, int z)
  {
  boolean top = world.isWalkable(x, y+1, z, currentNode);
  if(top)
    {
    return y+1;
    }
  boolean mid = world.isWalkable(x, y  , z, currentNode);
  if(mid)
    {
    return y;
    }
  boolean bot = world.isWalkable(x, y-1, z, currentNode);
  if(bot)
    {
    return y-1;
    }
  return 0;
  }

private void flushNodes()
  {
  this.nodeCache.addAll(allNodes);
  this.allNodes.clear();
  }

private Node findOrMakeNode(int x, int y, int z)
  {
  for(Node n : allNodes)
    {
    if(n.equals(x, y, z))
      {
      return n;
      }
    }
  Node n = null;
  if(nodeCache.isEmpty())
    {
    n = new Node(x,y,z);
    n.calcTraveCost(world, currentNode);
    }
  else
    {
    n = nodeCache.pop();   
    n.obstacle = false;
    n.closed = false;
    n.parentNode = null;
    n.x = x;
    n.y = y;
    n.z = z;
    n.g = 0;
    n.f = 0;
    n.calcTraveCost(world, currentNode);
    }  
  allNodes.add(n);
  return n;
  }

}
