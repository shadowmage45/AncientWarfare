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
package shadowmage.ancient_warfare.common.crafting;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.nbt.NBTTagCompound;
import shadowmage.ancient_warfare.common.block.BlockLoader;
import shadowmage.ancient_warfare.common.civics.types.Civic;
import shadowmage.ancient_warfare.common.civics.types.ICivicType;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.gates.IGateType;
import shadowmage.ancient_warfare.common.gates.types.Gate;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.npcs.NpcTypeBase;
import shadowmage.ancient_warfare.common.registry.ArmorRegistry;
import shadowmage.ancient_warfare.common.registry.VehicleUpgradeRegistry;
import shadowmage.ancient_warfare.common.research.IResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoal;
import shadowmage.ancient_warfare.common.research.ResearchGoalNumbers;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.StructureClientInfo;
import shadowmage.ancient_warfare.common.tracker.entry.PlayerEntry;
import shadowmage.ancient_warfare.common.utils.InventoryTools;
import shadowmage.ancient_warfare.common.utils.ItemStackWrapperCrafting;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.armors.IVehicleArmorType;
import shadowmage.ancient_warfare.common.vehicles.missiles.Ammo;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;
import shadowmage.ancient_warfare.common.vehicles.types.VehicleType;
import shadowmage.ancient_warfare.common.vehicles.upgrades.IVehicleUpgradeType;

import com.google.common.collect.Lists;

