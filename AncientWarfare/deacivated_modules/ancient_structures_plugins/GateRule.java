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
package shadowmage.ancient_structures.common.structures.data.rules;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import shadowmage.ancient_framework.common.utils.BlockPosition;
import shadowmage.ancient_framework.common.utils.BlockTools;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.ancient_structures.common.structures.data.ProcessedStructure;
import buildcraft.transport.Gate;

public class GateRule
{

int teamNum = 0;
int gateType = 0;
int facing = 0;
int x1;
int y1;
int z1;
int x2;
int y2;
int z2;

public static GateRule populateRule(ScannedGateEntry g)
  {
  GateRule rule = new GateRule();
  rule.x1 = g.pos1.x;
  rule.y1 = g.pos1.y;
  rule.z1 = g.pos1.z;
  rule.x2 = g.pos2.x;
  rule.y2 = g.pos2.y;
  rule.z2 = g.pos2.z;  
  rule.teamNum = g.g.teamNum;
  rule.gateType = g.g.getGateType().getGlobalID();
  rule.facing = g.facing;
  return rule;
  }

public static GateRule parseRule(List<String> lines)
  {
  GateRule rule = new GateRule();
  for(String t : lines)
    {
    if(t.toLowerCase().startsWith("type"))
      {
      rule.gateType = StringTools.safeParseInt("=", t);
      }   
    else if(t.toLowerCase().startsWith("pos1"))
      {
      int[] pos = StringTools.safeParseIntArray("=", t);
      rule.x1 = pos[0];
      rule.y1 = pos[1];
      rule.z1 = pos[2];
      }
    else if(t.toLowerCase().startsWith("pos2"))
      {
      int[] pos = StringTools.safeParseIntArray("=", t);
      rule.x2 = pos[0];
      rule.y2 = pos[1];
      rule.z2 = pos[2];
      }
    else if(t.toLowerCase().startsWith("team"))
      {
      rule.teamNum = StringTools.safeParseInt("=", t);
      }
    else if(t.toLowerCase().startsWith("facing"))
      {
      rule.facing = StringTools.safeParseInt("=", t);
      }
    }
  return rule;
  }

public List<String> getRuleLines()
  {
  List<String> lines = new ArrayList<String>();
  lines.add("gate:");
  lines.add("pos1="+StringTools.getCSVStringForArray(new int[]{x1,y1,z1}));
  lines.add("pos2="+StringTools.getCSVStringForArray(new int[]{x2,y2,z2}));
  lines.add("team="+String.valueOf(teamNum));
  lines.add("type="+String.valueOf(this.gateType));
  lines.add("facing="+String.valueOf(facing));
  lines.add(":endgate");
  return lines;
  }

public Entity getEntityToSpawn(World world, int facing, ProcessedStructure struct, BlockPosition buildPos, int teamNum)
  {  
  BlockPosition p1 = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(x1-struct.xOffset,y1-struct.verticalOffset, z1-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  BlockPosition p2 = BlockTools.getTranslatedPosition(buildPos, new BlockPosition(x2-struct.xOffset,y2-struct.verticalOffset, z2-struct.zOffset), facing, new BlockPosition(struct.xSize, struct.ySize, struct.zSize));
  
  
  
  EntityGate gate = Gate.constructGate(world, p1, p2, Gate.getGateByID(gateType), (byte) ((this.facing + BlockTools.getRotationAmount(2, facing)) % 4));
  if(teamNum>=0 && teamNum < 16)
    {
    gate.teamNum = teamNum;
    }
  return gate;
  }

}
