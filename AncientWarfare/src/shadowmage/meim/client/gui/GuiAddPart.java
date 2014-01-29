package shadowmage.meim.client.gui;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.ancient_framework.client.model.ModelPiece;
import shadowmage.ancient_framework.client.model.PrimitiveBox;
import shadowmage.meim.client.modelrenderer.MEIMModelRenderer;

public class GuiAddPart extends GuiContainerAdvanced
{

private final GuiMEIM parent;
final ModelPiece basePart;
/**
 * piece
 */
String pieceName = "";
GuiTextInputLine nameBox;


public GuiAddPart(GuiMEIM parent, ModelPiece basePart)
  {
  super(parent.container);
  this.parent = parent;
  this.basePart = basePart;
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
  // TODO Auto-generated method stub
  return null;
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  // TODO Auto-generated method stub
  
  }



@Override
public void updateScreenContents()
  {
  if(this.nameBox!=null)
    {
    this.pieceName = nameBox.getText();
    }  
  }

public void closeGUI()
  {
  mc.displayGuiScreen(parent);
  }

@Override
public void onElementActivated(IGuiElement element)
  {
  switch(element.getElementNumber())
  {
  case 0:
  this.closeGUI();
  break;
  case 1:
  if(pieceName.equals(""))
    {
    break;
    }
  ModelPiece piece = new ModelPiece(parent.model, pieceName, 0, 0, 0, 0, 0, 0, basePart);
  parent.model.addPiece(piece);
  PrimitiveBox box = new PrimitiveBox(piece, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0);
  piece.addPrimitive(box);
  parent.swapParts(piece);
  parent.refreshGui();
  this.closeGUI();
  break;
  
  default:
  break;  
  }
  }

@Override
public void setupControls()
  {
  this.addGuiButton(0, 35, 16, "Done");  
  this.addGuiButton(1, 35, 16, "Add Box");
  int size = 12;
  int buffer = 2;
  int col1 = -guiLeft + 4;
  int col2 = col1+size+buffer;
  int col3 = col2+size+buffer;
  int col4 = col3+size+buffer;
  int col5 = col4+size+buffer;
  int col6 = col5+size+buffer;
  int col7 = col6+size+buffer;
  int row1 = -guiTop + 4;
  int row2 = row1+size+buffer;
  int row3 = row2+size+buffer;
  int row4 = row3+size+buffer;
  int row5 = row4+size+buffer;
  int row6 = row5+size+buffer;
  int row7 = row6+size+buffer;
  int row8 = row7+size+buffer;
  int row9 = row8+size+buffer;
  int row10 = row9+size+buffer;
  
  nameBox = this.addTextField(3,  180, 10, 30, pieceName);
  }

@Override
public void updateControls()
  {
  this.nameBox.setText(pieceName);
  this.nameBox.updateRenderPos(this.getXSize()/2-90, this.getYSize()/2-8);
  this.getElementByNumber(0).updateRenderPos(-guiLeft + width - 40, -guiTop+4);
  this.getElementByNumber(1).updateRenderPos(-guiLeft + width - 40, -guiTop+4+16+4);
  }


}
