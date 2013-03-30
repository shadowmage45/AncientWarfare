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


public class PathWorldAccessTest extends PathWorldAccess
{

int[][][] worldChunk = new int [100][10][100];

/**
 * @param world
 */
public PathWorldAccessTest()
  {
  super(null);
  for(int x = 0; x < 100; x++)
    {
    for(int z = 0; z < 100; z++)
      {
      worldChunk[x][0][z] = 1;
      }
    }  
  for(int x = 3; x<30 ; x++)
    {
    worldChunk[x][1][3] = 1;
    worldChunk[x][2][3] = 1;
    worldChunk[x][1][20] = 1;
    worldChunk[x][2][20] = 1;
    }  
  for(int z = 3; z<20 ; z++)
    {
    worldChunk[3][1][z] = 1;
    if(z!=0)
      {
      worldChunk[3][2][z] = 1;
      }
//    worldChunk[1][2][z] = 1;
    }
  }

@Override
public int getBlockId(int x, int y, int z)
  {
  if(x<0 || x>=100 || y < 0 || y >=10 || z< 0 || z>=100)
    {
    return 1;
    }
  return worldChunk[x][y][z];
  }

}
