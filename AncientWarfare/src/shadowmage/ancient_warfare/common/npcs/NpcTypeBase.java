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
package shadowmage.ancient_warfare.common.npcs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringTranslate;
import shadowmage.ancient_warfare.common.civics.CivicWorkType;
import shadowmage.ancient_warfare.common.crafting.RecipeType;
import shadowmage.ancient_warfare.common.crafting.ResourceListRecipe;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.npcs.ai.NpcAIObjective;
import shadowmage.ancient_warfare.common.npcs.types.NpcArcher;
import shadowmage.ancient_warfare.common.npcs.types.NpcBandit;
import shadowmage.ancient_warfare.common.npcs.types.NpcBanditRanged;
import shadowmage.ancient_warfare.common.npcs.types.NpcCombatEngineer;
import shadowmage.ancient_warfare.common.npcs.types.NpcCommander;
import shadowmage.ancient_warfare.common.npcs.types.NpcCourier;
import shadowmage.ancient_warfare.common.npcs.types.NpcCraftsman;
import shadowmage.ancient_warfare.common.npcs.types.NpcDummy;
import shadowmage.ancient_warfare.common.npcs.types.NpcFarmer;
import shadowmage.ancient_warfare.common.npcs.types.NpcFisherman;
import shadowmage.ancient_warfare.common.npcs.types.NpcFootsoldier;
import shadowmage.ancient_warfare.common.npcs.types.NpcLumberjack;
import shadowmage.ancient_warfare.common.npcs.types.NpcMedic;
import shadowmage.ancient_warfare.common.npcs.types.NpcMiner;
import shadowmage.ancient_warfare.common.npcs.types.NpcResearcher;
import shadowmage.ancient_warfare.common.npcs.types.NpcSiegeEngineer;
import shadowmage.ancient_warfare.common.npcs.types.NpcVillager;
import shadowmage.ancient_warfare.common.registry.NpcRegistry;
import shadowmage.ancient_warfare.common.vehicles.missiles.IAmmoType;

public abstract class NpcTypeBase implements INpcType
{

public static NpcTypeBase [] npcTypes = new NpcTypeBase[256];

public static String[] defaultTargetList = new String[]{"Zombie", "Spider", "CaveSpider", "Blaze", 
    "Ghast", "Giant", "LavaSlime", "PigZombie", "Silverfish", "Skeleton", "Slime"};

public static INpcType npcDummy = new NpcDummy(0);
public static INpcType npcVillager = new NpcVillager(1);
public static INpcType npcFootSoldier = new NpcFootsoldier(2);//icon made
public static INpcType npcArcher = new NpcArcher(3);//icon made
public static INpcType npcSiegeEngineer = new NpcSiegeEngineer(4);
public static INpcType npcMedic = new NpcMedic(5);//icon made
public static INpcType npcCombatEngineer = new NpcCombatEngineer(6);
public static INpcType npcCommander = new NpcCommander(7);
public static INpcType npcFisherman = new NpcFisherman(8);//icon made
public static INpcType npcCraftsman = new NpcCraftsman(9);//icon made
public static INpcType npcMiner = new NpcMiner(10);//icon made
public static INpcType npcFarmer = new NpcFarmer(11);//icon made
public static INpcType npcLumberjack = new NpcLumberjack(12);//icon made
public static INpcType npcCourier = new NpcCourier(13);
//chest courier ?
public static INpcType npcResearcher = new NpcResearcher(15);
public static INpcType npcBandit = new NpcBandit(16);
public static INpcType npcBanditArcher = new NpcBanditRanged(17);

protected int npcType;
protected boolean isCombatUnit = false;
protected boolean isVanillaVillager = false;
protected boolean isAvailableInSurvival = true;
protected boolean isBandit = false;
protected String iconTexture = "foo";
protected String configName = "civilian";
protected String[] defaultTargets = null;
protected List<NpcLevelEntry> levelEntries = new ArrayList<NpcLevelEntry>();

public NpcTypeBase(int type)
  {
  this.npcType = type;
  if(type>=0 && type<npcTypes.length)
    {
    npcTypes[type] = this;
    }
  }

@Override
public String[] getDefaultTargets()
  {
  return this.defaultTargets;
  }

@Override
public String getConfigName()
  {
  return this.configName;
  }

@Override
public int getActionTicks(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).actionTicks;
    }
  return 35;
  }

@Override
public ItemStack getUpkeepAdditionalItem(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).upkeepAdditionalItem;
    }  
  return null;
  }

@Override
public int getUpkeepCost(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).upkeepCost;
    }
  return 8;
  }

@Override
public int getAttackDamage(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).attackDamage;
    }
  return 4;
  }

@Override
public int getGlobalNpcType()
  {
  return npcType;
  }

@Override
public int getNumOfLevels()
  {
  return this.levelEntries.size();
  }

@Override
public String getDisplayName(int level)
  {
  return this.getLevelName(level);
  }

@Override
public String getLocalizedName(int level)
  {
  return StringTranslate.getInstance().translateKey(this.getLevelName(level));
  }

@Override
public String getLocalizedTooltip(int level)
  {
  return StringTranslate.getInstance().translateKey(this.getDisplayTooltip(level));
  }


@Override
public float getRangedAttackDistance(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).rangedAttackDistance;
    }
  return 16;
  }