public class AWCraftingManager
{

private String[][] recipePatterns = new String[][] {{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
private Object[][] recipeItems;
List<ResourceListRecipe> vehicleRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> ammoRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> civicRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> civicMiscRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> upgradeRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> npcRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> npcMiscRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> researchRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> componentRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> alchemyRecipes = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> alchemyMiscRecipes = new ArrayList<ResourceListRecipe>();

List<ResourceListRecipe> structureRecipesServer = new ArrayList<ResourceListRecipe>();
List<ResourceListRecipe> structureRecipesClient = new ArrayList<ResourceListRecipe>();

private Map<RecipeType, List<ResourceListRecipe>> recipesByType = new HashMap<RecipeType, List<ResourceListRecipe>>();

public static List<ShapedRecipes> vanillaRecipeList = new ArrayList<ShapedRecipes>();

private AWCraftingManager()
  {
  recipesByType.put(RecipeType.VEHICLE, vehicleRecipes);
  recipesByType.put(RecipeType.AMMO, ammoRecipes);
  recipesByType.put(RecipeType.CIVIC, civicRecipes);
  recipesByType.put(RecipeType.CIVIC_MISC, civicMiscRecipes);
  recipesByType.put(RecipeType.VEHICLE_MISC, upgradeRecipes);
  recipesByType.put(RecipeType.NPC, npcRecipes);
  recipesByType.put(RecipeType.NPC_MISC, npcMiscRecipes);
  recipesByType.put(RecipeType.RESEARCH, researchRecipes);
  recipesByType.put(RecipeType.AMMO_MISC, componentRecipes);
  recipesByType.put(RecipeType.ALCHEMY, alchemyRecipes);
  recipesByType.put(RecipeType.ALCHEMY_MISC, alchemyMiscRecipes);
  recipesByType.put(RecipeType.NONE, new ArrayList<ResourceListRecipe>());
  this.recipeItems = new Object[][] {{ItemLoader.ironRings}, {Item.helmetChain}, {Item.plateChain}, {Item.legsChain}, {Item.bootsChain}};

  }

private static AWCraftingManager INSTANCE = new AWCraftingManager();

public static AWCraftingManager instance()
  {
  return INSTANCE;
  }

public ResourceListRecipe getRecipeByResult(ItemStack result)
  {
  for(List<ResourceListRecipe> list : this.recipesByType.values())
    {
    for(ResourceListRecipe valid : list)
      {
      if(result.itemID==valid.getResult().itemID && result.getItemDamage()==valid.getResult().getItemDamage())
        {
        Config.logDebug("examining tags: "+result.getTagCompound() + " :: " +valid.getResult().getTagCompound());
        NBTTagCompound tag = valid.getResult().getTagCompound();
        if(tag!=null)
          {
          tag.setName("tag");
          }
        tag = result.getTagCompound();
        if(tag!=null)
          {
          tag.setName("tag");
          }
        if(ItemStack.areItemStackTagsEqual(result, valid.getResult()))
          {
          return valid;
          }      
        } 
            
      }
    } 
  return null;
  }

public ResourceListRecipe validateRecipe(ResourceListRecipe recipe)
  {
  if(recipe==null){return null;}
  for(List<ResourceListRecipe> list : this.recipesByType.values())
    {
    for(ResourceListRecipe valid : list)
      {
      if(InventoryTools.doItemsMatch(valid.result, recipe.result))
        {
        if(areResourceListsIdentical(valid, recipe))
          {
          return valid;
          }
        }      
      }
    }
  return null;
  }

public ResourceListRecipe getStructureRecipeFor(String name)
  {
  for(ResourceListRecipe recipe : this.structureRecipesServer)
    {
    if(recipe.getDisplayName().toLowerCase().equals(name.toLowerCase()))
      {
      return recipe;
      }
    }
  return null;
  }

public List<ResourceListRecipe> getRecipesContaining(PlayerEntry entry, String text, EnumSet<RecipeType> types, boolean creative)
  {
  String name;
  List<ResourceListRecipe> recipes = new ArrayList<ResourceListRecipe>();
  List<ResourceListRecipe> list;
  if(entry!=null)
    {
    for(RecipeType t : types)
      {
      list = this.recipesByType.get(t);
      for(ResourceListRecipe recipe : list)
        {
        name = recipe.displayName;
        if(name.toLowerCase().contains(text.toLowerCase()) && (creative || Config.disableResearch || recipe.canBeCraftedBy(entry)))
          {
          recipes.add(recipe);
          }
        }
      }
    }  
  return recipes;  
  }

protected boolean areResourceListsIdentical(ResourceListRecipe a, ResourceListRecipe b)
  {
  boolean found = false;
  for(ItemStackWrapperCrafting stack : a.resources)
    {
    found = false;
    for(ItemStackWrapperCrafting bstack : b.resources)
      {
      if(stack.matches(bstack))
        {
        found = true;
        break;
        }
      }
    if(!found)
      {
      return false;
      }
    }  
  return a.resources.size() == b.resources.size();
  }

/**
 * should be called from AWCore during final INIT stages
 */
public void loadRecipes()
  {
  Config.logDebug("LOADING RECIPES");
  this.addGateRecipes();//done
  this.addCivicRecipes();//done
  this.addUpgradeRecipes();//done
  this.addAmmoRecipes();//done
  this.addNpcRecipes();//?? not done
  this.addVehicleRecipes();//NEEDS MATERIALS
  this.addStructureRecipes();//dynamic-done
  this.addResearchRecipes();//done  
  this.addAlchemyRecipes();
  
  //research book
  this.vanillaRecipeList.add(CraftingManager.getInstance().addRecipe(new ItemStack(ItemLoader.researchBook), new Object[] {"gll","ppp","gll", 'p', Item.paper, 'l', Item.leather, 'g', Item.ingotGold} ));
  
  //research table
  this.vanillaRecipeList.add( CraftingManager.getInstance().addRecipe(new ItemStack(BlockLoader.crafting,1, 0 ), new Object[] {"sps","wcw","w_w", 's', new ItemStack(Block.stoneSingleSlab,1,0), 'w', Block.planks, 'c', Block.chest, 'p', Item.paper}) );
  
  //civil engineering
  this.vanillaRecipeList.add(CraftingManager.getInstance().addRecipe(new ItemStack(BlockLoader.crafting,1, 1 ), new Object[] {"sss","wcw","w_w", 's', Item.ingotIron, 'w', Block.planks, 'c', Block.chest} ));
  
  //structure engineering
  this.vanillaRecipeList.add( CraftingManager.getInstance().addRecipe(new ItemStack(BlockLoader.crafting,1, 2 ), new Object[] {"sss","wcw", 's', Block.stone, 'w', Block.planks, 'c', Block.chest} ));
  
  //vehicle
  this.vanillaRecipeList.add( CraftingManager.getInstance().addRecipe(new ItemStack(BlockLoader.crafting,1, 3 ), new Object[] {"sss","wcw", 's', new ItemStack(Block.stoneSingleSlab,1,0), 'w', Block.planks, 'c', Block.chest} ));
  
  //ammo
  this.vanillaRecipeList.add( CraftingManager.getInstance().addRecipe(new ItemStack(BlockLoader.crafting,1, 4 ), new Object[] {"sis","wcw", 's', new ItemStack(Block.stoneSingleSlab,1,0), 'w', Block.planks, 'c', Block.chest, 'i', Item.ingotIron} ));
  
  //npc
  this.vanillaRecipeList.add( CraftingManager.getInstance().addRecipe(new ItemStack(BlockLoader.crafting,1, 5 ), new Object[] {"sis","ici","i_i", 'i', Item.ingotIron, 'c', Block.chest, 's', Item.ingotGold} ));

  //chemistry/alchemy table
  this.vanillaRecipeList.add( CraftingManager.getInstance().addRecipe(new ItemStack(BlockLoader.crafting,1, 6 ), new Object[] {"pip","pcp","p_p", 'p', Block.woodSingleSlab, 'c', Block.chest, 'i', Item.clay} ));
 
  //auto-craft table
  this.vanillaRecipeList.add( CraftingManager.getInstance().addRecipe(new ItemStack(BlockLoader.crafting,1, 7 ), new Object[] {"ppp","pcp","p_p", 'p', Block.woodSingleSlab, 'c', Block.chest} ));
     
  //iron rings
  this.vanillaRecipeList.add( CraftingManager.getInstance().addRecipe(new ItemStack(ItemLoader.componentItem, 5, ItemLoader.ironRings.getItemDamage()), new Object[] {"_i_","i_i", 'i', Item.ingotIron} ));
      
  this.vanillaRecipeList.add( CraftingManager.getInstance().addRecipe(BlockLoader.trashcan.copy(), new Object[] {"psp", "rtr", "blb", 'p', Block.planks, 's', Item.stick, 't', Block.stone, 'r', Item.redstone, 'b', Block.stoneBrick, 'l', Item.bucketLava} ));
  //iron ring->chain mail  
  for (int i = 0; i < this.recipeItems[0].length; ++i)
    {
    Object object = this.recipeItems[0][i];

    for (int j = 0; j < this.recipeItems.length - 1; ++j)
      {
      Item item = (Item)this.recipeItems[j + 1][i];
      CraftingManager.getInstance().addRecipe(new ItemStack(item), new Object[] {this.recipePatterns[j], 'X', object});
      }
    }
  }

protected void addResearchRecipes()
  {
  ResourceListRecipe recipe;
  for(IResearchGoal goal : ResearchGoal.researchGoals)
    {
    if(goal==null){continue;}
    recipe = goal.constructRecipe();
    if(recipe!=null)
      {
      this.researchRecipes.add(recipe);
      }    
    }   
  }

protected void addAlchemyRecipes()
  {
  ResourceListRecipe recipe;
  
  recipe = new ResourceListRecipe(new ItemStack(Item.gunpowder), RecipeType.ALCHEMY);
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.netherrack,4), false, false));
  this.alchemyRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.cement.itemID, 8, ItemLoader.cement.getItemDamage()), RecipeType.ALCHEMY);
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.bucketWater), false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.sand), false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.clay), false, false));
  this.alchemyRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.reinforced,16,0), RecipeType.ALCHEMY_MISC);
  recipe.resources.add(new ItemStackWrapperCrafting(ItemLoader.cement, false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(Block.stoneBrick, 16, true, false));
  this.alchemyMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.reinforced,16,1), RecipeType.ALCHEMY_MISC);
  recipe.resources.add(new ItemStackWrapperCrafting(ItemLoader.cement, false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(Block.brick, 16, true, false));
  this.alchemyMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.reinforced,16,2), RecipeType.ALCHEMY_MISC);
  recipe.resources.add(new ItemStackWrapperCrafting(ItemLoader.cement, false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(Block.netherBrick, 16, true, false));
  this.alchemyMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.reinforced,16,3), RecipeType.ALCHEMY_MISC);
  recipe.resources.add(new ItemStackWrapperCrafting(ItemLoader.cement, false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(Block.obsidian, 8, true, false));
  this.alchemyMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.reinforced,16,4), RecipeType.ALCHEMY_MISC);
  recipe.resources.add(new ItemStackWrapperCrafting(ItemLoader.cement, false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(Block.sandStone, 16, true, false));
  this.alchemyMiscRecipes.add(recipe);
  }

