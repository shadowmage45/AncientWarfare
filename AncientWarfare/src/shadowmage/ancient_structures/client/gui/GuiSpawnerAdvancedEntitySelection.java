package shadowmage.ancient_structures.client.gui;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityList;
import net.minecraft.util.StatCollector;
import shadowmage.ancient_structures.client.gui.elements.Button;
import shadowmage.ancient_structures.client.gui.elements.CompositeScrolled;
import shadowmage.ancient_structures.client.gui.elements.Label;
import shadowmage.ancient_structures.common.config.AWStructureStatics;
import shadowmage.ancient_structures.common.tile.SpawnerSettings.EntitySpawnSettings;
import shadowmage.ancient_warfare.common.container.ContainerBase;

public class GuiSpawnerAdvancedEntitySelection extends GuiContainerBase
{

private GuiContainerBase parent;
private EntitySpawnSettings settings;
CompositeScrolled area;

Label selectionLabel;

public GuiSpawnerAdvancedEntitySelection(GuiContainerBase parent, EntitySpawnSettings settings)
  {
  super((ContainerBase) parent.inventorySlots, 256, 240, defaultBackground);
  this.parent = parent;
  this.settings = settings;
  }

@Override
protected boolean onGuiCloseRequested()
  {
  Minecraft.getMinecraft().displayGuiScreen(parent);
  return false;
  }

@Override
public void initElements()
  {
  area = new CompositeScrolled(0, 40, 256, 200);
  addGuiElement(area); 
  
  Label label = new Label(8, 8, StatCollector.translateToLocal("guistrings.spawner.select_entity"));
  addGuiElement(label);
  
  Button button = new Button(xSize-8-55, 8, 55, 12, StatCollector.translateToLocal("guistrings.done"))
    {
    @Override
    protected void onPressed()
      {
      Minecraft.getMinecraft().displayGuiScreen(parent);
      }
    };
  addGuiElement(button);
  
  selectionLabel = new Label(8, 20, settings.getEntityId());
  addGuiElement(selectionLabel);
  }

@Override
public void setupElements()
  {
  area.clearElements();
  
  int totalHeight = 8;
  Map<String, Class> mp = EntityList.stringToClassMapping;
  
  Button button;
  for(String name : mp.keySet())
    {
    if(AWStructureStatics.excludedSpawnerEntities.contains(name))
      {
      continue;//skip excluded entities
      }
    button = new Button(8, totalHeight, 256-8-16, 12, name)
      {
      @Override
      protected void onPressed()
        {
        settings.setEntityToSpawn(text);
        selectionLabel.setText(text);
        refreshGui();
        }
      };    
    area.addGuiElement(button);
    totalHeight+=12;
    }
  area.setAreaSize(totalHeight);
  }

}
