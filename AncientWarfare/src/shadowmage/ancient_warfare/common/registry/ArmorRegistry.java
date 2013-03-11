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
package shadowmage.ancient_warfare.common.registry;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.common.item.AWItemBase;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.armors.VehicleArmorStone;

public class ArmorRegistry
{

public static IVehicleArmorType armorStone = new VehicleArmorStone(0);



private ArmorRegistry(){}
private static ArmorRegistry INSTANCE;
private Map<Integer, IVehicleArmorType> armorInstances = new HashMap<Integer, IVehicleArmorType>();

public static ArmorRegistry instance()
  {
  if(INSTANCE==null){INSTANCE = new ArmorRegistry();}
  return INSTANCE;
  }

public void registerArmorTypes()
  {
  this.registerArmorType(armorStone);
  }

public void registerArmorType(IVehicleArmorType armor)
  {
  AWItemBase item = ItemLoader.armorItem; 
  ItemLoader.instance().addSubtypeToItem(item, armor.getGlobalArmorType(), armor.getDisplayName(), armor.getDisplayTooltip());  
  this.armorInstances.put(armor.getGlobalArmorType(), armor);
  }

public IVehicleArmorType getArmorType(int type)
  {
  return this.armorInstances.get(type);
  }

public IVehicleArmorType getArmorForStack(ItemStack stack)
  {
  if(stack!=null && stack.itemID==ItemLoader.armorItem.itemID)
    {
    return armorInstances.get(stack.getItemDamage());
    }      
  return null;
  }


}
