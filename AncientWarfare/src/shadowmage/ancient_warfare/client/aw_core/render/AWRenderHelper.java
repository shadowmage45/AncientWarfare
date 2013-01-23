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
package shadowmage.ancient_warfare.client.aw_core.render;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import shadowmage.ancient_warfare.client.aw_structure.render.BoundingBoxRender;
import shadowmage.ancient_warfare.common.aw_core.block.BlockPosition;
import shadowmage.ancient_warfare.common.aw_core.block.BlockTools;
import shadowmage.ancient_warfare.common.aw_core.item.ItemLoader;
import shadowmage.ancient_warfare.common.aw_core.utils.Pos3f;
import shadowmage.ancient_warfare.common.aw_structure.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.aw_structure.item.ItemStructureBuilderCreative;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class AWRenderHelper implements ITickHandler
{

private static AWRenderHelper INSTANCE;
private AWRenderHelper()
  {  
  }
public static AWRenderHelper instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new AWRenderHelper();
    }
  return INSTANCE;
  }

@Override
public void tickStart(EnumSet<TickType> type, Object... tickData)
  {
  
  }

@Override
public void tickEnd(EnumSet<TickType> type, Object... tickData)
  {
  //System.out.println("rendering from tick");
  //this.renderStructureBB();
  //System.out.println("END rendering from tick");
  }

private void renderStructureBB()
  {
  Minecraft mc = Minecraft.getMinecraft();
  if(mc==null)
    {
    return;
    }
  EntityPlayer player = mc.thePlayer;
  if(player==null)
    {
    return;
    } 
  ItemStack stack = player.inventory.getCurrentItem();
  if(stack==null || stack.getItem()==null)
    {
    return;
    }
  int id = stack.itemID;
  BlockPosition hit = BlockTools.getBlockClickedOn(player, player.worldObj, true);
  if(hit==null)
    {
    return;
    }
  
  if(id == ItemLoader.structureCreativeBuilder.itemID || id == ItemLoader.structureCreativeBuilderTicked.itemID || id==ItemLoader.structureBuilderDirect.itemID)
    {
    if(stack.hasTagCompound() && stack.getTagCompound().hasKey("structData"))
      {
      NBTTagCompound tag = stack.getTagCompound().getCompoundTag("structData");
      if(id==ItemLoader.structureBuilderDirect.itemID)
        {               
        if(tag.hasKey("clientData"))
          {
          tag = tag.getCompoundTag("clientData");
          BlockPosition size = new BlockPosition(tag.getCompoundTag("size"));
          BlockPosition offset = new BlockPosition(tag.getCompoundTag("offset"));        
          Pos3f playerOffset = new Pos3f(player.posX, player.posY, player.posZ);
          int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
          BoundingBoxRender.renderBoundingBox(hit, face, offset, size, playerOffset);   
          } 
        }
      else
        {
        if(tag.hasKey("name"))
          {
          StructureClientInfo struct = StructureManager.instance().getClientStructure(tag.getString("name"));
          if(struct!=null)
            {           
            BlockPosition size = new BlockPosition(struct.xSize, struct.ySize, struct.zSize);           
            Pos3f playerOffset = new Pos3f(player.posX, player.posY, player.posZ);
            BlockPosition offset = new BlockPosition (struct.xOffset, struct.yOffset, struct.zOffset);
            int face = BlockTools.getPlayerFacingFromYaw(player.rotationYaw);
            BoundingBoxRender.renderBoundingBox(hit, face, offset, size, playerOffset);              
            }
          }
        }      
      }
    }  
  }

@Override
public EnumSet<TickType> ticks()
  {
  return EnumSet.of(TickType.RENDER);
  }

@Override
public String getLabel()
  {
  return "AWRenderTicker";
  }

@ForgeSubscribe
public void handleRenderLastEvent(RenderWorldLastEvent evt)
  {
  this.renderStructureBB();
  }
}
