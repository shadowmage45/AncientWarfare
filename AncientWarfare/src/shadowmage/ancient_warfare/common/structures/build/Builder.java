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
package shadowmage.ancient_warfare.common.structures.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.manager.BlockDataManager;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.AWStructure;
import shadowmage.ancient_warfare.common.structures.data.BlockData;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureBB;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.structures.data.rules.CivicRule;
import shadowmage.ancient_warfare.common.structures.data.rules.EntityRule;
import shadowmage.ancient_warfare.common.structures.data.rules.GateRule;
import shadowmage.ancient_warfare.common.structures.data.rules.InventoryRule;
import shadowmage.ancient_warfare.common.structures.data.rules.MachineRule;
import shadowmage.ancient_warfare.common.structures.data.rules.NpcRule;
import shadowmage.ancient_warfare.common.structures.data.rules.VehicleRule;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.world_gen.LootGenerator;

public abstract class Builder implements INBTTaggable
{
/**
 * the structure being built
 */
public final ProcessedStructure struct;

/**
 * the world in which the structure is being built
 */
public World world;

/**
 * current tick...used for...ticking...
 */
int tickNum = 0; 

/**
 * build passes
 */
int currentPriority = 0;

/**
 * highest priority found when scanning blockrules
 */
int maxPriority;

/**
 * the facing chosen for this builder
 */
public int facing = 0;

/**
 * current iteration position through template
 */
public int currentX = 0;

/**
 * current iteration position through template
 */
public int currentY = 0;

/**
 * current iteration position through template
 */
public int currentZ = 0;

/**
 * the chosen build position for this structure
 */
public final BlockPosition buildPos;

/**
 * flipped if this structure is finished, will be removed from ticking queues
 */
protected boolean isFinished = false;

/**
 * what dimension this builder is working in, used for save/load from NBT
 */
public int dimension = 0;


/************************************************************ OVERRIDE VALUES ************************************************************/
boolean spawnVehicles = true;
boolean spawnNpcs = true;
boolean spawnGates = true;
int overrideTeam=-1;


/**
 * hack to get around overwriting tile entity blocks during non-world gen instant-builders
 */
protected Map<BlockPosition, BlockData> deferredBlocks = new HashMap<BlockPosition, BlockData>();

/**
 * md5 of the original file for the structure for this builder.
 * If builder is loaded from disc and the passed-in struct has a different md5
 * the builder will invalidate itself....
 */
String md5;

/**
 * RNG used for structure building for non-world gen
 */
protected Random random = new Random();

public Builder(World world, ProcessedStructure struct, int facing, BlockPosition hit)
  {
  this(struct, facing, hit);
  this.setWorld(world);
  }

protected Builder(ProcessedStructure struct, int facing, BlockPosition hit)
  {
  this.struct = struct;
  this.buildPos = hit;  
  this.facing = facing;
  for(Integer i : struct.blockRules.keySet())
    {
    if(i>this.maxPriority)
      {
      this.maxPriority = i;
      }
    }
  this.md5 = String.valueOf(struct.md5);
  }

public void setWorld(World world)
  {
  this.world = world;
  if(world!=null)
    {
    this.dimension = this.world.provider.dimensionId;
    }
  }

public void setOverrides(int team, boolean vehicle, boolean npc, boolean gate)
  {
  this.setTeamOverride(team);
  this.setVehicleOverride(vehicle);
  this.setNPCOverride(npc);
  this.setGateOverride(gate);
  }

public void clearBuilderFromStructure()
  {
  this.struct.removeBuilder(this);
  }

public void doFillAround(int depth)
  {
  BlockPosition a = this.buildPos.copy();
  a.moveLeft(facing, struct.xOffset);
  a.moveBack(facing, struct.zOffset);
  BlockPosition b = a.copy();
  b.moveRight(facing, struct.xSize);
  b.moveForward(facing, struct.zSize);
  BlockPosition min = BlockTools.getMin(a, b);
  BlockPosition max = BlockTools.getMax(a, b);
 
  switch(facing)
  {
  case 0:
  min.x++;
  max.x++;
  break;
  
  case 1:
  min.x++;
  max.x++;
  min.z++;
  max.z++;
  break;
  
  case 2:
  min.z++;
  max.z++;
  break; 
  }  
  
  int x = min.x;
  int y = min.y;
  int z = min.z;
  int x1 = max.x;
  int z1 = max.z;
  int bx, bz;
  int dec = 0;
  int setID = 0;  
  for(int i = 0; i <= depth; i++)
    {
    for(bx = x; bx < x1; bx++)
      {
      for(bz = z; bz < z1; bz++)
        {
        if(bx>=min.x && bx < max.x && bz>=min.z && bz < max.z)
          {
          continue;
          }        
        if(!world.isBlockSolidOnSide(bx, y, bz, ForgeDirection.UP))
          {          
          if(dec<5 || world.isAirBlock(bx, y+1, bz))
            {
            setID = world.getBiomeGenForCoords(bx, bz).topBlock;   
            }
          else
            {
            setID = world.getBiomeGenForCoords(bx, bz).fillerBlock;
            }
          if(setID<=0)
            {
            setID = Block.dirt.blockID;
            }
          if(y>=1)
            {
            setBlock(world, bx, y, bz, setID, 0);            
            }
          }
        }
      }
    dec++;
    y--;
    x--;
    x1++;
    z--;
    z1++;
    if(y<=0)
      {
      break;
      }
    }
  }

public void doFillBeneathStraight(int depth)
  {
  BlockPosition a = this.buildPos.copy();
  a.moveLeft(facing, struct.xOffset);
  a.moveBack(facing, struct.zOffset);
  BlockPosition b = a.copy();
  b.moveRight(facing, struct.xSize);
  b.moveForward(facing, struct.zSize);
  BlockPosition min = BlockTools.getMin(a, b);
  BlockPosition max = BlockTools.getMax(a, b);
  int x = min.x;
  int y = min.y - struct.verticalOffset - 1;
  int z = min.z;
  int x1 = max.x;
  int z1 = max.z;
  int bx, bz, by;
  switch(facing)
  {
  case 0:
  x++;
  x1++;
  break;
  
  case 1:
  x++;
  x1++;
  z++;
  z1++;
  break;
  
  case 2:
  z++;
  z1++;  
  break;
 
  }  
  for(bx = x; bx < x1; bx++)
    {
    for(bz = z; bz < z1; bz++)
      {
      for(by = y; by > y-depth && by>0; by--)
        {
        if(!world.isBlockSolidOnSide(bx, by, bz, ForgeDirection.UP))
          {
          setBlock(world, bx, by, bz, Block.stone.blockID, 0);
          }
        else
          {
          break;
          }
        }
      }
    }
  }

public void doFillBeneathInvPyramid(int depth)
  {
  BlockPosition a = this.buildPos.copy();
  a.moveLeft(facing, struct.xOffset);
  a.moveBack(facing, struct.zOffset);
  BlockPosition b = a.copy();
  b.moveRight(facing, struct.xSize);
  b.moveForward(facing, struct.zSize);
  BlockPosition min = BlockTools.getMin(a, b);
  BlockPosition max = BlockTools.getMax(a, b);
  
  int xDiff = max.x - min.x;
  int zDiff = max.z - min.z;
  
  int x = min.x;
  int y = min.y - struct.verticalOffset - 1;
  int z = min.z;
  int x1 = max.x;
  int z1 = max.z;
  int bx, bz;
  switch(facing)
  {
  case 0:
  x++;
  x1++;
  break;
  
  case 1:
  x++;
  x1++;
  z++;
  z1++;
  break;
  
  case 2:
  z++;
  z1++;  
  break;
 
  }
  
  int dec = 0;
  int id;
  int setID = 0;
  while((xDiff>0 || zDiff >0) && dec <= depth)
    {
    for(bx = x; bx<x1; bx++)
      {
      for(bz = z; bz<z1; bz++)
        {
        if(dec<5)
          {
          setID = world.getBiomeGenForCoords(bx, bz).topBlock;   
          setID = setID== Block.sand.blockID ? Block.sandStone.blockID : setID;
          }
        else
          {
          setID = Block.stone.blockID;
          }
        if(setID<=0)
          {
          setID = Block.dirt.blockID;
          }
        if(!world.isBlockSolidOnSide(bx, y, bz, ForgeDirection.UP))
          {
          setBlock(world, bx, y, bz, setID, 0);
          }
        }
      } 
    y--;
    dec++;
    if(xDiff>0)
      {
      xDiff--;
      x++;
      x1--;
      }
    if(zDiff>0)
      {
      zDiff--;
      z++;
      z1--;
      }
    }
  
  }

/**
 * for instantBuilder--construct
 * for tickedBuilder--setup bounds/block
 */
public abstract void startConstruction();

/**
 * for instantBuilder--NOOP
 * for tickedBuilder--remove tickerBlock
 */
public abstract void finishConstruction();

/**
 * for instantBuilder--NOOP
 * for tickedBuilder--place singleBlock or increment timer between blocks
 */
public abstract void onTick();


public boolean isFinished()
  {
  return isFinished;
  }

public void setWorldGen()
  {
  isWorldGen = true;
  }

protected boolean isWorldGen = false;

public void setFinished()
  {  
  this.placeNonBlocks(world);
  this.isFinished = true;
  this.clearBuilderFromStructure();
  }

public void setVehicleOverride(boolean val)
  {
  this.spawnVehicles = val;
  }

public void setNPCOverride(boolean val)
  {
  this.spawnNpcs = val;
  }

public void setGateOverride(boolean val)
  {
  this.spawnGates = val;
  }

public void setTeamOverride(int type)
  {
  this.overrideTeam = type;
  }

/**
 * calls leveling and clearing functions, does not validate the site
 */
protected void preConstruction()
  {  
  if(struct.getLevelingMax() > 0)
    {
    doLeveling();
    }
  if(struct.getClearingMax() > 0)
    {
    doClearing();
    }
  }

/**
 * does leveling according to maxLeveling, levelingBuffer, minBounds, maxBounds.  No preservation
 */
protected void doLeveling()
  {
  StructureBB bb = AWStructure.getLevelingBoundingBox(buildPos, facing, struct.xOffset, struct.verticalOffset, struct.zOffset, struct.xSize, struct.ySize, struct.zSize, struct.getLevelingMax(), struct.getLevelingBuffer());  
  int rnd = this.random.nextInt(2);
  int id = Block.stone.blockID;
  if(rnd ==0)
    {
    id = Block.dirt.blockID;
    }
  List<BlockPosition> blocksToLevel = BlockTools.getAllBlockPositionsBetween(bb.pos1, bb.pos2);
  for(BlockPosition pos : blocksToLevel)
    {
    int testID = world.getBlockId(pos.x, pos.y, pos.z);
    if(testID==0)
      {
      setBlock(world, pos.x, pos.y, pos.z, id, 0);
      }
    }
  }

protected void doClearing()
  {
  
  StructureBB bb = AWStructure.getClearingBoundinBox(buildPos, facing, struct.xOffset, struct.verticalOffset, struct.zOffset, struct.xSize, struct.ySize, struct.zSize, struct.getClearingMax(), struct.getClearingBuffer());
  
  //StructureBB bb = struct.getClearingBB(buildPos, facing);  
  StructureBB bounds = struct.getStructureBB(buildPos, facing);
  BlockPosition minBounds = BlockTools.getMin(bounds.pos1, bounds.pos2);
  BlockPosition maxBounds = BlockTools.getMax(bounds.pos1, bounds.pos2);  
//  Config.logDebug("Clearing Bounds: "+bb.pos1.toString() +"---"+bb.pos2.toString());    
  List<BlockPosition> blocksToClear = BlockTools.getAllBlockPositionsBetween(bb.pos1, bb.pos2);  
  for(BlockPosition pos : blocksToClear)
    {
    if(pos.x >=minBounds.x && pos.x <=maxBounds.x && pos.y >=minBounds.y && pos.y <=maxBounds.y && pos.z >=minBounds.z && pos.z <=maxBounds.z)
      {
      continue;
      }
    int id = world.getBlockId(pos.x, pos.y, pos.z);
    if(!shouldPreserveBlockDuringClearing(id))
      {
      setBlock(world, pos.x, pos.y, pos.z, 0, 0);
      }
    }  
  }

protected void setBlock(World world, int x, int y, int z, int id, int meta)
  { 
  if(y<=0)
    {
    return;
    }  
  if(id<0)
    {
    Config.logError("Attempt to set block to invalid id: "+x+","+y+","+z+","+"::"+id+"-"+meta + " from structure: "+struct.name);
    Exception e = new Exception();
    e.printStackTrace();
    return;
    }
  if(world.blockExists(x, y, z))
    {
    if(isWorldGen)
      {
      try
        {
        world.setBlock(x, y, z, id);
        world.setBlockMetadataWithNotify(x, y, z, meta, 0);
        }
      catch(Exception e)
        {
        Config.logError("caught exception while attempting to place block: "+x+","+y+","+z+" :: "+id+"-"+meta);
        }
      }
    else
      {
      world.setBlock(x, y, z, id);
      world.setBlockMetadataWithNotify(x, y, z, meta, 3);
//      world.setBlock(x, y, z, id, meta, 3);      
      }
    }
  }

/**
 * 
 * @param world world in which to build
 * @param x world x coord
 * @param y world y coord
 * @param z world z coord
 * @param rule current blockRule
 * @param defer should deferPlacement of overwritten tile-entity blocks?
 * @param worldGen should use world random/gen random?
 */
protected void handleBlockRulePlacement(World world, int x, int y, int z, BlockRule rule, boolean worldGen)
  {   
  if(y<=0)
    {
    return;
    }
  /**
   * check to see if block should be placed by rule percent chance
   */
  int rnd = random.nextInt(100);
  if(rnd>=rule.baseChance)
    {
    return;
    }
  /**
   * else check to see whether should use
   * 
   * validity/checking vars
   */
  int checkLen = 0;
  boolean validBlocks = false;
  boolean validSpecials = false;
  boolean validSpawner = false;
  if(rule.blockData!=null)
    {
    validBlocks = true;
    checkLen += rule.blockData.length;
    }
  if(rule.ruinsSpecialData!=null)
    {
    validSpecials = true;
    checkLen += rule.ruinsSpecialData.length;
    }   
  if(rule.spawnerTypes!=null)
    {
    validSpawner = true;
    checkLen += rule.spawnerTypes.length;
    }
    
  rnd = random.nextInt(checkLen);
  if(validBlocks)
    {
    if(rnd>=rule.blockData.length)
      {
      rnd -=rule.blockData.length;      
      }
    else
      {     
      BlockData data = rule.blockData[rnd];
      if(rule.swapGroup > -1)
        {
        String biomeName = world.getBiomeGenForCoords(x, z).biomeName;
        data = this.getSwappedDataFor(rule, biomeName, data);
        }
      placeBlockData(world, x, y, z, data, rule);
      return;
      }
    }  
  if(validSpecials)
    {
    if(rnd>=rule.ruinsSpecialData.length)
      {
      rnd -= rule.ruinsSpecialData.length;
      }
    else
      {
      placeSpecials(world, x, y, z, rule.ruinsSpecialData[rnd]);     
      return;
      }
    } 
  if(validSpawner)
    {
    if(rnd>=rule.spawnerTypes.length)
      {
      rnd -= rule.spawnerTypes.length;      
      }
    else
      {
      handleNamedSpawner(world, x, y, z, rule.spawnerTypes[rnd]);
      }
    }
  }

protected void placeBlockData(World world, int x, int y, int z, BlockData data, BlockRule rule)
  {  
  int meta = BlockDataManager.instance().getRotatedMeta(data.id, data.meta, getRotationAmt(facing));
  this.setBlock(world, x,y,z, data.id, meta);
  if(rule.inventoryLevel>=0)
    {
    TileEntity te = world.getBlockTileEntity(x, y, z);
    if(te instanceof IInventory)
      {
      IInventory inventory = (IInventory)te;
      LootGenerator.instance().generateLootFor(inventory, inventory.getSizeInventory(), rule.inventoryLevel, random); 
      }
    }
  else if(rule.inventoryRules!=null && rule.inventoryRules.length>0)
    {
    int iRuleNum = rule.inventoryRules[random.nextInt(rule.inventoryRules.length)];
    if(iRuleNum!=0)
      {
      InventoryRule iRule = struct.inventoryRules.get(iRuleNum);
      if(iRule!=null && iRule.items!=null)
        {
        TileEntity te = world.getBlockTileEntity(x, y, z);
        if(te instanceof IInventory)
          {
          IInventory inv = (IInventory)te;
          ItemStack stack = null;
          for(int i = 0; i < inv.getSizeInventory(); i++)
            {
            stack = iRule.items.getStackInSlot(i);
            if(stack!=null)
              {
              inv.setInventorySlotContents(i, stack.copy());
              }
            }
          }      
        }      
      }
    }
  }

private void placeVehicles(World world)
  {
  VehicleBase vehicle;
  for(VehicleRule rule : this.struct.vehicleRules)
    {
    vehicle = rule.getVehicleToSpawn(world, facing, struct, buildPos, this.overrideTeam);
    if(vehicle!=null)
      {
      world.spawnEntityInWorld(vehicle);
      }
    }
  }

private void placeNpcs(World world)
  {
  Entity npc = null;
  for(NpcRule rule : this.struct.NPCRules)
    {
    npc = rule.getEntityToSpawn(world, facing, struct, buildPos, this.overrideTeam);
    if(npc!=null)
      {
      world.spawnEntityInWorld(npc);
      }
    }
  }

private void placeCivics(World world)
  {
  for(CivicRule civ : struct.civicRules)
    {
    civ.handleWorldPlacement(world, facing, struct, buildPos);
    }
  }

private void placeGates(World world)
  {
	Entity e;
  for(GateRule g : this.struct.gateRules)
    {
	  e = g.getEntityToSpawn(world, facing, struct, buildPos, this.overrideTeam);
	  if(e!=null)
	  {
		    world.spawnEntityInWorld(e);		  
	  }
    }
  }

private void placeMachines(World world)
  {
  for(MachineRule m : this.struct.machineRules)
    {
    m.createBlock(world, buildPos, struct, facing);
    }
  }

/**
 * called to start placing entities from the EntityRule list, first -> last
 * @param world
 */
private void placeEntities(World world)
  {
  Entity entity;
  for(EntityRule rule : this.struct.entityRules)
    {
    entity = rule.getEntityToSpawn(world, facing, struct, buildPos);
    if(entity!=null)
      {
      world.spawnEntityInWorld(entity);
      }
    }
  }

protected void placeNonBlocks(World world)
  {
  this.placeEntities(world);
  if(spawnVehicles)
    {
    this.placeVehicles(world);
    }
  if(spawnNpcs)
    {
    this.placeNpcs(world);
    }
  if(spawnGates)
    {
    this.placeGates(world);
    }
  this.placeMachines(world);
  this.placeCivics(world);
  }

protected void placeSpecials(World world, int x, int y, int z, String name)
  {
  if(name == null)
    {
    return;
    }
  if(name.toLowerCase().startsWith("preserveblock"))
    {
    return;
    } 
  if(name.toLowerCase().endsWith("chest"))
    {
    createChestAt(world, x, y, z, 54, 0);
    TileEntityChest te = (TileEntityChest) world.getBlockTileEntity(x, y, z);
    if(name.toLowerCase().startsWith("easy"))
      {
      LootGenerator.instance().generateLootFor(te, 27, 0, random);
      }
    else if(name.toLowerCase().startsWith("medium"))
      {
      LootGenerator.instance().generateLootFor(te, 27, 1, random);
      }
    else if(name.toLowerCase().startsWith("hard"))
      {
      LootGenerator.instance().generateLootFor(te, 27, 2, random);
      }      
    return;
    }
  else
    {
    setBlock(world, x,y,z, 0, 0);
    }
  }

protected void createChestAt(World world, int x, int y, int z, int id, int meta)
  {
  setBlock(world, x,y,z,id,meta);
  }

protected void setChestLoot(World world, int x, int y, int z, List<ItemStack> items)
  {
  TileEntityChest chst = (TileEntityChest) world.getBlockTileEntity(x, y, z);
  for(int i = 0; i < items.size() && i < chst.getSizeInventory(); i++)
    {
    chst.setInventorySlotContents(i, items.get(i));
    }
  }

protected void handleNamedSpawner(World world, int x, int y, int z, String name)
  {
  world.setBlock(x, y, z, Block.mobSpawner.blockID, 0, 3);
  TileEntityMobSpawner ent = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);  
  if(ent!=null)
    {
    ent.getSpawnerLogic().setMobID(name);//t.setMobID(name);
    }
  }

