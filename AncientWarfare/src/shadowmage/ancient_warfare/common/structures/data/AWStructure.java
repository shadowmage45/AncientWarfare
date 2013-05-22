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
package shadowmage.ancient_warfare.common.structures.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import shadowmage.ancient_warfare.common.structures.build.Builder;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.structures.data.rules.CivicRule;
import shadowmage.ancient_warfare.common.structures.data.rules.EntityRule;
import shadowmage.ancient_warfare.common.structures.data.rules.GateRule;
import shadowmage.ancient_warfare.common.structures.data.rules.InventoryRule;
import shadowmage.ancient_warfare.common.structures.data.rules.NpcRule;
import shadowmage.ancient_warfare.common.structures.data.rules.SwapRule;
import shadowmage.ancient_warfare.common.structures.data.rules.VehicleRule;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.IDPairCount;
import shadowmage.ancient_warfare.common.world_gen.WorldGenStructureEntry;

public abstract class AWStructure
{

/**
 * used to determine if the structure can be edited, (won't allow editing if there are known builders using
 * the struct)
 */
private Set<Builder> openBuilders = new HashSet<Builder>();

/**
 * if it is being edited....
 */
private List<String> openEditors = new ArrayList<String>();

private WorldGenStructureEntry worldGen;

/**
 * mostly file IO stuff...
 */
public String md5;
public String filePath;
public String name = "";

/**
 * is this structure available in survival-mode drafting station (should have a valid resourceList)
 */
public boolean survival = false;

/**
 * advanced world-gen settings (can spawn underground, in water/lava, partially underground, etc..)
 */
public boolean underground = false;
public int undergroundMinLevel=1;
public int undergroundMaxLevel=255;
public int undergroundMaxAirAbove = 0;
public int minSubmergedDepth = 0;
public int maxWaterDepth = 0;
public int maxLavaDepth = 0;

/**
 * preservation flags for entire structure
 */
public boolean preserveWater = false;
public boolean preserveLava = false;
public boolean preservePlants = false;
public boolean preserveBlocks = false;

/**
 * individual blockRules, will override structure rules for individual blocks
 * (incl advanced feature not supported by Ruins--per block preserve info)
 */
public Map<Integer, BlockRule> blockRules = new HashMap<Integer, BlockRule>();
public ArrayList<VehicleRule> vehicleRules = new ArrayList<VehicleRule>();
public ArrayList<NpcRule> NPCRules = new ArrayList<NpcRule>();
public Map<Integer, SwapRule> swapRules = new HashMap<Integer, SwapRule>();
public ArrayList<EntityRule> entityRules = new ArrayList<EntityRule>();
public ArrayList<CivicRule> civicRules = new ArrayList<CivicRule>();
public Map<Integer, InventoryRule> inventoryRules = new HashMap<Integer, InventoryRule>();
public ArrayList<GateRule> gateRules = new ArrayList<GateRule>();

/**
 * only set to false for bad values during parsing, struct is then discarded and not loaded into structures map
 */
public boolean isValid = true;

/**
 * if resourceList has been calculated or loaded from disk, this will not be null...
 */
public  List<IDPairCount> cachedCounts = null;

/**
 * structure biome settings
 */

public String[] biomesOnlyIn;
public String[] biomesNotIn;

/**
 * array of ruleID references making up this structure
 * these refer to the key in the blockRules map
 * this array basically holds the levels from the ruins template
 */
public short [][][] structure;

/**
 * how many blocks may be non-solid below this structure
 */
private int maxOverhang;

/**
 * how many blocks vertically above base may be cleared 
 */
private int maxVerticalClear;

/**
 * how many blocks around the structure to clear (outside of w,h,l)
 */
private int clearingBuffer;

/**
 * maximum vertical fill distance for missing blocks below the structure
 * overrides overhang numbers
 */
private int maxLeveling;

/**
 * how many blocks outside of w,h,l should be leveled around the structure
 */
private int levelingBuffer;

/**
 * valid targets to build this structure upon.
 * may be overridden with a custom list
 */
public int[] validTargetBlocks = {1,2,3,12,13};

/**
 * i.e. embed_into_distance
 * how far should this structure generate below the chosen site level
 */
public int verticalOffset;
public int xOffset;
public int zOffset;

public int xSize;//x dimension
public int zSize;//z dimension
public int ySize;//y dimension

public void setOverhang(int overhang){this.maxOverhang = overhang;}
public void setLevelingMax(int leveling){this.maxLeveling = leveling;}
public void setLevelingBuffer(int buffer){this.levelingBuffer = buffer;}
public void setClearingMax(int clearing){this.maxVerticalClear = clearing;}
public void setClearingBuffer(int buffer){this.clearingBuffer = buffer;}
public int getLevelingMaxRaw(){return this.maxLeveling;};
public int getLevelingBufferRaw(){return this.levelingBuffer;}
public int getClearingMaxRaw(){return this.maxVerticalClear;}
public int getClearingBufferRaw(){return this.clearingBuffer;}
public int getOverHangRaw(){return this.maxOverhang;}

public int getLevelingMax()
  {
  if(this.worldGen!=null && this.worldGen.hasLevlingOverride())
    {
    return worldGen.maxLeveling;
    }
  return this.maxLeveling;
  }

public int getLevelingBuffer()
  {
  if(this.worldGen!=null && this.worldGen.hasLevlingOverride())
    {
    return worldGen.levelingBuffer;
    }
  return this.levelingBuffer;
  }

public int getOverhangMax()
  {
  if(this.worldGen!=null && this.worldGen.hasOverhangOverride())
    {
    return worldGen.overhang;
    }
  return this.maxOverhang;
  }

public int getClearingMax()
  {
  if(this.worldGen!=null && this.worldGen.hasClearingOverride())
    {
    return this.worldGen.maxClearing;
    }
  return this.maxVerticalClear;
  }

public int getClearingBuffer()
  {
  if(this.worldGen!=null && this.worldGen.hasClearingOverride())
    {
    return this.worldGen.clearingBuffer;
    }
  return this.clearingBuffer;
  }

public void setWorldGenEntry(WorldGenStructureEntry ent)
  {
  this.worldGen = ent;
  }

public WorldGenStructureEntry getWorldGenEntry()
  {
  return this.worldGen;
  }

public boolean isLocked()
  {
  if(this.openEditors.size()==0)
    {
    return false;
    }
  Iterator<String> it = this.openEditors.iterator();
  String name;
  EntityPlayer player;
  while(it.hasNext())
    {
    name = it.next();
    player = MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(name);
    if(player==null)
      {
      it.remove();
      }
    }
  if(this.openEditors.size()>0)
    {
    return true;
    }
  return false;
  }

public int openEditorcount()
  {
  return this.openEditors.size();  
  }

public void addEditor(String name)
  {
//  Config.logDebug("adding editor name: "+name);
  this.openEditors.add(name);
  }

public void removeEditor(String name)
  {
//  Config.logDebug("removing editor name: "+name);
  this.openEditors.remove(name);
  }

public int openBuilderCount()
  {
  return this.openBuilders.size();
  }

public void addBuilder(Builder build)
  {
//  Config.logDebug("adding builder");
  this.openBuilders.add(build);
  }

public void removeBuilder(Builder build)
  {
//  Config.logDebug("removing builder");
  this.openBuilders.remove(build);
  }

public static StructureBB getBoundingBox(BlockPosition hit, int facing, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize)  
  {  
  BlockPosition fl = hit.copy();
  fl.moveLeft(facing, xOffset);
  fl.moveForward(facing, zOffset);
  fl.y -=yOffset;
  
  BlockPosition br = fl.copy();
  br.y+= ySize-1;
  br.moveRight(facing, xSize-1);
  br.moveForward(facing, zSize-1);
  return new StructureBB(fl, br);
  }

public static StructureBB getClearingValidationBox(BlockPosition hit, int facing, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, int maxClearing)
  {
  StructureBB bb = getBoundingBox(hit, facing, xOffset, yOffset, zOffset, xSize, ySize, zSize);  
  int cl = ySize - yOffset - maxClearing;
  if(cl<0)
    {
    bb.pos2.y -= cl;
    }
  bb.pos2.y ++;
  bb.pos1.y = bb.pos2.y;
  return bb;
  }

public static StructureBB getLevelingBoundingBox(BlockPosition hit, int facing, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, int maxLeveling, int levelingBuffer)
  {
  StructureBB bb = getBoundingBox(hit, facing, xOffset, yOffset, zOffset, xSize, ySize, zSize);
  BlockPosition min = BlockTools.getMin(bb.pos1, bb.pos2);
  BlockPosition max = BlockTools.getMax(bb.pos1, bb.pos2);
  min.y+=yOffset;
  min.y--;  
  max.y = min.y;  
  min.y -= maxLeveling - 1;  
  min.x -= levelingBuffer;
  min.z -= levelingBuffer;
  max.x += levelingBuffer;
  max.z += levelingBuffer;
  bb.pos1 = min;
  bb.pos2 = max;
  return bb;
  }

public static StructureBB getClearingBoundinBox(BlockPosition hit, int facing, int xOffset, int yOffset, int zOffset, int xSize, int ySize, int zSize, int maxVerticalClear, int clearingBuffer)
  {
  StructureBB bb = getBoundingBox(hit, facing, xOffset, yOffset, zOffset, xSize, ySize, zSize);
  
  BlockPosition fl = bb.pos1.copy();
  BlockPosition br = bb.pos2.copy();
  
  fl.y += yOffset;
  fl.moveLeft(facing, clearingBuffer);
  fl.moveBack(facing, clearingBuffer);
  br.y = fl.y + maxVerticalClear - 1;;
  br.moveRight(facing, clearingBuffer);
  br.moveForward(facing, clearingBuffer);
  
  bb.pos1 = fl;
  bb.pos2 = br;
  return bb;
  }

}
