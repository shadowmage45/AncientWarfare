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
package shadowmage.ancient_warfare.common.config;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;

/**
 * client-side only game settings
 * @author Shadowmage
 *
 */
public class Settings
{

private static Configuration config;

public static Property overlayProp;
public static Property advOverlayProp;
public static Property mouseAimProp;
public static Property mouseRangeProp;
public static Property trajectoryIterationsProp;
public static Property npcAITicksProp;
public static Property renderVehiclesInFirstPersonProp;
public static Property renderVehicleNameplates;
public static Property renderNpcNameplates;

public static boolean renderVehiclesInFirstPerson = true;
private static boolean renderOverlay = true;
private static boolean renderAdvancedOverlay = true;

private static boolean enableMouseAim = true;
private static int mouseLookRange = 140;
private static int trajectoryIterationsClient = 20;

public static boolean useVehicleInventoryModels = true;

private Settings(){};
private static Settings INSTANCE;
public static Settings instance()
 {
 if(INSTANCE==null)
   {
   INSTANCE = new Settings();
   }
 return INSTANCE;
 }

public void loadSettings()
  {
  this.config = Config.getConfig();
  config.addCustomCategoryComment("client-settings", "These settings are effective client-side only, and may differ from client to client.  Most only effect local performance, and none may necessarily be used to 'cheat'.");
  this.overlayProp = config.get("client-settings", "render_overlay", true, "Render the basic stats overlay when riding a vehicle?");
  this.advOverlayProp = config.get("client-settings", "render_overlay_advanced", true, "Render the advanced vehicle overlay?");
  this.mouseAimProp = config.get("client-settings", "use_mouse_aim", true, "Use auto-aiming from mouse intput/cursor position?  May be toggled in-game to do manual aiming.");
  this.mouseRangeProp = config.get("client-settings", "mouse_aim_look_range", 140, "The distance to which a ray-trace will be performed when calculating auto-aim.  Lower settings may improve performance at the cost of being unable to auto-aim past that distance.");
  this.trajectoryIterationsProp = config.get("client-settings", "power_calculation_accuracy", 20, "How many iterations should be done for calculating power settings for mouse-aim.  Higher settings may cause lag on slower computers.  Lower settings WILL reduce accuracy of auto-aim.");
  this.renderVehiclesInFirstPersonProp = config.get("client-settings", "render_vehicles_first_person", true, "Should render the vehicle you are riding, while in first person view?");
  this.renderVehicleNameplates = config.get("client-settings", "vehicle_nameplats", true, "Should render vehicle nameplates?");
  this.renderNpcNameplates = config.get("client-settings", "npc_nameplates", true, "Should render npc nameplates?");  
  this.renderVehiclesInFirstPerson = renderVehiclesInFirstPersonProp.getBoolean(true);
  this.renderOverlay = overlayProp.getBoolean(true);
  this.renderAdvancedOverlay = advOverlayProp.getBoolean(true);
  this.enableMouseAim = mouseAimProp.getBoolean(true);
  this.useVehicleInventoryModels = config.get("client-settings", "vehicle_inventory_models", true, "Use vehicle models instead of icons for rendering of vehicle items?").getBoolean(true);
  }

public static boolean getMouseAim()
  {
  return enableMouseAim;
  }

public static boolean getRenderOverlay()
  {
  return renderOverlay;
  }

public static boolean getRenderAdvOverlay()
  {
  return renderAdvancedOverlay;
  }

public static boolean getRenderNpcNameplates()
  {
  return renderNpcNameplates.getBoolean(true);
  }

public static boolean getRenderVehicleNameplates()
  {
  return renderVehicleNameplates.getBoolean(true);
  }

public static void setRenderNpcNameplates(boolean val)
  {
  boolean current = renderNpcNameplates.getBoolean(true);
  if(val!=current)
    {
    renderNpcNameplates.value = String.valueOf(val);
    config.save();
    }
  }

public static void setRenderVehicleNameplates(boolean val)
  {
  boolean current = renderVehicleNameplates.getBoolean(true);
  if(val!=current)
    {
    renderVehicleNameplates.value = String.valueOf(val);
    config.save();
    }
  }

public static int getClientPowerIterations()
  {
  return trajectoryIterationsClient;
  }

public static void setMouseAim(boolean aim)
  {
  if(aim!=enableMouseAim)
    {
    enableMouseAim = aim;
    mouseAimProp.value = String.valueOf(aim);
    config.save();
    }  
  }

public static void setRenderOverlay(boolean rend)  
  {
  if(rend!=renderOverlay)
    {
    renderOverlay = rend;
    overlayProp.value = String.valueOf(rend);
    config.save();
    }
  }

public static void setRenderAdvOverlay(boolean rend)  
  {
  if(rend!=renderAdvancedOverlay)
    {
    renderAdvancedOverlay = rend;
    advOverlayProp.value = String.valueOf(rend);
    config.save();
    }
  }

public static void setRenderVehiclesInFirstPerson(boolean rend)
  {
  if(rend!=renderVehiclesInFirstPerson)
    {
    renderVehiclesInFirstPerson = rend;
    renderVehiclesInFirstPersonProp.value = String.valueOf(rend);
    config.save();
    }
  }




}
