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
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.pathfinding.Node;
import shadowmage.ancient_warfare.common.utils.Trig;

public class TreeFinder
{

LinkedList<Node> openList = new LinkedList<Node>();
List<Node> badNodes = new ArrayList<Node>();
List<Node> foundNodes = new ArrayList<Node>();

/**
 * currently un-used nodes, to reduce memory churn
 */
List<Node> cache = new ArrayList<Node>();

int id = 0;
int meta = 0;
int x;
int y;
int z;

public static TreeFinder instance(){return INSTANCE;}
private static TreeFinder INSTANCE = new TreeFinder();
private TreeFinder(){}

public List<Node> findTreeNodes(World world, int x, int y, int z, int blockID, int blockMeta)
  {
  Config.logDebug("doing treefind run at: "+x+","+y+","+z);
  this.x = x;
  this.y = y;
  this.z = z;
  this.id = blockID;
  this.meta = blockMeta;
  
  Node start = getNode(x,y,z);
  foundNodes.add(start);
  openList.add(start);  
  
  Node toCheck;
  while(!openList.isEmpty())
    {
    toCheck = openList.poll();
    foundNodes.add(toCheck);
    this.addNeighborNodes(world, toCheck.x, toCheck.y, toCheck.z);
    }  
  
  List<Node> foundNodes = new ArrayList<Node>();
  Config.logDebug("tree finder-------------");
  for(Node n : this.foundNodes)
    {
    Config.logDebug("found nodes: "+n);
    foundNodes.add(new Node(n.x, n.y, n.z));
    }
  
  cache.addAll(this.foundNodes);
  cache.addAll(badNodes);
  cache.addAll(openList);
  this.foundNodes.clear();
  this.badNodes.clear();
  this.openList.clear();
  return foundNodes;  
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
  int[] offset;
  int x1,y1,z1;
  for(int i =0; i < offsets.length; i++)
    {
    offset = offsets[i];
    x1 = x+offset[0];
    y1 = y+offset[1];
    z1 = z+offset[2];
    if(!Trig.isBetween(x1, this.x-4, this.x+4) || !Trig.isBetween(z1, this.z-4, this.z+4) || !Trig.isBetween(y1, this.y, this.y+16))
      {
      continue;
      }
    Node n = getNode(x1, y1, z1);
    if(!badNodes.contains(n) && !openList.contains(n) &&!foundNodes.contains(n))
      {
      if(isTree(world, n))
        {      
        openList.add(n);
        }
      else
        {
        badNodes.add(n);
        }
      }
    }
  }

protected boolean isTree(World world, Node n)
  {
  if(world.getBlockId(n.x, n.y, n.z)==id && (world.getBlockMetadata(n.x, n.y, n.z) & 3)==meta)
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
