package shadowmage.meim.client.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_framework.client.gui.GuiContainerAdvanced;
import shadowmage.ancient_framework.client.gui.elements.GuiButtonSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiCheckBoxSimple;
import shadowmage.ancient_framework.client.gui.elements.GuiNumberInputLine;
import shadowmage.ancient_framework.client.gui.elements.GuiTextInputLine;
import shadowmage.ancient_framework.client.gui.elements.IFileSelectCallback;
import shadowmage.ancient_framework.client.gui.elements.IGuiElement;
import shadowmage.meim.client.modelrenderer.MEIMModelBox;
import shadowmage.meim.client.modelrenderer.MEIMModelRenderer;
import shadowmage.meim.common.config.MEIMConfig;

public class GuiExportModel extends GuiContainerAdvanced implements IFileSelectCallback
{

private List<String> fileSelection = new ArrayList<String>();
private GuiMEIM parent;
private GuiButtonSimple done;
private GuiButtonSimple export;
private GuiTextInputLine nameField;
private GuiTextInputLine superField;
private GuiCheckBoxSimple customBox;

//TODO
private GuiNumberInputLine texSizeX;
private GuiNumberInputLine texSizeY;


public GuiExportModel(GuiMEIM parent)
  {
  super(parent.container);
  this.parent = parent;
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
  return "/shadowmage/meim/resources/gui/guiBackgroundLarge.png";
  }

@Override
public void renderExtraBackGround(int mouseX, int mouseY, float partialTime)
  {
  int border = 2;
  int buffer = 2;
  int size = 12;
  
  int[] rows = new int[20];
  int[] cols = new int[20];
  
  for(int i = 0; i < 20; i++)
    {
    if(i == 0)
      {
      rows[i] = border;
      cols[i] = border;
      }
    else
      {
      rows[i] = rows[i-1]+buffer+size;
      cols[i] = cols[i-1]+buffer+size;
      }
    }
  this.drawStringGui("ModelName:", cols[0], rows[0], 0xffffffff);
  this.drawStringGui("SuperClass:", cols[0], rows[2], 0xffffffff);
  this.drawStringGui("CustomRender?", cols[2], rows[4]+2, 0xffffffff);
  }

@Override
public void updateScreenContents()
  {
  }


@Override
public void onElementActivated(IGuiElement element)
  {
  this.forceUpdate = true;
  switch(element.getElementNumber())
    {
    case 0://done
    mc.displayGuiScreen(parent);
    break;
    case 1://modelNameBox
    break;
    case 2://superClassNameBox
    break;
    case 3://useCustomModelRenderClass checkBox
    break;
    case 4://export button
    mc.displayGuiScreen(new GuiFileSelect(this, this, MEIMConfig.getJavaExportDir(), true));
    break;
    case 5://texXSize
    break;
    case 6://texYSize
    break;
    }
  }

@Override
public void setupControls()
  {
  int border = 2;
  int buffer = 2;
  int size = 12;
  
  int[] rows = new int[20];
  int[] cols = new int[20];
  
  for(int i = 0; i < 20; i++)
    {
    if(i == 0)
      {
      rows[i] = border;
      cols[i] = border;
      }
    else
      {
      rows[i] = rows[i-1]+buffer+size;
      cols[i] = cols[i-1]+buffer+size;
      }
    }
  
  this.done =  this.addGuiButton(0, 35, 16, "Done");
  done.updateRenderPos(getXSize()-35-2, rows[0]);
  this.export = this.addGuiButton(4, 45, 16, "Export");
  export.updateRenderPos(getXSize()-45-2, rows[2]);
  
  this.nameField = this.addTextField(1, 160, 12, 20, "");
  nameField.updateRenderPos(cols[0], rows[1]);
  this.superField = this.addTextField(2, 160, 12, 20, "");
  superField.updateRenderPos(cols[0], rows[3]);
  this.customBox = this.addCheckBox(3, 16, 16);
  customBox.updateRenderPos(cols[0], rows[4]);
  
  this.texSizeX = this.addNumberField(5, 40, 12, 6, "0");
  this.texSizeX.setValue(256);
  this.texSizeX.updateRenderPos(cols[0], rows[6]);
  
  this.texSizeY = this.addNumberField(6, 40, 12, 6, "0");
  this.texSizeY.setValue(256);
  this.texSizeY.updateRenderPos(cols[4], rows[6]);
  
  }

@Override
public void updateControls()
  {
  }

private void exportModel()
  {  
//  String fileName;
//  if(fileSelection.size()>0)
//    {
//    fileName = fileSelection.get(0);
//    fileSelection.clear();
//    }
//  else
//    {
//    MEIMConfig.logError("EmptyFile list when attempting to export");
//    return;
//    }
//  
//  String modelName = this.nameField.getText();
//  String superName = this.superField.getText(); 
//  String renderType = this.customBox.checked() ? "ModelRendererCustom" : "ModelRenderer";
//  if(modelName.equals(""))
//    {
//    modelName = "ModelNameHere";
//    }
//  if(superName.equals(""))
//    {
//    superName = "ModelBase";
//    }
//  
//  //TODO timestamp
//  ArrayList<String> lines = new ArrayList<String>();
//  lines.add("//auto-generated model template");
//  lines.add("//template generated by MEIM");
//  lines.add("//template v 1.0");
//  lines.add("//author Shadowmage45 (shadowage_catapults@hotmail.com)");
//  lines.add(" ");
//  lines.add("package foo.bad.pkg.set.yours.here;");
//  lines.add(" ");
//  lines.add(" ");
//  lines.add("public class "+modelName+" extends "+superName);
//  lines.add("{");
//  lines.add(" ");
//  for(MEIMModelRenderer rend : parent.model.baseParts)
//    {
//    if(rend.boxName==null)
//      {
//      continue;
//      }
//    lines.add(renderType+" "+rend.boxName+";");
//    if(rend.childModels!=null)
//      {
//      recursiveName(lines, rend, renderType);
//      }
//    }
//  lines.add("public "+modelName+"(){");  
//  for(MEIMModelRenderer rend : parent.model.baseParts)
//    {
//    recursiveDetails(lines, rend, renderType);
//    }
//  lines.add("  }");
//  lines.add(" ");
//  lines.add("@Override");
//  lines.add("public void render(Entity entity, float f1, float f2, float f3, float f4, float f5, float f6)");
//  lines.add("  {");
//  lines.add("  super.render(entity, f1, f2, f3, f4, f5, f6);");
//  lines.add("  setRotationAngles(f1, f2, f3, f4, f5, f6, entity);");
//  for(MEIMModelRenderer rend : parent.model.baseParts)
//    {
//    if(renderType.equals("ModelRendererCustom"))
//      {
//      lines.add("  "+rend.boxName+".bindParentTexture(entity.getTexture());");
//      }
//    lines.add("  "+rend.boxName+".render(f6);");
//    }
//  lines.add("  }");
//  lines.add(" ");
//  
//  lines.add("public void setPieceRotation(ModelRenderer model, float x, float y, float z)");
//  lines.add("  {");
//  lines.add("  model.rotateAngleX = x;");
//  lines.add("  model.rotateAngleY = y;");
//  lines.add("  model.rotateAngleZ = z;");
//  lines.add("  }");
//  lines.add("}");
//
//  try 
//    {    
//    long ts = System.currentTimeMillis();    
//    
//    File outputfile = new File(fileName);
//    MEIMConfig.logDebug("attempting export to: "+outputfile.getAbsolutePath());
//    if(!outputfile.exists())
//      {
//      File newoutputfile = new File(outputfile.getParent());
//      newoutputfile.mkdirs();
//      outputfile.createNewFile();
//      }
//    FileWriter writer = new FileWriter(outputfile);    
//    for(String line : lines)
//      {
//      writer.write(line +"\n");
//      }
//    writer.close();
//    }
//  catch (IOException e) 
//    {
//    MEIMConfig.logError("Error during export of java file");
//    e.printStackTrace();
//    }
//  for(String line : lines)
//    {
//    System.out.println(line);
//    }
  }

private void recursiveDetails(ArrayList<String> lines, MEIMModelRenderer rend, String renderType)
  {
  lines.add("  "+rend.boxName+" = new "+renderType+"(this,\""+rend.boxName+"\");");
  lines.add("  "+rend.boxName+".setTextureOffset("+rend.textureOffsetX+","+rend.textureOffsetY+");");
  lines.add("  "+rend.boxName+".setTextureSize("+texSizeX.getIntVal()+","+texSizeY.getIntVal()+");");
  lines.add("  "+rend.boxName+".setRotationPoint("+rend.rotationPointX+"f, "+rend.rotationPointY+"f, "+rend.rotationPointZ+"f);");
  lines.add("  setPieceRotation("+rend.boxName+","+rend.rotateAngleX+"f, "+rend.rotateAngleY+"f, "+rend.rotateAngleZ+"f);");
  //lines.add("  "+rend.boxName+".rotateAngleX="+rend.rotateAngleX+"f;");
  //lines.add("  "+rend.boxName+".rotateAngleY="+rend.rotateAngleY+"f;");
  //lines.add("  "+rend.boxName+".rotateAngleZ="+rend.rotateAngleZ+"f;");

  if(rend.cubeList!=null)
    {
    for(MEIMModelBox box : rend.cubeList)
      {
      lines.add("  "+rend.boxName+".addBox("+box.posX1+"f,"+box.posY1+"f,"+box.posZ1+"f,"+box.width+","+box.height+","+box.length+");");        
      }      
    }

  if(rend.childModels!=null)
    {
    for(MEIMModelRenderer renderer : rend.childModels)
      {
      recursiveDetails(lines, renderer, renderType);
      lines.add("  "+rend.boxName+".addChild("+renderer.boxName+");");
      }
    }
  }

private void recursiveName(ArrayList<String> lines, MEIMModelRenderer rend, String renderType)
  {
  for(MEIMModelRenderer render : rend.childModels)
    {
    if(render.boxName==null)
      {
      continue;
      }
    lines.add(renderType+" "+render.boxName+";");
    if(render.childModels!=null)
      {
      recursiveName(lines, render, renderType);
      }
    }
  }


@Override
public void handleFileSelection(File file)
  {
  if(!this.fileSelection.isEmpty())
    {
    this.exportModel();
    }
  }

}
