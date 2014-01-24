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
package shadowmage.ancient_warfare.client.gui.teams;

import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerTeamControl;
import shadowmage.ancient_warfare.common.tracker.TeamTracker;
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry;

public class GuiTeamAlliance extends GuiContainerAdvanced
{

GuiButtonSimple prev;
GuiButtonSimple next;
GuiButtonSimple select;
GuiNumberInputLine inputBox;
GuiButtonSimple done;
GuiScrollableArea area;

TeamEntry entry;

ContainerTeamControl container;

public GuiTeamAlliance(Container container)
  {
  super(container);
  this.container = (ContainerTeamControl)container;
  this.shouldCloseOnVanillaKeys = true;
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
  return Config.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {

  }

@Override
public void updateScreenContents()
  {

  }

@Override
public void setupControls()
  {
  this.entry = TeamTracker.instance().getTeamEntryFor(player);
  this.done = this.addGuiButton(0, getXSize()-55-8, 8, 55, 16, "Back");
   
  this.prev = this.addGuiButton(2, 8+40+4, 8, 40, 16, "Prev");
  this.next = this.addGuiButton(3, getXSize()-40-8-4-40, 8, 40, 16, "Next");
  this.inputBox = this.addNumberField(4, 64, 12, 3, "0").setAsIntegerValue().setMinMax(0, 15).setIntegerValue(0);
  this.inputBox.updateRenderPos(8+40+4+40+4, 10);
  this.area = new GuiScrollableArea(5, this, 8, 8+16+4, getXSize()-16, getYSize()-16-16-4, 0);
  this.guiElements.put(5, area);
  }

@Override
public void updateControls()
  {
  this.area.elements.clear();
  
  this.entry = TeamTracker.instance().getTeamEntryFor(player);
  int rank = entry.getPlayerRank(player.getEntityName());
 
  String selectName = entry.nonHostileTeams.contains(inputBox.getIntVal()) ? "Remove" : "Add";
  this.select = this.addGuiButton(1, 8, 8, 40, 16, selectName);
  select.updateRenderPos(guiLeft+8, guiTop+8);
  
  int targetY = 0;
  
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Team: "+entry.teamNum + " Your Rank: "+rank).updateRenderPos(0, targetY));
  targetY+=14;
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Allied Teams:").updateRenderPos(0, targetY));
  targetY+=16;
  
  for(Integer i : entry.nonHostileTeams)
    {
    area.elements.add(new GuiString(0, area, getXSize()-16-12, 10, "Allied Team: "+String.valueOf(i)).updateRenderPos(0, targetY));
    targetY+=12;
    }
  }



@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==done)
    {
    mc.displayGuiScreen(new GuiTeamControl(this.container));
    }
  else if(element==prev)
    {
    int num = this.inputBox.getIntVal();
    if(num==0)
      {
      num=15;
      }
    else
      {
      num--;
      }
    this.inputBox.setIntegerValue(num);
    this.refreshGui();
    }
  else if(element==next)
    {
    int num = this.inputBox.getIntVal();
    if(num==15)
      {
      num=0;
      }
    else
      {
      num++;
      }
    this.inputBox.setIntegerValue(num);
    this.refreshGui();
    }  
  else if(element==inputBox)
    {
    int num = this.inputBox.getIntVal();
    if(num>15){num = 0;}
    if(num<0){num = 15;}
    this.inputBox.setIntegerValue(num);//reseat value to re-normalize, if needed
    this.refreshGui();
    }
  else if(element==select)
    {
    int team = this.inputBox.getIntVal();
    boolean present = entry.nonHostileTeams.contains(team);
    player.addChatMessage(present? "Removed " + team +" from allied teams." : "Added "+team+ " to allied teams.");
    NBTTagCompound tag = new NBTTagCompound();
    tag.setBoolean("hostile", !present);
    tag.setInteger("hostTeam", team);
    tag.setInteger("team", entry.teamNum);
    this.sendDataToServer(tag);    
    }  
  }

}
