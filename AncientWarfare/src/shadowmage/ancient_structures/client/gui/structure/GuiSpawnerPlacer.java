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
package shadowmage.ancient_structures.client.gui.structure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityList;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_structures.common.config.AWLog;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.container.ContainerSpawnerPlacer;
import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_warfare.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_warfare.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_warfare.client.gui.elements.GuiString;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerBase;

public class GuiSpawnerPlacer extends GuiContainerAdvanced
{

GuiButtonSimple done;
ContainerSpawnerPlacer container;
GuiScrollableArea area;
GuiScrollableArea area2;

HashMap<IGuiElement, String> buttonToName = new HashMap<IGuiElement, String>();
/**
 * @param container
 */
public GuiSpawnerPlacer(ContainerBase container)  
  {
  super(container);
  this.container = (ContainerSpawnerPlacer) container;
  this.shouldCloseOnVanillaKeys = false;
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(buttonToName.containsKey(element))
    {
    container.mobID = buttonToName.get(element);
    }
  else if(element==done)
    {    
    AWLog.logDebug("sending data packet to container");
    NBTTagCompound tag = new NBTTagCompound();
    tag.setString("EntityId", container.mobID);
    tag.setShort("MinSpawnDelay", (short) container.minSpawnDelay);
    tag.setShort("MaxSpawnDelay", (short) container.maxSpawnDelay);
    tag.setShort("SpawnCount", (short) container.spawnCount);
    tag.setShort("MaxNearbyEntities", (short) container.maxNearbyEntities);
    tag.setShort("RequiredPlayerRange", (short) container.activatingRangeFromPlayer);
    tag.setShort("SpawnRange", (short) container.spawnRange);     
    this.sendDataToServer(tag); 
    this.closeGUI();
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
  return Config.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawStringGui("Mob to spawn: "+this.container.mobID, 8, 8, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  area.updateGuiPos(guiLeft, guiTop);  
  }

@Override
public void setupControls()
  {

  this.guiElements.clear();
  
  done = new GuiButtonSimple(0, this, 55, 12, "Done");
  done.updateRenderPos(getXSize()-8-55, 8);
  this.guiElements.put(0, done);
  
  Collection<String> names = EntityList.stringToClassMapping.keySet();
  int areaHeight = (getYSize()-16-18-8)/2;
  int area1Y = 8+18+4;
  int area2Y = area1Y + areaHeight;
  area = new GuiScrollableArea(1, this, 8, area1Y, getXSize()-16, areaHeight, areaHeight);
  this.guiElements.put(1, area);
  
  List<String> sortedNames = new ArrayList<String>();
  sortedNames.addAll(names);
  Collections.sort(sortedNames);
  
  Iterator<String> it= sortedNames.iterator();
  String name;
  int totalHeight = 0;
  GuiButtonSimple button;
  while(it.hasNext() && (name=it.next())!=null)
    {
    if(AWStructureStatics.excludedSpawnerEntities.contains(name)){continue;}
    button = (GuiButtonSimple) new GuiButtonSimple(0, area, 256-16-20, 14, name).updateRenderPos(0, totalHeight);
    area.elements.add(button);
    buttonToName.put(button, name);
    totalHeight+=16;
    }  
  area.updateTotalHeight(totalHeight);
  
  area2 = new GuiScrollableArea(2, this, 8, area2Y, getXSize()-16, areaHeight, areaHeight);
  this.guiElements.put(2, area2);

  
  totalHeight = 0;
  GuiString label;
  GuiNumberInputLine input;
  
  label = new GuiString(0, area2, 100, 12, "minSpawnDelay");
  label.updateRenderPos(0, totalHeight);
  area2.elements.add(label);
  
  input = new GuiNumberInputLine(0, area2, 50, 12, 4, "")
    {
    @Override
    public void onValueUpdated(float value)
      {
      container.minSpawnDelay = getIntVal();
      }
    };
  input.setAsIntegerValue();
  input.setIntegerValue(container.minSpawnDelay);
  input.updateRenderPos(160, totalHeight);
  area2.elements.add(input);
  totalHeight+=12;
  
  
  label = new GuiString(0, area2, 100, 12, "maxSpawnDelay");
  label.updateRenderPos(0, totalHeight);
  area2.elements.add(label);
  
  input = new GuiNumberInputLine(0, area2, 50, 12, 4, "")
    {
    @Override
    public void onValueUpdated(float value)
      {
      container.maxSpawnDelay = getIntVal();
      }
    };
  input.setAsIntegerValue();
  input.setIntegerValue(container.maxSpawnDelay);
  input.updateRenderPos(160, totalHeight);
  area2.elements.add(input);
  totalHeight+=12;
  
  
  label = new GuiString(0, area2, 100, 12, "spawnCount");
  label.updateRenderPos(0, totalHeight);
  area2.elements.add(label);
  
  input = new GuiNumberInputLine(0, area2, 50, 12, 4, "")
    {
    @Override
    public void onValueUpdated(float value)
      {
      container.spawnCount = getIntVal();
      }
    };
  input.setAsIntegerValue();
  input.setIntegerValue(container.spawnCount);
  input.updateRenderPos(160, totalHeight);
  area2.elements.add(input);
  totalHeight+=12;
    
  
  label = new GuiString(0, area2, 100, 12, "maxNearbyEntities");
  label.updateRenderPos(0, totalHeight);
  area2.elements.add(label);
  
  input = new GuiNumberInputLine(0, area2, 50, 12, 4, "")
    {
    @Override
    public void onValueUpdated(float value)
      {
      container.maxNearbyEntities = getIntVal();
      }
    };
  input.setAsIntegerValue();
  input.setIntegerValue(container.maxNearbyEntities);
  input.updateRenderPos(160, totalHeight);
  area2.elements.add(input);
  totalHeight+=12;
  
  
  label = new GuiString(0, area2, 100, 12, "activatingRangeFromPlayer");
  label.updateRenderPos(0, totalHeight);
  area2.elements.add(label);
  
  input = new GuiNumberInputLine(0, area2, 50, 12, 4, "")
    {
    @Override
    public void onValueUpdated(float value)
      {
      container.activatingRangeFromPlayer = getIntVal();
      }
    };
  input.setAsIntegerValue();
  input.setIntegerValue(container.activatingRangeFromPlayer);
  input.updateRenderPos(160, totalHeight);
  area2.elements.add(input);
  totalHeight+=12;
  
  
  label = new GuiString(0, area2, 100, 12, "spawnRange");
  label.updateRenderPos(0, totalHeight);
  area2.elements.add(label);
  
  input = new GuiNumberInputLine(0, area2, 50, 12, 4, "")
    {
    @Override
    public void onValueUpdated(float value)
      {
      container.maxSpawnDelay = getIntVal();
      }
    };
  input.setAsIntegerValue();
  input.setIntegerValue(container.spawnRange);
  input.updateRenderPos(160, totalHeight);
  area2.elements.add(input);
  totalHeight+=12;
  
  area2.updateTotalHeight(totalHeight);
  }

@Override
public void updateControls()
  {
  }

}
