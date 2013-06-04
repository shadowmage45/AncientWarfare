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
package shadowmage.ancient_warfare.client.gui.info;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_warfare.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_warfare.client.gui.elements.IGuiElement;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.container.ContainerDummy;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;

public class GuiRecipeDetails extends GuiContainerAdvanced
{

GuiContainerAdvanced parent;
ResourceListRecipe recipe;

/**
 * @param container
 */
public GuiRecipeDetails(GuiContainerAdvanced parent, ResourceListRecipe recipe)
  {
  super(new ContainerDummy());
  this.shouldCloseOnVanillaKeys = true;
  this.parent = parent;
  this.recipe = recipe;
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  }

@Override
public int getXSize()
  {
  return 120;
  }

@Override
public int getYSize()
  {
  return 100;
  }

@Override
public String getGuiBackGroundTexture()
  {
  return Config.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawStringGui(recipe.getDisplayName(), 5, 5, 0xffffffff);
  this.drawStringGui("Required resources:", 5, 15, 0xffffffff);
  int x = 0;
  int y = 0;
  for(ItemStackWrapperCrafting stack : recipe.getResourceList())
    {
    this.renderItemStack(stack.getFilter(), guiLeft + x*18 + 8, guiTop + y * 18 + 5+10+10, mouseX, mouseY, true);
    x++;
    if(x>=9)      
      {
      x = 0;
      y++;
      }
    }
  }

@Override
protected void keyTyped(char par1, int par2)
  {
  if(par2 == this.mc.gameSettings.keyBindInventory.keyCode || par2 == Keyboard.KEY_ESCAPE)
    {
    mc.displayGuiScreen(parent);
    return;
    }
  super.keyTyped(par1, par2);
  }

@Override
public void updateScreenContents()
  {
  // TODO Auto-generated method stub
  }

@Override
public void setupControls()
  {
  // TODO Auto-generated method stub
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub
  }

}
