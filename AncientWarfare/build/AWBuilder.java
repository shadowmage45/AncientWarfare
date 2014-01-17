/**
   Copyright 2012-2013 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class AWBuilder
{

public static void main(String[] args)
  {
  if(args.length<=0){return;}
  String forgeBasePath = args[0];
  String mcpPath =forgeBasePath + "/mcp";
  String destSourcePath = mcpPath+"/src/minecraft";
  
  File file = new File("");
  String repositoryPath = new File("").getAbsolutePath().replace('\\', '/')+"/src";
  System.out.println("origin path: "+repositoryPath);
  System.out.println("dest path: "+destSourcePath);
  
  
  String versionNumber = getVersionNumber(repositoryPath);
  copyFiles(repositoryPath, destSourcePath);
  replaceVersionNumber(destSourcePath+"/mcmod.info", versionNumber);
  replaceVersionNumber(destSourcePath+"/shadowmage/ancient_warfare/common/config/Config.java", versionNumber);
  buildFiles(mcpPath);
  }

private static void copyFiles(String sourcePath, String destinationPath)
  {
  sourcePath = sourcePath.replace('/', '\\');
  destinationPath = destinationPath.replace('/', '\\');
  String copyCommand = "xcopy \""+sourcePath+ "\" \"" + destinationPath + "\" /y/e/q";
  System.out.println(copyCommand);
  try
    {
    Process process = Runtime.getRuntime().exec(copyCommand);
    process.waitFor();
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }
  catch (InterruptedException e)
    {     
    e.printStackTrace();
    }
  }

private static String getVersionNumber(String repoPath)
  {
  Properties prop = new Properties();
  try
    {
    prop.load(new FileInputStream(new File(repoPath+"/version.properties")));
    return prop.getProperty("version");
    } 
  catch (FileNotFoundException e)
    {
    e.printStackTrace();
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }
  return "INVALID-VERSION";
  }

private static void replaceVersionNumber(String inputFile, String versionNumber)
  {
  System.out.println("received version# "+versionNumber);
  System.out.println("file to replace for/from: "+inputFile);
  String test = "@VERSION@";
  try
    {
    List<String> lines = Files.readLines(new File(inputFile), Charsets.UTF_8);
    File outputFile = new File(inputFile);
    FileWriter writer = new FileWriter(outputFile);
    for(String line : lines)
      {
      if(line.contains(test))
        {
        line = line.replace(test, versionNumber);
        }
      writer.write(line+"\n");
      }
    writer.close();
    } 
  catch (IOException e1)
    {
    e1.printStackTrace();
    }
  }

private static void buildFiles(String mcpPath)
  {
  /**
   * TODO
   * build a batch file to call
   * insert the relevant cd commands/etc
   * should allow for the call-path to be correct for calling the mcp-recompile.bat
   */
  mcpPath = mcpPath.replace('/', '\\');
  Process p = null;
  try
    {
    
    String recompileCommand = "cmd /c "+mcpPath+"\\runtime\\bin\\python\\python_mcp "+mcpPath+"\\runtime\\recompile.py %*";
    System.out.println("calling recompile : "+recompileCommand);
    p = Runtime.getRuntime().exec(recompileCommand);
    p.waitFor();
    
    System.out.println("calling reobfuscate");
//    p = Runtime.getRuntime().exec(mcpPath+"\\reobfuscate_srg.bat");
//    p.waitFor();  
    } 
  catch (InterruptedException e)
    {
    e.printStackTrace();
    } 
  catch (IOException e)
    {
    e.printStackTrace();
    }   
  }

private static void packageBuiltFiles(){}

private static void cleanupFiles(){}

}
