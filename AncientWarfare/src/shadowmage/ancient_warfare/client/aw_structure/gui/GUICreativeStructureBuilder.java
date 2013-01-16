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

import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.aw_core.gui.GuiContainerAdvanced;

public class GUICreativeStructureBuilder extends GuiContainerAdvanced
{
/**
 * need option to force team number/setting (override template)
 * 
 * checkBox forceTeam
 * merchantButtons to select forcedTeam number
 * display list of structures on the left, (as buttons?)-- basic info on the right (name, sizes)
 * button to set selection (add selection info to builder itemStack NBTTag)
 */


/**
 * @param par1Container
 */
public GUICreativeStructureBuilder(Container container)
  {
  super(container);
  }

@Override
public int getXSize()
  {
  // TODO Auto-generated method stub
  return 0;
  }

@Override
public int getYSize()
  {
  // TODO Auto-generated method stub
  return 0;
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
  // TODO Auto-generated method stub
  
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

@Override
public void handleUpdateFromContainer(NBTTagCompound tag)
  {
  // TODO Auto-generated method stub
  
  }


}
