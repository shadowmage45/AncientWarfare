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
package shadowmage.ancient_warfare.common.structures.build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.interfaces.IAmmoType;
import shadowmage.ancient_warfare.common.interfaces.INBTTaggable;
import shadowmage.ancient_warfare.common.manager.BlockDataManager;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.registry.VehicleRegistry;
import shadowmage.ancient_warfare.common.structures.data.AWStructure;
import shadowmage.ancient_warfare.common.structures.data.BlockData;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureBB;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.structures.data.rules.VehicleRule;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.upgrades.IVehicleUpgradeType;
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
int overrideVehicle=-2;
int overrideNPC=-2;
int overrideGate=-2;
int overrideTeam=-1;
int overrideRank=-1;


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
    this.dimension = world.getWorldInfo().getDimension();
    }
  }

public void clearBuilderFromStructure()
  {
  this.struct.removeBuilder(this);
  }

/**
 * for instantBuilder--construct
 * for tickedBuilder--add to tickQue in AWStructureModule
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

public void setFinished()
  {
  this.isFinished = true;
  this.clearBuilderFromStructure();
  }

public void setVehicleOverride(int type)
  {
  this.overrideVehicle =type;
  }

public void setNPCOverride(int type)
  {
  this.overrideNPC = type;
  }

public void setGateOverride(int type)
  {
  this.overrideGate = type;
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
      world.setBlock(pos.x, pos.y, pos.z, id);
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
  Config.logDebug("Clearing Bounds: "+bb.pos1.toString() +"---"+bb.pos2.toString());    
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
      world.setBlock(pos.x, pos.y, pos.z, 0);
      }
    }  
  }

protected void placeBlock(World world, int x, int y, int z, int id, int meta)
  { 
  world.setBlockAndMetadata(x, y, z, id, meta);
  }

/**
 * used for instant builder during non-world-gen....
 * @param world
 * @param pos
 * @param id
 * @param meta
 */