protected void addCivicRecipes()
  {
  ResourceListRecipe recipe;
  for(ICivicType civic : Civic.civicList)
    {
    if(civic==null){continue;}
    recipe = civic.constructRecipe();
    if(recipe!=null)
      {
      this.civicRecipes.add(recipe);
      }    
    }  
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.warehouseStorage.blockID,1,0), RecipeType.CIVIC_MISC);
  recipe.neededResearch.add(ResearchGoalNumbers.civics1);
  recipe.neededResearch.add(ResearchGoalNumbers.logistics3);
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.chest, 1), false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.planks, 4), true, false));
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.warehouseStorage.blockID,1,1), RecipeType.CIVIC_MISC);
  recipe.neededResearch.add(ResearchGoalNumbers.civics2);
  recipe.neededResearch.add(ResearchGoalNumbers.logistics4);
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.chest, 2), false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.planks, 4), true, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.paper, 2), true, false));
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.warehouseStorage.blockID,1,2), RecipeType.CIVIC_MISC);
  recipe.neededResearch.add(ResearchGoalNumbers.civics3);
  recipe.neededResearch.add(ResearchGoalNumbers.logistics5);
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Block.chest, 2), false, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.ingotIron, 8), true, false));
  recipe.resources.add(new ItemStackWrapperCrafting(new ItemStack(Item.paper, 2), true, false));
  this.civicMiscRecipes.add(recipe);
    
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.crafting,1,0), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,1,false);//
  recipe.addResource(Block.stoneSingleSlab,2,false);//
  recipe.addResource(Block.planks, 4, true);//
  recipe.addResource(Item.paper,1,false);//
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.crafting,1,1), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,1,false);//
  recipe.addResource(Item.ingotIron,3,false);//
  recipe.addResource(Block.planks, 4, true);//
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.crafting,1,2), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,1,false);//
  recipe.addResource(Block.planks, 2, true);//
  recipe.addResource(Block.stone, 3, false);//
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.crafting,1,3), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,1,false);//
  recipe.addResource(Block.stoneSingleSlab,3,false);//
  recipe.addResource(Block.planks, 2, true);//
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.crafting,1,4), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,1,false);//
  recipe.addResource(Block.stoneSingleSlab,2,false);
  recipe.addResource(Item.ingotIron,1,false);//
  recipe.addResource(Block.planks, 2, true);//
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.crafting,1,5), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,1,false);  
  recipe.addResource(Item.ingotIron,5,false);
  recipe.addResource(Item.ingotGold,2,false);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.crafting,1,6), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,1,false);  
  recipe.addResource(Block.planks,6,false);
  recipe.addResource(Item.clay,1,false);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(BlockLoader.crafting,1,7), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,1,false);  
  recipe.addResource(Block.planks,7,false);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.backpack,1,0), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,1,false);  
  recipe.addResource(Block.cloth,8,true);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics2);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.backpack,1,16), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,2,false);  
  recipe.addResource(Block.cloth,8,true);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics3);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.backpack,1,32), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,3,false);  
  recipe.addResource(Block.cloth,8,true);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics4);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.backpack,1,48), RecipeType.CIVIC_MISC);
  recipe.addResource(Block.chest,4,false);  
  recipe.addResource(Block.cloth,8,true);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics5);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(BlockLoader.trashcan, RecipeType.CIVIC_MISC);
  recipe.addResource(Item.redstone, 2, false);
  recipe.addResource(Block.planks, 2, true);
  recipe.addResource(Block.stoneBrick, 2, true);
  recipe.addResource(Block.stone, 1, false);
  recipe.addResource(Item.bucketLava, 1, false);
  recipe.addResource(Item.stick, 1, false);
  this.civicMiscRecipes.add(recipe);
    
  recipe = new ResourceListRecipe(BlockLoader.mailbox, RecipeType.CIVIC_MISC);
  recipe.addResource(Item.enderPearl, 1, false);
  recipe.addResource(Item.ingotIron, 8, false);
  recipe.addResource(Block.chest, 1, false);
  recipe.addResource(Item.dyePowder, 4, 4, false);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics4);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(BlockLoader.mailboxIndustrial, RecipeType.CIVIC_MISC);
  recipe.addResource(Item.enderPearl, 1, false);
  recipe.addResource(Item.ingotIron, 8, false);
  recipe.addResource(Block.chest, 1, false);
  recipe.addResource(Item.dyePowder, 7, 4, false);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics5);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(BlockLoader.chunkloader, RecipeType.CIVIC_MISC);
  recipe.addResource(Item.enderPearl, 1, false);
  recipe.addResource(Block.stoneBrick,4, true);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics2);
  this.civicMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(BlockLoader.chunkloaderDeluxe, RecipeType.CIVIC_MISC);
  recipe.addResource(Item.enderPearl, 2, false);
  recipe.addResource(Block.obsidian,4, false);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics4);
  this.civicMiscRecipes.add(recipe);
  }

