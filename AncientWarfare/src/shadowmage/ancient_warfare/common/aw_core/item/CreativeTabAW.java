package shadowmage.ancient_warfare.common.aw_core.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class CreativeTabAW extends CreativeTabs
{

private static CreativeTabAW INSTANCE = new CreativeTabAW("Ancient Warfare");

public static CreativeTabAW instance()
  {
  return INSTANCE;
  }

private CreativeTabAW(String label)
  {
  super(label); 
  }

@SideOnly(Side.CLIENT)

/**
 * Get the ItemStack that will be rendered to the tab.
 */
@Override
public ItemStack getIconItemStack()
  {
  return new ItemStack(Item.stick,1);
  }

/**
 * Gets the translated Label.
 */
@Override
public String getTranslatedTabLabel()
  {
  return this.getTabLabel();
  }

}