protected boolean isAirBlock(World world, int x, int y, int z)
  {
  return world.getBlockId(x, y, z)==0;
  }

protected boolean isAirBlock(int id)
  {
  return id==0;
  }

protected boolean isPlant(int id)
  {  
  if(id == Block.plantRed.blockID || id == Block.plantYellow.blockID)
    {
    return true;
    }
  if(id == Block.cactus.blockID || id == Block.reed.blockID)
    {
    return true;
    }
  if(id == Block.mushroomBrown.blockID || id == Block.mushroomRed.blockID || id == Block.netherStalk.blockID) 
    {
    return true;
    }
  if(id == Block.pumpkinStem.blockID || id == Block.melonStem.blockID || id == Block.pumpkin.blockID || id == Block.melon.blockID)
    {
    return true;
    }
  if(id == Block.crops.blockID || id == Block.carrot.blockID || id == Block.potato.blockID)
    {
    return true;
    }
  //TODO check for logs, leaves, saplings ???  
  return false;
  }

protected boolean isWater(int id)
  {  
  if(id==Block.waterMoving.blockID || id==Block.waterStill.blockID)
    {
    return true;
    }
  return false;
  }

protected boolean isLava(int id)
  {
  if(id==Block.lavaMoving.blockID || id==Block.lavaStill.blockID)
    {
    return true;
    }
  return false;
  }