protected void addGateRecipes()
  {
  ResourceListRecipe recipe;
  for(IGateType gate : Gate.gateTypes)
    {
    if(gate==null){continue;}
    recipe = gate.constructRecipe();
    if(recipe!=null)
      {
      this.civicMiscRecipes.add(recipe);      
      }
    }
  }

protected void addStructureRecipes()
  {
  ResourceListRecipe recipe;
  for(ProcessedStructure structure : StructureManager.instance().getSurvivalModeStructures())
    {
    if(structure==null){continue;}
    recipe = structure.constructRecipe();
    if(recipe!=null)
      {
      this.structureRecipesServer.add(recipe);
      }
    }
  }

protected void addNpcRecipes()
  {
  ResourceListRecipe recipe;
  for(NpcTypeBase npc : NpcTypeBase.npcTypes)
    {
    if(npc==null){continue;}    
    for(int i = 0; i < npc.getNumOfLevels(); i++)
      {
      recipe = npc.constructRecipe(i);
      if(recipe!=null)
        {
        this.npcRecipes.add(recipe);
        }
      }      
    }
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.npcCommandBaton,1,0), RecipeType.NPC_MISC);
  recipe.addResource(Item.stick, 4, false);
  recipe.addResource(Item.ingotIron, 2, false);
  recipe.addNeededResearch(ResearchGoalNumbers.command2);
  this.npcMiscRecipes.add(recipe);  
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.npcCommandBaton,1,1), RecipeType.NPC_MISC);
  recipe.addResource(Item.stick, 4, false);
  recipe.addResource(Item.ingotGold, 2, false);
  recipe.addResource(Item.diamond, 1, false);
  recipe.addNeededResearch(ResearchGoalNumbers.command4);
  this.npcMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.courierRouteSlip,1,0), RecipeType.NPC_MISC);
  recipe.addResource(Item.paper, 1, false);
  recipe.addResource(Item.dyePowder, 1, 2, false);
  recipe.addResource(Block.planks, 1, true);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics3);
  this.npcMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.courierRouteSlip,1,1), RecipeType.NPC_MISC);
  recipe.addResource(Item.paper, 2, false);
  recipe.addResource(Item.dyePowder, 1, 2, false);
  recipe.addResource(Block.planks, 1, true);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics4);
  this.npcMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.courierRouteSlip,1,2), RecipeType.NPC_MISC);
  recipe.addResource(Item.paper, 3, false);
  recipe.addResource(Item.dyePowder, 1, 2, false);
  recipe.addResource(Block.planks, 1, true);
  recipe.addNeededResearch(ResearchGoalNumbers.logistics5);
  this.npcMiscRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(new ItemStack(ItemLoader.researchBook), RecipeType.NPC_MISC);
  recipe.addResource(Item.paper, 3, false);
  recipe.addResource(Item.leather, 4, false);
  recipe.addResource(Item.ingotGold, 2, false);  
  this.npcMiscRecipes.add(recipe);
  }

