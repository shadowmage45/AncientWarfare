/**
   Copyright 2012 John Cummens (aka Shadowmage, Shadowmage4513)
   This software is distributed under the terms of the GNU General Public License.
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
package shadowmage.ancient_framework.client.proxy;

import shadowmage.ancient_framework.common.network.PacketBase;
import shadowmage.ancient_framework.common.proxy.CommonProxy;
import cpw.mods.fml.common.network.PacketDispatcher;

public class ClientProxyBase extends CommonProxy
{

public ClientProxyBase()
  {
  
  }

@Override
public void sendPacketToServer(PacketBase pkt)
  {
  PacketDispatcher.sendPacketToServer(pkt.get250Packet());
  }

}
