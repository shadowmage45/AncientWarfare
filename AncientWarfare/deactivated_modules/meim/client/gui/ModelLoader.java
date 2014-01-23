package shadowmage.meim.client.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.meim.client.meim_model.MEIMModelBase;
import shadowmage.meim.client.modelrenderer.MEIMModelBox;
import shadowmage.meim.client.modelrenderer.MEIMModelRenderer;
import shadowmage.meim.common.config.MEIMConfig;

public class ModelLoader
{

public MEIMModelBase loadModel(String fileName)
  {
  MEIMModelBase model = new MEIMModelBase();
  FileInputStream fis = null;
  try
    {
    fis = new FileInputStream(new File(fileName));
    } 
  catch (FileNotFoundException e)
    {      
    e.printStackTrace();
    return model;
    }
  Scanner scan = new Scanner(fis);
  ArrayList<String> lines = new ArrayList<String>();
  while(scan.hasNext())
    {
    lines.add(scan.next());
    }
  scan.close();
  try
    {
    fis.close();
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }

  this.parseModelLines(lines, model);
  return model;
  }

private void parseModelLines(List<String> lines, MEIMModelBase model)
  {
  Iterator<String> it = lines.iterator();
  String line;
  String split[];
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("part="))
      {
      split = StringTools.safeParseStringArray("=", line);
      String name = split[0];
      String parent = split[1];
      float x = StringTools.safeParseFloat(split[2]);
      float y = StringTools.safeParseFloat(split[3]);
      float z = StringTools.safeParseFloat(split[4]);
      float rx = StringTools.safeParseFloat(split[5]);
      float ry = StringTools.safeParseFloat(split[6]);
      float rz = StringTools.safeParseFloat(split[7]);
      int tx = StringTools.safeParseInt(split[8]);
      int ty = StringTools.safeParseInt(split[9]);
      int tw = (int)StringTools.safeParseFloat(split[10]);
      int th = (int)StringTools.safeParseFloat(split[11]);
      MEIMModelRenderer parentModel;
      if(!parent.toLowerCase().equals("null"));
        {
        parentModel = model.getRenderForName(parent);
        }      
        MEIMModelRenderer rend = new MEIMModelRenderer(model, name, parentModel);
        rend.setRotationPoint(x, y, z);
        rend.rotateAngleX = rx;
        rend.rotateAngleY = ry;
        rend.rotateAngleZ = rz;
        rend.setTextureSize(tw, th);
        rend.setTextureOffset(tx, ty);
        if(parentModel==null)
          {
          model.baseParts.add(rend);
          }
        else// if(parentModel != null);
          {
          parentModel.addChild(rend);
          }
      }    
    else if(line.toLowerCase().startsWith("box="))
      {
      split = StringTools.safeParseStringArray("=", line);
      String name = split[0];      
      float x = StringTools.safeParseFloat(split[1]);
      float y = StringTools.safeParseFloat(split[2]);
      float z = StringTools.safeParseFloat(split[3]);      
      int bw = StringTools.safeParseInt(split[4]);
      int bh = StringTools.safeParseInt(split[5]);
      int bl = StringTools.safeParseInt(split[6]);      
      MEIMModelRenderer rend = model.getRenderForName(name);
      if(rend!=null)
        {
        rend.addBox(x, y, z, bw, bh, bl);
        }      
      }
    }
  }

public void saveModel(MEIMModelBase model, String name)
  {
  try
    {
    File saveFile = new File(name);
    List<String> lines = getModelLines(model);

    if(!saveFile.exists())
      {
      File newoutputfile = new File(saveFile.getParent());
      newoutputfile.mkdirs();

      saveFile.createNewFile();
      }
    FileWriter writer = new FileWriter(saveFile);
    for(String line : lines)
      {
      writer.write(line+"\n");
      }
    writer.close();
    }
  catch (IOException e)
    {
    MEIMConfig.logDebug("error exporting model for name: "+name);
    e.printStackTrace();
    }

  }

private List<String> getModelLines(MEIMModelBase model)
{
List<String> lines = new ArrayList<String>();

for(MEIMModelRenderer rend : model.baseParts)
  {
  recurseModel(rend, lines);
  }

return lines;
}

private void recurseModel(MEIMModelRenderer rend, List<String> lines)
  {  
  String name = rend.boxName;
  String x = String.valueOf(rend.rotationPointX);
  String y = String.valueOf(rend.rotationPointY);
  String z = String.valueOf(rend.rotationPointZ);
  String rx = String.valueOf(rend.rotateAngleX);
  String ry = String.valueOf(rend.rotateAngleY);
  String rz = String.valueOf(rend.rotateAngleZ);
  String tx = String.valueOf(rend.textureOffsetX);
  String ty = String.valueOf(rend.textureOffsetY);
  String tw = String.valueOf(rend.textureWidth);
  String th = String.valueOf(rend.textureHeight);  
  String parent = rend.parent==null? "null" : rend.parent.boxName;  
  lines.add("part="+name+","+parent+","+x+","+y+","+z+","+rx+","+ry+","+rz+","+tx+","+ty+","+tw+","+th);  
  for(MEIMModelBox box : rend.cubeList)
    {
    x = String.valueOf(box.posX1);
    y = String.valueOf(box.posY1);
    z = String.valueOf(box.posZ1);
    rx = String.valueOf(box.width);
    ry = String.valueOf(box.height);
    rz = String.valueOf(box.length);
    lines.add("box="+name+","+x+","+y+","+z+","+rx+","+ry+","+rz);
    }
  if(rend.childModels!=null)
    {
    for(MEIMModelRenderer render : rend.childModels)
      {
      recurseModel(render, lines);
      }
    }

  }
}
