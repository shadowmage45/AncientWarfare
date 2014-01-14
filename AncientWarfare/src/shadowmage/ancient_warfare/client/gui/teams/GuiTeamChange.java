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
import shadowmage.ancient_warfare.common.tracker.entry.TeamEntry.TeamMemberEntry;

public class GuiTeamChange extends GuiContainerAdvanced
{

GuiButtonSimple prev;
GuiButtonSimple next;
GuiNumberInputLine inputBox;

GuiButtonSimple select;
GuiButtonSimple done;
GuiScrollableArea area;

ContainerTeamControl container;


public GuiTeamChange(Container container)
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
  this.done = this.addGuiButton(0, getXSize()-40-8, 8, 40, 16, "Back");
  this.select = this.addGuiButton(1, 8, 8, 40, 16, "Apply");
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
  int teamNum = this.inputBox.getIntVal();
  teamNum = teamNum<0 ? 0 : teamNum> 15 ? 15 : teamNum;
  this.inputBox.setIntegerValue(teamNum);
  
  TeamEntry entry = TeamTracker.instance().getTeamEntryFor(player.worldObj, teamNum);
  this.area.elements.clear();
  
  int targetY = 0;
    
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 12, "Team Members for team "+teamNum+":").updateRenderPos(0, targetY));
  targetY+=16;
  
  for(TeamMemberEntry member : entry.members)
    {
    addTeamMemberEntry(member.getMemberName(), targetY);
    targetY+=12;
    }  
  }

protected void addTeamMemberEntry(String name, int targetY)
  {
  area.elements.add(new GuiString(0, area, getXSize()-16-12, 10, name).updateRenderPos(0, targetY));
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==prev)
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
  else if(element==done)
    {
    mc.displayGuiScreen(new GuiTeamView(container));
    }
  else if(element==select)
    {
    NBTTagCompound tag = new NBTTagCompound();    
    tag.setBoolean("apply", true);
    tag.setString("name", player.getEntityName());
    tag.setInteger("team", this.inputBox.getIntVal());
    this.sendDataToServer(tag);
    player.addChatMessage("Sent team application to team: "+this.inputBox.getIntVal());
    mc.displayGuiScreen(new GuiTeamView(container));
    }
  else if(element==this.inputBox)
    {
    int num = this.inputBox.getIntVal();
    if(num>15){num = 0;}
    if(num<0){num = 15;}
    this.inputBox.setIntegerValue(num);//reseat value to re-normalize, if needed
    this.refreshGui();
    }
  }

}
