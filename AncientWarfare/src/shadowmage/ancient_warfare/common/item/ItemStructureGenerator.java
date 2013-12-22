/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
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
package shadowmage.ancient_warfare.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.build.BuilderInstant;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureBuildSettings;
import shadowmage.ancient_warfare.common.utils.BlockPosition;
import shadowmage.ancient_warfare.common.utils.BlockTools;
import shadowmage.ancient_warfare.common.world_gen.WorldGenManager;
import shadowmage.ancient_warfare.common.world_gen.WorldGenStructureEntry;

public class ItemStructureGenerator extends ItemBuilderInstant
{



/**
 * @param itemID
 */
public ItemStructureGenerator(int itemID)
  {
  super(itemID);
  }


@Override
public boolean attemptConstruction(World world, ProcessedStructure struct,   BlockPosition hit, int facing, StructureBuildSettings settings)
  {
  if(!struct.isLocked())
    {
    int chunkX = hit.x/16;
    int chunkZ = hit.z/16;
    int x = chunkX*16 + world.rand.nextInt(16);
    int z = chunkZ*16 + world.rand.nextInt(16);
    int y = 0;
    boolean placed = false;   
    int dim = world.provider.dimensionId;
    int face = world.rand.nextInt(4);
    String biomeName = world.getBiomeGenForCoords(x, z).biomeName;
    y = WorldGenManager.instance().getTopBlockHeight(world, x, z, struct.maxWaterDepth, struct.maxLavaDepth, struct.validTargetBlocks)+1;
    WorldGenStructureEntry ent = struct.getWorldGenEntry();
    if(ent!=null)
      {
      WorldGenManager.instance().setGeneratedAt(dim, x, y, z, face, ent.value, ent.name, ent.unique);    
      } 
    else
      {
      return false;
      }

    placed = WorldGenManager.instance().validatePlacementSurface(world, x, y, z, face, struct, world.rand);    
    WorldGenManager.instance().removeGenEntryFor(dim, x, y, z, face, ent.value, ent.name, ent.unique);
    
    if(placed)
      {
      Config.log("Ancient Warfare generating structure at: "+x+","+y+","+z +" :: "+struct.name);
      Config.logDebug("generated : "+struct.name + " unique: "+ent.unique + " in biome: "+biomeName + " has exclusions of: "+ent.biomesNot + "  has inclusions of: "+ent.biomesOnly);
      WorldGenManager.instance().setGeneratedAt(dim, x, y, z, face, ent.value, ent.name, ent.unique);        
      BuilderInstant builder = new BuilderInstant(world, struct, face, new BlockPosition(x,y,z));
      builder.setWorldGen();
      builder.startConstruction();
      }
    else
      {
      
      Config.logDebug("placement fail");
      }
    return true;
    }
  return false;
  }

@Override
public boolean onUsedFinal(World world, EntityPlayer player, ItemStack stack, BlockPosition hit, int side)
  {   
  if(world.isRemote)
    {
    return true;
    }
  if(player.capabilities.isCreativeMode && isShiftClick(player))
    {
    openGUI(player);
    return true;
    }
  if(hit==null)
    {
    hit = new BlockPosition(player.posX, player.posY, player.posZ);
    }
//  hit = BlockTools.offsetForSide(hit, side);   
  NBTTagCompound tag;
  if(stack.hasTagCompound() && stack.getTagCompound().hasKey("structData"))
    {
    tag = stack.getTagCompound().getCompoundTag("structData");
    }
  else
    {
    tag = new NBTTagCompound();
    }
  if(tag.hasKey("name") && hit !=null)
    {    
    StructureBuildSettings settings = StructureBuildSettings.constructFromNBT(tag);
    ProcessedStructure struct = StructureManager.instance().getStructureServer(tag.getString("name"));
    if(struct==null)
      {
      Config.logError("Structure Manager returned NULL structure to build for name : "+tag.getString("name"));      
      tag = new NBTTagCompound();
      stack.setTagInfo("structData", tag);
      return true;
      }
    if(!this.attemptConstruction(world, struct, hit, BlockTools.getPlayerFacingFromYaw(player.rotationYaw), settings))
      {
      player.addChatMessage("Structure is currently locked for editing!!");
      }
    else
      {
      if(!player.capabilities.isCreativeMode)
        {
        ItemStack item = player.getHeldItem();
        if(item!=null)
          {
          item.stackSize--;
          if(item.stackSize<=0)
            {          
            player.setCurrentItemOrArmor(0, null);
            }
          }
        }
      }
    }

  return true;
  }


}
