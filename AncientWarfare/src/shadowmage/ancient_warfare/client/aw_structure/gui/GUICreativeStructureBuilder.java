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
package shadowmage.ancient_warfare.client.aw_structure.gui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.LowerStringMap;
import shadowmage.ancient_warfare.client.aw_core.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.aw_structure.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.aw_core.utils.StringTools;
import shadowmage.ancient_warfare.common.aw_structure.container.ContainerStructureSelectCreative;
import shadowmage.ancient_warfare.common.aw_structure.item.ItemStructureBuilderCreative;
import shadowmage.ancient_warfare.common.aw_structure.store.StructureManager;

public class GUICreativeStructureBuilder extends GuiContainerAdvanced
{
/**
 * need option to force team number/setting (override template)
 * 
 * checkBox forceTeam, force vehicle, force gate, force npc
 * merchantButtons to select forcedTeam, forcedvehicle, forcedgate, forcedNPC
 * display list of structures on the left, (as buttons?)-- basic info on the right (name, sizes)
 * button to set selection (add selection info to builder itemStack NBTTag)
 */

private final List<StructureClientInfo> clientStructures;
int currentLowestViewed = 0;
private boolean shouldForceUpdate = false;
String currentStructure = "";

/**
 * @param par1Container
 */
public GUICreativeStructureBuilder(Container container)
  {
  super(container);
  if(container instanceof ContainerStructureSelectCreative)
    {
    clientStructures = StructureManager.instance().getClientStructures();
    }  
  else
    {
    clientStructures = null;
    }    
  if(clientStructures==null)
    {
    closeGUI();
    }
  ItemStack builderItem = player.inventory.getCurrentItem();
  if(builderItem==null || !(builderItem.getItem() instanceof ItemStructureBuilderCreative))
    {
    return;
    } 
  
  if(builderItem.stackTagCompound!=null)
    {
    currentStructure = builderItem.stackTagCompound.getCompoundTag("structData").getString("name");
    }
  }

@Override
public int getXSize()
  {
  return 256;
  }

@Override
public int getYSize()
  {  
  return 240;
  }

@Override
public String getGuiBackGroundTexture()
  {
  // TODO Auto-generated method stub
  return null;
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {  
//  int maxDisplayed = this.currentLowestViewed +10 > clientStructures.size() ? clientStructures.size()-this.currentLowestViewed : this.currentLowestViewed+10;
//  for(int i = this.currentLowestViewed; i < maxDisplayed; i++)
//    {
//    this.drawString(fontRenderer, StringTools.subStringBeginning(clientStructures.get(i).name, 10), 20, 20 * i + 10, 0xffffffff);
//    }  
  }

@Override
public void setupGui()
  {
  this.controlList.clear();
  this.addGuiButton(0, 256-35-10, 10, 35, 18, "Done"); 
  this.addGuiButton(1, 10, 40, 35, 18, "Prev");
  this.addGuiButton(2, 50, 40, 35, 18, "Next");
  
  int buttonNum = 3;
  for(int i = 0; i+currentLowestViewed < clientStructures.size() && buttonNum<11; i++, buttonNum++)
    {
    this.addGuiButton(buttonNum, 10, 60 + (20*i) , 90, 18, StringTools.subStringBeginning(clientStructures.get(this.currentLowestViewed + i).name, 10));
    //this.drawString(fontRenderer, StringTools.subStringBeginning(clientStructures.get(i).name, 10), 20, 20 * i + 10, 0xffffffff);
    }  
  
  
  }

@Override
public void updateScreenContents()
  {
  if(shouldForceUpdate)
    {
    shouldForceUpdate = false;
    this.initGui();
    }  
  }

@Override
public void buttonClicked(GuiButton button)
  {
 
  System.out.println("buttonID: "+button.id);
  System.out.println("lowestViewed "+this.currentLowestViewed);
  if(button.id==1)
    {
    if(this.currentLowestViewed-8 >=0)
      {
      shouldForceUpdate = true;
      System.out.println("decrementing!");
      this.currentLowestViewed-=8;
      return;
      }
    }
  else if(button.id==2);
    {
    if(this.currentLowestViewed+8 < this.clientStructures.size())
      {
      System.out.println("incrementing!");
      this.currentLowestViewed+=8;
      shouldForceUpdate = true;
      return;
      }      
    }
    
  this.shouldForceUpdate = true;
  
  if(button.id>=3 && button.id < 11)
    {
    int index = (this.currentLowestViewed + button.id) - 3;
    if(index>=this.clientStructures.size())
      {
      System.out.println("OOB index > size -- "+index);      
      return;
      }
    this.setStructureName(this.clientStructures.get(index).name);
    }
  }


public void setStructureName(String name)
  {
  this.currentStructure = name;
  NBTTagCompound tag = new NBTTagCompound();
  tag.setString("name", name);
  this.sendDataToServer(tag);
  }


}
