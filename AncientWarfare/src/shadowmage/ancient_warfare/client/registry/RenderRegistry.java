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
package shadowmage.ancient_warfare.client.registry;

import java.util.HashMap;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraftforge.client.MinecraftForgeClient;
import shadowmage.ancient_warfare.client.model.ModelBallistaMobile;
import shadowmage.ancient_warfare.client.model.ModelBallistaStand;
import shadowmage.ancient_warfare.client.model.ModelBatteringRam;
import shadowmage.ancient_warfare.client.model.ModelCannonMobileFixed;
import shadowmage.ancient_warfare.client.model.ModelCannonStandFixed;
import shadowmage.ancient_warfare.client.model.ModelCannonStandTurret;
import shadowmage.ancient_warfare.client.model.ModelCatapultMobileFixed;
import shadowmage.ancient_warfare.client.model.ModelCatapultMobileTurret;
import shadowmage.ancient_warfare.client.model.ModelCatapultStandFixed;
import shadowmage.ancient_warfare.client.model.ModelCatapultStandTurret;
import shadowmage.ancient_warfare.client.model.ModelChestCart;
import shadowmage.ancient_warfare.client.model.ModelHwacha;
import shadowmage.ancient_warfare.client.model.ModelTrebuchetMobileFixed;
import shadowmage.ancient_warfare.client.model.ModelTrebuchetStandFixed;
import shadowmage.ancient_warfare.client.model.ModelTrebuchetStandTurret;
import shadowmage.ancient_warfare.client.model.ModelVehicleBase;
import shadowmage.ancient_warfare.client.render.RenderMissileHelper;
import shadowmage.ancient_warfare.client.render.RenderVehicleBase;
import shadowmage.ancient_warfare.client.render.RenderVehicleHelper;
import shadowmage.ancient_warfare.client.render.missile.RenderArrow;
import shadowmage.ancient_warfare.client.render.missile.RenderShot;
import shadowmage.ancient_warfare.client.render.vehicle.RenderBallistaMobile;
import shadowmage.ancient_warfare.client.render.vehicle.RenderBallistaStand;
import shadowmage.ancient_warfare.client.render.vehicle.RenderBatteringRam;
import shadowmage.ancient_warfare.client.render.vehicle.RenderCannonMobileFixed;
import shadowmage.ancient_warfare.client.render.vehicle.RenderCannonStandFixed;
import shadowmage.ancient_warfare.client.render.vehicle.RenderCannonStandTurret;
import shadowmage.ancient_warfare.client.render.vehicle.RenderCatapultMobileFixed;
import shadowmage.ancient_warfare.client.render.vehicle.RenderCatapultMobileTurret;
import shadowmage.ancient_warfare.client.render.vehicle.RenderCatapultStandFixed;
import shadowmage.ancient_warfare.client.render.vehicle.RenderCatapultStandTurret;
import shadowmage.ancient_warfare.client.render.vehicle.RenderChestCart;
import shadowmage.ancient_warfare.client.render.vehicle.RenderHwacha;
import shadowmage.ancient_warfare.client.render.vehicle.RenderTrebuchetLarge;
import shadowmage.ancient_warfare.client.render.vehicle.RenderTrebuchetMobileFixed;
import shadowmage.ancient_warfare.client.render.vehicle.RenderTrebuchetStandFixed;
import shadowmage.ancient_warfare.client.render.vehicle.RenderTrebuchetStandTurret;
import shadowmage.ancient_warfare.common.item.ItemLoader;
import shadowmage.ancient_warfare.common.missiles.Ammo;
import shadowmage.ancient_warfare.common.missiles.MissileBase;
import shadowmage.ancient_warfare.common.registry.VehicleRegistry;
import shadowmage.ancient_warfare.common.soldiers.NpcBase;
import shadowmage.ancient_warfare.common.vehicles.IVehicleType;
import shadowmage.ancient_warfare.common.vehicles.VehicleBase;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * handle render information
 * @author Shadowmage
 *
 */
