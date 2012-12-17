package shadowmage.ancient_warfare.common.aw_core.utils;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

/**
 * used by missiles to communicate hit information to vastly different classes
 * (soldiers and vehicles)
 * soldiers and vehicles use this for stat tracking purposes
 * effectively server-side only.
 * @author Shadowmage
 *
 */
public interface IMissileHitCallback
{

/**
 * callback for when a fired missile impacts a position
 * @param world
 * @param x
 * @param y
 * @param z
 */
public abstract void onMissileImpact(World world, double x, double y, double z);

/**
 * callback for when a fired missile impacts an entity
 * @param world
 * @param entity
 */
public abstract void onMissileImpactEntity(World world, Entity entity);


  

}
