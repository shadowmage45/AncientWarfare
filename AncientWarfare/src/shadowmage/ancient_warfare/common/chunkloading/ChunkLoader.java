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
package shadowmage.ancient_warfare.common.chunkloading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.machine.TEChunkLoader;
import shadowmage.ancient_warfare.common.utils.BlockPosition;

public class ChunkLoader implements LoadingCallback, OrderedLoadingCallback
{

private ChunkLoader()
  {
  }

private static ChunkLoader INSTANCE;
public static ChunkLoader instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new ChunkLoader();
    }
  return INSTANCE;
  }

private HashMap<Integer, ArrayList<Ticket>> dimensionTickets = new HashMap<Integer, ArrayList<Ticket>>();


/**
 * first pass, cull tickets
 */
@Override
public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount)
  {
  return tickets;
  }

@Override
public void ticketsLoaded(List<Ticket> tickets, World world)
  {
  //this.dimensionTickets.get(world.getWorldInfo().getDimension()).addAll(tickets);
  for(Ticket tk : tickets)
    {
    NBTTagCompound tag = tk.getModData();
    if(tag==null)
      {
      
      }
    else if(tag!=null && tag.hasKey("buildTE"))
      {
      tag = tag.getCompoundTag("buildTE");
      BlockPosition tePos = new BlockPosition(tag.getCompoundTag("pos"));
//      TEBuilder builder = (TEBuilder) world.getBlockTileEntity(tePos.x, tePos.y, tePos.z);
//      if(builder!=null)
//        {
//        builder.setTicket(tk);
//        }
      }  
    else if(tag!=null && tag.hasKey("chunkTE"))
      {
      tag = tag.getCompoundTag("chunkTE");
      BlockPosition tePos = new BlockPosition(tag.getCompoundTag("pos"));
      TileEntity te =  world.getBlockTileEntity(tePos.x, tePos.y, tePos.z);
      if(te instanceof TEChunkLoader)
        {
        Config.logDebug("sending chunk ticket to TE on load");
        ((TEChunkLoader)te).setTicket(tk);
        }
      }
    else//not null, does not have buildTE key
      {
      
      }
    
    /**
     * if has made it this far...
     */
    
    }
  }

}
