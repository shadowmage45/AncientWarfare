package buildcraft.api.power;

import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.api.power.PowerHandler.PowerReceiver;

public interface IPowerReceptor
{

public PowerReceiver getPowerReceiver(ForgeDirection side);

public void doWork(PowerHandler workProvider);

public World getWorld();

}
