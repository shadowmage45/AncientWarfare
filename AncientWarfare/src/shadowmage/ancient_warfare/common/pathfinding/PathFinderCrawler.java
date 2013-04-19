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
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class PathFinderCrawler
{

/**
 * OPEN-LIST
 */
private PriorityQueue<Node> qNodes = new PriorityQueue<Node>();

/**
 * a list of all working-set nodes, both open and closed.  used to prevent spurious object creation
 * as well as keep already visited but closed nodes scores valid and cached, as well as for pulling
 * live nodes from the 'open-list' without having to manually synch them back in/update values
 */
private ArrayList<Node> allNodes = new ArrayList<Node>();

/**
 * current-node neighbors, just a cached list..
 */
private ArrayList<Node> searchNodes = new ArrayList<Node>();

private Node currentNode;

/**
 * start and target points
 */
int sx;
int sy;
int sz;
int tx;
int ty;
int tz;
int minx;
int miny;
int minz;
int maxx;
int maxy;
int maxz;
int searchBufferRange = 10;
int maxRange = 80;
PathWorldAccess world;
long startTime;
public long maxRunTime = 15000000l;//15ms, default time..public so may be overriden at run-time...must be reset between runs
public long maxSearchIterations = 600;
public boolean quickStop = false;
private Node bestEndNode = null;
private float bestPathLength = 0.f;
private float bestPathDist = Float.POSITIVE_INFINITY;
private int searchIteration;

public List<Node> findPath(PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz, int maxRange)
  {  
  this.world = world;
  this.sx = x;
  this.sy = y;
  this.sz = z;
  this.tx = tx;
  this.ty = ty;
  this.tz = tz;
  this.maxRange = maxRange;  
  minx = x < tx? x : tx;
  maxx = x < tx? tx : x;
  miny = y < ty? y : ty;
  maxy = y < ty? ty : y;
  minz = z < tz? z : tz;
  maxz = z < tz? tz : z;
  minx-=searchBufferRange;
  maxx+=searchBufferRange;
  miny-=searchBufferRange;
  maxy+=searchBufferRange;
  minz-=searchBufferRange;
  maxz+=searchBufferRange;
  this.startTime = System.nanoTime();  
  this.currentNode = getOrMakeNode(sx, sy, sz, null);
  this.currentNode.g = 0;
  this.currentNode.f = this.currentNode.getH(tx, ty, tz);
  this.qNodes.offer(this.currentNode);
  this.bestEndNode = this.currentNode;
  this.bestPathLength = 0;
  this.bestPathDist = Float.POSITIVE_INFINITY;
  this.searchLoop();
  LinkedList<Node> path = new LinkedList<Node>();
  Node n = this.currentNode;
  Node c = null;
  Node p = null;
//  Config.logDebug("theta path:");
  while(n!=null)
    {
    p = c;
    c = new Node(n.x, n.y, n.z);
    c.parentNode = p;
    path.push(c);
//    Config.logDebug(c.toString());
    n = n.parentNode;
    }
//  Config.logDebug("end-theta path:");
  this.currentNode = null;
  this.world = null; 
  this.bestEndNode = null;
  this.allNodes.clear();
  this.qNodes.clear();
  this.searchNodes.clear();
  return path;
  }

protected void searchLoop()
  {
  while (currentNode.x!=tx || currentNode.y!=ty || currentNode.z !=tz)
    {
    
    
    }
  }

protected boolean canMoveDiagonal(Node c, int dx, int dz)
  {
  return true;
  }

private Node getOrMakeNode(int x, int y, int z, Node p)
  {
  Node n = null;
  for(Node c : this.allNodes)
    {
    if(c.equals(x, y, z))
      {
      return c;
      }
    }
  n = new Node(x,y,z);
  if(p!=null)
    {
    n.travelCost = world.getTravelCost(x, y, z);
    n.parentNode = p;
    n.g = p.g + n.getDistanceFrom(p)+n.travelCost;
    n.f = n.g + n.getDistanceFrom(tx, ty, tz);
    }  
  allNodes.add(n);
  return n;
  }
}
