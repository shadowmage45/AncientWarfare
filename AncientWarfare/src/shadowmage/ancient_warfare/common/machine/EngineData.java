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
package shadowmage.ancient_warfare.common.machine;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import shadowmage.ancient_framework.common.registry.entry.Description;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.plugins.PluginProxy;
import cpw.mods.fml.common.registry.GameRegistry;

public class EngineData extends MachineData
{

protected EngineData()
  {
  }

public static TileEntity getTEFor(int dmg)
  {
  switch(dmg)
  {
  case 0:
  return PluginProxy.bcProxy.getHandCrankEngineTE();  
  }
  return PluginProxy.bcProxy.getHandCrankEngineTE();  
  }

public static String getEngineTexture(int meta)
  {
  switch(meta)
  {
  case 0:
  return "engineHandCrank.png";  
  case 1:
  return "engineWaterwheel.png";  
  case 2:
  default:
  return "engineHandCrank.png";  
  }
  }

public static void registerIcons(IconRegister registry, Description d)
  {
  d.setIcon(registry.registerIcon("ancientwarfare:machine/chunkSimpleSide"), 0);//trash front
  d.setIcon(registry.registerIcon("ancientwarfare:machine/chunkDeluxeSide"), 1);//trash left
  d.setIcon(registry.registerIcon("ancientwarfare:machine/chunkDeluxeSide"), 2);//trash rear
  d.setIcon(registry.registerIcon("ancientwarfare:machine/chunkDeluxeSide"), 3);//trash right
  d.setIcon(registry.registerIcon("ancientwarfare:machine/chunkDeluxeSide"), 4);//trash bottom
  d.setIcon(registry.registerIcon("ancientwarfare:machine/chunkDeluxeSide"), 5);//trash top  
  }

public static void addSubBlocks(List list)
  {
  
  }

public static void registerBlockData()
  {
  Description d = BlockLoader.engineBlock.description;
  
  GameRegistry.registerTileEntity(PluginProxy.bcProxy.getHandCrankEngineClass(), "Hand Cranked Engine");
  if(PluginProxy.bcLoaded)
    {
    d.addDisplayStack(new ItemStack(BlockLoader.engineBlock,1,0));
    d.setName("block.multi.engine.0", 0);
    }
  
  }
}
