package shadowmage.meim.client.gui;

import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.client.model.ModelBaseAW;
import shadowmage.ancient_framework.client.model.ModelPiece;
import shadowmage.ancient_framework.common.container.ContainerBase;
import shadowmage.meim.client.modelrenderer.MEIMModelRenderer;
import shadowmage.meim.common.config.MEIMConfig;

public class GuiSwapParents extends GuiContainerAdvanced
{

GuiMEIM parent;
ModelPiece thePart;
List<String> partNames = new ArrayList<String>();
int currentLowestDisplayed = 0;

public GuiSwapParents(ContainerBase cont, GuiMEIM parent, ModelPiece part)
    {
    super(cont);
    this.parent = parent;
    this.thePart = part;
    }

public void addAllParts(ModelBaseAW model)
  {
  this.partNames.clear();
  for(ModelPiece rend : model.getBasePieces())
    {
    this.recursePartNames(rend);
    }
  }

public void recursePartNames(ModelPiece renderer)
  {
  if(!renderer.getName().equals(thePart.getName()))
    {
    partNames.add(renderer.getName());
    for(ModelPiece rend : renderer.getChildren())
      {
      recursePartNames(rend);
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
public String getGuiBackGroundTexture()
  {
  return null;
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  for(int i = currentLowestDisplayed, y = 35+guiTop, k = 0; i < partNames.size() && k <10; i++, y +=10, k++)
    {
    this.drawString(fontRenderer, this.partNames.get(i), 5, y, 0xffffffff);
    }
  }

@Override
public void updateScreenContents()
  {
  
  }


public String getNameClickedOn(int lowestDisplayed, int index)
  {
  int choice = lowestDisplayed + index;
  MEIMConfig.logDebug("adjusted selection index: "+choice);
  if(choice<partNames.size())
    {
    return partNames.get(choice);
    }
  return null;
  }

@Override
protected void mouseClicked(int x, int y, int par3)
  {
  MEIMConfig.logDebug("mouse clicked" + x+","+y);
  
  super.mouseClicked(x, y, par3); 
  //left click
  if(par3==0)
    {
    if(x<5 || x >100 || y <35+guiTop || y > 135+guiTop)
      {
      MEIMConfig.logDebug("out of bounds click");
      return;
      }

    int adjY = y - (35+guiTop);
    int index = adjY/10;
    MEIMConfig.logDebug("index: "+index);
    String name = getNameClickedOn(currentLowestDisplayed, index);
    if(name==null)
      {
      MEIMConfig.logDebug("improper name returned");
      }
    else
      {
      MEIMConfig.logDebug("clicked on name: "+name);
      }
    ModelPiece rend = this.parent.model.getPiece(name);
    if(rend!=null)
      {
      if(thePart.getParent()!=null)
        {
        thePart.getParent().removeChild(thePart);        
        }
      rend.addChild(thePart);
      mc.displayGuiScreen(parent);
      }    
    }  
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
  {
  case 0:
  mc.displayGuiScreen(parent);

  case 1:
  if(this.currentLowestDisplayed-10 >=0)
    {
    this.currentLowestDisplayed-=10;
    }
  break;

  case 2:
  if(this.currentLowestDisplayed+10 < this.partNames.size())
    {
    this.currentLowestDisplayed+=10;
    }
  break;
  }
  }

@Override
public void setupControls()
  {
  this.addAllParts(parent.model);  
  this.addGuiButton(0, 35, 16, "Done");    
  this.addGuiButton(1, 35, 12, "Prev");
  this.addGuiButton(2, 35, 12, "Next");
  }

@Override
public void updateControls()
  {
  this.getElementByNumber(0).updateRenderPos(-guiLeft + width - 40, -guiTop+4);
  this.getElementByNumber(1).updateRenderPos(-guiLeft + 2, 20);
  this.getElementByNumber(2).updateRenderPos(-guiLeft + 2+2+35, 20);
  // TODO Auto-generated method stub
  
  }

}
