package shadowmage.ancient_warfare.common.aw_core.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.registry.LanguageRegistry;

import shadowmage.ancient_warfare.common.aw_core.registry.entry.ItemDescription;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class DescriptionRegistry
{


private static DescriptionRegistry INSTANCE;
private DescriptionRegistry(){}

private class ItemDescriptionIDPair
{
public ItemDescriptionIDPair(int id, int dmg)
  {
  this.itemID = id;
  this.dmg = dmg;  
  }
public int itemID;
public int dmg;
}

public static DescriptionRegistry instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new DescriptionRegistry();
    }
  return INSTANCE;
  }

private static Map<ItemDescriptionIDPair,ItemDescription> descriptions = new HashMap<ItemDescriptionIDPair,ItemDescription>();

public void registerItem(Item item, String displayName)
  {
  this.registerItem(item.shiftedIndex, 1, displayName);
  }

public void registerItem(ItemStack stack, String displayName)
  {
  if(stack!=null)
    {
    this.registerItem(stack.itemID, stack.getItemDamage(), displayName);
    }
  }

/**
 * final regsiterItem call, all others go here, also registers item with languageRegistry
 * @param id
 * @param dmg
 * @param displayName
 */
public void registerItem(int id, int dmg, String displayName)
  {
  if(!this.contains(id, dmg))
    {
    this.descriptions.put(new ItemDescriptionIDPair(id, dmg), new ItemDescription(displayName));
    LanguageRegistry.addName(new ItemStack(id,1,dmg), displayName);
    }
  }

/**
 * stack version of addDescription
 * @param stack
 * @param description
 */
public void addDescription(ItemStack stack, String description)
  {
  if(this.contains(stack))
    {
    this.getEntryFor(stack.itemID, stack.getItemDamage());
    this.descriptions.get(new ItemDescriptionIDPair(stack.itemID, stack.getItemDamage())).setDescription(description);
    }    
  }

/**
 * item version of addDescription
 * @param item
 * @param description
 */
public void addDescription(Item item, String description)
  {
  this.addDescription(item.shiftedIndex, 1, description);
  }

/**
 * final addDescription call, all others end up here
 * @param id
 * @param dmg
 * @param desc
 */
public void addDescription(int id, int dmg, String desc)
  {
  if(this.contains(id, dmg))
    {
    this.getEntryFor(id, dmg).setDescription(desc);
    }
  }

/**
 * return an entry for an id/dmg pair
 * @param id
 * @param dmg
 * @return
 */
public ItemDescription getEntryFor(int id, int dmg)
  {
  for(ItemDescriptionIDPair desc : this.descriptions.keySet())
    {
    if(desc.itemID == id && desc.dmg == dmg)
      {
      return this.descriptions.get(desc);
      }
    }
  return null;
  }

/**
 * return description for an itemStack, dmg sensitive
 * @param stack
 * @return
 */
public String getDescriptionFor(ItemStack stack)
  {  
  return stack==null? "" : this.getDescriptionFor(stack.itemID, stack.getItemDamage());
  }

/**
 * return description for an item, using dmg1
 * @param item
 * @return
 */
public String getDescriptionFor(Item item)
  {
  return this.getDescriptionFor(item.shiftedIndex, 1);
  }

/**
 * return description for item id/dmg pair
 * @param id
 * @param dmg
 * @return
 */
public String getDescriptionFor(int id, int dmg)
  {
  if(this.contains(id, dmg))
    {
    return this.getEntryFor(id, dmg).description;        
    }
  return "";
  }

public boolean contains(ItemStack stack)
  {
  if(stack==null)
    {
    return false;
    }
  return this.contains(stack.itemID, stack.getItemDamage());
  }

public boolean contains(Item item)
  {
  return this.contains(item.shiftedIndex, 1);
  }

public boolean contains(int id, int dmg)
  {
  if(this.getEntryFor(id, dmg)!=null)
    {
    return true;
    }
  return false;
  }
}
