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

public class PathFinderJump
{


boolean finished = false;
boolean foundPath = true;
PathWorldAccess world;

Node startNode;
Node goalNode;
Node currentNode;

PriorityQueue<Node> qNodes = new PriorityQueue<Node>();
//List<Node> closedNodes = new ArrayList<Node>();
List<Node> allNodes = new ArrayList<Node>();

private LinkedList<Node> nodeCache = new LinkedList<Node>();


public List<Node> findPath(PathWorldAccess world, double x, double y, double z, double x1, double y1, double z1, int searchRange)
  {
  long t1 = System.nanoTime();
  this.world = world;
  setupInitialNodes(x, y, z, x1, y1, z1, searchRange);
 
  search();
    
  //useJump ? jumpSearch() : search();
//  search();
  this.world = null;  
  long t2 = System.nanoTime();
  Config.logDebug("pathGen time nanos: "+ (t2-t1));
  if(!foundPath)
    {
    Config.logDebug("could not find path!!!");
    }
  LinkedList<Node> path = new LinkedList<Node>();
  Node n = goalNode;  
  while(n!=null)
    {    
    path.push(n);
    n = n.parentNode;
    }
  for(Node node : path)
    {
    Config.logDebug(node.toString());
    }
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
  this.finished = false;
  foundPath = true;
  }

public void search()
  {
  while(!qNodes.isEmpty())
    {
//    Config.logDebug("MOVING----------------------");
//    Config.logDebug("from: "+currentNode.toString());
    currentNode = qNodes.poll();
//    Config.logDebug("to: "+currentNode.toString());
//    Config.logDebug("STOPPED----------------------");
    if(currentNode.equals(goalNode))
      {
      Config.logDebug("hit GOAL NODE");
      finished = true;
      return;
      }    
    currentNode.closed = true;
    searchNeighbors();
    }
  foundPath = false;
  finished = true;
  }

List<Node> searchingNodes = new ArrayList<Node>();

public void searchNeighbors()
  { 
  searchingNodes.clear(); 
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
  
  cullNeighbors();
  
  float tent;
  for(Node node : searchingNodes)
    { 
    node.calcTraveCost(world, currentNode);
    if(node.obstacle)
      {
      node.closed = true;
      continue;
      }
    tent = currentNode.g + currentNode.getDistanceFrom(node);
    if(node.closed)
      {      
      if(tent>node.g)
        {
        continue;
        }
      }
    if(!qNodes.contains(node) || tent < node.g)
      {
      node.parentNode = currentNode;
      node.g = tent;
      node.f = node.g + node.getH(goalNode);
      if(!qNodes.contains(node))
        {
        qNodes.offer(node);
        }
      node.closed = false;
      }
    } 
  }

private void cullNeighbors()
  {
  Iterator<Node> it = searchingNodes.iterator();
  Node n;
  int dx;
  int dz;  
  int gx;
  int gz;
  boolean directPathBlocked = false;
  while(it.hasNext())
    {
    n = it.next();
    if(!world.isWalkable(n.x, n.y, n.z, currentNode))
      {
      n.obstacle = true;
      n.closed = true;
      it.remove();
      continue;
      }
    dx = currentNode.x-n.x;
    dz = currentNode.z-n.z;
    gx = currentNode.x - goalNode.x;
    gz = currentNode.z - goalNode.z;
    gx = gx>1 ? 1 : gx < -1 ? -1 : 0;
    gz = gz>1 ? 1 : gz < -1 ? -1 : 0;
    directPathBlocked = world.isWalkable(gx, currentNode.y, gz, currentNode);
    
    if(directPathBlocked)//allow more nodes
      {
//      Config.logDebug("direct path blocked, allowing all nodes");
      }
    else
      {
//      Config.logDebug("attempting cull of: "+n.toString());
//      Config.logDebug("moving from: "+currentNode.toString());
//      Config.logDebug("x,z: "+dx+","+dz+"  gx, gz: "+gx+","+gz);
      if(dx!=gx && dz!=gz)
        {
       
        //it.remove();
        continue;
        }
      else
        {
        //n.closed = false;
//        Config.logDebug("keeping node");
        }
      }
    }
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
    }
  else
    {
    n = nodeCache.pop();   
    n.obstacle = false;
    n.closed = false;
    n.x = x;
    n.y = y;
    n.z = z;
    n.g = 0;
    n.f = 0;
    }  
  allNodes.add(n);
  return n;
  }

}
