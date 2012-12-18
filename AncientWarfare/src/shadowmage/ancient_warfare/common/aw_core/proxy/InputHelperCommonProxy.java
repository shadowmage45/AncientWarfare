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