/**
 * checks for plants, lava, water, etc during clearing
 * @param id
 * @return
 */
protected boolean shouldPreserveBlockDuringClearing(int id)
  {
  if(struct.preserveBlocks)
    {
    return true;
    }
  if(struct.preserveLava && isLava(id))    
    {
    return true;
    }
  if(struct.preserveWater && isWater(id))
    {
    return true;
    }
  if(struct.preservePlants && isPlant(id))
    {
    return true;
    }
  return false;
  }

protected boolean shouldSkipBlock(World world, BlockRule rule, int x, int y, int z, int currentPriority)
  {
  int id = world.getBlockId(x, y, z);
  int meta = world.getBlockMetadata(x, y, z);
  boolean airBlock = id==0;
  if(rule.order!=currentPriority)
    {
    return true;
    }
  if(rule.ruleNumber==0 && airBlock)
    {
    return true;
    }
  if(!airBlock &&  struct.preserveBlocks)
    {
    return true;
    }  
  if(!airBlock && rule.preserveBlocks )    
    {    
    return rule.shouldPreserveBlock(id, meta);
    }
  if(isPlant(id) && (rule.preservePlants || struct.preservePlants))
    {
    return true;
    }
  if(isWater(id) && (rule.preserveWater || struct.preserveWater))
    {
    return true;
    }
  if(isLava(id) && (rule.preserveLava || struct.preserveLava))
    {
    return true;
    } 
  int rnd = random.nextInt(100);
  if(rnd>=rule.baseChance)
    {
    return true;
    }
  return false;
  }

