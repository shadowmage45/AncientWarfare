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

import shadowmage.ancient_warfare.common.config.Config;

public class PathFinderThread implements Runnable
{

boolean working = false;
boolean finished = false;
boolean shouldInterrupt = false;
boolean stoppedEarly = false;

PathWorldAccess world;

Node startNode;
Node goalNode;
Node currentNode;

Node bestFoundEndNode;
float bestFoundDistance;
float bestPathLength;
float searchLength;
int nodesSearched = 0;

IPathableCallback caller;

PriorityQueue<Node> qNodes = new PriorityQueue<Node>();
List<Node> allNodes = new ArrayList<Node>();
List<Node> searchingNodes = new ArrayList<Node>();
private LinkedList<Node> nodeCache = new LinkedList<Node>();
private LinkedList<Node> foundPath = new LinkedList<Node>();

@Override
public void run()
  {
  long t = System.nanoTime();
  working = true;
  finished = false;
  shouldInterrupt = false;  
  this.nodesSearched = 0;
  search();
  t = System.nanoTime() - t;
  Config.logDebug("PATH TIME NANOS: "+t+" nodes directly examined: "+nodesSearched);
  this.world = null; 
  if(stoppedEarly)
    {
    Config.logDebug("stopped early");
    }  
  Node n = goalNode;  
  while(n!=null)
    {    
    foundPath.push(n);
    n = n.parentNode;
    }  
  this.goalNode = null;
  this.currentNode = null;
  this.bestFoundEndNode = null;
  this.bestFoundDistance = 0;
  this.bestPathLength = 0; 
  this.finished = true;
  if(!shouldInterrupt)
    {
    this.caller.onPathFound(foundPath);
    }
  PathThreadManager.instance(!this.world.isRemote()).onThreadFinished(this);
  this.working = false;
  this.caller = null;
  }

/**
 * must call this to setup path params, and then call run() to actually initiate search
 * @param world
 * @param x
 * @param y
 * @param z
 * @param x1
 * @param y1
 * @param z1
 * @param searchRange
 */
public void findPath(PathWorldAccess world, int x, int y, int z, int x1, int y1, int z1, int searchRange)
  {
  this.world = world;
  this.foundPath.clear();
  searchLength = searchRange;
  setupInitialNodes(x, y, z, x1, y1, z1, searchRange);   
  }

public void setPreviousPathEndNode(Node n)
  {
  this.bestFoundEndNode = n;
  this.bestFoundDistance = this.bestFoundEndNode.getDistanceFrom(goalNode);
  this.bestPathLength = n.getPathLength();
  }

public void setupInitialNodes(double x, double y, double z, double x1, double y1, double z1, int searchRange)
  {
  this.qNodes.clear();
  this.flushNodes();
  nodesSearched = 0;
  this.startNode = findOrMakeNode((int)x, (int)y, (int)z);
  this.goalNode = findOrMakeNode((int)x1, (int)y1, (int)z1);
  startNode.g = 0;
  startNode.f = startNode.getH(goalNode);
  this.qNodes.add(startNode);
  this.currentNode = startNode;
  this.bestFoundEndNode = currentNode;
  this.bestFoundDistance = this.bestFoundEndNode.getDistanceFrom(goalNode);
  this.bestPathLength = 0;
  this.finished = false;
  this.stoppedEarly = false;
  }

public void search()
  {
  this.nodesSearched = 0;
  while(!qNodes.isEmpty())
    {
    this.nodesSearched++;
    Thread.yield();
    currentNode = qNodes.poll();
    if(currentNode.equals(goalNode))
      {
      Config.logDebug("hit GOAL NODE");
      finished = true;
      break;
      }    
    currentNode.closed = true;
    searchNeighbors();
    if(this.nodesSearched>=2000)
      {
      Config.logDebug("nodes searched>2000");
//      this.shouldInterrupt = true;
      }
    if(this.stoppedEarly)
      {
      break;
      }
    if(this.shouldInterrupt)
      {
      this.goalNode = this.bestFoundEndNode;
      break;       
      }
    }  
  }

private boolean shouldTerminateEarly()  
  {
  float dist = this.currentNode.getDistanceFrom(goalNode);
  float len = this.currentNode.getPathLength();
  if((dist < bestFoundDistance || len > bestPathLength ))
    {//
    this.bestFoundEndNode = this.currentNode;
    this.bestFoundDistance = dist;
    this.bestPathLength = len;
//    Config.logDebug("found new best end point. pathLen: "+this.bestFoundEndNode.getPathLength() + "rawDist: "+dist+ " ND: "+bestFoundEndNode.toString());
    if(len>searchLength)
      {
//      Config.logDebug("search length exceeded, terminating search");      
      return true;
      }
    }
  return false;
  }

public void searchNeighbors()
  {
  if(this.shouldTerminateEarly())
    {
    this.stoppedEarly = true;
    this.goalNode = this.currentNode;
    return;
    }
  
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

  float tent;
  for(Node node : searchingNodes)
    {     
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
