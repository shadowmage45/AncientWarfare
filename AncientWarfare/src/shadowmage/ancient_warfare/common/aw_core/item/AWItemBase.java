package shadowmage.ancient_warfare.common.aw_core.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class AWItemBase extends Item
{

protected List subTypes = new ArrayList<ItemStack>();

public AWItemBase(int itemID, boolean hasSubTypes)
  {
  super(itemID);  
  this.setHasSubtypes(hasSubTypes);
  this.setCreativeTab(CreativeTabAW.instance());
  }

@Override
public String getTextureFile()
  {
  return "/shadowmage/ancient_warfare/resources/item/items.png";
  }

/**
 * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
 */
@SideOnly(Side.CLIENT)
@Override
public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
  {
  if(this.hasSubtypes)
    {
    for(Object obj : this.subTypes)
      {
      ItemStack stack = (ItemStack)obj;
      if(stack!=null)
        {
        par3List.add(stack);
        }
      }
    }
  }

/**
 * add a subtype to this Item, to be displayed in creative inventories
 * @param stack
 */
public void addSubType(ItemStack stack)
  {
  if(stack==null)
    {
    return;
    }
  /**
   * if it does not obviously contain the stack
   */
  if(!this.subTypes.contains(stack))
    {
    /**
     * test to see if it contains a matching stack for itemID/DMG
     */
    for(Object obj : this.subTypes)
      {
      ItemStack test = (ItemStack)obj;
      if(test!=null)
        {
        if(ItemStack.areItemStacksEqual(stack, test))
          {
          return;
          }        
        }
      }
    this.subTypes.add(stack);
    }
  
  }

@Override
public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
  {
  //TODO setup desc. registry, to add tooltip info from items/stacks/we
  super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
  }

/**
 * the first/top call for iconIndex
 */
@Override
public int getIconIndex(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining)
  {
  if(stack.hasTagCompound())
    {
    NBTTagCompound tag = stack.getTagCompound();
    if(tag.hasKey("AWModData"))
      {
      NBTTagCompound modTag = tag.getCompoundTag("AWModData");
      if(modTag.hasKey("icon"))
        {
        return modTag.getInteger("icon");
        }
      }
    }
  if(stack.getItem().getHasSubtypes())
    {
    return stack.getItemDamage();
    }
  return super.getIconIndex(stack, renderPass, player, usingItem, useRemaining);
  }

public void setModTag(ItemStack stack)
  {
  NBTTagCompound modData = new NBTTagCompound("AWModData");
  stack.setTagInfo("AWModData", modData);
  }

public static void setIconIndex(ItemStack stack, int index)
  {  
  if(stack.hasTagCompound())
    {
    stack.getTagCompound().getCompoundTag("AWModData").setInteger("icon", index);
    }   
  }

}
