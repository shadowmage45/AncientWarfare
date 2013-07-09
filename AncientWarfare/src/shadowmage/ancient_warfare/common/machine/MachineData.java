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
package shadowmage.ancient_warfare.common.machine;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.registry.DescriptionRegistry2;
import shadowmage.ancient_warfare.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import cpw.mods.fml.common.registry.GameRegistry;

/**
 * static data and reference class for machines, to give a central organization point
 * @author Shadowmage
 *
 */
public class MachineData
{

/**
 * 
 */
public MachineData()
  {
  // TODO Auto-generated constructor stub
  }

public static TileEntity getTEFor(int dmg)
  {
  switch(dmg)
  {
  case 0:
  return new TETrashcan();
  case 1:
  return new TEMailBox();
  }
  return null;
  }

public static void registerIcons(IconRegister registry, Description d)
  {
  d.setIcon(registry.registerIcon("ancientwarfare:machine/trashFront"), 0);//trash front
  d.setIcon(registry.registerIcon("ancientwarfare:machine/trashSide"), 1);//trash left
  d.setIcon(registry.registerIcon("ancientwarfare:machine/trashSide"), 2);//trash rear
  d.setIcon(registry.registerIcon("ancientwarfare:machine/trashSide"), 3);//trash right
  d.setIcon(registry.registerIcon("ancientwarfare:machine/trashBottom"), 4);//trash bottom
  d.setIcon(registry.registerIcon("ancientwarfare:machine/trashTop"), 5);//trash top  
  
  d.setIcon(registry.registerIcon("ancientwarfare:machine/mailFront"), 6);//trash front
  d.setIcon(registry.registerIcon("ancientwarfare:machine/mailSide"), 7);//trash left
  d.setIcon(registry.registerIcon("ancientwarfare:machine/mailSide"), 8);//trash rear
  d.setIcon(registry.registerIcon("ancientwarfare:machine/mailSide"), 9);//trash right
  d.setIcon(registry.registerIcon("ancientwarfare:machine/mailBottom"), 10);//trash bottom
  d.setIcon(registry.registerIcon("ancientwarfare:machine/mailTop"), 11);//trash top
  }

public static void addSubBlocks(List list)
  {
  
  }

public static Icon getIcon(TileEntity te, int meta, int side)
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(BlockLoader.machineBlock.blockID);  
  if(te instanceof TEMachine)
    {
    /**
     * 0=f
     * 1=r
     * 2=re
     * 3=l
     * 4=bot
     * 5=top
     */
    TEMachine tem = (TEMachine)te;
    int machine = meta;    
    machine*=6;    
    if(side==0)//bottom
      {
      return d.getIconFor(machine+4);
      }
    else if(side==1)//top
      {
      return d.getIconFor(machine+5);
      }
    else
      {
      side = BlockTools.getCardinalFromSide(side);    
      int rot = (tem.getRotation() + side) % 4;
      return d.getIconFor(machine+rot);      
      }
    } 
  if(side==0)
    {
    return d.getIconFor(meta*6+4);
    }
  else if(side==1)
    {
    return d.getIconFor(meta*6+5);
    }
  else if(side==2)
    {
    return d.getIconFor(meta*6+0);
    }
  return d.getIconFor(meta*6+1);
  }

public static void registerBlockData()
  {
  Description d = DescriptionRegistry2.instance().getDescriptionFor(BlockLoader.machineBlock.blockID);
  
  GameRegistry.registerTileEntity(TETrashcan.class, "Trashcan");
  d.addDisplayStack(new ItemStack(BlockLoader.machineBlock,1,0));
  d.setName("Trash Disposal", 0);  
  
  GameRegistry.registerTileEntity(TEMailBox.class, "Mailbox");
  d.addDisplayStack(new ItemStack(BlockLoader.machineBlock,1,1));
  d.setName("Mailbox", 1);
  }

}
