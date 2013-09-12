/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.civics.worksite.te.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.pathfinding.Node;

public class TreeFinder
{

List<Node> allNodes = new ArrayList<Node>();
LinkedList<Node> openList = new LinkedList<Node>();
List<Node> badNodes = new ArrayList<Node>();
List<Node> foundNodes = new ArrayList<Node>();
List<Node> searchNodes = new ArrayList<Node>(); 
Node testNode = new Node(0,0,0);

/**
 * currently un-used nodes, to reduce memory churn
 */
List<Node> cache = new ArrayList<Node>();

int id = 0;
int meta = 0;

/**
 * 
 */
public TreeFinder()
  {
  // TODO Auto-generated constructor stub
  }

public List<Node> findTreeNodes(World world, int x, int y, int z, int blockID, int blockMeta)
  {
  id = blockID;
  meta = blockMeta;
  openList.add(getNode(x, y, z));  
  Node toCheck;
  while(!openList.isEmpty())
    {
    searchNodes.clear();
    toCheck = openList.poll();
    this.addNeighborNodes(world, toCheck.x, toCheck.y, toCheck.z);
    for(Node n : searchNodes)
      {
      if(!openList.contains(n) && !badNodes.contains(n))
        {
        openList.add(n);
        }
      }
    }  
  List<Node> foundNodes = new ArrayList<Node>();
  for(Node n : this.foundNodes)
    {
    foundNodes.add(new Node(n.x, n.y, n.z));
    }
  cache.addAll(this.foundNodes);
  cache.addAll(badNodes);
  cache.addAll(openList);
  cache.addAll(searchNodes);
  this.foundNodes.clear();
  this.badNodes.clear();
  this.openList.clear();
  this.searchNodes.clear();
  return Collections.emptyList();  
  }

static int[][] offsets = new int[17][3];

static
{
offsets[0] = new int[]{-1,0,0};
offsets[1] = new int[]{+1,0,0};
offsets[2] = new int[]{0,0,-1};
offsets[3] = new int[]{0,0,+1};
offsets[4] = new int[]{-1,0,-1};
offsets[5] = new int[]{-1,0,+1};
offsets[6] = new int[]{+1,0,-1};
offsets[7] = new int[]{+1,0,+1};
offsets[8] = new int[]{-1,1,0};
offsets[9] = new int[]{+1,1,0};
offsets[10] = new int[]{0,1,-1};
offsets[11] = new int[]{0,1,+1};
offsets[12] = new int[]{-1,1,-1};
offsets[13] = new int[]{-1,1,+1};
offsets[14] = new int[]{+1,1,-1};
offsets[15] = new int[]{+1,1,+1};
offsets[16] = new int[]{0,1,0};
}

protected void addNeighborNodes(World world, int x, int y, int z)
  {
  searchNodes.clear();
  int[] offset;
  for(int i =0; i < offsets.length; i++)
    {
    offset = offsets[i];
    if(isTree(world, x+offset[0], y+offset[1], z+offset[2]))
      {
      this.foundNodes.add(new Node(x+offset[0], y+offset[1], z+offset[2]));
      }
    }
  }

protected boolean isTree(World world, int x, int y, int z)
  {
  if(world.getBlockId(x, y, z)==id && world.getBlockMetadata(x, y, z)==meta)
    {
    return true;
    }
  return false;
  }

protected Node getNode(int x, int y, int z)
  {
  if(!cache.isEmpty())
    {
    return cache.remove(0).reassign(x, y, z);
    }
  return new Node(x, y, z);
  }

}
