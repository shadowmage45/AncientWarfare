/**
 * Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
 * This software is distributed under the terms of the GNU General Public Licence.
 * Please see COPYING for precise license information.
 */
package shadowmage.ancient_warfare.client.aw_core.proxy;

import shadowmage.ancient_warfare.common.aw_core.proxy.CommonProxy;

public class ClientProxy extends CommonProxy
{

public ClientProxy()
  {
  this.inputHelper = new InputHelperClientProxy();
  }

}