protected void addVehicleRecipes()
  {
  ResourceListRecipe recipe;
  for(IVehicleType vehicle : VehicleType.vehicleTypes)
    {
    if(vehicle==null){continue;}
    for(int i = 0; i < vehicle.getMaterialType().getNumOfLevels(); i++)
      {
      recipe = vehicle.constructRecipe(i);
      if(recipe!=null)
        {
        this.addVehicleRecipe(vehicle.constructRecipe(i));        
        }
      }
    } 
  }

protected void addUpgradeRecipes()
  {
  ResourceListRecipe recipe;
  for(IVehicleUpgradeType upgrade : VehicleUpgradeRegistry.instance().getUpgradeList())
    {
    if(upgrade==null){continue;}
    recipe = upgrade.constructRecipe();
    if(recipe!=null)
      {
      this.upgradeRecipes.add(recipe);
      }
    }  
  for(IVehicleArmorType t : ArmorRegistry.instance().getArmorTypes())
    {
    recipe = t.constructRecipe();
    if(t!=null)
      {
      this.upgradeRecipes.add(recipe);
      }
    }
 
  RecipeType type = RecipeType.VEHICLE_MISC;
  
  recipe = new ResourceListRecipe(ItemLoader.mobilityUnit, 1 , type);
  recipe.addResource(Item.ingotIron, 2, false);
  recipe.addResource(Block.pistonBase, 1, false);
  recipe.addResource(Item.redstone, 4, false);
  recipe.addNeededResearch(ResearchGoalNumbers.mobility1);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.turretComponents, 1 , type);
  recipe.addResource(Item.ingotIron, 1, false);
  recipe.addResource(Block.pistonBase, 1, false);
  recipe.addResource(Item.silk, 1, false);
  recipe.addResource(Item.redstone, 2, false);
  recipe.addNeededResearch(ResearchGoalNumbers.turrets1);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.torsionUnit, 1 , type);
  recipe.addResource(Item.ingotIron, 1, false);
  recipe.addResource(Item.stick, 2, false);
  recipe.addResource(Item.silk, 4, false);
  recipe.addNeededResearch(ResearchGoalNumbers.torsion1);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.counterWeightUnit, 1 , type);
  recipe.addResource(Item.ingotIron, 2, false);
  recipe.addResource(Block.cobblestone, 4, false);
  recipe.addResource(Item.silk, 4, false);
  recipe.addNeededResearch(ResearchGoalNumbers.counterweights1);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.powderCase, 1 , type);
  recipe.addResource(Item.ingotIron, 4, false);
  recipe.addResource(Block.cloth, 2, true);
  recipe.addNeededResearch(ResearchGoalNumbers.explosives1);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.equipmentBay, 1 , type);
  recipe.addResource(Item.ingotIron, 6, false);
  recipe.addResource(Block.cloth, 2, true);
  recipe.addResource(Block.planks, 2, true);  
  this.upgradeRecipes.add(recipe);

  recipe = new ResourceListRecipe(ItemLoader.wood1, 8 , type);
  recipe.addResource(Item.leather, 1, false);
  recipe.addResource(Block.planks, 7, true);  
  recipe.addNeededResearch(ResearchGoalNumbers.wood1);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.wood2, 8 , type);
  recipe.addResource(Item.leather, 2, false);
  recipe.addResource(Item.ingotIron, 1, false);
  recipe.addResource(Block.planks, 5, true);    
  recipe.addNeededResearch(ResearchGoalNumbers.wood2);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.wood3, 8 , type);
  recipe.addResource(Item.leather, 1, false);
  recipe.addResource(Item.ingotIron, 3, false);
  recipe.addResource(Block.planks, 4, true);    
  recipe.addNeededResearch(ResearchGoalNumbers.wood3);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.wood4, 8 , type);  
  recipe.addResource(Item.ingotIron, 4, false);
  recipe.addResource(Block.planks, 4, true);    
  recipe.addNeededResearch(ResearchGoalNumbers.wood4);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.iron1, 8 , type);  
  recipe.addResource(Item.ingotIron, 8, false);    
  recipe.addNeededResearch(ResearchGoalNumbers.iron1);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.iron2, 8 , type);  
  recipe.addResource(Item.ingotIron, 8, false);
  recipe.addResource(Item.coal, 2, true);
  recipe.addNeededResearch(ResearchGoalNumbers.iron2);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.iron3, 8 , type);  
  recipe.addResource(Item.ingotIron, 12, false);
  recipe.addResource(Item.coal, 4, true);
  recipe.addNeededResearch(ResearchGoalNumbers.iron3);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.iron4, 8 , type);  
  recipe.addResource(Item.ingotIron, 8, false);
  recipe.addResource(Item.ingotGold, 4, false);
  recipe.addResource(Item.redstone, 4, false);
  recipe.addResource(Item.coal, 8, true);
  recipe.addNeededResearch(ResearchGoalNumbers.iron4);
  this.upgradeRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.iron5, 8 , type);  
  recipe.addResource(Item.ingotIron, 8, false);
  recipe.addResource(Item.ingotGold, 8, false);
  recipe.addResource(Item.redstone, 4, false);
  recipe.addResource(Item.coal, 8, true);
  recipe.addNeededResearch(ResearchGoalNumbers.iron5);
  this.upgradeRecipes.add(recipe);
  
  }

