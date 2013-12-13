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
package shadowmage.ancient_warfare.client.gui.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiItemStack;
import shadowmage.ancient_framework.client.gui.elements.GuiScrollableArea;
import shadowmage.ancient_framework.client.gui.elements.GuiString;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.common.config.Statics;
import shadowmage.ancient_framework.common.registry.DescriptionRegistry;
import shadowmage.ancient_framework.common.registry.entry.Description;
import shadowmage.ancient_framework.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.client.render.RenderTools;
import shadowmage.ancient_warfare.common.container.ContainerDummy;
import shadowmage.ancient_warfare.common.crafting.AWCraftingManager;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleType;

public class GuiInfoBase extends GuiContainerAdvanced
{


GuiContainerAdvanced baseParent;
GuiContainerAdvanced parent;
ResourceListRecipe recipe;
GuiScrollableArea area;
GuiButtonSimple back;
List<String> detailText = new ArrayList<String>();

HashMap<GuiButtonSimple, IResearchGoal> buttonGoalMap = new HashMap<GuiButtonSimple, IResearchGoal>();
HashMap<GuiButtonSimple, ResourceListRecipe> buttonRecipeMap = new HashMap<GuiButtonSimple, ResourceListRecipe>();
HashSet<GuiItemStack> stackButtons = new HashSet<GuiItemStack>();

/**
 * @param container
 */
public GuiInfoBase(GuiContainerAdvanced parent, ResourceListRecipe recipe)
  {
  super(new ContainerDummy());
  this.parent = parent;
  this.recipe = recipe;
  Description d = DescriptionRegistry.instance().getDescriptionFor(recipe.getResult().itemID);
  if(d!=null)
    {
    String s = d.getDescription(this.recipe.getResult().getItemDamage());
    if(s!=null && !s.equals(""))
      {
      this.detailText.add(StatCollector.translateToLocal(s));
      this.detailText.add("");
      }
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
public String getGuiBackGroundTexture()
  {
  return Statics.texturePath+"gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  this.drawStringGui(recipe.getLocalizedDisplayName(), 5, 5, 0xffffffff);
  this.drawStringGui("Required resources:", 5, 15, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  this.area.updateGuiPos(guiLeft, guiTop);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  if(element==back)
    {
    mc.displayGuiScreen(parent);
    }
  if(buttonGoalMap.containsKey(element))
    {
    mc.displayGuiScreen(new GuiResearchGoal(this , buttonGoalMap.get(element)));
    }
  if(buttonRecipeMap.containsKey(element))
    {
    this.handleRecipeClick(this.buttonRecipeMap.get(element));
    }
  if(stackButtons.contains(element))
    {
    ResourceListRecipe recipe = AWCraftingManager.instance().getRecipeByResult(((GuiItemStack)element).getStack());
    if(recipe!=null)
      {
      this.handleRecipeClick(recipe);
      }
    }
  }

protected void handleRecipeClick(ResourceListRecipe recipe)
  {  
  if(recipe.type==RecipeType.RESEARCH)
    {
    this.handleResearchDetailsClick(recipe);
    }
  else if(recipe.type==RecipeType.VEHICLE)
    {
    this.handleVehicleDetailsClick(recipe);
    }
  else
    {
    this.handleRecipeDetailsClick(recipe);    
    }
  }

protected void handleRecipeDetailsClick(ResourceListRecipe recipe)
  {
  mc.displayGuiScreen(new GuiRecipeDetails(this, recipe));    
  }

protected void handleResearchDetailsClick(ResourceListRecipe recipe)
  {
  int id = recipe.getResult().getItemDamage();
  IResearchGoal goal = ResearchGoal.getGoalByID(id);
  mc.displayGuiScreen(new GuiResearchGoal(this, goal));
  }

protected void handleVehicleDetailsClick(ResourceListRecipe recipe)
  {
  int type = recipe.getResult().getItemDamage();
  int lev = recipe.getResult().getTagCompound().getCompoundTag("AWVehSpawner").getInteger("lev");
  IVehicleType t = VehicleType.getVehicleType(type);
  mc.displayGuiScreen(new GuiVehicleDetails(this, recipe, t, lev));
  }

@Override
public void setupControls()
  {
  int elementNum = 100;
  GuiItemStack item;
  int x = 0;
  int y = 0;
  for(ItemStackWrapperCrafting stack : recipe.getResourceList())
    {
    item = new GuiItemStack(elementNum, this);
    item.updateRenderPos(x*18+8, y*18+5+10+10);
    item.isClickable = true;
    item.setItemStack(stack.getFilter());
    for(String l : (List<String>)stack.getFilter().getTooltip(player, false))
      {
      item.addToToolitp(l);
      }
    item.addToToolitp("Click to view detailed recipe information");
    item.addToToolitp("(if available)");
    stackButtons.add(item);
    this.guiElements.put(elementNum, item);
    elementNum++;
    x++;
    if(x>=9)      
      {
      x = 0;
      y++;
      }
    }
  
  this.area = new GuiScrollableArea(0, this, 5, 5+16+5+30, 256-10, 240-10-16-5-30, 0);
  int nextElementY = 0; 
  
  List<String> displayText = RenderTools.getFormattedLines(detailText, 220);
  GuiString string;  
  for(String line : displayText)
    {
    string = new GuiString(elementNum, area, 240, 10, line);
    string.updateRenderPos(0, nextElementY);
    nextElementY+=10;
    elementNum++;
    area.elements.add(string);
    }  
  nextElementY+=10;
  area.addGuiElement(new GuiString(elementNum, area, 240-24, 10, "Used In Recipes: ").updateRenderPos(0, nextElementY));
  nextElementY += 10;
  List<ResourceListRecipe> recipes = AWCraftingManager.instance().getRecipesContaining(recipe.getResult());
  GuiButtonSimple button;
  for(ResourceListRecipe recipe : recipes)
    {
    button = new GuiButtonSimple(elementNum, area, 240-24 - 22, 16, recipe.getLocalizedDisplayName());
    button.updateRenderPos(22, nextElementY+1);
    button.addToToolitp("Click to view detailed recipe information");
    elementNum++;
    item = new GuiItemStack(elementNum, area).setItemStack(recipe.getResult());
    item.updateRenderPos(0, nextElementY);
    buttonRecipeMap.put(button, recipe);
    area.addGuiElement(button);
    area.addGuiElement(item);
    elementNum++;
    nextElementY += 18;
    }
  nextElementY += 10;
  area.addGuiElement(new GuiString(elementNum, area, 240-24, 10, "Required Research: ").updateRenderPos(0, nextElementY));
  nextElementY += 10;
  
  for(IResearchGoal g : recipe.getNeededResearch())
    {
    button = new GuiButtonSimple(elementNum, area, 240-24, 16, g.getLocalizedName());
    button.updateRenderPos(0, nextElementY);
    button.addToToolitp("Click to view detailed research goal information");
    buttonGoalMap.put(button, g);
    area.addGuiElement(button);
    elementNum++;
    nextElementY += 18;
    }    
  area.updateTotalHeight(nextElementY);
  area.updateGuiPos(guiLeft, guiTop);
  this.guiElements.put(0, area);
  
  back = this.addGuiButton(1, this.getXSize()-40, 5, 35, 16, "Back");
  }

@Override
public void updateControls()
  {
  // TODO Auto-generated method stub

  }

}
