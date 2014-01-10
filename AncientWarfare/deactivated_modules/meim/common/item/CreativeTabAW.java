package shadowmage.meim.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabAW extends CreativeTabs
{

private static CreativeTabAW INSTANCE = new CreativeTabAW("MEIM");

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
  return new ItemStack(ItemLoader.guiOpener);
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