public class RenderRegistry
{

private RenderRegistry(){}
private static RenderRegistry INSTANCE;
public static RenderRegistry instance()
  {
  if(INSTANCE==null){INSTANCE = new RenderRegistry();}
  return INSTANCE;
  }

/**
 * dummy render to be used in case a vehicle render doesn't exist...
 */
private RenderCatapultStandFixed dummyRender = new RenderCatapultStandFixed();

private HashMap<Integer, Render> missileRenders = new HashMap<Integer, Render>();
private HashMap<Integer, RenderVehicleBase> vehicleRenders = new HashMap<Integer, RenderVehicleBase>();
private HashMap<Integer, ModelVehicleBase> vehicleModels = new HashMap<Integer, ModelVehicleBase>();

public void loadRenders()
  {  
  /**
   * vehicles..
   */  
  RenderingRegistry.registerEntityRenderingHandler(VehicleBase.class, RenderVehicleHelper.instance());
  this.addVehicleRender(VehicleRegistry.CATAPULT_STAND_FIXED, new RenderCatapultStandFixed(), new ModelCatapultStandFixed());
  this.addVehicleRender(VehicleRegistry.CATAPULT_STAND_TURRET, new RenderCatapultStandTurret(), new ModelCatapultStandTurret());
  this.addVehicleRender(VehicleRegistry.CATAPULT_MOBILE_FIXED, new RenderCatapultMobileFixed(), new ModelCatapultMobileFixed());
  this.addVehicleRender(VehicleRegistry.CATAPULT_MOBILE_TURRET, new RenderCatapultMobileTurret(), new ModelCatapultMobileTurret());
  this.addVehicleRender(VehicleRegistry.BALLISTA_STAND_FIXED, new RenderBallistaStand(), new ModelBallistaStand());
  this.addVehicleRender(VehicleRegistry.BALLISTA_STAND_TURRET, new RenderBallistaStand(), new ModelBallistaStand());
  this.addVehicleRender(VehicleRegistry.BALLISTA_MOBILE_FIXED, new RenderBallistaMobile(), new ModelBallistaMobile());
  this.addVehicleRender(VehicleRegistry.BALLISTA_MOBILE_TURRET, new RenderBallistaMobile(), new ModelBallistaMobile());
  this.addVehicleRender(VehicleRegistry.BATTERING_RAM, new RenderBatteringRam(), new ModelBatteringRam());
  this.addVehicleRender(VehicleRegistry.CANNON_STAND_FIXED, new RenderCannonStandFixed(), new ModelCannonStandFixed());
  this.addVehicleRender(VehicleRegistry.CANNON_STAND_TURRET, new RenderCannonStandTurret(), new ModelCannonStandTurret());
  this.addVehicleRender(VehicleRegistry.CANNON_MOBILE_FIXED, new RenderCannonMobileFixed(), new ModelCannonMobileFixed());  
  this.addVehicleRender(VehicleRegistry.HWACHA, new RenderHwacha(), new ModelHwacha());
  this.addVehicleRender(VehicleRegistry.TREBUCHET_STAND_FIXED, new RenderTrebuchetStandFixed(), new ModelTrebuchetStandFixed());
  this.addVehicleRender(VehicleRegistry.TREBUCHET_STAND_TURRET, new RenderTrebuchetStandTurret(), new ModelTrebuchetStandTurret());
  this.addVehicleRender(VehicleRegistry.TREBUCHET_MOBILE_FIXED, new RenderTrebuchetMobileFixed(), new ModelTrebuchetMobileFixed());
  this.addVehicleRender(VehicleRegistry.TREBUCHET_LARGE, new RenderTrebuchetLarge(), new ModelTrebuchetStandFixed());
  this.addVehicleRender(VehicleRegistry.CHEST_CART, new RenderChestCart(), new ModelChestCart());
  
  /**
   * missiles...
   */
  RenderingRegistry.registerEntityRenderingHandler(MissileBase.class, new RenderMissileHelper());
  this.addMissileRender(Ammo.ammoStoneShot10.getAmmoType(), new RenderShot());
  this.addMissileRender(Ammo.ammoStoneShot25.getAmmoType(), new RenderShot());
  this.addMissileRender(Ammo.ammoStoneShot50.getAmmoType(), new RenderShot());
  this.addMissileRender(Ammo.ammoFireShot10.getAmmoType(), new RenderShot());
  this.addMissileRender(Ammo.ammoFireShot25.getAmmoType(), new RenderShot());
  this.addMissileRender(Ammo.ammoFireShot50.getAmmoType(), new RenderShot());  
  this.addMissileRender(Ammo.ammoArrow.getAmmoType(), new RenderArrow());    
  this.addMissileRender(Ammo.ammoRocket.getAmmoType(), new RenderArrow());
  
  /**
   * load up the vehicle item renderer...
   */
  MinecraftForgeClient.registerItemRenderer(ItemLoader.vehicleSpawner.itemID, RenderVehicleHelper.instance());
  /**
   * npcs...
   */
  RenderingRegistry.registerEntityRenderingHandler(NpcBase.class, new RenderBiped(new ModelBiped(), 1.0f));
  }

public void addVehicleRender(IVehicleType type, RenderVehicleBase rend, ModelVehicleBase model)
  {
  this.vehicleRenders.put(type.getGlobalVehicleType(), rend);
  this.vehicleModels.put(type.getGlobalVehicleType(), model);
  }
  
public void addMissileRender(int type, Render rend)
  {
  this.missileRenders.put(type, rend); 
  }

public Render getRenderForMissile(int type)
  {
  return this.missileRenders.get(type);
  }

public RenderVehicleBase getRenderForVehicle(int type)
  {
  if(!this.vehicleRenders.containsKey(type))
    {
    return dummyRender;
    }
  return this.vehicleRenders.get(type);
  }

public ModelVehicleBase getVehicleModel(int type)
  {  
  return this.vehicleModels.get(type);
  }


}
