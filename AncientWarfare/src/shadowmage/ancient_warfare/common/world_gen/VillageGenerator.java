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
package shadowmage.ancient_warfare.common.world_gen;

import java.util.Random;
import java.util.WeakHashMap;

import net.minecraft.world.gen.structure.ComponentVillage;
import net.minecraft.world.gen.structure.ComponentVillageStartPiece;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import shadowmage.ancient_warfare.common.config.Config;
import shadowmage.ancient_warfare.common.manager.StructureManager;
import shadowmage.ancient_warfare.common.structures.data.ProcessedStructure;
import shadowmage.ancient_warfare.common.world_gen.village.AWVCAdvancedLibrary;
import shadowmage.ancient_warfare.common.world_gen.village.AWVCFortress;
import shadowmage.ancient_warfare.common.world_gen.village.AWVCTower;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class VillageGenerator
{

/**
 * gen flow:
 * 
 *  START
 *  register IVillageCreationHandler in village registry
 *  when village is generated VillageRegistry.addExtraVillageComponents is called to add mod components
 *    this adds a StructurePieceWeight to the weighted pieces list, as determined by the IVillageCreationHandler getVillagePieceWeight call
 *    pieces with num to generate <=0 are removed here
 *    returns a list of weighted pieces
 *  list of pieces is passed to ComponentVillageStartPiece to setup list of pieces to attempt to generate
 *    actual generation is called in StructureVillagePieces.func_75083_a
 *      this eventually links back to the IVillageCreationHandler buildComponent() call
 *        this is really the first call, and should more aptly be called attemptBuildComponent    
 *          return null for a 'cannot build here' flag, else return the BUILT component.
 *      if multiple copies of a structure are to be built, return a separate new instance per..instance...
 *      last two params are ORIENTATION, COMPONENT_TYPE_NUMBER
 * 
 * 
 */


/**
 * essentially
 * create a new village gen handler for EACH structure
 *    gen handler can be just an instance of a class -- needs nothing special
 * create a new village component class for EACH COMPONENT
 * 
 * register that gen handler for that component
 * 
 */

public static VillageGenHook fortress;
public static VillageGenHook library;
public static VillageGenHook tower;
public static Random teamRandom = new Random();
public WeakHashMap<ComponentVillageStartPiece, Integer> villageMap = new WeakHashMap<ComponentVillageStartPiece, Integer>();

private static VillageGenerator INSTANCE = new VillageGenerator();
public static VillageGenerator instance(){return INSTANCE;}
private VillageGenerator(){};

public static void load()
  {
  ProcessedStructure struct = StructureManager.instance().getStructureServer("mineArea");
  fortress = new VillageGenHook(AWVCFortress.class, 1, 20, struct);  
  VillagerRegistry.instance().registerVillageCreationHandler(fortress);
  
  struct = StructureManager.instance().getStructureServer("advancedVillageLibrary");
  library = new VillageGenHook(AWVCAdvancedLibrary.class, 1, 20, struct);
  VillagerRegistry.instance().registerVillageCreationHandler(library);
  
  struct = StructureManager.instance().getStructureServer("towerTest1");
  tower = new VillageGenHook(AWVCTower.class, 1, 20, struct);
  VillagerRegistry.instance().registerVillageCreationHandler(tower);
  }

public static VillageGenComponent constructComponent(Class<? extends ComponentVillage> clz, ComponentVillageStartPiece start, int type, int face, ProcessedStructure struct, int x, int y, int z, StructureBoundingBox box)
  {
  if(!instance().villageMap.containsKey(start))
    {
    /**
     * TODO assign each village as hostile, neutral
     */
    int team = teamRandom.nextInt(2);
    Config.logDebug("assigining team random of: "+team);
    instance().villageMap.put(start, team);
    
    }
  VillageGenComponent part = null;
  Config.logDebug("should construct component for structure: "+struct.name);
  if(clz == AWVCFortress.class)
    {
    part  = new AWVCFortress(start, type, face, struct, box);
    } 
  else if(clz== AWVCAdvancedLibrary.class)
    {
    part = new AWVCAdvancedLibrary(start, type, face, struct, box);
    }
  else if(clz == AWVCTower.class)
    {
    part = new AWVCTower(start, type, face, struct, box);
    }
  return part;
  }

}
