package shadowmage.meim.client.proxy;

import net.minecraft.client.Minecraft;
import shadowmage.ancient_framework.common.container.ContainerDummy;
import shadowmage.meim.client.gui.GuiMEIM;
import shadowmage.meim.client.meim_model.MEIMModelBase;
import shadowmage.meim.common.proxy.CommonProxy;

public class ClientProxy extends CommonProxy
{

public static MEIMModelBase model;

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
  this.model = (MEIMModelBase)model;
  }

public void exportModel(String fullPathName)
  {
  
  }


}
