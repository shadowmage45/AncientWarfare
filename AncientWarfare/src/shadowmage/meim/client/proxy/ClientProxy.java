package shadowmage.meim.client.proxy;

import net.minecraft.client.Minecraft;
import shadowmage.ancient_framework.client.model.ModelBaseAW;
import shadowmage.ancient_framework.common.container.ContainerDummy;
import shadowmage.meim.client.gui.GuiMEIM;
import shadowmage.meim.common.proxy.CommonProxy;

public class ClientProxy extends CommonProxy
{

public static ModelBaseAW model;

public void openMEIMGUI()
  {
  Minecraft.getMinecraft().displayGuiScreen(new GuiMEIM(new ContainerDummy(Minecraft.getMinecraft().thePlayer, 0, 0, 0)));
  }

public Object getModel()
  {
  return model;
  }

public void setModel(Object model)
  {
  this.model = (ModelBaseAW)model;
  }

public void exportModel(String fullPathName)
  {
  
  }


}
