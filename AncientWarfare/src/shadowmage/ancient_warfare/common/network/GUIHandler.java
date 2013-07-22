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
package shadowmage.ancient_warfare.common.network;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shadowmage.ancient_warfare.client.gui.civic.GuiCivicBase;
import shadowmage.ancient_warfare.client.gui.civic.GuiCivicTownHall;
import shadowmage.ancient_warfare.client.gui.civic.GuiCivicTownHallInfo;
import shadowmage.ancient_warfare.client.gui.civic.GuiCivicWarehouse;
import shadowmage.ancient_warfare.client.gui.crafting.GuiAlchemy;
import shadowmage.ancient_warfare.client.gui.crafting.GuiAmmoCrafting;
import shadowmage.ancient_warfare.client.gui.crafting.GuiAutoCrafting;
import shadowmage.ancient_warfare.client.gui.crafting.GuiCivilEngineering;
import shadowmage.ancient_warfare.client.gui.crafting.GuiEngineeringStation;
import shadowmage.ancient_warfare.client.gui.crafting.GuiNpcCraft;
import shadowmage.ancient_warfare.client.gui.crafting.GuiResearch;
import shadowmage.ancient_warfare.client.gui.crafting.GuiVehicleCrafting;
import shadowmage.ancient_warfare.client.gui.info.GuiResearchBook;
import shadowmage.ancient_warfare.client.gui.machine.GuiChunkloaderDeluxe;
import shadowmage.ancient_warfare.client.gui.machine.GuiFoodProcessor;
import shadowmage.ancient_warfare.client.gui.machine.GuiMailbox;
import shadowmage.ancient_warfare.client.gui.machine.GuiMailboxIndustrial;
import shadowmage.ancient_warfare.client.gui.machine.GuiTrashcan;
import shadowmage.ancient_warfare.client.gui.npc.GuiBackpack;
import shadowmage.ancient_warfare.client.gui.npc.GuiCommandBaton;
import shadowmage.ancient_warfare.client.gui.npc.GuiCourierRoutingSlip;
import shadowmage.ancient_warfare.client.gui.npc.GuiNpcBase;
import shadowmage.ancient_warfare.client.gui.npc.GuiNpcCourier;
import shadowmage.ancient_warfare.client.gui.settings.GuiClientSettings;
import shadowmage.ancient_warfare.client.gui.settings.GuiDebugInfo;
import shadowmage.ancient_warfare.client.gui.structure.GuiCSB;
import shadowmage.ancient_warfare.client.gui.structure.GuiEditorSelect;
import shadowmage.ancient_warfare.client.gui.structure.GuiStructureScanner;
import shadowmage.ancient_warfare.client.gui.structure.GuiSurvivalBuilder;
import shadowmage.ancient_warfare.client.gui.teams.GuiTeamControl;
import shadowmage.ancient_warfare.client.gui.vehicle.GuiVehicleAmmoSelection;
import shadowmage.ancient_warfare.client.gui.vehicle.GuiVehicleInventory;
import shadowmage.ancient_warfare.common.AWCore;
import shadowmage.ancient_warfare.common.civics.TECivic;
import shadowmage.ancient_warfare.common.civics.TECivicTownHall;
import shadowmage.ancient_warfare.common.civics.TECivicWarehouse;
import shadowmage.ancient_warfare.common.container.ContainerAWAutoCrafting;
import shadowmage.ancient_warfare.common.container.ContainerAWCrafting;
import shadowmage.ancient_warfare.common.container.ContainerBackpack;
import shadowmage.ancient_warfare.common.container.ContainerBase;
import shadowmage.ancient_warfare.common.container.ContainerCSB;
import shadowmage.ancient_warfare.common.container.ContainerChunkloaderDeluxe;
import shadowmage.ancient_warfare.common.container.ContainerCivicTE;
import shadowmage.ancient_warfare.common.container.ContainerCivicTownHallInfo;
import shadowmage.ancient_warfare.common.container.ContainerCivicWarehouse;
import shadowmage.ancient_warfare.common.container.ContainerCivilEngineering;
import shadowmage.ancient_warfare.common.container.ContainerCommandBaton;
import shadowmage.ancient_warfare.common.container.ContainerCourierRoutingSlip;
import shadowmage.ancient_warfare.common.container.ContainerDebugInfo;
import shadowmage.ancient_warfare.common.container.ContainerDummy;
import shadowmage.ancient_warfare.common.container.ContainerEditor;
import shadowmage.ancient_warfare.common.container.ContainerFoodProcessor;
import shadowmage.ancient_warfare.common.container.ContainerMailbox;
import shadowmage.ancient_warfare.common.container.ContainerMailboxIndustrial;
import shadowmage.ancient_warfare.common.container.ContainerNpcBase;
import shadowmage.ancient_warfare.common.container.ContainerNpcCourier;
import shadowmage.ancient_warfare.common.container.ContainerStructureScanner;
import shadowmage.ancient_warfare.common.container.ContainerSurvivalBuilder;
import shadowmage.ancient_warfare.common.container.ContainerTeamControl;
import shadowmage.ancient_warfare.common.container.ContainerTrashcan;
import shadowmage.ancient_warfare.common.container.ContainerVehicle;
import shadowmage.ancient_warfare.common.crafting.TEAWAlchemy;
import shadowmage.ancient_warfare.common.crafting.TEAWAmmoCraft;
import shadowmage.ancient_warfare.common.crafting.TEAWAutoCrafting;
import shadowmage.ancient_warfare.common.crafting.TEAWCivicCraft;
import shadowmage.ancient_warfare.common.crafting.TEAWNpcCraft;
import shadowmage.ancient_warfare.common.crafting.TEAWResearch;
import shadowmage.ancient_warfare.common.crafting.TEAWStructureCraft;
import shadowmage.ancient_warfare.common.crafting.TEAWVehicleCraft;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.machine.TEChunkLoaderDeluxe;
import shadowmage.ancient_warfare.common.machine.TEFoodProcessor;
import shadowmage.ancient_warfare.common.machine.TEMailBox;
import shadowmage.ancient_warfare.common.machine.TEMailBoxIndustrial;
import shadowmage.ancient_warfare.common.machine.TETrashcan;
import shadowmage.ancient_warfare.common.npcs.NpcBase;
import shadowmage.ancient_warfare.common.npcs.waypoints.CourierRoutingInfo;
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
public static final int TEAM_CONTROL = 6;
public static final int NPC_COMMAND_BATON = 7;
public static final int CIVIC_BASE = 8;
public static final int NPC_BASE = 9;
public static final int NPC_COURIER = 10;
public static final int COURIER_SLIP = 11;
public static final int CIVIC_WAREHOUSE = 12;
public static final int CIVIC_TOWNHALL = 13;
public static final int CIVIC_TOWNHALL_INFO = 14;
public static final int TRASHCAN = 15;
public static final int MAILBOX = 16;
public static final int MAILBOX_INDUSTRIAL = 17;
public static final int CHUNKLOADER = 18;//TODO
public static final int CHUNKLOADER_DEULXE = 19;
public static final int FOOD_PROCESSOR = 20;

