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
package shadowmage.ancient_warfare.common.network;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.client.gui.settings.GuiClientSettings;
import shadowmage.ancient_warfare.client.gui.structure.GuiCSB;
import shadowmage.ancient_warfare.client.gui.structure.GuiEditorSelect;
import shadowmage.ancient_warfare.client.gui.structure.GuiStructureScanner;
import shadowmage.ancient_warfare.client.gui.structure.GuiSurvivalBuilder;
import shadowmage.ancient_warfare.client.gui.vehicle.GuiVehicleDebug;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.container.ContainerBase;
import shadowmage.ancient_warfare.common.container.ContainerCSB;
import shadowmage.ancient_warfare.common.container.ContainerDummy;
import shadowmage.ancient_warfare.common.container.ContainerEditor;
import shadowmage.ancient_warfare.common.container.ContainerStructureScanner;
import shadowmage.ancient_warfare.common.container.ContainerSurvivalBuilder;
import shadowmage.ancient_warfare.common.container.ContainerVehicle;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.IGuiHandler;

public class GUIHandler implements IGuiHandler
{

/**
 * GUI IDs....listed here for...uhh...keeping track of them
 */
public static final int STRUCTURE_SELECT = 0;
public static final int STRUCTURE_SCANNER = 1;
public static final int STRUCTURE_BUILD_DIRECT = 2;
public static final int STRUCTURE_EDITOR = 3;
public static final int STRUCTURE_SCAN_EDIT = 4;
public static final int SETTINGS = 5;
public static final int VEHICLE_DEBUG = 99;



private static GUIHandler INSTANCE;
private GUIHandler(){}
public static GUIHandler instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE= new GUIHandler();
    }
  return INSTANCE;
  }

@Override
public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
  switch(ID)
  {
  case STRUCTURE_SELECT:
  return new ContainerCSB(player);
  
  case STRUCTURE_SCANNER:
  return new ContainerStructureScanner(player);
  
  case STRUCTURE_BUILD_DIRECT:
  return new ContainerSurvivalBuilder(player);
  
  case STRUCTURE_EDITOR:  
  return new ContainerEditor(player);
  
  case STRUCTURE_SCAN_EDIT:
  ContainerEditor edit = new ContainerEditor(player);
  return edit;
  
  case SETTINGS:
  return new ContainerDummy();
  
  case 6:
  return null;
  case 7:
  return null;
  case 8:
  return null;
  case 9:
  return null;
  case 10:
  return null;  
  case VEHICLE_DEBUG:
  VehicleBase vehicle = (VehicleBase)world.getEntityByID(x);
  if(vehicle!=null)
    {
    return new ContainerVehicle(player, vehicle, vehicle);
    }
  return null;//TODO make/set gui & container..
  } 
  return null;
  }

@Override
public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {  
  switch(ID)
  {
  case STRUCTURE_SELECT:
  return new GuiCSB(new ContainerCSB(player));
  
  case STRUCTURE_SCANNER:
  return new GuiStructureScanner(new ContainerStructureScanner(player));
  
  case STRUCTURE_BUILD_DIRECT:
  return new GuiSurvivalBuilder(new ContainerSurvivalBuilder(player));
  
  case STRUCTURE_EDITOR:  
  return new GuiEditorSelect(new ContainerEditor(player));
  
  case STRUCTURE_SCAN_EDIT:  
  return new GuiEditorSelect(new ContainerEditor(player));
  
  case SETTINGS:
  return new GuiClientSettings(new ContainerDummy());
  
  case 6:
  return null;
  case 7:
  return null;
  case 8:
  return null;
  case 9:
  return null;
  case 10:
  return null;  
  case VEHICLE_DEBUG:
  VehicleBase vehicle = (VehicleBase)world.getEntityByID(x);
  if(vehicle!=null)
    {
    return new GuiVehicleDebug(new ContainerVehicle(player, vehicle, vehicle));
    }
  return null;//TODO make/set gui & container..
  } 
  return null;
  }

/**
 * auto-wrapper for sending an openGUI packet from client-server to open a server side GUI without
 * special scripting in every damn entity/TE, also handles sending init data after the GUI is opened
 * all synched containers must openGUI through here, or they must handle synching manually
 * @param ID
 * @param player
 * @param world
 * @param x
 * @param y
 * @param z
 */
public void openGUI(int ID, EntityPlayer player, World world, int x, int y, int z)
  {
  if(player.worldObj.isRemote)
    {
    Packet03GuiComs pkt = new Packet03GuiComs();
    pkt.setGuiToOpen((byte)ID, x, y, z);
    AWCore.proxy.sendPacketToServer(pkt);
    }
  else
    {
    FMLNetworkHandler.openGui(player, AWCore.instance, ID, world, x, y, z);
    if(player.openContainer instanceof ContainerBase)
      {
      List<NBTTagCompound> packetTags = ((ContainerBase)player.openContainer).getInitData();
      if(packetTags!=null)
        {
        for(NBTTagCompound tag : packetTags)
          {
          if(tag!=null)
            {
            Packet03GuiComs pkt = new Packet03GuiComs();
            pkt.setInitData(tag);
            AWCore.proxy.sendPacketToPlayer(player, pkt);
            }
          }
        }
      }
    }
  }

}
