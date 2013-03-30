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
import java.util.HashMap;
import java.util.List;

public class NodeMap
{

private HashMap<Integer, NodeXLevel> nodeMap = new HashMap<Integer, NodeXLevel>();

private List<Node> nodeList = new ArrayList<Node>();

private Node getNodeFromList(PathWorldAccess world, int x, int y, int z)
  {
  for(Node nd : nodeList)
    {
    if(nd.equals(x, y, z))
      {     
      return nd;
      }
    }
  Node nd = new Node(x,y,z);
  this.nodeList.add(nd);
  return nd;
  }

public Node getOrMakeNode(PathWorldAccess world, int x, int y, int z, Node parent, Node goal)
  {
  return getNodeFromList(world, x, y, z);
//  Node node = this.getNode(x, y, z);
//  if(node==null)
//    {
//    node = new Node(x, y, z);
//    if(!this.nodeMap.containsKey(x))
//      {
//      this.nodeMap.put(x, new NodeXLevel());
//      }
//    if(!this.nodeMap.get(x).yLevels.containsKey(y))
//      {
//      this.nodeMap.get(x).yLevels.put(y, new NodeYLevel());
//      }
//    this.nodeMap.get(x).yLevels.get(y).zLevels.put(z, node);
//    }  
//  return node;
  }

public Node getNode(int x, int y, int z)
  {
  if(this.nodeMap.containsKey(x))
    {
    return this.nodeMap.get(x).get(y, z);
    }
  return null;
  }

public void putNode(int x, int y, int z, Node node)
  {
  if(!this.nodeMap.containsKey(x))
    {
    this.nodeMap.put(x, new NodeXLevel());
    }
  this.nodeMap.get(x).put(y, z, node);
  }

public void clear()
  {
  this.nodeMap.clear();
  this.nodeList.clear();
  }

private class NodeXLevel
{
private HashMap<Integer, NodeYLevel> yLevels = new HashMap<Integer, NodeYLevel>();

public Node get(int y, int z)
  {
  if(this.yLevels.containsKey(y))
    {
    return this.yLevels.get(y).get(z);
    }
  return null;
  }

public void put(int y, int z, Node node)
  {
  if(!this.yLevels.containsKey(y))
    {
    this.yLevels.put(y, new NodeYLevel());
    }
  this.yLevels.get(y).put(z, node);
  }

}

private class NodeYLevel
{
private HashMap<Integer, Node> zLevels = new HashMap<Integer, Node>();
public Node get(int z)
  {
  return this.zLevels.get(z);
  }

public void put(int z, Node node)
  {
  this.zLevels.put(z, node);
  }
}


}
