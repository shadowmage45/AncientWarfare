package shadowmage.meim.client.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import shadowmage.ancient_framework.client.model.ModelBaseAW;
import shadowmage.ancient_framework.client.model.ModelPiece;
import shadowmage.ancient_framework.client.model.PrimitiveBox;
import shadowmage.ancient_framework.common.utils.StringTools;
import shadowmage.meim.client.meim_model.MEIMModelBase;
import shadowmage.meim.client.modelrenderer.MEIMModelBox;
import shadowmage.meim.client.modelrenderer.MEIMModelRenderer;
import shadowmage.meim.common.config.MEIMConfig;

public class ModelLoader
{

public ModelBaseAW loadModel(String fileName)
  {
  FileInputStream fis = null;
  try
    {
    fis = new FileInputStream(new File(fileName));
    } 
  catch (FileNotFoundException e)
    {      
    e.printStackTrace();
    return null;
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
  return this.parseModelLines(lines);
  }

private ModelBaseAW parseModelLines(List<String> lines)
  {
  ModelBaseAW model = new ModelBaseAW();
  HashMap<String, Integer> txMap = new HashMap<String, Integer>();
  HashMap<String, Integer> tyMap = new HashMap<String, Integer>();
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
      model.setTextureSize(tw, th);      
      ModelPiece piece = new ModelPiece(model, name, x, y, z, rx, ry, rz, model.getPiece(parent));
      model.addPiece(piece);
      txMap.put(name, tx);
      tyMap.put(name, ty);
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
      ModelPiece piece = model.getPiece(name);
      int tx = txMap.get(name);
      int ty = tyMap.get(name);
      if(piece==null){continue;}
      PrimitiveBox box = new PrimitiveBox(piece, x, y, z, x+bw, y+bh, z+bl, 0, 0, 0, tx, ty);
      piece.addPrimitive(box);
      }
    }
  return model;
  }

public void saveModel(ModelBaseAW model, String name)
  {
//  try
//    {
//    File saveFile = new File(name);
//    List<String> lines = getModelLines(model);
//
//    if(!saveFile.exists())
//      {
//      File newoutputfile = new File(saveFile.getParent());
//      newoutputfile.mkdirs();
//
//      saveFile.createNewFile();
//      }
//    FileWriter writer = new FileWriter(saveFile);
//    for(String line : lines)
//      {
//      writer.write(line+"\n");
//      }
//    writer.close();
//    }
//  catch (IOException e)
//    {
//    MEIMConfig.logDebug("error exporting model for name: "+name);
//    e.printStackTrace();
//    }

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
