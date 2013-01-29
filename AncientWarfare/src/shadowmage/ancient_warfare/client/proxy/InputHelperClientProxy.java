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
package shadowmage.ancient_warfare.client.proxy;

import shadowmage.ancient_warfare.common.proxy.InputHelperCommonProxy;

/**
 * used to get client movement input, to relay to server, to be applied to vehicle server side
 * @author Shadowmage
 *
 */
public class InputHelperClientProxy extends InputHelperCommonProxy
{


/**
 * input model:
 * get input from client, send to server;
 * server updates internal state, tracks acceleration, motion, position;
 * RELAY input from server to ALL tracking clients (including rider/sender)
 * clients accept pos and motion update packets from server
 * BUT they also calc acceleration locally (hopefully in sych with server) based upon the input packets relayed from inputHelper
 * 
 * 
 */


@Override
public boolean checkInput()
  {
  return false;
  }

}