protected void addAmmoRecipes()
  {
  ResourceListRecipe recipe;
  for(IAmmoType ammo : Ammo.ammoTypes)
    {
    if(ammo==null){continue;}
      {
      recipe = ammo.constructRecipe();
      if(recipe!=null)
        {
        ammoRecipes.add(recipe);
        }
      }
    } 
  
  RecipeType type = RecipeType.AMMO_MISC;
  
  recipe = new ResourceListRecipe(ItemLoader.flameCharge, 8 , type);
  recipe.addResource(Item.coal, 2, true);
  recipe.addResource(Block.cloth, 1, true);
  recipe.addNeededResearch(ResearchGoalNumbers.flammables1);
  this.componentRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.explosiveCharge, 8 , type);
  recipe.addResource(Item.gunpowder, 2, false);
  recipe.addResource(Item.coal, 1, true);
  recipe.addResource(Item.silk, 1, false);
  recipe.addNeededResearch(ResearchGoalNumbers.explosives1);
  this.componentRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.rocketCharge, 8 , type);
  recipe.addResource(Item.gunpowder, 2, false);
  recipe.addResource(Item.paper, 1, false);
  recipe.addResource(Item.silk, 1, false);
  recipe.addNeededResearch(ResearchGoalNumbers.rockets1);
  this.componentRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.clusterCharge, 8 , type);  
  recipe.addResource(Item.ingotIron, 1, false);
  recipe.addResource(Block.cloth, 1, true);
  recipe.addNeededResearch(ResearchGoalNumbers.ballistics1);
  this.componentRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.napalmCharge, 8 , type);  
  recipe.addResource(Item.gunpowder, 4, false);
  recipe.addResource(Block.cloth, 2, true);
  recipe.addResource(Block.cactus, 2, true);
  recipe.addNeededResearch(ResearchGoalNumbers.flammables2);
  recipe.addNeededResearch(ResearchGoalNumbers.ballistics1);
  this.componentRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.clayCasing, 8 , type);  
  recipe.addResource(Item.clay, 4, false);
  recipe.addResource(Block.cloth, 1, true);
  this.componentRecipes.add(recipe);
  
  recipe = new ResourceListRecipe(ItemLoader.ironCasing, 8 , type);  
  recipe.addResource(Item.ingotIron, 4, false);
  recipe.addResource(Block.cloth, 1, true);
  recipe.addNeededResearch(ResearchGoalNumbers.iron1);
  this.componentRecipes.add(recipe);
  }

