package shadowmage.ancient_warfare.common.aw_core.proxy;

import shadowmage.ancient_warfare.common.aw_core.AWCore;

public class InputHelperCommonProxy
{

public static final byte FORWARD = 2;
public static final byte REVERSE = 0;
public static final byte LEFT = 0;
public static final byte RIGHT = 2;
public static final byte NO_INPUT = 1;

/**
 * 0-reverse
 * 1-no input
 * 2-forward
 * @return
 */
public byte getForwardInput()
  {
  return 0;
  }

/**
 * 0-left
 * 1-no input
 * 2-right
 * @return
 */
public byte getStrafeInput()
  {
  return 0;
  }

}