public static final int BACKPACK = 39;
public static final int INFO = 40;
public static final int CIVIL_ENGINEERING = 41;
public static final int RESEARCH = 42;
public static final int ENGINEERING = 43;
public static final int VEHICLE_CRAFT = 44;
public static final int NPC_CRAFT = 45;
public static final int AMMO_CRAFT = 46;
public static final int ALCHEMY_CRAFT = 47;
public static final int AUTO_CRAFT = 48;

public static final int VEHICLE_AMMO_SELECT = 98;
public static final int VEHICLE_DEBUG = 99;
public static final int PERFORMANCE = 100;

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
  VehicleBase vehicle;
  NpcBase npc;
  TileEntity te;
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
  
  case TEAM_CONTROL:
  return new ContainerTeamControl(player);
  
  case NPC_COMMAND_BATON:
  return new ContainerCommandBaton(player);
  
  case CIVIC_BASE:  
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TECivic)
    {
    return new ContainerCivicTE(player, (TECivic)te);
    }  
  return null;
  
  case NPC_BASE:
  npc = (NpcBase)world.getEntityByID(x);
  if(npc!=null)
    {
    return new ContainerNpcBase(player, npc);
    }
  return null;
  
  case NPC_COURIER:
  npc = (NpcBase)world.getEntityByID(x);
  if(npc!=null)
    {
    return new ContainerNpcCourier(player, npc);
    }
  return null;  
  
  case COURIER_SLIP:
  ItemStack stack = player.inventory.getCurrentItem();
  if(stack!=null && stack.itemID==ItemLoader.courierRouteSlip.itemID)
    {    
    CourierRoutingInfo info = new CourierRoutingInfo(stack);
    ContainerCourierRoutingSlip container = new ContainerCourierRoutingSlip(player, info);
    return container;
    }
  return null;
  
  case CIVIC_WAREHOUSE:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TECivicWarehouse)
    {
    TECivicWarehouse tew = (TECivicWarehouse)te;
    return new ContainerCivicWarehouse(player, tew);
    }
  return null;
  
  case CIVIC_TOWNHALL:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TECivicTownHall)
    {
    TECivicTownHall tew = (TECivicTownHall)te;
    return new ContainerCivicTE(player, tew);
    }
  return null;
  
  case CIVIC_TOWNHALL_INFO:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TECivicTownHall)
    {
    TECivicTownHall tew = (TECivicTownHall)te;
    return new ContainerCivicTownHallInfo(player, tew);
    }
  return null;
  
  case TRASHCAN:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TETrashcan)
    {
    return new ContainerTrashcan(player, (TETrashcan)te);
    }
  return null;
  
  case MAILBOX:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEMailBox)
    {
    return new ContainerMailbox(player, (TEMailBox)te);
    }
  return null;
  
  case MAILBOX_INDUSTRIAL:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEMailBoxIndustrial)
    {
    return new ContainerMailboxIndustrial(player, (TEMailBoxIndustrial)te);
    }
  return null;
  
  case CHUNKLOADER_DEULXE:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEChunkLoaderDeluxe)
    {
    return new ContainerChunkloaderDeluxe(player, (TEChunkLoaderDeluxe) te);
    }
  return null;
  
  case FOOD_PROCESSOR:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEFoodProcessor)
    {
    return new ContainerFoodProcessor(player, (TEFoodProcessor) te);
    }
  return null;
  
  case INFO:
  return new ContainerDummy();
  
  case BACKPACK:
  if(player.inventory.getCurrentItem()!=null && player.inventory.getCurrentItem().itemID == ItemLoader.backpack.itemID)
    {
    return new ContainerBackpack(player);
    }
  return null;
  
  case CIVIL_ENGINEERING:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWStructureCraft)
    {
    TEAWStructureCraft tew = (TEAWStructureCraft)te;
    return new ContainerCivilEngineering(player, tew);
    }
  return null;
  
  case RESEARCH:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWResearch)
    {
    TEAWResearch tew = (TEAWResearch)te;
    return new ContainerAWCrafting(player, tew);
    }
  return null;
  
  case ENGINEERING:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWCivicCraft)
    {
    TEAWCivicCraft tew = (TEAWCivicCraft)te;
    return new ContainerAWCrafting(player, tew);
    }
  return null;
  
  case VEHICLE_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWVehicleCraft)
    {
    TEAWVehicleCraft tew = (TEAWVehicleCraft)te;
    return new ContainerAWCrafting(player, tew);
    }
  return null;
  
  case NPC_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWNpcCraft)
    {
    TEAWNpcCraft tew = (TEAWNpcCraft)te;
    return new ContainerAWCrafting(player, tew);
    }
  return null;
  
  case AMMO_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWAmmoCraft)
    {
    TEAWAmmoCraft tew = (TEAWAmmoCraft)te;
    return new ContainerAWCrafting(player, tew);
    }
  return null;
  
  case ALCHEMY_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWAlchemy)
    {
    TEAWAlchemy tew = (TEAWAlchemy)te;
    return new ContainerAWCrafting(player, tew);
    }
  return null;
  
  case AUTO_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWAutoCrafting)
    {
    TEAWAutoCrafting tew = (TEAWAutoCrafting)te;
    return new ContainerAWAutoCrafting(player, tew);
    }
  return null;
    
  case VEHICLE_AMMO_SELECT:
  return new ContainerDummy();
  
  case VEHICLE_DEBUG:
  vehicle = (VehicleBase)world.getEntityByID(x);
  if(vehicle!=null)
    {
    return new ContainerVehicle(player, vehicle, vehicle);
    }
  return null;
  
  case PERFORMANCE:
  return new ContainerDebugInfo(player);  
  
  } 
  return null;
  }

