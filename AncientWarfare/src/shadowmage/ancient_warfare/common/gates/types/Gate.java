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

import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.gates.EntityGate;
import shadowmage.ancient_warfare.common.gates.IGateType;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

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
protected int maxHealth = 40;
protected int modelType = 0;

protected float moveSpeed = 0.5f *0.05f; ///1/2 block / second

protected ItemStack displayStack;

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
  return texture;
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

public static Gate getGateByID(int id)
  {
  if(id>=0 && id<gateTypes.length)
    {
    return gateTypes[id];
    }
  return basicWood;
  }

public static EntityGate constructGate(World world, BlockPosition pos1, BlockPosition pos2, Gate type, byte facing)
  {
  EntityGate ent = new EntityGate(world);
  ent.setGateType(type);
  ent.setHealth(type.getMaxHealth());
  if(pos1.x==pos2.x)
    {
    if(facing==1 || facing==3)
      {
      facing++;
      facing %= 4;
      }
    }
  else if(pos1.z==pos2.z)
    {
    if(facing==0 || facing==2)
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
  d.setIconTexture(g.getIconTexture(), g.getGlobalID());  
  }



}
