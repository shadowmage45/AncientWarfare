/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Optimized A* Using node-caching for all current in-play nodes, to try and limit object creation and recalculation of variables
 * @author Shadowmage
 *
 */
public class PathFinderAStar
{
/**
 * A STAR PSUEDOCODE ********************************************************************
 * LISTS : OPEN, CLOSED
 * ADD START NODE TOP OPEN-LIST
 * WHILE OPEN-LIST IS NOT EMPTY
 *    CURRENT = OPEN-LIST.LOWEST-F
 *    IF CURRENT == GOAL THEN END
 *    ADD CURRENT TO CLOSED LIST
 *    LIST NEIGHBORS = CURRENT.GETNEIGHBORS
 *      FOR N IN NEIGHBORS
 *        IF N.CLOSED && C.G + DIST(C,N) > N.G (new path to neighbor is longer than the neighbors current path)
 *          CONTINUE
 *        ELSE IF FRESH NODE OR C.G + DIST(C,N) < N.G (better path found to already-examined node)
 *          N.G = C.G + DIST(C,N)
 *          N.P = C
 *          N.F = N.G + N.H(GOAL)
 *          IF N.CLOSED
 *            REMOVE N FROM CLOSED LIST
 *          IF N NOT IN OPEN SET
 *            ADD TO OPEN SET  
 * **************************************************************************************
 */


/**
 * OPEN-LIST
 */
private PriorityQueue<Node> qNodes = new PriorityQueue<Node>();

/**
 * CLOSED-LIST
 */
private ArrayList<Node> allNodes = new ArrayList<Node>();

/**
 * cache to old garbage nodes, to reduce new-object creation
 */
private LinkedList<Node> nodeCache = new LinkedList<Node>();

/**
 * current-node neighbors
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
PathWorldAccess world;

public List<Node> findPath(PathWorldAccess world, int x, int y, int z, int tx, int ty, int tz, int maxRange)
  {
  this.world = world;
  this.sx = x;
  this.sy = y;
  this.sz = z;
  this.tx = tx;
  this.ty = ty;
  this.tz = tz;    
  this.currentNode = getOrMakeNode(sx, sy, sz, null);
  this.currentNode.g = 0;
  this.currentNode.f = this.currentNode.getH(tx, ty, tz);
  this.qNodes.offer(this.currentNode);
  this.searchLoop();
  LinkedList<Node> path = new LinkedList<Node>();
  Node n = this.currentNode;
  Node c = null;
  Node p = null;
  while(n!=null)
    {
    p = c;
    c = new Node(n.x, n.y, n.z);
    c.parentNode = p;
    path.push(c);
//    Config.logDebug(c.toString());
    n = n.parentNode;
    }
  this.currentNode = null;
  this.world = null;    
  this.allNodes.clear();
  this.qNodes.clear();
  this.searchNodes.clear();
  return path;
  }

private void searchLoop()
  {
  while(!qNodes.isEmpty())
    {
    this.currentNode = this.qNodes.poll();
    this.allNodes.add(currentNode);
    if(currentNode.equals(tx, ty, tz))
      {
//      Config.logDebug("goal hit");     
      break;
      }    
    currentNode.closed = true;    
    this.findNeighbors(currentNode);
    float tent;
    /**
     * *        IF N.CLOSED && C.G + DIST(C,N) > N.G (new path to neighbor is longer than the neighbors current path)
 *          CONTINUE
 *        ELSE IF FRESH NODE OR C.G + DIST(C,N) < N.G (better path found to already-examined node)
 *          N.G = C.G + DIST(C,N)
 *          N.P = C
 *          N.F = N.G + N.H(GOAL)
 *          IF N.CLOSED
 *            REMOVE N FROM CLOSED LIST
 *          IF N NOT IN OPEN SET
 *            ADD TO OPEN SET  
     */
    for(Node n : this.searchNodes)
      {  
      tent = currentNode.g + currentNode.getDistanceFrom(n);
      if(n.closed && tent > n.g)//new path from this node to already examined node is longer than its current path, disregard
        {
        continue;
        }
      if(!qNodes.contains(n) || tent < n.g)
        {
        n.parentNode = currentNode;
        n.g = tent;
        n.f = n.g + n.getH(tx, ty, tz);
        if(!qNodes.contains(n))
          {
          qNodes.offer(n);
          }
        n.closed = false;
        }      
      }
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
    searchNodes.add(getOrMakeNode(x, y, z, p));
    }
  }

/**
 * 
 */
private Node getOrMakeNode(int x, int y, int z, Node p)
  {
  Node n = null;
  for(Node c : this.allNodes)
    {
    if(c.equals(x, y, z))
      {
//      Config.logDebug("getting node from allNodes "+c.toString());
      return c;
      }
    }
  n = new Node(x,y,z);
  if(p!=null)
    {
    n.parentNode = p;
    n.g = p.g + n.getDistanceFrom(p);
    n.f = n.g + n.getDistanceFrom(tx, ty, tz);
    }  
//  Config.logDebug("adding new/cached node to allNodes "+n.toString());
  allNodes.add(n);
  return n;
  }

}