protected boolean shouldSwapRule(BlockRule rule)
  {
  if(rule.swapGroup>-1)
    {
    return true;
    }
  return false;
  }

protected BlockData getSwappedDataFor(BlockRule rule, String biomeName, BlockData data)
  {  
  return struct.getSwappedData(rule.swapGroup, biomeName, data);
  }

/**
 * get rotation amount relative to default facing of 2
 * @param facing
 * @return
 */
protected int getRotationAmt(int facing)
  {
  if(facing==2)
    {
    return 0;
    }
  else if(facing==3)
    {
    return 1;
    }
  else if(facing==0)
    {
    return 2;
    }
  else if(facing==1)
    {
    return 3;
    }
  return 0;
  }


/**
 * return true if could increment coords OR build pass
 * @return
 */
protected boolean tryIncrementing()
  {  
  if(!this.incrementCoords())
    {
    if(!this.incrementBuildPass())
      {      
      return false;
      }    
    }
  return true;
  }

/**
 * return true if can continue (currentPriority++ is valid priority) 
 * @return
 */
protected boolean incrementBuildPass()
  {
  this.currentX = 0;
  this.currentY = 0;
  this.currentZ = 0;
  this.currentPriority++;
  if(this.currentPriority>this.maxPriority)
    {
    return false;
    }
  return true;
  }

