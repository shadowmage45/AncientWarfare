package shadowmage.ancient_warfare.common.aw_core.registry.entry;

import java.util.ArrayList;
import java.util.List;

public class ItemDescription
{

public String displayName;
public String description = "";
public List usedToCraft = new ArrayList<String>();
public List usedIn = new ArrayList<String>();//what this item is used IN, if it is a module type item (armor, upgrades, ammo)

public ItemDescription(String name)
  {
  this.displayName = name;
  }

public ItemDescription setDescription(String desc)
  {
  this.description = desc;
  return this;
  }

public void addCraft(String in)
  {
  this.usedToCraft.add(in);
  }

public void addUsedIn(String in)
  {
  this.usedIn.add(in);
  }

/**
 * return the description for this entry as a series of strings formatted for length
 * @param len 
 * @return
 */
public List<String> getFormattedDescription(int charLen)
  {
  //TODO
  return null;
  }

}
