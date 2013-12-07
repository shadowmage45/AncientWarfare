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
package shadowmage.ancient_warfare.common.nei;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import shadowmage.ancient_warfare.client.gui.crafting.GuiCraftingTabbed;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.RecipeInfo;
import codechicken.nei.recipe.TemplateRecipeHandler;

public class AWNeiRecipeHandler extends TemplateRecipeHandler
{

/**
 * 
 */
public AWNeiRecipeHandler()
  {
  // TODO Auto-generated constructor stub
  }

@Override
public String getRecipeName()
  {
  return "AW Crafting";
  }

@Override
public String getGuiTexture()
  {
  return "textures/gui/container/crafting_table.png";
  }

@Override
public String getOverlayIdentifier()
  {
  return "awcrafting";
  }

@Override
public void loadTransferRects()
  {
  transferRects.add(new RecipeTransferRect(new Rectangle(84, 23, 24, 18), "awcrafting"));
  }

@Override
public Class<? extends GuiContainer> getGuiClass()
  {
  return GuiCraftingTabbed.class;
  }

public class AWCachedRecipe extends CachedRecipe
{

public ArrayList<PositionedStack> ingredients;
public PositionedStack result;
public AWCachedRecipe(ResourceListRecipe recipe)
  {
  result = new PositionedStack(recipe.getResult(), 119, 24);
  ingredients = new ArrayList<PositionedStack>();
  setIngredients(recipe.getResourceList());
  }

public void setIngredients(List<ItemStackWrapperCrafting> items)
  {
  for(int x = 0; x < 3; x++)
    {
    for(int y = 0; y < 3; y++)
      {
      if((y*3+x)>=items.size() || items.get(y*3+x) == null)
        {
        continue;
        }
      PositionedStack stack = new PositionedStack(items.get(y*3+x).getFilter(), 25+x*18, 6+y*18);
      stack.setMaxSize(64);
      ingredients.add(stack);
      }
    }
  }

@Override
public List<PositionedStack> getIngredients()
  {
  return getCycledIngredients(cycleticks / 20, ingredients);
  }

@Override
public PositionedStack getResult()
  {
  return result;
  }
}

@Override
public void loadCraftingRecipes(String outputId, Object... results)
  {
  Config.logDebug("handling load recipes request for: "+outputId + " results: "+results);
  if(results.length>=1 && results[0]!=null && results[0] instanceof ItemStack)
    {
    ItemStack stack = (ItemStack)results[0];
    if(stack.hasTagCompound())
      {
      stack.getTagCompound().setName("tag");
      }
    Config.logDebug("stack: "+stack+ "  tag: "+stack.getTagCompound());
    loadCraftingRecipes((ItemStack)results[0]);
    }  
  }

@Override
public void loadCraftingRecipes(ItemStack result)
  {  
  List<ResourceListRecipe> allrecipes = AWCraftingManager.getAllRecipes();
  for(ResourceListRecipe irecipe : allrecipes)
    {
    if(InventoryTools.doItemsMatch(irecipe.getResult(), result))
      {
      this.arecipes.add(new AWCachedRecipe(irecipe));
      }    
    } 
  }

@Override
public void loadUsageRecipes(ItemStack ingredient)
  {
  List<ResourceListRecipe> allrecipes = AWCraftingManager.getAllRecipes();
  for(ResourceListRecipe irecipe : allrecipes)
    {
    for(ItemStackWrapperCrafting t : irecipe.getResourceList())
      {
      if(t.matches(ingredient))
        {
        arecipes.add(new AWCachedRecipe(irecipe));
        break;
        }
      }    
    } 
  }

public boolean hasOverlay(GuiContainer gui, Container container, int recipe)
  {
  return super.hasOverlay(gui, container, recipe) || RecipeInfo.hasDefaultOverlay(gui, "awcrafting");
  }


}