/**
 * return true if can continue (increment was to a valid coord)
 * @return
 */
protected boolean incrementCoords()
  {
  if(this.currentX+1 < this.struct.xSize )
    {
    this.currentX++;
    return true;
    }
  if(this.currentZ+1 < this.struct.zSize)
    {
    this.currentZ++;
    this.currentX = 0;
    return true;
    }
  if(this.currentY+1 < this.struct.ySize)
    {
    this.currentY++;
    this.currentX = 0;
    this.currentZ = 0;
    return true;
    }
  return false;
  }

public void setProgress(int x, int y, int z, int pass)
  {
  this.currentX = x;
  this.currentY = y;
  this.currentZ = z;
  this.currentPriority = pass;
  }

public NBTTagCompound getNBTTag()
  {
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", this.struct.name);
  tag.setBoolean("finished", isFinished);
  tag.setByte("face", (byte)facing);
  tag.setInteger("x", currentX);
  tag.setInteger("y", currentY);
  tag.setInteger("z", currentZ);
  tag.setInteger("p", currentPriority);
  tag.setInteger("mP", maxPriority);
  tag.setInteger("dim", this.world.provider.dimensionId);
  tag.setInteger("bX", this.buildPos.x);
  tag.setInteger("bY", this.buildPos.y);
  tag.setInteger("bZ", this.buildPos.z);
  tag.setInteger("ovT", this.overrideTeam);
  tag.setBoolean("sV", this.spawnVehicles);
  tag.setBoolean("sN", this.spawnNpcs);
  tag.setBoolean("sG", this.spawnGates);
  tag.setString("md5", this.md5);
  return tag;
  }

