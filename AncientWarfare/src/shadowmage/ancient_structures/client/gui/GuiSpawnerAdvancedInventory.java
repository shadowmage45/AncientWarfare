package shadowmage.ancient_structures.client.gui;

import shadowmage.ancient_structures.common.container.ContainerSpawnerAdvancedInventoryBase;
import shadowmage.ancient_warfare.common.container.ContainerBase;

public class GuiSpawnerAdvancedInventory extends GuiContainerBase
{

public GuiSpawnerAdvancedInventory(ContainerBase par1Container)
  {
  super(par1Container, 256, 240, defaultBackground);
  }

@Override
public void initElements()
  {

  }

@Override
public void setupElements()
  {

  }

@Override
protected boolean onGuiCloseRequested()
  {
  ((ContainerSpawnerAdvancedInventoryBase)inventorySlots).sendSettingsToServer();
  return true;
  }

}