@Override
public String getLevelName(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).name;
    }
  return "No level Name";
  }

@Override
public String getDisplayTooltip(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).tooltip;
    }
  return null;
  }

@Override
public String getDisplayTexture(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).texture;
    }
  return "foo.png";
  }

protected void addLevel(NpcLevelEntry entry)
  {
  this.levelEntries.add(entry);
  }

public NpcLevelEntry addLevel(int type, int level, String tex, ItemStack toolStack, ItemStack[] armorStacks)
  {
  NpcLevelEntry entry = new NpcLevelEntry(type, level, tex, toolStack, armorStacks);
  this.levelEntries.add(entry);
  return entry;
  }

/**
 * overridable helper method to be used during setting of npc constructor info
 * @param level
 * @return
 */
protected ItemStack getToolStack(int level)
  {  
  return null;
  }

/**
 * overridable helper method to be used during setting of npc constructor info
 * @param level
 * @return
 */
protected ItemStack[] getArmorStack(int level)
  {
  ItemStack[] stacks = new ItemStack[4];  
  return stacks;
  }

@Override
public int getMaxHealth(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).health;
    }
  return 20;
  }

@Override
public boolean isCombatUnit()
  {
  return isCombatUnit;
  }

@Override
public int getInventorySize(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).inventorySize;
    }
  return 9;
  }

@Override
public int getSpecInventorySize(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).specInventorySize;
    }
  return 1;
  }

@Override
public ItemStack getTool(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).toolStack;
    }
  return null;
  }

@Override
public ItemStack[] getArmor(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).getArmorStacks();
    }
  return new ItemStack[4];
  }

@Override
public IAmmoType getAmmoType(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).ammo;
    }
  return null;
  }

@Override
public float getAccuracy(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).accuracy;
    }
  return 1.f;
  }

@Override
public NpcVarsHelper getVarsHelper(NpcBase npc)
  {
  return new NpcVarHelperDummy(npc);
  }

@Override
public boolean isVanillaVillager()
  {
  return isVanillaVillager;
  }

public static INpcType getNpcType(int num)
  {
  if(num>=0 && num < npcTypes.length)
    {
    return npcTypes[num];
    }
  return null;
  }

public static INpcType[] getNpcTypes()
  {
  return npcTypes;
  }

@Override
public List<NpcAIObjective> getAI(NpcBase npc, int level)
  {
  ArrayList<NpcAIObjective> aiEntries = new ArrayList<NpcAIObjective>(); 
  return aiEntries;
  }

@Override
public List<CivicWorkType> getWorkTypes(int level)
  {
  if(level>=0 && level< this.levelEntries.size())
    {
    return this.levelEntries.get(level).workTypes;
    }
  return null;
  }

@Override
public String getIconTexture()
  {
  return "ancientwarfare:npc/"+iconTexture;
  }

@Override
public void openGui(EntityPlayer player, NpcBase npc)
  {  
  //GUIHandler.instance().openGUI(GUIHandler.NPC_BASE, player, npc.worldObj, npc.entityId, 0, 0);
  }

@Override
public Collection<Integer> getNeededResearch(int level)
  {
  if(level>=0 && level<this.levelEntries.size())
    {
    return this.levelEntries.get(level).getNeededResearch();
    }
  return Collections.emptySet();
  }

@Override
public ResourceListRecipe constructRecipe(int level)
  {
  if(this.isVanillaVillager || !this.isAvailableInSurvival){return null;}
  if(level>=0 && level<this.levelEntries.size())
    {
    NpcLevelEntry entry = this.levelEntries.get(level);
    ResourceListRecipe recipe = new ResourceListRecipe(NpcRegistry.getStackFor(this, level), RecipeType.NPC);
    recipe.addNeededResearch(this.getNeededResearch(level));
    recipe.addResources(entry.getNeededResources());
    ItemStack[] stacks = this.getArmor(level);
    if(stacks!=null)
      {
      for(ItemStack stack : stacks)
        {
        if(stack!=null)
          {
          recipe.addResource(stack, 1, false, false);          
          }
        }      
      }
    ItemStack stack = this.getTool(level);
    if(stack!=null)
      {
      recipe.addResource(stack, 1, false, false);      
      }
    recipe.addResource(new ItemStack(ItemLoader.rations), this.getUpkeepCost(level), false, false);
    recipe.addResource(new ItemStack(Item.paper), level+1, false, false);
    return recipe;
    }
  return null;
  }


@Override
public boolean isBandit()
  {
  return this.isBandit;
  }

@Override
public boolean isAvailableInSurvival()
  {
  return this.isAvailableInSurvival;
  }

public class NpcVarHelperDummy extends NpcVarsHelper
{

/**
 * @param npc
 */
public NpcVarHelperDummy(NpcBase npc)
  {
  super(npc);
  }

@Override
public void onTick()
  {

  }

@Override
public float getVar1()
  {
  return 0;
  }

@Override
public float getVar2()
  {
  return 0;
  }

@Override
public float getVar3()
  {
  return 0;
  }

@Override
public float getVar4()
  {
  return 0;
  }

@Override
public float getVar5()
  {
  return 0;
  }

@Override
public float getVar6()
  {
  return 0;
  }

@Override
public float getVar7()
  {
  return 0;
  }

@Override
public float getVar8()
  {
  return 0;
  }
}

}
