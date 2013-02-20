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
package shadowmage.ancient_warfare.client.gui;

public interface IGuiElement
{

public int getElementNumber();
public void drawElement(int mouseX, int mouseY, int guiLeft, int guiTop);
public boolean isMouseOver(int x, int y);
public void onMousePressed(int x, int y, int num);
public void onMouseReleased(int x, int y, int num);
public void onMouseMoved(int x, int y, int num);
public void onMouseWheel(int x, int y, int wheel);
public void onKeyTyped(char ch, int keyNum);
public boolean handleMousePressed(int x, int y, int num);
public boolean handleMouseReleased(int x, int y, int num);
public boolean handleMouseMoved(int x, int y, int num);
public boolean handleMouseWheel(int x, int y, int wheel);
public boolean handleKeyInput(char ch, int keyNum);

}
