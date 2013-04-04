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

import java.util.LinkedList;
import java.util.List;

public class EntityPath
{

private LinkedList<Node> path = new LinkedList<Node>();

public EntityPath()
  {
  
  }

public void setPath(List<Node> pathNodes)
  {
  this.path.clear();
  this.addPath(pathNodes); 
  }

/**
 * adds nodes onto a path, towards a new/updated target
 * @param pathNodes
 */
public void addPath(List<Node> pathNodes)
  {  
  this.path.addAll(pathNodes);
//  Node prev = null;
//  Node current = null;
//  for(Node n : pathNodes)
//    {
//    prev = current;
//    current = new Node(n.x, n.y, n.z);
//    current.f = n.f;
//    current.g = n.g;
//    current.travelCost = n. travelCost;
//    current.parentNode = prev;
//    if(prev!=null)
//      {
//      prev.childNode = current;
//      }
//    this.path.add(current);
//    } 
  }

public boolean containsPoint(int x, int y, int z)
  {
  for(Node n : this.path)
    {
    if(n.equals(x, y, z))
      {
      return true;
      }
    }
  return false;
  }

public Node getEndNode()
  {
  return this.path.peekLast();
  }

/**
 * return the first node (does not remove)
 * @return
 */
public Node getFirstNode()
  {
  return this.path.peekFirst();
  }

/**
 * return and remove the first node
 * @return
 */
public Node claimNode()
  {
  return this.path.poll();
  }

public int getPathNodeLength()
  {
  return this.path.size();
  }

public float getPathLength()
  {
  if(this.path.isEmpty())
    {
    return 0;
    }
  return this.path.get(path.size()-1).getPathLength();
  }

public List<Node> getPath()
  {
  return path;
  }

}
