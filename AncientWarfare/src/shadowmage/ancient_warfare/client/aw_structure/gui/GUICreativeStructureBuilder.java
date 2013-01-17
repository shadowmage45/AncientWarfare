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
import net.minecraft.server.management.LowerStringMap;
import shadowmage.ancient_warfare.client.aw_core.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.aw_structure.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.aw_structure.container.ContainerStructureSelectCreative;
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

private final ItemStack builder;
private final List<StructureClientInfo> clientStructures;
int currentLowestViewed = 0;

/**
 * @param par1Container
 */
public GUICreativeStructureBuilder(Container container)
  {
  super(container);
  if(container instanceof ContainerStructureSelectCreative)
    {
    builder = ((ContainerStructureSelectCreative)container).builderItem;
    clientStructures = StructureManager.instance().getClientStructures();
    }  
  else
    {
    builder = null;
    clientStructures = null;
    }    
  if(builder==null || clientStructures==null)
    {
    closeGUI();
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
  int maxDisplayed = this.currentLowestViewed +10 > clientStructures.size() ? clientStructures.size()-this.currentLowestViewed : this.currentLowestViewed+10;
  for(int i = this.currentLowestViewed; i < maxDisplayed; i++)
    {
    this.drawString(fontRenderer, clientStructures.get(i).name, 20, 20 * i + 10, 0xffffffff);
    }  
  }

@Override
public void setupGui()
  {
  // TODO Auto-generated method stub  
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub  
  }

@Override
public void buttonClicked(GuiButton button)
  {
  // TODO Auto-generated method stub  
  }


}
