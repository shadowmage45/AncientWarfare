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
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.structures.data.rules.EntityRule;
import shadowmage.ancient_warfare.common.structures.data.rules.NpcRule;
import shadowmage.ancient_warfare.common.structures.data.rules.VehicleRule;
import shadowmage.ancient_warfare.common.structures.file.StructureExporter;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;

public class ScannedStructureData
{

public BlockPosition pos1;
public BlockPosition pos2;
public BlockPosition buildKey;
public int originFacing;
public int xSize;
public int ySize;
public int zSize;
public BlockData[][][] allBlocks;
public ArrayList<BlockData> blockIDs = new ArrayList<BlockData>();
private List<ScannedEntityEntry> includedEntities = new ArrayList<ScannedEntityEntry>();

/**
 * set by structure scanner GUI prior to export
 *  
 */
public String name = "";
public boolean world;
public boolean survival;
public boolean creative;
public int structureWeight = 1;
public int chunkDistance = 0;
public int chunkAttempts = 1;


/**
 * @param face
 * @param pos1
 * @param pos2
 * @param key an offset vector, relative to frontLeft corner.  Number of blocks to offset the structure left, down, and towards viewer
 */
public ScannedStructureData(int face, BlockPosition pos1, BlockPosition pos2,BlockPosition key)
  {
  this.pos1 = BlockTools.getMin(pos1, pos2);
  this.pos2 = BlockTools.getMax(pos1, pos2);
  this.buildKey = key;
  this.originFacing = face;  
  BlockPosition size = BlockTools.getBoxSize(pos1, pos2);
  this.setSize(originFacing, size.x, size.y, size.z);
  this.setArraySize(originFacing, size.x, size.y, size.z);
  }

public void setSize(int facing, int x, int y, int z)
  {
  this.xSize = x;
  this.ySize = y;
  this.zSize = z;
  }

public void setArraySize(int facing, int x, int y, int z)
  {
  this.allBlocks = new BlockData[x][y][z];
  }

public void scan(World world)
  {  
  int indexX = 0;
  int indexY = 0;
  int indexZ = 0;
  int id;
  int meta;
  for(int x = pos1.x; x <= pos2.x; x++, indexX++, indexY=0)
    {
    for(int y = pos1.y; y <= pos2.y; y++, indexY++, indexZ=0)
      {
      for(int z = pos1.z; z <= pos2.z; z++, indexZ++)
        {       
        id = world.getBlockId(x, y, z);
        meta = world.getBlockMetadata(x, y, z);
        if(id==Block.doorWood.blockID || id==Block.doorIron.blockID)
          {
          int lowerID = world.getBlockId(x, y-1, z);
          if(lowerID==Block.doorWood.blockID || lowerID==Block.doorIron.blockID)
            {
            meta = 8;
            }
          }
        allBlocks[indexX][indexY][indexZ] = new BlockData(id, meta);        
        }      
      }    
    }    
  this.scanForEntities(world);
  this.normalizeForNorthFacing();
  }

protected void scanForEntities(World world)
  {  
  this.includedEntities.clear();
  AxisAlignedBB bb = AxisAlignedBB.getAABBPool().getAABB(pos1.x, pos1.y, pos1.z, pos2.x+1, pos2.y+1, pos2.z+1);
  List<Entity> scannedEntities = world.getEntitiesWithinAABBExcludingEntity(null, bb);
  float x;
  float y;
  float z;  
  for(Entity e : scannedEntities)
    {
    if(AWStructureModule.instance().isScannableEntity(e.getClass()))
      {
      x = (float) (e.posX - pos1.x);
      y = (float) (e.posY - pos1.y);
      z = (float) (e.posZ - pos1.z);
      this.includedEntities.add(new ScannedEntityEntry(e, x, y, z, e.rotationYaw, e.rotationPitch));
      }
    }
  }

protected void addToBlocksList(BlockData data)
  {
  boolean shouldAdd = true;
  for(BlockData block : this.blockIDs)
    {
    if(block.id==data.id && block.meta==data.meta)
      {
      shouldAdd = false;
      }
    }
  if(shouldAdd)
    {
    this.blockIDs.add(data.copy());
    }
  }

public int getRuleForBlock(int id, int meta)
  {
  for(int i = 0; i< this.blockIDs.size(); i++)
    {
    if(this.blockIDs.get(i).id==id && this.blockIDs.get(i).meta==meta)
      {
      return i;
      }
    }
  return 0;
  } 

/*********************************************  NORMALIZATION  ***********************************************/

private void normalizeForNorthFacing()
  {
  int newXSize;// = this.xSize;
  int newYSize = this.ySize;
  int newZSize;// = this.zSize;
  BlockData[][][] newBlocks;
  if(this.originFacing==1 || this.originFacing ==3)
    {
    newXSize = this.zSize;
    newZSize = this.xSize;  
    }
  else
    {
    newXSize = this.xSize;
    newZSize = this.zSize;
    }
    
  newBlocks = new BlockData[newXSize][newYSize][newZSize];
  
  this.blockIDs.clear();
  this.addToBlocksList(new BlockData(0,0));
  int rotationAmount = BlockTools.getRotationAmount(originFacing, 2);
  for(int x = 0; x<this.xSize; x++)
    {
    for(int y = 0; y < this.ySize; y++)
      {
      for(int z = 0; z< this.zSize; z++)
        {
        BlockPosition pos = BlockTools.getNorthRotatedPosition(x,y,z, this.originFacing, newXSize, newZSize);      
        BlockData data = this.allBlocks[x][y][z];
        data.rotateRight(rotationAmount);
        newBlocks[pos.x][pos.y][pos.z] = data;
        if(data.id!=0)
          {
          this.addToBlocksList(newBlocks[pos.x][pos.y][pos.z]);
          }
        }
      }
    }  
  this.xSize = newXSize;
  this.ySize = newYSize;
  this.zSize = newZSize;
  this.allBlocks = newBlocks;
  int x;
  int y;
  int z;
  float xOff;
  float yOff;
  float zOff;
  float newXOff;
  float newZOff;
  BlockPosition pos;// = new BlockPosition(0,0,0);
  for(ScannedEntityEntry entry : this.includedEntities)
    {
    x = MathHelper.floor_float(entry.x);
    y = MathHelper.floor_float(entry.y);
    z = MathHelper.floor_float(entry.z);
    xOff = entry.x % 1.f;
    zOff = entry.z % 1.f;
    yOff = entry.y % 1.f;
    pos = BlockTools.getNorthRotatedPosition(x, y, z, this.originFacing, newXSize, newZSize);
    newXOff = getRotatedOffsetX(this.originFacing, xOff, zOff);
    newZOff = getRotatedOffsetZ(this.originFacing, xOff, zOff);
    entry.bx = pos.x;
    entry.by = pos.y;
    entry.bz = pos.z;
    entry.ox = newXOff;
    entry.oy = yOff;
    entry.oz = newZOff;
    entry.x = pos.x+newXOff;
    entry.y = pos.y+yOff;
    entry.z = pos.z+newZOff;
    entry.r += BlockTools.getRotationAmt(originFacing) * 90;
    if(entry.hangingDirection>=0)//is painting or item frame, adjust internal rotation info...
      {
      Config.logDebug("original hangDir"+entry.hangingDirection);
      entry.hangingDirection = (BlockTools.getRotationAmount(this.originFacing, 2)+entry.hangingDirection)%4;
      Config.logDebug("new hangDir"+entry.hangingDirection);
      }
    }
  }


private float getRotatedOffsetX(int rotation, float xOff, float zOff)
  {
  if(rotation==0)//south, invert x,z
    {
    return 1 - xOff;//new BlockPosition(xSize - x - 1 , y, zSize - z - 1 );
    }
  if(rotation==1)//west
    {
    return zOff;//new BlockPosition(xSize - z - 1, y, x);
    }
  if(rotation==2)//north, no change
    {
    return xOff;//new BlockPosition(x,y,z);
    }
  if(rotation==3)//east
    {
    return 1 - zOff;//new BlockPosition(z, y, zSize - x - 1);
    }
  return xOff;
  }

private float getRotatedOffsetZ(int rotation, float xOff, float zOff)
  {
  if(rotation==0)//south, invert x,z
    {
    return 1 - zOff;//new BlockPosition(xSize - x - 1 , y, zSize - z - 1 );
    }
  if(rotation==1)//west
    {
    return xOff;//new BlockPosition(xSize - z - 1, y, x);
    }
  if(rotation==2)//north, no change
    {
    return zOff;//new BlockPosition(x,y,z);
    }
  if(rotation==3)//east
    {
    return 1 - xOff;//new BlockPosition(z, y, zSize - x - 1);
    }
  return zOff;
  }
  
/*********************************************  CONVERSION  ***********************************************/

/**
 * returns a processed structure populated with enough data to _build_ this structure,
 * though it is not as configured as if done through a proper template
 * @param name
 * @return
 */
public ProcessedStructure convertToProcessedStructure()
  {
  ProcessedStructure struct = new ProcessedStructure();
  struct.name = String.valueOf(this.name);
  struct.xSize = this.xSize;
  struct.ySize = this.ySize;
  struct.zSize = this.zSize;
  
  struct.xOffset = this.buildKey.x;
  struct.verticalOffset = this.buildKey.y;
  struct.zOffset = this.buildKey.z;
  
  BlockData[] blocks = this.getAllBlockTypes();
  for(int i = 0; i < blocks.length; i++)
    {
    BlockData data = blocks[i];
    BlockRule rule = new BlockRule(i, data.id, data.meta);
    struct.blockRules.put(Integer.valueOf((int) rule.ruleNumber), rule);    
    }
  struct.structure = new short[xSize][ySize][zSize];
  for(int x = 0; x <struct.structure.length; x++)
    {
    for(int y = 0; y <struct.structure[x].length; y++)
      {
      for(int z = 0; z < struct.structure[x][y].length; z++)
        {
        BlockData data = this.allBlocks[x][y][z];
        struct.structure[x][y][z]=(short) this.getRuleForBlock(data.id, data.meta);
        }
      }
    }
  this.addEntitiesToStructure(struct, includedEntities);
  List<String> lines = StructureExporter.getExportLinesFor(struct);
  struct.setTemplateLines(lines);
  return struct;
  }

public BlockData[] getAllBlockTypes()
  {  
  BlockData[] datas = new BlockData[this.blockIDs.size()];
  for(int i = 0; i <this.blockIDs.size(); i++)
    {
    datas[i]=this.blockIDs.get(i);
    }
  return datas;
  }

private void addEntitiesToStructure(ProcessedStructure struct, List<ScannedEntityEntry> entities)
  {
  EntityRule rule;
  ScannedEntityEntry entry;
  Class clz;
  for(int i = 0; i < entities.size(); i++)
    {
    entry = entities.get(i);
    clz = entry.ent.getClass();
    if(clz==NpcBase.class)
      {
      addNpcToStructure(struct, entry, (NpcBase)entry.ent);
      }
    else if(clz==VehicleBase.class)
      {
      addVehicleToStructure(struct, entry, (VehicleBase)entry.ent);
      }
    else if(clz==EntityVillager.class)
      {
      addVillagerToStructure(struct, entry, (EntityVillager)entry.ent);
      }
    else
      {
      rule = EntityRule.populateRule(entities.get(i));      
      if(rule!=null)
        {
        struct.entityRules.add(rule);
        }
      } 
    }
  }

private void addNpcToStructure(ProcessedStructure struct, ScannedEntityEntry entry, NpcBase npc)
  {
  NpcRule rule = NpcRule.populateRule(entry, npc);
  if(rule!=null)
    {
    struct.NPCRules.add(rule);
    }
  }

private void addVehicleToStructure(ProcessedStructure struct, ScannedEntityEntry entry, VehicleBase vehicle)
  {
  VehicleRule rule = VehicleRule.populateRule(entry, vehicle);
  if(rule!=null)
    {
    struct.vehicleRules.add(rule);
    }
  }

private void addGateToStructure(ProcessedStructure struct, ScannedEntityEntry entry, Entity gate)
  {
  
  }

private void addVillagerToStructure(ProcessedStructure struct, ScannedEntityEntry entry, EntityVillager villager)
  {
  NpcRule rule = NpcRule.populateRule(entry, villager);
  if(rule!=null)
    {
    struct.NPCRules.add(rule);
    }
  }

}