protected void placeBlockWithDefer(World world, int x, int y, int z, int id, int meta)
  {
  if(world.getBlockTileEntity(x, y, z)!=null)
    {
    world.setBlock(x, y, z, 0);    
    this.deferredBlocks.put(new BlockPosition(x,y,z), new BlockData(id, meta));  
    }
  else
    {   
    world.setBlockAndMetadata(x, y, z, id, meta);
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
protected void handleBlockRulePlacement(World world, int x, int y, int z, BlockRule rule, boolean defer, boolean worldGen)
  {   
 
  /**
   * check to see if block should be placed by rule percent chance
   */
  int rnd = random.nextInt(100);
  if(rnd>=rule.baseChance)
    {
    return;
    }
 
  /**
   * if the blockRule represents a gate, place it.  That is all.
   */
  if(rule.gateNum>-1)
    {
    this.placeGate(world, x, y, z, rule.gateNum, overrideGate, overrideRank, overrideTeam);
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
  boolean validVehicles = false;
  boolean validNPCs = false;  
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
  if(rule.vehicles!=null)
    {
    validVehicles = true;
    checkLen += rule.vehicles.length;
    }
  if(rule.npcs!=null)
    {
    validNPCs = true;
    checkLen += rule.npcs.length;
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
      placeBlockData(world, x, y, z, data, defer);
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
  if(validVehicles)
    {
    if(rnd>=rule.vehicles.length)
      {
      rnd -= rule.vehicles.length;      
      }
    else
      {
      placeVehicle(world, x, y, z, rule, rule.vehicles[rnd], overrideVehicle, overrideRank, overrideTeam);    
      return;
      }    
    }
  if(validNPCs)
    {
    if(rnd>=rule.npcs.length)
      {
      rnd -= rule.npcs.length;
      }
    else
      {
      placeNPC(world, x, y, z, rule.npcs[rnd], overrideNPC, overrideRank, overrideTeam);
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

protected void placeBlockData(World world, int x, int y, int z, BlockData data, boolean defer)
  {  
  int meta = BlockDataManager.instance().getRotatedMeta(data.id, data.meta, getRotationAmt(facing));
  if(defer)
    {
    this.placeBlockWithDefer(world, x,y,z , data.id, meta);
    }
  else
    {
    this.placeBlock(world, x,y,z, data.id, meta);
    }   
  }

protected void placeVehicle(World world, int x, int y, int z, BlockRule parentRule, int vehicleType, int overrideType, int overrideRank, int overrideTeam)
  {
  VehicleRule vehRule = struct.vehicleRules.get(vehicleType);
  if(vehRule==null)
    {
    return;
    }
  int type = overrideType>=0 ? overrideType : vehRule.vehicleType;
  int rank = overrideRank;
  VehicleBase vehicle = VehicleRegistry.instance().getVehicleForType(world, type, rank);
  if(vehicle!=null)
    {
    List<IAmmoType> vehicleAmmoTypes = vehicle.vehicleType.getValidAmmoTypes();
    for(int i = 0; i < vehRule.ammoTypes.length && i <vehicle.inventory.ammoInventory.getSizeInventory(); i++)
      {
      int ammoType = vehRule.ammoTypes[i];
      if(ammoType>vehicleAmmoTypes.size())
        {
        ammoType = this.random.nextInt(vehicleAmmoTypes.size());
        }
      ItemStack ammoStack = vehicleAmmoTypes.get(ammoType).getAmmoStack(64);
      vehicle.inventory.ammoInventory.setInventorySlotContents(i, ammoStack);
      }
    List<IVehicleUpgradeType> vehicleUpgradeTypes = vehicle.vehicleType.getValidUpgrades();
    for(int i = 0; i < vehRule.upgradeTypes.length && i < vehicle.inventory.upgradeInventory.getSizeInventory(); i++)
      {
      int upgradeType = vehRule.upgradeTypes[i];
      if(upgradeType>=vehicleUpgradeTypes.size())
        {
        upgradeType = this.random.nextInt(vehicleUpgradeTypes.size());
        }
      ItemStack upgradeStack = vehicleUpgradeTypes.get(upgradeType).getUpgradeStack(1);
      vehicle.inventory.upgradeInventory.setInventorySlotContents(i, upgradeStack);
      }
    List<IVehicleArmorType> vehicleArmors = vehicle.vehicleType.getValidArmors();
    for(int i = 0; i < vehRule.armorFrontTypes.length && i < vehicle.inventory.armorInventory.getSizeInventory(); i++)
      {
      int armorType = vehRule.armorFrontTypes[i];
      if(armorType>=vehicleArmors.size())
        {
        armorType = this.random.nextInt(vehicleArmors.size());
        }
      ItemStack upgradeStack = vehicleArmors.get(armorType).getArmorStack(1);
      vehicle.inventory.armorInventory.setInventorySlotContents(i, upgradeStack);
      }    
    vehicle.setPosition(x+0.5d, y, z+0.5d);
    float rotation = (float)parentRule.orientation * -45.f;
    rotation -= 90 * this.getRotationAmt(facing);
    vehicle.rotationYaw = vehicle.prevRotationYaw = rotation;
    world.spawnEntityInWorld(vehicle);
    }
  }

protected void placeNPC(World world, int x, int y, int z, int npcType, int overrideType, int overrideRank, int overrideTeam)
  {
  //get npc rule from struct, apply overrides, get npc from registry, apply upgrades
  //spawn npc
  }

protected void placeGate(World world, int x, int y, int z, int gateType, int overrideType, int overrideRank, int overrideTeam)
  {
  //TODO re-do templates to include a gate reference number, to know what 'blocks' belong to what gate.  these will become a type of
  //    gate 'rule', if a simple one
  //use gateType if override.  If gate # does not exist, create.
  //add piece to gate
  //at end of build, construct gates
  //TODO how to store gate stuff---scan building upon reload?
  
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
    int[] tables = new int[]{0,1,2,3,4,5,6,7};
    List<ItemStack> loot = null;
    if(name.toLowerCase().startsWith("easy"))
      {
      loot = LootGenerator.instance().getRandomLoot(30, 3, 15, tables, random);
      }
    else if(name.toLowerCase().startsWith("medium"))
      {
      loot = LootGenerator.instance().getRandomLoot(65, 6, 20, tables, random);
      }
    else if(name.toLowerCase().startsWith("hard"))
      {
      loot = LootGenerator.instance().getRandomLoot(105, 6, 25, tables, random);
      }
    if(loot!=null)
      {
      int meta = 0;
      createChestAt(world, x, y, z, meta);
      setChestLoot(world, x, y, z, loot);
      }   
    return;
    }
  else
    {
    world.setBlock(x, y, z, 0);
    //TODO handle other special rules (custom chests, ?)
    }
  }

protected void createChestAt(World world, int x, int y, int z, int meta)
  {
  world.setBlockAndMetadataWithUpdate(x, y, z, 54, 0, true);
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
  world.setBlockAndMetadata(x, y, z, Block.mobSpawner.blockID, 0);
  TileEntityMobSpawner ent = (TileEntityMobSpawner) world.getBlockTileEntity(x, y, z);  
  if(ent==null)
    {
    return;
    }
  ent.setMobID(name);
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
  tag.setInteger("dim", this.world.getWorldInfo().getDimension());
  tag.setInteger("bX", this.buildPos.x);
  tag.setInteger("bY", this.buildPos.y);
  tag.setInteger("bZ", this.buildPos.z);
  tag.setInteger("ovT", this.overrideTeam);
  tag.setInteger("ovV", this.overrideVehicle);
  tag.setInteger("ovN", this.overrideNPC);
  tag.setInteger("ovG", this.overrideGate);
  tag.setInteger("ovR", this.overrideRank);
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
  this.overrideVehicle = tag.getInteger("ovV");
  this.overrideNPC = tag.getInteger("ovN");
  this.overrideGate = tag.getInteger("ovG");
  this.overrideRank = tag.getInteger("ovR");
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
    Config.logError("Severe error loading ticked builder from disc.  The structure it was building has been changed.  The builder will be invalidated.");
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
