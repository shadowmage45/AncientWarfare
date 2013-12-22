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
package shadowmage.ancient_structures.common.structures.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import shadowmage.ancient_framework.common.utils.ByteTools;
import shadowmage.ancient_framework.common.utils.StringTools;

/**
 * in-memory template w/ methods to handle breaking data up into packet-sized chunks of bytes
 * @author Shadowmage
 *
 */
public class MemoryTemplate
{
ArrayList<String> templateLines = new ArrayList<String>();


public void setLines(List<String> lines)
  {
  templateLines.clear();
  templateLines.addAll(lines);
  }

public List<String> getLines()
  {
  return this.templateLines;
  }

public List<byte[]> getPacketBytes(int packetSize) throws UnsupportedEncodingException, IOException
  {
  byte [] fullFile = StringTools.getByteArray(templateLines);  
  return ByteTools.getByteChunks(fullFile, packetSize);
  }

}
