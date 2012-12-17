package shadowmage.ancient_warfare.common.aw_core.proxy;

import shadowmage.ancient_warfare.common.aw_core.AWCore;

public class InputHelper
{
/**
 * actually returns the proxied instance of InputHelperCommonProxy
 * @return
 */
public static InputHelperCommonProxy intance()
  {
  return AWCore.proxy.inputHelper;
  }

/**
 * to deny instantiation of this class
 */
private InputHelper(){}

}