public void addStructureRecipe(ProcessedStructure struct)
  {
  if(struct==null || !struct.survival){return;}
  ResourceListRecipe recipe = null;
  for(ResourceListRecipe test : this.structureRecipesServer)
    {
    if(test.getDisplayName().equals(struct.name))
      {
      recipe = test;
      break;
      }
    }
  if(recipe!=null)
    {
    this.structureRecipesServer.remove(recipe);
    }
  recipe = struct.constructRecipe();
  if(recipe!=null)
    {
    this.structureRecipesServer.add(recipe);
    }
  }

public void addStructureRecipe(StructureClientInfo info)
  {
  if(info==null || !info.survival){return;}
  ResourceListRecipe recipe = null;
  for(ResourceListRecipe test : this.structureRecipesClient)
    {
    if(test.getDisplayName().equals(info.name))
      {
      recipe = test;
      break;
      }
    }
  if(recipe!=null)
    {
    this.structureRecipesClient.remove(recipe);
    }
  recipe = info.constructRecipe();
  if(recipe!=null)
    {
    this.structureRecipesClient.add(recipe);
    }
  }

public void resetClientData()
  {
  this.structureRecipesClient.clear();
  }

public void addVehicleRecipe(ResourceListRecipe recipe)
  {
  this.vehicleRecipes.add(recipe);
  }