@Override
public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
  {  
  VehicleBase vehicle;
  NpcBase npc;
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
  return new GuiClientSettings(player, new ContainerDummy());
  
  case TEAM_CONTROL:
  return new GuiTeamControl(new ContainerTeamControl(player));
  
  case NPC_COMMAND_BATON:
  return new GuiCommandBaton(new ContainerCommandBaton(player));
  
  case CIVIC_BASE:  
  TileEntity te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TECivic)
    {
    return new GuiCivicBase(new ContainerCivicTE(player, (TECivic)te), (TECivic)te);
    }  
  return null;
  
  case NPC_BASE:
  npc = (NpcBase)world.getEntityByID(x);
  if(npc!=null)
    {
    return new GuiNpcBase(new ContainerNpcBase(player, npc),npc);
    }
  return null;
  
  case NPC_COURIER:
  npc = (NpcBase)world.getEntityByID(x);
  if(npc!=null)
    {
    return new GuiNpcCourier(new ContainerNpcCourier(player, npc),npc);
    }
  return null;
  
  case COURIER_SLIP:
  ItemStack stack = player.inventory.getCurrentItem();
  if(stack!=null && stack.itemID==ItemLoader.courierRouteSlip.itemID)
    {    
    CourierRoutingInfo info = new CourierRoutingInfo(stack);
    ContainerCourierRoutingSlip container = new ContainerCourierRoutingSlip(player, info);
    return new GuiCourierRoutingSlip(container);
    }
  return null;
  
  case CIVIC_WAREHOUSE:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TECivicWarehouse)
    {
    TECivicWarehouse tew = (TECivicWarehouse)te;
    return new GuiCivicWarehouse(new ContainerCivicWarehouse(player, tew), tew);
    }
  return null;
  
  case CIVIC_TOWNHALL:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TECivicTownHall)
    {
    TECivicTownHall tew = (TECivicTownHall)te;
    return new GuiCivicTownHall(new ContainerCivicTE(player, tew), tew);
    }
  return null;
  
  case CIVIC_TOWNHALL_INFO:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TECivicTownHall)
    {
    TECivicTownHall tew = (TECivicTownHall)te;
    return new GuiCivicTownHallInfo(new ContainerCivicTownHallInfo(player, tew), tew);
    }
  return null;
  
  case CIVIL_ENGINEERING:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWStructureCraft)
    {
    TEAWStructureCraft tew = (TEAWStructureCraft)te;
    return new GuiCivilEngineering(new ContainerCivilEngineering(player, tew));
    }
  return null;
  
  case TRASHCAN:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TETrashcan)
    {
    return new GuiTrashcan(new ContainerTrashcan(player, (TETrashcan)te));
    }
  return null;
  
  case MAILBOX:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEMailBox)
    {
    return new GuiMailbox(new ContainerMailbox(player, (TEMailBox)te));
    }
  return null;
  
  case MAILBOX_INDUSTRIAL:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEMailBoxIndustrial)
    {
    return new GuiMailboxIndustrial(new ContainerMailboxIndustrial(player, (TEMailBoxIndustrial)te));
    }
  return null;
  
  case CHUNKLOADER_DEULXE:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEChunkLoaderDeluxe)
    {
    return new GuiChunkloaderDeluxe(new ContainerChunkloaderDeluxe(player, (TEChunkLoaderDeluxe) te));
    }
  return null;
  
  case FOOD_PROCESSOR:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEFoodProcessor)
    {
    return new GuiFoodProcessor(new ContainerFoodProcessor(player, (TEFoodProcessor) te));
    }
  return null;
  
  case RESEARCH:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWResearch)
    {
    TEAWResearch tew = (TEAWResearch)te;
    return new GuiResearch(new ContainerAWCrafting(player, tew));
    }
  return null;
  
  case ENGINEERING:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWCivicCraft)
    {
    TEAWCivicCraft tew = (TEAWCivicCraft)te;
    return new GuiEngineeringStation(new ContainerAWCrafting(player, tew));
    }
  return null;
  
  case BACKPACK:
  if(player.inventory.getCurrentItem()!=null && player.inventory.getCurrentItem().itemID == ItemLoader.backpack.itemID)
    {
    return new GuiBackpack(new ContainerBackpack(player));   
    }
  return null;
  
  case INFO:
  return new GuiResearchBook(player);
  
  case VEHICLE_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWVehicleCraft)
    {
    TEAWVehicleCraft tew = (TEAWVehicleCraft)te;
    return new GuiVehicleCrafting(new ContainerAWCrafting(player, tew));
    }
  return null;
  
  case AMMO_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWAmmoCraft)
    {
    TEAWAmmoCraft tew = (TEAWAmmoCraft)te;
    return new GuiAmmoCrafting(new ContainerAWCrafting(player, tew));
    }
  return null;
    
  case NPC_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWNpcCraft)
    {
    TEAWNpcCraft tew = (TEAWNpcCraft)te;
    return new GuiNpcCraft(new ContainerAWCrafting(player, tew));
    }
  return null;
  
  case ALCHEMY_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWAlchemy)
    {
    TEAWAlchemy tew = (TEAWAlchemy)te;
    return new GuiAlchemy(new ContainerAWCrafting(player, tew));
    }
  return null;
  
  case AUTO_CRAFT:
  te = world.getBlockTileEntity(x, y, z);
  if(te instanceof TEAWAutoCrafting)
    {
    TEAWAutoCrafting tew = (TEAWAutoCrafting)te;
    return new GuiAutoCrafting(new ContainerAWAutoCrafting(player, tew));
    }
  return null;
  
  case VEHICLE_AMMO_SELECT:
  vehicle = (VehicleBase)world.getEntityByID(x);
  if(vehicle!=null)
    {
    return new GuiVehicleAmmoSelection(new ContainerDummy(), vehicle);
    }
  return null;
  
  case VEHICLE_DEBUG:
  vehicle = (VehicleBase)world.getEntityByID(x);
  if(vehicle!=null)
    {
    return new GuiVehicleInventory(new ContainerVehicle(player, vehicle, vehicle));
    }
  return null;//TODO make/set gui & container..
  
  case PERFORMANCE:
  return new GuiDebugInfo(new ContainerDebugInfo(player));
  
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
