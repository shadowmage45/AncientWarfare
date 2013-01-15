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
package shadowmage.ancient_warfare.client.aw_core.gui;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.aw_core.config.Config;
import shadowmage.ancient_warfare.common.aw_core.container.ContainerBase;
import shadowmage.ancient_warfare.common.aw_core.utils.IContainerGUICallback;
import shadowmage.ancient_warfare.common.aw_structure.container.ContainerChooseStructure;
import shadowmage.ancient_warfare.common.aw_structure.container.data.StructureInfo;

public class GuiSimpleBase extends GuiContainer implements IContainerGUICallback
{
protected final EntityPlayer player;
protected final ContainerBase container;
/**
   * @param par1Container
   */
public GuiSimpleBase(Container container)
  {
  super(container);
  if(container instanceof ContainerBase)
    {
    player = ((ContainerBase)container).getPlayer();   
    }
  else
    {
    player=null;
    }
  this.container = (ContainerBase)this.inventorySlots;
  if(container==null)
    {
    Config.logError("Attempt to pass vanilla MC Container to :"+this.getClass());
    //throw new IllegalArgumentException("Attempt to pass vanilla MC Container to :"+this.getClass());
    }
  }


@Override
public void handleUpdateInfo(NBTTagCompound tag)
  {

  }

@Override
protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
  {
  
  }

}