public List<ResourceListRecipe> getRecipesOfTypesFor(PlayerEntry entry, boolean creative, RecipeType...recipeTypes)
  {
  List<ResourceListRecipe> found = new ArrayList<ResourceListRecipe>();
  for(RecipeType type : recipeTypes)
    {
    List<ResourceListRecipe> recipes = this.recipesByType.get(type);
    for(ResourceListRecipe recipe : recipes)
      {
      if(creative || Config.disableResearch  || recipe.canBeCraftedBy(entry) )
        {
        found.add(recipe);
        }
      }
    }
  return found; 
  }

public List<ResourceListRecipe> getStructureRecipesClient()
  {
  return Lists.newArrayList(this.structureRecipesClient);
  }

public List<ResourceListRecipe> getStructureRecipesServer()
  {
  return Lists.newArrayList(this.structureRecipesServer);
  }

public List<ResourceListRecipe> getRecipesContaining(ItemStack stack)
  {
  List<ResourceListRecipe> recipes = new ArrayList<ResourceListRecipe>();
  for(List<ResourceListRecipe> list : this.recipesByType.values())
    {
    for(ResourceListRecipe recipe : list)
      {
      for(ItemStackWrapperCrafting item : recipe.resources)
        {
        if(item.matches(stack))
          {
          recipes.add(recipe);
          }
        }
      }
    }
  return recipes;
  }

public List<ResourceListRecipe> getRecipesDependantOn(IResearchGoal goal)
  {
  List<ResourceListRecipe> recipes = new ArrayList<ResourceListRecipe>();
  for(ResourceListRecipe recipe : this.vehicleRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.ammoRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.civicRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.civicMiscRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.upgradeRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.npcRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.npcMiscRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.alchemyRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  for(ResourceListRecipe recipe : this.alchemyMiscRecipes)
    {
    if(recipe.neededResearch.contains(goal.getGlobalResearchNum()))
      {
      recipes.add(recipe);
      }
    }
  return recipes;
  }

}
