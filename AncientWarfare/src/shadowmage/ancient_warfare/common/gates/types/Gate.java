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
package shadowmage.ancient_warfare.common.gates.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.gates.IGateType;
import shadowmage.ancient_warfare.common.gates.TEGateProxy;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public abstract class Gate implements IGateType
{

public static final Gate[] gateTypes = new Gate[16];


public static final Gate basicWood = new GateBasicWood(0);
public static final Gate basicIron = new GateBasicIron(1);

public static final Gate singleWood = new GateSingleSlideWood(4);
public static final Gate singleIron = new GateSingleIron(5);

public static final Gate doubleWood = new GateDoubleSlideWood(8);
public static final Gate doubleIron = new GateDoubleIron(9);

public static final Gate rotatingBridge = new GateRotatingBridge(12);


protected int globalID = 0;
protected String displayName = "";
protected String tooltip = "";
protected String texture = "";
protected String iconTexture = "";
protected int maxHealth = 40;
protected int modelType = 0;

protected boolean canSoldierInteract = true;

protected float moveSpeed = 0.5f *0.05f; ///1/2 block / second

protected ItemStack displayStack;

protected Set<Integer> neededResearch = new HashSet<Integer>();
protected List<ItemStackWrapperCrafting> resourceStacks = new ArrayList<ItemStackWrapperCrafting>();
/**
 * 
 */
public Gate(int id)
  {
  this.globalID = id;
  if(id>=0 && id < gateTypes.length && gateTypes[id]==null)
    {
    gateTypes[id] = this;
    }
  this.displayStack = new ItemStack(ItemLoader.gateSpawner,1,id);
  }

@Override
public String getIconTexture()
  {
  return iconTexture;
  }

@Override
public int getGlobalID()
  {  
  return globalID;
  }

@Override
public int getModelType()
  {
  return modelType;
  }


@Override
public String getDisplayName()
  {
  return displayName;
  }

@Override
public String getTooltip()
  {
  return tooltip;
  }

@Override
public ItemStack getConstructingItem()
  {
  return new ItemStack(ItemLoader.gateSpawner,1,this.globalID);
  }

@Override
public ItemStack getDisplayStack()
  {
  return displayStack;
  }

@Override
public int getMaxHealth()
  {
  return maxHealth;
  }

@Override
public float getMoveSpeed()
  {
  return moveSpeed;
  }

@Override
public String getTexture()
  {
  return texture;
  }

@Override
public boolean canActivate(EntityGate gate, boolean open)
  {
  return true;
  }

@Override
public boolean canSoldierActivate()
  {
  return canSoldierInteract;
  }

public static Gate getGateByID(int id)
  {
  if(id>=0 && id<gateTypes.length)
    {
    return gateTypes[id];
    }
  return basicWood;
  }


@Override
public void onUpdate(EntityGate ent)
  {
  // TODO Auto-generated method stub
  }

@Override
public void setCollisionBoundingBox(EntityGate gate)
  {
  if(gate.pos1==null || gate.pos2==null){return;}
  boolean wideOnXAxis = gate.pos1.x!=gate.pos2.x;  
  BlockPosition min = BlockTools.getMin(gate.pos1, gate.pos2);
  BlockPosition max = BlockTools.getMax(gate.pos1, gate.pos2);
  if(gate.edgePosition>0)
    {
    gate.boundingBox.setBounds(min.x, max.y+0.5d, min.z, max.x+1, max.y+1, max.z+1);
    }
  else
    {
    gate.boundingBox.setBounds(min.x, min.y, min.z, max.x+1, max.y+1, max.z+1);    
    }    
  }

@Override
public boolean arePointsValidPair(BlockPosition pos1, BlockPosition pos2)
  {
  return pos1.x == pos2.x || pos1.z == pos2.z;
  }

@Override
public void setInitialBounds(EntityGate gate, BlockPosition pos1, BlockPosition pos2)
  {
  BlockPosition min = BlockTools.getMin(pos1, pos2);
  BlockPosition max = BlockTools.getMax(pos1, pos2);
  boolean wideOnXAxis = min.x!=max.x;
  float width = wideOnXAxis ? max.x-min.x+1 : max.z-min.z + 1;
  float xOffset = wideOnXAxis ? width*0.5f: 0.5f;
  float zOffset = wideOnXAxis ? 0.5f : width*0.5f;
  gate.pos1 = min;
  gate.pos2 = max;
  gate.edgeMax = max.y - min.y + 1;
  gate.setPosition(min.x+xOffset, min.y, min.z+zOffset);
  }

@Override
public void onGateStartOpen(EntityGate gate)
  {
  if(gate.worldObj.isRemote)
    {
    return;
    }
  int id;
  BlockPosition min = BlockTools.getMin(gate.pos1, gate.pos2);
  BlockPosition max = BlockTools.getMax(gate.pos1, gate.pos2);
  for(int x = min.x; x <= max.x; x++)
    {
    for(int y = min.y; y <=max.y; y++)
      {
      for(int z = min.z; z<= max.z; z++)
        {
        id = gate.worldObj.getBlockId(x, y, z);
        if(id==BlockLoader.gateProxy.blockID)
          {
          gate.worldObj.setBlockToAir(x, y, z);
          }
        }
      }
    }
  }

@Override
public void onGateFinishOpen(EntityGate gate)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void onGateStartClose(EntityGate gate)
  {
  // TODO Auto-generated method stub
  
  }

@Override
public void onGateFinishClose(EntityGate gate)
  {
  if(gate.worldObj.isRemote)
    {
    return;
    }
  int id;
  BlockPosition min = BlockTools.getMin(gate.pos1, gate.pos2);
  BlockPosition max = BlockTools.getMax(gate.pos1, gate.pos2);
  for(int x = min.x; x <= max.x; x++)
    {
    for(int y = min.y; y <=max.y; y++)
      {
      for(int z = min.z; z<= max.z; z++)
        {
        id = gate.worldObj.getBlockId(x, y, z);
        if(id==0)
          {
          gate.worldObj.setBlock(x, y, z, BlockLoader.gateProxy.blockID);
          TileEntity te = gate.worldObj.getBlockTileEntity(x, y, z);
          if(te!=null && te instanceof TEGateProxy)
            {
            TEGateProxy teg = (TEGateProxy)te;
            teg.setOwner(gate);
            }
          }
        }
      }
    }
  }

/**
 * 
 * @param world
 * @param pos1
 * @param pos2
 * @param type
 * @param facing
 * @return a fully setup gate, or null if chosen spawn position is invalid (blocks in the way) 
 */
public static EntityGate constructGate(World world, BlockPosition pos1, BlockPosition pos2, Gate type, byte facing)
  {
  EntityGate ent = new EntityGate(world);
  BlockPosition min = BlockTools.getMin(pos1, pos2);
  BlockPosition max = BlockTools.getMax(pos1, pos2);
  for(int x = min.x; x <=max.x ;x++)
    {
    for(int y = min.y; y <=max.y ;y++)
      {
      for(int z = min.z; z <=max.z ;z++)
        {
        if(world.getBlockId(x, y, z)!=0)
          {
          return null;
          }
        }
      }
    }
  ent.setGateType(type);
  ent.setHealth(type.getMaxHealth());
  if(pos1.x==pos2.x)
    {
    if(facing==0 || facing==2)
      {
      facing++;
      facing %= 4;
      }
    }
  else if(pos1.z==pos2.z)
    {
    if(facing==1 || facing==3)
      {
      facing++;
      facing %= 4;
      }
    }  
  ent.gateOrientation = facing;
  type.setInitialBounds(ent, pos1, pos2);
  type.onGateFinishClose(ent);
  
  return ent;
  }

public static ItemStack getItemToConstruct(int type)
  {
  return getGateByID(type).getConstructingItem();
  }

public static void registerGateTypes()
  {
  for(Gate g : gateTypes)
    {
    if(g!=null)
      {
      registerGateType(g);
      }
    }
  }

private static void registerGateType(IGateType g)
  {
  Description d = ItemLoader.instance().addSubtypeInfoToItem(ItemLoader.gateSpawner, g.getGlobalID(), g.getDisplayName(), "", g.getTooltip());
  d.addDisplayStack(g.getDisplayStack());
  d.setIconTexture("ancientwarfare:gate/"+g.getIconTexture(), g.getGlobalID());  
  }

@Override
public ResourceListRecipe constructRecipe()
  {
  ResourceListRecipe recipe = new ResourceListRecipe(getConstructingItem(), RecipeType.CIVIC_MISC);
  recipe.addNeededResearch(getNeededResearch());
  if(!this.resourceStacks.isEmpty())
    {
    recipe.addResources(resourceStacks);
    }  
  else
    {
    recipe.addResource(new ItemStack(Item.paper), 1, false, false);
    }
  return recipe;
  }

@Override
public Collection<Integer> getNeededResearch()
  {
  return this.neededResearch;
  }

}
