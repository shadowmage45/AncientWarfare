package shadowmage.meim.client.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.meim.client.meim_model.MEIMModelBase;
import shadowmage.meim.client.modelrenderer.MEIMModelRenderer;
import shadowmage.meim.client.texture.TextureManager;
import shadowmage.meim.common.config.MEIMConfig;

public class GuiTextureMapEditor extends GuiContainerAdvanced
{

final GuiMEIM parent;


TextureExportMap map = new TextureExportMap();
BufferedImage img = new BufferedImage(256,256,BufferedImage.TYPE_INT_ARGB);
int imgNum;

int currentLowestDisplayed = 0;
boolean contentsChanged = false;
String currentPartName = "";

public GuiTextureMapEditor(GuiMEIM parent)
  {
  super(parent.container);
  this.parent = parent;
  if(this.parent.currentPart!=null)
    {
    this.currentPartName = parent.currentPart.getName();
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
  return 256;
  }

@Override
public String getGuiBackGroundTexture()
  {  
  return null;
  }

@Override
protected void mouseClicked(int x, int y, int par3)
  {
  super.mouseClicked(x, y, par3);  
//  if(isMouseOverControl(x, y))
//    {
//    return;
//    }
//  if(par3==0)
//    {
//    if(x<5 || x >100 || y <guiTop+35 || y > guiTop+135)
//      {     
//      return;
//      }
//    int adjY = y - (35+guiTop);
//    int index = adjY/10;   
//    String name = getNameClickedOn(currentLowestDisplayed, index);   
//    MEIMModelRenderer rend = this.parent.model.getRenderForName(name);
//    if(rend!=null)
//      {
//      this.currentPartName = name;
//      parent.swapParts(rend);
//      parent.refreshGui();
//      this.forceUpdate = true;
//      }    
//    }
//  else if(par3==1)
//    {
//
//    }  
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  //TODO

  this.drawString(fontRenderer, "CurrentPart: "+currentPartName, 5, 5, 0xffffffff);
   
  for(int i = currentLowestDisplayed, y = 35+guiTop, k = 0; i < partNames.size() && k <10; i++, y +=10, k++)
    {
    this.drawString(fontRenderer, this.partNames.get(i), 5, y, 0xffffffff);
    }
  /**
   * left-side, prev/next buttons  --prev depth/more depth
   * list of parts, perhaps crop depth
   * 
   * select part to change its texture mapping
   * 
   * 
   */
  this.renderTextureMap();
  }


public void renderTextureMap()
  {  
  TextureManager.bindTexture();  
  this.drawTexturedModalRect(width-256-40, 0, 0, 0, 256, 256);
  TextureManager.resetBoundTexture();
  }

/**
 * transfer all information from TE local map data into a local texture
 */
public void setupMap()
  { 
  for(int x = 0; x<256; x++)
    {
    for(int y = 0; y<256; y++)
      {        
      this.img.setRGB(x, y, map.getColorFor(x, y));
      }
    }
  TextureManager.updateTextureContents(img);
  }

public void initializeTexture()
  {
  for(int x = 0; x<256; x++)
    {
    for(int y = 0; y<256; y++)
      {        
      this.map.set(x,y,(byte) 0);
      this.img.setRGB(x, y, map.getColorForIndex((byte) 0));
      }
    } 
//  for(MEIMModelRenderer rend : parent.model.baseParts)
//    {
//    this.recurseAddPartMaps(rend);
//    }
  }

GuiNumberInputLine xBox;
GuiNumberInputLine yBox;

@Override
public void updateScreenContents()
  {  
  if(contentsChanged)
    {
//    if(this.parent.currentPart!=null)
//      {
//      this.parent.currentPart.reinitCubeList();
//      }
    this.initializeTexture();
    this.setupMap();
    this.contentsChanged = false;
    }
 
  }

//@Override
//public void onTextBoxActivated(GuiTextFieldAdvanced box, int i)
//  {
//  switch(box.textBoxNumber)
//  {
//  case 0:
//  if(parent.currentPart!=null)
//    {
//    this.parent.currentPart.textureOffsetX+=i;
//    this.contentsChanged = true;
//    this.forceUpdate = true;
//    this.xBox.setText(String.valueOf(this.parent.currentPart.textureOffsetX));
//    }  
//  break;
//  
//  case 1:
//  if(parent.currentPart!=null)
//    {
//    this.parent.currentPart.textureOffsetY+=i;
//    this.contentsChanged = true;
//    this.forceUpdate = true;
//    this.yBox.setText(String.valueOf(this.parent.currentPart.textureOffsetY));
//    }
//  break;
//  
//  default:
//  break;
//  }
//  // TODO Auto-generated method stub
//  super.onTextBoxActivated(box, i);
//  }

List<String> partNames = new ArrayList<String>();

public void addAllParts(MEIMModelBase model)
  {
  this.partNames.clear();
  for(MEIMModelRenderer rend : model.baseParts)
    {
    this.recursePartNames(rend);
    }
  }

public void recursePartNames(MEIMModelRenderer renderer)
  {
  partNames.add(renderer.boxName);
  if(renderer.childModels!=null)
    {
    for(MEIMModelRenderer rend : renderer.childModels)
      {
      recursePartNames(rend);
      }
    }
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

public void recurseAddPartMaps(MEIMModelRenderer rend)
  {
  if(rend != null && rend.cubeList!=null && rend.cubeList.size()>0)
    {
    this.map.addBoxToMapAt(rend.cubeList.get(0), rend.textureOffsetX, rend.textureOffsetY);
    }
  if(rend.childModels!=null && rend.childModels.size()>0)
    {
    for(MEIMModelRenderer render : rend.childModels)
      {
      recurseAddPartMaps(render);
      }
    }
  }

public void exportImage()
  {
  try 
    {
    BufferedImage bi = img;
    long ts = System.currentTimeMillis();
    File outputfile = new File(MEIMConfig.getTexExportDir(), String.valueOf(ts+".png"));
    MEIMConfig.logDebug("attempting export to: "+outputfile.getAbsolutePath());
    if(!outputfile.exists())
      {
      File newoutputfile = new File(outputfile.getParent());
      newoutputfile.mkdirs();
      //outputfile.mkdirs();
      outputfile.createNewFile();
      }
    ImageIO.write(bi, "png", outputfile);
    }
  catch (IOException e) 
    {
    MEIMConfig.logDebug("Error during export of UV map");
    e.printStackTrace();
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

//  case 3:
//  if(parent.currentPart!=null)
//    {
//    this.parent.currentPart.textureOffsetX++;
//    }
//  break;
//  case 4:
//  if(parent.currentPart!=null)
//    {
//    this.parent.currentPart.textureOffsetY++;
//    }
//  break;  
//  case 5:
//  if(parent.currentPart!=null)
//    {
//    this.parent.currentPart.textureOffsetX--;
//    }
//  break;
//  case 6:
//  if(parent.currentPart!=null)
//    {
//    this.parent.currentPart.textureOffsetY--;
//    }
//  break;
//
//  case 7:
//  this.exportImage();
//  break;
//  
//  case 8:
//  if(parent.currentPart!=null)
//    {
//    this.parent.currentPart.textureOffsetX = xBox.getIntVal();
//    this.contentsChanged = true;
//    
//    } 
//  break;
//  
//  case 9:
//  if(parent.currentPart!=null)
//    {
//    this.parent.currentPart.textureOffsetY = yBox.getIntVal();
//    this.contentsChanged = true;
//    }
//  break;
//  
//  default:
//  break;
  }
  this.forceUpdate = true;
  this.contentsChanged = true;
  }

@Override
public void setupControls()
  {
//  this.addAllParts(parent.model);
  this.initializeTexture();
  this.setupMap();
  this.addGuiButton(0, 35, 16, "Done");  
  this.addGuiButton(7, 35, 16, "Export");  
  
  this.addGuiButton(1, 35, 12, "Prev");
  this.addGuiButton(2, 35, 12, "Next");

  this.addGuiButton(3, 30, 12, "+x");
  this.addGuiButton(4, 30, 12, "+y");
  this.addGuiButton(5, 30, 12, "-x");
  this.addGuiButton(6, 30, 12, "-y");  
  xBox = this.addNumberField(8, 28, 10, 4, "");
  yBox = this.addNumberField(9, 28, 10, 4, "");  
  }

@Override
public void updateControls()
  {
//  if(this.parent.currentPart!=null)
//    {
//    this.xBox.setValue(this.parent.currentPart.textureOffsetX);
//    this.yBox.setValue(this.parent.currentPart.textureOffsetY);    
//    } 
//  else
//    {
//    this.xBox.setValue(0);
//    this.yBox.setValue(0);    
//    }
  this.getElementByNumber(0).updateRenderPos(-guiLeft + width - 40, -guiTop+4);
  this.getElementByNumber(1).updateRenderPos(-guiLeft + 2, 20);
  this.getElementByNumber(2).updateRenderPos(-guiLeft + 2+2+35, 20);
  this.getElementByNumber(3).updateRenderPos( -guiLeft + 2,      160);
  this.getElementByNumber(4).updateRenderPos(-guiLeft + 2+2+30, 160);
  this.getElementByNumber(5).updateRenderPos(-guiLeft + 2,      160+12+2+12+2);
  this.getElementByNumber(6).updateRenderPos(-guiLeft + 2+2+30, 160+12+2+12+2);
  this.getElementByNumber(7).updateRenderPos(-guiLeft + width - 40, -guiTop+4+4+16);
  this.getElementByNumber(8).updateRenderPos( -guiLeft+2, 160+12+2);
  this.getElementByNumber(9).updateRenderPos(-guiLeft+2+2+30, 160+12+2);
  }





}
