/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public Licence.
   Please see COPYING for precise license information.

   This file is part of Ancient Warfare.

   Ancient Warfare is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   Ancient Warfare is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with Ancient Warfare.  If not, see <http://www.gnu.org/licenses/>.
 */
package shadowmage.ancient_warfare.common.structures.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import shadowmage.ancient_warfare.common.AWStructureModule;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.structures.data.rules.BlockRule;
import shadowmage.ancient_warfare.common.structures.data.rules.SwapRule;
import shadowmage.ancient_warfare.common.structures.data.rules.VehicleRule;
import shadowmage.ancient_warfare.common.utils.IDPairCount;
import shadowmage.ancient_warfare.common.utils.StringTools;

import com.google.common.io.ByteStreams;

public class StructureLoader
{

private StructureLoader()
  {
  }

private static StructureLoader INSTANCE;
public static StructureLoader instance()
  {
  if(INSTANCE==null)
    {
    INSTANCE = new StructureLoader();
    }
  return INSTANCE;
  }
/**
 * called probableStructureFiles because they haven't been opened/read/checked for validity
 */
private List<File> probableStructureFiles = new ArrayList<File>();
private List<File> probableRuinsFiles = new ArrayList<File>();

/**
 * used to keep track of layer number during parsing
 */
int currentLayer = 0;

/**
 * scans directores from AWStructureModule for probable structure template files and loads them into
 * a temporary file list for parsing...
 */
public void scanForPrebuiltFiles()
  {
  probableStructureFiles.clear();  
  this.recursiveScan(new File(AWStructureModule.includeDirectory), probableStructureFiles, "."+Config.TEMPLATE_EXTENSION);  
  this.recursiveScan(new File(AWStructureModule.convertDirectory), probableRuinsFiles, ".tml");
  }

private void recursiveScan(File directory, List<File> fileList, String extension)
  {
  if(directory==null)
    {
    Config.logError("Could not locate "+directory+" directory to load structures!");
    return;
    }
  File[] allFiles = directory.listFiles();
  if(allFiles==null)
    {
    Config.logError("Could not locate "+directory+" directory to load structures!--no files in directory file list!");
    return;
    }
  File currentFile;
  for(int i = 0; i < allFiles.length; i++)
    {
    currentFile = allFiles[i];
    if(currentFile.isDirectory())
      {
      recursiveScan(currentFile, fileList, extension);
      }
    else if(isProbableFile(currentFile, extension))
      {
      fileList.add(currentFile);
      }
    }
  }

private boolean isProbableFile(File file, String extension)
  {
  return file.getName().toLowerCase().endsWith(extension);
  }

private List<ProcessedStructure> processFilesFor(List<File> fileList)
  {
  List<ProcessedStructure> structures = new ArrayList<ProcessedStructure>();
  ProcessedStructure struct;
  for(File file : fileList)
    {
    struct = this.processFile(file);
    if(struct!=null)
      {
      structures.add(struct);
      }
    }  
  return structures;  
  }

public String getMD5(byte[] bytes)
  {  
  byte[] md5Bytes;
  MessageDigest md;
  try
    {
    md = MessageDigest.getInstance("MD5");
    } 
  catch (NoSuchAlgorithmException e)
    {
    e.printStackTrace();
    return "";
    }    
  md5Bytes = md.digest(bytes);
  BigInteger md5Num = new BigInteger(1, md5Bytes);
  String md5String = md5Num.toString(16);
  if(md5String.length()<32)
    {
    while(md5String.length()<32)
      {
      md5String = "0"+md5String;
      }
    }
  return md5String;
  }

private ProcessedStructure processFile(File file)
  {
  Scanner reader = null;
  FileInputStream fis = null;
  ByteArrayInputStream bais;
  byte[] fileBytes;
  
  String md5String = "";
  try
    {
    fis = new FileInputStream(file);
    fileBytes = ByteStreams.toByteArray(fis);
    fis.close();        
    md5String = getMD5(fileBytes);    
    bais = new ByteArrayInputStream(fileBytes);
    reader = new Scanner(bais);
    } 
  catch (FileNotFoundException e)
    {    
    Config.logError("There was an error while parsing template file: "+file.getName()); 
    e.printStackTrace();    
    return null;
    } 
  catch (IOException e)
    {
    Config.logError("There was an error while parsing template file: "+file.getName()); 
    e.printStackTrace();    
    return null;
    }   
  List<String> lines = new ArrayList<String>();
  String line;
  /**
   * throw everything into a list, close the file
   */
  while(reader.hasNextLine())
    {    
    lines.add(reader.nextLine());
    }  
  reader.close();

  ProcessedStructure struct = null;
  /**
   * process from a nice in-memory list
   */
  try
    {
    if(file.getName().endsWith("."+Config.TEMPLATE_EXTENSION))
      {      
      struct = this.loadStructureAW(lines, md5String);
      }
    else
      {
      struct = this.loadStructureRuins(lines);
      }
    }
  catch(Exception e)
    {        
    Config.logError("PARSING EXCEPTION: There was an error while parsing template file: "+file.getName());
    e.printStackTrace();
    return null;
    }
  if(struct !=null && !struct.isValid)
    {
    Config.logError("INVALID STRUCTURE: There was an error while parsing template file: "+file.getName()); 
    return null;
    }  
  struct.filePath = file.getAbsolutePath();

  String name = file.getName();
  if(name.endsWith("."+Config.TEMPLATE_EXTENSION) || name.endsWith(".tml"))
    {
    name = name.substring(0, name.length()-4);
    }
  struct.name = name; 
  //
  return struct;
  }

public List<ProcessedStructure> processStructureFiles()
  {
  return processFilesFor(probableStructureFiles);
  }

public void convertRuinsTemplates()
  {
  for(File file : this.probableRuinsFiles)
    {
    String name = file.getName();
    name = name.split(".tml")[0];
    String newFile = String.valueOf(AWStructureModule.outputDirectory+name+"."+Config.TEMPLATE_EXTENSION);

    ProcessedStructure raw = processFile(file);
    if(raw==null || !raw.isValid)
      {
      continue;
      }    
    raw.name = String.valueOf(name);
    if(!StructureExporter.writeStructureToFile(raw, newFile, false))
      {
      continue;
      }       
    String renamedName = file.getName()+".cvt";
    File renamedFile = new File(AWStructureModule.convertDirectory+renamedName);
    file.renameTo(renamedFile);
    }
  }

public ProcessedStructure loadStructureAW(List<String> lines, String md5)
  {
  this.currentLayer = 0;
  ProcessedStructure struct = new ProcessedStructure();
  if(lines==null)
    {
    struct.isValid = false;
    return null;
    }  
  Iterator<String> it = lines.iterator();
  String line;
  while(it.hasNext())
    {
    line = it.next();    
    if(line.toLowerCase().startsWith("survival"))
      {
      struct.survival = StringTools.safeParseBoolean("=", line);
      } 
    else if(line.toLowerCase().startsWith("underground"))
      {
      struct.underground = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("undergroundminlevel"))
      {
      struct.undergroundMinLevel = StringTools.safeParseInt("=", line);//Integer.parseInt(line.split("=")[1]);
      }
    else if(line.toLowerCase().startsWith("undergroundmaxlevel"))
      {
      struct.undergroundMaxLevel = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("undergroundmaxairabove"))
      {
      struct.undergroundMaxAirAbove = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("minsubmergeddepth"))
      {
      struct.minSubmergedDepth = StringTools.safeParseInt("=", line);
      }    
    else if(line.toLowerCase().startsWith("maxwaterdepth"))
      {
      struct.maxWaterDepth = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("maxlavadepth"))
      {
      struct.maxLavaDepth = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("xsize"))
      {
      struct.xSize = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("ysize"))
      {
      struct.ySize = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("zsize"))
      {
      struct.zSize = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("validtargetblocks"))
      {      
      struct.validTargetBlocks = StringTools.safeParseIntArray("=",line);
      }
    else if(line.toLowerCase().startsWith("verticaloffset"))
      {
      struct.verticalOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("xoffset"))
      {
      struct.xOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("zoffset"))
      {
      struct.zOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("maxoverhang"))
      {
      struct.maxOverhang = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("maxleveling"))
      {
      struct.maxLeveling = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("levelingbuffer"))
      {
      struct.levelingBuffer = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("maxverticalclear"))
      {
      struct.maxVerticalClear = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("clearingbuffer"))
      {
      struct.clearingBuffer = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("preservewater"))// 
      {
      struct.preserveWater = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preservelava"))// 
      {
      struct.preserveLava = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preserveplants"))// 
      {
      struct.preservePlants = StringTools.safeParseBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preserveblocks"))// 
      {
      struct.preserveBlocks = StringTools.safeParseBoolean("=", line);
      }    
    else if(line.toLowerCase().startsWith("biomesonlyin"))
      {
      struct.biomesOnlyIn = StringTools.safeParseStringArray("=", line);
      }
    else if(line.toLowerCase().startsWith("biomesnotin"))
      {
      struct.biomesNotIn = StringTools.safeParseStringArray("=", line);
      }



    /**
     * parse out block rules
     */
    else if(line.toLowerCase().startsWith("rule:"))
      {      
      this.parseRule(struct, it);
      }

    /**
     * parse out vehicle setups
     */
    else if(line.toLowerCase().startsWith("vehicle:"))
      {      
      this.parseVehicle(struct, it);
      }

    /**
     * parse out npc setups
     */
    else if(line.toLowerCase().startsWith("npc:"))
      {      
      this.parseNPC(struct, it);
      }

    else if(line.toLowerCase().startsWith("resources:"))
      {
      this.parseResources(struct, it);
      }
    
    else if(line.toLowerCase().startsWith("swap:"))
      {
      this.parseSwap(struct, it);
      }
    /**
     * parse out layers
     */
    else if(line.toLowerCase().startsWith("layer:"))
      {
      this.parseLayer(struct, it);
      }

    if(!struct.isValid)
      {
      return null;
      }
    }  
  if(!struct.isValid)
    {
    return null;
    }
  struct.blockRules.put(0, new BlockRule(0,0,0));
  struct.md5 = md5;
  struct.setTemplateLines(lines);
  return struct;
  }


public ProcessedStructure loadStructureRuins(List<String> lines)
  {
  this.currentLayer = 0;
  ProcessedStructure struct = new ProcessedStructure();
  if(lines==null)
    {
    struct.isValid = false;
    return null;
    }  
  struct.blockRules.put(0, new BlockRule(0,0,0));
  Iterator<String> it = lines.iterator();
  String line;
  while(it.hasNext() && (line = it.next())!=null)
    {
    if(line.toLowerCase().startsWith("acceptable_target_blocks"))
      {
      struct.validTargetBlocks = StringTools.safeParseIntArray("=", line);
      }
    else if(line.toLowerCase().startsWith("dimensions"))
      {
      int[] dim = StringTools.safeParseIntArray("=", line);
      if(dim.length != 3 )
        {
        struct.isValid = false;
        Config.logError("Error encountered while parsing Ruins .tml file, improper dimensions specified");
        return null;
        }
      struct.ySize = dim[0];
      struct.zSize = dim[1];
      struct.xSize = dim[2];
      struct.xOffset = struct.xSize/2;
      struct.zOffset = 1;
      }
    else if(line.toLowerCase().startsWith("embed_into_distance"))
      {
      struct.verticalOffset = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("allowable_overhang"))
      {
      struct.maxOverhang = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("max_cut_in"))
      {
      struct.maxVerticalClear = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("max_leveling"))
      {
      struct.maxLeveling = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("leveling_buffer"))
      {
      struct.levelingBuffer = StringTools.safeParseInt("=", line);
      }
    else if(line.toLowerCase().startsWith("preserve_water"))
      {
      struct.preserveWater = StringTools.safeParseIntAsBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preserve_pants"))
      {
      struct.preserveLava = StringTools.safeParseIntAsBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("preserve_lava"))
      {
      struct.preservePlants = StringTools.safeParseIntAsBoolean("=", line);
      }
    else if(line.toLowerCase().startsWith("rule"))
      {
      BlockRule rule = BlockRule.parseRuinsRule(line, struct.blockRules.size());
      if(rule!=null)
        {
        struct.blockRules.put((int) rule.ruleNumber, rule);
        }
      }
    else if(line.toLowerCase().startsWith("layer"))
      {
      this.parseLayer(struct, it);
      }    
    }  

  if(!struct.isValid)
    {
    return null;
    }
  struct.setTemplateLines(lines);
  return struct;
  }


private void parseResources(ProcessedStructure struct, Iterator<String> it)
  {
  String line;
  String[] sp1;
  String[] sp2;
  List<IDPairCount> resourceList = new ArrayList<IDPairCount>();
  while(it.hasNext())
    {
    line = it.next();
    if(line.startsWith("resources:"))
      {
      continue;
      }    
    if(line.startsWith(":endresources"))
      {
      break;
      }
    sp1 = line.split(",");
    if(sp1.length<2)
      {
      break;
      }
    line = sp1[0];
    sp2 = line.split("-");
    int id = 0;
    int meta = 0;
    int qty = 0;
    if(sp2.length>1)
      {
      meta = StringTools.safeParseInt(sp2[1]);
      }
    if(StringTools.isNumber(sp2[0]))
      {
      id = StringTools.safeParseInt(sp2[0]);
      }
    else
      {
      //TODO parse item/block names...
      }
    line = sp1[1];
    if(StringTools.isNumber(line))
      {
      qty = StringTools.safeParseInt(line);
      }
    if(qty>0 && id>0)
      {
      resourceList.add(new IDPairCount(id, meta, qty));
      }    
    }
  if(resourceList.size()>0)
    {
    struct.cachedCounts = resourceList;
    }  
  }

private void parseLayer(ProcessedStructure struct, Iterator<String> it)
  {
  if(!it.hasNext())
    {
    return;
    }  
  if(struct.structure==null)
    {
    if(struct.xSize ==0 || struct.ySize==0 || struct.zSize==0)
      {
      Config.logError("Invalid structure size while attempting to create layers, one or more axis are size 0!");
      struct.isValid = false;
      return;
      }
    else
      {
      struct.structure = new short[struct.xSize][struct.ySize][struct.zSize];
      }
    }
  String line;
  String [] vals;
  int currentRow = 0;

  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("layer:") || line.toLowerCase().startsWith("layer"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith(":endlayer") || line.toLowerCase().startsWith("endlayer"))
      {
      break;      
      }
    else
      {
      vals = line.split(",");
      for(int i = 0; i < vals.length; i++)
        {
        if(i>=struct.structure.length ||currentLayer>=struct.structure[i].length || currentRow>= struct.structure[i][currentLayer].length)
          {
          Config.logError("Error parsing layer in structure, an array index was out of bounds! (Layer larger than dimensions!)");
          struct.isValid = false;
          break;
          }
        struct.structure[i][this.currentLayer][currentRow] = Short.parseShort(vals[i]);
        }      
      currentRow++;
      }    
    }
  this.currentLayer++;
  }

/**
 * parses out a single blockRule from the iterator of lines passed in
 * @param it
 */
private void parseRule(ProcessedStructure struct, Iterator<String> it)
  {
  if(!it.hasNext())
    {
    struct.isValid = false;
    return;
    }
  ArrayList<String> ruleLines = new ArrayList<String>();  
  String line;  
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("rule:"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith(":endrule"))
      {
      break;      
      }
    else
      {
      ruleLines.add(line);      
      }    
    }     
  BlockRule rule = BlockRule.parseRule(ruleLines);
  if(rule!=null)
    {    
    struct.blockRules.put(Integer.valueOf((int) rule.ruleNumber), rule);    
    }
  else
    {
    Config.logError("Error parsing block rule for structure!");
    struct.isValid = false;
    }
  }

/**
 * parse out a single vehicleRule/setup type from the iterator of lines
 * @param it
 */
private void parseVehicle(ProcessedStructure struct, Iterator<String> it)
  {
  if(!it.hasNext())
    {
    struct.isValid = false;
    return;
    }
  ArrayList<String> ruleLines = new ArrayList<String>();  
  String line;  
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("rule:"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith(":endrule"))
      {
      break;      
      }
    else
      {
      ruleLines.add(line);      
      }    
    }     
  VehicleRule rule = VehicleRule.parseRule(ruleLines);
  if(rule!=null)
    {    
    struct.vehicleRules.put(Integer.valueOf((int) rule.ruleNumber), rule);     
    }
  else
    {
    Config.logError("Error parsing vehicle rule for structure!");
    struct.isValid = false;
    }
  }

/**
 * parse out a single npcRule/setup type from the iterator of lines
 * @param it
 */
private void parseNPC(ProcessedStructure struct, Iterator<String> it)
  {

  }


private void parseSwap(ProcessedStructure struct, Iterator<String> it)
  {
  if(!it.hasNext())
    {
    struct.isValid = false;
    return;
    }
  ArrayList<String> ruleLines = new ArrayList<String>();  
  String line;  
  while(it.hasNext())
    {
    line = it.next();
    if(line.toLowerCase().startsWith("swap:"))
      {
      continue;
      }
    else if(line.toLowerCase().startsWith(":endswap"))
      {
      break;      
      }
    else
      {
      ruleLines.add(line);      
      }    
    }     
  SwapRule rule = SwapRule.parseRule(ruleLines);
  if(rule!=null)
    {    
    struct.swapRules.put(Integer.valueOf((int) rule.ruleNumber), rule);  
    }
  else
    {
    Config.logError("Error parsing vehicle rule for structure!");
    struct.isValid = false;
    }
  }
}