public void readFromNBT(NBTTagCompound tag)
  {
  this.isFinished = tag.getBoolean("finished");  
  this.currentX = tag.getInteger("x");
  this.currentY = tag.getInteger("y");
  this.currentZ = tag.getInteger("z");
  this.currentPriority = tag.getInteger("p");
  this.maxPriority = tag.getInteger("mP");
  this.dimension = tag.getInteger("dim");  
  this.overrideTeam = tag.getInteger("ovT");
  this.spawnVehicles = tag.getBoolean("sV");
  this.spawnNpcs = tag.getBoolean("sN");
  this.spawnGates = tag.getBoolean("sG");  
  }

public static BuilderTicked readTickedBuilderFromNBT(NBTTagCompound tag)
  {  
  String name = tag.getString("name");
  String md5 = tag.getString("md5");
  ProcessedStructure struct = StructureManager.instance().getStructureServer(name);
  if(struct==null)
    {
    return null;
    }
  if(!struct.md5.equals(md5))
    {
    Config.logError("Severe error loading ticked builder from disc (MD5 Failure).  The structure it was building has been changed.  The builder will be invalidated.");
    return null;
    }
  BlockPosition hit = new BlockPosition(tag.getInteger("bX"), tag.getInteger("bY"), tag.getInteger("bZ"));
  int facing = tag.getByte("face");
  BuilderTicked builder = new BuilderTicked(struct, facing, hit);
  builder.readFromNBT(tag);
  struct.addBuilder(builder);
  return builder;
  }


}
